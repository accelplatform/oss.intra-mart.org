package org.intra_mart.common.aid.jdk.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.intra_mart.common.aid.jdk.java.io.TrimmedFilterWriter;

import junit.framework.TestCase;

/**
 *
 * TrimmedFilterWriter のテストクラス
 *
 */
public class TrimmedFilterWriterTest extends TestCase {

        /* (非 Javadoc)
         * @see junit.framework.TestCase#setUp()
         */
        protected void setUp() throws Exception {
                 super.setUp();
        }

        /* (非 Javadoc)
         * @see junit.framework.TestCase#tearDown()
         */
        protected void tearDown() throws Exception {
                super.tearDown();
        }

	/**
	 * 基礎となる文字の配列
	 */
	private char[] charArray = "ABCDEFGHIJKLMNOPQR\nSTUVWXYZ".toCharArray();

	/**
	 * 空白除去される文字列
	 */
	private char[] charBuffer = "0	2 4\n6 8 10".toCharArray();


	/**
	 * write(int) メソッドのテスト
	 * @throws IOException
	 */
	public void testTrim() throws IOException{
		StringWriter sw	= new StringWriter();
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		tw.write('a');
		tw.flush();
		tw.close();
		assertEquals("a",	sw.toString());			
	}

	/**
	 * write(int) メソッドのテスト
	 * @throws IOException
	 */
	public void testTrimBase() throws IOException{
		StringWriter sw	= new StringWriter();
		sw.write(charArray);
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		tw.write('a');
		tw.flush();
		tw.close();
		assertEquals("ABCDEFGHIJKLMNOPQR\nSTUVWXYZa",	sw.toString());			
	}

	/**
	 * 引数の単一文字が空白の場合
	 * @throws IOException
	 */
	public void testTrimArgIntWhiteSpace() throws IOException{
		StringWriter sw	= new StringWriter();
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		tw.write(' ');
		tw.flush();
		tw.close();
		assertEquals("",	sw.toString());			
	}

	/**
	 * 基礎となる出力ストリームと引数の単一文字が空白の場合
	 * @throws IOException
	 */
	public void testTrimBaseArgIntWhiteSpace() throws IOException{
		StringWriter sw	= new StringWriter();
		sw.write("          ".toCharArray());
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		tw.write(' ');
		tw.flush();
		tw.close();
		assertEquals("          ",	sw.toString());			
	}

	/**
	 * 基礎となる出力ストリームと引数の文字の配列が空白の場合
	 * @throws IOException
	 */
	public void testTrimBaseArgCbufWhiteSpace() throws IOException{
		StringWriter sw	= new StringWriter();
		sw.write("          ".toCharArray());
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		tw.write("   ".toCharArray());
		tw.flush();
		tw.close();
		assertEquals("          ",	sw.toString());			
	}


	/**
	 * write(String) メソッドのテスト
	 * @throws IOException
	 */
	public void testTrimArgString() throws IOException{
		StringWriter sw	= new StringWriter();
		sw.write(charArray);
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		tw.write(String.valueOf(charBuffer));
		tw.flush();
		tw.close();
		assertEquals("ABCDEFGHIJKLMNOPQR\nSTUVWXYZ0246810",
				sw.toString());			
	}

	/**
	 * write(char[]) メソッドのテスト
	 * @throws IOException
	 */
	public void testTrimArgCbuf() throws IOException{
		StringWriter sw	= new StringWriter();
		sw.write(charArray);
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		tw.write(charBuffer);
		tw.flush();
		tw.close();
		assertEquals("ABCDEFGHIJKLMNOPQR\nSTUVWXYZ0246810",
				sw.toString());			

	}

	/**
	 * write(char[], int, int) メソッドのテスト
	 * @throws IOException
	 */
	public void testTrimArgCbufOffLen() throws IOException{
		StringWriter sw = new StringWriter();
		sw.write(charArray);
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		tw.write(charBuffer,3, 5);
		tw.flush();
		tw.close();
		assertEquals("ABCDEFGHIJKLMNOPQR\nSTUVWXYZ46",sw.getBuffer().toString());

	}

	
	/**
	 * 指定された文字の配列がnullの場合に NullPointerException を throw します。
	 * 
	 * @throws IOException
	 * @throws NullPointerException
	 */

	public void testTrimNullPointerException()
		throws IOException, NullPointerException{

		StringWriter sw = new StringWriter();
		sw.write(charArray);
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		
		charBuffer = null;
		try {
			tw.write(charBuffer,0, 0);
			fail("NullPointerExceptionが正しく発生しませんでした。");
		}
		catch(NullPointerException ex){			
		}

	}

	/**
	 * 指定された引数が以下の場合に IndexOutOfBoundsException を throw します。
	 * 1.オフセット off が負の場合
	 * 2.文字数 len が負の場合
	 * 3.オフセット off + 文字数 len が文字の配列 cbuf の長さよりも大きい場合
	 * 
	 * @throws IOException
	 * @throws IndexOutOfBoundsException
	 */
	public void testTrimIndexOutOfBoundsException() throws IOException {
		StringWriter sw = new StringWriter();
		sw.write(charArray);
		TrimmedFilterWriter tw = new TrimmedFilterWriter(sw);
		charBuffer = "a c".toCharArray();
		
		// off < 0
		try {
			tw.write(charBuffer,-1, 2);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}

		// off > cbuf.length
		try {
			tw.write(charBuffer,10, 1);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}

		// len < 0
		try {
			tw.write(charBuffer,0, -1);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}

		// off + len > cbuf.length
		try {
			tw.write(charBuffer, 2, 2);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}

		// off + len < 0
		try {
			tw.write(charBuffer, 0, -1);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
			tw.write(charBuffer, -1, 0);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
			tw.write(charBuffer, -1, -1);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}
	}

}
