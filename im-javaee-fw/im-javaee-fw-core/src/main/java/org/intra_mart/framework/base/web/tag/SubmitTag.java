/*
 * SubmitTag.java
 *
 * Created on 2002/08/12, 13:16
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
 * Submitタグです。
 * intra-martフレームワークのFormタグ内でのみ使用できるタグです。
 *
 * @author INTRAMART
 * @since 3.2
 */
public class SubmitTag extends BodyTagSupport {

    private static class SubmitEventsAttributes extends EventsAttributes {

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
    private SubmitEventsAttributes events;

    /**
     * アプリケーション
     */
    private String application;

    /**
     * サービス
     */
    private String service;

    /**
     * name
     */
    private String name;

    /**
     * value
     */
    private String value;

    /**
     * onFocus
     */
    private String onFocus;

    /**
     * onBlur
     */
    private String onBlur;

    /**
     * サービスマネージャ
     *
     * @since 4.3
     */
    private ServiceManager serviceManager;

    /**
     * SubmitTagを新規に生成します。
     */
    public SubmitTag() {
        try {
            this.serviceManager = ServiceManager.getServiceManager();
        } catch (Exception e) {
            throw new FrameworkRuntimeException(e);
        }
        this.coreAttrs = new CoreAttributes();
        this.i18n = new I18nAttributes();
        this.events = new SubmitEventsAttributes();
    }

    /**
     * 開始タグを検知したときにJSPエンジンから呼ばれます。
     * Submitタグを解釈します。
     *
     * @throws JspException JSP出力時に例外が発生
     * @return EVAL_BODY_BUFFERED
     */
    public int doStartTag() throws JspException {
        FormTag form = null;
        String formName = null;

        // 外部のFormタグの取得
        try {
            form = (FormTag)findAncestorWithClass(this, FormTag.class);
            if (form == null) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.web.tag.i18n")
                            .getString("SubmitTag.SubmitMustInForm");
                } catch (MissingResourceException e) {
                }
                throw new FrameworkTagException(message);
            }
        } catch (Exception e) {
            throw new FrameworkTagException(e.getMessage(), e);
        }

        // Formのname属性を取得
        formName = form.getName();
        if (formName == null || formName.trim().length() == 0) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.web.tag.i18n")
                        .getString("SubmitTag.FormNeedsNameAtttibute");
            } catch (MissingResourceException e) {
            }
            throw new FrameworkTagException(message);
        }

        try {
            String contextPath = null;
            String url = null;
            Vector serviceParams = null;
            String tempPath = null;

            contextPath =
                ((HttpServletRequest)pageContext.getRequest())
                    .getContextPath();

            // URL生成。アプリケーションIDが設定されていない場合はformのアプリケーションIDを取得
            if (getApplication() == null  || getApplication().trim().length() == 0) {
                tempPath = contextPath
				+ "/"
				+ form.getApplication()
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

            // Submitタグ開始
            pageContext.getOut().print("<INPUT type=\"button\"");

            // name
            writer.printAttribute("name", getName());

            // value
            writer.printAttribute("value", getValue());

            // onfocus
            writer.printAttribute("onfocus", getOnfocus());

            // onblur
            writer.printAttribute("onblur", getOnblur());

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
            pageContext.getOut().print("/>");
        } catch (Exception e) {
            throw new FrameworkTagException(e.getMessage(), e);
        }

        return EVAL_BODY_BUFFERED;
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
     * 属性<CODE>name</CODE>を取得します。
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * 属性<CODE>name</CODE>を設定します。
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 属性<CODE>value</CODE>を取得します。
     *
     * @return value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * 属性<CODE>value</CODE>を設定します。
     *
     * @param value value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 属性<CODE>onFocus</CODE>を取得します。
     *
     * @return onFocus
     */
    public String getOnfocus() {
        return this.onFocus;
    }

    /**
     * 属性<CODE>onFocus</CODE>を設定します。
     *
     * @param onFocus onFocus
     */
    public void setOnfocus(String onFocus) {
        this.onFocus = onFocus;
    }

    /**
     * 属性<CODE>onBlur</CODE>を取得します。
     *
     * @return onBlur
     */
    public String getOnblur() {
        return this.onBlur;
    }

    /**
     * 属性<CODE>onBlur</CODE>を設定します。
     *
     * @param onBlur onBlur
     */
    public void setOnblur(String onBlur) {
        this.onBlur = onBlur;
    }

    /**
     * 属性<CODE>class</CODE>の値を取得します。
     *
     * @return class
     */
    public String getSubmitclass() {
        return this.coreAttrs.getTagClass();
    }

    /**
     * 属性<CODE>class</CODE>の値を設定します。
     *
     * @param submitClass class
     */
    public void setSubmitclass(String submitClass) {
        this.coreAttrs.setTagClass(submitClass);
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
