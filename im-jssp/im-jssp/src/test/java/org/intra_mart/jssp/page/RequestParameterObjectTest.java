package org.intra_mart.jssp.page;

import junit.framework.TestCase;

public class RequestParameterObjectTest extends TestCase {
	
	public void testGetFileNameFromContentDispositionField_引数がnull() {
		String EXPECTED_FILE_NAME = null;
		String input = null;
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}

	/**
	 * //------------------------
	 * *** ダブルクウォート有り（ファイル名：「予定;表.xls」）
	 * //------------------------
	 * - filename="C:\\Documents and Settings\\user\\デスクトップ\\予定;表.xls"
	 * - filename="C:\\Documents and Settings\\user\\デスクトップ\\予定;表.xls"; testProp="testValue"
	 * 
	 * - filename="C:/Documents and Settings/user/デスクトップ/予定;表.xls"
	 * - filename="C:/Documents and Settings/user/デスクトップ/予定;表.xls"; testProp="testValue"
	 * 
	 * - filename="予定;表.xls"
	 * - filename="予定;表.xls"; testProp="testValue"
	 */
	public void testGetFileNameFromContentDispositionField_ダブルクウォート有り0() {
		String EXPECTED_FILE_NAME = "予定;表.xls";
		String input = "form-data; name=\"ctrlName\"; filename=\"C:\\Documents and Settings\\user\\デスクトップ\\予定;表.xls\"";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート有り1() {
		String EXPECTED_FILE_NAME = "予定;表.xls";
		String input = "form-data; name=\"ctrlName\"; filename=\"C:\\Documents and Settings\\user\\デスクトップ\\予定;表.xls\"; testProp=\"testValue\"";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート有り2() {
		String EXPECTED_FILE_NAME = "予定;表.xls";
		String input = "form-data; name=\"ctrlName\"; filename=\"C:/Documents and Settings/user/デスクトップ/予定;表.xls\"";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート有り3() {
		String EXPECTED_FILE_NAME = "予定;表.xls";
		String input = "form-data; name=\"ctrlName\"; filename=\"C:/Documents and Settings/user/デスクトップ/予定;表.xls\"; testProp=\"testValue\"";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート有り4() {
		String EXPECTED_FILE_NAME = "予定;表.xls";
		String input = "form-data; name=\"ctrlName\"; filename=\"予定;表.xls\"";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート有り5() {
		String EXPECTED_FILE_NAME = "予定;表.xls";
		String input = "form-data; name=\"ctrlName\"; filename=\"予定;表.xls\"; testProp=\"testValue\"";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);

	}
	
	/**
	 * //------------------------
	 * *** ダブルクウォート無し（ファイル名：「予定表.xls」）
	 * //------------------------
	 * - filename=C:\\Documents and Settings\\user\\デスクトップ\\予定表.xls
	 * - filename=C:\\Documents and Settings\\user\\デスクトップ\\予定表.xls; testProp=testValue
	 * 
	 * - filename=C:/Documents and Settings/user/デスクトップ/予定表.xls
	 * - filename=C:/Documents and Settings/user/デスクトップ/予定表.xls; testProp=testValue
	 * 
	 * - filename=予定表.xls
	 * - filename=予定表.xls; testProp=testValue
	 */
	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し0() {
		String EXPECTED_FILE_NAME = "予定表.xls";
		String input = "form-data; name=ctrlName; filename=C:\\Documents and Settings\\user\\デスクトップ\\予定表.xls";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}
	
	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し1() {
		String EXPECTED_FILE_NAME = "予定表.xls";
		String input = "form-data; name=ctrlName; filename=C:\\Documents and Settings\\user\\デスクトップ\\予定表.xls; testProp=testValue";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}
	
	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し2() {
		String EXPECTED_FILE_NAME = "予定表.xls";
		String input = "form-data; name=ctrlName; filename=C:/Documents and Settings/user/デスクトップ/予定表.xls";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}
	
	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し3() {
		String EXPECTED_FILE_NAME = "予定表.xls";
		String input = "form-data; name=ctrlName; filename=C:/Documents and Settings/user/デスクトップ/予定表.xls; testProp=testValue";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}
	
	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し4() {
		String EXPECTED_FILE_NAME = "予定表.xls";
		String input = "form-data; name=ctrlName; filename=予定表.xls";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}
	
	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し5() {
		String EXPECTED_FILE_NAME = "予定表.xls";
		String input = "form-data; name=ctrlName; filename=予定表.xls; testProp=testValue";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}

	/**
	 * //------------------------
	 * *** ダブルクウォート無しで予期しないパターン（「;」で区切ることが出来ない → ファイル名：「予定」）
	 * //------------------------
	 * - filename=C:\\Documents and Settings\\user\\デスクトップ\\予定;表.xls
	 * - filename=C:\\Documents and Settings\\user\\デスクトップ\\予定;表.xls; testProp=testValue
	 * 
	 * - filename=C:/Documents and Settings/user/デスクトップ/予定;表.xls
	 * - filename=C:/Documents and Settings/user/デスクトップ/予定;表.xls; testProp=testValue
	 * 
	 * - filename=予定;表.xls
	 * - filename=予定;表.xls; testProp=testValue
	 */
	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し_予期しないパターン0() {
		String EXPECTED_FILE_NAME = "予定";
		String input = "form-data; name=ctrlName; filename=C:\\Documents and Settings\\user\\デスクトップ\\予定;表.xls";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し_予期しないパターン1() {
		String EXPECTED_FILE_NAME = "予定";
		String input = "form-data; name=ctrlName; filename=C:\\Documents and Settings\\user\\デスクトップ\\予定;表.xls; testProp=testValue";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);

	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し_予期しないパターン2() {
		String EXPECTED_FILE_NAME = "予定";
		String input = "form-data; name=ctrlName; filename=C:/Documents and Settings/user/デスクトップ/予定;表.xls";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);

	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し_予期しないパターン3() {
		String EXPECTED_FILE_NAME = "予定";
		String input = "form-data; name=ctrlName; filename=C:/Documents and Settings/user/デスクトップ/予定;表.xls; testProp=testValue";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);

	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し_予期しないパターン4() {
		String EXPECTED_FILE_NAME = "予定";
		String input = "form-data; name=ctrlName; filename=予定;表.xls";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);

	}

	public void testGetFileNameFromContentDispositionField_ダブルクウォート無し_予期しないパターン5() {
		String EXPECTED_FILE_NAME = "予定";
		String input = "form-data; name=ctrlName; filename=予定;表.xls; testProp=testValue";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}


	/**
	 * //------------------------
	 * *** filenameパラメータが含まれていない（ファイル名：「null」）
	 * //------------------------
	 */
	public void testGetFileNameFromContentDispositionField_filenameパラメータ無し() {
		String EXPECTED_FILE_NAME = null;	
		String input = "form-data; name=\"ctrlName\"";
		String output = RequestParameterObject.getFileNameFromContentDispositionField(input);
		assertEquals(EXPECTED_FILE_NAME, output);
	}
	

}
