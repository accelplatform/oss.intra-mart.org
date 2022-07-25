/*
 * StringUtil.java
 *
 * Created on 2005/06/23,  13:37:49
 */
package org.intra_mart.data_migration.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 文字列関連のユーティリティです。
 *
 * @author intra-mart
 * 
 */
public class StringUtil {

	private static Random random = new Random();
	
    /**
     * コンストラクタ
     */
    private StringUtil() {}
    
    /**
     * ユニークＩＤを取得します。
     * 長さは20バイト固定です。
     * 
     * @return ID
     */
	public static String getUniqueId() {
		byte[] src = new byte[16];
		MessageDigest md = null;
		byte[] b = null;

		random.nextBytes(src);
		try {
			md = MessageDigest.getInstance("MD5");
			b = md.digest(src);
		} catch (NoSuchAlgorithmException ex) {
		}

		StringBuffer dec = new StringBuffer("");
		for (int i = 0; i < b.length; i++) {
			int val = b[i] & 0xFF;
			if (val < 16) {
				dec.append("0");
			}
			dec.append(Integer.toString(val, 16));
		}
		return dec.toString().substring(0, 20);
	}
}
