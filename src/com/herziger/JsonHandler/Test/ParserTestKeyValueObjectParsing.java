package com.herziger.JsonHandler.Test;
import static org.junit.Assert.assertArrayEquals;


import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.herziger.JsonHandler.Parser.Parser;

public class ParserTestKeyValueObjectParsing {
	private static Parser parser;
	private static String json;

	@BeforeClass
	public static void testSetup() throws JsonParseException, IOException {
		json = "{\"schema\": \"http://domain.com/json.schema\",\"data\":{\"key_1\":\"value_1\",\"key_2\": \"value_2\",\"key_3\": \"value_3\",\"key_4\": \"value_4\"}}";
		parser = new Parser(json);
		parser.initJsonObject();
		parser.drillThroughObjectKeys(new String[]{"data"});
	}
	
	@Test
	public void testGet1DimLabelsArray() throws JsonParseException, JsonMappingException, IOException { 
		String[][] keyValuePairs = parser.getKeyValuePairs(); 
		assertArrayEquals(new String[]{"key_3","key_2","key_1","key_4"}, keyValuePairs[0]);
		assertArrayEquals(new String[]{"value_3","value_2","value_1","value_4"}, keyValuePairs[1]);
	}

	@AfterClass
	public static void endTest() { 
		parser = null;
	}
}
