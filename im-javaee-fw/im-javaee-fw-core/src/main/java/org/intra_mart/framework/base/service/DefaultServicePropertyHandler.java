/*
 * DefaultServicePropertyHandler.java
 *
 * Created on 2002/01/07, 14:15
 */

package org.intra_mart.framework.base.service;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;

/**
 * サービスの設定情報に接続するデフォルトのプロパティハンドラです。 <BR>
 * プロパティファイルはアプリケーション毎に分割されます。この場合のファイル名は「 <I>プレフィックス </I>_ <I>アプリケーションID
 * </I>.properties」です。また、アプリケーションに依存しない「 <I>プレフィックス </I>.properties」があります。 <BR>
 * プロパティファイルのプレフィックスは
 * {@link org.intra_mart.framework.system.property.PropertyManager#getPropertyHandlerParams(String)}
 * でキーに {@link ServiceManager#SERVICE_PROPERTY_HANDLER_KEY}
 * を指定したときに取得されるパラメータのうち {@link #DEFAULT_BUNDLE_NAME}で取得されるものとなります。
 * 指定しなかった場合のプロパティファイルのプレフィックスは {@link #DEFAULT_BUNDLE_NAME_PARAM}で示されるものです。
 * <BR>
 * プロパティの検索順は、アプリケーションごとのプロパティファイル「 <I>プレフィックス </I>_ <I>アプリケーションID
 * </I>.properties」が優先されます。該当するプロパティが見つからない場合、引き続いて「 <I>プレフィックス
 * </I>.properties」を検索します。 <BR>
 * プロパティの設定内容は以下のとおりです。 プロパティの設定内容は以下のとおりです。 <TABLE border="1">
 * <TR>
 * <TH nowrap>キー</TH>
 * <TH nowrap>内容</TH>
 * <TH nowrap>共通</TH>
 * <TH nowrap>アプリケーション毎</TH>
 * </TR>
 * <TR>
 * <TD nowrap>client.encoding</TD>
 * <TD>クライアントのエンコーディング <BR>
 * 設定しない場合、リクエストに対する文字コードの変換は行われません。</TD>
 * <TD align="center">○</TD>
 * <TD align="center">×</TD>
 * </TR>
 * <TR>
 * <TD nowrap>encoding.attribute</TD>
 * <TD>ログインユーザのエンコーディングを保存する属性名 <BR>
 * このプロパティは共通のプロパティファイルのみに設定します。</TD>
 * <TD align="center">○</TD>
 * <TD align="center">×</TD>
 * </TR>
 * <TR>
 * <TD nowrap>client.locale</TD>
 * <TD>クライアントのロケール <BR>
 * クライアントからのリクエストのロケールのデフォルトです。</TD>
 * <TD align="center">○</TD>
 * <TD align="center">×</TD>
 * </TR>
 * <TR>
 * <TD nowrap>locale.attribute</TD>
 * <TD>ログインユーザのロケールを保存する属性名 <BR>
 * このプロパティは共通のプロパティファイルのみに設定します。</TD>
 * <TD align="center">○</TD>
 * <TD align="center">×</TD>
 * </TR>
 * <TR>
 * <TD nowrap>context.path</TD>
 * <TD><B>
 * <I>非推奨です。このプロパティは設定しないでください。コンテキストパスの取得はjavax.servlet.http.HttpServletRequestのgetContextPathメソッドを使用してください。
 * </I> </B> <BR>
 * コンテキストパス <BR>/ <I>コンテキストパス </I>で指定します。 <BR>
 * パスの指定は必ず&quot;/&quot;で開始してください。（例：/imart） <BR>
 * このプロパティは共通のプロパティファイルのみに設定します。 <BR>
 * このプロパティが設定されている場合、im-J2EE Frameworkのタグライブラリを使用したときに遷移するWebアプリケーションが変更されます。
 * </TD>
 * <TD align="center">○</TD>
 * <TD align="center">×</TD>
 * </TR>
 * <TR>
 * <TD nowrap>servlet.path</TD>
 * <TD>{@link ServiceServlet}にマッピングされているパス（必須） <BR>/
 * <I>web.xmlで指定したサーブレットのマッピング </I>で指定します。 <BR>
 * パスの指定は必ず&quot;/&quot;で開始してください。（例：/ServiceServlet） <BR>
 * このプロパティは共通のプロパティファイルのみに設定します。</TD>
 * <TD align="center">○</TD>
 * <TD align="center">×</TD>
 * </TR>
 * <TR>
 * <TD nowrap>application.param</TD>
 * <TD>アプリケーションIDを引き渡すときに使われる、リクエストのパラメータ名 <BR>
 * このプロパティは共通のプロパティファイルのみに設定します。</TD>
 * <TD align="center">○</TD>
 * <TD align="center">×</TD>
 * </TR>
 * <TR>
 * <TD nowrap>service.param</TD>
 * <TD>サービスIDを引き渡すときに使われる、リクエストのパラメータ名 <BR>
 * このプロパティは共通のプロパティファイルのみに設定します。</TD>
 * <TD align="center">○</TD>
 * <TD align="center">×</TD>
 * </TR>
 * <TR>
 * <TD nowrap>exception.attribute</TD>
 * <TD>例外を例外ページに渡すときの属性名 <BR>
 * このプロパティは共通のプロパティファイルのみに設定します。</TD>
 * <TD align="center">○</TD>
 * <TD align="center">×</TD>
 * </TR>
 * <TR>
 * <TD nowrap>controller.class. <I>サービスID </I></TD>
 * <TD><I>アプリケーションID </I>と <I>サービスID </I>に対応する {@link ServiceController}のクラス名
 * </TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>transition.class. <I>サービスID </I></TD>
 * <TD><I>アプリケーションID </I>と <I>サービスID </I>に対応する {@link Transition}のクラス名</TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>nextpage.path. <I>サービスID </I>. <I>キー </I></TD>
 * <TD><I>アプリケーションID </I>、 <I>サービスID </I>と <I>キー </I>に対応する次の遷移先のパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始） <BR>
 * 遷移先を <I>アプリケーションID </I>と <I>サービスID </I>よりも細かく指定したい場合などに使用します。</TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>nextpage.path. <I>サービスID </I></TD>
 * <TD><I>アプリケーションID </I>と <I>サービスID </I>に対応する次の遷移先のパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>input.error.page.path. <I>サービスID </I>. <I>キー </I></TD>
 * <TD><I>サービスID </I>と <I>キー </I>に対応する入力エラーページのパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>input.error.page.path. <I>サービスID </I></TD>
 * <TD><I>サービスID </I>に対応するデフォルトの入力エラーページのパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>input.error.page.path</TD>
 * <TD>デフォルトの入力エラーページのパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">○</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>service.error.page.path. <I>サービスID </I>. <I>キー </I></TD>
 * <TD><I>サービスID </I>と <I>キー </I>に対応するサービス処理エラーページのパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>service.error.page.path. <I>サービスID </I></TD>
 * <TD><I>サービスID </I>に対応するデフォルトのサービス処理エラーページのパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>service.error.page.path</TD>
 * <TD>デフォルトのサービス処理エラーページのパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">○</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>system.error.page.path. <I>サービスID </I>. <I>キー </I></TD>
 * <TD><I>サービスID </I>と <I>キー </I>に対応するシステム処理エラーページのパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>system.error.page.path. <I>サービスID </I></TD>
 * <TD><I>サービスID </I>に対応するデフォルトのシステム処理エラーページのパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">×</TD>
 * <TD align="center">○</TD>
 * </TR>
 * <TR>
 * <TD nowrap>system.error.page.path</TD>
 * <TD>デフォルトのシステム処理エラーページのパス <BR>
 * コンテキストルートからの相対パスで指定します。（パスの指定は&quot;/&quot;で開始）</TD>
 * <TD align="center">○</TD>
 * <TD align="center">○</TD>
 * </TR>
 * </TABLE>
 * 
 * @author INTRAMART
 * @version 1.0
 */
