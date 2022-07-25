package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;


/**
 * １つのリクエストパラメータを保持するオブジェクトの共通インターフェースの
 * 基本実装を提供します。
 *
 */
public abstract class AbstractRequestParameter4mimeEntity extends AbstractRequestParameter{
	private Map headerMap = new HashMap();
	private String name = "";			// パラメータ名
	private String charset = null;		// パラメータ名の文字エンコーディング名

	/**
	 * コンストラクタ
	 * @param name パラメータ名
	 * @param value パラメータ値
	 * @param request リクエスト
	 * @throws IOException
	 */
	protected AbstractRequestParameter4mimeEntity(InputStream header, ServletRequest request) throws IOException{
		super(request);

		// Header の解析
		this.charset = request.getCharacterEncoding();
		BufferedReader br;
		if(this.charset != null){
			br = new BufferedReader(new InputStreamReader(header, this.charset));
		}
		else{
			br = new BufferedReader(new InputStreamReader(header));
		}
		while(true){
			String line = br.readLine();						// １行取得
			if(line != null){
				int colon = line.indexOf(":");
				if(colon != -1){
					this.headerMap.put(line.substring(0, colon).trim(), line.substring(colon + 1).trim());
					if(line.startsWith("Content-Disposition:")){
						// コントロール名称の取得
						int nameIndex = line.indexOf("name=");
						if(nameIndex != -1){
							int endIndex = line.indexOf(";", nameIndex);
							if(endIndex == -1){ endIndex = line.length(); }
							if(line.charAt(nameIndex + 5) == '"'){
								// " で囲まれている
								this.name = line.substring(nameIndex + 6, endIndex - 1).trim();
							}
							else{
								this.name = line.substring(nameIndex + 5, endIndex).trim();
							}
						}
					}
				}
				else{
					this.headerMap.put(line.trim(), "");
				}
			}
			else{
				break;
			}
		}
	}

	/**
	 * パラメータの名称を返します。
	 * @return パラメータ名を含む入力ストリーム
	 * @throws IOException 入出力エラー
	 */
	public InputStream getNameAsStream() throws IOException{
		if(this.charset != null){
			try {
				return new ByteArrayInputStream(this.name.getBytes(this.charset));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Parameter-name I/O error: " + this.charset, e);
			}
		}
		else{
			return new ByteArrayInputStream(this.name.getBytes());
		}
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
		Set set = this.headerMap.keySet();
		return (String[]) set.toArray(new String[set.size()]);
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
		return (String) this.headerMap.get(name);
	}

	/**
	 * 指定された文字列がヘッダの名称かどうかを判定します。<P>
	 *
	 * @param name ヘッダ名称
	 * @return ヘッダ名称が値にマップされている場合 true
	 */
	public boolean hasHeader(String name){
		return this.headerMap.containsKey(name);
	}

	/**
	 * データ部を入力ストリームとして取得します。<P>
	 *
	 * @return データ
	 */
	public abstract InputStream getValueAsStream() throws IOException;
}

