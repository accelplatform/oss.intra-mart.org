package org.intra_mart.jssp.page;

import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;


import org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter;
import org.intra_mart.jssp.util.RuntimeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * <!-- JavaScriptオブジェクト -->
 * リクエストパラメータ情報オブジェクト。<BR>
 * <BR>
 * リクエストパラメータの情報を保持します。<BR>
 * 
 * @scope public
 * @name RequestParameter
 */
public class RequestParameterObject extends ScriptableObject implements Serializable{

	private static final ScriptableObject PROTOTYPE = new RequestParameterObject();

	// パラメータ情報
	private RequestParameter parameter;						

	/** バイト配列を文字コード変換せずに取得する為の静的変数 */
	private static String NOT_CONVERT_CHARSET = "ISO-8859-1";

	/**
	 * プロトタイプ用のコンストラクタです。
	 */
	public RequestParameterObject(){
		// 基本メソッドの登録
		try{
			String[] names = {
			                  "getName",
			                  "getValue",
			                  "getValueAsStream",
			                  "getLength",
			                  "getHeader",
			                  "getHeaderNames",
			                  "getFileName"
			                  };
			this.defineFunctionProperties(names, RequestParameterObject.class, ScriptableObject.DONTENUM);
		}
		catch(Exception e){
			IllegalStateException ise = new IllegalStateException("JavaScript-API <RequestParameter in Request> initialize error.");
			ise.initCause(e);
			throw ise;
		}
	}
	
	
	/**
	 * パラメータオブジェクトを新しく作成します。
	 * @param parameter
	 */
	public RequestParameterObject(RequestParameter parameter){
		super();
		this.parameter = parameter;

		// 基本メソッドの追加登録
		this.setPrototype(PROTOTYPE);
	}
	
	
	/**
	 * パラメータオブジェクトを新しく作成します。
	 * 
	 * @param name パラメータ名
	 * @param value パラメータ値
	 * @param httpRequest リクエスト
	 */
	public RequestParameterObject(String name, String value, HttpServletRequest httpRequest){
		this(new RequestParameter4javascriptAPI(name, value, httpRequest));
	}

	
	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return JavaScript 実行環境上での名称
	 */
	public String getClassName() {
		return "RequestParameter";
	}

	/**
	 * このオブジェクトのパラメータ名を取得します。<br>
	 * <br>
	 * @scope public
	 * @return String パラメータ名
	 */
	public String getName(){
		return this.parameter.getName();
	}

	/**
	 * 現在のパラメータ値を返します。<br>
	 * <br>
	 * パラメータ値は、リクエストの文字エンコーディングから文字コード変換されています。<br>
	 * 
	 * @scope public
	 * @return String パラメータ値
	 */
	public String getValue(){
		return this.parameter.getValue();
	}

	/**
	 * 現在のパラメータ値を返します。<br>
	 * <br>
	 * パラメータ値は、文字コード変換されません。<br>
	 * パラメータ値の各バイトがそれぞれ String の１文字に対応します。<br>
	 * つまり、パラメータ値のバイト数は、そのまま String の文字数に等しくなります。<br>
	 * 
	 * @scope public
	 * @return String パラメータ値
	 */
	public String getValueAsStream() throws IOException{
		int max = (int) this.getLength();
		InputStream inputStream = this.parameter.getValueAsStream();
		try{
			Writer out = new CharArrayWriter(max);
			Reader in = new InputStreamReader(inputStream, NOT_CONVERT_CHARSET);
			char[] chars = new char[8 * 1024];
			while(true){
				int len = in.read(chars);
				if(len != -1){
					out.write(chars, 0, len);
				}
				else{
					break;
				}
			}
			return out.toString();
		}
		finally{
			inputStream.close();
		}
	}

	/**
	 * データ長を返します。<br>
	 * <br>
	 * @scope public
	 * @return Number データ長（単位はバイト）
	 */
	public double getLength(){
		return this.parameter.getValueLength();
	}

	/**
	 * 指定のヘッダ名をもつ値を返します。<br>
	 * <br>
	 * @scope public
	 * @param name String ヘッダ名
	 * @return String ヘッダの値
	 */
	public String getHeader(String name){
		return this.parameter.getHeader(name);
	}

	/**
	 * ヘッダ名称一覧を返します。<br>
	 * <br>
	 * このエンティティのもつヘッダ情報の名称一覧を返します。<BR>
	 * ヘッダ情報がない場合、このメソッドは null を返します。<br>
	 * 
	 * @scope public
	 * @return Array ヘッダ名称の配列
	 */
	public Scriptable getHeaderNames(){
		return RuntimeObject.newArrayInstance(this.parameter.getHeaderNames());
	}

