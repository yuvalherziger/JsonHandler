package com.herziger.JsonHandler.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.herziger.JsonHandler.Parser.Parser;

public class ParserTestSimpleKeyValue {
	private static Parser parser;
	private static String json;

	@BeforeClass
	public static void testSetup() throws JsonParseException, IOException {
		json = "{\"schema\":\"http://domain.com/json.schema\",\"data\":{\"eventType\":\"product_viewed\",\"productId\":\"ab1234f009fc12\",\"productName\":\"Samsung Galaxy S5 LTE+ 16GB 4G White\",\"isDefault\":false,\"custom_param\":\"false\"}}";
		parser = new Parser(json);
		parser.initJsonObject();
		parser.drillThroughObjectKeys(new String[] { "data" });
	}

	@Test
	public void testGetString1ValueByKey() throws JsonParseException,
			JsonMappingException, IOException {
		assertEquals("product_viewed", parser.getValueByKey("eventType"));
	}

	@Test
	public void testGetString2ValueByKey() throws JsonParseException,
			JsonMappingException, IOException {
		assertEquals("ab1234f009fc12", parser.getValueByKey("productId"));
	}

	@Test
	public void testGetString3ValueByKey() throws JsonParseException,
			JsonMappingException, IOException {
		assertEquals("Samsung Galaxy S5 LTE+ 16GB 4G White",
				parser.getValueByKey("productName"));
	}

	@Test
	public void testGetBoolean1ValueByKey() throws JsonParseException,
			JsonMappingException, IOException {
		assertEquals("false", parser.getValueByKey("custom_param"));
	}

	@Test
	public void testGetBoolean2ValueByKey() throws JsonParseException,
			JsonMappingException, IOException {
		assertEquals("false", parser.getValueByKey("isDefault"));
	}

	@AfterClass
	public static void endTest() {
		parser = null;
	}
}
