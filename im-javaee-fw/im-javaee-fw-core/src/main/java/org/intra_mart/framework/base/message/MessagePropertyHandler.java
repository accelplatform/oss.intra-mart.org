/*
 * MessagePropertyHandler.java
 *
 * Created on 2003/08/06, 18:00
 */

package org.intra_mart.framework.base.message;

import java.util.Locale;

import org.intra_mart.framework.system.property.PropertyHandler;

/**
 * メッセージの設定情報に接続するクラスです。
 *
 * @author INTRAMART
 * @since 4.2
 */
public interface MessagePropertyHandler extends PropertyHandler {

    /**
     * プロパティの動的読み込みが可能かどうか調べます。
     *
     * @return true：プロパティの動的読み込みが可能、false：プロパティの動的読み込み不可
     * @throws MessagePropertyException チェック時に例外が発生
     */
    public boolean isDynamic() throws MessagePropertyException;

    /**
     * メッセージを取得します。
     * <code>application</code>で指定されるアプリケーション固有のプロパティから
     * <code>key</code>に該当するメッセージを取得します。
     * システムが稼動しているロケールをヒントにメッセージを取得します。
     *
     * @param application アプリケーションID
     * @param key メッセージのキー
     * @return メッセージ
     * @throws MessagePropertyException メッセージ取得時に例外が発生
     */
    public String getMessage(String application, String key)
        throws MessagePropertyException;

    /**
     * メッセージを取得します。
     * <code>application</code>で指定されるアプリケーション固有のプロパティから
     * <code>key</code>に該当するメッセージを取得します。
     * <code>locale</code>で指定されるロケールをヒントにメッセージを取得します。
     *
     * @param application アプリケーションID
     * @param key メッセージのキー
     * @param locale ロケール
     * @return メッセージ
     * @throws MessagePropertyException メッセージ取得時に例外が発生
     */
    public String getMessage(String application, String key, Locale locale)
        throws MessagePropertyException;
}
