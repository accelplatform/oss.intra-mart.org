/*
 * FrameTag.java
 *
 * Created on 2002/01/29, 11:39
 */

package org.intra_mart.framework.base.web.tag;

import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.intra_mart.framework.base.service.ServiceManager;
import org.intra_mart.framework.base.service.ServiceServlet;
import org.intra_mart.framework.base.web.tag.attribute.CoreAttributes;
import org.intra_mart.framework.system.exception.FrameworkRuntimeException;

/**
 * FRAMEタグです。
 * intra-martフレームワークを使用する場合のセッションフレームワーク、サービスフレームワークの一部となります。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class FrameTag extends BodyTagSupport implements ParameterSendable {

    /**
     * サービスマネージャ
     */
    private ServiceManager serviceManager;

    /**
     * コア属性
     */
    private CoreAttributes coreAttrs;

    /**
     * パラメータの集合
     */
    private Collection params;

    /**
     * アプリケーション
     */
    private String application;

    /**
     * サービス
     */
    private String service;

    /**
     * longdesc
     */
    private String longdesc;

    /**
     * name
     */
    private String name;

    /**
     * frameborder
     */
    private String frameborder;

    /**
     * marginwidth
     */
    private String marginwidth;

    /**
     * marginheight
     */
    private String marginheight;

    /**
     * noresize
     */
    private String noresize;

    /**
     * scrolling
     */
    private String scrolling;

    /**
     * FrameTagを新規に生成します。
     */
    public FrameTag() {
        super();
        try {
            this.serviceManager = ServiceManager.getServiceManager();
        } catch (Exception e) {
            throw new FrameworkRuntimeException(e);
        }
        this.params = null;
        this.coreAttrs = new CoreAttributes();
    }

    /**
     * 開始タグを検知したときにJSPエンジンから呼ばれます。
     * FRAMEタグを解釈します。
     *
     * @return EVAL_BODY_BUFFERED
     * @throws JspException タグの解釈時に例外が発生
     */
    public int doStartTag() throws JspException {
        this.params = new Vector();

        return EVAL_BODY_BUFFERED;
    }

    /**
     * 終了タグを検知したときにJSPエンジンから呼ばれます。
     * FRAMEタグを終了します。
     *
     * @return EVAL_PAGE
     * @throws JspException タグの終了時に例外が発生
     */
    public int doEndTag() throws JspException {
        String contextPath = null;
        String url = null;
        Vector srcParams = null;
        String tempPath = null;

        try {
            TagWriter writer = new TagWriter(pageContext.getOut());

            contextPath =
                    ((HttpServletRequest)pageContext.getRequest())
                        .getContextPath();

            tempPath = contextPath
				+ "/"
				+ getApplication()
				+ ServiceServlet.REQUEST_SEPARATOR
				+ getService()
				+ "."
				+ serviceManager.getExtesion();
            
            // パラメータ
            srcParams = new Vector();
            if (this.params != null) {
                srcParams.addAll(this.params);
            }

            // リクエストとレスポンスの取得
            HttpServletRequest request =
                (HttpServletRequest)pageContext.getRequest();
            HttpServletResponse response =
                (HttpServletResponse)pageContext.getResponse();

            // エンコーディングの取得
            String encoding =
                this.serviceManager.getEncoding(request, response);

	        url =
	            ((HttpServletResponse)pageContext.getResponse()).encodeURL(
	                TagWriter.createURL(tempPath, srcParams, encoding));

            // FRAMEタグ開始
            pageContext.getOut().print("<FRAME src=\"" + url + "\"");

            // longdesc
            writer.printAttribute("longdesc", getLongdesc());

            // name
            writer.printAttribute("name", getName());

            // frameborder
            writer.printAttribute("frameborder", getFrameborder());

            // marginwidth
            writer.printAttribute("marginwidth", getMarginwidth());

            // marginheight
            writer.printAttribute("marginheight", getMarginheight());

            // noresize
            writer.printAttribute("noresize", getNoresize());

            // scrolling
            writer.printAttribute("scrolling", getScrolling());

            // ID
            writer.printAttribute("id", getId());

            // コア属性
            this.coreAttrs.setTagWriter(writer);
            this.coreAttrs.printAttributes();

            // 開始タグの終了
            pageContext.getOut().print(">");

            // 内容の表示
            if (bodyContent != null) {
                pageContext.getOut().print(bodyContent.getString());
            }

            // タグの終了
            pageContext.getOut().print("</FRAME>");
        } catch (Exception e) {
            throw new FrameworkTagException(e.getMessage(), e);
        }

        return EVAL_PAGE;
    }

    /**
     * パラメータを追加します。
     *
     * @param name パラメータ名
     * @param value パラメータの値
     */
    public void addParameter(String name, String value) {
        FrameworkTagParam param = new FrameworkTagParam();
        param.setName(name);
        param.setValue(value);
        this.params.add(param);
    }

    /**
     * アプリケーションを取得します。
     *
     * @return アプリケーション
     */
    public String getApplication() {
        return this.application;
    }

    /**
     * アプリケーションを設定します。
     *
     * @param application アプリケーション
     */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * サービスを取得します。
     *
     * @return サービス
     */
    public String getService() {
        return this.service;
    }

    /**
     * serviceを設定します。
     *
     * @param service サービス
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * longdescを取得します。
     *
     * @return longdesc
     */
    public String getLongdesc() {
        return this.longdesc;
    }

    /**
     * longdescを設定します。
     *
     * @param longdesc longdesc
     */
    public void setLongdesc(String longdesc) {
        this.longdesc = longdesc;
    }

    /**
     * nameを取得します。
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * nameを設定します。
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * frameborderを取得します。
     *
     * @return frameborder
     */
    public String getFrameborder() {
        return this.frameborder;
    }

    /**
     * frameborderを設定します。
     *
     * @param frameborder frameborder
     */
    public void setFrameborder(String frameborder) {
        this.frameborder = frameborder;
    }

    /**
     * marginwidthを取得します。
     *
     * @return marginwidth
     */
    public String getMarginwidth() {
        return this.marginwidth;
    }

    /**
     * marginwidthを設定します。
     *
     * @param marginwidth marginwidth
     */
    public void setMarginwidth(String marginwidth) {
        this.marginwidth = marginwidth;
    }

    /**
     * marginheightを取得します。
     *
     * @return marginheight
     */
    public String getMarginheight() {
        return this.marginheight;
    }

    /**
     * marginheightを設定します。
     *
     * @param marginheight marginheight
     */
    public void setMarginheight(String marginheight) {
        this.marginheight = marginheight;
    }

    /**
     * noresizeを取得します。
     *
     * @return noresize
     */
    public String getNoresize() {
        return this.noresize;
    }

    /**
     * noresizeを設定します。
     *
     * @param noresize noresize
     */
    public void setNoresize(String noresize) {
        this.noresize = noresize;
    }

    /**
     * scrollingを取得します。
     *
     * @return scrolling
     */
    public String getScrolling() {
        return this.scrolling;
    }

    /**
     * scrollingを設定します。
     *
     * @param scrolling scrolling
     */
    public void setScrolling(String scrolling) {
        this.scrolling = scrolling;
    }

    /**
     * frameClassを取得します。
     *
     * @return frameClass
     * @deprecated このメソッドではなく{@link #getFrameclass()}メソッドを使用してください。
     */
    public String getFrameClass() {
        return getFrameclass();
    }

    /**
     * frameClassを設定します。
     *
     * @param frameClass frameClass
     * @deprecated このメソッドではなく{@link #setFrameclass(String)}メソッドを使用してください。
     */
    public void setFrameClass(String frameClass) {
        setFrameclass(frameClass);
    }

    /**
     * frameClassを取得します。
     *
     * @return frameClass
     */
    public String getFrameclass() {
        return this.coreAttrs.getTagClass();
    }

    /**
     * frameClassを設定します。
     *
     * @param frameClass frameClass
     */
    public void setFrameclass(String frameClass) {
        this.coreAttrs.setTagClass(frameClass);
    }

    /**
     * styleを取得します。
     *
     * @return style
     */
    public String getStyle() {
        return this.coreAttrs.getStyle();
    }

    /**
     * styleを設定します。
     *
     * @param style style
     */
    public void setStyle(String style) {
        this.coreAttrs.setStyle(style);
    }

    /**
     * titleを取得します。
     *
     * @return title
     */
    public String getTitle() {
        return this.coreAttrs.getTitle();
    }

    /**
     * titleを設定します。
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.coreAttrs.setTitle(title);
    }
}
