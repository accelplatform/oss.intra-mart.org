/*
 * ExceptionUtil.java
 *
 * Created on 2005/05/17,  10:49:59
 */
package org.intra_mart.data_migration.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 例外を扱うユーティリティです。
 *
 * @author intra-mart
 * 
 */
public class ExceptionUtil {

    /**
     * コンストラクタ
     */
    private ExceptionUtil() {
    }
    
    /**
     * スタックとレースを文字列として返します。<br>
     * パラメータの例外クラスがNullの場合は空文字を返します。
     * 
     * @param throwable 例外クラス
     * @return スタックトレースの文字列
     */
    public static String getStackTrace(Throwable throwable) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PrintStream print = new PrintStream(stream);
            throwable.printStackTrace(print);
            print.close();
            return stream.toString();
        } catch (NullPointerException e) {
            return "";
        }
    }
}