public class DefaultServicePropertyHandler implements ServicePropertyHandler {

    /**
     * デフォルトのリソースバンドル名のプレフィックス
     */
    public static final String DEFAULT_BUNDLE_NAME = "ServiceConfig";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * データリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

    /**
     * アプリケーションごとのサービスリソース情報が設定されているリソースバンドル
     * 
     * @uml.property name="bundles"
     * @uml.associationEnd
     * @uml.property name="bundles" multiplicity="(0 1)"
     *               qualifier="locale:java.util.Locale
     *               bundle:java.util.ResourceBundle"
     */
    private Map bundles = new HashMap();

    /**
     * 共通のサービスリソース情報が設定されているリソースバンドル
     * 
     * @uml.property name="commonBundle"
     * @uml.associationEnd
     * @uml.property name="commonBundle" multiplicity="(0 1)"
     *               qualifier="locale:java.util.Locale
     *               commonBundle:java.util.ResourceBundle"
     */
    private Map commonBundle = new HashMap();

    /**
     * DefaultServicePropertyHandlerを新規に生成します。
     */
    public DefaultServicePropertyHandler() {
        setBundlePrefix(null);
    }

    /**
     * リソースバンドルのプレフィックスを設定します。
     * 
     * @param bundlePrefix
     *            リソースバンドルのプレフィックス
     * @since 3.2
     * 
     * @uml.property name="bundlePrefix"
     */
    private void setBundlePrefix(String bundlePrefix) {
        this.bundlePrefix = bundlePrefix;
    }

