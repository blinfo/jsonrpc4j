package com.googlecode.jsonrpc4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonRpcClientTest {

	private ByteArrayOutputStream baos;
	private JsonRpcClient client;

	@Before
	public void setUp() {
		client = new JsonRpcClient();
		baos = new ByteArrayOutputStream();
	}

	@After
	public void tearDown() {
		client = null;
	}

	private JsonNode readJSON(ByteArrayOutputStream baos)
		throws JsonProcessingException,
		IOException {
		return client.getObjectMapper().readTree(baos.toString());
	}

	@Test
	public void testInvokeNoParams()
		throws Throwable {
		
		client.invoke("test", new Object[0], baos);
		JsonNode node = readJSON(baos);
		assertFalse(node.has("data"));

		client.invoke("test", (Object[])null, baos);
		node = readJSON(baos);
		assertFalse(node.has("data"));
	}

	@Test
	public void testInvokeArrayParams()
		throws Throwable {
		client.invoke("test", new Object[] { 1, 2 }, baos);
		JsonNode node = readJSON(baos);

		assertTrue(node.has("data"));
		assertTrue(node.get("data").isArray());
		assertEquals(1, node.get("data").get(0).intValue());
		assertEquals(2, node.get("data").get(1).intValue());
	}

	@Test
	public void testInvokeHashParams()
			throws Throwable {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("hello", "Guvna");
		params.put("x", 1);
		client.invoke("test", params, baos);
		JsonNode node = readJSON(baos);

		assertTrue(node.has("data"));
		assertTrue(node.get("data").isObject());
		assertEquals("Guvna", node.get("data").get("hello").textValue());
		assertEquals(1, node.get("data").get("x").intValue());
	}

//	@Test
//	public void testInvokeCustomHeader()
//			throws Throwable {
//		Map<String, Object> header = new HashMap<String, Object>();
//		header.put("auth", "s3cr3td1E6e5t");
//		client.setAdditionalJsonHeaders(header);
//		client.invoke("test", new Object[0], baos);
//		JsonNode node = readJSON(baos);
//
//		assertTrue(node.has("auth"));
//		assertEquals("s3cr3td1E6e5t", node.get("auth").textValue());
//	}

}
