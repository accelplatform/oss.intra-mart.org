package org.intra_mart.common.aid.jdk.util.report;

import java.io.IOException;

/**
 * メッセージ出力用のスケルトンクラスです。<p>
 * サブクラスでは、report メソッドをオーバーライドして、
 * 任意の出力先にメッセージを出力する機能を実装する必要があります。
 *
 */
public abstract class AbstractMessageReporter implements MessageReporter{
	/**
	 * 唯一のコンストラクタ。
	 */
	protected AbstractMessageReporter(){
		super();
	}

	/**
	 * メッセージを出力します。
	 * @param message メッセージ
	 */
	public abstract void report(String message);

	/**
	 * data をメッセージとして出力します。<br>
	 * このメソッドは、以下の呼出と同じです。
	 * <p>report(String.valueOf(data))<p>
	 * @param data データ
	 */
	public void report(Object data){
		this.report(String.valueOf(data));
	}

	/**
	 * このオブジェクトを閉じます。
	 * オブジェクトが閉じられると、report メソッドは何もしなくなります。
	 * 必要に応じてサブクラスでオーバーライドしてください。
	 * @throws IOException 入出力エラー
	 */
	public void close() throws IOException{
		return;
	}

	/**
	 * このオブジェクトのバッファの情報をすべて出力します。
	 * このメソッドは、何も行いません。
	 * 必要に応じてサブクラスでオーバーライドしてください。
	 * @throws IOException 入出力エラー
	 */
	public void flush() throws IOException{
		return;
	}
}
