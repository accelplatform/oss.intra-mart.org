/*
 * MessageParamTag.java
 *
 * Created on 2003/07/08, 16:00
 */

package org.intra_mart.framework.base.web.tag;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;

/**
 * メッセージの生成で使用するパラメータを設定します。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class MessageParamTag extends BodyTagSupport {

    /**
     * パラメータの値
     */
    private Object value;

    /**
     * ParamTagを新規に生成します。
     */
    public MessageParamTag() {
        super();
    }

    /**
     * 外側の一番近い{@link MessageTag}にパラメータを設定します。
     *
     * @throws JspException JSP出力時に例外が発生
     * @return EVAL_BODY_BUFFERED
     */
    public int doStartTag() throws JspException {
        try {
            MessageTag parent =
                (MessageTag) findAncestorWithClass(this, MessageTag.class);
            if (parent != null) {
                parent.addParameter(getValue());
            }
        } catch (Exception e) {
            throw new FrameworkTagException(e.getMessage(), e);
        }

        return EVAL_BODY_BUFFERED;
    }

    /**
     * パラメータの値を取得します。
     *
     * @return パラメータの値
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * パラメータの値を設定します。
     *
     * @param value パラメータの値
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
