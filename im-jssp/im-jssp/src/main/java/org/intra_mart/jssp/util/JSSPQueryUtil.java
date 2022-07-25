package org.intra_mart.jssp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * JSSPQueryに関するユーティリティクラスです。
 */
public class JSSPQueryUtil {

	/**
	 * JSSPQueryエンコードを行います。
	 * 
	 * @param s エンコード対象文字列
	 * @return エンコード後の文字列
	 */
	public static String encode(String s){
		StringBuffer str = new StringBuffer();

		int len = s.length();
		for(int idx = 0; idx < len; idx++){
			char c = s.charAt(idx);
			if(c >= 0x30 && c <= 0x39){
				str.append(c);
			}
			else if(c >= 0x41 && c <= 0x5a){
				str.append(c);
			}
			else if(c >= 0x61 && c <= 0x7a){
				str.append(c);
			}
			else{
				str.append('(');
				str.append(Integer.toString(c, 16).toCharArray());
				str.append(')');
			}
		}

		return str.toString();
	}

	/**
	 * JSSPQueryデコードを行います。
	 * 
	 * @param s デコード対象文字列
	 * @return デコード後の文字列
	 */
	public static String decode(String s){
		StringBuffer str = new StringBuffer();

		int len = s.length();
		for(int idx = 0; idx < len; idx++){
			char c = s.charAt(idx);
			if(c == '('){
				StringBuffer sb = new StringBuffer();
				for(idx++; idx < len; idx++){
					c = s.charAt(idx);
					if(c != ')'){
						sb.append((char) c);
					}
					else{
						break;
					}
				}
				str.append((char) Integer.parseInt(sb.toString(), 16));
			}
			else{
				str.append(c);
			}
		}

		return str.toString();
	}


	private static final int EQUAL_LEN = "=".length();

	/**
	 * クエリー文字列から、キーに該当する値を取得します。<BR>
	 * 取得した値は、URLデコード → JSSPQueryデコードされています。
	 * 
	 * @param queryString 検索対象のクエリー文字列
	 * @param key キー
	 * @return キーに該当する値。キーに該当する値が存在しない場合はnull
	 */
	public static String getValueFromQueryString(String queryString, String key){

		if(queryString.indexOf(key) != -1){
			String value = queryString.substring(queryString.indexOf(key) + key.length() + EQUAL_LEN);
			
			// 値の切り出し
			if(value.indexOf("&") == -1) {
				value = value.substring(0);
			}
			else {
				value = value.substring(0, value.indexOf("&"));
			}
			
			// URLデコード
			try {
				value = URLDecoder.decode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) { /* Do Nothing */  }
			
			// JSSPQueryデコード(「(2f)」→「/」など)
			String decodedPath = JSSPQueryUtil.decode(value);

			// 2個以上連続している「/」を削除
			return decodedPath.replaceAll("/{2,}", "");
		
		}
		else {
			return null;
		}
	}
}
