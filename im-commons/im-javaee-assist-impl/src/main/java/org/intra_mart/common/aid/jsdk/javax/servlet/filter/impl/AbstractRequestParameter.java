package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import javax.servlet.ServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter;


/**
 * １つのリクエストパラメータを保持するオブジェクトの共通インターフェースの
 * 基本実装を提供します。
 *
 */
public abstract class AbstractRequestParameter implements RequestParameter{
	/**
	 * このパラメータのリクエスト
	 * 循環参照によってメモリリークする可能性を回避するために
	 * 弱参照とする。
	 */
	private Reference requestReference = null;

	/**
	 * パラメータオブジェクトを作成します。
	 */
	protected AbstractRequestParameter(ServletRequest request){
		super();
		this.requestReference = new WeakReference(request);
	}

	/**
	 * パラメータの名称を返します。
	 * @return パラメータ名を含む入力ストリーム
	 * @throws IOException 入出力エラー
	 */
	public abstract InputStream getNameAsStream() throws IOException;

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
	public String getName(){
		try{
			Reader in = this.toReader(this.getNameAsStream());
			try{
				return this.toString(in);
			}
			finally{
				in.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("Parameter-name input error.", e);
		}
	}

	/**
	 * このオブジェクトがヘッダ情報を持つかどうかを判定します。<P>
	 * このメソッドは、{@link #getHeaderNames() } が null を返す場合、
	 * false を返します。
	 *
	 * @return ヘッダ情報を持つ場合 true
	 */
	public boolean hasHeaders(){
		return this.getHeaderNames() != null;
	}

	/**
	 * ヘッダ名称一覧を返します。<P>
	 *
	 * このエンティティのもつヘッダ情報の名称一覧を返します。<BR>
	 * ヘッダ情報がない場合、このメソッドは null を返します。
	 *
	 * @return ヘッダ名称
	 */
	public abstract String[] getHeaderNames();

	/**
	 * 指定のヘッダの値を返します。<P>
	 *
	 * 指定のヘッダ名がない場合や、ヘッダ自体がない場合は、
	 * null を返します。
	 *
	 * @param name ヘッダ名称
	 * @return ヘッダ名称にマップされている値
	 */
	public abstract String getHeader(String name);

	/**
	 * 指定された文字列がヘッダの名称かどうかを判定します。<P>
	 *
	 * @param name ヘッダ名称
	 * @return ヘッダ名称が値にマップされている場合 true
	 */
	public abstract boolean hasHeader(String name);

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
		long len = 0;
		try{
			InputStream in = this.getValueAsStream();
			try{
				while(in.read() != -1){ len++; }
			}
			finally{
				in.close();
			}
		}
		catch(IOException ioe){
			throw new RuntimeException("Parameter-value input error.", ioe);
		}
		return len;
	}

	/**
	 * データ部を文字列表現として返します。<P>
	 * 入力されたバイトデータから文字列への変換には、
	 * リクエストの文字エンコーディング(javax.servlet.ServletRequest#getCharacterEncoding())を使います。<BR>
	 *
	 * @return データ
	 */
	public String getValue(){
		try{
			Reader in = this.getValueAsReader();
			try{
				return this.toString(in);
			}
			finally{
				in.close();
			}
		}
		catch(IOException ioe){
			throw new RuntimeException("Parameter-value input error.", ioe);
		}
	}

	/**
	 * データ部を Reader として取得します。<P>
	 *
	 * 文字コード変換には、
	 * リクエストの文字エンコーディング(javax.servlet.ServletRequest#getCharacterEncoding())を使います。<BR>
	 * @return データ
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public Reader getValueAsReader() throws UnsupportedEncodingException, IOException{
		return this.toReader(this.getValueAsStream());
	}

	/**
	 * データ部を入力ストリームとして取得します。<P>
	 *
	 * @return データ
	 */
	public abstract InputStream getValueAsStream() throws IOException;

	/**
	 * パラメータを文字コード変換するための文字コード名を返します。
	 * @return 文字コード名
	 */
	protected String getCharacterEncoding(){
		ServletRequest request = (ServletRequest) this.requestReference.get();
		if(request != null){
			return request.getCharacterEncoding();
		}
		else{
			return null;
		}
	}

	/**
	 * 指定の入力ストリームから文字コード変換してデータを読み込むための
	 * Reader を作成して返します。
	 * @throws UnsupportedEncodingException
	 */
	private Reader toReader(InputStream in) throws UnsupportedEncodingException{
		String charset = this.getCharacterEncoding();
		if(charset != null){
			return new InputStreamReader(in, charset);
		}
		else{
			return new InputStreamReader(in);
		}
	}

	/**
	 * Reader から読み込んだ全ての文字を含む文字列を返します。
	 * @throws IOException
	 */
	private String toString(Reader in) throws IOException{
		CharArrayWriter writer = new CharArrayWriter(8 * 1024);
		try{
			char[] chars = new char[8 * 1024];
			while(true){
				int len = in.read(chars);
				if(len != -1){
					writer.write(chars, 0, len);
				}
				else{
					break;
				}
			}
			return writer.toString();
		}
		finally{
			writer.close();
		}
	}
}

