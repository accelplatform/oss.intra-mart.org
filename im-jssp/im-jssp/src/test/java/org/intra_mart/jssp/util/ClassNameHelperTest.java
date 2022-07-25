package org.intra_mart.jssp.util;

import junit.framework.TestCase;

public class ClassNameHelperTest extends TestCase {


	public void testToFilePath_() throws Exception {
		// TODO 未テスト
	}
	

	public void testToClassNameString_ファイルパス指定_Win() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge\\foo\\bar\\windows.js");
		assertEquals("_hoge._foo._bar._windows_js", actual);
	}

	public void testToClassNameString_ファイルパス指定_UNIX() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge/foo/bar/unix.js");
		assertEquals("_hoge._foo._bar._unix_js", actual);
	}


	public void testToClassNameStringString_ファイルパス指定_Win() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge\\foo\\windows.js", "_");
		assertEquals("_hoge._foo._windows_js", actual);
	}

	public void testToClassNameStringString_ファイルパス指定_UNIX() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge/foo/unix.js", "*");
		assertEquals("*hoge.*foo.*unix_js", actual);
	}
	
	public void testToClassNameStringString_接頭辞_1文字指定() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge/1_moji", "_");
		assertEquals("_hoge._1_moji", actual);
	}

	public void testToClassNameStringString_接頭辞_2文字指定() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge/2_moji", "ab");
		assertEquals("ahoge.a2_moji", actual);
	}


	public void testToClassNameStringString_接頭辞_空文字() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge/kara", "");
		assertEquals("hoge.kara", actual);
	}

	public void testToClassNameStringString_接頭辞_null() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge/null", null);
		assertEquals("hoge.null", actual);
	}

	
	public void testToClassNameStringString_キャッシュチェック() throws Exception {
		String first = ClassNameHelper.toClassName("hoge_cache", "1");
		assertEquals("1hoge_cache", first);

		String actual = ClassNameHelper.toClassName("hoge_cache", "2");
		assertTrue( !"2hoge_cache".equals(actual));
		assertEquals("1hoge_cache", actual);
	}

	
	public void testToClassNameStringString_キャッシュチェック相互() throws Exception {
		String first = ClassNameHelper.toClassName("hoge_cache/sougo");
		assertEquals("_hoge_cache._sougo", first);

		String actual = ClassNameHelper.toClassName("hoge_cache/sougo", "2");
		assertTrue( !"2hoge_cache".equals(actual));
		assertEquals("_hoge_cache._sougo", actual);
	}


	private int[] vistaAddedCharCode_1 = { 0x7626 }; // 瘦
	private int[] vistaAddedCharCode_2 = { 0x4ff1 }; // 俱
	private int[] vistaAddedCharCode_3 = { 0x525d }; // 剝
	private int[] vistaAddedCharCode_4 = { 0x541e }; // 吞

	private String vistaAdded_1 = new String(vistaAddedCharCode_1, 0, 1); // 瘦
	private String vistaAdded_2 = new String(vistaAddedCharCode_2, 0, 1); // 俱
	private String vistaAdded_3 = new String(vistaAddedCharCode_3, 0, 1); // 剝
	private String vistaAdded_4 = new String(vistaAddedCharCode_4, 0, 1); // 吞
	
	private String vistaAddedStrings = vistaAdded_1 + vistaAdded_2 + vistaAdded_3 + vistaAdded_4;
	
	private int[] sPairCharCode_1 = { 0x2000B }; // 𠀋 : d840 と dc0b 
	private int[] sPairCharCode_2 = { 0x2123D }; // 𡈽 : d844 と de3d
	private int[] sPairCharCode_3 = { 0x20B9F }; // 𠮟 : d842 と df9f

	private String sPair_1 = new String(sPairCharCode_1, 0, 1); // 𠀋
	private String sPair_2 = new String(sPairCharCode_2, 0, 1); // 𡈽
	private String sPair_3 = new String(sPairCharCode_3, 0, 1); // 𠮟
	
	private String sPairStrings = sPair_1 + sPair_2 + sPair_3 + vistaAddedStrings;
	
	public void testToClassNameString_サロゲートペア_Win() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge\\foo\\bar\\" + sPairStrings +"\\windows.js");
		assertEquals("_hoge._foo._bar._" + sPairStrings + "._windows_js", actual);
	}

	public void testToClassNameString_サロゲートペア_UNIX() throws Exception {
		String actual = ClassNameHelper.toClassName("hoge/foo/bar/" + sPairStrings +"/unix.js");
		assertEquals("_hoge._foo._bar._" + sPairStrings + "._unix_js", actual);
	}

}
