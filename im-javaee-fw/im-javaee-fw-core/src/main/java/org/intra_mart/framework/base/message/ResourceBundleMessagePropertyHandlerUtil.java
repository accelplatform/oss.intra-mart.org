/*
 * ResourceBundleMessagePropertyHandlerUtil.java
 *
 * Created on 2003/08/06, 18:00
 */

package org.intra_mart.framework.base.message;

import java.util.Locale;
import java.util.ResourceBundle;

import java.util.MissingResourceException;

/**
 * 指定されたリソースバンドルからメッセージプロパティを取得するユーティリティです。
 *
 * @author INTRAMART
 * @since 4.2
 */
class ResourceBundleMessagePropertyHandlerUtil {

    /**
     * キーに該当するメッセージを取得します。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param key キー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws MessagePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getMessage(ResourceBundle applicationBundle, String application, String key, Locale locale) throws MessagePropertyException {
        String message;

        try {
			message = applicationBundle.getString(key);
        } catch (MissingResourceException e) {
            String errorMessage = null;
            try {
				errorMessage = ResourceBundle.getBundle("org.intra_mart.framework.base.service.i18n").getString("ResourceBundleMessagePropertyHandlerUtil.FailedToGetMessage");
            } catch (MissingResourceException ex) {
            }
            throw new MessagePropertyException(errorMessage + " : application = " + application + ", key = " + key + ", Locale = " + locale, e);
        }

        return message;
    }
}
