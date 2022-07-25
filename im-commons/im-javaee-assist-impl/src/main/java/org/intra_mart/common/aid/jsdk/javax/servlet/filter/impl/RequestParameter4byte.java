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
public class RequestParameter4byte extends AbstractRequestParameter{
	private byte[] name;				// パラメータ名
	private byte[] value;				// パラメータ値

	/**
	 * コンストラクタ
	 * @param name パラメータ名
	 * @param value パラメータ値
	 * @param request リクエスト
	 */
	public RequestParameter4byte(byte[] name, byte[] value, ServletRequest request){
		super(request);
		this.name = name;
		this.value = value;
	}

	/**
	 * パラメータの名称を返します。
	 * @return パラメータ名を含む入力ストリーム
	 * @throws IOException 入出力エラー
	 */
	public InputStream getNameAsStream() throws IOException{
		return new ByteArrayInputStream(this.name);
	}

	/**
	 * ヘッダ名称一覧を返します。<P>
	 *
	 * このエンティティのもつヘッダ情報の名称一覧を返します。<BR>
	 * ヘッダ情報がない場合、このメソッドは null を返します。
	 *
	 * @return ヘッダ名称
	 */
	public String[] getHeaderNames(){
		return null;
	}

	/**
	 * 指定のヘッダの値を返します。<P>
	 *
	 * 指定のヘッダ名がない場合や、ヘッダ自体がない場合は、
	 * null を返します。
	 *
	 * @param name ヘッダ名称
	 * @return ヘッダ名称にマップされている値
	 */
	public String getHeader(String name){
		return null;
	}

	/**
	 * 指定された文字列がヘッダの名称かどうかを判定します。<P>
	 *
	 * @param name ヘッダ名称
	 * @return ヘッダ名称が値にマップされている場合 true
	 */
	public boolean hasHeader(String name){
		return false;
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

