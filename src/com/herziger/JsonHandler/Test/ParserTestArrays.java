package com.herziger.JsonHandler.Test;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.herziger.JsonHandler.Parser;

public class ParserTestArrays {
	private static Parser parser;
	private static String json;

	@BeforeClass
	public static void testSetup() throws JsonParseException, IOException {
		json = "{\"schema\":\"http://domain.com/json.schema\",\"data\":{\"entityLabels\":[\"label_X1\",\"label_X2\",\"label_X3\"],\"entityValues\":[[\"X1Y1\",\"X2Y1\",\"X3Y1\"],[\"X1Y2\",\"X2Y2\",\"X3Y2\"],[\"X1Y3\",\"X2Y3\",\"X3Y3\"]]}}";
		parser = new Parser(json);
		parser.initJsonObject();
		parser.drillThroughObjectKeys(new String[]{"data"});
	}
	
	@Test
	public void testGet1DimLabelsArray() throws JsonParseException, JsonMappingException, IOException { 
		assertArrayEquals(new String[]{"label_X1","label_X2","label_X3"}, parser.get1DimLabelsArray("entityLabels"));
	}
	
	@Test
	public void testGet2DimValuesArray() throws JsonParseException, JsonMappingException, IOException {
		String[][] values = parser.get2DimValuesArray("entityValues"); 
		System.out.println(values[0].length);
		assertArrayEquals(new String[]{"X1Y1"," X2Y1"," X3Y1"}, values[0]);
		assertArrayEquals(new String[]{"X1Y2"," X2Y2"," X3Y2"}, values[1]);
		assertArrayEquals(new String[]{"X1Y3"," X2Y3"," X3Y3"}, values[2]);
	}

	@AfterClass
	public static void endTest() { 
		parser = null;
	}
}
