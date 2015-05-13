package com.herziger.JsonHandler.Parser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
public class Parser {

	private String jsonString;
	private JsonParser jsonParser;
	private HashMap<String,Object> jsonHashMap;
	private String[] jKeys;
	private boolean hashMapInitiated;
	public String[] getjKeys() {
		return jKeys;
	}

	public void setjKeys(String[] jKeys) {
		this.jKeys = jKeys;
	}

	public String[] getjValues() {
		return jValues;
	}

	public void setjValues(String[] jValues) {
		this.jValues = jValues;
	}

	private String[] jValues;
	
	public String getJsonString() {
		return jsonString;
	}
 
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public HashMap<String,Object> getJsonHashMap() {
		return jsonHashMap;
	}

	public void setJsonHashMap(HashMap<String,Object> jsonHashMap) {
		this.jsonHashMap = jsonHashMap;
		hashMapInitiated = true;
	}
	
	// assign JSON Parser with JSON input string
	public void initJsonObject() throws JsonParseException, IOException { 
		setJsonObject(new JsonFactory().createJsonParser(jsonString));
	}
	public void setJsonObject(JsonParser _jsonParser) {
		jsonParser = _jsonParser;
	}
	// JSON string constructor
	public Parser(String jsonString) {
		super();
		this.jsonString = jsonString;
		hashMapInitiated = false;
	}
	public Parser() { 
		hashMapInitiated = false;
	}



	@SuppressWarnings("unchecked")
	public String[][] getKeyValuePairs() throws JsonParseException, JsonMappingException, IOException {
		if (!hashMapInitiated) 
			setJsonHashMap(new ObjectMapper().readValue(jsonParser , HashMap.class));
		
		@SuppressWarnings("rawtypes")
		Iterator iterator = jsonHashMap.entrySet().iterator();
		String[] objectKeys = new String[jsonHashMap.size()];
		String[] objectValues = new String[jsonHashMap.size()];
		int i = 0;
	    while (iterator.hasNext()) {
	    	@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)iterator.next();

	    	objectKeys[i] = String.valueOf(pair.getKey());
	    	objectValues[i] = String.valueOf(pair.getValue());
	        iterator.remove(); // avoids a ConcurrentModificationException
	        i++;
	    }
	    String[][] objectElements = new String[][]{objectKeys,objectValues};
	    return objectElements;
	}
	
	
	public void drillThroughObjectKeys(String[] _key) throws JsonParseException, IOException { 
		JSONObject currentJsonObject = (JSONObject)JSONValue.parse(jsonString);
		for (int i = 0; i < _key.length; i++) { 
			currentJsonObject = (JSONObject)currentJsonObject.get(_key[i]);
		}
		setJsonObject(new JsonFactory().createJsonParser(currentJsonObject.toJSONString()));
	}
	
	@SuppressWarnings("unchecked")
	public String getValueByKey(String _key) throws JsonParseException, JsonMappingException, IOException { 
		if (!hashMapInitiated) 
			setJsonHashMap(new ObjectMapper().readValue(jsonParser , HashMap.class));
		return String.valueOf(jsonHashMap.get(_key));
	}
	public String[][] get2DimValuesArray(String valuesKeyName) { 
		String values = "";
		try {
			values = getValueByKey(valuesKeyName);
			String valuesArr[] = values.split("\\]\\, \\[");
			ArrayList<String[]> arrayList = new ArrayList<String[]>();
			for (String s: valuesArr) {           
				s = s.replace("[","").replace("]","").replace("\"", "");
				arrayList.add(s.split("\\,"));
			}
			String[][] outputTwoDimArray = new String[arrayList.size()][arrayList.get(0).length];
			float sizeCheck = 0;

			for (String[] s: arrayList) {
				sizeCheck += (float)s.length;
			}
			for (String[] s: arrayList) {
				if ((float)s.length != sizeCheck / arrayList.size())
					return null;				
			}
			int i = 0;
			for (String[] s: arrayList) {
				outputTwoDimArray[i] = s;
				i++;
			}
			return outputTwoDimArray;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] get1DimLabelsArray(String labelsKeyName) {
		String labels = "";
		try {
			labels = getValueByKey(labelsKeyName);
			String labelsArray[] = labels.split("\\,");
			for (int i = 0; i < labelsArray.length; i++) {           
				labelsArray[i] = labelsArray[i].replace("[","").replace("]","").replace("\"","").trim();
			}
			return labelsArray;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; 
	}
}
