/*
 * 作成日: 2003/08/06
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package org.intra_mart.framework.base.message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;

/**
 * メッセージの設定情報に接続するデフォルトのプロパティハンドラです。
 * <BR>プロパティファイルはアプリケーション毎に分割されます。この場合のファイル名は「<I>プレフィックス</I>_<I>アプリケーションID</I>[_<I>ロケール</I>].properties」です。
 * <BR>_<I>ロケール</I>を追加することによってロケールごとにメッセージを変更することも可能です。ロケールによるプロパティファイルの優先順位はJava<sup><font size=-2>TM</font></sup>のjava.util.ResourceBundleと同様のルールに従って決定されます。
 * <BR>プロパティファイルのプレフィックスは{@link org.intra_mart.framework.system.property.PropertyManager#getPropertyHandlerParams(String)}でキーに{@link MessageManager#MESSAGE_PROPERTY_HANDLER_KEY}を指定したときに取得されるパラメータのうち{@link #DEFAULT_BUNDLE_NAME}で取得されるものとなります。
 * 指定しなかった場合のプロパティファイルのプレフィックスは{@link #DEFAULT_BUNDLE_NAME_PARAM}で示されるものです。
 * <BR><BR>プロパティの内容は「<I>キー</I>=<I>メッセージ</I>」の形式で指定します。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class DefaultMessagePropertyHandler implements MessagePropertyHandler {

    /**
     * デフォルトのリソースバンドル名のプレフィックス
     */
    public static final String DEFAULT_BUNDLE_NAME = "MessageConfig";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * メッセージ情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

    /**
     * アプリケーションごとの地域対応されたメッセージ情報が設定されているリソースバンドル
     */
    private Map localizedBundles;

    /**
     * DefaultMessagePropertyHandlerを新規に生成します。
     */
    public DefaultMessagePropertyHandler() {
        setBundlePrefix(null);
        setLocalizedApplicationBundles(new HashMap());
    }

    /**
     * プロパティの動的読み込みが可能かどうか調べます。
     * このクラスではこのメソッドは常にfalseを返します。
     *
     * @return 常にfalse
     * @throws MessagePropertyException チェック時に例外が発生
     */
    public boolean isDynamic() throws MessagePropertyException {
        return false;
    }

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
        throws MessagePropertyException {

        return getMessage(application, key, Locale.getDefault());
    }

    /**
     * メッセージを取得します。
     * <code>application</code>で指定されるアプリケーション固有のプロパティから
     * <code>key</code>に該当するメッセージを取得します。
     * <code>locale</code>で指定されたロケールをヒントにメッセージを取得します。
     *
     * @param application アプリケーションID
     * @param key メッセージのキー
     * @param locale ロケール
     * @return メッセージ
     * @throws MessagePropertyException メッセージ取得時に例外が発生
     */
    public String getMessage(String application, String key, Locale locale)
        throws MessagePropertyException {

        return ResourceBundleMessagePropertyHandlerUtil.getMessage(
            getResourceBundle(application, locale),
            application,
            key,
            locale);
    }

    /**
     * プロパティハンドラを初期化します。
     *
     * @param params 初期パラメータ
     * @throws PropertyHandlerException プロパティハンドラの初期化時に例外が発生
     */
    public void init(PropertyParam[] params) throws PropertyHandlerException {
        String bundleName = null;

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].getName().equals(DEFAULT_BUNDLE_NAME_PARAM)) {
                    bundleName = params[i].getValue();
                }
            }
        }
        if (bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        }

        setBundlePrefix(bundleName);
    }

    /**
     * リソースバンドルのプレフィックスを設定します。
     *
     * @param bundlePrefix リソースバンドルのプレフィックス
     */
    private void setBundlePrefix(String bundlePrefix) {
        this.bundlePrefix = bundlePrefix;
    }

    /**
     * リソースバンドルのプレフィックスを取得します。
     *
     * @return リソースバンドルのプレフィックス
     */
    private String getBundlePrefix() {
        return this.bundlePrefix;
    }

    /**
     * アプリケーションごとの地域対応されたリソースバンドルの集合を設定します。
     *
     * @param applicationBundles アプリケーションごとの地域対応されたリソースバンドルの集合
     */
    private void setLocalizedApplicationBundles(Map applicationBundles) {
        this.localizedBundles = applicationBundles;
    }

    /**
     * アプリケーションごとの地域対応されたリソースバンドルの集合を取得します。
     *
     * @return アプリケーションごとの地域対応されたリソースバンドルの集合
     */
    private Map getLocalizedApplicationBundles() {
        return this.localizedBundles;
    }

    /**
     * アプリケーションIDとロケールで指定されたリソースバンドルを取得します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws MessagePropertyException リソースバンドルの取得時に例外が発生
     */
    private ResourceBundle getResourceBundle(String application, Locale locale)
        throws MessagePropertyException {

        ResourceBundle result = null;
        Map keyLocale = null;
        Locale realLocale = locale;

        if (realLocale == null) {
            realLocale = new Locale("", "");
        }
        synchronized (this.localizedBundles) {
            keyLocale = (Map)getLocalizedApplicationBundles().get(realLocale);
            if (keyLocale == null) {
                keyLocale = new HashMap();
                getLocalizedApplicationBundles().put(realLocale, keyLocale);
            }
        }
        synchronized (keyLocale) {
            result = (ResourceBundle)keyLocale.get(application);
            if (result == null) {
                result = createResourceBundle(application, realLocale);
                keyLocale.put(application, result);
            }
        }

        return result;
    }

    /**
     * アプリケーションIDとロケールで指定されたリソースバンドルを生成します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws MessagePropertyException リソースバンドルの取得時に例外が発生
     */
    private ResourceBundle createResourceBundle(
        String application,
        Locale locale)
        throws MessagePropertyException {

    	// パッケージに対応
    	String applicationID = application;
    	String packageId = "";
    	if (application.indexOf('.') != -1) {
    		applicationID = application.substring(application.lastIndexOf('.') + 1);
    		packageId = application.substring(0, application.lastIndexOf('.')).replace('.', '/') + '/';
    	}
    	
        try {
            if (locale == null) {
                return ResourceBundle.getBundle(
                		packageId + getBundlePrefix() + "_" + applicationID);
            } else {
                return ResourceBundle.getBundle(
                		packageId + getBundlePrefix() + "_" + applicationID, locale);
            }
        } catch (MissingResourceException e) {
            throw new MessagePropertyException(e.getMessage(), e);
        }
    }
}
