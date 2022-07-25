/*
 * ApplicationException.java
 *
 * Created on 2001/11/21, 18:21
 */

package org.intra_mart.framework.system.exception;

/**
 * フレームワーク内で発生したアプリケーション例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ApplicationException extends FrameworkException {

    /**
     * 詳細メッセージを指定しないで <code>ApplicationException</code> を構築します。
     */
    public ApplicationException() {
    	super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>ApplicationException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public ApplicationException(String msg) {
        super(msg);
    }

    /**
     * この例外の発生原因となる例外を持つ <code>ApplicationException</code> を構築します。
     *
     * @param throwable この例外の発生原因となる例外
     */
    public ApplicationException(Throwable throwable) {
        super(throwable);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>ApplicationException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public ApplicationException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
