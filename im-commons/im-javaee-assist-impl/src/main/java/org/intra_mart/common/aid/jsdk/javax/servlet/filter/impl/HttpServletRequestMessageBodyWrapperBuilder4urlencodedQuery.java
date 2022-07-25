package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletRequestMessageBodyWrapper;


/**
 * HttpServletRequest を拡張したインタフェースです。
 * 
 */
public class HttpServletRequestMessageBodyWrapperBuilder4urlencodedQuery extends AbstractHttpServletRequestMessageBodyWrapperBuilder{
	/**
	 * インスタンス化のためのゼロパラメータコンストラクタ。
	 */
	public HttpServletRequestMessageBodyWrapperBuilder4urlencodedQuery(){
		super();
	}

	/**
	 * ExtendedHttpServletRequest を作成して返します。<p>
	 * @param request リクエスト
	 * @throws IOException 入出力エラー
	 * @throws ServletException その他の実行時エラー
	 */
	public HttpServletRequestMessageBodyWrapper buildExtendedHttpServletRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		boolean parentRequestParameter = Boolean.valueOf(this.getFilterConfig().getInitParameter("parent.request.parameter")).booleanValue();
		boolean parseQueryString = Boolean.valueOf(this.getFilterConfig().getInitParameter("parse.query.string")).booleanValue();
		
		return new HttpServletRequestMessageBodyWrapper4urlencodedQuery(request, parentRequestParameter,parseQueryString);
	}
}
