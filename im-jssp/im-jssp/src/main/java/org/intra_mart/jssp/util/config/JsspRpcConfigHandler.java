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
	
	/**
	 * 「JSON形式の文字列表現」と「JavaScriptオブジェクト」の変換処理を行うスクリプト(以下、Marshallerスクリプト)
	 * をキャッシュするか否かを返却します。<br>
	 * デフォルトは、true を返却します。<br>
	 * このメソッドの返却値が trueの場合、Marshallerスクリプトは、キャッシュされます。<br>
	 * 
	 * このメソッドの返却値が falseの場合、変換処理を行う度にMarshallerスクリプトを解釈しなおします。<br>
	 * （つまり、サーバ起動後も、Marshallerスクリプトの変更が反映されます。Marshallerスクリプトの開発時に有用です。）
	 * 
	 * @return Marshallerスクリプトをキャッシュする場合は true、
	 * 			それ以外は false を返却します。
	 */
	public boolean isCacheMarshallerScriptScope();
		
	/**
	 * JSSP-RPCを利用する際に必要なCSJSライブラリ「im_json.js」のパスを取得します。<br>
	 * 本メソッドで返却されるパスが "/" で始まる場合は、現在のコンテキストのルートに対する相対パスであると解釈します。<br>
	 * デフォルトは、「/csjs/im_json.js」を返却します。<br>
	 * 
	 * <!--このメソッドの返却値は、&lt;jsspRpc&gt;タグ生成時にあわせて作成される&lt;script&gt;タグのsrc属性に利用されます。-->
	 * 
	 * @return CSJSライブラリ「im_json.js」のパス
	 */
	public String getCsjsPath4ImJson();
	
	
	/**
	 * JSSP-RPCを利用する際に必要なCSJSライブラリ「im_ajax_request.js」のパスを取得します。<br>
	 * 本メソッドで返却されるパスが "/" で始まる場合は、現在のコンテキストのルートに対する相対パスであると解釈します。<br>
	 * デフォルトは、「/csjs/im_ajax_request.js」を返却します。<br>
	 * 
	 * <!--このメソッドの返却値は、&lt;jsspRpc&gt;タグ生成時にあわせて作成される&lt;script&gt;タグのsrc属性に利用されます。-->
	 * 
	 * @return CSJSライブラリ「im_ajax_request.js」のパス
	 */
	public String getCsjsPath4ImAjaxRequest();

	
	/**
	 * JSSP-RPCを利用する際に必要なCSJSライブラリ「im_jssp_rpc.js」のパスを取得します。<br>
	 * 本メソッドで返却されるパスが "/" で始まる場合は、現在のコンテキストのルートに対する相対パスであると解釈します。<br>
	 * デフォルトは、「/csjs/im_jssp_rpc.js」を返却します。<br>
	 * 
	 * <!--このメソッドの返却値は、&lt;jsspRpc&gt;タグ生成時にあわせて作成される&lt;script&gt;タグのsrc属性に利用されます。-->
	 * 
	 * @return CSJSライブラリ「im_jssp_rpc.js」のパス
	 */
	public String getCsjsPath4ImJsspRpc();

}