    /**
     * リソースバンドルのプレフィックスを取得します。
     * 
     * @return リソースバンドルのプレフィックス
     * @since 3.2
     * 
     * @uml.property name="bundlePrefix"
     */
    private String getBundlePrefix() {
        return this.bundlePrefix;
    }

    /**
     * アプリケーションごとのリソースバンドルの集合を設定します。
     * 
     * @param applicationBundles
     *            アプリケーションごとのリソースバンドルの集合
     * @since 3.2
     */
    private void setApplicationBundles(Map applicationBundles) {
        this.bundles = applicationBundles;
    }

    /**
     * アプリケーションIDとロケールからリソースバンドルを取得します。
     * 
     * @param application
     *            アプリケーションID
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 4.2
     */
    private void setApplicationBundle(String application, ResourceBundle bundle) {

        setApplicationBundle(application, bundle, (Locale) null);
    }

    /**
     * アプリケーションIDとロケールからリソースバンドルを取得します。
     * 
     * @param application
     *            アプリケーションID
     * @param リソースバンドル
     * @param locale
     *            ロケール
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 4.2
     */
    private void setApplicationBundle(String application,
            ResourceBundle bundle, Locale locale) {

        Map appMap = (Map) this.bundles.get(application);
        if (appMap == null) {
            appMap = new HashMap();
            this.bundles.put(application, appMap);
        }
        appMap.put(locale, bundle);
    }

    /**
     * アプリケーションごとのリソースバンドルの集合を取得します。
     * 
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 3.2
     */
    private Map getApplicationBundles() {
        return this.bundles;
    }

    /**
     * アプリケーションIDとロケールからリソースバンドルを取得します。
     * 
     * @param application
     *            アプリケーションID
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 4.2
     */
    private ResourceBundle getApplicationBundle(String application) {
        return getApplicationBundle(application, (Locale) null);
    }

    /**
     * アプリケーションIDとロケールからリソースバンドルを取得します。
     * 
     * @param application
     *            アプリケーションID
     * @param locale
     *            ロケール
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 4.2
     */
    private ResourceBundle getApplicationBundle(String application,
            Locale locale) {

        Map appMap = (Map) this.bundles.get(application);
        if (appMap == null) {
            return null;
        }
        return (ResourceBundle) appMap.get(locale);
    }

    /**
     * 共通のリソースバンドルを設定します。
     * 
     * @param commonBundle
     *            共通のリソースバンドル
     * @since 3.2
     */
    private void setCommonBundle(ResourceBundle commonBundle) {
        setCommonBundle(commonBundle, (Locale) null);
    }

    /**
     * 共通のリソースバンドルを設定します。
     * 
     * @param locale
     *            ロケール
     * @param commonBundle
     *            共通のリソースバンドル
     * @since 4.2
     */
    private void setCommonBundle(ResourceBundle commonBundle, Locale locale) {
        this.commonBundle.put(locale, commonBundle);
    }

