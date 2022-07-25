package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletRequest;

/**
 * １つのリクエストパラメータを保持するオブジェクトの共通インターフェースの
 * 基本実装を提供します。
 *
 */
public class RequestParameter4mimeEntity4parameter extends AbstractRequestParameter4mimeEntity{
	private byte[] value;					// パラメータ値

	/**
	 * コンストラクタ
	 * @param name パラメータ名
	 * @param value パラメータ値
	 * @param request リクエスト
	 * @throws IOException
	 */
	public RequestParameter4mimeEntity4parameter(InputStream header, byte[] value, ServletRequest request) throws IOException{
		super(header, request);

		this.value = value;
	}

	/**
	 * データ部のバイト長を返します。<P>
	 * このメソッドは、#getInputStream() が返す入力ストリームから、
	 * データを取り出して取得できたバイト数を返します。<br>
	 * パフォーマンスを向上させる場合は、サブクラスでオーバーライドして、
	 * メソッドを再定義してください。
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
}

