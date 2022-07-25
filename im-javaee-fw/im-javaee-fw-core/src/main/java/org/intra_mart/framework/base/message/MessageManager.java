/*
 * MessageManager.java
 *
 * Created on 2003/08/05, 18:00
 */

package org.intra_mart.framework.base.message;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.log.LogConstant;
import org.intra_mart.framework.system.log.LogManager;
import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyManager;
import org.intra_mart.framework.system.property.PropertyManagerException;

/**
 * メッセージマネージャです。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class MessageManager {

    /**
     * メッセージフレームワークのログのプレフィックス
     */
    static String LOG_HEAD = "[J2EE][Message]";

    /**
     * メッセージプロパティハンドラのキー
     */
    public static final String MESSAGE_PROPERTY_HANDLER_KEY = "i18n_message";

    /**
     * メッセージマネージャ取得フラグ
     */
    private static Boolean semaphore = new Boolean(true);

    /**
     * メッセージマネージャ
     */
    private static MessageManager manager;

    /**
     * メッセージプロパティハンドラ
     */
    private MessagePropertyHandler messagePropertyHandler;

    /**
     * MessageManagerを生成するコンストラクタです。
     * このコンストラクタは明示的に呼び出すことはできません。
     *
     * @throws MessageManagerException メッセージマネージャの生成に失敗した
     */
    private MessageManager() throws MessageManagerException {
        PropertyManager propertyManager;

        // プロパティマネージャの取得
        try {
            propertyManager = PropertyManager.getPropertyManager();
        } catch (PropertyManagerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.message.i18n")
                        .getString("MessageManager.FailedToGetPropertyManager");
            } catch (MissingResourceException ex) {
            }
            throw new MessageManagerException(message, e);
        }

        // メッセージプロパティハンドラの取得
        try {
            this.messagePropertyHandler =
                (MessagePropertyHandler)propertyManager.getPropertyHandler(
                    MESSAGE_PROPERTY_HANDLER_KEY);
        } catch (PropertyHandlerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.message.i18n")
                        .getString("MessageManager.FailedToGetMessagePropertyHandler");
            } catch (MissingResourceException ex) {
            }
            throw new MessageManagerException(
                message + " : " + MESSAGE_PROPERTY_HANDLER_KEY,
                e);
        }
    }

    /**
     * メッセージマネージャを取得します。
     *
     * @return メッセージマネージャ
     * @throws MessageManagerException メッセージマネージャの生成に失敗した
     */
    public static MessageManager getMessageManager()
        throws MessageManagerException {

        if (manager == null) {
            synchronized (semaphore) {
                if (manager == null) {
                    manager = new MessageManager();
                }
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.message.i18n")
                            .getString("MessageManager.SuccessedToCreateManager");
                } catch (MissingResourceException ex) {
                }
                LogManager.getLogManager().getLogAgent().sendMessage(
                    MessageManager.class.getName(),
                    LogConstant.LEVEL_INFO,
                    LOG_HEAD + message);
            }
        }

        return manager;
    }

    /**
     * ロケールを指定せず、キーに該当するメッセージを取得します。
     * このメソッドは{@link #getMessage(java.lang.String, java.lang.String, java.lang.Object[], java.util.Locale) getMessage(application, key, args, java.util.Locale.getDefault())}を呼んだときと同じ動作をします。
     *
     * @param application アプリケーションID
     * @param key キー
     * @param args メッセージに設定するパラメータ
     * @return メッセージ（存在しない場合<code>???<I>key</I>???</code>）
     */
    public String getMessage(String application, String key, Object[] args) {
        return getMessage(application, key, args, Locale.getDefault());
    }

    /**
     * ロケールを指定し、キーに該当するメッセージを取得します。
     * 指定されたアプリケーションID、キー、ロケールに一致するメッセージをプロパティから取得し、
     * パラメータをメッセージ内部に埋め込んだ結果を返します。<BR><BR>
     * このメソッドでは内部で以下のような動作をします。
     * <OL>
     * <LI>{@link #getMessagePropertyHandler()}を呼び、MessagePropertyHandlerを取得する。
     * <LI>{@link MessagePropertyHandler#getMessage(java.lang.String, java.lang.String, java.util.Locale) MessagePropertyHandler.getMessage(application, key, locale)}を呼び、メッセージを取得する。
     * <LI><code>new java.text.MessageFormat(<I>上で取得したメッセージ</I>).format(args)</code>を呼び、パラメータを埋め込んだメッセージを返す。
     * </OL>
     *
     * @param application アプリケーションID
     * @param key キー
     * @param args メッセージに設定するパラメータ
     * @param locale ロケール
     * @return メッセージ（存在しない場合<code>???<I>key</I>???</code>）
     */
    public String getMessage(
        String application,
        String key,
        Object[] args,
        Locale locale) {

        String errorMessage = "???" + key + "???";
        String original = null;

        // プロパティハンドラの取得
        MessagePropertyHandler handler = getMessagePropertyHandler();
        if (handler == null) {
            String warning = null;
            try {
                warning =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.message.i18n")
                        .getString("MessageManager.FailedToGetMessagePropertyHandler");
            } catch (MissingResourceException ex) {
            }
            LogManager.getLogManager().getLogAgent().sendMessage(
                MessageManager.class.getName(),
                LogConstant.LEVEL_WARNNING,
                LOG_HEAD
                    + warning
                    + " : application = "
                    + application
                    + ", key = "
                    + key
                    + ", locale = "
                    + locale
                    + " : getMessage(String, String, Object[], Locale)");
            return errorMessage;
        }

        // メッセージの取得
        try {
            original = handler.getMessage(application, key, locale);
        } catch (MessagePropertyException e) {
            String warning = null;
            try {
                warning =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.message.i18n")
                        .getString("MessageManager.FailedToGetMessage");
            } catch (MissingResourceException ex) {
            }
            LogManager.getLogManager().getLogAgent().sendMessage(
                MessageManager.class.getName(),
                LogConstant.LEVEL_WARNNING,
                LOG_HEAD
                    + warning
                    + " : application = "
                    + application
                    + ", key = "
                    + key
                    + ", locale = "
                    + locale
                    + " : getMessage(String, String, Object[], Locale)");
            return errorMessage;
        }

        // 取得したメッセージをもとにフォーマットを生成
        MessageFormat format = new MessageFormat(original);

        // フォーマットにしたがって引数を添えて終了
        return format.format(args);
    }

    /**
     * メッセージプロパティハンドラを取得します。
     *
     * @return メッセージプロパティハンドラ
     */
    public MessagePropertyHandler getMessagePropertyHandler() {
        return this.messagePropertyHandler;
    }
}
