package org.intra_mart.common.aid.jdk.util;

import java.util.Date;

import junit.framework.TestCase;

/**
 * int charCode_1 = 0x2000B; // 𠀋 : d840 と dc0b 
 * int charCode_2 = 0x2123D; // 𡈽 : d844 と de3d
 * int charCode_3 = 0x20B9F; // 𠮟 : d842 と df9f
 *
	//------------------瘦俱剝吞------------------
	0x7626,
	0x4ff1,
	0x525d,
	0x541e,
	//------------------------------------
 */
public class CodePointUtilTest extends TestCase {

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
	
	private String sPairStrings = sPair_1 + sPair_2 + sPair_3;
	
	// ================================================================================
	// containsSurrogatePair()
	// ================================================================================
	public void test_containsSurrogatePair_001(){ //サロゲートペア文字のみ(){
		
		String targetString = sPairStrings; // "𠀋𡈽𠮟";

		boolean actual = CodePointUtil.containsSurrogatePair(targetString);
		assertEquals("サロゲートペア文字が含まれて「いる」と判定されること", true, actual);

	}

	public void test_containsSurrogatePair_002(){ //ASCIIのみ(){
		String targetString = "abcABC123`*?";	
		boolean actual;

		actual = CodePointUtil.containsSurrogatePair(targetString);
		assertEquals("サロゲートペア文字が含まれて「いない」と判定されること", false, actual);
	}

	public void test_containsSurrogatePair_003(){ // 今までの2バイト文字のみ(){
		String targetString = "あいうアイウ亜意卯";	
		boolean actual;

		actual = CodePointUtil.containsSurrogatePair(targetString);
		assertEquals("サロゲートペア文字が含まれて「いない」と判定されること", false, actual);
	}

	public void test_containsSurrogatePair_004(){ // ASCII_と_今までの2バイト文字(){
		String targetString = "abcABC123`*?" + "あいうアイウ亜意卯";	
		boolean actual;

		actual = CodePointUtil.containsSurrogatePair(targetString);
		assertEquals("サロゲートペア文字が含まれて「いない」と判定されること", false, actual);
	}

	public void test_containsSurrogatePair_005(){ // 字形変更文字(){
		String targetString = "逢餅祁";
		boolean actual;

		actual = CodePointUtil.containsSurrogatePair(targetString);
		assertEquals("サロゲートペア文字が含まれて「いない」と判定されること", false, actual);
	}

	public void test_containsSurrogatePair_006(){ // Vista追加文字(){
		String targetString = vistaAddedStrings; // "瘦俱剝吞";
		boolean actual;

		actual = CodePointUtil.containsSurrogatePair(targetString);
		assertEquals("サロゲートペア文字が含まれて「いない」と判定されること", false, actual);
	}

	public void test_containsSurrogatePair_007(){ // サロゲートペア文字とそのほかの文字の複合パターン(){
		String string4oneByte     = "abcABC123`*?";
		String string4twoByte     = "あいうアイウ亜意卯";
		String string4jikeiHenkou = "逢餅祁";
		String string4vistaTsuika = vistaAddedStrings; //"瘦俱剝吞";
		String string4surrogate   = sPairStrings; //"𠀋𡈽𠮟";

		
		String targetString = string4surrogate + string4oneByte + string4twoByte + string4jikeiHenkou + string4vistaTsuika + string4surrogate;	
		boolean actual;

		actual = CodePointUtil.containsSurrogatePair(targetString);
		assertEquals("サロゲートペア文字が含まれて「いる」と判定されること", true, actual);
	}

	
	public void test_containsSurrogatePair_008(){ // 負荷テスト(){
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName() + ".test_containsSurrogatePair_負荷テスト");

		String targetString;
		boolean actual;
		
		StringBuilder sb = new StringBuilder();
		String string4surrogate = sPairStrings; //"𠀋𡈽𠮟";
		String append = "abcABC123`*?/$あいうアイウ亜意卯逢餅祁" + vistaAddedStrings; // 瘦俱剝吞"; // 30文字
		
		int max = 300000;
		for(int counter = 0; counter < max; counter++){
			sb.append(append);
		}
	
		targetString = sb.toString();
		logger.fine("targetString.length(): " + targetString.length() + " 文字");
		
