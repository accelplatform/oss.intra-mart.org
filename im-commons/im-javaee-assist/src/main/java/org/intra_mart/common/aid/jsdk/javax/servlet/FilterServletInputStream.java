package org.intra_mart.common.aid.jsdk.javax.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;


/**
 * InputStream を ServletInputStream に変換するためのラッパです。<p>
 * このクラスは、基礎ストリームの対応するメソッドを呼び出すだけです。
 * 各メソッドをオーバーライドして再定義することにより、
 * 入力をフィルタリングする事ができます。
 *
 */
public class FilterServletInputStream extends ServletInputStream{
	/**
	 * 基礎入力
	 */
	private InputStream in;

	/**
	 * 指定の入力を基礎入力とするサーブレット入力ストリームを作成します。
	 * @param in 入力ストリーム
	 */
	public FilterServletInputStream(InputStream in){
		super();
		if (in != null) {
			this.in = in;
		} else {
			throw new NullPointerException("in is null.");
		}
	}

	/**
	 * このオブジェクトがラップしている入力ストリームを返します。
	 * @return 入力ストリーム
	 */
	protected InputStream getInputStream(){
		return this.in;
	}

	/**
	 * 基礎入力から１バイト読み込みます。<p>
	 * このメソッドは、以下と同じです。<br>
	 * <code>
	 * getInputStream().read()
	 * </code>
	 * @return データの次のバイト。ストリームの終わりに達した場合は -1
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public int read() throws IOException{
		return this.getInputStream().read();
	}

	/**
	 * 基礎入力から指定のバイト配列にデータを読み込みます。<p>
	 * このメソッドは、以下と同じです。<br>
	 * <code>
	 * getInputStream().read(b)
	 * </code>
	 * @param b データの読み込み先のバッファ
	 * @return バッファに読み込まれたバイトの合計数。ストリームの終わりに達してデータがない場合は -1
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public int read(byte[] b) throws IOException{
		return this.getInputStream().read(b);
	}

	/**
	 * 基礎入力から指定のバイト配列にデータを読み込みます。<p>
	 * このメソッドは、以下と同じです。<br>
	 * <code>
	 * getInputStream().read(b, off, len)
	 * </code>
	 * @param b データの読み込み先のバッファ
	 * @param off データが書き込まれる配列 b の開始オフセット
	 * @param len 読み込む最大バイト数
	 * @return バッファに読み込まれたバイトの合計数。ストリームの終わりに達してデータがない場合は -1
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public int read(byte[] b, int off, int len) throws IOException{
		return this.getInputStream().read(b, off, len);
	}

	/**
	 * 指定のバイト数だけ入力をスキップします。<p>
	 * このメソッドは、以下と同じです。<br>
	 * <code>
	 * getInputStream().skip(n)
	 * </code>
	 * @param n スキップするバイト数
	 * @return 実際にスキップされたバイト数
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public long skip(long n) throws IOException{
		return this.getInputStream().skip(n);
	}

	/**
	 * この入力ストリームのメソッドの次の呼び出し側からブロックされることなく、
	 * この入力ストリームから読み込むことができる(またはスキップできる)バイト数
	 * を返します。<p>
	 * このメソッドは、以下と同じです。<br>
	 * <code>
	 * getInputStream().available()
	 * </code>
	 * @return ブロックしないで入力ストリームから読み込むことができるバイト数
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public int available() throws IOException{
		return this.getInputStream().available();
	}

	/**
	 * 入力を閉じます。<p>
	 * このメソッドは、以下と同じです。<br>
	 * <code>
	 * getInputStream().close()
	 * </code>
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void close() throws IOException{
		this.getInputStream().close();
	}

	/**
	 * 入力ストリームの現在位置にマークを設定します。<p>
	 * このメソッドは、以下と同じです。<br>
	 * <code>
	 * getInputStream().mark(readlimit)
	 * </code>
	 * @param readlimit マーク位置が無効になる前に読み込み可能なバイトの最大リミット
	 */
	public void mark(int readlimit){
		this.getInputStream().mark(readlimit);
	}

	/**
	 * このストリームの位置を、
	 * 入力ストリームで最後に mark メソッドが呼び出されたときのマーク位置に
	 * 再設定します。<p>
	 * このメソッドは、以下と同じです。<br>
	 * <code>
	 * getInputStream().reset()
	 * </code>
	 * @throws IOException ストリームにマークが設定されていなかった場合、またはマークが無効になっていた場合
	 */
	public void reset() throws IOException{
		this.getInputStream().reset();
	}

	/**
	 * 入力ストリームが mark および reset メソッドをサポートしているかどうかを
	 * 判定します。<p>
	 * このメソッドは、以下と同じです。<br>
	 * <code>
	 * getInputStream().markSupported()
	 * </code>
	 * @return このストリームインスタンスが mark および reset メソッドをサポートしている場合は true、サポートしていない場合は false
	 */
	public boolean markSupported(){
		return this.getInputStream().markSupported();
	}
}

