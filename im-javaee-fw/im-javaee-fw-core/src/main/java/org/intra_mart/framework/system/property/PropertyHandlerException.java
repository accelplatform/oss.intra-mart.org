/*
 * PropertyHandlerException.java
 *
 * Created on 2001/11/08, 11:13
 */

package org.intra_mart.framework.system.property;

import org.intra_mart.framework.system.exception.FrameworkException;

/**
 * プロパティハンドラの情報取得時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class PropertyHandlerException extends FrameworkException {

    /**
     * 詳細メッセージを指定しないで <code>PropertyHandlerException</code> を構築します。
     */
    public PropertyHandlerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>PropertyHandlerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public PropertyHandlerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>PropertyHandlerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public PropertyHandlerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
