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
	private HashMap<String, Object> jsonHashMap;
	private String[] jKeys;
	private String[] jValues;
	private boolean hashMapInitiated;

	// constructor - jsonString:
	public Parser(String jsonString) {
		super();
		this.jsonString = jsonString;
		hashMapInitiated = false;
	}

	// empty constructor:
	public Parser() {
		hashMapInitiated = false;
	}

	// getters, setters:
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

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public HashMap<String, Object> getJsonHashMap() {
		return jsonHashMap;
	}

	public void setJsonHashMap(HashMap<String, Object> jsonHashMap) {
		this.jsonHashMap = jsonHashMap;
		hashMapInitiated = true;
	}

	public void setJsonObject(JsonParser _jsonParser) {
		jsonParser = _jsonParser;
	}

	// initialize JSON object
	public void initJsonObject() throws JsonParseException, IOException {
		setJsonObject(new JsonFactory().createJsonParser(jsonString));
	}

	// returns a 2-D array of keys and values (i.e. [[k1,k2,k3],[v1,v2,v3]]):
	@SuppressWarnings("unchecked")
	public String[][] getKeyValuePairs() throws JsonParseException,
			JsonMappingException, IOException {
		if (!hashMapInitiated)
			setJsonHashMap(new ObjectMapper().readValue(jsonParser,
					HashMap.class));

		@SuppressWarnings("rawtypes")
		Iterator iterator = jsonHashMap.entrySet().iterator();
		String[] objectKeys = new String[jsonHashMap.size()];
		String[] objectValues = new String[jsonHashMap.size()];
		int i = 0;
		while (iterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry) iterator.next();

			objectKeys[i] = String.valueOf(pair.getKey());
			objectValues[i] = String.valueOf(pair.getValue());
			iterator.remove(); // avoids a ConcurrentModificationException
			i++;
		}
		String[][] objectElements = new String[][] { objectKeys, objectValues };
		return objectElements;
	}

	// some JSON arrays require quite some drill down to the relevant data
	// object
	// (e.g. self explanatory JSONs in some web tracking platforms):
	public void drillThroughObjectKeys(String[] _key)
			throws JsonParseException, IOException {
		JSONObject currentJsonObject = (JSONObject) JSONValue.parse(jsonString);
		for (int i = 0; i < _key.length; i++) {
			currentJsonObject = (JSONObject) currentJsonObject.get(_key[i]);
		}
		setJsonObject(new JsonFactory().createJsonParser(currentJsonObject
				.toJSONString()));
	}

	// simple getter for a single value, based on a key:
	@SuppressWarnings("unchecked")
	public String getValueByKey(String _key) throws JsonParseException,
			JsonMappingException, IOException {
		if (!hashMapInitiated)
			setJsonHashMap(new ObjectMapper().readValue(jsonParser,
					HashMap.class));
		return String.valueOf(jsonHashMap.get(_key));
	}

	// returns a 2-d array instead of a HashMap,
	// that is, as some compilers like earler versions of Janino can't handle
	// generics:
	public String[][] get2DimValuesArray(String valuesKeyName) {
		String values = "";
		try {
			values = getValueByKey(valuesKeyName);
			String valuesArr[] = values.split("\\]\\, \\[");
			ArrayList<String[]> arrayList = new ArrayList<String[]>();
			for (String s : valuesArr) {
				s = s.replace("[", "").replace("]", "").replace("\"", "");
				arrayList.add(s.split("\\,"));
			}
			String[][] outputTwoDimArray = new String[arrayList.size()][arrayList
					.get(0).length];
			float sizeCheck = 0;

			for (String[] s : arrayList) {
				sizeCheck += (float) s.length;
			}
			for (String[] s : arrayList) {
				if ((float) s.length != sizeCheck / arrayList.size())
					return null;
			}
			int i = 0;
			for (String[] s : arrayList) {
				outputTwoDimArray[i] = s;
				i++;
			}
			return outputTwoDimArray;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// returns a 1-d array based on a key name:
	public String[] get1DimLabelsArray(String labelsKeyName) {
		String labels = "";
		try {
			labels = getValueByKey(labelsKeyName);
			String labelsArray[] = labels.split("\\,");
			for (int i = 0; i < labelsArray.length; i++) {
				labelsArray[i] = labelsArray[i].replace("[", "")
						.replace("]", "").replace("\"", "").trim();
			}
			return labelsArray;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
