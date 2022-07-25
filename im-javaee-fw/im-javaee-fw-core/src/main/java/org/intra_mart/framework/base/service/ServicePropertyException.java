/*
 * ServicePropertyException.java
 *
 * Created on 2001/12/17, 14:27
 */

package org.intra_mart.framework.base.service;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * サービスプロパティ取得時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ServicePropertyException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>ServicePropertyException</code> を構築します。
     */
    public ServicePropertyException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>ServicePropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public ServicePropertyException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>ServicePropertyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public ServicePropertyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