    /**
     * 共通のリソースバンドルを取得します。
     * 
     * @return 共通のリソースバンドル
     * @throws ServicePropertyException
     *             共通のリソースバンドル取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle getCommonBundle() throws ServicePropertyException {
        return getCommonBundle(null);
    }

    /**
     * 共通のリソースバンドルを取得します。
     * 
     * @return 共通のリソースバンドル
     * @throws ServicePropertyException
     *             共通のリソースバンドル取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle getCommonBundle(Locale locale)
            throws ServicePropertyException {

        ResourceBundle result;
        Locale realLocale = locale;
        if (realLocale == null) {
            realLocale = Locale.getDefault();
        }

        synchronized (this.commonBundle) {
            result = (ResourceBundle) commonBundle.get(realLocale);
            if (result == null) {
                result = createCommonBundle(realLocale);
                setCommonBundle(result, realLocale);
            }
        }

        return result;
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     * 
     * @param application
     *            アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException
     *             リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle createCommonBundle(Locale locale)
            throws ServicePropertyException {

        try {
            return ResourceBundle.getBundle(getBundlePrefix(), locale);
        } catch (MissingResourceException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        }
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを取得します。
     * 
     * @param application
     *            アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException
     *             リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle getResourceBundle(String application)
            throws ServicePropertyException {

        return getResourceBundle(application, (Locale) null);
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを取得します。
     * 
     * @param application
     *            アプリケーションID
     * @param locale
     *            ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException
     *             リソースバンドルの取得時に例外が発生
     * @since 4.2
     */
    private ResourceBundle getResourceBundle(String application, Locale locale)
            throws ServicePropertyException {

        ResourceBundle result;

        synchronized (this.bundles) {
            result = getApplicationBundle(application, locale);
            if (result == null) {
                result = createResourceBundle(application, locale);
                setApplicationBundle(application, result, locale);
            }
        }

        return result;
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     * 
     * @param application
     *            アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException
     *             リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle createResourceBundle(String application)
            throws ServicePropertyException {

        try {
            return ResourceBundle.getBundle(getPropertyPackage(application)
                    + getBundlePrefix() + "_" + getApplicationID(application));

        } catch (MissingResourceException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        }
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     * 
     * @param application
     *            アプリケーションID
     * @param locale
     *            ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException
     *             リソースバンドルの取得時に例外が発生
     * @since 4.2
     */
    private ResourceBundle createResourceBundle(String application,
            Locale locale) throws ServicePropertyException {

        ResourceBundle bundle = null;
        try {
            if (locale == null) {
                bundle = ResourceBundle
                        .getBundle(getPropertyPackage(application)
                                + getBundlePrefix() + "_"
                                + getApplicationID(application));

            } else {
                bundle = ResourceBundle.getBundle(
                        getPropertyPackage(application) + getBundlePrefix()
                                + "_" + getApplicationID(application), locale);

            }
        } catch (MissingResourceException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        }
        return bundle;
    }

    /**
     * プロパティハンドラを初期化します。
     * 
     * @param params
     *            初期パラメータ
     * @throws PropertyHandlerException
     *             プロパティハンドラの初期化時に例外が発生
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
     * プロパティの動的読み込みが可能かどうか調べます。 このクラスではこのメソッドは常にfalseを返します。
     * 
     * @return 常にfalse
     * @throws ServicePropertyException
     *             チェック時に例外が発生
     * @since 3.2
     */
    public boolean isDynamic() throws ServicePropertyException {
        return false;
    }

    /**
     * クライアントのエンコードを取得します。
     * 
     * @return クライアントのエンコーディング
     * @throws ServicePropertyException
     *             クライアントのエンコードの取得時に例外が発生
     */
    public String getClientEncoding() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getClientEncoding(getCommonBundle(new Locale("", "")));
    }

    /**
     * クライアントのエンコードを取得します。
     * 
     * @return クライアントのエンコーディング
     * @throws ServicePropertyException
     *             クライアントのエンコードの取得時に例外が発生
     * @since 4.2
     */
    public Locale getClientLocale() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getClientLocale(getCommonBundle(new Locale("", "")));
    }

