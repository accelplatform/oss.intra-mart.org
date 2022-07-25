package org.intra_mart.framework.system.exception;

/**
 * イントラマートコンテナ内で発生した例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class IMContainerException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>IMContainerException</code> を構築します。
     */
    public IMContainerException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>IMContainerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public IMContainerException(String msg) {
        super(msg);
    }

    /**
     * この例外の発生原因となる例外を持つ <code>IMContainerException</code> を構築します。
     *
     * @param throwable この例外の発生原因となる例外
     */
    public IMContainerException(Throwable throwable) {
        super(throwable);
    }
    
    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>IMContainerException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public IMContainerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
