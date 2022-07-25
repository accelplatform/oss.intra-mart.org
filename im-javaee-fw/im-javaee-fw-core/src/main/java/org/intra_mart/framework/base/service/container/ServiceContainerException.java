/*
 * ServiceManagerException.java
 *
 * Created on 2001/12/17, 14:35
 */

package org.intra_mart.framework.base.service.container;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * サービスコンテナ設定時の例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ServiceContainerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>ServiceContainerException</code> を構築します。
     */
    public ServiceContainerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>ServiceContainerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public ServiceContainerException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>ServiceContainerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public ServiceContainerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
