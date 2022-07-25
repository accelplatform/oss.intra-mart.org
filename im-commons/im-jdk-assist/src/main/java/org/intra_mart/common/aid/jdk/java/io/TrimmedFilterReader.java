package org.intra_mart.common.aid.jdk.java.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 * このクラスは、基礎となる java.io.Reader をフィルタリングした
 * Writer 機能を提供します。<p>
 * フィルタ処理として、
 * 文字入力ストリームから Java の基準に従った空白文字ではない文字を読み込みます。
 * <br>
 *
 */
public class TrimmedFilterReader extends FilterReader {

        /**
	 * Reader をフィルタリングする新しい TrimmedFilterReader を作成します。<p>
         * @param in 基本となるストリームを提供する Reader オブジェクト
         */
        public TrimmedFilterReader(Reader in) {
                super(in);
        }


	/**
	 * 単一の文字を読み込みます。<p>
	 * 文字入力ストリームから読み込んだ文字が Java の基準に従った空白文字である場合に、
	 * 次の文字を読み込みを行います。<p>
	 * 文字の値は 0 ～ 65535 (0x00-0xffff) の範囲の値をとる int として返されます。
	 * ストリームの終わりに達して読み込むデータがない場合は -1 を返します。<p>
	 * このメソッドは、入力データが読み込めるようになるか、
	 * ストリームの終わりが検出されるか、
	 * または例外がスローされるまでブロックします。
	 * 
	 * @return 0 ～ 65535 (0x00-0xffff) の範囲の整数としての、読み込まれた文字。ストリームの終わりに達した場合は -1
	 * @throws IOException 入出力エラーが発生した場合
	 * @see java.io.Reader#read() java.io.Reader#read()
	 */
	public int read() throws IOException {
		int c = super.read();
		if (Character.isWhitespace((char)c)){
			return this.read();
		}
		return c;
	}

	/**
	 * 配列の一部に文字を読み込みます。<p>
	 * 
	 * 入力ストリームから len 文字数分の文字を指定された文字の配列に格納します。
	 * 実際に読み込まれた文字数は整数として返されます。<p>
	 * このメソッドは、入力データが読み込めるようになるか、
	 * ファイルの終わりが検出されるか、
	 * あるいは例外がスローされるまでブロックします。<p>
	 * 文字の配列 cbuf が null の場合は、NullPointerException がスローされます。<p>
	 * off が負の場合、len が負の場合、あるいは off+len が配列 cbuf の長さより大きい場合は、
	 * IndexOutOfBoundsException がスローされます。<p>
	 * len が 0 の場合は、文字が読み込まれず、0 が返されます。
	 * そうでない場合は、Java の基準に従った空白文字でない
	 * 1 文字以上の読み込み処理が行われます。
	 * ストリームがファイルの終わりに達しているために読み込む文字がない場合は、
	 * 値 -1 が返されます。
	 * そうでない場合は、Java の基準に従った空白文字でない len 文字数分の文字が読み込まれ、
	 * 文字の配列 cbuf に格納されます。<p> 
	 * すべての場合において、文字 cbuf[0] ～ cbuf[off]
	 * および文字 cbuf[off+len] ～ cbuf[b.length-1] は影響を受けません。<p> 
	 * ファイルの終わりに達している以外の理由で最初のバイトが読み込めない場合は、
	 * IOException がスローされます。
	 * 特に、入力ストリームが閉じている場合には IOException がスローされます。<p> 
	 * このメソッドは、単純に read() メソッドを繰り返し呼び出します。
	 * 最初の呼び出しが IOException になる場合、
	 * その例外は呼び出し側から read(cbuf, off, len) メソッドに返されます。
	 * read() の以降の呼び出しが IOException になった場合、
	 * その例外はファイルの終わりが検出された場合と同じようにキャッチおよび処理されます。
	 * つまり、その時点までに読み込まれた文字は cbuf に格納され、
	 * 例外が発生するまでに読み込まれた文字数が返されます。
	 * 
	 * @param cbuf 転送先バッファ
	 * @param off 文字の格納開始オフセット
	 * @param len 読み込む文字の最大数
	 * @return 読み込まれた文字数。
	 * @see java.io.Reader#read(char[], int, int)
	 */
	public int read(char[] cbuf, int off, int len) throws IOException, NullPointerException{
		
		if (cbuf == null) {
		    throw new NullPointerException("cbuf is null");
		} else if ((off < 0) || (off > cbuf.length) || (len < 0)
				|| ((off + len) > cbuf.length)
					|| ((off + len) < 0)) {
		    throw new IndexOutOfBoundsException("off is negative,"
			+ "or len is negative,"
			+ "or off+len is greater than the length of the array cbuf");
		} else if (len == 0) {
		    return 0;
		}

		int c = -1;
		int charCount = 0;
		
		while(charCount < len) {			
			c = this.read();	// 文字の読み込み
			if (c == -1) {
				break;
			}
			cbuf[off+charCount] = (char)c; // 読み込んだ文字を格納
			charCount++;
		}
		return charCount;	// 読み込んだ文字数を返却
	}

}