    /**
     * 入力例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getInputErrorPagePath(String, String)}で取得されるページを返します。 このメソッドは
     * {@link #getInputErrorPagePath(String, String, String, Locale) getInputErrorPagePath(application, service, key, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param key
     *            遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく
     *             {@link #getInputErrorPagePath(String, String, String, Locale)}
     *             を使用してください。
     */
    public String getInputErrorPagePath(String application, String service,
            String key) throws ServicePropertyException {

        return getInputErrorPagePath(application, service, key, (Locale) null);
    }

    /**
     * 入力例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getInputErrorPagePath(String, String)}で取得されるページを返します。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param key
     *            遷移先のキー
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(String application, String service,
            String key, Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getInputErrorPagePath(
                getCommonBundle(locale),
                getResourceBundle(application, locale), application, service,
                key);
    }

    /**
     * 入力例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getInputErrorPagePath(String)}で取得されるページを返します。 このメソッドは
     * {@link #getInputErrorPagePath(String, String, Locale) getInputErrorPagePath(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく
     *             {@link #getInputErrorPagePath(String, String, Locale)}
     *             を使用してください。
     */
    public String getInputErrorPagePath(String application, String service)
            throws ServicePropertyException {

        return getInputErrorPagePath(application, service, (Locale) null);
    }

    /**
     * 入力例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getInputErrorPagePath(String)}で取得されるページを返します。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(String application, String service,
            Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getInputErrorPagePath(
                getCommonBundle(locale),
                getResourceBundle(application, locale), application, service);
    }

    /**
     * 入力例外時のページのパスを取得します。 該当するページのパスが取得できない場合、 {@link #getInputErrorPagePath()}
     * で取得されるページを返します。 このメソッドは
     * {@link #getInputErrorPagePath(String, Locale) getInputErrorPagePath(application, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getInputErrorPagePath(String, Locale)}
     *             を使用してください。
     */
    public String getInputErrorPagePath(String application)
            throws ServicePropertyException {

        return getInputErrorPagePath(application, (Locale) null);
    }

    /**
     * 入力例外時のページのパスを取得します。 該当するページのパスが取得できない場合、 {@link #getInputErrorPagePath()}
     * で取得されるページを返します。
     * 
     * @param application
     *            アプリケーションID
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(String application, Locale locale)
            throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getInputErrorPagePath(
                getCommonBundle(locale),
                getResourceBundle(application, locale), application);
    }

    /**
     * 入力例外時のページのパスを取得します。 このメソッドは
     * {@link #getInputErrorPagePath(Locale) getInputErrorPagePath((java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getInputErrorPagePath(Locale)}を使用してください。
     */
    public String getInputErrorPagePath() throws ServicePropertyException {
        return getInputErrorPagePath((Locale) null);
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(Locale locale)
            throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
                .getInputErrorPagePath(getCommonBundle(locale));
    }

    /**
     * 遷移先のページのパスを取得します。 サービスから遷移先が一意に決まる場合にこのメソッドを使用します。 このメソッドは
     * {@link #getNextPagePath(String, String, Locale) getNextPagePath(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getNextPagePath(String, String, Locale)}
     *             を使用してください。
     */
    public String getNextPagePath(String application, String service)
            throws ServicePropertyException {

        return getNextPagePath(application, service, (Locale) null);
    }

    /**
     * 遷移先のページのパスを取得します。 サービスから遷移先が一意に決まる場合にこのメソッドを使用します。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getNextPagePath(String application, String service,
            Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getNextPagePath(
                getResourceBundle(application, locale), application, service);
    }

    /**
     * 遷移先のページのパスを取得します。 サービスから遷移先が一意に決まらない場合にこのメソッドを使用します。 <CODE>key <CODE>が
     * <CODE>null <CODE>の場合、 {@link #getNextPagePath(String, String)}
     * と同じ動作になります。 このメソッドは
     * {@link #getNextPagePath(String, String, String, Locale) getNextPagePath(application, service, key, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param key
     *            遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく
     *             {@link #getNextPagePath(String, String, String, Locale)}
     *             を使用してください。
     */
    public String getNextPagePath(String application, String service, String key)
            throws ServicePropertyException {

        return getNextPagePath(application, service, key, (Locale) null);
    }

