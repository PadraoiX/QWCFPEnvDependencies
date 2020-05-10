package br.com.pix.qware.qwcfp.bailarina.client;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;

import org.hibernate.mapping.Map;
import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.pix.qwvdt.client.DateDeserializer;

public class ResponseString {
	
	private String responseString;
	private String reason;
	private int statusCode;
	private String content;

	public ResponseString(String responseString, String reason, int statusCode, String content) {
		this.responseString = responseString;
		this.reason = reason;
		this.statusCode = statusCode;
		this.content = content;
	}
	
	public ResponseString(String responseString) {
		this.responseString = responseString;
		this.reason = "Sucess!";
		this.statusCode = 200;
		this.content = "";
	}
	
	public JSONArray toJsonArray() {
		if (responseString != null) {
			return new JSONArray(responseString);	
		}
		return new JSONArray();
	}
	
	public <T> T toGson(Class<T> type) {
		 return new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create().fromJson(responseString, type);
	}
	
	public <T> T toGson(Type type) {
		return new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create().fromJson(responseString, type);
	}

	@Override
	public String toString() {
		return responseString;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getContent() {
		return content;
	}
	
	public String getMessage() {
		Gson message = new Gson();
		
		try {
			HashMap<String,String> map = message.fromJson(content, HashMap.class);
			
			if (map  != null && map.containsKey("message")) {
				return map.get("message");
			}else {
				return "";
			}
		} catch (Exception e) {
			return "Não foi possivel interpretar a mensagem de retorno.";
		}
	}

	
}
