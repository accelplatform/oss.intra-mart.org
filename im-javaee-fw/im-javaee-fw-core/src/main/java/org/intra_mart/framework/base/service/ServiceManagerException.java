/*
 * ServiceManagerException.java
 *
 * Created on 2001/12/17, 14:35
 */

package org.intra_mart.framework.base.service;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * サービスマネージャ設定時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ServiceManagerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>ServiceManagerException</code> を構築します。
     */
    public ServiceManagerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>ServiceManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public ServiceManagerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>ServiceManagerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public ServiceManagerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
