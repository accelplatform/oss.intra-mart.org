package org.intra_mart.common.aid.jdk.util.charset;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * このクラスは J2SE,v1.4 でサポートされているエンコーディングセットと、
 * IANAに登録されているキャラクタセット名とのマッピング情報を管理するオブジェクトです。<p>
 * 
 * J2SE,v1.4でサポートされているエンコーディングセット名から
 * IANAに登録されているキャラクタセット名へのマッピングについては、
 * 定義ファイル
 * <code>org/intra_mart/common/aid/jdk/util/charset/jdk2iana.properties</code>
 * に記載されています。<p>
 * 
 * IANAに登録されているキャラクタセット名と
 * J2SE,v1.4 でサポートされているエンコーディングセット名へのマッピングについては、
 * 定義ファイル
 * <code>org/intra_mart/common/aid/jdk/util/charset/jdk2iana.properties</code>
 * に記載されています。<p>
 * 
 */
public class CharacterSetManager {

	/**
	 * IANAに登録されているキャラクタセット名から
	 * J2SEでサポートされているエンコーディングセット名への
	 * マッピングが記載されている定義ファイル名
	 */
	private static final String IANA_TO_JDK_PROPERTIES_FILE_PATH 
			= "org/intra_mart/common/aid/jdk/util/charset/iana2jdk";

	/**
	 * J2SEでサポートされているエンコーディングセット名から
	 * IANAに登録されているキャラクタセット名への
	 * マッピングが記載されている定義ファイル名
	 */
	private static final String JDK_TO_IANA_PROPERTIES_FILE_PATH
			= "org/intra_mart/common/aid/jdk/util/charset/jdk2iana";

	/**
	 * IANAに登録されているキャラクタセット名から
	 * J2SEでサポートされているエンコーディングセット名への
	 * マッピング情報を格納するプロパティズ
	 */
	private static Properties iana2jdkProperties = new Properties();

	/**
	 * J2SEでサポートされているエンコーディングセット名から
	 * IANAに登録されているキャラクタセット名への
	 * マッピング情報を格納するプロパティズ
	 */
	private static Properties jdk2ianaProperties = new Properties();

	/**
	 * J2SE,v1.4 でサポートされているエンコーディングセットと、
	 * IANAに登録されているキャラクタセット名との
	 * マッピング情報をプロパティズに格納します。
	 */
	static {
		// IANAからJ2SEへのマッピング情報をプロパティズに格納
		setProperties(CharacterSetManager.IANA_TO_JDK_PROPERTIES_FILE_PATH,
		              CharacterSetManager.iana2jdkProperties);
		              
		// JDKからIANAへのマッピング情報をプロパティズに格納
		setProperties(CharacterSetManager.JDK_TO_IANA_PROPERTIES_FILE_PATH,
		              CharacterSetManager.jdk2ianaProperties);
	}

	/**
	 * 指定されたリソースファイルから、
	 * マッピング情報を取得し、指定されたプロパティズに格納します。
	 * 
	 * @param path リソースバンドルのファミリ名
	 * @param properties リソースからのマッピング情報を格納するプロパティズ
	 * @throws MissingResourceException リソースの照合に失敗した場合
	 */
	private static void setProperties(String path, Properties properties)
	   throws MissingResourceException{
		ResourceBundle resourceBundle = ResourceBundle.getBundle(path);
		Enumeration cursor = resourceBundle.getKeys();
	
		String encodingName = null;
		String encodingValue = null;

		while(cursor.hasMoreElements()){
			// リソースファイルから、マッピング情報を逐一取得
			encodingName = (String) cursor.nextElement();
			encodingValue = resourceBundle.getString(encodingName);
			// 取得したマッピング情報をプロパティズに追加
			properties.setProperty(encodingName, encodingValue);
		}	
	}

	/**
	 * 指定されたキャラクタセット名 に対応する
	 * J2SEでサポートされているエンコーディングセット名を返却します。<br>
	 * 対応するエンコーディングセット名が見つからない場合には
	 * 指定されたキャラクタセット名をそのまま返却します。
	 * 
	 * @param ianaCharacterSetName IANAに登録されている キャラクタセット名
	 * @return J2SEでサポートされているエンコーディングセット名
	 */
	public static String toJDKName(String ianaCharacterSetName){
		return CharacterSetManager.iana2jdkProperties.getProperty(
		                     ianaCharacterSetName,ianaCharacterSetName);	
	}

	/**
	 * 指定されたエンコーディングセット名に対応する
	 * IANAに登録されている キャラクタセット名 を返却します。<br>
	 * 対応するキャラクタセット名が見つからない場合には
	 * 指定されたエンコーディングセット名をそのまま返却します。
	 * 
	 * @param jdkEncodingSetName J2SEでサポートされているエンコーディングセット名
	 * @return IANAに登録されているキャラクタセット名
	 */
	public static String toIANAName(String jdkEncodingSetName){
		return CharacterSetManager.jdk2ianaProperties.getProperty(
		                         jdkEncodingSetName,jdkEncodingSetName);
		
	}
}

// End of File.