/*
 * FormTag.java
 *
 * Created on 2002/01/15, 17:24
 */

package org.intra_mart.framework.base.web.tag;

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
import org.intra_mart.framework.system.exception.FrameworkRuntimeException;

/**
 * FORMタグです。
 * intra-martフレームワークを使用する場合のセッションフレームワーク、サービスフレームワークの一部となります。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class FormTag extends BodyTagSupport {

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
     * アプリケーション
     */
    private String application;

    /**
     * サービス
     */
    private String service;

    /**
     * method
     */
    private String method;

    /**
     * enctype
     */
    private String enctype;

    /**
     * accept
     */
    private String accept;

    /**
     * name
     */
    private String name;

    /**
     * onSubmit
     */
    private String onSubmit;

    /**
     * onReset
     */
    private String onReset;

    /**
     * accept-charset
     */
    private String acceptCharset;

    /**
     * target
     */
    private String target;

    /**
     * FormTagを新規に生成します。
     */
    public FormTag() {
        super();
        try {
            this.serviceManager = ServiceManager.getServiceManager();
        } catch (Exception e) {
            throw new FrameworkRuntimeException(e);
        }
        this.coreAttrs = new CoreAttributes();
        this.i18n = new I18nAttributes();
        this.events = new EventsAttributes();
    }

    /**
     * 開始タグを検知したときにJSPエンジンから呼ばれます。
     * FORMタグを解釈します。
     *
     * @return EVAL_BODY_BUFFERED
     * @throws JspException タグの解釈時に例外が発生
     */
    public int doStartTag() throws JspException {
        String contextPath = null;
        String url = null;
        Vector serviceParams = null;
        String tempPath = null;

        try {
            TagWriter writer = new TagWriter(pageContext.getOut());

            contextPath = ((HttpServletRequest) pageContext.getRequest())
					.getContextPath();

            tempPath = contextPath
				+ "/"
				+ getApplication()
				+ ServiceServlet.REQUEST_SEPARATOR
				+ getService()
				+ "."
				+ serviceManager.getExtesion();

            // パラメータ
            serviceParams = new Vector();

            // リクエストとレスポンスの取得
            HttpServletRequest request =
                (HttpServletRequest)pageContext.getRequest();
            HttpServletResponse response =
                (HttpServletResponse)pageContext.getResponse();

            // 文字エンコーディングの取得
            String encoding =
                this.serviceManager.getEncoding(request, response);

            // セッションを含めたURLの取得
            url =
                ((HttpServletResponse)pageContext.getResponse()).encodeURL(
                    TagWriter.createURL(tempPath, serviceParams, encoding));
            // FORMタグ開始
            pageContext.getOut().print("<FORM action=\"" + url + "\"");

            // method
            if (getMethod() != null && !getMethod().equals("")) {
                writer.printAttribute("method", getMethod());
            }

            // enctype
            writer.printAttribute("enctype", getEnctype());

            // accept
            writer.printAttribute("accept", getAccept());

            // name
            writer.printAttribute("name", getName());

            // onSubmit
            writer.printAttribute("onSubmit", getOnsubmit());

            // onReset
            writer.printAttribute("onReset", getOnreset());

            // accept-charset
            writer.printAttribute("accept-charset", getAcceptcharset());

            // target
            writer.printAttribute("target", getTarget());

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

            // タグの終了
            pageContext.getOut().print(">");

        } catch (Exception e) {
            throw new FrameworkTagException(e.getMessage(), e);
        }

        return EVAL_BODY_BUFFERED;
    }

    /**
     * 終了タグを検知したときにJSPエンジンから呼ばれます。
     * FORMタグを終了します。
     *
     * @return EVAL_PAGE
     * @throws JspException タグの終了時に例外が発生
     */
    public int doEndTag() throws JspException {
        try {
            // 内容の表示
            pageContext.getOut().print(bodyContent.getString());

            // タグの終了
            pageContext.getOut().print("</FORM>");
        } catch (Exception e) {
            throw new FrameworkTagException(e.getMessage(), e);
        }

        return EVAL_PAGE;
    }

    /**
     * 属性<CODE>application</CODE>の値を取得します。
     *
     * @return アプリケーション
     */
    public String getApplication() {
        return this.application;
    }

    /**
     * 属性<CODE>application</CODE>の値を設定します。
     *
     * @param application アプリケーション
     */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * 属性<CODE>service</CODE>の値を取得します。
     *
     * @return サービス
     */
    public String getService() {
        return this.service;
    }

    /**
     * 属性<CODE>service</CODE>の値を設定します。
     *
     * @param service サービス
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * 属性<CODE>method</CODE>の値を取得します。
     *
     * @return method
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * 属性<CODE>method</CODE>の値を設定します。
     *
     * @param method method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 属性<CODE>enctype</CODE>の値を取得します。
     *
     * @return enctype
     */
    public String getEnctype() {
        return this.enctype;
    }

    /**
     * 属性<CODE>target</CODE>の値を取得します。
     *
     * @return target
     */
    public String getTarget() {
        return this.target;
    }

    /**
     * 属性<CODE>target</CODE>の値を設定します。
     *
     * @param target target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * 属性<CODE>enctype</CODE>の値を設定します。
     *
     * @param enctype enctype
     */
    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    /**
     * 属性<CODE>accept</CODE>の値を取得します。
     *
     * @return accept
     */
    public String getAccept() {
        return this.accept;
    }

    /**
     * 属性<CODE>accept</CODE>の値を設定します。
     *
     * @param accept accept
     */
    public void setAccept(String accept) {
        this.accept = accept;
    }

    /**
     * 属性<CODE>name</CODE>の値を取得します。
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * 属性<CODE>name</CODE>の値を設定します。
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 属性<CODE>onSubmit</CODE>の値を取得します。
     *
     * @return onSubmit
     */
    public String getOnsubmit() {
        return this.onSubmit;
    }

    /**
     * 属性<CODE>onSubmit</CODE>の値を設定します。
     *
     * @param onSubmit onSubmit
     */
    public void setOnsubmit(String onSubmit) {
        this.onSubmit = onSubmit;
    }

    /**
     * 属性<CODE>onReset</CODE>の値を取得します。
     *
     * @return onReset
     */
    public String getOnreset() {
        return this.onReset;
    }

    /**
     * 属性<CODE>onReset</CODE>の値を設定します。
     *
     * @param onReset onReset
     */
    public void setOnreset(String onReset) {
        this.onReset = onReset;
    }

    /**
     * 属性<CODE>accept-charset</CODE>の値を取得します。
     *
     * @return accept-charset
     */
    public String getAcceptcharset() {
        return this.acceptCharset;
    }

    /**
     * 属性<CODE>accept-charset</CODE>の値を設定します。
     *
     * @param acceptCharset accept-charset
     */
    public void setAcceptcharset(String acceptCharset) {
        this.acceptCharset = acceptCharset;
    }

    /**
     * 属性<CODE>class</CODE>の値を取得します。
     *
     * @return class
     */
    public String getFormclass() {
        return this.coreAttrs.getTagClass();
    }

    /**
     * 属性<CODE>class</CODE>の値を設定します。
     *
     * @param formClass class
     */
    public void setFormclass(String formClass) {
        this.coreAttrs.setTagClass(formClass);
    }

    /**
     * 属性<CODE>style</CODE>の値を取得します。
     *
     * @return style
     */
    public String getStyle() {
        return this.coreAttrs.getStyle();
    }

    /**
     * 属性<CODE>style</CODE>の値を設定します。
     *
     * @param style style
     */
    public void setStyle(String style) {
        this.coreAttrs.setStyle(style);
    }

    /**
     * 属性<CODE>title</CODE>の値を取得します。
     *
     * @return title
     */
    public String getTitle() {
        return this.coreAttrs.getTitle();
    }

    /**
     * 属性<CODE>title</CODE>の値を設定します。
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.coreAttrs.setTitle(title);
    }

    /**
     * 属性<CODE>lang</CODE>の値を取得します。
     *
     * @return lang
     */
    public String getLang() {
        return this.i18n.getLang();
    }

    /**
     * 属性<CODE>lang</CODE>の値を設定します。
     *
     * @param lang lang
     */
    public void setLang(String lang) {
        this.i18n.setLang(lang);
    }

    /**
     * 属性<CODE>dir</CODE>の値を取得します。
     *
     * @return dir
     */
    public String getDir() {
        return this.i18n.getDir();
    }

    /**
     * 属性<CODE>dir</CODE>の値を設定します。
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
