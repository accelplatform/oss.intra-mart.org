package org.intra_mart.common.aid.jdk.java.lang;

/**
 * 数値型に関するユーティリティクラス。<BR>
 * <BR>
 * 数値型に関するユーティリティクラス<BR>
 */
public class NumberUtil {

    /**
     * コンストラクタ。<BR>
     * <BR>
     * 
     */
    protected NumberUtil() {
        super();
    }

    /**
     * オブジェクトを整数に変換します。<BR>
     * <BR>
     * 数値に変換できなかった場合は、デフォルト値を返却します。
     * @param object 変換元オブジェクト
     * @param def デフォルト値
     * @return 変換後の整数
     */
    public static int toInt(Object object,int def) {
        if(object instanceof Number) {
            return ((Number)object).intValue(); 
        }
        else {
            return def;
        }
    }

    /**
     * オブジェクトを倍精度実数に変換します。<BR>
     * <BR>
     * 数値に変換できなかった場合は、デフォルト値を返却します。
     * @param object 変換元オブジェクト
     * @param def デフォルト値
     * @return 変換後の倍精度実数
     */
    public static double toDouble(Object object,double def) {
        if(object instanceof Number) {
            return ((Number)object).doubleValue(); 
        }
        else {
            return def;
        }
    }
}
