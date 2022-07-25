/*
 * ErrorHelperBean.java
 *
 * Created on 2002/02/28, 17:21
 */

package org.intra_mart.framework.base.web.bean;

/**
 * 例外ページで使用するHelperBeanです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ErrorHelperBean extends HelperBean {

    /**
     * 例外
     */
    private Throwable exception;

    /**
     * ErrorHelperBeanを新規に生成します。
     *
     * @throws HelperBeanException ErrorHelperBean生成時に例外が発生
     */
    public ErrorHelperBean() throws HelperBeanException {
        super();
        this.exception = null;
    }

    /**
     * 例外を取得します。
     *
     * @return 例外
     */
    public Throwable getException() throws HelperBeanException {
        if (this.exception == null) {
            synchronized (this) {
                if (this.exception == null) {
                    try {
                        this.exception = (Throwable)getRequest().getAttribute(getServicePropertyHandler().getExceptionAttributeName());
                    } catch (Exception e) {
                        throw new HelperBeanException(e.getMessage(), e);
                    }
                }
            }
        }

        return this.exception;
    }
}
