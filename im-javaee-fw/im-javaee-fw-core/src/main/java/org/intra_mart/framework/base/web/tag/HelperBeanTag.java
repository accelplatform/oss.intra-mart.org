/*
 * HelperBeanTag.java
 *
 * Created on 2002/07/11, 18:22
 */

package org.intra_mart.framework.base.web.tag;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.framework.base.web.bean.HelperBean;

import javax.servlet.jsp.JspException;

/**
 * HelperBeanタグです。
 *
 * @author INTRAMART
 * @since 3.2
 */
public class HelperBeanTag extends BodyTagSupport {

    /**
     * クラス名
     */
    private String helperclass;

    /**
     * HelperBeanTagを新規に生成します。
     */
    public HelperBeanTag() {
    }

    /**
     * 開始タグを検知したときにJSPエンジンから呼ばれます。
     * HelperBeanタグを解釈します。
     *
     * @return SKIP_BODY
     * @throws JspException タグの解釈時に例外が発生
     */
    public int doStartTag() throws JspException {
        HelperBean bean = null;
        HttpServletRequest request = null;
        HttpServletResponse response = null;

        // HelperBeanの生成
        try {
            bean = (HelperBean)Class.forName(getHelperclass()).newInstance();
        } catch (Exception e) {
            throw new JspException(e.getMessage(), e);
        }

        // リクエストの設定
        request = (HttpServletRequest)pageContext.getRequest();
        bean.setRequest(request);

        // レスポンスの設定
        response = (HttpServletResponse)pageContext.getResponse();
        bean.setResponse(response);

        // 初期化
        try {
            bean.init();
        } catch (Exception e) {
            throw new JspException(e.getMessage(), e);
        }

        // HelperBeanをスクリプティング変数へ設定
        pageContext.setAttribute(getId(), bean);

        // ボディ部分の評価なし
        return SKIP_BODY;
    }

    /**
     * クラス名を設定します。
     *
     * @param className クラス名
     * @deprecated このメソッドではなく{@link #setHelperclass(java.lang.String)}メソッドを使用してください。
     */
    public void setClass(String className) {
        setHelperclass(className);
    }

    /**
     * クラス名を設定します。
     *
     * @param helperclass クラス名
     * @since 4.0
     */
    public void setHelperclass(String helperclass) {
        this.helperclass = helperclass;
    }

    /**
     * クラス名を取得します。
     *
     * @return クラス名
     * @since 4.0
     */
    public String getHelperclass() {
        return this.helperclass;
    }
}
