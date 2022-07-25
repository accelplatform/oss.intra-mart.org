package org.intra_mart.common.aid.jdk.java.io;

import java.io.*;

/**
 * このクラスは、16進数表現されたデータを書き込むための
 * ストリームフィルタを実装します。<BR>
 * この出力ストリームに書き込まれたバイトは、16進数表現され
 * 基礎出力ストリームに書き込まれます。<BR>
 * フィルタリングされた結果、基礎ストリームに書き込まれるデータは、
 * [0-9a-f] の範囲のキャラクタで構成されます。
 *
 * @see java.io.OutputStream
 */
public class HexOutputStream extends OutputStream{
	// クラス変数
	private static final byte[] HEX_ELEMENTS = ("0123456789abcdef").getBytes();

	/**
	 * フィルタ処理される基礎出力ストリームです。<P>
	 */
	protected OutputStream out;

	/**
	 * 指定された OutputStream に書き込む HexOutputStream を作成します。<P>
	 *
	 * @param out 出力ストリーム
	 */
	public HexOutputStream(OutputStream out){
		super();
		this.out = out;
	}

	/**
	 * バイトをエンコード出力ストリームに書き込みます。<P>
	 *
	 * このメソッドは、バイトを上位４ビットと下位ビットに分け、
	 * それぞれ16進数表現されたキャラクターコードに置き換えて、
	 * 計２バイトのデータを基礎ストリームに書き込みます。
	 *
	 * @param b 書き込まれるバイト
	 * @exception IOException 入出力エラーが発生した場合。
	 *                        特に、出力ストリームが閉じられている場合に
	 *                        IOException がスローされる
	 */
	public synchronized void write(int b) throws IOException{
		ensureOpen();		// 準備ＯＫ？
		out.write(HEX_ELEMENTS[(b >> 4) & 0x0f]);
		out.write(HEX_ELEMENTS[b & 0x0f]);
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
		ensureOpen();		// 準備ＯＫ？
		for(int idx = off; len-- > 0; idx++){
			out.write(HEX_ELEMENTS[(b[idx] >> 4) & 0x0f]);
			out.write(HEX_ELEMENTS[b[idx] & 0x0f]);
		}
	}

	/**
	 * ストリームを閉じます。<P>
	 *
	 * ストリームを閉じ、これに関連するすべての
	 * システムリソースを解放します。<BR>
	 * もし、このストリームがバッファ内に出力バイトを保持している場合、
	 * それらを終端処理をして出力ストリームに強制的に書き込みます。
	 *
	 * @exception IOException 入出力エラーが発生した場合
	 */
	public synchronized void close() throws IOException{
		if (out == null){ return; }
		flush();
		out.close();
		out = null;
	}

	// 基礎ストリームのチェック
	private void ensureOpen() throws IOException {
		if(out == null){ throw new IOException("Stream closed"); }
	}
}


/* End of File */