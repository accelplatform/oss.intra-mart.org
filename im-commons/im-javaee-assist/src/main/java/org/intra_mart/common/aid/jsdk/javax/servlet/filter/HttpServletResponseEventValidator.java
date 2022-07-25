package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;



/**
 * レスポンスイベントが有効か無効かを判定するバリデータ。
 * このバリデータが無効と判定した場合、イベントリスナは呼び出されません。
 * 
 */
public interface HttpServletResponseEventValidator{
	/**
	 * この Validator を初期化します。
	 * @param config 初期化パラメータ
	 *
	 * @throws HttpServletResponseEventValidatorException 初期化に失敗した場合
	 */
	public void init(HttpServletResponseEventConfig config) throws HttpServletResponseEventValidatorException;

	/**
	 * イベントリスナを動作させるかどうかの意思決定をします。
	 * @param request リクエスト
	 * @param reqponse レスポンス
	 * @return イベントリスナを動作させる場合 true。そうでない場合 false。
	 * @throws HttpServletResponseEventValidatorException エラーが発生した場合
	 */
	public boolean isValid(HttpServletRequest request, ExtendedHttpServletResponse response) throws HttpServletResponseEventValidatorException;

}
