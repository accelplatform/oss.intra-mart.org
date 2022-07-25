package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * １つのリクエストパラメータを保持するオブジェクトの共通インターフェース。
 *
 */
public interface RequestParameter{
	/**
	 * パラメータ名を返します。<P>
	 *
	 * GET の場合、各パラメータのパラメータ名を返します。<br>
	 * POST の場合、
	 * フォームから送信された情報のうち、このエンティティが表す情報の
	 * もととなったフォーム中に定義されたコントロールの名称を返します。<BR>
	 *
	 * @return パラメータ名
	 */
	public String getName();

	/**
	 * パラメータの名称を返します。
	 * @return パラメータ名を含む入力ストリーム
	 * @throws IOException 入出力エラー
	 */
	public InputStream getNameAsStream() throws IOException;

	/**
	 * データ部を文字列表現として返します。<P>
	 * 入力されたバイトデータから文字列への変換には、
	 * リクエストの文字エンコーディング(javax.servlet.ServletRequest#getCharacterEncoding())を使います。<BR>
	 *
	 * @return データ
	 */
	public String getValue();

	/**
	 * データ部を Reader として取得します。<P>
	 *
	 * 文字コード変換には、
	 * リクエストの文字エンコーディング(javax.servlet.ServletRequest#getCharacterEncoding())を使います。<BR>
	 * @return データ
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public Reader getValueAsReader() throws UnsupportedEncodingException, IOException;

	/**
	 * データ部を入力ストリームとして取得します。<P>
	 *
	 * @return データ
	 * @throws IOException
	 */
	public InputStream getValueAsStream() throws IOException;

	/**
	 * データ部のバイト長を返します。<P>
	 *
	 * @return データのサイズ
	 */
	public long getValueLength();

	/**
	 * このオブジェクトがヘッダ情報を持つかどうかを判定します。<P>
	 *
	 * @return ヘッダ情報を持つ場合 true
	 */
	public boolean hasHeaders();

	/**
	 * ヘッダ名称一覧を返します。<P>
	 *
	 * このエンティティのもつヘッダ情報の名称一覧を返します。<BR>
	 * ヘッダ情報がない場合、このメソッドは null を返します。
	 *
	 * @return ヘッダ名称
	 */
	public String[] getHeaderNames();

	/**
	 * 指定のヘッダの値を返します。<P>
	 *
	 * 指定のヘッダ名がない場合や、ヘッダ自体がない場合は、
	 * null を返します。
	 *
	 * @param name ヘッダ名称
	 * @return ヘッダ名称にマップされている値
	 */
	public String getHeader(String name);

	/**
	 * 指定された文字列がヘッダの名称かどうかを判定します。<P>
	 *
	 * @param name ヘッダ名称
	 * @return ヘッダ名称が値にマップされている場合 true
	 */
	public boolean hasHeader(String name);
}

