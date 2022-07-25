/*
 * LogConstant.java
 *
 * Created on 2003/08/01, 18:00
 */

package org.intra_mart.framework.system.log;

/**
 * ログフレームワークで使用される定数値です。
 *
 * @author INTRAMART
 * @since 4.2
 */
public interface LogConstant {

    /**
     * DEBUG時に出力するレベルです。
     */
    public static final String LEVEL_DEBUG = "DEBUG";

    /**
     * 情報を出力するレベルです。
     */
    public static final String LEVEL_INFO = "INFO";

    /**
     * 警告を出力するレベルです。
     */
    public static final String LEVEL_WARNNING = "WARNNING";

    /**
     * 例外を出力するレベルです。
     */
    public static final String LEVEL_ERROR = "ERROR";
}