		long start;
		start = (new Date()).getTime();
		actual = CodePointUtil.containsSurrogatePair(targetString);
		logger.fine("LAP 1: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("サロゲートペア文字が含まれて「いない」と判定されること", false, actual);

		// サロゲートペア文字追加
		sb.append(string4surrogate);
		targetString = sb.toString();
		logger.fine("targetString.length(): " + targetString.length() + " 文字");
		
		start = (new Date()).getTime();
		actual = CodePointUtil.containsSurrogatePair(targetString);
		logger.fine("LAP 2: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("サロゲートペア文字が含まれて「いる」と判定されること", true, actual);
		
	}

	
	// ================================================================================
	// length()
	// ================================================================================
	public void test_length_001(){ // サロゲートペア文字のみ(){
		String targetString = sPairStrings; //"𠀋𡈽𠮟";
		int actual;

		actual = CodePointUtil.length(targetString);;
		assertEquals("サロゲートペア文字が正しくカウントされていること", 3, actual);
	}

	public void test_length_002(){ // ASCIIのみ() {
		String targetString = "abcABC123`*?";	
		int actual;

		actual = CodePointUtil.length(targetString);;
		assertEquals("ASCII文字が正しくカウントされていること", 12, actual);
	}

	public void test_length_003(){ // 今までの2バイト文字のみ(){
		String targetString = "あいうアイウ亜意卯";	
		int actual;

		actual = CodePointUtil.length(targetString);;
		assertEquals("今までの2バイト文字が正しくカウントされていること", 9, actual);
	}

	public void test_length_004(){ // ASCII_と_今までの2バイト文字(){
		String targetString = "abcABC123`*?" + "あいうアイウ亜意卯";	
		int actual;

		actual = CodePointUtil.length(targetString);;
		assertEquals("ASCII_と_今までの2バイト文字が正しくカウントされていること", 21, actual);
	}

	public void test_length_005(){ // サロゲートペア文字とそのほかの文字の複合パターン(){
		String targetString = sPairStrings + "abcABC123`*?" + "あいうアイウ亜意卯" + "逢餅祁" + vistaAddedStrings + sPairStrings;	
		int actual;

		actual = CodePointUtil.length(targetString);;
		assertEquals("サロゲートペア文字とそのほかの文字の複合パターン文字が正しくカウントされていること", 34, actual);
	}

	public void test_length_006(){ // 負荷テスト(){
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName() + ".test_length_負荷テスト");

		String targetString;
		int actual;
		
		StringBuilder sb = new StringBuilder();
		String string4surrogate   = sPairStrings; //"𠀋𡈽𠮟";
		String append = "abcABC123`*?/$あいうアイウ亜意卯逢餅祁" + vistaAddedStrings; // 瘦俱剝吞"; // 30文字
		
		int max = 300000;
		for(int counter = 0; counter < max; counter++){
			sb.append(append);
		}
	
		targetString = sb.toString();
		logger.fine("targetString.length(): " + targetString.length() + " 文字");
		
		long start;
		start = (new Date()).getTime();
		actual = CodePointUtil.length(targetString);
		logger.fine("LAP 1: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("文字数が正しくカウントされていること", targetString.length(), actual);

		// サロゲートペア文字追加
		sb.append(string4surrogate);
		targetString = sb.toString();
		logger.fine("targetString.length(): " + targetString.length() + " 文字");
		
		start = (new Date()).getTime();
		actual = CodePointUtil.length(targetString);
		logger.fine("LAP 2: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("サロゲートペア文字がそれぞれ一文字としてカウントされていること", targetString.length() - 3, actual);
		
	}

	
	// ================================================================================
	// charAt()
	// TODO ( CSJSではcharCodeAt()かな！？ )
	// ================================================================================
	public void test_charAt_001(){ // サロゲートペア文字のみ(){
		String targetString = sPairStrings; //"𠀋𡈽𠮟";

		int actual = CodePointUtil.charAt(targetString, 1);
		int expected = 0x2123D; // Character.codePointAt("𡈽".toCharArray(), 0);
		assertEquals("【𡈽】サロゲートペア文字が一文字としてカウントされていること", expected, actual);

	}

	public void test_charAt_002(){ // ASCIIのみ(){
		String targetString = "abcABC123`*?";	
		int actual;
		int expected;
		
		actual = CodePointUtil.charAt(targetString, 1);
		expected = 0x62; // Character.codePointAt("b".toCharArray(), 0);
		assertEquals("【b】文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 6);
		expected = 0x31; // Character.codePointAt("1".toCharArray(), 0);
		assertEquals("【1】文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 10);
		expected = 0x2A; // Character.codePointAt("*".toCharArray(), 0);
		assertEquals("【*】文字が正しく取得できること", expected, actual);
	}


	public void test_charAt_003(){ // 今までの2バイト文字のみ(){
		String targetString = "あいうアイウ亜意卯";	
		int actual;
		int expected;

		actual = CodePointUtil.charAt(targetString, 1);
		expected = 0x3044; // Character.codePointAt("い".toCharArray(), 0);
		assertEquals("【い】文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 5);
		expected = 0x30A6; // Character.codePointAt("ウ".toCharArray(), 0);
		assertEquals("【ウ】文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 7);
		expected = 0x610F; // Character.codePointAt("意".toCharArray(), 0);
		assertEquals("【意】文字が正しく取得できること", expected, actual);
	}

	public void test_charAt_004(){ // ASCII_と_今までの2バイト文字(){
		String targetString = "abcABC123`*?" + "あいうアイウ亜意卯";	
		int actual;
		int expected;

		actual = CodePointUtil.charAt(targetString, 1);
		expected = 0x62; // Character.codePointAt("b".toCharArray(), 0);
		assertEquals("【b】文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 6);
		expected = 0x31; // Character.codePointAt("1".toCharArray(), 0);
		assertEquals("【1】文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 10);
		expected = 0x2A; // Character.codePointAt("*".toCharArray(), 0);
		assertEquals("【*】文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 13);
		expected = 0x3044; // Character.codePointAt("い".toCharArray(), 0);
		assertEquals("【い】文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 17);
		expected = 0x30A6; // Character.codePointAt("ウ".toCharArray(), 0);
		assertEquals("【ウ】文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 19);
		expected = 0x610F; // Character.codePointAt("意".toCharArray(), 0);
		assertEquals("【意】文字が正しく取得できること", expected, actual);
	}


	public void test_charAt_005(){ // サロゲートペア文字とそのほかの文字の複合パターン(){
		String string4oneByte     = "abcABC123`*?";
		String string4twoByte     = "あいうアイウ亜意卯";
		String string4jikeiHenkou = "逢餅祁";
		String string4vistaTsuika = vistaAddedStrings; // "瘦俱剝吞";
		String string4surrogate   = sPairStrings; //"𠀋𡈽𠮟";

		
		String targetString = string4surrogate + string4oneByte + string4twoByte + string4jikeiHenkou + string4vistaTsuika + string4surrogate;	
		int actual;
		int expected;

		actual = CodePointUtil.charAt(targetString, 1);
		expected = 0x2123D; // Character.codePointAt("𡈽".toCharArray(), 0);
		assertEquals("【𡈽】サロゲートペア文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 6);
		expected = 0x41; // Character.codePointAt("A".toCharArray(), 0);
		assertEquals("【A】ASCIIバイト文字が正しく取得できること", expected, actual);

		actual = CodePointUtil.charAt(targetString, 21);
		expected = 0x4E9C; // Character.codePointAt("亜".toCharArray(), 0);
		assertEquals("【亜】2バイト文字が正しく取得できること", expected, actual);
		
		actual = CodePointUtil.charAt(targetString, 25);
		expected = 0x9905; // Character.codePointAt("餅".toCharArray(), 0);
		assertEquals("【餅】字形変形文字が正しく取得できること", expected, actual);
		
		actual = CodePointUtil.charAt(targetString, 29);
		expected = 0x525D; // Character.codePointAt("剝".toCharArray(), 0);
		assertEquals("【剝】Vista追加文字が正しく取得できること", expected, actual);
		
		actual = CodePointUtil.charAt(targetString, 31);
		expected = 0x2000B; // Character.codePointAt("𠀋".toCharArray(), 0);
		assertEquals("【𠀋】(再登場)サロゲートペア文字が正しく取得できること", expected, actual);
	}


	
	// ================================================================================
	// indexOf()
	// ================================================================================
	public void test_indexOf_001(){ // サロゲートペア文字のみ(){
		String targetString = sPairStrings; //"𠀋𡈽𠮟";
		int actual;
		
		actual= CodePointUtil.indexOf(targetString, sPair_2); // "𡈽");
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること", 1, actual);

		actual= CodePointUtil.indexOf(targetString, sPair_2, 1); // "𡈽", 1);
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること(from指定あり)", 1, actual);

		actual= CodePointUtil.indexOf(targetString, sPair_2, 2); // "𡈽", 2);
		assertEquals("【𡈽】サロゲートペア文字が検索対象から外れること", -1, actual);
	}

	public void test_indexOf_002(){ // ASCIIのみ(){
		String targetString = "abcABC123`*?";	
		int actual;

		actual= CodePointUtil.indexOf(targetString, "C");
		assertEquals("【C】文字が検索対象になること", 5, actual);

		actual= CodePointUtil.indexOf(targetString, "C", 3);
		assertEquals("【C】文字が検索対象になること(from指定あり)", 5, actual);

		actual= CodePointUtil.indexOf(targetString, "C", 6);
		assertEquals("【C】文字が検索対象から外れること", -1, actual);

	}


	public void test_indexOf_003(){ // 今までの2バイト文字のみ(){
		String targetString = "あいうアイウ亜意卯";	
		int actual;

		actual= CodePointUtil.indexOf(targetString, "亜");
		assertEquals("【亜】文字が検索対象になること", 6, actual);

		actual= CodePointUtil.indexOf(targetString, "亜", 3);
		assertEquals("【亜】文字が検索対象になること(from指定あり)", 6, actual);

		actual= CodePointUtil.indexOf(targetString, "亜", 7);
		assertEquals("【亜】文字が検索対象から外れること", -1, actual);
	}

	public void test_indexOf_004(){ // ASCII_と_今までの2バイト文字(){
		String targetString = "abcABC123`*?" + "あいうアイウ亜意卯";	
		int actual;

		actual= CodePointUtil.indexOf(targetString, "C");
		assertEquals("【C】文字が検索対象になること", 5, actual);

		actual= CodePointUtil.indexOf(targetString, "C", 3);
		assertEquals("【C】文字が検索対象になること(from指定あり)", 5, actual);

		actual= CodePointUtil.indexOf(targetString, "C", 6);
		assertEquals("【C】文字が検索対象から外れること", -1, actual);

		
		actual= CodePointUtil.indexOf(targetString, "亜");
		assertEquals("【亜】文字が検索対象になること", 18, actual);

		actual= CodePointUtil.indexOf(targetString, "亜", 13);
		assertEquals("【亜】文字が検索対象になること(from指定あり)", 18, actual);

		actual= CodePointUtil.indexOf(targetString, "亜", 19);
		assertEquals("【亜】文字が検索対象から外れること", -1, actual);
		
	}


	public void test_indexOf_005(){ // サロゲートペア文字とそのほかの文字の複合パターン(){
		String string4oneByte     = "abcABC123`*?";
		String string4twoByte     = "あいうアイウ亜意卯";
		String string4jikeiHenkou = "逢餅祁";
		String string4vistaTsuika = vistaAddedStrings; // "瘦俱剝吞";
		String string4surrogate   = sPairStrings; //"𠀋𡈽𠮟";

		// "𠀋𡈽𠮟" + "abcABC123`*?" + "あいうアイウ亜意卯" + "逢餅祁" + "瘦俱剝吞" + "𠀋𡈽𠮟"
		String targetString = string4surrogate + string4oneByte + string4twoByte + string4jikeiHenkou + string4vistaTsuika + string4surrogate;
		int actual;

		actual= CodePointUtil.indexOf(targetString, sPair_2); // "𡈽");
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること", 1, actual);

		actual= CodePointUtil.indexOf(targetString, sPair_2, 1); // "𡈽", 1);
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること(from指定あり)", 1, actual);

		actual= CodePointUtil.indexOf(targetString, sPair_2, 2); // "𡈽", 2);
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること(from指定あり,二つ目がヒット)", 32, actual);

		actual= CodePointUtil.indexOf(targetString, sPair_2, 33); // "𡈽", 33);
		assertEquals("【𡈽】サロゲートペア文字が検索対象から外れること", -1, actual);
		
		
		actual= CodePointUtil.indexOf(targetString, "*");
		assertEquals("【*】ASCII文字が検索対象になること", 13, actual);

		actual= CodePointUtil.indexOf(targetString, "*", 3);
		assertEquals("【*】ASCII文字が検索対象になること(from指定あり)", 13, actual);

		actual= CodePointUtil.indexOf(targetString, "*", 14);
		assertEquals("【*】ASCII文字が検索対象から外れること", -1, actual);

		
		actual= CodePointUtil.indexOf(targetString, "イ");
		assertEquals("【イ】今までの2バイト文字が検索対象になること", 19, actual);

		actual= CodePointUtil.indexOf(targetString, "イ", 13);
		assertEquals("【イ】今までの2バイト文字が検索対象になること(from指定あり)", 19, actual);

		actual= CodePointUtil.indexOf(targetString, "イ", 20);
		assertEquals("【イ】今までの2バイト文字が検索対象から外れること", -1, actual);


		actual= CodePointUtil.indexOf(targetString, "餅");
		assertEquals("【餅】字形変更文字が検索対象になること", 25, actual);

		actual= CodePointUtil.indexOf(targetString, "餅", 13);
		assertEquals("【餅】字形変更文字が検索対象になること(from指定あり)", 25, actual);

		actual= CodePointUtil.indexOf(targetString, "餅", 26);
		assertEquals("【餅】字形変更文字が検索対象から外れること", -1, actual);


		actual= CodePointUtil.indexOf(targetString, vistaAdded_3); // "剝");
		assertEquals("【剝】Vista追加文字が検索対象になること", 29, actual);

		actual= CodePointUtil.indexOf(targetString, vistaAdded_3, 13); // "剝", 13);
		assertEquals("【剝】Vista追加文字が検索対象になること(from指定あり)", 29, actual);

		actual= CodePointUtil.indexOf(targetString, vistaAdded_3, 30); // "剝", 30);
		assertEquals("【剝】Vista追加文字が検索対象から外れること", -1, actual);

	}

	
	// ================================================================================
	// lastIndexOf()
	// ================================================================================
	public void test_lastIndexOf_001(){ // サロゲートペア文字のみ(){
		String targetString = sPairStrings; //"𠀋𡈽𠮟";
		int actual;
		
		actual= CodePointUtil.lastIndexOf(targetString, sPair_2); // "𡈽");
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること", 1, actual);

		actual= CodePointUtil.lastIndexOf(targetString, sPair_2, 1); // "𡈽", 1);
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること(from指定あり)", 1, actual);

		actual= CodePointUtil.lastIndexOf(targetString, sPair_2, 0); // "𡈽", 0);
		assertEquals("【𡈽】サロゲートペア文字が検索対象から外れること", -1, actual);
	}

	public void test_lastIndexOf_002(){ // ASCIIのみ(){
		String targetString = "abcABC123`*?";	
		int actual;

		actual= CodePointUtil.lastIndexOf(targetString, "C");
		assertEquals("【C】文字が検索対象になること", 5, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "C", 5);
		assertEquals("【C】文字が検索対象になること(from指定あり)", 5, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "C", 4);
		assertEquals("【C】文字が検索対象から外れること", -1, actual);

	}


	public void test_lastIndexOf_003(){ // 今までの2バイト文字のみ(){
		String targetString = "あいうアイウ亜意卯";	
		int actual;

		actual= CodePointUtil.lastIndexOf(targetString, "亜");
		assertEquals("【亜】文字が検索対象になること", 6, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "亜", 6);
		assertEquals("【亜】文字が検索対象になること(from指定あり)", 6, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "亜", 5);
		assertEquals("【亜】文字が検索対象から外れること", -1, actual);
	}

	public void test_lastIndexOf_004(){ // ASCII_と_今までの2バイト文字(){
		String targetString = "abcABC123`*?" + "あいうアイウ亜意卯";	
		int actual;

		actual= CodePointUtil.lastIndexOf(targetString, "C");
		assertEquals("【C】文字が検索対象になること", 5, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "C", 5);
		assertEquals("【C】文字が検索対象になること(from指定あり)", 5, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "C",4);
		assertEquals("【C】文字が検索対象から外れること", -1, actual);

		
		actual= CodePointUtil.lastIndexOf(targetString, "亜");
		assertEquals("【亜】文字が検索対象になること", 18, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "亜", 18);
		assertEquals("【亜】文字が検索対象になること(from指定あり)", 18, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "亜", 17);
		assertEquals("【亜】文字が検索対象から外れること", -1, actual);
		
	}


	public void test_lastIndexOf_005(){ // サロゲートペア文字とそのほかの文字の複合パターン(){
		String string4oneByte     = "abcABC123`*?";
		String string4twoByte     = "あいうアイウ亜意卯";
		String string4jikeiHenkou = "逢餅祁";
		String string4vistaTsuika = vistaAddedStrings; // "瘦俱剝吞";
		String string4surrogate   = sPairStrings; //"𠀋𡈽𠮟";
		// "𠀋𡈽𠮟" + "abcABC123`*?" + "あいうアイウ亜意卯" + "逢餅祁" + "瘦俱剝吞" + "𠀋𡈽𠮟"
		String targetString = string4surrogate + string4oneByte + string4twoByte + string4jikeiHenkou + string4vistaTsuika + string4surrogate;	
		int actual;

		actual= CodePointUtil.lastIndexOf(targetString, sPair_2); // "𡈽");
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること", 32, actual);

		actual= CodePointUtil.lastIndexOf(targetString, sPair_2, 32); // "𡈽", 32);
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること(from指定あり)", 32, actual);

		actual= CodePointUtil.lastIndexOf(targetString, sPair_2, 31); // "𡈽", 31);
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること(from指定あり,二つ目がヒット)", 1, actual);

		actual= CodePointUtil.lastIndexOf(targetString, sPair_2, 1); // "𡈽", 1);
		assertEquals("【𡈽】サロゲートペア文字が検索対象になること(from指定あり,二つ目がヒット)", 1, actual);

		actual= CodePointUtil.lastIndexOf(targetString, sPair_2, 0); // "𡈽", 0);
		assertEquals("【𡈽】サロゲートペア文字が検索対象から外れること", -1, actual);
		
		
		actual= CodePointUtil.lastIndexOf(targetString, "*");
		assertEquals("【*】ASCII文字が検索対象になること", 13, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "*", 13);
		assertEquals("【*】ASCII文字が検索対象になること(from指定あり)", 13, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "*", 12);
		assertEquals("【*】ASCII文字が検索対象から外れること", -1, actual);

		
		actual= CodePointUtil.lastIndexOf(targetString, "イ");
		assertEquals("【イ】今までの2バイト文字が検索対象になること", 19, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "イ", 19);
		assertEquals("【イ】今までの2バイト文字が検索対象になること(from指定あり)", 19, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "イ", 18);
		assertEquals("【イ】今までの2バイト文字が検索対象から外れること", -1, actual);


		actual= CodePointUtil.lastIndexOf(targetString, "餅");
		assertEquals("【餅】字形変更文字が検索対象になること", 25, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "餅", 25);
		assertEquals("【餅】字形変更文字が検索対象になること(from指定あり)", 25, actual);

		actual= CodePointUtil.lastIndexOf(targetString, "餅", 24);
		assertEquals("【餅】字形変更文字が検索対象から外れること", -1, actual);


		actual= CodePointUtil.lastIndexOf(targetString, vistaAdded_3); // "剝");
		assertEquals("【剝】Vista追加文字が検索対象になること", 29, actual);

		actual= CodePointUtil.lastIndexOf(targetString, vistaAdded_3, 29); // "剝", 29);
		assertEquals("【剝】Vista追加文字が検索対象になること(from指定あり)", 29, actual);

		actual= CodePointUtil.lastIndexOf(targetString, vistaAdded_3, 28); // "剝", 28);
		assertEquals("【剝】Vista追加文字が検索対象から外れること", -1, actual);

	}

	// ================================================================================
	// substring()
	// TODO どうする？ → 注意：start が end より大きいならば、それらは交換される。
	// ================================================================================
	public void test_substring_001(){ // サロゲートペア文字のみ() {
		String actual;
		String targetString = sPairStrings + sPairStrings; //"𠀋" + "𡈽" + "𠮟" + "𠀋" + "𡈽" + "𠮟";

// * ************ JIS2004対応後に、以下を削除。（∵テストが実行時エラーとなるため）************
// String targetString = "あ" + "い" + "う" + "ア" + "イ" + "ウ";
// * ************ JIS2004対応後に、以下を削除。（∵テストが実行時エラーとなるため）************

		actual = CodePointUtil.substring(targetString, 3);
		assertEquals("startのみ指定する場合、startが正の場合、部分文字列が正しいこと", sPairStrings, actual); //"𠀋𡈽𠮟", actual);

		actual = CodePointUtil.substring(targetString, 0);
		assertEquals("startのみ指定する場合、startが0の場合、部分文字列が正しいこと", targetString, actual);

		try {
			actual = CodePointUtil.substring(targetString, -2);
			assertEquals("startのみ指定する場合、startが負の場合、部分文字列が正しいこと", targetString, actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 3, 3);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "", actual);

		actual = CodePointUtil.substring(targetString, 3, 4);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1, actual); // "𠀋", actual);

		actual = CodePointUtil.substring(targetString, 3, 5);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2, actual); //"𠀋𡈽", actual);

		try {
			actual = CodePointUtil.substring(targetString, 3, 0);
			assertEquals("startが正、endが0の場合、部分文字列が正しいこと", sPair_1 + sPair_2 + sPair_3, actual); //"𠀋𡈽𠮟", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -4);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2, actual); //"𠀋𡈽", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -4, 2);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2, actual); //"𠀋𡈽", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 0, 2);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2, actual); //"𠀋𡈽", actual);

		try {
			actual = CodePointUtil.substring(targetString, 2, -3);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2, actual); //"𠀋𡈽", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -2);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2, actual); //"𠀋𡈽", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 0, 1);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", sPair_1, actual); //"𠀋", actual);

		actual = CodePointUtil.substring(targetString, 0, 2);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2, actual); //"𠀋𡈽", actual);

		actual = CodePointUtil.substring(targetString, 0, 0);
		assertEquals("startが0、endが0の場合、部分文字列が正しいこと", "", actual);

		try {
			actual = CodePointUtil.substring(targetString, 0, -6);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -5);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -4);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 3);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2 + sPair_3, actual); //"𠀋𡈽𠮟", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 4);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2 + sPair_3 + sPair_1, actual); //"𠀋𡈽𠮟𠀋", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 5);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2 + sPair_3 + sPair_1 + sPair_2, actual); //"𠀋𡈽𠮟𠀋𡈽", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 6);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2 + sPair_3 + sPair_1 + sPair_2 + sPair_3, actual); //"𠀋𡈽𠮟𠀋𡈽𠮟", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 7);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + sPair_2 + sPair_3 + sPair_1 + sPair_2 + sPair_3, actual); //"𠀋𡈽𠮟𠀋𡈽𠮟", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 0);
			assertEquals("startが負、endが0の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -3);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -2);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -1);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

	}

	public void test_substring_002(){ // ASCIIのみ() {
		String actual;
		String targetString = "abcABC";

		actual = CodePointUtil.substring(targetString, 3);
		assertEquals("startのみ指定する場合、startが正の場合、部分文字列が正しいこと", "ABC", actual);

		actual = CodePointUtil.substring(targetString, 0);
		assertEquals("startのみ指定する場合、startが0の場合、部分文字列が正しいこと", targetString, actual);

		try {
			actual = CodePointUtil.substring(targetString, -2);
			assertEquals("startのみ指定する場合、startが負の場合、部分文字列が正しいこと", targetString, actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 3, 3);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "", actual);

		actual = CodePointUtil.substring(targetString, 3, 4);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "A", actual);

		actual = CodePointUtil.substring(targetString, 3, 5);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "AB", actual);

		try {
			actual = CodePointUtil.substring(targetString, 3, 0);
			assertEquals("startが正、endが0の場合、部分文字列が正しいこと", "abc", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -4);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "ab", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -3);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "ab", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -2);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "ab", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 0, 1);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", "a", actual);

		actual = CodePointUtil.substring(targetString, 0, 2);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", "ab", actual);

		actual = CodePointUtil.substring(targetString, 0, 0);
		assertEquals("startが0、endが0の場合、部分文字列が正しいこと", "", actual);

		try {
			actual = CodePointUtil.substring(targetString, 0, -6);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -5);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -4);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 3);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "abc", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 4);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "abcA", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 5);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "abcAB", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 6);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "abcABC", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 7);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "abcABC", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 0);
			assertEquals("startが負、endが0の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -3);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -2);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -1);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

	}

	public void test_substring_003(){ // 今までの2バイト文字のみ() {
		String actual;
		String targetString = "あいうアイウ";

		actual = CodePointUtil.substring(targetString, 3);
		assertEquals("startのみ指定する場合、startが正の場合、部分文字列が正しいこと", "アイウ", actual);

		actual = CodePointUtil.substring(targetString, 0);
		assertEquals("startのみ指定する場合、startが0の場合、部分文字列が正しいこと", targetString, actual);

		try {
			actual = CodePointUtil.substring(targetString, -2);
			assertEquals("startのみ指定する場合、startが負の場合、部分文字列が正しいこと", targetString, actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 3, 3);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "", actual);

		actual = CodePointUtil.substring(targetString, 3, 4);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "ア", actual);

		actual = CodePointUtil.substring(targetString, 3, 5);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "アイ", actual);

		try {
			actual = CodePointUtil.substring(targetString, 3, 0);
			assertEquals("startが正、endが0の場合、部分文字列が正しいこと", "あいう", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -4);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "あい", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -3);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "あい", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -2);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "あい", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 0, 1);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", "あ", actual);

		actual = CodePointUtil.substring(targetString, 0, 2);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", "あい", actual);

		actual = CodePointUtil.substring(targetString, 0, 0);
		assertEquals("startが0、endが0の場合、部分文字列が正しいこと", "", actual);

		try {
			actual = CodePointUtil.substring(targetString, 0, -6);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -5);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -4);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 3);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "あいう", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 4);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "あいうア", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 5);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "あいうアイ", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 6);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "あいうアイウ", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 7);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "あいうアイウ", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 0);
			assertEquals("startが負、endが0の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -3);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -2);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -1);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}
	}

	public void test_substring_004(){ // ASCII_と_今までの2バイト文字() {
		String actual;
		String targetString = "いあbcAう";

		actual = CodePointUtil.substring(targetString, 3);
		assertEquals("startのみ指定する場合、startが正の場合、部分文字列が正しいこと", "cAう", actual);

		actual = CodePointUtil.substring(targetString, 0);
		assertEquals("startのみ指定する場合、startが0の場合、部分文字列が正しいこと", targetString, actual);

		try {
			actual = CodePointUtil.substring(targetString, -2);
			assertEquals("startのみ指定する場合、startが負の場合、部分文字列が正しいこと", targetString, actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 3, 3);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "", actual);

		actual = CodePointUtil.substring(targetString, 3, 4);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "c", actual);

		actual = CodePointUtil.substring(targetString, 3, 5);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "cA", actual);

		try {
			actual = CodePointUtil.substring(targetString, 3, 0);
			assertEquals("startが正、endが0の場合、部分文字列が正しいこと", "いあb", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -4);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "いあ", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -3);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "いあ", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -2);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "いあ", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 0, 1);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", "い", actual);

		actual = CodePointUtil.substring(targetString, 0, 2);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", "いあ", actual);

		actual = CodePointUtil.substring(targetString, 0, 0);
		assertEquals("startが0、endが0の場合、部分文字列が正しいこと", "", actual);

		try {
			actual = CodePointUtil.substring(targetString, 0, -6);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -5);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -4);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 3);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "いあb", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 4);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "いあbc", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 5);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "いあbcA", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 6);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "いあbcAう", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 7);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", "いあbcAう", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 0);
			assertEquals("startが負、endが0の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -3);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -2);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -1);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}
	}


	public void test_substring_005(){ // サロゲートペア文字_と_そのほかの文字の複合パターン() {
		String actual;
		String targetString = sPair_1 + "a" + "あ" + "逢" + vistaAdded_1 + sPair_2;

// * ************ JIS2004対応後に、以下を削除。（∵テストが実行時エラーとなるため）************
// String targetString = "あ" + "い"+ "う" + "ア" + "イ" + "ウ";
// * ************ JIS2004対応後に、以下を削除。（∵テストが実行時エラーとなるため）************

		actual = CodePointUtil.substring(targetString, 3);
		assertEquals("startのみ指定する場合、startが正の場合、部分文字列が正しいこと", "逢" + vistaAdded_1 + sPair_2, actual); //"逢瘦𡈽", actual);

		actual = CodePointUtil.substring(targetString, 0);
		assertEquals("startのみ指定する場合、startが0の場合、部分文字列が正しいこと", targetString, actual);

		try {
			actual = CodePointUtil.substring(targetString, -2);
			assertEquals("startのみ指定する場合、startが負の場合、部分文字列が正しいこと", targetString, actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 3, 3);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "", actual);

		actual = CodePointUtil.substring(targetString, 3, 4);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "逢", actual);

		actual = CodePointUtil.substring(targetString, 3, 5);
		assertEquals("startが正、endが正の場合、部分文字列が正しいこと", "逢" + vistaAdded_1, actual); // "逢瘦", actual);

		try {
			actual = CodePointUtil.substring(targetString, 3, 0);
			assertEquals("startが正、endが0の場合、部分文字列が正しいこと", sPair_1 + "aあ", actual); // "𠀋aあ", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -4);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1 + "a", actual); // "𠀋a", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -3);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1 + "a", actual); //"𠀋a", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 2, -2);
			assertEquals("startが正、endが正の場合、部分文字列が正しいこと", sPair_1 + "a", actual); //"𠀋a", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		actual = CodePointUtil.substring(targetString, 0, 1);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", sPair_1, actual); //"𠀋", actual);

		actual = CodePointUtil.substring(targetString, 0, 2);
		assertEquals("startが0、endが正の場合、部分文字列が正しいこと", sPair_1 + "a", actual); //"𠀋a", actual);

		actual = CodePointUtil.substring(targetString, 0, 0);
		assertEquals("startが0、endが0の場合、部分文字列が正しいこと", "", actual);

		try {
			actual = CodePointUtil.substring(targetString, 0, -6);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -5);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, 0, -4);
			assertEquals("startが0、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 3);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + "aあ", actual); //"𠀋aあ", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 4);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + "aあ逢", actual); //"𠀋aあ逢", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 5);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + "aあ逢" + vistaAdded_1, actual); // "𠀋aあ逢瘦", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 6);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + "aあ逢" + vistaAdded_1 + sPair_2, actual); // "𠀋aあ逢瘦𡈽", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 7);
			assertEquals("startが負、endが正の場合、部分文字列が正しいこと", sPair_1 + "aあ逢" + vistaAdded_1 + sPair_2, actual); // "𠀋aあ逢瘦𡈽", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, 0);
			assertEquals("startが負、endが0の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -3);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -2);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}

		try {
			actual = CodePointUtil.substring(targetString, -2, -1);
			assertEquals("startが負、endが負の場合、部分文字列が正しいこと", "", actual);
			fail("JavaのCodePointUtil用のfail()です。CSJSでは不要です。");
		}
		catch (Throwable t) {
			// JavaのCodePointUtil用のassertTrue()です。CSJSでは不要です。");
			assertTrue("IndexOutOfBoundsExceptionが発生すること", t instanceof IndexOutOfBoundsException);
		}
	}

	
	// ================================================================================
	// convCodePointIndex2Index()
	// ================================================================================
	public void test_convCodePointIndex2Index_001(){
		String string4surrogate   = sPairStrings; //"𠀋𡈽𠮟";
		String string4oneByte     = "abcABC123`*?";
		String string4twoByte     = "あいうアイウ亜意卯";
		String string4jikeiHenkou = "逢餅祁";
		String string4vistaTsuika = vistaAddedStrings; // "瘦俱剝吞";

		String targetString = string4surrogate + string4oneByte + string4twoByte + string4jikeiHenkou + string4vistaTsuika + string4surrogate;	
		int actual;

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 0);
		assertEquals("【𠀋】サロゲートペア文字が正しくカウントされていること", 0, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 1);
		assertEquals("【𡈽】サロゲートペア文字が正しくカウントされていること", 2, actual);
	
		actual = CodePointUtil.convCodePointIndex2Index(targetString, 2);
		assertEquals("【𠮟】サロゲートペア文字が正しくカウントされていること", 4, actual);

		
		actual = CodePointUtil.convCodePointIndex2Index(targetString, 3);
		assertEquals("【a】ASCII文字が正しくカウントされていること", 6, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 4);
		assertEquals("【b】ASCII文字が正しくカウントされていること", 7, actual);
		
		actual = CodePointUtil.convCodePointIndex2Index(targetString, 14);
		assertEquals("【?】ASCII文字が正しくカウントされていること", 17, actual);

		
		actual = CodePointUtil.convCodePointIndex2Index(targetString, 15); 
		assertEquals("【あ】今までの2バイト文字が正しくカウントされていること", 18, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 16);
		assertEquals("【い】今までの2バイト文字が正しくカウントされていること", 19, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 23);
		assertEquals("【卯】今までの2バイト文字が正しくカウントされていること", 26, actual);

		
		actual = CodePointUtil.convCodePointIndex2Index(targetString, 24);
		assertEquals("【逢】字形変更文字が正しくカウントされていること", 27, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 25);
		assertEquals("【餅】字形変更文字が正しくカウントされていること", 28, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 26);
		assertEquals("【祁】字形変更文字が正しくカウントされていること", 29, actual);


		actual = CodePointUtil.convCodePointIndex2Index(targetString, 27);
		assertEquals("【瘦】Vista追加文字が正しくカウントされていること", 30, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 28);
		assertEquals("【俱】Vista追加文字が正しくカウントされていること", 31, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 29);
		assertEquals("【剝】Vista追加文字が正しくカウントされていること", 32, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 30);
		assertEquals("【吞】Vista追加文字が正しくカウントされていること", 33, actual);

		
		actual = CodePointUtil.convCodePointIndex2Index(targetString, 31);
		assertEquals("【𠀋】サロゲートペア文字(二回目)が正しくカウントされていること", 34, actual);

		actual = CodePointUtil.convCodePointIndex2Index(targetString, 32);
		assertEquals("【𡈽】サロゲートペア文字(二回目)が正しくカウントされていること", 36, actual);
	
		actual = CodePointUtil.convCodePointIndex2Index(targetString, 33);
		assertEquals("【𠮟】サロゲートペア文字(二回目)が正しくカウントされていること", 38, actual);
	
	}
	
	public void test_convCodePointIndex2Index_002(){ // 負荷テスト(){
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName() + ".test_convCodePointIndex2Index_負荷テスト");

		String targetString;	
		int actual;
		
		StringBuilder sb = new StringBuilder();
		String string4surrogate = sPairStrings; //"𠀋𡈽𠮟";
		String append = "abcABC123`*?/$あいうアイウ亜意卯逢餅祁" + vistaAddedStrings; // 瘦俱剝吞"; // 30文字
		
		int max = 300000;
		for(int counter = 0; counter < max; counter++){
			sb.append(append);
		}

		sb.append(string4surrogate);
		
		targetString = sb.toString();

		logger.fine("targetString.length(): " + targetString.length() + " 文字");
		
		
		long start;
		
		start = (new Date()).getTime();
		actual = CodePointUtil.convCodePointIndex2Index(targetString, (append.length() * max));
		logger.fine("LAP 1: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("大量文字列でもサロゲートペア文字が正しくカウントされていること", (append.length() * max), actual);
		
		start = (new Date()).getTime();
		actual = CodePointUtil.convCodePointIndex2Index(targetString, (append.length() * max) + 1);
		logger.fine("LAP 2: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("大量文字列でもサロゲートペア文字が正しくカウントされていること", (append.length() * max) + 2, actual);

		start = (new Date()).getTime();
		actual = CodePointUtil.convCodePointIndex2Index(targetString, (append.length() * max) + 2);
		logger.fine("LAP 3: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("大量文字列でもサロゲートペア文字が正しくカウントされていること", (append.length() * max) + 4, actual);

		start = (new Date()).getTime();
		actual = CodePointUtil.convCodePointIndex2Index(targetString, (append.length() * max) + 3);
		logger.fine("LAP 4: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("大量文字列でもサロゲートペア文字が正しくカウントされていること", (append.length() * max) + 6, actual);
	}
	


	// ================================================================================
	// convIndex2CodePointIndex()
	// ================================================================================
	public void test_convIndex2CodePointIndex_001(){
		String string4surrogate   = sPairStrings; //"𠀋𡈽𠮟";
		String string4oneByte     = "abcABC123`*?";
		String string4twoByte     = "あいうアイウ亜意卯";
		String string4jikeiHenkou = "逢餅祁";
		String string4vistaTsuika = vistaAddedStrings; // "瘦俱剝吞";

		String targetString = string4surrogate + string4oneByte + string4twoByte + string4jikeiHenkou + string4vistaTsuika + string4surrogate;	
		int actual;

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 0);
		assertEquals("【𠀋】サロゲートペア文字が正しくカウントされていること", 0, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 2);
		assertEquals("【𡈽】サロゲートペア文字が正しくカウントされていること", 1, actual);
	
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 4);
		assertEquals("【𠮟】サロゲートペア文字が正しくカウントされていること", 2, actual);

		
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 6);
		assertEquals("【a】ASCII文字が正しくカウントされていること", 3, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 7);
		assertEquals("【b】ASCII文字が正しくカウントされていること", 4, actual);
		
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 17);
		assertEquals("【?】ASCII文字が正しくカウントされていること", 14, actual);

		
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 18); 
		assertEquals("【あ】今までの2バイト文字が正しくカウントされていること", 15, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 19);
		assertEquals("【い】今までの2バイト文字が正しくカウントされていること", 16, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 26);
		assertEquals("【卯】今までの2バイト文字が正しくカウントされていること", 23, actual);

		
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 27);
		assertEquals("【逢】字形変更文字が正しくカウントされていること", 24, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 28);
		assertEquals("【餅】字形変更文字が正しくカウントされていること", 25, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 29);
		assertEquals("【祁】字形変更文字が正しくカウントされていること", 26, actual);


		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 30);
		assertEquals("【瘦】Vista追加文字が正しくカウントされていること", 27, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 31);
		assertEquals("【俱】Vista追加文字が正しくカウントされていること", 28, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 32);
		assertEquals("【剝】Vista追加文字が正しくカウントされていること", 29, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 33);
		assertEquals("【吞】Vista追加文字が正しくカウントされていること", 30, actual);

		
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 34);
		assertEquals("【𠀋】サロゲートペア文字(二回目)が正しくカウントされていること", 31, actual);

		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 36);
		assertEquals("【𡈽】サロゲートペア文字(二回目)が正しくカウントされていること", 32, actual);
	
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, 38);
		assertEquals("【𠮟】サロゲートペア文字(二回目)が正しくカウントされていること", 33, actual);
	
	}
	
	public void test_convIndex2CodePointIndex_002(){ // 負荷テスト(){
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName() + ".test_convIndex2CodePointIndex_負荷テスト");

		String targetString;	
		int actual;
		
		StringBuilder sb = new StringBuilder();
		String string4surrogate   = sPairStrings; //"𠀋𡈽𠮟";
		String append = "abcABC123`*?/$あいうアイウ亜意卯逢餅祁" + vistaAddedStrings; // 瘦俱剝吞"; // 30文字
		
		int max = 300000;
		for(int counter = 0; counter < max; counter++){
			sb.append(append);
		}

		sb.append(string4surrogate);
		
		targetString = sb.toString();

		logger.fine("targetString.length(): " + targetString.length() + " 文字");
		
		
		long start;
		
		start = (new Date()).getTime();
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, (append.length() * max));
		logger.fine("LAP 1: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("大量文字列でもサロゲートペア文字が正しくカウントされていること", (append.length() * max), actual);
		
		start = (new Date()).getTime();
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, (append.length() * max) + 2);
		logger.fine("LAP 2: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("大量文字列でもサロゲートペア文字が正しくカウントされていること", (append.length() * max) + 1, actual);

		start = (new Date()).getTime();
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, (append.length() * max) + 4);
		logger.fine("LAP 3: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("大量文字列でもサロゲートペア文字が正しくカウントされていること", (append.length() * max) + 2, actual);

		start = (new Date()).getTime();
		actual = CodePointUtil.convIndex2CodePointIndex(targetString, (append.length() * max) + 6);
		logger.fine("LAP 4: " + ((new Date()).getTime() - start) + "[ms]");
		assertEquals("大量文字列でもサロゲートペア文字が正しくカウントされていること", (append.length() * max) + 3, actual);
	}
	
}
