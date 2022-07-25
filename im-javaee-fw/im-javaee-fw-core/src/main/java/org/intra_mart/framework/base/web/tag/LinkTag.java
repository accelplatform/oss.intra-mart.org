/*
 * LinkTag.java
 *
 * Created on 2002/01/28, 14:16
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
import org.intra_mart.framework.base.web.tag.attribute.EventsAttributes;
import org.intra_mart.framework.base.web.tag.attribute.I18nAttributes;

/**
 * LINKタグです。
 * intra-martフレームワークを使用する場合のセッションフレームワーク、サービスフレームワークの一部となります。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class LinkTag extends BodyTagSupport implements ParameterSendable {

    /**
     * サービスマネージャ
     */
    private ServiceManager serviceManager;

    /**
     * コア属性
     */
    private CoreAttributes coreAttrs;

    /**
     * 国際化属性
     */
    private I18nAttributes i18n;

    /**
     * イベント属性
     */
    private EventsAttributes events;

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
     * charset
     */
    private String charset;

    /**
     * type
     */
    private String type;

    /**
     * name
     */
    private String name;

    /**
     * hreflang
     */
    private String hreflang;

    /**
     * rel
     */
    private String rel;

    /**
     * rev
     */
    private String rev;

    /**
     * accesskey
     */
    private String accesskey;

    /**
     * shape
     */
    private String shape;

    /**
     * coords
     */
    private String coords;

    /**
     * target
     *
     * @since 3.2
     */
    private String target;

    /**
     * tabindex
     */
    private String tabindex;

    /**
     * onfocus
     */
    private String onfocus;

    /**
     * onblur
     */
    private String onblur;

    /**
     * LinkTagを新規に生成します。
     */
    public LinkTag() {
        super();
        try {
            this.serviceManager = ServiceManager.getServiceManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.params = null;
        this.coreAttrs = new CoreAttributes();
        this.i18n = new I18nAttributes();
        this.events = new EventsAttributes();
    }

    /**
     * 開始タグを検知したときにJSPエンジンから呼ばれます。
     * LINKタグを解釈します。
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
     * LINKタグを終了します。
     *
     * @return EVAL_PAGE
     * @throws JspException タグの終了時に例外が発生
     */
    public int doEndTag() throws JspException {
        String contextPath = null;
        String url = null;
        Vector hrefParams = null;
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
            hrefParams = new Vector();
            if (this.params != null) {
                hrefParams.addAll(this.params);
            }

            // リクエストとレスポンスの取得
            HttpServletRequest request =
                (HttpServletRequest)pageContext.getRequest();
            HttpServletResponse response =
                (HttpServletResponse)pageContext.getResponse();

            // 文字エンコーディングの取得
            String encoding =
                this.serviceManager.getEncoding(request, response);

            url =((HttpServletResponse)pageContext
                        .getResponse())
                        .encodeURL(TagWriter.createURL(
                tempPath, hrefParams, encoding));

            // Aタグ開始
            pageContext.getOut().print("<A href=\"" + url + "\"");

            // charset
            writer.printAttribute("charset", getCharset());

            // type
            writer.printAttribute("type", getType());

            // name
            writer.printAttribute("name", getName());

            // hreflang
            writer.printAttribute("hreflang", getHreflang());

            // rel
            writer.printAttribute("rel", getRel());

            // rev
            writer.printAttribute("rev", getRev());

            // accesskey
            writer.printAttribute("accesskey", getAccesskey());

            // shape
            writer.printAttribute("shape", getShape());

            // coords
            writer.printAttribute("coords", getCoords());

            // tabindex
            writer.printAttribute("tabindex", getTabindex());

            // target
            writer.printAttribute("target", getTarget());

            // onFocus
            writer.printAttribute("onFocus", getOnfocus());

            // onBlur
            writer.printAttribute("onBlur", getOnblur());

            // ID
            writer.printAttribute("id", getId());

            // コア属性
            this.coreAttrs.setTagWriter(writer);
            this.coreAttrs.printAttributes();

            // 国際化属性
            this.i18n.setTagWriter(writer);
            this.i18n.printAttributes();

            // イベント属性
            this.events.setTagWriter(writer);
            this.events.printAttributes();

            // 開始タグの終了
            pageContext.getOut().print(">");

            // 内容の表示
            pageContext.getOut().print(bodyContent.getString());

            // タグの終了
            pageContext.getOut().print("</A>");
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
     * charsetを取得します。
     *
     * @return charset
     */
    public String getCharset() {
        return this.charset;
    }

    /**
     * charsetを設定します。
     *
     * @param charset charset
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * typeを取得します。
     *
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * typeを設定します。
     *
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
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
     * hreflangを取得します。
     *
     * @return hreflang
     */
    public String getHreflang() {
        return this.hreflang;
    }

    /**
     * hreflangを設定します。
     *
     * @param hreflang hreflang
     */
    public void setHreflang(String hreflang) {
        this.hreflang = hreflang;
    }

    /**
     * relを取得します。
     *
     * @return rel
     */
    public String getRel() {
        return this.rel;
    }

    /**
     * relを設定します。
     *
     * @param rel rel
     */
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
     * revを取得します。
     *
     * @return rev
     */
    public String getRev() {
        return this.rev;
    }

    /**
     * relを設定します。
     *
     * @param rev rev
     */
    public void setRev(String rev) {
        this.rev = rev;
    }

    /**
     * accesskeyを取得します。
     *
     * @return accesskey
     */
    public String getAccesskey() {
        return this.accesskey;
    }

    /**
     * accesskeyを設定します。
     *
     * @param accesskey accesskey
     */
    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    /**
     * shapeを取得します。
     *
     * @return shape
     */
    public String getShape() {
        return this.shape;
    }

    /**
     * shapeを設定します。
     *
     * @param shape shape
     */
    public void setShape(String shape) {
        this.shape = shape;
    }

    /**
     * coordsを取得します。
     *
     * @return coords
     */
    public String getCoords() {
        return this.coords;
    }

    /**
     * coordsを設定します。
     *
     * @param coords coords
     */
    public void setCoords(String coords) {
        this.coords = coords;
    }

    /**
     * targetを取得します。
     *
     * @return target
     * @since 3.2
     */
    public String getTarget() {
        return this.target;
    }

    /**
     * targetを設定します。
     *
     * @param target target
     * @since 3.2
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * tabindexを取得します。
     *
     * @return tabindex
     */
    public String getTabindex() {
        return this.tabindex;
    }

    /**
     * tabindexを設定します。
     *
     * @param tabindex tabindex
     */
    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    /**
     * onfocusを取得します。
     *
     * @return onfocus
     */
    public String getOnfocus() {
        return this.onfocus;
    }

    /**
     * onfocusを設定します。
     *
     * @param onfocus onfocus
     */
    public void setOnfocus(String onfocus) {
        this.onfocus = onfocus;
    }

    /**
     * onblurを取得します。
     *
     * @return onblur
     */
    public String getOnblur() {
        return this.onblur;
    }

    /**
     * onblurを設定します。
     *
     * @param onblur onblur
     */
    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }

    /**
     * linkClassを取得します。
     *
     * @return linkClass
     * @deprecated このメソッドではなく{@link #getLinkclass()}メソッドを使用してください。
     */
    public String getLinkClass() {
        return getLinkclass();
    }

    /**
     * linkClassを設定します。
     *
     * @param linkClass class
     * @deprecated このメソッドではなく{@link #setLinkclass(String)}メソッドを使用してください。
     */
    public void setLinkClass(String linkClass) {
        setLinkclass(linkClass);
    }

    /**
     * linkClassを取得します。
     *
     * @return linkClass
     */
    public String getLinkclass() {
        return this.coreAttrs.getTagClass();
    }

    /**
     * linkClassを設定します。
     *
     * @param linkClass class
     */
    public void setLinkclass(String linkClass) {
        this.coreAttrs.setTagClass(linkClass);
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

    /**
     * langを取得します。
     *
     * @return lang
     */
    public String getLang() {
        return this.i18n.getLang();
    }

    /**
     * langを設定します。
     *
     * @param lang lang
     */
    public void setLang(String lang) {
        this.i18n.setLang(lang);
    }

    /**
     * dirを取得します。
     *
     * @return dir
     */
    public String getDir() {
        return this.i18n.getDir();
    }

    /**
     * dirを設定します。
     *
     * @param dir dir
     */
    public void setDir(String dir) {
        this.i18n.setDir(dir);
    }

    /**
     * 属性<CODE>onClick</CODE>の値を取得します。
     *
     * @return onClick
     */
    public String getOnclick() {
        return this.events.getOnClick();
    }

    /**
     * 属性<CODE>onClick</CODE>の値を設定します。
     *
     * @param onClick onClick
     */
    public void setOnclick(String onClick) {
        this.events.setOnClick(onClick);
    }

    /**
     * 属性<CODE>onDblClick</CODE>の値を取得します。
     *
     * @return onDblClick
     */
    public String getOndblclick() {
        return this.events.getOnDblClick();
    }

    /**
     * 属性<CODE>obDblClick</CODE>の値を設定します。
     *
     * @param onDblClick onDblClick
     */
    public void setOndblclick(String onDblClick) {
        this.events.setOnDblClick(onDblClick);
    }

    /**
     * 属性<CODE>onMouseDown</CODE>の値を取得します。
     *
     * @return onMouseDown
     */
    public String getOnmousedown() {
        return this.events.getOnMouseDown();
    }

    /**
     * 属性<CODE>onMouseDown</CODE>の値を設定します。
     *
     * @param onMouseDown onMouseDown
     */
    public void setOnmousedown(String onMouseDown) {
        this.events.setOnMouseDown(onMouseDown);
    }

    /**
     * 属性<CODE>onMouseUp</CODE>の値を取得します。
     *
     * @return onMouseUp
     */
    public String getOnmouseup() {
        return this.events.getOnMouseUp();
    }

    /**
     * 属性<CODE>onMouseUp</CODE>の値を設定します。
     *
     * @param onMouseUp onMouseUp
     */
    public void setOnmouseup(String onMouseUp) {
        this.events.setOnMouseUp(onMouseUp);
    }

    /**
     * 属性<CODE>onMouseOver</CODE>の値を取得します。
     *
     * @return onMouseOver
     */
    public String getOnmouseover() {
        return this.events.getOnMouseOver();
    }

    /**
     * 属性<CODE>onMouseOver</CODE>の値を設定します。
     *
     * @param onMouseOver onMouseOver
     */
    public void setOnmouseover(String onMouseOver) {
        this.events.setOnMouseOver(onMouseOver);
    }

    /**
     * 属性<CODE>onMouseMove</CODE>の値を取得します。
     *
     * @return onMouseMove
     */
    public String getOnmousemove() {
        return this.events.getOnMouseMove();
    }

    /**
     * 属性<CODE>onMouseMove</CODE>の値を設定します。
     *
     * @param onMouseMove onMouseMove
     */
    public void setOnmousemove(String onMouseMove) {
        this.events.setOnMouseMove(onMouseMove);
    }

    /**
     * 属性<CODE>onMouseOut</CODE>の値を取得します。
     *
     * @return onMouseOut
     */
    public String getOnmouseout() {
        return this.events.getOnMouseOut();
    }

    /**
     * 属性<CODE>onMouseOut</CODE>の値を設定します。
     *
     * @param onMouseOut onMouseOut
     */
    public void setOnmouseout(String onMouseOut) {
        this.events.setOnMouseOut(onMouseOut);
    }

    /**
     * 属性<CODE>onKeyPress</CODE>の値を取得します。
     *
     * @return onKeyPress
     */
    public String getOnkeypress() {
        return this.events.getOnKeyPress();
    }

    /**
     * 属性<CODE>onKeyPress</CODE>の値を設定します。
     *
     * @param onKeyPress onKeyPress
     */
    public void setOnkeypress(String onKeyPress) {
        this.events.setOnKeyPress(onKeyPress);
    }

    /**
     * 属性<CODE>onKeyDown</CODE>の値を取得します。
     *
     * @return onKeyDown
     */
    public String getOnkeydown() {
        return this.events.getOnKeyDown();
    }

    /**
     * 属性<CODE>onKeyDown</CODE>の値を設定します。
     *
     * @param onKeyDown onKeyDown
     */
    public void setOnkeydown(String onKeyDown) {
        this.events.setOnKeyDown(onKeyDown);
    }

    /**
     * 属性<CODE>onKeyUp</CODE>の値を取得します。
     *
     * @return onKeyUp
     */
    public String getOnkeyup() {
        return this.events.getOnKeyUp();
    }

    /**
     * 属性<CODE>onKeyUp</CODE>の値を設定します。
     *
     * @param onKeyUp onKeyUp
     */
    public void setOnkeyup(String onKeyUp) {
        this.events.setOnKeyUp(onKeyUp);
    }
}
