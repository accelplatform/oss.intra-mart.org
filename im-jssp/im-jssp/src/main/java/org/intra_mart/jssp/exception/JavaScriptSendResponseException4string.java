package org.intra_mart.jssp.exception;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

/**
 * JavaScript 任意ソースのレスポンス要求の例外
 */
public class JavaScriptSendResponseException4string extends JavaScriptSendResponseException{
	/**
	 * JavaScript の現在の実行を中断して指定のソースをメッセージボディ部として
	 * レスポンスするための例外を新しく作成します。
	 * @param str メッセージボディ部として送信するデータ
	 */
	public JavaScriptSendResponseException4string(String str){
		super(str);
	}

	/**
	 * 送信します。
	 * @param resp レスポンス
	 * @throws IOException 入出力エラー
	 */
	public void send(HttpServletResponse resp) throws IOException{
		Writer out = resp.getWriter();
		out.write(this.getSource());
		out.close();
	}
}

/* End of File */