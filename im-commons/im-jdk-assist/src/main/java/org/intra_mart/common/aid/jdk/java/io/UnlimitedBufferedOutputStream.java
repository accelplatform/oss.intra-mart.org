package org.intra_mart.common.aid.jdk.java.io;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 出力データを一時的にバッファリングする出力ストリームの実装です。<p>
 * このクラスは、
 * {@link java.io.BufferedOutputStream} と異なり、
 * {@link #flush()} メソッドが呼び出されるまで、
 * すべてのバイトをメモリ中にバッファリングします。
 * したがって、明示的にフラッシュしなければ、データが出力される事は
 * ありません。
 * 逆に言うと、明示的にフラッシュするまですべてのデータをメモリ中に
 * 保存する事になるため、大きなデータを扱う場合には十分な注意が必要です。
 *
 */
public class UnlimitedBufferedOutputStream extends FilterOutputStream{
	private ByteArrayOutputStream pool = new ByteArrayOutputStream();

	/**
	 * 出力データをバッファリングするためのフィルター出力ストリームを
	 * 新しく作成します。
	 * @param out 出力先ストリーム
	 */
	public UnlimitedBufferedOutputStream(OutputStream out){
		super(out);
	}

	/**
	 * 指定のバイトを出力します。<p>
	 * バイトは、{@link #flush()}メソッドが呼び出されるまで、
	 * オブジェクト内部のバッファに溜め込まれます。
	 * バイトを基本となるストリームに書き込むためには、{@link #flush()}メソッドを呼び出してください。
	 * @param b バイト
	 * @throws IOException 入出力エラー
	 */
	public synchronized void write(int b) throws IOException{
		this.pool.write(b);
	}

	/**
	 * 指定された byte 配列の、オフセット位置 off から始まる len バイトを出力ストリームに書き込みます。<p>
	 * バイトは、{@link #flush()}メソッドが呼び出されるまで、
	 * オブジェクト内部のバッファに溜め込まれます。
	 * バイトを基本となるストリームに書き込むためには、{@link #flush()}メソッドを呼び出してください。
	 * @param b データ
	 * @param off データの開始オフセット
	 * @param len 書き込むバイト数
	 * @throws IOException 入出力エラー
	 */
	public synchronized void write(byte[] b, int off, int len) throws IOException{
		this.pool.write(b, off, len);
	}

	/**
	 * b.length バイトのデータを出力ストリームに書き込みます。<p>
	 * バイトは、{@link #flush()}メソッドが呼び出されるまで、
	 * オブジェクト内部のバッファに溜め込まれます。
	 * バイトを基本となるストリームに書き込むためには、{@link #flush()}メソッドを呼び出してください。
	 * @param b データ
	 * @throws IOException 入出力エラー
	 */
	public synchronized void write(byte[] b) throws IOException{
		this.pool.write(b);
	}

	/**
	 * この出力ストリームをフラッシュし、
	 * バッファに入っている出力バイトをすべて強制的に書き込みます。
	 * @throws IOException 入出力エラー
	 */
	public synchronized void flush() throws IOException{
		try{
			this.pool.writeTo(this.out);
			this.out.flush();
		}
		finally{
			this.pool.reset();
		}
	}
}

