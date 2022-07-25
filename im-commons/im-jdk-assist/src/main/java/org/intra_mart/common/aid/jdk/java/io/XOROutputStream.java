package org.intra_mart.common.aid.jdk.java.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * このクラスは、指定のバイトデータとの排他論理和を計算した結果の
 * バイトデータを書き込むためのストリームフィルタを実装します。<BR>
 * この出力ストリームに書き込まれたバイトは、指定のバイトデータとの
 * 排他論理和を計算して、その結果のバイトデータが
 * 基礎出力ストリームに書き込まれます。<BR>
 * フィルタリングされた結果、基礎ストリームに書き込まれるデータは、
 * 同じバイトデータによって排他論理和を取ることにより、
 * もとのバイトデータに戻すことができます。
 *
 * @see java.io.OutputStream
 */
public class XOROutputStream extends FilterOutputStream{
	/**
	 *
	 */
	private int xorByte;

	/**
	 * 指定された OutputStream に対して
	 * b との排他論理和を取ったバイトデータを書き込む
	 * XOROutputStream を作成します。<P>
	 *
	 * @param out 出力ストリーム
	 * @param b 排他論理和を取るためのバイト
	 */
	public XOROutputStream(OutputStream out, int b){
		super(out);
		this.xorByte = b;
	}

	/**
	 * バイトをエンコード出力ストリームに書き込みます。<P>
	 *
	 * @param b 書き込まれるバイト
	 * @exception IOException 入出力エラーが発生した場合。
	 *                        特に、出力ストリームが閉じられている場合に
	 *                        IOException がスローされる
	 */
	public synchronized void write(int b) throws IOException{
		this.out.write(b ^ this.xorByte);
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
		byte[] strm = new byte[len];
		for(int idx = 0; idx < len; idx++){
			strm[idx] = (byte) (b[off++] ^ this.xorByte);
		}
		this.out.write(strm);
	}
}


/* End of File */