package org.intra_mart.common.aid.jdk.java.io;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * このクラスは、URL デコードされたデータを読み込むための
 * ストリームフィルタを実装します。<BR>
 * この入力ストリームに読み込まれたバイトは、URL デコードされ
 * 基礎入力ストリームに読み込まれます。<BR>
 *
 */
public class URLDecodeInputStream extends FilterInputStream{
	/**
	 * 指定された InputStream から URL デコードしながらバイトを読み込む
	 * 入力ストリームを作成します。<P>
	 *
	 * @param in 入力ストリーム
	 */
	public URLDecodeInputStream(InputStream in){
		super(in);
	}

	/**
	 * デコードデータのバイトを読み込みます。<P>
	 *
	 * このメソッドは、BASE64 のデコードに十分な入力が利用できるようになるまで
	 * ブロックします。
	 *
	 * @return 読み込まれたバイト。圧縮された入力の最後に達した場合は -1
	 * @throws EOFException デコード途中で入力が終了した場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized int read() throws IOException{
		int chr = this.in.read();
		if(chr != '%'){
			if(chr != '+'){
				return chr;
			}
			else{
				return 0x20;
			}
		}
		else{
			int highByte = this.in.read();
			if(highByte == '%'){
				return '%';
			}
			else{
				if(highByte != -1){
					int lowByte = this.in.read();
					if(lowByte != -1){
						return (Character.digit((char) highByte, 16) << 4) | Character.digit((char) lowByte, 16);
					}
				}
				throw new EOFException("The input was completed while decoding had been imperfect.");
			}
		}
	}

	/**
	 * 入力ストリームからバイト配列に最大 len バイトのデータを読み込みます。
	 * このメソッドは入力データが読み込み可能になるまでブロックします。
	 * @param b データが読み込まれるバッファ
	 * @param off データの開始オフセット
	 * @param len 読み込むバイトの最大値
	 * @return バッファに読み込まれたバイト数。ストリームの終わりに達して読み込むデータがない場合は -1
	 * @throws EOFException デコード途中で入力が終了した場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized int read(byte[] b, int off, int len) throws IOException{
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)){
			throw new IndexOutOfBoundsException("Read size error: " + String.valueOf(len));
		}

		for(int idx = len; 0 < idx; idx--){
			int data = this.read();
			if(data == -1){
				if(idx != len){ return len - idx; } else{ return -1; }
			}
			else{
				b[off++] = (byte) data;
			}
		}
		return len;
	}
}


/* End of File */