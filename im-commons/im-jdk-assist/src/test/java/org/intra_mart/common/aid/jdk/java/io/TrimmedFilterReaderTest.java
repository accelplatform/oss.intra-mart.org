package org.intra_mart.common.aid.jdk.java.io;

import java.io.IOException;
import java.io.StringReader;

import org.intra_mart.common.aid.jdk.java.io.TrimmedFilterReader;

import junit.framework.TestCase;

/**
 * TrimmedFilterReader のテストクラス
 *
 */
public class TrimmedFilterReaderTest extends TestCase {


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
	 * 空白除去される文字列
	 */
	private String str = "0	2 4\n6 8 10";
	
	/**
	 * 引数で指定される文字の配列
	 */
	private char[] charArray = "ABCDEFGHIJKLMNOPQR\nSTUVWXYZ".toCharArray();

	/**
	 * 基礎となる入力ストリームが空白だけの場合
	 * @throws IOException
	 */
	public void testTrim() throws IOException {
		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(String.valueOf(' ')));
		int c = in.read();
		assertEquals(-1,c);

	}

	/**
	 * read()の場合
	 * @throws IOException
	 */
	public void testTrimReader() throws IOException {
		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(str));
		char charBuffer[] = new char[7];
		charBuffer[0] = (char)in.read();
		charBuffer[1] = (char)in.read();
		charBuffer[2] = (char)in.read();
		charBuffer[3] = (char)in.read();
		charBuffer[4] = (char)in.read();
		charBuffer[5] = (char)in.read();
		charBuffer[6] = (char)in.read();
		assertEquals("0246810",String.valueOf(charBuffer));

	}

	/**
	 * 入力文字ストリームが空白の場合
	 * @throws IOException
	 */
	public void testTrimBaseWhiteSpace() throws IOException {
		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(""));
		int d = in.read(charArray,2,20);
		
		assertEquals("ABCDEFGHIJKLMNOPQR\nSTUVWXYZ",String.valueOf(charArray));
		assertEquals(0,d);
	}

	/**
	 * 引数の文字の配列が空白の場合
	 * @throws IOException
	 */
	public void testTrimArgCbufWhiteSpace() throws IOException {
		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(str));
		charArray = "                                 ".toCharArray();
		int d = in.read(charArray,2,20);
		assertEquals("  0246810                        ",String.valueOf(charArray));
		assertEquals(7,d);
	}

	/**
	 * 入力文字ストリームと引数の文字の配列が空白の場合
	 * @throws IOException
	 */
	public void testTrimBaseArgCbufWhiteSpace() throws IOException {
		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(""));
		charArray = "                                 ".toCharArray();
		int d = in.read(charArray,2,20);
		
		assertEquals("                                 ",String.valueOf(charArray));
		assertEquals(0,d);
	}

	/**
	 * len が空白を含まない読み込み文字数よりも大きい場合
	 * @throws IOException
	 */
	public void testTrimArgCbufOffLen() throws IOException {
		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(str));
		int d = in.read(charArray,2,20);
		
		assertEquals("AB0246810JKLMNOPQR\nSTUVWXYZ",String.valueOf(charArray));
		assertEquals(7,d);

	}

	/**
	 * len が空白を含まない読み込み文字数と等しい場合
	 * @throws IOException
	 */
	public void testTrimArgCbufOffLenSame() throws IOException {
		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(str));
		int d = in.read(charArray,20,7);
		
		assertEquals("ABCDEFGHIJKLMNOPQR\nS0246810",String.valueOf(charArray));
		assertEquals(7,d);

	}

	/**
	 * len が空白を含まない読み込み文字数よりも小さい場合
	 * @throws IOException
	 */
	public void testTrimArgCbufOffLenlittle() throws IOException {
		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(str));
		int d = in.read(charArray,0,3);
		
		assertEquals("024DEFGHIJKLMNOPQR\nSTUVWXYZ",String.valueOf(charArray));
		assertEquals(3,d);
	}

	/**
	 * 指定された文字の配列がnullの場合に NullPointerException を throw します。
	 * 
	 * @throws IOException
	 * @throws NullPointerException
	 */

	public void testTrimNullPointerException()
		throws IOException, NullPointerException{

		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(str));
		charArray = null;
		
		try {
			int d = in.read(charArray,0,0);
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
		TrimmedFilterReader in = new TrimmedFilterReader(new StringReader(str));
		
		// off < 0
		try {
			int d = in.read(charArray,-1,2);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}

		// off > cbuf.length
		try {
			int d = in.read(charArray,30,1);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}

		// len < 0
		try {
			int d = in.read(charArray,0,-1);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}

		// off + len > cbuf.length
		try {
			int d = in.read(charArray,26,2);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
			d = in.read(charArray,0,28);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}

		// off + len < 0
		try {
			int d = in.read(charArray,0,-1);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
			d = in.read(charArray,-1,0);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
			d = in.read(charArray,-1,-1);
			fail("IndexOutOfBoundsExceptionが正しく発生しませんでした。");
		}
		catch(IndexOutOfBoundsException ex){			
		}
	}

}
