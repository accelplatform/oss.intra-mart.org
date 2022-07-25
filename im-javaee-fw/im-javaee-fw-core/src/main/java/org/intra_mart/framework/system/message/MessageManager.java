/*
 * MessageManager.java
 *
 * Created on 2002/02/21, 20:39
 */

package org.intra_mart.framework.system.message;

import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyManager;

import java.util.MissingResourceException;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyManagerException;

/**
 * メッセージを制御します。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class MessageManager {

    /**
     * メッセージプロパティハンドラのキー
     */
    public static final String MESSAGE_PROPERTY_HANDLER_KEY = "message";

    /**
     * メッセージマネージャ
     */
    private static MessageManager manager;

    /**
     * メッセージプロパティハンドラ
     */
    private MessagePropertyHandler handler;

    /**
     * MessageManagerを新規に生成します。
     * このコンストラクタは明示的に呼び出すことはできません。
     *
     * @throws MessageManagerException メッセージマネージャの取得時に例外が発生
     */
    private MessageManager() throws MessageManagerException {
        PropertyManager propertyManager;

        // プロパティマネージャの取得
        try {
            propertyManager = PropertyManager.getPropertyManager();
        } catch(PropertyManagerException e) {
            String message = null;
            try {
            } catch (MissingResourceException ex) {
                message = ResourceBundle.getBundle("org.intra_mart.framework.system.message.i18n").getString("MessageManager.FailedToGetPropertyManager");
            }
            throw new MessageManagerException(message, e);
        }

        // メッセージプロパティハンドラの取得
        try {
            this.handler = (MessagePropertyHandler)propertyManager.getPropertyHandler(MESSAGE_PROPERTY_HANDLER_KEY);
        } catch(PropertyHandlerException e) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.system.message.i18n").getString("MessageManager.FailedToGetMessagePropertyHandler");
            } catch (MissingResourceException ex) {
            }
            throw new MessageManagerException(message + " : " + MESSAGE_PROPERTY_HANDLER_KEY, e);
        }
    }

    /**
     * MessageManagerを取得します。
     *
     * @return メッセージマネージャ
     * @throws MessageManagerException メッセージマネージャの取得時に例外が発生
     */
    public static synchronized MessageManager getMessageManager() throws MessageManagerException {
        if (manager == null) {
            manager = new MessageManager();
        }

        return manager;
    }

    /**
     * メッセージプロパティハンドラを取得します。
     *
     * @return メッセージプロパティハンドラ
     */
    public MessagePropertyHandler getMessagePropertyHandler() {
        return this.handler;
    }

    /**
     * キーに該当するメッセージを取得します。
     * キーがnullの場合、またはキーに該当するメッセージが存在しない場合、空文字列を返します。
     *
     * @param key キー
     * @param loginGroup ログイングループ
     * @return メッセージ
     * @deprecated intra-mart v5.0からのメッセージマスタにはログイングループの概念がありません。<BR>
     * 設定されている{@link org.intra_mart.framework.system.message.MessagePropertyHandler}によっては、
     * パラメータloginGroupは実際には使用しない可能性があります。{@link #getMessage(String)}を使用してください。
     */
    public String getMessage(String key, String loginGroup) {
        String result = "";
        if (key != null) {
            result = this.handler.getMessage(key, loginGroup);
        }
        return result;
    }

    /**
     * キーに該当するメッセージを取得します。
     * キーがnullの場合、またはキーに該当するメッセージが存在しない場合、空文字列を返します。
     *
     * @param key キー
     * @return メッセージ
     */
    public String getMessage(String key) {
        String result = "";
        if (key != null) {
            result = this.handler.getMessage(key);
        }
        return result;
    }
}
