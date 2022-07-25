/*
 * TransitionException.java
 *
 * Created on 2001/12/25, 14:48
 */

package org.intra_mart.framework.base.service;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * トランジションに関連する例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class TransitionException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>TransitionException</code> を構築します。
     */
    public TransitionException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>TransitionException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public TransitionException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>TransitionException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public TransitionException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
