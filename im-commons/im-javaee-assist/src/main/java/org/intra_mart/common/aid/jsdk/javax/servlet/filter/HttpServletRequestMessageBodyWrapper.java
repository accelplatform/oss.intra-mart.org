package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest を拡張したインタフェースです。
 * 
 */
public interface HttpServletRequestMessageBodyWrapper extends HttpServletRequest{
	/**
	 * リクエストのメッセージボディを返します。<p>
	 * このメソッドは、{@link javax.servlet.ServletRequest#getInputStream() }
	 * または {@link javax.servlet.ServletRequest#getReader() } を
	 * 実行した後でも、メッセージボディを含む入力ストリームを返します。<br>
	 * また同様に、
	 * {@link javax.servlet.ServletRequest#getParameter(String name) }
	 * を実行した後でも、メッセージボディを含む入力ストリームを返します。
	 * @return メッセージボディを含む入力ストリーム
	 * @throws IOException 入出力の例外が発生した場合
	 */
	public InputStream getMessageBody() throws IOException;

	/**
	 * パラメータ情報を返します。
	 * @return パラメータ
	 * @throws IOException 入出力エラー
	 */
	public RequestParameter[] getRequestParameters() throws IOException;
}

