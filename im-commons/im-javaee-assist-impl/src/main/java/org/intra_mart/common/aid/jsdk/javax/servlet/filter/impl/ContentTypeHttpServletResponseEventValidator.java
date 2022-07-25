package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import javax.mail.internet.ContentType;
import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventConfig;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventValidator;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletResponseEventValidatorException;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;



/**
 * 指定のコンテントタイプの時のみ有効判定をするバリデータ
 */
public class ContentTypeHttpServletResponseEventValidator implements HttpServletResponseEventValidator{
	private static final String CONF_ID_PRIMARY_TYPE = "primary-type";
	private static final String CONF_ID_SUB_TYPE = "sub-type";

	private String primaryType = null;
	private String subType = null;

	/**
	 *
	 */
	public ContentTypeHttpServletResponseEventValidator(){
		super();
	}

	/**
	 * この Validator を初期化します。
	 * @param config 初期化パラメータ
	 *
	 * @throws HttpServletResponseEventValidatorException 初期化に失敗した場合
	 */
	public void init(HttpServletResponseEventConfig config) throws HttpServletResponseEventValidatorException{
		this.primaryType = config.getInitParameter(CONF_ID_PRIMARY_TYPE);
		this.subType = config.getInitParameter(CONF_ID_SUB_TYPE);
	}

	/**
	 * イベントリスナを動作させるかどうかの意思決定をします。
	 * @param request リクエスト
	 * @param reqponse レスポンス
	 * @return イベントリスナを動作させる場合 true。そうでない場合 false。
	 * @throws HttpServletResponseEventValidatorException エラーが発生した場合
	 */
	public boolean isValid(HttpServletRequest request, ExtendedHttpServletResponse response) throws HttpServletResponseEventValidatorException{
		ContentType contentType = response.getContentTypeObject();
		if(contentType != null){
			if(this.primaryType.equals(contentType.getPrimaryType())){
				if(this.subType.equals(contentType.getSubType())){
					return true;
				}
			}
		}

		return false;
	}
}
