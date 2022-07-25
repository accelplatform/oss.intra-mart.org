package org.intra_mart.framework.system.exception;

/**
 * フレームワーク内で発生したランタイム例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class FrameworkRuntimeException extends RuntimeException {


    /**
     * 詳細メッセージを指定しないで <code>FrameworkRuntimeException</code> を構築します。
     */
    public FrameworkRuntimeException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>FrameworkRuntimeException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public FrameworkRuntimeException(String msg) {
        super(msg);
    }

    /**
     * この例外の発生原因となる例外を持つ <code>FrameworkRuntimeException</code> を構築します。
     *
     * @param throwable この例外の発生原因となる例外
     */
    public FrameworkRuntimeException(Throwable throwable) {
        super(throwable);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>FrameworkRuntimeException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public FrameworkRuntimeException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
