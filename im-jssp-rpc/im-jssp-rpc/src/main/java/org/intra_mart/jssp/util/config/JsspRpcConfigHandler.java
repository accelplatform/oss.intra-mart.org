package org.intra_mart.jssp.util.config;

import org.intra_mart.jssp.util.config.JSSPConfigHandler;

/**
 * JSSP-RPC実行環境に関するコンフィグファイルの設定値を取得するためのインターフェースです。
 */
public interface JsspRpcConfigHandler extends JSSPConfigHandler {
	
	/**
	 * JSSP-RPC通信時に付加するサフィックス（拡張子）を取得します。
	 * @return 拡張子
	 */
	public String getJsspRpcURISuffix();

	
	/**
	 * 「JSON形式の文字列表現」と「JavaScriptオブジェクト」の変換処理が記述されているページパスを取得します。
	 * 
	 * @return 「JSON形式の文字列表現」と「JavaScriptオブジェクト」の変換処理が記述されているページパス
	 */
	public String getMarshallerPagePath();
	
	
	/**
	 * 「JSON形式の文字列表現」を「JavaScriptオブジェクト」に変換する関数の名称を取得します。
	 * 
	 * @return 「JSON形式の文字列表現」を「JavaScriptオブジェクト」に変換する関数の名称
	 */
	public String getUnmarshallFunctionName();
	
	
	/**
	 * 「JavaScriptオブジェクト」を「JSON形式の文字列表現」に変換する関数の名称を取得します。
	 * 
	 * @return JavaScriptオブジェクトをJSON形式の文字列表現に変換する関数の名称
	 */
	public String getMarshallFunctionName();

	
	/**
	 * Functionオブジェクトの argumentsプロパティを、配列を表すJSON形式の文字列表現に変換する関数の名称を取得します。<br/>
	 * クライアントサイドJavaScrip用の設定です。<br/>
	 * 具体的には、&lt;IMART type="jsspRpc"&gt;で生成されるJavaScriptソース内で使用します。<br/>
	 * <br/>
	 * （本メソッドで返却値で特定される関数は、
	 * JSSP-RPCを用いてサーバサイドJavaScript のロジックを呼び出す際、可変長引数を利用可能にするために必要です。
	 * この関数を利用するには「Functionオブジェクトの argumentsプロパティ」を引数として渡す必要があります。）
	 * 
	 * @return Functionオブジェクトの argumentsプロパティを、配列を表すJSON形式の文字列表現に変換する関数の名称
	 */
	public String getMarshall4ArgumentsFunctionName();
	
	/**
	 * 「JSON形式の文字列表現」から「JavaScriptオブジェクト」への変換に失敗した場合に、
	 * UnmarshallException をスローするか否かを返却します。<br>
	 * デフォルトは、true を返却します。
	 * このメソッドの返却値が falseの場合、リクエストの内容が「JSON形式の文字列表現」でない場合でも、
	 * サーバロジックの実行可能がとなります。<br>
	 * （GETメソッドでもサーバロジックを実行可能にする場合は、falseに設定してください）
	 * 
	 * @return 変換失敗時に UnmarshallException をスローする場合は true、
	 * 			それ以外は false を返却します。
	 */
	public boolean isThrowUnmarshallException();
	
	
}
