/*
 * HelperBeanException.java
 *
 * Created on 2002/02/28, 18:45
 */

package org.intra_mart.framework.base.web.bean;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * HelperBean内で発生した例外です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class HelperBeanException extends SystemException {

    /**
     * 詳細メッセージを指定しないで <code>HelperBeanException</code> を構築します。
     */
    public HelperBeanException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>HelperBeanException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public HelperBeanException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>HelperBeanException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public HelperBeanException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
