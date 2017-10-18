package passwordprotector;

import java.util.HashMap;

public class JSONObject {
	private HashMap<String,byte[]> objectData;
	
	public JSONObject(String jsonString) throws InvalidJSONException {
		jsonString = stripWhiteSpaceNotInQuotes(jsonString);
		if (jsonString.startsWith("{") || jsonString.endsWith("}")) {
			throw new InvalidJSONException("Malformed JSON Object: Missing Starting or Ending Brace");
		}
		jsonString = jsonString.substring(1, jsonString.length()-2);
		for (int i = 0; i < jsonString.length(); i++) {

		}
	}
	
	private String stripWhiteSpaceNotInQuotes(String s) {
		return ""; //TODO Implement
	}
	
	public String toString() {
		return ""; //TODO Implement
	}
	
	public String getValueOfTypeString(String key) {
		return ""; //TODO Implement
	}
	
	public int getValueOfTypeInt(String key) {
		return 0; //TODO Implement
	}
	
	public float getValueOfTypeFloat(String key) {
		return 0; //TODO Implement
	}
	
	public boolean getValueOfTypeBoolean(String key) {
		return false; //TODO Implement
	}
	
	public JSONObject getValueOfTypeObject(String key) throws InvalidJSONException {
		return new JSONObject(""); //TODO Implement
	}
	
	public String getValueFromArrayOfTypeString(String key, int index) {
		return ""; //TODO implement
	}
	
	public int getValueFromArrayOfTypeInt(String key, int index) {
		return 0; //TODO implement
	}
	
	public float getValueOfTypeFloat(String key, int index) {
		return 0; //TODO implement
	}
	
	public boolean getValueOfTypeBoolean(String key, int index) {
		return false; //TODO implement
	}
	
	public JSONObject getValueOfTypeObject(String key, int index) throws InvalidJSONException {
		return new JSONObject(""); //TODO Implement
	}
	
	public JSONDataType getDataType(String key) {
		return JSONDataType.NULL; //TODO implement
	}
	
	public JSONDataType getDataType(String key, int index) {
		return JSONDataType.NULL; //TODO implement
	}
	
	private int parseJSONObject(String s) throws InvalidJSONException {
		if (!s.startsWith("{") || !s.endsWith("}")) {
			throw new InvalidJSONException("Malformed JSON Object: Missing Starting or Ending Brace");
		}
		s = s.substring(1, s.length()-2);
		int i;
		for (i = 0; i < s.length(); i++) {
			
		}
		return i;
	}
	
	private int parseValue(String s) {
		int i;
		for (i = 0; i < s.length(); i++) {
			
		}
		return i;
	}
	
	private int parseString(String s) throws InvalidJSONException {
		if (!s.startsWith("\"") || !s.endsWith("\"")) {
			throw new InvalidJSONException("Malformed JSON Object: Missing Quotation Mark");
		}
		int i;
		for (i = 0; i < s.length(); i++) {
			
		}
		return i;
	}
	
	private int parseNumber(String s) {
		int i;
		for (i = 0; i < s.length(); i++) {
			
		}
		return i;
	}
	
	private int parseArray(String s) throws InvalidJSONException {
		if (!s.startsWith("[") || !s.endsWith("]")) {
			throw new InvalidJSONException("Malformed JSON Object: Array Missing [ or ]");
		}
		int i;
		for (i = 0; i < s.length(); i++) {
			
		}
		return i;
	}
	
}
