package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.exception.EmptyQueryException;
import org.intra_mart.common.aid.jsdk.javax.servlet.exception.ExcessiveQueryException;
import org.intra_mart.common.aid.jsdk.javax.servlet.exception.LengthRequiredException;
import org.intra_mart.common.aid.jsdk.javax.servlet.exception.UnripeQueryException;


/**
 * HttpServletRequestMessageBodyWrapper の抽象実装です。
 * このクラスは、リクエストのメッセージボディ部をメモリ内で保存します。
 */
public abstract class AbstractBufferedHttpServletRequestMessageBodyWrapper extends AbstractHttpServletRequestMessageBodyWrapper{
	/**
	 * 入力されたメッセージボディを保存するバイト配列
	 */
	private byte[] queryStream;

	/**
	 * 拡張されたリクエストを作成します。
	 * @param request サーブレットリクエスト
	 * @param parentRequestParameter 初期化パラメータ「parent.request.parameter」のboolean値
	 * @param parseQueryString 初期化パラメータ「parse.query.string」のboolean値
	 * @throws IOException
	 */
	public AbstractBufferedHttpServletRequestMessageBodyWrapper(HttpServletRequest request, 
																 boolean parentRequestParameter,
																 boolean parseQueryString)
				throws ServletException, IOException{
		
		super(request, 
			  parentRequestParameter,
			  parseQueryString);
		
		int max = request.getContentLength();
		if (max >= 0) {
			ServletInputStream sis = request.getInputStream();
			try {
				this.queryStream = new byte[max];
				while(max > 0){
					int len = sis.read(this.queryStream, this.queryStream.length - max, max);
					if(len != -1){
						max -= len;
					}
					else{
						// 入力データが予定よりも少ない
						if(max != this.queryStream.length){
							throw new UnripeQueryException("The input was completed while acquiring the request query: " + (this.queryStream.length - max) + "/" + this.queryStream.length);
						}
						else{
							throw new EmptyQueryException("Request query was unreceivable: 0/" + max);
						}
					}
				}
				// データ受信完了→超過データのチェック
				if(sis.read() != -1){
					// 受信データが多すぎる？
					int len = 1;
					while (sis.read() != -1) { len++; }
					throw new ExcessiveQueryException("The input of a request query is over CONTENT_LENGTH: " + (this.queryStream.length + len) + "/" + this.queryStream.length);
				}
			} finally {
				sis.close();		// 入力ストリームを閉じる
			}
		} else {
			throw new LengthRequiredException("Size specification of a query is unknown: " + max);
		}
	}


// AbstractExtendedHttpServletRequest interface
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
	public InputStream getMessageBody() throws IOException{
		return new ByteArrayInputStream(this.queryStream);
	}
}

