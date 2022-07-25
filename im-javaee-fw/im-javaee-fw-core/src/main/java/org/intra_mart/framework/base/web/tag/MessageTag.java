/*
 * MessageTag.java
 *
 * Created on 2003/08/04, 12:00
 */

package org.intra_mart.framework.base.web.tag;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.intra_mart.framework.base.message.MessageManager;
import org.intra_mart.framework.base.message.MessageManagerException;
import org.intra_mart.framework.base.service.ServiceManager;
import org.intra_mart.framework.base.service.ServiceManagerException;
import org.intra_mart.framework.system.log.LogManager;
import org.intra_mart.framework.system.log.LogConstant;

/**
 * Messageタグです。
 * im-J2EE Frameworkを使用する場合のMessageフレームワークの一部となります。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class MessageTag extends BodyTagSupport {

    /**
     * MessageManager
     */
    private MessageManager messageManager;

    /**
     * ServiceManager
     */
    private ServiceManager serviceManager;

    /**
     * パラメータの集合
     */
    private Vector params;

    /**
     * アプリケーション
     */
    private String application;

    /**
     * キー
     */
    private String key;

    /**
     * ロケール
     */
    private String locale;

    /**
     * MessageTagを新規に生成します。
     */
    public MessageTag() {
        super();
        try {
            this.messageManager = MessageManager.getMessageManager();
        } catch (MessageManagerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.web.tag.i18n")
                        .getString("MessageTag.FailedToGetMessageManager");
            } catch (MissingResourceException ex) {
            }
            LogManager.getLogManager().getLogAgent().sendMessage(
                MessageTag.class.getName(),
                LogConstant.LEVEL_ERROR,
                message,
                e);
        }
        try {
            this.serviceManager = ServiceManager.getServiceManager();
        } catch (ServiceManagerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.web.tag.i18n")
                        .getString("MessageTag.FailedToGetServiceManager");
            } catch (MissingResourceException ex) {
            }
            LogManager.getLogManager().getLogAgent().sendMessage(
                MessageTag.class.getName(),
                LogConstant.LEVEL_ERROR,
                message,
                e);
        }
        this.params = null;
    }

    /**
     * 開始タグを検知したときにJSPエンジンから呼ばれます。
     * Messageタグを解釈します。
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
     * Messageタグを終了します。
     *
     * @return EVAL_PAGE
     * @throws JspException タグの終了時に例外が発生
     */
    public int doEndTag() throws JspException {
        String contextPath = null;
        String serviceServletPath = null;
        String url = null;
        Object[] messageParams = null;
        FrameworkTagParam param = null;
        String destLocaleString = null;
        Locale destLocale = null;

        try {
            // パラメータ
            int paramSize = this.params.size();
            messageParams = new Object[paramSize];

            for (int i = 0; i < paramSize; i++) {
                messageParams[i] = this.params.elementAt(i);
            }

            destLocaleString = getLocale();
            if (destLocaleString == null
                || destLocaleString.trim().equals("")) {
                destLocale =
                    this.serviceManager.getLocale(
                        (HttpServletRequest)pageContext.getRequest(),
                        (HttpServletResponse)pageContext.getResponse());
            } else {
                destLocale = getRealLocale(destLocaleString);
            }

            // Messageタグ開始
            pageContext.getOut().print(
                this.messageManager.getMessage(
                    getApplication(),
                    getKey(),
                    messageParams,
                    destLocale));
        } catch (Exception e) {
            throw new FrameworkTagException(e.getMessage(), e);
        }

        return EVAL_PAGE;
    }

    /**
     * ロケール文字列から実際のロケールを取得します。
     * ロケール文字列は以下の書式に従います。<BR>
     * <I>言語</I>_<I>国</I>[_<I>バリアント</I>]<BR>
     * ロケール文字列のアンダーバー(_)はハイフン(-)であってもかまいません。
     *
     * @param localeString ロケール文字列
     * @return ロケール
     */
    private Locale getRealLocale(String localeString) {
        StringTokenizer tokenizer = new StringTokenizer(localeString, "-_");
        if(tokenizer.countTokens() == 1) {
            String language = tokenizer.nextToken();
            return new Locale(language);
        }else if (tokenizer.countTokens() == 2) {
            String language = tokenizer.nextToken();
            String country = tokenizer.nextToken();
            return new Locale(language, country);
        } else if (tokenizer.countTokens() == 3) {
            String language = tokenizer.nextToken();
            String country = tokenizer.nextToken();
            String variant = tokenizer.nextToken();
            return new Locale(language, country, variant);
        } else {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.web.tag.i18n")
                        .getString("MessageTag.LocaleStringIncorrect");
            } catch (MissingResourceException e) {
            }
            throw new IllegalArgumentException(
                message + " : \"" + localeString + "\"");
        }
    }

    /**
     * パラメータを追加します。
     *
     * @param value パラメータの値
     */
    public void addParameter(Object value) {
        this.params.add(value);
    }

    /**
     * applicationを取得します。
     *
     * @return application
     */
    public String getApplication() {
        return this.application;
    }

    /**
     * applicationを設定します。
     *
     * @param application application
     */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * keyを取得します。
     *
     * @return key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * keyを設定します。
     *
     * @param key key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * localeを取得します。
     *
     * @return locale
     */
    public String getLocale() {
        return this.locale;
    }

    /**
     * localeを設定します。
     *
     * @param locale locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }
}
