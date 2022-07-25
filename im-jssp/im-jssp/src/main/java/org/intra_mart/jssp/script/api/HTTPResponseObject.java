package org.intra_mart.jssp.script.api;

import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.intra_mart.jssp.exception.JavaScriptSendResponseException;
import org.intra_mart.jssp.exception.JavaScriptSendResponseException4stream;
import org.intra_mart.jssp.exception.JavaScriptSendResponseException4string;
import org.mozilla.javascript.ScriptableObject;
/**
 * HTTPレスポンスオブジェクト。 <br/>
 * <br/>
 * クライアントへ応答を送信する際の処理を支援するオブジェクトです。 <br/>
 * {@link Web#getHTTPResponse}メソッドにて取得可能です。<br/>
 * 
 * @name HTTPResponse
 * @scope public
 */
public class HTTPResponseObject extends ScriptableObject implements Serializable{

	private static final ScriptableObject PROTOTYPE = new HTTPResponseObject();

	private HttpServletResponse httpServletResponse = null;

	/**
	 * プロトタイプ用のコンストラクタです。
	 */
	public HTTPResponseObject(){
		// 基本メソッドの登録
		try{
			String[] names = {
			                  "setContentType",
			                  "setContentLength",
			                  "setHeader",
			                  "sendMessageBody",
			                  "sendMessageBodyString"
			                  };
			this.defineFunctionProperties(names, HTTPResponseObject.class, ScriptableObject.DONTENUM);
		}
		catch(Exception e){
			throw new IllegalStateException("JavaScript-API initialize error: " + HTTPResponseObject.class.getName(), e);
		}
	}
	/**
	 * パラメータオブジェクトを新しく作成します。
	 * @param
	 */
	public HTTPResponseObject(HttpServletResponse response){
		super();
		this.httpServletResponse = response;

		// 基本メソッドの追加登録
		this.setPrototype(PROTOTYPE);
	}

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return 実行環境上での名称
	 */
	public String getClassName() {
		return "HTTPResponse";
	}

	/**
	 * レスポンスの Content-Type を設定します。<br/>
	 * 
     * @scope public
	 * @param type String コンテントタイプを指定する String
	 */
	public void setContentType(String type){
		this.httpServletResponse.setContentType(type);
	}

	/**
	 * レスポンスのメッセージボディ部分の長さをセットします。<br/>
	 * HTTP Servlet ではこのメソッドは HTTP ヘッダの<br/>
	 * Content-Length フィールドにセットします。<br/>
	 * 
     * @scope public
	 * @param len Number クライアントに送り返すメッセージボディの長さを指定する整数値。 HTTP の Content-Length ヘッダフィールドの値
	 */
	public void setContentLength(int len){
		this.httpServletResponse.setContentLength(len);
	}

	/**
	 * 指定された名前と値を持つレスポンスヘッダを設定します。<br/>
	 * このヘッダが既に設定されている場合は上書きされます。<br/>
	 * 
     * @scope public
	 * @param name String ヘッダの名称
	 * @param value String ヘッダの値
	 */
	public void setHeader(String name, String value){
		this.httpServletResponse.setHeader(name, value);
	}

	/**
	 * 指定のデータを送信します。<br/>
	 * JavaScript の現在の実行は中断します。再開する事はありません。<br/>
	 * 
     * @scope public
	 * @param strm String 送信データ
	 * @throws JavaScriptSendResponseException 正常に次の処理へ移れる場合
	 * @throws NullPointerException 引数が null だった場合
	 */
	public void sendMessageBody(String strm){
		throw new JavaScriptSendResponseException4stream(strm);
	}

	/**
	 * 指定のデータを送信します。<br/>
	 * JavaScript の現在の実行は中断します。再開する事はありません。<br/>
	 * 送信データは文字列として扱われ、ServletResponse#getWriter() が
	 * 返す Writer によって出力されます。したがって、文字コードは自動的に
	 * 自動的に変換されます。<br/>
	 * 
     * @scope public
	 * @param str String 送信データ
	 * @throws JavaScriptSendResponseException 正常に次の処理へ移れる場合
	 * @throws NullPointerException 引数が null だった場合
	 */
	public void sendMessageBodyString(String str){
		throw new JavaScriptSendResponseException4string(str);
	}
}
