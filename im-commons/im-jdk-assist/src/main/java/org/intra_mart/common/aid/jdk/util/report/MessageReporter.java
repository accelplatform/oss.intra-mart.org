package org.intra_mart.common.aid.jdk.util.report;

import java.io.IOException;

/**
 * メッセージ出力のためのインターフェースです。<p>
 * このインターフェースを実装したクラスでは、report メソッドで
 * 指定されたメッセージを任意の出力先に対して出力する機能を提供します。<br>
 *
 */
public interface MessageReporter{

	/**
	 * メッセージを出力します。
	 * @param message メッセージ
	 */
	public void report(String message);

	/**
	 * data をメッセージとして出力します。
	 * @param data データ
	 */
	public void report(Object data);

	/**
	 * このオブジェクトを閉じます。
	 * オブジェクトが閉じられると、report メソッドは何もしなくなります。
	 * このメソッドは、任意のオペレーションです。
	 * @throws IOException 入出力エラー
	 */
	public void close() throws IOException;

	/**
	 * このオブジェクトのバッファの情報をすべて出力します。
	 * このメソッドは、任意のオペレーションです。
	 * @throws IOException 入出力エラー
	 */
	public void flush() throws IOException;
}
