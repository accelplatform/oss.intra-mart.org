package org.intra_mart.jssp.exception;

/**
 * JavaScript 実行中のintra-martプログラムへのリダイレクト要求の例外
 */
public class JavaScriptBatonPassException extends JSSPTransitionalException{

	private String page;	// リダイレクト先プログラム
	private Object args;	// リダイレクト先への引数群

	/**
	 * @param targetProgram リダイレクト先ＵＲＬ
	 * @param params 遷移先プログラム実行時の引数
	 */
	public JavaScriptBatonPassException(String targetProgram, Object params){
		super();
		page = targetProgram;
		args = params;
	}


	/**
	 * リダイレクト先コンテンツパスの取得メソッド
	 * @return 処理遷移先コンテンツのパス
	 */
	public String getPath(){
		return page;
	}

	
	/**
	 * リダイレクト先への引数の取得メソッド
	 * @return 引数オブジェクト
	 */
	public Object getArgument(){
		return args;
	}
}
