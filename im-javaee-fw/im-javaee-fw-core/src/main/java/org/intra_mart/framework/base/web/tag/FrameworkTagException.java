/*
 * FrameworkTagException.java
 *
 * Created on 2002/03/10, 14:37
 */

package org.intra_mart.framework.base.web.tag;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.jsp.JspException;

/**
 * 拡張タグの例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class FrameworkTagException extends JspException {

    /**
     * 詳細メッセージを指定しないで <code>FrameworkTagException</code> を構築します。
     */
    public FrameworkTagException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>FrameworkTagException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public FrameworkTagException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>FrameworkTagException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public FrameworkTagException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    /**
     * この例外の発生原因となる例外を持つ <code>FrameworkTagException</code> を構築します。
     *
     * @param throwable この例外の発生原因となる例外
     */
    public FrameworkTagException(Throwable throwable) {
        super(throwable);
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (getRootCause() != null) {
            getRootCause().printStackTrace();
        }
    }

    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
        if (getRootCause() != null) {
            getRootCause().printStackTrace(printStream);
        }
    }

    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        if (getRootCause() != null) {
            getRootCause().printStackTrace(printWriter);
        }
    }
}
