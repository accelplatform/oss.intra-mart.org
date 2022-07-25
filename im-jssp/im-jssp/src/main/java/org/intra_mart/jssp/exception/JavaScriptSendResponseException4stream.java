package org.intra_mart.jssp.exception;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * JavaScript 任意ソースのレスポンス要求の例外
 */
public class JavaScriptSendResponseException4stream extends JavaScriptSendResponseException{
	/**
	 * JavaScript の現在の実行を中断して指定のソースをメッセージボディ部として
	 * レスポンスするための例外を新しく作成します。
	 * @param str メッセージボディ部として送信するデータ
	 */
	public JavaScriptSendResponseException4stream(String str){
		super(str);
	}

	/**
	 * 送信します。
	 * @param resp レスポンス
	 * @throws IOException 入出力エラー
	 */
	public void send(HttpServletResponse resp) throws IOException{
		char[] src = this.getSource();

		// メモリの大量消費を防ぐために
		// src からバイト配列や char などはしない。
		OutputStream out = resp.getOutputStream();
		int len = src.length;
		for(int idx = 0; idx < len; idx++){
			out.write((int) src[idx]);
		}
		out.close();
	}
}
