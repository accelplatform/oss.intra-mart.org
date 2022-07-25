package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter;


/**
 * HttpServletRequest を拡張したインタフェースです。
 * 
 */
public class HttpServletRequestMessageBodyWrapper4urlencodedQuery extends AbstractBufferedHttpServletRequestMessageBodyWrapper{
	/**
	 * 拡張されたリクエストを作成します。
	 * @param request サーブレットリクエスト
	 * @param parentRequestParameter 初期化パラメータ「parent.request.parameter」のboolean値
	 * @param parseQueryString 初期化パラメータ「parse.query.string」のboolean値
	 * @throws IOException
	 */
	public HttpServletRequestMessageBodyWrapper4urlencodedQuery(HttpServletRequest request,
																 boolean parentRequestParameter,
																 boolean parseQueryString)
				throws ServletException, IOException{
		super(request,
			  parentRequestParameter,
			  parseQueryString);
	}


// AbstractExtendedHttpServletRequest interface
	/**
	 * パラメータ情報を返します。
	 * このメソッドは、
	 * application/x-www-form-urlencoded で作成されたクエリを解析します。
	 * @param in 入力ストリーム
	 * @return パラメータ
	 * @throws IOException 入出力エラー
	 */
	protected RequestParameter[] getRequestParameters(InputStream in) throws IOException{
		Collection collection = new ArrayList();		// 器

		// ＵＲＬ引数の解析
		byte[] name = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int preReadChr = -1;
		
		while(true){
			int chr = in.read();
			if(chr == '='){
				name = this.urlDecode(out.toByteArray());
				out.reset();				// バッファのクリア
			}
			else if(chr == '&' || chr == -1){

				// 「key=value」「key=」形式の場合
				if( name != null ) {
					// 通常データ→デコード→登録
					collection.add(new RequestParameter4byte(name, this.urlDecode(out.toByteArray()), this));
				}
				// 「=」が含まれない形式の場合 (ただし、「=value」形式はリクエストパラメータとして追加しない)
				else if( name == null && preReadChr != '=' ){
					// 通常データ→デコード→登録
					collection.add(new RequestParameter4byte(this.urlDecode(out.toByteArray()), "".getBytes(), this));
				}
				
				if(chr != -1){
					name = null;
					out.reset();		// バッファのクリア
				}
				else{
					break;				// 入力の終了
				}
			}
			else{
				out.write(chr);			// 入力データを保存
			}
			
			// 走査した文字を格納
			preReadChr = chr;
		}

		return (RequestParameter[]) collection.toArray(new RequestParameter[collection.size()]);
	}

	/**
	 * URLデコードします。
	 * @param b バイト列
	 * @return デコード結果バイト列
	 */
	private byte[] urlDecode(byte[] b) throws IOException{
		return this.urlDecode(new ByteArrayInputStream(b));
	}

	/**
	 * URLデコードします。
	 * @param in 入力
	 * @return デコード結果バイト列
	 */
	private byte[] urlDecode(InputStream in) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		while(true){
			int chr = in.read();
			if(chr != -1){
				switch(chr){
					case '%':
						out.write((Character.digit((char) in.read(), 16) * 0x10) + Character.digit((char) in.read(), 16));
						break;
					case '+':
						out.write(' ');
						break;
					default:
						out.write(chr);
				}
			}
			else{
				break;
			}
		}

		return out.toByteArray();
	}
}

