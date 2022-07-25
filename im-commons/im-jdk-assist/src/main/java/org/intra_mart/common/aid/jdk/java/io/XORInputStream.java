package org.intra_mart.common.aid.jdk.java.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * このクラスは、指定されたバイトとの排他論理和計算された結果の
 * データを読み込むためのストリームフィルタを実装します。<BR>
 * 入力ストリームより読み込まれたバイトは、
 * 指定されたバイトデータとの排他論理和を計算した結果として
 * read() メソッドにより返されます。<BR>
 *
 * @see java.io.InputStream
 */
public class XORInputStream extends FilterInputStream{
	/**
	 *
	 */
	private int xorByte;

	/**
	 * 指定された InputStream から指定されたバイトとの排他論理和を
	 * 取りながら読み込む XORInputStream を作成します。<P>
	 *
	 * @param in 入力ストリーム
	 * @param b 排他論理和を取るためのバイト
	 */
	public XORInputStream(InputStream in, int b){
		super(in);
		this.xorByte = b;
	}

	/**
	 * デコードデータのバイトを読み込みます。<P>
	 *
	 * ２バイトの 16進数表現されたキャラクタデータを
	 * 読み込んで１バイトの数値として返します。
	 * このメソッドは、２バイトの入力があるまでブロックします。
	 *
	 * @return 読み込まれたバイト。
	 * @exception IOException 入出力エラーが発生した場合
	 * @exception NumberFormatException 16進数として有効ではない文字を読み込んだ場合
	 */
	public synchronized int read() throws IOException, NumberFormatException{
		int b = in.read();
		if(b != -1){
			return b ^ this.xorByte;
		}
		else{
			return -1;
		}
	}

	/**
	 * 入力ストリームからバイト配列へ最大 len バイトのデータを読み込みます。このメソッドは、入力が可能になるまでブロックします。
	 * @param b データの読み込み先のバッファ
	 * @param off データの開始オフセット
	 * @param len 読み込まれる最大バイト数
	 * @return バッファに読み込まれたバイトの合計数。ストリームの終わりに達してデータがない場合は -1
	 * @throws IOException 入出力エラー
	 */
	public synchronized int read(byte[] b, int off, int len) throws IOException{
		if(len > 0){
			int counter = 0;
			while(counter < len){
				int data = this.read();
				if(data != -1){
					b[off++] = (byte) data;
					counter++;
				}
				else{
					break;
				}
			}
			if(counter != 0){ return counter; } else{ return -1; }
		}
		else{
			throw new IllegalArgumentException("len is invalid.");
		}
	}
}


/* End of File */