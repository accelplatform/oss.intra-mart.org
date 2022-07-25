package org.intra_mart.common.aid.jdk.java.io;

import java.io.*;

/**
 * このクラスは、URL エンコードされたデータを書き込むための
 * ストリームフィルタを実装します。<BR>
 * この出力ストリームに書き込まれたバイトは、URL エンコードされ
 * 基礎出力ストリームに書き込まれます。<BR>
 *
 * @see java.io.OutputStream
 */
public class URLEncodeOutputStream extends FilterOutputStream{
	// クラス変数
	private static final byte[] HEX_ELEMENTS = ("0123456789abcdef").getBytes();

	/**
	 * 指定された OutputStream に書き込む BASE64デコーダーを作成します。<P>
	 *
	 * @param out 出力ストリーム
	 */
	public URLEncodeOutputStream(OutputStream out){
		super(out);
	}

	/**
	 * バイトをデコードして出力ストリームに書き込みます。<P>
	 *
	 * このメソッドは、デコード結果が出力可能な 1 バイトのデータになるまで、
	 * バッファに溜め込みます。
	 * バッファに溜め込まれたデータは、flush メソッドや close メソッドを
	 * 実行しても基礎出力ストリームに書き出されることはありません。
	 * バッファにデータが溜まった状態で、flush メソッドや close メソッドを
	 * 実行した場合、バッファ内のデータは破棄されます。
	 *
	 * @param b 書き込まれるバイト
	 * @exception ArithmeticException デコード中に計算結果がオーバーフローしてしまった場合
	 * @exception IllegalArgumentException バイト b が 0 より小さいか 0x3f よりも大きい場合
	 * @exception IOException 入出力エラーが発生した場合。
	 *                        特に、出力ストリームが閉じられている場合に
	 *                        IOException がスローされる
	 */
	public synchronized void write(int b) throws IOException{
		writeByte(b);
	}

	/**
	 * 指定のバイト b をデコードしたバイトを基礎出力ストリームに書き込みます。
	 * @param b 書き込まれるバイト
	 * @exception ArithmeticException デコード中に計算結果がオーバーフローしてしまった場合
	 * @exception IOException 入出力エラーが発生した場合
	 */
	private void writeByte(int b) throws ArithmeticException, IOException{
		if(b == 0x20){
			// 半角スペース発見！！
			this.out.write(0x2b);		// '+' 記号へのエンコード
		}
		else if(b <= 0x29 || b == 0x2b || b == 0x2c || b == 0x2f || (b >= 0x3a && b <= 0x3f) || (b >= 0x5b && b <= 0x5e) || b == 0x60 || b >= 0x7b){
				this.out.write(0x25);		// ヘッダ文字 '%' の出力

				// 16 進数へのエンコード
				this.out.write(HEX_ELEMENTS[(b >> 4) & 0x0f]);
				this.out.write(HEX_ELEMENTS[b & 0x0f]);
		}
		else{
				this.out.write(b);			// スルー処理
		}
	}

	/**
	 * バイト列をエンコード出力ストリームに書き込みます。<P>
	 *
	 * オフセット off から始まる指定のバイト配列からこの出力ストリームに
	 * len バイトを書き込みます。配列 b 内の一定のバイトが出力ストリームに
	 * 順番に書き込まれます。この処理で最初に書き込まれるバイトは要素 b[off]、
	 * 最後に書き込まれるバイトは要素 b[off+len-1] です。<P>
	 * b が null の場合は、NullPointerException がスローされます。<P>
	 * off が負の場合、len が負の場合、あるいは off+len が配列 b の長さより
	 * 大きい場合は、IndexOutOfBoundsException がスローされます。<BR>
	 * このメソッドは、配列 b 内の off から len バイトについて、
	 * int 型引数をひとつだけとる write メソッドを呼び出します。
	 *
	 * @param b データ
	 * @param off データの開始オフセット
	 * @param len 書き込むバイト数
	 * @exception ArithmeticException デコード中に計算結果がオーバーフローしてしまった場合
	 * @exception IllegalArgumentException バイト b が 0 より小さいか 0x3f よりも大きい場合
	 * @exception IOException 入出力エラーが発生した場合。
	 *                        特に、出力ストリームが閉じられている場合に
	 *                        IOException がスローされる
	 */
	public synchronized void write(byte[] b, int off, int len) throws IOException{
		while(0 <= --len){ writeByte(b[off++]); }
	}
}


/* End of File */