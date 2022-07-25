/*
 * ParamTag.java
 *
 * Created on 2002/01/29, 10:27
 */

package org.intra_mart.framework.base.web.tag;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;

/**
 * リンク先、遷移先に渡すパラメータを設定します。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ParamTag extends BodyTagSupport {

    /**
     * パラメータ名
     */
    private String name;

    /**
     * パラメータの値
     */
    private String value;

    /**
     * ParamTagを新規に生成します。
     */
    public ParamTag() {
        super();
    }

    /**
     * 外側の一番近い{@link ParameterSendable}にパラメータを設定します。
     *
     * @throws JspException JSP出力時に例外が発生
     * @return EVAL_BODY_BUFFERED
     */
    public int doStartTag() throws JspException {
        try {
            ParameterSendable parent = (ParameterSendable)findAncestorWithClass(this, ParameterSendable.class);
            if (parent != null) {
                parent.addParameter(getName(), getValue());
            }
        } catch (Exception e) {
            throw new FrameworkTagException(e.getMessage(), e);
        }

        return EVAL_BODY_BUFFERED;
    }

    /**
     * パラメータ名を取得します。
     *
     * @return パラメータ名
     */
    public String getName() {
        return this.name;
    }

    /**
     * パラメータ名を設定します。
     *
     * @param name パラメータ名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * パラメータの値を取得します。
     *
     * @return パラメータの値
     */
    public String getValue() {
        return this.value;
    }

    /**
     * パラメータの値を設定します。
     *
     * @param value パラメータの値
     */
    public void setValue(String value) {
        this.value = value;
    }
}
