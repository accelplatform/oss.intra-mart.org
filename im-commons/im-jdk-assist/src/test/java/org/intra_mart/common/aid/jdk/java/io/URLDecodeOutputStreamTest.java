package org.intra_mart.common.aid.jdk.java.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.intra_mart.common.aid.jdk.java.io.URLDecodeOutputStream;
import org.intra_mart.common.aid.jdk.java.io.URLEncodeOutputStream;

import junit.framework.TestCase;

/**
 * URLDecodeOutputStream のテストクラス
 */
public class URLDecodeOutputStreamTest extends TestCase {

    protected void setUp() throws Exception {
            super.setUp();
    }

    protected void tearDown() throws Exception {
            super.tearDown();
    }

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[])}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列が「空文字」<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列が「空文字」であること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			ちなみに、デコード対象文字列が「null」はテスト対象外<BR>
	 * 								（∵nullをバイト配列にすることはできないから）<BR>
	 */
	public void testWrite_int_001() throws Exception {
		
		String src = "";
		String result = getEncodedStringUseWriteInt(src);
				
        assertEquals("", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[])}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列が英数字のみ<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列がデコード対象文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_int_002() throws Exception {
		
		String src = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String result = getEncodedStringUseWriteInt(src);
				
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[])}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列が記号（%xy化されていない、かつ、「%」「+」以外）<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列がデコード対象文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_int_003() throws Exception {

		String src = "!\"#$&'()*,-./:;<=>?@[\\]^_`{|}~";
		String result = getEncodedStringUseWriteInt(src);
				
        assertEquals("!\"#$&'()*,-./:;<=>?@[\\]^_`{|}~", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[])}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列が日本語（%xy化されていない)<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列がデコード対象文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_int_004() throws Exception {

		String src = "あいうえお";
		String result = getEncodedStringUseWriteInt(src);
				
        assertEquals("あいうえお", result);
	
	}
	
	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[])}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列がURLエンコードされた日本語<BR>
	 * 								（プラットフォームのデフォルトの符号化方式を符号化方式として使用)<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列が、デコード対象文字列のエンコード前の文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_int_005() throws Exception {
		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		System.out.println("----------------------" + ste[0].getMethodName() + "----------------------");		

		String src = "あいうえお";
		System.out.println("src = " + src);

		src = URLEncoder.encode(src, System.getProperty("file.encoding"));
		System.out.println("encodedSrc = " + src);
		
		String result = getEncodedStringUseWriteInt(src);
		System.out.println("result = " + result);
				
        assertEquals("あいうえお", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[])}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列がUTF-8でURLエンコードされた日本語<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列がデコード対象文字列のエンコード前の文字列とは<B>違うこと</B><BR>
	 * 【@】TestCaseType 			異常系（？）<BR>
	 * 【@】TestCaseMemo			<BR>
	 * 		<strong>
	 * 			{@link URLDecodeOutputStream}は、<BR>
	 * 			プラットフォームのデフォルトの符号化方式を使用して、「%xy」の形式の連続シーケンスが表すを決定。<BR>
	 * 			{@link URLEncodeOutputStream}は、<BR>
	 * 			プラットフォームのデフォルトの符号化方式を符号化方式としている。<BR>
	 * 			（つまり、文字コードの指定ができない。）<BR><BR>
	 * 			したがって、分散環境の場合、あるOS上でエンコード→別のOS上でデコードした場合、正常にデコードできない。<BR>
	 * 			そのまた逆(デコード→エンコード)もしかり。<BR>
	 * 		</strong><BR>
	 */
	public void testWrite_int_006() throws Exception {
		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		System.out.println("----------------------" + ste[0].getMethodName() + "----------------------");		

		String src = "あいうえお";
		System.out.println("src = " + src);

		src = URLEncoder.encode(src, "UTF-8");
		System.out.println("encodedSrc = " + src);
		
		
		String result = getEncodedStringUseWriteInt(src);
		System.out.println("result = " + result);

		// ！！！値が異なること！！！
        assertNotSame("あいうえお", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[])}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列がURLエンコードされた記号<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列が、デコード対象文字列のエンコード前の文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_int_007() throws Exception {
		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		System.out.println("----------------------" + ste[0].getMethodName() + "----------------------");		

		String src = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
		System.out.println("src = " + src);

		src = URLEncoder.encode(src, "UTF-8");
		System.out.println("encodedSrc = " + src);
		
		
		String result = getEncodedStringUseWriteInt(src);
		System.out.println("result = " + result);
		
        assertNotSame("!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~", result);
	}
	
	
	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[], int, int)}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列が「空文字」<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列が「空文字」であること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			ちなみに、デコード対象文字列が「null」はテスト対象外<BR>
	 * 								（∵nullをバイト配列にすることはできないから）<BR>
	 */
	public void testWrite_byteIntInt_001() throws Exception {
		
		String src = "";
		String result = getEncodedStringUseWritebyteIntInt(src);
				
        assertEquals("", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[], int, int)}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列が英数字のみ<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列がデコード対象文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_byteIntInt_002() throws Exception {
		
		String src = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String result = getEncodedStringUseWritebyteIntInt(src);
				
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[], int, int)}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列が記号（%xy化されていない、かつ、「%」「+」以外）<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列がデコード対象文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_byteIntInt_003() throws Exception {

		String src = "!\"#$&'()*,-./:;<=>?@[\\]^_`{|}~";
		String result = getEncodedStringUseWritebyteIntInt(src);
				
        assertEquals("!\"#$&'()*,-./:;<=>?@[\\]^_`{|}~", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[], int, int)}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列が日本語（%xy化されていない)<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列がデコード対象文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_byteIntInt_004() throws Exception {

		String src = "あいうえお";
		String result = getEncodedStringUseWritebyteIntInt(src);
				
        assertEquals("あいうえお", result);
	
	}
	
	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[], int, int)}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列がURLエンコードされた日本語<BR>
	 * 								（プラットフォームのデフォルトの符号化方式を符号化方式として使用)<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列が、デコード対象文字列のエンコード前の文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_byteIntInt_005() throws Exception {
		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		System.out.println("----------------------" + ste[0].getMethodName() + "----------------------");		

		String src = "あいうえお";
		System.out.println("src = " + src);

		src = URLEncoder.encode(src, System.getProperty("file.encoding"));
		System.out.println("encodedSrc = " + src);
		
		String result = getEncodedStringUseWritebyteIntInt(src);
		System.out.println("result = " + result);
				
        assertEquals("あいうえお", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[], int, int)}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列がUTF-8でURLエンコードされた日本語<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列がデコード対象文字列のエンコード前の文字列とは<B>違うこと</B><BR>
	 * 【@】TestCaseType 			異常系（？）<BR>
	 * 【@】TestCaseMemo			<BR>
	 * 		<strong>
	 * 			{@link URLDecodeOutputStream}は、<BR>
	 * 			プラットフォームのデフォルトの符号化方式を使用して、「%xy」の形式の連続シーケンスが表すを決定。<BR>
	 * 			{@link URLEncodeOutputStream}は、<BR>
	 * 			プラットフォームのデフォルトの符号化方式を符号化方式としている。<BR>
	 * 			（つまり、文字コードの指定ができない。）<BR><BR>
	 * 			したがって、分散環境の場合、あるOS上でエンコード→別のOS上でデコードした場合、正常にデコードできない。<BR>
	 * 			そのまた逆(デコード→エンコード)もしかり。<BR>
	 * 		<strong><BR>
	 */
	public void testWrite_byteIntInt_006() throws Exception {
		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		System.out.println("----------------------" + ste[0].getMethodName() + "----------------------");		

		String src = "あいうえお";
		System.out.println("src = " + src);

		src = URLEncoder.encode(src, "UTF-8");
		System.out.println("encodedSrc = " + src);
		
		
		String result = getEncodedStringUseWritebyteIntInt(src);
		System.out.println("result = " + result);

		// ！！！値が異なること！！！
        assertNotSame("あいうえお", result);
	
	}

	/**
	 * 【@】TestCaseTitle 			{@link URLDecodeOutputStream#write(byte[], int, int)}のテスト<BR>
	 * 【@】TestCasePreCondition 	デコード対象文字列がURLエンコードされた記号<BR>
	 * 【@】TestCasePostCondition 	デコード後の文字列が、デコード対象文字列のエンコード前の文字列と同じであること<BR>
	 * 【@】TestCaseType 			正常系<BR>
	 * 【@】TestCaseMemo			なし<BR>
	 */
	public void testWrite_byteIntInt_007() throws Exception {
		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		System.out.println("----------------------" + ste[0].getMethodName() + "----------------------");		

		String src = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
		System.out.println("src = " + src);

		src = URLEncoder.encode(src, "UTF-8");
		System.out.println("encodedSrc = " + src);
		
		
		String result = getEncodedStringUseWritebyteIntInt(src);
		System.out.println("result = " + result);
		
        assertNotSame("!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~", result);
	}
	
	
	
	
	/**
	 * {@link URLDecodeOutputStream#write(byte[])}を利用して、URLデコードを行います。
	 * @param src URLデコード対象文字列
	 * @return URLデコードされた文字列
	 * @throws IOException
	 */
	private String getEncodedStringUseWriteInt(String src) throws Exception {

		String result;
		ByteArrayOutputStream bos = null;
		URLDecodeOutputStream udos = null;
		try {
			byte[] b = src.getBytes();

			bos = new ByteArrayOutputStream();
			udos = new URLDecodeOutputStream(bos);
			
			for(int idx = 0; idx < b.length; idx++){
				udos.write(b[idx]);			
			}
			
			result = bos.toString();
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
		finally{
			udos.close();
			bos.close();
			udos = null;
			bos = null;
		}
		
		return result;
	}

	/**
	 * {@link URLDecodeOutputStream#write(byte[], int, int)}を利用して、URLデコードを行います。
	 * @param src URLデコード対象文字列
	 * @return URLデコードされた文字列
	 * @throws IOException
	 */
	private String getEncodedStringUseWritebyteIntInt(String src) throws Exception {

		String result;
		ByteArrayOutputStream bos = null;
		URLDecodeOutputStream udos = null;
		try {
			byte[] b = src.getBytes();

			bos = new ByteArrayOutputStream();
			udos = new URLDecodeOutputStream(bos);
			
			udos.write(b, 0, b.length);
			
			result = bos.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
		finally{
			udos.close();
			bos.close();
			udos = null;
			bos = null;
		}
		
		return result;
	}

}