    /**
     * 遷移先のページのパスを取得します。 サービスから遷移先が一意に決まらない場合にこのメソッドを使用します。 <CODE>key <CODE>が
     * <CODE>null <CODE>の場合、 {@link #getNextPagePath(String, String)}
     * と同じ動作になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param key
     *            遷移先のキー
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getNextPagePath(String application, String service,
            String key, Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getNextPagePath(
                getResourceBundle(application, locale), application, service,
                key);
    }

    /**
     * サービスコントローラのクラス名を取得します。 該当するサービスコントローラが存在しない場合、nullを返します。 このメソッドは
     * {@link #getServiceControllerName(String, String, Locale) getServiceControllerName(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @return サービスコントローラのクラス名、存在しない場合はnull
     * @throws ServicePropertyException
     *             サービスコントローラのクラス名の取得時に例外が発生
     * @deprecated このメソッドではなく
     *             {@link #getServiceControllerName(String, String, Locale)}
     *             を使用してください。
     */
    public String getServiceControllerName(String application, String service)
            throws ServicePropertyException {

        return getServiceControllerName(application, service, null);
    }

    /**
     * サービスコントローラのクラス名を取得します。 該当するサービスコントローラが存在しない場合、nullを返します。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param locale
     *            ロケール
     * @return サービスコントローラのクラス名、存在しない場合はnull
     * @throws ServicePropertyException
     *             サービスコントローラのクラス名の取得時に例外が発生
     * @since 4.2
     */
    public String getServiceControllerName(String application, String service,
            Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
                .getServiceControllerName(
                        getResourceBundle(application, locale), application,
                        service);
    }