	/**
	 * アップロードされたファイルのファイル名を取得します。<br>
	 * ファイルアップロードをしたリクエストではない場合は、null を返します。<br>
	 * <br>
	 * ヘッダは、下記のような形式で受け取れる<br>
	 * <blockquote>
	 * Content-Disposition: form-data; name=&quot;ctrl_name&quot;; filename=&quot;file_path&quot;<br>
	 * </blockquote>
	 * 
	 * 本メソッドは、Content-Dispositionヘッダの以下の項目を対象に解析を行います。
	 * <table border="1">
	 *   <tr>
	 *     <th>ctrl_name</th>
	 *     <td>&lt;INPUT type=&quot;file&quot;&gt; の name 属性</td>
	 *   </tr>
	 *   <tr>
	 *     <th>file_path</th>
	 *     <td>クライアントでのファイル名</td>
	 *   </tr>
	 * </table>
	 * 
	 * @scope public
	 * @return String ファイル名
	 */
	public String getFileName(){
		String value = this.getHeader("Content-Disposition");
		return getFileNameFromContentDispositionField(value);
	}

	/**
	 * Content-Descriptionフィールド(see. RFC 2183) から、ファイル名を取得します。
	 * 
	 * @param contentDispositionField Content-Descriptionフィールドの文字列
	 * @return ファイル名<br>
	 * 			（Content-Descriptionフィールドに filenameパラメータが含まれない場合は nullを返却します）
	 * 
	 * @deprecated RequestParameterObject内だけで利用するメソッドです。
	 * 				（スコープをpublicにしているのは、ユニットテストを実行するためです）
	 */
	public static String getFileNameFromContentDispositionField(String contentDispositionField){

		if(contentDispositionField == null){
			return null;
		}

		String fileName = null;

		int pt = contentDispositionField.indexOf("filename=");
		if(pt != -1){
			fileName = contentDispositionField.substring(pt + "filename=".length()).trim();
			
			if(fileName.startsWith("\"")){
				fileName = fileName.substring(1);
				fileName = fileName.substring(0, fileName.indexOf("\""));
			}
			else {
				int semiColon = fileName.indexOf(";");
				if(semiColon != -1){
					fileName = fileName.substring(0, semiColon).trim();
				}
			}
			
			int pathSep = fileName.lastIndexOf("/");
			if(pathSep != -1){
				fileName = fileName.substring(pathSep + 1);
			}
			int pathSep4win = fileName.lastIndexOf("\\");
			if(pathSep4win != -1){
				fileName = fileName.substring(pathSep4win + 1);
			}
		}
		
		return fileName;
		
	}
}

/**
 * １つのリクエストパラメータを保持するオブジェクトの共通インターフェース。
 */
class RequestParameter4javascriptAPI implements RequestParameter{
	private String name;
	private String value;
	private String charset = null;

	/**
	 * コンストラクタ
	 * @param name パラメータ名
	 * @param value パラメータ値
	 * @param request リクエスト
	 */
	public RequestParameter4javascriptAPI(String name, String value, ServletRequest request){
		super();
		this.name = name;
		this.value = value;
		this.charset = request.getCharacterEncoding();
	}

	/**
	 * パラメータの名称を返します。
	 * @return パラメータ名を含む入力ストリーム
	 * @throws IOException 入出力エラー
	 */
	public InputStream getNameAsStream() throws IOException{
		if(this.charset != null){
			return new ByteArrayInputStream(this.name.getBytes(this.charset));
		}
		else{
			return new ByteArrayInputStream(this.name.getBytes());
		}
	}

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
		return this.name;
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
	 * データ部を文字列表現として返します。<P>
	 * 入力されたバイトデータから文字列への変換には、
	 * リクエストの文字エンコーディング(javax.servlet.ServletRequest#getCharacterEncoding())を使います。<BR>
	 *
	 * @return データ
	 */
	public String getValue(){
		return this.value;
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
		return new StringReader(this.value);
	}

	/**
	 * データ部を入力ストリームとして取得します。<P>
	 *
	 * @return データ
	 */
	public InputStream getValueAsStream() throws IOException{
		if(this.charset != null){
			return new ByteArrayInputStream(this.value.getBytes(this.charset));
		}
		else{
			return new ByteArrayInputStream(this.value.getBytes());
		}
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter#getValueLength()
	 */
	public long getValueLength() {
		if(this.charset != null){
			try {
				return this.value.getBytes(this.charset).length;
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Parameter I/O Error: " + this.charset, e);
			}
		}
		else{
			return this.value.getBytes().length;
		}
	}

	/* (非 Javadoc)
	 * @see org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter#hasHeaders()
	 */
	public boolean hasHeaders() {
		return false;
	}
}
