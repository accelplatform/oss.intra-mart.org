package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;

/**
 * アップロード・ファイルを格納するリクエストパラメータ。
 * アップロード・ファイルのデータは、メモリ上に保持されます。
 */
public class RequestParameter4mimeEntity4fileInMemory extends AbstractRequestParameter4mimeEntity{
	
	/**
	 * パラメータ値
	 */
	private byte[] value; 

	/**
	 * コンストラクタ
	 * @param header ヘッダー
	 * @param value パラメータ値
	 * @param request リクエスト
	 * @throws IOException
	 */
	public RequestParameter4mimeEntity4fileInMemory(InputStream header, 
													 byte[] value,
													 ServletRequest request) throws IOException{
		super(header, request);
		this.value = value;
	}

	/**
	 * データ部のバイト長を返します。<P>
	 *
	 * @return データのサイズ
	 */
	public long getValueLength(){
		return this.value.length;
	}

	/**
	 * データ部を入力ストリームとして取得します。<P>
	 *
	 * @return データ
	 */
	public InputStream getValueAsStream() throws IOException{		
		return new ByteArrayInputStream(this.value);
	}

	
	/* (非 Javadoc)
	 * @see org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter#getValue()
	 */
	public String getValue() {

		String charset = this.getCharacterEncoding();
		
		if(charset != null) {
			try {
				return new String(this.value, charset);
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Parameter-name I/O error: " + charset, e);
			}
		} 
		else {
			return new String(this.value);			
		}

	}	
	
}

