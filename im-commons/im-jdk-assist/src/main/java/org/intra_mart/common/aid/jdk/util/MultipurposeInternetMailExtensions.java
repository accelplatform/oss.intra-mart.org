package org.intra_mart.common.aid.jdk.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;

import org.intra_mart.common.aid.jdk.java.util.ResourceBundleProperties;


/**
 * MIME タイプを持つオブジェクトです。<P>
 *
 */
public class MultipurposeInternetMailExtensions{
	// インスタンス変数
	private String mimeType = "application/octet-stream";

	/**
	 * MIMEタイプを表すオブジェクトを構築します。<P>
	 *
	 * MIME タイプは引数 filename の拡張子により解決されます。<BR>
	 * 引数 filename に拡張子がない場合や、拡張子から MIME タイプを解決
	 * 出来なかった場合には、オブジェクトの表す MIME タイプは
	 * application/octet-stream となります。<BR>
	 *
	 * @param filename ファイル名
	 */
	public MultipurposeInternetMailExtensions(String filename){
		int dot = filename.lastIndexOf(".");
		if(dot != -1){
			this.mimeType = getResourceBundle("mimeType")
			.getProperty(filename.substring(dot).toLowerCase(), "application/octet-stream");
		}
	}

	/**
	 * MIMEタイプを表すオブジェクトを構築します。<P>
	 *
	 * MIME タイプは引数 path の表すファイル名の拡張子により解決されます。<BR>
	 * 引数 path の表すファイル名に拡張子がない場合や、拡張子から MIME タイプを
	 * 解決出来なかった場合には、オブジェクトの表す MIME タイプは
	 * application/octet-stream となります。<BR>
	 *
	 * @param path ファイル名を表す File クラスのインスタンス
	 */
	public MultipurposeInternetMailExtensions(File path){
		this(path.getName());
	}

	/**
	 * 指定のMIMEタイプを表すオブジェクトを構築します。<P>
	 *
	 * このコンストラクタにより構築されたオブジェクトの表す MIME タイプは、
	 * mediaType "/" subType となります。<BR>
	 *
	 * @param mediaType メディアタイプを表すキーワード
	 * @param subType サブタイプを表すキーワード
	 */
	public MultipurposeInternetMailExtensions(String mediaType, String subType){
		this.mimeType = mediaType.concat("/").concat(subType);
	}

	/**
	 * このオブジェクトが表す MIME タイプの Media Type を返します。<P>
	 *
	 * @return Media Type を表す文字列
	 */
	public String getMediaType(){
		return this.mimeType.substring(0, this.mimeType.indexOf("/"));
	}

	/**
	 * このオブジェクトが表す MIME タイプの subtype を返します。<P>
	 *
	 * @return subtype を表す文字列
	 */
	public String getSubType(){
		return this.mimeType.substring(this.mimeType.indexOf("/") + 1);
	}

	/**
	 * このオブジェクトが表す MIME タイプを返します。<P>
	 *
	 * @return MIME タイプを表す文字列
	 */
	public String getType(){
		return this.mimeType;
	}

	/**
	 * このオブジェクトの文字列表現を返します。<P>
	 *
	 * @return MIME タイプを表す文字列
	 */
	public String toString(){
		return getType();
	}
	
	private static Map aResourceTable = new HashMap();	// ファイルキャッシュ

	/**
	 * ResourceBundle サブ・クラスの取得メソッド<p>
	 * 取得できた ResourceBundle オブジェクトは、Hashtable にて
	 * キャッシュされ、次回からはキャッシュが利用される。
	 * 
	 * @param sFName
	 * @return Properties
	 * @throws MissingResourceException
	 */
	private static Properties getResourceBundle(String sFName) throws MissingResourceException{
		// リソース・サブクラスの取得
		Properties properties = (Properties) aResourceTable.get(sFName);

		if(properties == null){
			// リソース・サブクラスの新規取得(例外は呼び出し側にて処理)
			properties = new ResourceBundleProperties(sFName);
			// ハッシュテーブルへの保存
			aResourceTable.put(sFName, properties);
		}

		// リソースサブクラスの返却
		return properties;
	}
}


/* End of File */