package org.intra_mart.common.aid.jdk.java.io;

import java.io.*;

/**
 * このクラスは、16進数表現されたデータを読み込むための
 * ストリームフィルタを実装します。<BR>
 * 入力ストリームより読み込まれたバイトは、16進数表現からバイトに変換され
 * read() メソッドにより返されます。<BR>
 *
 * @see java.io.InputStream
 */
public class HexInputStream extends InputStream{
	/**
	 * フィルタ処理される入力ストリームです。<P>
	 */
	protected InputStream in;

	/**
	 * 指定された InputStream から読み込む HexInputStream を作成します。<P>
	 *
	 * @param in 入力ストリーム
	 */
	public HexInputStream(InputStream in){
		super();
		this.in = in;
	}

	private void ensureOpen() throws IOException {
		if(in == null){ throw new IOException("Stream closed"); }
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
		ensureOpen();		// 準備ＯＫ？
		int upper = in.read();
		if(upper != -1){
			int b = (Character.digit((char) upper, 16) << 4) | Character.digit((char) in.read(), 16);
			if(b >= 0){ return b; }
			throw new NumberFormatException("The unsuitable character was read.");
		}
		else{
			return -1;
		}
	}

	/**
	 * ストリームを閉じます。<P>
	 *
	 * 入力ストリームを閉じ、これに関連するすべての
	 * システムリソースを解放します。
	 *
	 * @exception IOException 入出力エラーが発生した場合
	 */
	public synchronized void close() throws IOException {
		if(in == null){ return; }
		in.close();
		in = null;
    }
}


/* End of File */