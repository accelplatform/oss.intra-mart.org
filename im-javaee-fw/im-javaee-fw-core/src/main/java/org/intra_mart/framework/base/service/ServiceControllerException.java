/*
 * ServiceControllerException.java
 *
 * Created on 2001/12/25, 15:58
 */

package org.intra_mart.framework.base.service;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * サービスコントローラに関連する例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ServiceControllerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>ServiceControllerException</code> を構築します。
     */
    public ServiceControllerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>ServiceControllerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public ServiceControllerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>ServiceControllerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public ServiceControllerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
