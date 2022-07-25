/*
 * ServiceServletException.java
 *
 * Created on 2001/12/26, 10:28
 */

package org.intra_mart.framework.base.service;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * サービスに関連する例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ServiceServletException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>ServiceServletException</code> を構築します。
     */
    public ServiceServletException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>ServiceServletException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public ServiceServletException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>ServiceServletException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public ServiceServletException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
