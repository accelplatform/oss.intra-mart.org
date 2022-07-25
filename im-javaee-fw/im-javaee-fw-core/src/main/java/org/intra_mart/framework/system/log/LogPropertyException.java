/*
 * LogPropertyException.java
 *
 * Created on 2003/07/10, 20:00
 */

package org.intra_mart.framework.system.log;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * ログプロパティ取得時の例外です。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class LogPropertyException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>LogPropertyException</code> を構築します。
     */
    public LogPropertyException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>LogPropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public LogPropertyException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>LogPropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public LogPropertyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
