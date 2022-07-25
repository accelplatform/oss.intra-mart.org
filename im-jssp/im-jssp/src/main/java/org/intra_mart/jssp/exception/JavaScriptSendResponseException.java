package org.intra_mart.jssp.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * JavaScript 任意ソースのレスポンス要求の例外
 */
public abstract class JavaScriptSendResponseException extends JSSPTransitionalException{

	private char[] chars;	// 送信ソース

	/**
	 * JavaScript の現在の実行を中断して指定のソースをメッセージボディ部として
	 * レスポンスするための例外を新しく作成します。
	 * @param str メッセージボディ部として送信するデータ
	 */
	public JavaScriptSendResponseException(String str){
		super();
		if(str != null){
			this.chars = str.toCharArray();
		}
		else{
			throw new NullPointerException("HTTP response source is empty.");
		}
	}

	/**
	 * レスポンスするデータを返します。
	 * @return コンストラクタに指定されたデータ
	 */
	public char[] getSource(){
		return this.chars;
	}

	/**
	 * 送信します。
	 * @param resp レスポンス
	 * @throws IOException 入出力エラー
	 */
	public abstract void send(HttpServletResponse resp) throws IOException;
}
