package org.intra_mart.common.aid.jdk.java.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * このクラスは、BASE64 エンコードされたデータを書き込むための
 * ストリームフィルタを実装します。<BR>
 * この出力ストリームに書き込まれたバイトは、BASE64 エンコードされ
 * 基礎出力ストリームに書き込まれます。<BR>
 *
 * @see java.io.OutputStream
 */
public class BASE64EncodeOutputStream extends FilterOutputStream{
	// クラス変数
	private static final byte[] B_ELEMENTS = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/").getBytes();

	// インスタンス変数
	private boolean ready = true;
	private int strm = 0;
	private int surplus = 0;
	private byte[] buf = new byte[4];
	private int nextIndex = 0;

	/**
	 * 指定された OutputStream に書き込む BASE64 エンコード出力ストリームを作成します。<P>
	 *
	 * @param out 出力ストリーム
	 */
	public BASE64EncodeOutputStream(OutputStream out){
		super(out);
	}

	/**
	 * バイトをエンコード出力ストリームに書き込みます。<P>
	 *
	 * このメソッドは、3 バイトのデータを受け取るとエンコードされたバイトを
	 * 出力ストリームに書き込みます。入力されたバイトが 3 バイトに満たない
	 * 場合は、入力されたバイトが合計で 3 バイトになるまでバッファに
	 * 溜め込みます。
	 *
	 * @param b 書き込まれるバイト
	 * @exception IOException 入出力エラーが発生した場合。
	 *                        特に、出力ストリームが閉じられている場合に
	 *                        IOException がスローされる
	 */
	public synchronized void write(int b) throws IOException{
		strm = (strm << 8) | (b & 0x00ff);
		surplus += 8;
		while(surplus >= 6){
			surplus -= 6;
			int work = strm >> surplus;
			buf[nextIndex] = B_ELEMENTS[work];
			nextIndex = ++nextIndex & 0x03;
			if(nextIndex == 0){ out.write(buf); }
			strm ^= (work << surplus);
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
	 *
	 * @param b データ
	 * @param off データの開始オフセット
	 * @param len 書き込むバイト数
	 * @exception IOException 入出力エラーが発生した場合。
	 *                        特に、出力ストリームが閉じられている場合に
	 *                        IOException がスローされる
	 */
	public synchronized void write(byte[] b, int off, int len) throws IOException{
		while(0 <= --len){
			strm = (strm << 8) | (b[off++] & 0x00ff);
			surplus += 8;
			while(surplus >= 6){
				surplus -= 6;
				int work = strm >> surplus;
				buf[nextIndex] = B_ELEMENTS[work];
				nextIndex = ++nextIndex & 0x03;
				if(nextIndex == 0){ out.write(buf); }
				strm ^= (work << surplus);
			}
		}
	}

	/**
	 * 出力ストリームへのエンコードデータの書き込みを終了します。<P>
	 *
	 * このときストリームは閉じられません。
	 * 複数のフィルタを同じ出力ストリームに連続して適用するときに
	 * このメソッドを使用します。
	 *
	 * @exception IOException 入出力エラーが発生した場合
	 */
	public synchronized void finish() throws IOException{
		if(ready){
			if(surplus > 0){
				buf[3] = (byte) '=';
				buf[nextIndex] = B_ELEMENTS[strm << (6 - surplus)];
				if(nextIndex == 1){ buf[2] = (byte) '='; }
				strm = 0;
				surplus = 0;
				nextIndex = 0;
				out.write(buf);
			}
		}
		else{
			throw new IOException("Stream closed");
		}
	}

	/**
	 * ストリームを閉じます。<P>
	 *
	 * ストリームを閉じ、これに関連するすべての
	 * システムリソースを解放します。
	 * もし、このストリームがバッファ内に出力バイトを保持している場合、
	 * それらを終端処理をして出力ストリームに強制的に書き込みます。<br>
	 * このメソッドは、finish メソッドを呼び出した後、
	 * スーパークラス(java.io.FilterOutputStream)の
	 * close メソッドを呼び出します。
	 *
	 * @exception IOException 入出力エラーが発生した場合
	 */
	public synchronized void close() throws IOException{
		if(ready){
			finish();
			this.flush();
			ready = false;
		}
		super.close();
	}
}


/* End of File */