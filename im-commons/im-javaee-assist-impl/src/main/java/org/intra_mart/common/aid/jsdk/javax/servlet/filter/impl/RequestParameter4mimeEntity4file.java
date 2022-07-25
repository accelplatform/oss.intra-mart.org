package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletRequest;

/**
 * アップロード・ファイルを格納するリクエストパラメータ。
 * アップロード・ファイルのデータは、ディスク上に保持されます。
 */
public class RequestParameter4mimeEntity4file extends AbstractRequestParameter4mimeEntity{

	/**
	 * パラメータ値
	 */
	private File value;

	/**
	 * ファイル入出力時のバッファサイズ
	 */
	private int bufferSize;
	
	/**
	 * コンストラクタ
	 * @param header ヘッダー
	 * @param value パラメータ値
	 * @param request リクエスト
	 * @param bufferSize ファイル入出力時のバッファサイズ
	 * @throws IOException
	 */
	public RequestParameter4mimeEntity4file(InputStream header, 
											 File value, 
											 ServletRequest request,
											 int bufferSize) throws IOException{
		super(header, request);

		this.value = value;
		this.bufferSize = bufferSize;
	}

	/**
	 * データ部のバイト長を返します。<P>
	 *
	 * @return データのサイズ
	 */
	public long getValueLength(){
		return this.value.length();
	}

	/**
	 * データ部を入力ストリームとして取得します。<P>
	 *
	 * @return データ
	 */
	public InputStream getValueAsStream() throws IOException{
		return new BufferedInputStream(new FileInputStream(this.value), this.bufferSize);
		
	}
}

