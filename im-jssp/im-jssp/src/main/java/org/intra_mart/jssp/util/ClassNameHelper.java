package org.intra_mart.jssp.util;

import java.io.File;
import java.util.Map;
import java.util.StringTokenizer;

import org.intra_mart.common.aid.jdk.java.util.FixedSizeMap;


/**
 * クラス名に関するヘルパークラスです。
 */
public final class ClassNameHelper{
	private static Map<String, String> NAME_POOL = new FixedSizeMap(48);
	private static Map<String, String> FILE_POOL = new FixedSizeMap(32);

	/**
	 * クラス名からファイル名を取得します。
	 * 
	 * @param className クラス名
	 * @return 指定のクラス名を持つクラスファイルのファイル名称
	 */
	public static synchronized String toFilePath(String className){
		String fileName = (String) FILE_POOL.get(className);
		if(fileName == null){
			fileName = className.replace('.', File.separatorChar).concat(".class");
			FILE_POOL.put(className, fileName);
		}
		return fileName;
	}

	/**
	 * 引数で指定された名称をクラス名として使える形に変換します。<br>
	 * クラス名の一部として使えない文字が含まれている場合、それらを
	 * すべて'_'(アンダースコア）に置換します。<br>
	 * また、各ディレクトリ名をパッケージ名とし、
	 * 各々の要素名の先頭に '_'(アンダースコア)を付加します。
	 * 
	 * @param name 名称
	 * @return クラス名として使える文字列に変換した名前
	 */
	public static String toClassName(String name){
		return toClassName(name, "_");
	}
	
	/**
	 * 引数で指定された名称をクラス名として使える形に変換します。<br>
	 * クラス名の一部として使えない文字が含まれている場合、それらを
	 * すべて'_'(アンダースコア）に置換します。<br>
	 * また、各ディレクトリ名をパッケージ名とし、
	 * 各々の要素名の先頭に 引数 prefix を付加します。
	 * 
	 * @param name 名称
	 * @param prefix 各々の要素名の先頭に付与する接頭辞(1文字だけ指定してください)
	 * @return クラス名として使える文字列に変換した名前
	 */
	public static String toClassName(String name, String prefix){
		// キャッシュの確認
		String className = (String) NAME_POOL.get(name);
		if(className == null){
			StringTokenizer st = new StringTokenizer(name, "/\\");
			StringBuffer sb = new StringBuffer(name.length() * 2);

			while(st.hasMoreTokens()){
				sb.append(encodeClassName((String) st.nextToken(), prefix)).append(".");
			}

			className = sb.substring(0, sb.length() - 1);
			NAME_POOL.put(name, className);
		}
		return className;
	}
	
	
	private static String encodeClassName(String name, String prefix) {
		
		char[] s;
		int pad;
		
		if(prefix == null || prefix.length() == 0){
			pad = 0;
			s = new char[name.length() + pad];
		}
		else{
			pad = 1;
			s = new char[name.length() + pad];
			s[0] = prefix.charAt(0);
		}


		for(int idx = 0; idx < name.length(); idx++) {
			char c = name.charAt(idx);
			
			if(Character.isHighSurrogate(c)){
				char high = c;
				s[idx + pad] = high; 

				idx++;

				char low = name.charAt(idx);
				s[idx + pad] = low; 
			}
			else{
				if( Character.isJavaIdentifierPart(c) ){
					s[idx + pad] = c; 
				}
				else{
					s[idx + pad] = '_'; 
				}
			}
		}
		
		return new String(s);
	}

}
