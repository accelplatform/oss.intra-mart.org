package org.intra_mart.common.aid.jdk.java.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * このクラスは、BASE64 エンコードされたデータを読み込むための
 * ストリームフィルタを実装します。<BR>
 * 入力ストリームより読み込まれたバイトは、BASE64 デコードされ
 * read() メソッドにより返されます。<BR>
 *
 * @see java.io.InputStream
 */
public class BASE64DecodeInputStream extends FilterInputStream{
	// クラス変数
	private static final String B_ELEMENTS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	// インスタンス変数
	private int strm = 0;
	private int surplus = 0;
	private boolean ready = true;

	/**
	 * 指定された InputStream から読み込む BASE64 デコード入力ストリームを作成します。<P>
	 *
	 * @param in 入力ストリーム
	 */
	public BASE64DecodeInputStream(InputStream in){
		super(in);
	}

	/**
	 * デコードデータのバイトを読み込みます。<P>
	 *
	 * このメソッドは、BASE64 のデコードに十分な入力が利用できるようになるまで
	 * ブロックします。
	 *
	 * @return 読み込まれたバイト。圧縮された入力の最後に達した場合は -1
	 * @exception IOException 入出力エラーが発生した場合
	 */
	public synchronized int read() throws IOException{
		if(ready){
			while(surplus < 8){
				int piece = in.read();
				if(piece == -1){
					// ストリームの終わりを検知
					ready = false;
					if(surplus == 0){
						return -1;
					}
					else{
						throw new IOException("The end of a stream was detected to inside with data input.");
					}
				}
				if(piece == (int) '='){
					ready = false;
					switch(surplus){
						case 4:
							// もう一つ '=' を取得
							if(in.read() != (int) '='){
								throw new IOException("An inaccurate character");
							}
						case 2:
							// 終端コードを入力したので終了コードを返却
							return -1;
						case 0:
						case 6:
						default:
							throw new IOException("An inaccurate character");
					}
				}
				else{
					strm = (strm << 6) | B_ELEMENTS.indexOf(piece);
					if(strm < 0){
						ready = false;
						if(piece >= 0x0020 && piece <= 0x007e){
							throw new IOException("An inaccurate character: " + piece + "/" + Character.toString((char) piece));
						}
						else{
							throw new IOException("An inaccurate character: " + piece);
						}
					}
					surplus += 6;
				}
			}
			surplus -= 8;
			int result = strm >> surplus;
			strm ^= result << surplus;
			return result;
		}
		else{
			return -1;
		}
	}

	/**
	 * 入力ストリームからバイト配列に最大 len バイトのデータを読み込みます。
	 * このメソッドは入力データが読み込み可能になるまでブロックします。
	 * @param b データが読み込まれるバッファ
	 * @param off データの開始オフセット
	 * @param len 読み込むバイトの最大値
	 * @return バッファに読み込まれたバイト数。ストリームの終わりに達して読み込むデータがない場合は -1
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized int read(byte[] b, int off, int len) throws IOException{
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)){
			throw new IndexOutOfBoundsException("Read size error: " + String.valueOf(len));
		}

		for(int idx = len; 0 < idx; idx--){
			int data = read();
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