    /**
     * 処理例外時のページのパスを取得します。 このメソッドは
     * {@link #getServiceErrorPagePath(String, String, String, Locale) getServiceErrorPagePath(application, service, key, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param key
     *            遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく
     *             {@link #getServiceErrorPagePath(String, String, String, Locale)}
     *             を使用してください。
     */
    public String getServiceErrorPagePath(String application, String service,
            String key) throws ServicePropertyException {

        return getServiceErrorPagePath(application, service, key, (Locale) null);
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param key
     *            遷移先のキー
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(String application, String service,
            String key, Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
                .getServiceErrorPagePath(getCommonBundle(locale),
                        getResourceBundle(application, locale), application,
                        service, key);
    }

    /**
     * 処理例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getServiceErrorPagePath(String)}で取得されるページを返します。 このメソッドは
     * {@link #getServiceErrorPagePath(String, String, Locale) getServiceErrorPagePath(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく
     *             {@link #getServiceErrorPagePath(String, String, Locale)}
     *             を使用してください。
     */
    public String getServiceErrorPagePath(String application, String service)
            throws ServicePropertyException {

        return getServiceErrorPagePath(application, service, (Locale) null);
    }

    /**
     * 処理例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getServiceErrorPagePath(String)}で取得されるページを返します。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(String application, String service,
            Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
                .getServiceErrorPagePath(getCommonBundle(locale),
                        getResourceBundle(application, locale), application,
                        service);
    }

    /**
     * 処理例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getServiceErrorPagePath()}で取得されるページを返します。 このメソッドは
     * {@link #getServiceErrorPagePath(String, Locale) getServiceErrorPagePath(application, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getServiceErrorPagePath(String, Locale)}
     *             を使用してください。
     */
    public String getServiceErrorPagePath(String application)
            throws ServicePropertyException {

        return getServiceErrorPagePath(application, (Locale) null);
    }

    /**
     * 処理例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getServiceErrorPagePath()}で取得されるページを返します。
     * 
     * @param application
     *            アプリケーションID
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(String application, Locale locale)
            throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
                .getServiceErrorPagePath(getCommonBundle(locale),
                        getResourceBundle(application, locale), application);
    }

    /**
     * 処理例外時のページのパスを取得します。 このメソッドは
     * {@link #getServiceErrorPagePath(Locale) getServiceErrorPagePath((java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getServiceErrorPagePath(Locale)}
     *             を使用してください。
     */
    public String getServiceErrorPagePath() throws ServicePropertyException {
        return getServiceErrorPagePath((Locale) null);
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(Locale locale)
            throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
                .getServiceErrorPagePath(getCommonBundle(locale));
    }

    /**
     * システム例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getSystemErrorPagePath(String, String)}で取得されるページを返します。 このメソッドは
     * {@link #getSystemErrorPagePath(String, String, String, Locale) getSystemErrorPagePath(application, service, key, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param key
     *            遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく
     *             {@link #getSystemErrorPagePath(String, String, String, Locale)}
     *             を使用してください。
     */
    public String getSystemErrorPagePath(String application, String service,
            String key) throws ServicePropertyException {

        return getSystemErrorPagePath(application, service, key, (Locale) null);
    }

    /**
     * システム例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getSystemErrorPagePath(String, String)}で取得されるページを返します。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param key
     *            遷移先のキー
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(String application, String service,
            String key, Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getSystemErrorPagePath(
                getCommonBundle(locale),
                getResourceBundle(application, locale), application, service,
                key);
    }

    /**
     * システム例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getSystemErrorPagePath(String)}で取得されるページを返します。 このメソッドは
     * {@link #getSystemErrorPagePath(String, String, Locale) getSystemErrorPagePath(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく
     *             {@link #getSystemErrorPagePath(String, String, Locale)}
     *             を使用してください。
     */
    public String getSystemErrorPagePath(String application, String service)
            throws ServicePropertyException {

        return getSystemErrorPagePath(application, service, (Locale) null);
    }

    /**
     * システム例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getSystemErrorPagePath(String)}で取得されるページを返します。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(String application, String service,
            Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getSystemErrorPagePath(
                getCommonBundle(locale),
                getResourceBundle(application, locale), application, service);
    }

    /**
     * システム例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getSystemErrorPagePath()}で取得されるページを返します。 このメソッドは
     * {@link #getSystemErrorPagePath(String, Locale) getSystemErrorPagePath(application, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getSystemErrorPagePath(String, Locale)}
     *             を使用してください。
     */
    public String getSystemErrorPagePath(String application)
            throws ServicePropertyException {

        return getSystemErrorPagePath(application, (Locale) null);
    }

    /**
     * システム例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getSystemErrorPagePath()}で取得されるページを返します。
     * 
     * @param application
     *            アプリケーションID
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(String application, Locale locale)
            throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getSystemErrorPagePath(
                getCommonBundle(locale),
                getResourceBundle(application, locale), application);
    }

    /**
     * システム例外時のページのパスを取得します。 このメソッドは
     * {@link #getSystemErrorPagePath(Locale) getSystemErrorPagePath((java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getSystemErrorPagePath(Locale)}を使用してください。
     */
    public String getSystemErrorPagePath() throws ServicePropertyException {
        return getSystemErrorPagePath((Locale) null);
    }

    /**
     * システム例外時のページのパスを取得します。
     * 
     * @param locale
     *            ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException
     *             遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(Locale locale)
            throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
                .getSystemErrorPagePath(getCommonBundle(locale));
    }

    /**
     * トランジションのクラス名を取得します。 指定されたアプリケーションIDとサービスIDに対応するトランジション名を取得します。
     * 特に設定されていない場合、nullが返ります。 このメソッドは
     * {@link #getTransitionName(String, String, Locale) getTransitionName(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @return トランジションのクラス名、設定されていない場合はnull
     * @throws ServicePropertyException
     *             トランジションのクラス名の取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getTransitionName(String, String, Locale)}
     *             を使用してください。
     */
    public String getTransitionName(String application, String service)
            throws ServicePropertyException {

        return getTransitionName(application, service, (Locale) null);
    }

    /**
     * トランジションのクラス名を取得します。 指定されたアプリケーションIDとサービスIDに対応するトランジション名を取得します。
     * 特に設定されていない場合、nullが返ります。
     * 
     * @param application
     *            アプリケーションID
     * @param service
     *            サービスID
     * @param locale
     *            ロケール
     * @return トランジションのクラス名、設定されていない場合はnull
     * @throws ServicePropertyException
     *             トランジションのクラス名の取得時に例外が発生
     * @since 4.2
     */
    public String getTransitionName(String application, String service,
            Locale locale) throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getTransitionName(
                getResourceBundle(application, locale), application, service);
    }

