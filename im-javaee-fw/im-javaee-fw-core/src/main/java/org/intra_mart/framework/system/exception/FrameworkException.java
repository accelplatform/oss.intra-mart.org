/*
 * FrameworkException.java
 *
 * Created on 2001/10/25, 17:14
 */

package org.intra_mart.framework.system.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * フレームワークに共通するの例外のスーパークラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class FrameworkException extends Exception {

    /**
     * 詳細メッセージを指定しないで <code>FrameworkException</code> を構築します。
     */
    public FrameworkException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>FrameworkException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public FrameworkException(String msg) {
        super(msg);
    }

    /**
     * この例外の発生原因となる例外を持つ <code>FrameworkException</code> を構築します。
     *
     * @param throwable この例外の発生原因となる例外
     */
    public FrameworkException(Throwable throwable) {
        super(throwable);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>FrameworkException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public FrameworkException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    /**
     * この例外が発生した原因となる例外を取得します。
     *
     * @return この例外が発生した原因となる例外
     */
    public Throwable getException() {
        return getCause();
    }

    public void printStackTrace() {
        super.printStackTrace();
    }

    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
    }

    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
    }
}
