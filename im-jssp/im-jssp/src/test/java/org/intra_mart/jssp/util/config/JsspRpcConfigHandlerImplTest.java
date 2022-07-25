package org.intra_mart.jssp.util.config;

import java.util.Locale;

import junit.framework.TestCase;

/**
 * 「/org/intra_mart/jssp/util/config/jssp-rpc-test-config.xml」には、
 * JsspRpcConfigHandlerで取得可能な設定項目がすべて設定されています。
 * 本テストケースでは、上記ファイルの設定値を利用してテストを行います。
 * <br/>
 * <br/>
 * 「/org/intra_mart/jssp/util/config/jssp-rpc-test-empty-config.xml」は、
 * 必要最低限の設定がされているコンフィグファイルです。
 * このファイルはデフォルト値の確認テストに利用します。
 */
public class JsspRpcConfigHandlerImplTest extends TestCase {

	private String testConfigFileName = "/org/intra_mart/jssp/util/config/jssp-rpc-test-config.xml";
	private String emptyTestConfigFileName = "/org/intra_mart/jssp/util/config/jssp-rpc-test-empty-config.xml";

	public void testGetJsspRpcURISuffix_デフォルト値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(emptyTestConfigFileName);
		assertEquals(".jssprpc", config.getJsspRpcURISuffix());
	}

	public void testGetJsspRpcURISuffix_設定値値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(testConfigFileName);
		assertEquals(".TEST_jssprpc", config.getJsspRpcURISuffix());
	}
	
	
	public void testGetMarshallerPagePath_デフォルト値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(emptyTestConfigFileName);
		assertEquals("jssp/script/api/im_json", config.getMarshallerPagePath());
	}

	public void testGetMarshallerPagePath_設定値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(testConfigFileName);
		assertEquals("TEST_jssp_rpc/common/im_json", config.getMarshallerPagePath());
	}

	
	public void testGetUnmarshallFunctionName_デフォルト値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(emptyTestConfigFileName);
		assertEquals("imJsonUnmarshall", config.getUnmarshallFunctionName());
	}

	public void testGetUnmarshallFunctionName_設定値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(testConfigFileName);
		assertEquals("TEST_imJsonUnmarshall", config.getUnmarshallFunctionName());
	}
	

	public void testGetMarshallFunctionName_デフォルト値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(emptyTestConfigFileName);
		assertEquals("imJsonMarshall", config.getMarshallFunctionName());
	}

	public void testGetMarshallFunctionName_設定値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(testConfigFileName);
		assertEquals("TEST_imJsonMarshall", config.getMarshallFunctionName());
	}

	public void testIsThrowUnmarshallException_デフォルト値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(emptyTestConfigFileName);
		assertEquals(true, config.isThrowUnmarshallException());
	}

	public void testIsThrowUnmarshallException_設定値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(testConfigFileName);
		assertEquals(false, config.isThrowUnmarshallException());
	}


	public void testIsCacheMarshallerScriptScope_デフォルト値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(emptyTestConfigFileName);
		assertEquals(true, config.isCacheMarshallerScriptScope());
	}

	public void testIsCacheMarshallerScriptScope_設定値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(testConfigFileName);
		assertEquals(false, config.isCacheMarshallerScriptScope());
	}


	public void testGetCsjsPath4ImJson_デフォルト値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(emptyTestConfigFileName);
		assertEquals("/csjs/im_json.js", config.getCsjsPath4ImJson());
	}

	public void testGetCsjsPath4ImJson_設定値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(testConfigFileName);
		assertEquals("/TEST_csjs/im_json.js", config.getCsjsPath4ImJson());
	}


	public void testGetCsjsPath4ImAjaxRequest_デフォルト値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(emptyTestConfigFileName);
		assertEquals("/csjs/im_ajax_request.js", config.getCsjsPath4ImAjaxRequest());
	}

	public void testGetCsjsPath4ImAjaxRequest_設定値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(testConfigFileName);
		assertEquals("/TEST_csjs/im_ajax_request.js", config.getCsjsPath4ImAjaxRequest());
	}


	public void testGetCsjsPath4ImJsspRpc_デフォルト値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(emptyTestConfigFileName);
		assertEquals("/csjs/im_jssp_rpc.js", config.getCsjsPath4ImJsspRpc());
	}

	public void testGetCsjsPath4ImJsspRpc_設定値() throws Exception {
		JsspRpcConfigHandlerImpl config = new JsspRpcConfigHandlerImpl(testConfigFileName);
		assertEquals("/TEST_csjs/im_jssp_rpc.js", config.getCsjsPath4ImJsspRpc());
	}

}