    /**
     * サービスサーブレットのパスを取得します。
     * 
     * @return サービスサーブレットのパス
     * @throws ServicePropertyException
     *             サービスサーブレットのパスの取得時に例外が発生
     */
    public String getServiceServletPath() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getServiceServletPath(getCommonBundle(new Locale("", "")));
    }

    /**
     * コンテキストパスを取得します。
     * 
     * @return コンテキストパス
     * @throws ServicePropertyException
     *             コンテキストパスの取得時に例外が発生
     * @deprecated このメソッドではなく、javax.servlet.http.HttpRequestのgetContextPathを使用するようにしてください。
     */
    public String getContextPath() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getContextPath(getCommonBundle(new Locale("", "")));
    }

    /**
     * アプリケーションIDに該当するパラメータ名を取得します。 設定されていない場合、
     * {@link ServicePropertyHandler#DEFAULT_APPLICATION_PARAMETER}
     * で定義されている値を返します。
     * 
     * @return アプリケーションIDに該当するパラメータ名
     * @throws ServicePropertyException
     *             アプリケーションIDに該当するパラメータ名の取得時に例外が発生
     * @deprecated この実装は廃止されました。
     */
    public String getApplicationParamName() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getApplicationParamName(getCommonBundle(new Locale("", "")));
    }

    /**
     * サービスIDに該当するパラメータ名を取得します。 設定されていない場合、
     * {@link ServicePropertyHandler#DEFAULT_SERVICE_PARAMETER}で定義されている値を返します。
     * 
     * @return サービスIDに該当するパラメータ名
     * @throws ServicePropertyException
     *             サービスIDに該当するパラメータ名の取得時に例外が発生
     * @deprecated この実装は廃止されました。
     */
    public String getServiceParamName() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getServiceParamName(getCommonBundle(new Locale("", "")));
    }

    /**
     * 例外ページに遷移するときにjavax.servlet.http.HttpServletRequestに例外情報を属性として追加する場合の属性名を取得します。
     * 設定されていない場合、 {@link ServicePropertyHandler#DEFAULT_EXCEPTION_ATTRIBUTE}
     * で定義されている値を返します。
     * 
     * @return 例外の属性名
     * @throws ServicePropertyException
     *             属性名の取得時に例外が発生
     */
    public String getExceptionAttributeName() throws ServicePropertyException {
        try {
            return ResourceBundleServicePropertyHandlerUtil
                    .getExceptionAttributeName(getCommonBundle(new Locale("",
                            "")));
        } catch (MissingResourceException e) {
            return DEFAULT_EXCEPTION_ATTRIBUTE;
        }
    }

    /**
     * ログインユーザが使用するエンコードを保存しておくときの属性名を取得します。 設定されていない場合、
     * {@link #DEFAULT_ENCODING_ATTRIBUTE}で定義されている値を返します。
     * 
     * @return エンコードの属性名
     * @throws ServicePropertyException
     *             属性名の取得時に例外が発生
     */
    public String getEncodingAttributeName() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getEncodingAttributeName(getCommonBundle(new Locale("", "")));
    }

    /**
     * ログインユーザが使用するロケールを保存しておくときの属性名を取得します。 設定されていない場合、
     * {@link #DEFAULT_LOCALE_ATTRIBUTE }で定義されている値を返します 。
     * 
     * @return ロケールの属性名
     * @throws ServicePropertyException
     *             属性名の取得時に例外が発生
     * @since 4.2
     */
    public String getLocaleAttributeName() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getLocaleAttributeName(getCommonBundle(new Locale("", "")));
    }

    /**
     * propertiesファイルが存在するパッケージを取得します。 パッケージ化されていない場合は空文字を返却します。
     * 
     * @param application
     * @return パッケージ
     * @since 5.0
     */
    private String getPropertyPackage(String application) {
        String[] paramAry = application.split("[.]");
        StringBuffer buf = new StringBuffer();
        if (paramAry.length > 1) {
            for (int i = 0; i < paramAry.length - 1; i++) {
                buf.append(paramAry[i]);
                buf.append(File.separator);
            }
        }
        return buf.toString();
    }

    /**
     * アプリケーションIDを取得します。
     * 
     * @param application
     * @return アプリケーションID
     * @since 5.0
     */
    private String getApplicationID(String application) {
        String[] paramAry = application.split("[.]");
        String id = paramAry[paramAry.length - 1];
        return id;
    }
}