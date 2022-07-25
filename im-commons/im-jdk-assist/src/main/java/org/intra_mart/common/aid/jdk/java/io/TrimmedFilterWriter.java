package org.intra_mart.common.aid.jdk.java.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * このクラスは、基礎となる java.io.Writer をフィルタリングした
 * Writer 機能を提供します。<p>
 * フィルタ処理として、
 * Java の基準に従った空白ではない文字を文字出力ストリームに書き込みます。
 * <br>
 * 
 */
public class TrimmedFilterWriter extends FilterWriter {
	
	/**
	 * Writer をフィルタリングする新しい TrimmedFilterWriter を作成します。<p>
	 * 
	 * @param writer 基本となる文字出力ストリームを提供する java.io.Writer オブジェクト
	 */
	public TrimmedFilterWriter(Writer writer) {
	    super(writer);
	}

	/**
	 * 文字列の一部を書き込みます。
	 * 
	 * オフセット off から始まる指定の文字列 str から、
	 * len 文字分を文字出力ストリームに書き込みます。<br>
	 * この処理は、<p>
	 * <code>char[] strToCharArray = str.toCharArray();</code> 
	 * 上の式のように文字列 str を新しい文字の配列 strToCharArray に変換して
	 * <code>write(strToCharArray, off, len)</code> を呼び出す場合とまったく同じです。<p>
	 * 指定した文字の配列 str が null の場合は、NullPointerException がスローされます。 <p>
	 * 
	 * @param str 書き込まれる文字列
	 * @param off 文字の読み込み開始オフセット
	 * @param len 書き込む文字数
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(String str, int off, int len) throws IOException{
		if (str == null) {
		    throw new NullPointerException("str is null");
		}
		char[] cbuf = str.toCharArray();
		this.write(cbuf,off,len);
	}

	/**
	 * 文字の配列の一部を書き込みます。<p>
	 * 
	 * オフセット off から始まる指定の文字の配列 cbuf から、
	 * len 文字分を文字出力ストリームに書き込みます。<br>
	 * この処理で最初に書き込まれる文字は要素 b[off] で、
	 * 最後に書き込まれる文字は要素 cbuf[off+len-1] です。<p>
	 * この処理では、書き込む文字ごとに
	 * {@link #write(int) write(int)} のメソッドを呼び出します。
	 * 指定した文字の配列 cbuf が null の場合は、NullPointerException がスローされます。 <p>
	 * off が負の場合、len が負の場合、あるいは off+len が配列 cbuf の長さより大きい場合は、
	 * IndexOutOfBoundsException がスローされます。
	 * 
	 * @param cbuf 書き込む文字のバッファ
	 * @param off 文字の読み込み開始オフセット
	 * @param len 書き込む文字数
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(char[] cbuf, int off, int len) throws IOException{
		
		if (cbuf == null) {
		    throw new NullPointerException("cbuf is null");
		} else if ((off < 0) || (off > cbuf.length) || (len < 0)
				|| ((off + len) > cbuf.length)
					|| ((off + len) < 0)) {
		    throw new IndexOutOfBoundsException("off is negative,"
		    	+ "or len is negative,"
		    	+ "or off+len is greater than the length of the array cbuf");
		} else if (len != 0) {

			for(int writeCount = off; 0 < len; ++writeCount){		
				this.write(cbuf[writeCount]);	// データの書き出し
				len--;
			}
		}

	}

	/**
	 * 指定された文字が Java 空白文字でない場合に
	 * 単一文字を文字出力ストリームに書き込みます。<p>
	 * 書き込まれる文字は、指定された整数値の下位 16 ビットに格納されます。
	 * 上位 16 ビットは無視されます。
	 * @param c 文字
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(int c) throws IOException {
		if(!Character.isWhitespace((char)c)){
			super.write(c);
		}
	}

}
// End of File.