package org.intra_mart.common.aid.jdk.java.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * このクラスは、URL デコードされたデータを書き込むための
 * ストリームフィルタを実装します。<BR>
 * この出力ストリームに書き込まれたバイトは、URL デコードされ
 * 基礎出力ストリームに書き込まれます。<BR>
 *
 * @see java.io.OutputStream
 */
public class URLDecodeOutputStream extends FilterOutputStream{
	// インスタンス変数
	private int strm = 0;
	private int indexPoint = 0;
	private boolean nonFiltering = true;

	/**
	 * 指定された OutputStream に書き込む URLデコーダーを作成します。<P>
	 *
	 * @param out 出力ストリーム
	 */
	public URLDecodeOutputStream(OutputStream out){
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
	private synchronized void writeByte(int b) throws ArithmeticException, IOException{
		if(nonFiltering){
			// 通常処理
			switch(b){
				case 0x2b:						// '+' 記号発見！！
					this.out.write(0x20);		// 半角スペースへのデコード
					break;
				case 0x25:						// '%' 記号発見！！
					nonFiltering = false;		// フィルタ処理の開始
					break;
				default:
					this.out.write(b);			// スルー処理
			}
		}
		else{
			// エンコードデータのデコード中
			switch(indexPoint++){
				case 0:							// 1 バイト目
					strm = Character.digit((char) b, 16) << 4;
					break;
				case 1:							// 2 バイト目
					nonFiltering = true;
					indexPoint = 0;
					this.out.write(strm | Character.digit((char) b, 16));
					break;
				default:						// これはエラー
					throw new ArithmeticException("URL decode error.");
			}
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