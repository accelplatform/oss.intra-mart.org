/*
 * SubmitLinkTag.java
 *
 * Created on 2003/07/16, 17:00
 */

package org.intra_mart.framework.base.web.tag;

import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
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
 * サブミット専用のLINKタグです。
 * im-J2EE Frameworkを使用する場合のセッションフレームワーク、サービスフレームワークの一部となります。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class SubmitLinkTag extends BodyTagSupport {

    private static class SubmitLinkEventsAttributes extends EventsAttributes {

        /**
         * onClickイベントに追加するスクリプト
         */
        private String onClickScript;

        /**
         * onClickイベントに追加するスクリプトを設定します。
         *
         * @param parent onClickイベントに追加するスクリプト
         */
        public void setOnClickScript(String onClickScript) {
            this.onClickScript = onClickScript;
        }

        /**
         * onClickイベントに追加するスクリプトを取得します。
         *
         * @return onClickイベントに追加するスクリプト
         */
        public String getOnClickScript() {
            return this.onClickScript;
        }

        /**
         * 属性<CODE>onClick</CODE>を出力します。
         *
         * @throws IOException 出力中に例外が発生
         */
        protected void printOnClick() throws IOException {
            // スクリプトを追加
            String script = getOnClickScript();

            // ユーザ定義のスクリプトが指定されている場合、
            // 内容によってサブミットするかどうかを決定
            if (getOnClick() != null) {
                String realScript = "";
                realScript =
                    "var myFunction = new Function(&quot;"
                        + getOnClick()
                        + "&quot;); var result = myFunction();"
                        + " if (result == undefined || result == true) {"
                        + script
                        + "};";
                script = realScript;
            }
            getTagWriter().printAttribute("onClick", script);
        }
    }

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
    private SubmitLinkEventsAttributes events;

    /**
     * アプリケーション
     */
    private String application;

    /**
     * サービス
     */
    private String service;

    /**
     * フォーム名
     */
    private String form;

    /**
     * name
     */
    private String name;

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
     * サービスマネージャ
     *
     * @since 4.3
     */
    private ServiceManager serviceManager;

    /**
     * SubmitLinkTagを新規に生成します。
     */
    public SubmitLinkTag() {
        super();
        try {
            this.serviceManager = ServiceManager.getServiceManager();
        } catch (Exception e) {
            throw new FrameworkRuntimeException(e);
        }
        this.coreAttrs = new CoreAttributes();
        this.i18n = new I18nAttributes();
        this.events = new SubmitLinkEventsAttributes();
    }

    /**
     * 開始タグを検知したときにJSPエンジンから呼ばれます。
     * SUBMIT LINKタグを解釈します。
     *
     * @return EVAL_BODY_BUFFERED
     * @throws JspException タグの解釈時に例外が発生
     */
    public int doStartTag() throws JspException {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * 終了タグを検知したときにJSPエンジンから呼ばれます。
     * SUBMIT LINKタグを終了します。
     *
     * @return EVAL_PAGE
     * @throws JspException タグの終了時に例外が発生
     */
    public int doEndTag() throws JspException {
        String contextPath = null;
        String url = null;
        FormTag targetForm = null;
        String formName = null;

        // 以下の優先順位でルールでサブミットするFormのname属性を決定
        // １．form属性が指定されている場合、そのまま使用
        // ２．外側のFormタグのname属性を使用
        // ３．その他の場合はFrameworkTagExceptionをthrow
        formName = getForm();
        if (formName == null || formName.trim().equals("")) {
            // 属性formが指定されていない場合、外部のFormタグを取得
            try {
                targetForm =
                    (FormTag)findAncestorWithClass(this, FormTag.class);
                if (targetForm == null) {
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.web.tag.i18n")
                                .getString("SubmitLinkTag.FormNameNeeds");
                    } catch (MissingResourceException e) {
                    }
                    throw new FrameworkTagException(message);
                }
            } catch (Exception e) {
                throw new FrameworkTagException(e.getMessage(), e);
            }
            formName = targetForm.getName();
        }

        try {
            Vector serviceParams = null;
            String tempPath = null;

            contextPath =
                ((HttpServletRequest)pageContext.getRequest())
                    .getContextPath();
            
            // URL生成。アプリケーションIDが設定されていない場合はformのアプリケーションIDを取得
            if (getApplication() == null  || getApplication().trim().length() == 0) {
                tempPath = contextPath
				+ "/"
				+ targetForm.getApplication()
				+ ServiceServlet.REQUEST_SEPARATOR
				+ getService()
				+ "."
				+ serviceManager.getExtesion();
            } else {
                tempPath = contextPath
				+ "/"
				+ getApplication()
				+ ServiceServlet.REQUEST_SEPARATOR
				+ getService()
				+ "."
				+ serviceManager.getExtesion();
            }
            
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

            // onClickイベントのスクリプトの設定
            String scriptActionVariableName = "im_action";
            String script = "";
            script += "var "
                + scriptActionVariableName
                + " = document."
                + formName
                + ".action; ";

            // document.mainform.action に値を代入する。
            if (url != null && url.trim().length() != 0) {
                script += "document." + formName + ".action = '" + url + "'; ";
            }

            script += "document." + formName + ".submit(); ";
            script += "document."
                + formName
                + ".action = "
                + scriptActionVariableName
                + "; ";
            script += "return false;";
            this.events.setOnClickScript(script);

            TagWriter writer = new TagWriter(pageContext.getOut());

            // Aタグ開始
            pageContext.getOut().print("<A href=\"javascript:void(0);\"");

            // name
            writer.printAttribute("name", getName());

            // accesskey
            writer.printAttribute("accesskey", getAccesskey());

            // shape
            writer.printAttribute("shape", getShape());

            // coords
            writer.printAttribute("coords", getCoords());

            // tabindex
            writer.printAttribute("tabindex", getTabindex());

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
     * サービスを設定します。
     *
     * @param service サービス
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * ターゲットとなるフォーム名を取得します。
     *
     * @return ターゲットとなるフォーム名
     */
    public String getForm() {
        return this.form;
    }

    /**
     * ターゲットとなるフォーム名を設定します。
     *
     * @param form ターゲットとなるフォーム名
     */
    public void setForm(String form) {
        this.form = form;
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
