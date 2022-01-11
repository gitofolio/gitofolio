package com.gitofolio.api.service.common.secure;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;

@Service
public class XssProtector{
	
	private final Map<String, String> xssWords;
	
	public String convertXssSafeString(String string){
		String ans = string;
		for(Map.Entry<String, String> entry : this.xssWords.entrySet()) ans = ans.replace(entry.getKey(), entry.getValue());
		return ans;
	}
	
	public XssProtector(){
		xssWords = new HashMap<String, String>();
		xssWords.put("#", "&#35;");
		xssWords.put("<", "&lt;");
		xssWords.put(">", "&gt;");
		xssWords.put("\"", "&#34;");
		xssWords.put("\\(", "&#40;");
		xssWords.put("\\)", "&#41;");
		xssWords.put("'", "&#39;");
		xssWords.put("`", "&#96;");
		xssWords.put("/", "&#47;");
	}
	
}