package com.gitofolio.api.aop.log.datacollector;

import org.springframework.context.*;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.*;

import com.gitofolio.api.service.common.*;
	
import org.slf4j.*;

public class RequestData{
	
	private final String path;
	private final Object[] parameters;
	private final boolean success;
	private final long excutionTime;
	private final String exceptionName;
	private final List<StackTrace> stackTraces;
	private final ObjectMapper objectMapper;
	
	protected RequestData(Builder builder){
		this.path = builder.path;
		this.parameters = builder.parameters;
		this.success = builder.success;
		this.excutionTime = builder.excutionTime;
		this.exceptionName = builder.exceptionName;
		this.stackTraces = builder.stackTraces;
		this.objectMapper = builder.objectMapper;
	}
	
	public void doLog(Logger logger){
		if(this.success) this.logInfo(logger, "\n{}\n", this.toString());
		else this.logWarn(logger, "\n{}\n", this.toString());
	}
	
	private void logInfo(Logger logger, String slot, String data){
		logger.info(slot, data);
	}
	
	private void logWarn(Logger logger, String slot, String data){
		logger.warn(slot, data);
	}
	
	@Override
	public String toString(){
		try{
			return this.objectMapper.writeValueAsString(this);
		}
		catch(JsonProcessingException JPE){
			StringBuilder sb  = new StringBuilder();
			sb.append("path=").append(path).append(" success=").append(success).append(" RequestData JSON 파싱 오류 발생 ").append(JPE.getStackTrace());
			return sb.toString();
		}
	}
	
	public String getPath(){
		return this.path;
	}
	
	public Object[] getParameters(){
		return this.parameters;
	}
	
	public boolean getSuccess(){
		return this.success;
	}
	
	public long getExcutionTime(){
		return this.excutionTime;
	}
	
	public String getExceptionName(){
		return this.exceptionName;
	}
	
	public List<StackTrace> getStackTraces(){
		return this.stackTraces;
	}
	
	public static class Builder{
		
		private String path;
		private Object[] parameters;
		private boolean success;
		private long excutionTime;
		private String exceptionName;
		private List<StackTrace> stackTraces;
		private ObjectMapper objectMapper;
		
		public Builder(){
			this.objectMapper = ApplicationContextProvider.getApplicationContext().getBean("prettyObjectMapper", ObjectMapper.class);
		}
		
		public Builder path(String path){
			this.path = path;
			return this;
		}
		
		public Builder parameters(Object[] parameters){
			this.parameters = parameters;
			return this;
		}
		
		public Builder excutionTime(long excutionTime){
			this.excutionTime = excutionTime;
			return this;
		}
		
		public Builder exception(Exception e){
			this.success = isSuccess(e);
			return this;
		}
		
		private boolean isSuccess(Exception e){
			if(e == null) return true;
			
			this.setExceptionName(e);
			if(e instanceof UnCaughtException) return true;
			
			this.initStackTraces(e.	getStackTrace());
			return false;
		}
		
		private void setExceptionName(Exception e){
			this.exceptionName = e.getClass().getSimpleName();
		}
		
		private void initStackTraces(StackTraceElement[] stackTraceElements){
			this.stackTraces = new ArrayList();
			int cnt = 0;
			for(StackTraceElement stackTraceElement : stackTraceElements){
				if(!isOurClasspath(stackTraceElement.getClassName())) continue;
				if(cnt > 2) break;
				this.stackTraces.add(new StackTrace(stackTraceElement));
				cnt++;
			}
		}
		
		private boolean isOurClasspath(String classpath){
			if(classpath.charAt(4) == 'g' && classpath.charAt(5) == 'i' && classpath.charAt(6) == 't') return true;
			return false;
		}
		
		public RequestData build(){
			return new RequestData(this);
		}
		
	}
	
	private static class StackTrace{
		
		private final String className;
		private final String methodName;
		private final int lineNumber;
		
		public StackTrace(StackTraceElement stackTraceElement){
			this.className = stackTraceElement.getClassName();
			this.methodName = stackTraceElement.getMethodName();
			this.lineNumber = stackTraceElement.getLineNumber();
		}
		
		public String getClassName(){
			return this.className;
		}
		
		public String getMethodName(){
			return this.methodName;
		}
		
		public int getLineNumber(){
			return this.lineNumber;
		}
		
	}
	
}