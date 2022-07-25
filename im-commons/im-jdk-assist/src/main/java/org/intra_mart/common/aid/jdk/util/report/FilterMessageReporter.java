package org.intra_mart.common.aid.jdk.util.report;

import java.io.IOException;

/**
 * メッセージ出力ストリームをフィルタ処理するクラスのスーパークラスです。<p>
 * 基礎出力ストリームにメッセージを出力する前にメッセージを加工したりする
 * 追加機能を提供するクラスを作成するために利用することができます。<p>
 * このクラスは、単に MessageReporter の全てのメソッドを
 * オーバーライドし、各要求を基礎出力ストリームに渡す機能を提供しています。
 * サブクラスでは、メソッドを追加したり、必要に応じてメソッドをオーバーライド
 * することがあります。
 *
 */
public class FilterMessageReporter implements MessageReporter{
	/**
	 * 基礎出力ストリームです。
	 */
	protected MessageReporter out;

	/**
	 * 指定された基礎出力ストリームにメッセージを書き込む
	 * フィルターオブジェクトを作成します。
	 * @param out 基礎出力リポーター
	 */
	public FilterMessageReporter(MessageReporter out){
		super();
		this.out = out;
	}

	/**
	 * 基礎出力ストリームに対してメッセージを出力します。<br>
	 * このメソッドは、単純に message を基礎出力ストリームに渡します。
	 * サブクラスで、このメソッドをオーバーライドすることにより、
	 * データの加工などのフィルタリング処理を追加することができます。
	 * @param message メッセージ
	 */
	public synchronized void report(String message){
		this.out.report(message);
	}

	/**
	 * 基礎出力ストリームに対してメッセージを出力します。<br>
	 * このメソッドは、単純に data を基礎出力ストリームに渡します。
	 * サブクラスで、このメソッドをオーバーライドすることにより、
	 * データの加工などのフィルタリング処理を追加することができます。
	 * @param data データ
	 */
	public synchronized void report(Object data){
		this.out.report(data);
	}

	/**
	 * このオブジェクトを閉じます。
	 * オブジェクトが閉じられると、report メソッドは何もしなくなります。
	 * @throws IOException 入出力エラー
	 */
	public synchronized void close() throws IOException{
		try{
			this.flush();								// バッファの出力
		}
		catch(IOException ioe){
		}
		this.out.close();								// 基礎出力の閉鎖
	}

	/**
	 * このオブジェクトのバッファの情報をすべて出力します。
	 * このメソッドでは、基礎出力リポートオブジェクトも同時にフラッシュします。
	 * @throws IOException 入出力エラー
	 */
	public synchronized void flush() throws IOException{
		this.out.flush();
	}
}
