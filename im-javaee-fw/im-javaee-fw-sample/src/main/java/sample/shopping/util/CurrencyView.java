package sample.shopping.util;

/**
 * 通貨をあらわす数値文字列を、カンマ区切りにする
 * ユーティリティクラスです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class CurrencyView {
    /**
     * 数値文字列を通過表示用にカンマ区切りにします。
     *
     * @param data 変換対象の数値文字列
     * @return カンマ区切りの文字列
     */
    public static String getString(String data) {
        int i;
        int len = data.length();
        StringBuffer sf = null;

        sf = new StringBuffer(data);

        for (i = len - 3; i > 0; i = i - 3) {
            sf.insert(i, ',');
        }

        return new String(sf);
    }
}
