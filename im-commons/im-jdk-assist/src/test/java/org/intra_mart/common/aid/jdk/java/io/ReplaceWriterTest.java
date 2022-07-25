package org.intra_mart.common.aid.jdk.java.io;

import junit.framework.TestCase;

public class ReplaceWriterTest extends TestCase {

	private static String LINE_SEP = System.getProperty("line.separator");

	public void test_write_String_closeした場合() throws Exception {
		String oldWord = "bc";
		String newWord = "xyz";

		String str1 = "aaabbcbbbdfbccbb";
		String str2 = "caaabbcbbbdfbccbb";
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
		rw.write(str1);
		rw.write(str2);
		rw.close();
		sw.close();
		
		String expected  = "aaabxyzbbbdfxyzcbxyzaaabxyzbbbdfxyzcbb";
		assertEquals(expected, sw.toString());

	}

	public void test_write_String_flushした場合() throws Exception {
		String oldWord = "bc";
		String newWord = "xyz";

		String str1 = "aaabbcbbbdfbccbb";
		String str2 = "caaabbcbbbdfbccbb";
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
		rw.write(str1);
		rw.write(str2);
		rw.flush();
		sw.flush();
		
		String expected  = "aaabxyzbbbdfxyzcbxyzaaabxyzbbbdfxyzcbb";
		assertEquals(expected, sw.toString());

	}
	
	
	public void test_write_String_finish後にcloseした場合() throws Exception {
		String oldWord = "bc";
		String newWord = "xyz";

		String str1 = "aaabbcbbbdfbccbb";
		String str2 = "caaabbcbbbdfbccbb";
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
		rw.write(str1);
		rw.write(str2);
		rw.finish();
		rw.close();
		sw.close();
		
		String expected  = "aaabxyzbbbdfxyzcbxyzaaabxyzbbbdfxyzcbb";
		assertEquals(expected, sw.toString());

	}
	

	public void test_write_String_改行を含む場合() throws Exception {
		
		
		String oldWord = "bc";
		String newWord = "xyz";

		String str1 = "aaabbcbbbdfbccbb";
		String str2 = "caaabbcbbbdfbccbb";
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
		rw.write(str1);
		rw.write(LINE_SEP);
		rw.write(str2);
		rw.close();
		sw.close();
		
		String expected  = "aaabxyzbbbdfxyzcbb" + LINE_SEP + "caaabxyzbbbdfxyzcbb";
		assertEquals(expected, sw.toString());

	}

	
	private int[] sPairCharCode_1 = { 0x2000B }; // 𠀋 : d840 と dc0b 
	private int[] sPairCharCode_2 = { 0x2123D }; // 𡈽 : d844 と de3d
	private int[] sPairCharCode_3 = { 0x20B9F }; // 𠮟 : d842 と df9f

	private String sPair_1 = new String(sPairCharCode_1, 0, 1); // 𠀋
	private String sPair_2 = new String(sPairCharCode_2, 0, 1); // 𡈽
	private String sPair_3 = new String(sPairCharCode_3, 0, 1); // 𠮟

	public void test_write_String_置換対象がASCII_置換する文字列がサロゲートペアの場合() throws Exception {
		String oldWord = "bc";
		String newWord = sPair_1 + sPair_2 + sPair_3;

		String str1 = "aaabbcbbbdfbccbb";
		String str2 = "caaabbcbbbdfbccbb";
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
		rw.write(str1);
		rw.write(LINE_SEP);
		rw.write(str2);
		rw.close();
		sw.close();
		
		String expected  = "aaab" + sPair_1 + sPair_2 + sPair_3 + "bbbdf" + sPair_1 + sPair_2 + sPair_3 + "cbb" + LINE_SEP + "caaab" + sPair_1 + sPair_2 + sPair_3 + "bbbdf" + sPair_1 + sPair_2 + sPair_3 + "cbb";
		assertEquals(expected, sw.toString());

	}

	public void test_write_String_置換対象がサロゲートペア_置換する文字列がASCIIの場合_かつ_置換対象なし() throws Exception {
		String oldWord = sPair_1 + sPair_2 + sPair_3;
		String newWord = "bc";

		String str1 = "aaabbcbbbdfbccbb";
		String str2 = "caaabbcbbbdfbccbb";
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
		rw.write(str1);
		rw.write(LINE_SEP);
		rw.write(str2);
		rw.close();
		sw.close();
		
		String expected  = str1 + LINE_SEP + str2;
		assertEquals(expected, sw.toString());

	}

	public void test_write_String_置換対象がサロゲートペア_置換する文字列がASCIIの場合_かつ_置換対象あり() throws Exception {
		String oldWord = sPair_1 + sPair_2;
		String newWord = "xyz";

		String str1 = "aaabbcbb" + sPair_1 + sPair_2 + "bdfbccbb" + sPair_1 + sPair_2;
		String str2 = "caaabbcb" + sPair_1 + "bbdfb" + sPair_2 + "cc" + sPair_1 + sPair_2 + "bb";
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
		rw.write(str1);
		rw.write(LINE_SEP);
		rw.write(str2);
		rw.close();
		sw.close();
		
		String expected  = "aaabbcbb" + "xyz" + "bdfbccbb" + "xyz" + LINE_SEP + "caaabbcb" + sPair_1 + "bbdfb" + sPair_2 + "cc" + "xyz" + "bb";;
		assertEquals(expected, sw.toString());

	}
	
	public void test_write_String_置換対象がサロゲートペア_置換する文字列がサロゲートペアの場合_かつ_置換対象なし() throws Exception {
		String oldWord = sPair_1 + sPair_2;
		String newWord = sPair_3;

		String str1 = "aaabbcbb" + sPair_1 + sPair_3 + "bdfbccbb" + sPair_1 + sPair_3;
		String str2 = "caaabbcb" + sPair_1 + "bbdfb" + sPair_2 + "cc" + sPair_1 + sPair_3 + "bb";
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
		rw.write(str1);
		rw.write(LINE_SEP);
		rw.write(str2);
		rw.close();
		sw.close();
		
		String expected  = str1 + LINE_SEP + str2;
		assertEquals(expected, sw.toString());

	}	
	public void test_write_String_置換対象がサロゲートペア_置換する文字列がサロゲートペアの場合_かつ_置換対象あり() throws Exception {
		String oldWord = sPair_1 + sPair_2;
		String newWord = sPair_3;

		String str1 = "aaabbcbb" + sPair_1 + sPair_2 + "bdfbccbb" + sPair_1 + sPair_2;
		String str2 = "caaabbcb" + sPair_1 + "bbdfb" + sPair_2 + "cc" + sPair_1 + sPair_2 + "bb";
		
		java.io.StringWriter sw = new java.io.StringWriter();
		ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
		rw.write(str1);
		rw.write(LINE_SEP);
		rw.write(str2);
		rw.close();
		sw.close();
		
		String expected  = "aaabbcbb" + sPair_3 + "bdfbccbb" + sPair_3 + LINE_SEP + "caaabbcb" + sPair_1 + "bbdfb" + sPair_2 + "cc" + sPair_3 + "bb";;
		assertEquals(expected, sw.toString());

	}
	
}
