package sample.shopping.util;

import java.io.UnsupportedEncodingException;

/**
 * 文字列を指定されたコードでURLエンコードします。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class URLEncoder {

    /**
     * エンコードが可能かどうかを指定するフラグ
     */
    private static boolean isEnable = true;

    /**
     * 文字列を指定されたコードでURLエンコードします。
     *
     * @param str 変換対照の文字列
     * @param enc エンコード
     * @return URLエンコードされた文字列
     * @throws UnsupportedEncodingException サポートされていないエンコーディング
     */
    public static String encode(String str, String enc)
        throws UnsupportedEncodingException {

        if (isEnable) {
            byte[] data = str.getBytes(enc);
            StringBuffer buffer = new StringBuffer();
            String ch = null;

            for (int i = 0; i < data.length; i++) {
                buffer.append("%");
                ch = "0" + Integer.toHexString((int)data[i]);
                ch = ch.substring(ch.length() - 2);
                buffer.append(ch);
            }

            return new String(buffer);
        } else {
            return str;
        }
    }
}
