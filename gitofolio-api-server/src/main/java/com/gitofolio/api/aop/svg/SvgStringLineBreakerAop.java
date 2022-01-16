package com.gitofolio.api.aop.svg;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;

import com.gitofolio.api.aop.AnnotationExtractor;
import com.gitofolio.api.aop.svg.annotation.SvgStringLineBreaker;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.common.secure.XssProtector;

import java.lang.reflect.*;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.ArrayList;

@Aspect
@Component
public class SvgStringLineBreakerAop{
	
	/*
		계산식
		font-size : 13px
		font : Noto Sans KR
		아스키 코드에 없는 모든 값들은 (10+2)로 통일 - 예시로 한국어는 모두 가로 12픽셀 안에 들어옴
		아스키 배열은 32번째 부터 126까지 정의되어있음
	*/
	
	private final int[] ascii;
	private final int noAscii = 12;
	private final XssProtector xssProtector;
	private final AnnotationExtractor<SvgStringLineBreaker> annotationExtractor;
	
	@Around("@annotation(com.gitofolio.api.aop.svg.annotation.SvgStringLineBreaker)")
	public Object lineBreak(ProceedingJoinPoint joinPoint) throws Throwable{
		
		SvgStringLineBreaker svgStringLineBreaker = this.annotationExtractor.extractAnnotation(joinPoint, SvgStringLineBreaker.class);
		
		int width = svgStringLineBreaker.width();
		int idx = svgStringLineBreaker.idx();
		
		Object[] args = joinPoint.getArgs();
		String svgString = parseStringToSvgString(args[idx]);
		
		if(isEmptyString(svgString)) return joinPoint.proceed(args);
		svgString = breakLine(width, splitWords(width, svgString));
		
		if(args[idx].getClass().equals(String.class)) args[idx] = svgString;
		else{
			((SvgBreakAble)args[idx]).setBreakedString(svgString);
		}
		
		return joinPoint.proceed(args);
	}
	
	private String breakLine(int width, List<String> words){
		StringBuilder ans = new StringBuilder();
		StringBuilder line = new StringBuilder();
		int lineCnt = 0;
		for(String word : words){
			if(isTooLongWord(width, line.toString()+word)){
				ans.append(openTextTag(lineCnt))
					.append(xssProtector.convertXssSafeString(line.toString()))
					.append(closeTextTag());
				line.setLength(0);
				lineCnt++;
				line.append(word).append(" ");
				continue;
			}
			if(word.contains("\n")){
				ans.append(openTextTag(lineCnt))
					.append(xssProtector.convertXssSafeString(line.append(word).toString()))
					.append(closeTextTag());
				line.setLength(0);
				lineCnt++;
				continue;
			}
			line.append(word).append(" ");
		}
		ans.append(openTextTag(lineCnt))
			.append(xssProtector.convertXssSafeString(line.toString()))
			.append(closeTextTag());
		return ans.toString();
	}
	
	/*
		문자열을 단어, 개행문자기준으로 자름 
		이때, 하나의 문자열이 width값을 넘어간다면 강제로 자름
	*/
	private List<String> splitWords(int width, String word){
		String[] words = word.split(" ");
		List<String> ans = new ArrayList<String>();
		for(String string : words){
			if(isTooLongWord(width, string)){
				List<String> splitedWords = splitTooLongWord(width, string);
				for(String splitedWord : splitedWords) ans.add(splitedWord);
			}
			else ans.add(string);
		}
		return ans;
	}
	
	/*
		하나의 문자열이 너무 길어서 한 줄을 초과할시
		강제로 개행문자 처리한다.
	*/
	private List<String> splitTooLongWord(int width, String string){
		char[] word = string.toCharArray();
		List<String> ans = new ArrayList<String>();
		StringBuilder splited = new StringBuilder();
		int length = 0;
		for(char character : word){
			if(isAscii(character)) length += ascii[(int)character];
			else length += noAscii;
			if(length > width){
				ans.add(splited.toString());
				if(isAscii(character)) length = ascii[(int)character];
				else length = noAscii;
				splited.setLength(0);
			}
			splited.append(character);
		}
		ans.add(splited.toString());
		return ans;
	}
	
	private String parseStringToSvgString(Object target){
		String string = "";
		try{
			string = parseStringToSvgStringRealTask(target);
		}catch(Exception e){
			throw new IllegalArgumentException("SVGString으로 파싱 불가능한 타입입니다.");
		}
		return string;
	}
	
	private String parseStringToSvgStringRealTask(Object target) throws Exception{
		String string = "";
		if(target.getClass().equals(String.class)) string = (String)target;
		else{
			SvgBreakAble svgBreakAble = (SvgBreakAble)target;
			string = svgBreakAble.breakTarget();
		}
		return string;
	}
	
	private boolean isEmptyString(String string){
		return (string.equals("") || string.equals(" ")) ? true : false;
	}
	
