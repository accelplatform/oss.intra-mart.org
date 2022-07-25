/*
 * PropertyManagerException.java
 *
 * Created on 2001/11/08, 11:06
 */

package org.intra_mart.framework.system.property;

import org.intra_mart.framework.system.exception.FrameworkException;

/**
 * プロパティマネージャの設定時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class PropertyManagerException extends FrameworkException {

    /**
     * 詳細メッセージを指定しないで <code>PropertyManagerException</code> を構築します。
     */
    public PropertyManagerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>PropertyManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public PropertyManagerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>PropertyManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public PropertyManagerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
