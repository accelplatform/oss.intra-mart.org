/*
 * ServiceResourceMessage.java
 * 
 * Created on 2004/09/12 ,13:56:01
 */
package org.intra_mart.framework.base.service;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * リソースメッセージを取得するクラスです。
 * 
 * @author INTRAMART
 * @since 5.0
 */
class ServiceResourceMessage {

    /**
     * リソースメッセージを取得します。
     * 
     * @param key
     */
    static String getResourceString(String key) {
        String message = null;
        try {
            message = ResourceBundle.getBundle(
                    "org.intra_mart.framework.base.service.i18n").getString(
                    key);
        } catch (MissingResourceException ex) {
        }
        return message;
    }
}