	private boolean isTooLongWord(int width, String string){
		char[] word = string.toCharArray();
		int length = 0;
		for(char character : word){
			if(isAscii(character)) length += ascii[(int)character];
			else length += noAscii;
		}
		return (width < length) ? true : false;
	}
	
	private boolean isAscii(char character){
		return ((int)character > 31 && (int)character < 127) ? true : false;
	}
	
	private String openTextTag(int y){  
		return "<text y=\"" + y*16 + "\">";
	}
	
	private String closeTextTag(){
		return "</text>";
	}
	
	@Autowired
	public SvgStringLineBreakerAop(XssProtector xssProtector,
								  @Qualifier("annotationExtractor") AnnotationExtractor<SvgStringLineBreaker> annotationExtractor){
		this.annotationExtractor = annotationExtractor;
		this.xssProtector = xssProtector;
		ascii = new int[130];
		ascii[32] = 3;
		ascii[(int)'!'] = 4;
		ascii[(int)('\'')] = 4;
		ascii[(int)'#'] = 7;
		ascii[(int)'$'] = 8;
		ascii[(int)'%'] = 12;
		ascii[(int)'&'] = 9;
		ascii[(int)'"'] = 7;
		ascii[(int)'('] = ascii[(int)')'] = 4;
		ascii[(int)'*'] = 6;
		ascii[(int)'+'] = 7;
		ascii[(int)','] = 4;
		ascii[(int)'-'] = 4;
		ascii[(int)'.'] = 4;
		ascii[(int)'/'] = 5;
		ascii[(int)'0'] = 7;
		ascii[(int)'1'] = 7;
		ascii[(int)'2'] = 7;
		ascii[(int)'3'] = 7;
		ascii[(int)'4'] = 8;
		ascii[(int)'5'] = 7;
		ascii[(int)'6'] = 7;
		ascii[(int)'7'] = 7;
		ascii[(int)'8'] = 7;
		ascii[(int)'9'] = 7;
		ascii[(int)':'] = ascii[(int)';'] = 4;
		ascii[(int)'<'] = ascii[(int)'>'] = 7;
		ascii[(int)'='] = 7;
		ascii[(int)'?'] = 6;
		ascii[(int)'@'] = 13;
		ascii[(int)'A'] = 8;
		ascii[(int)'B'] = 9;
		ascii[(int)'C'] = 9;
		ascii[(int)'D'] = 9;
		ascii[(int)'E'] = 8;
		ascii[(int)'F'] = 7;
		ascii[(int)'G'] = 9;
		ascii[(int)'H'] = 9;
		ascii[(int)'I'] = 3;
		ascii[(int)'J'] = 7;
		ascii[(int)'K'] = 8;
		ascii[(int)'L'] = 7;
		ascii[(int)'M'] = 11;
		ascii[(int)'N'] = 9;
		ascii[(int)'O'] = 9;
		ascii[(int)'P'] = 8;
		ascii[(int)'Q'] = 10;
		ascii[(int)'R'] = 7;
		ascii[(int)'S'] = 7;
		ascii[(int)'T'] = 8;
		ascii[(int)'U'] = 9;
		ascii[(int)'V'] = 7;
		ascii[(int)'W'] = 12;
		ascii[(int)'X'] = 8;
		ascii[(int)'Y'] = 7;
		ascii[(int)'Z'] = 7;
		ascii[(int)'['] = ascii[']'] = 4;
		ascii[(int)'\\'] = 5;
		ascii[(int)'^'] = 8;
		ascii[(int)'_'] = 7;
		ascii[(int)'`'] = 8;
		ascii[(int)'a'] = 7;
		ascii[(int)'b'] = 8;
		ascii[(int)'c'] = 6;
		ascii[(int)'d'] = 8;
		ascii[(int)'e'] = 7;
		ascii[(int)'f'] = 4;
		ascii[(int)'g'] = 8;
		ascii[(int)'h'] = 8;
		ascii[(int)'i'] = 3;
		ascii[(int)'j'] = 4;
		ascii[(int)'k'] = 7;
		ascii[(int)'l'] = 4;
		ascii[(int)'m'] = 12;
		ascii[(int)'n'] = 8;
		ascii[(int)'o'] = 8;
		ascii[(int)'p'] = 8;
		ascii[(int)'q'] = 8;
		ascii[(int)'r'] = 6;
		ascii[(int)'s'] = 6;
		ascii[(int)'t'] = 4;
		ascii[(int)'u'] = 8;
		ascii[(int)'v'] = 6;
		ascii[(int)'w'] = 11;
		ascii[(int)'x'] = 6;
		ascii[(int)'y'] = 6;
		ascii[(int)'z'] = 6;
		ascii[(int)'{'] = ascii[(int)'}'] = 5;
		ascii[(int)'|'] = 3;
		ascii[(int)'~'] = 7;
	}
	
}