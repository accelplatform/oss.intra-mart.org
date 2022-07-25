/*
 * TextFileServicePropertyHandler.java
 *
 * Created on 2002/07/05, 17:48
 */

package org.intra_mart.framework.base.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;
import org.intra_mart.framework.util.*;

/**
 * 指定されたファイルからプロパティ情報を取得するサービスプロパティハンドラです。
 * ファイルの書式は{@link DefaultServicePropertyHandler}で示されるものと同じです。<BR>
 * ファイルが存在するディレクトリ名は{@link #PARAM_FILE_DIR}で示されるパラメータ名で指定します。<BR>
 * {@link #PARAM_DYNAMIC}で示されるパラメータにtrueを指定した場合
 * アプリケーション実行時にもプロパティの変更を動的に反映させることが可能となりますが、
 * パラメータの取得時に毎回ファイル操作を行うためパフォーマンスに悪影響を与える可能性があります。
 * このオプションは、開発時やデバッグ時のようにパラメータを頻繁に変更する必要がある場合のみtrueとし、
 * 通常はfalseに設定しておいてください。
 *
 * @author INTRAMART
 * @since 3.2
 */
public class TextFileServicePropertyHandler implements ServicePropertyHandler {

    /**
     * プロパティファイルのファイルパスのパラメータ名
     */
    public static final String PARAM_FILE_DIR = "file_dir";

    /**
     * プロパティファイルの再読み込み可能のパラメータ名
     */
    public static final String PARAM_DYNAMIC = "dynamic";

    /**
     * データリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

	/**
	 * アプリケーションごとのサービスリソース情報が設定されているリソースバンドル
	 * 
	 * @uml.property name="bundles"
	 * @uml.associationEnd 
	 * @uml.property name="bundles" multiplicity="(0 1)" qualifier="locale:java.util.Locale
	 * bundle:java.util.ResourceBundle"
	 */
	private Map bundles = new HashMap();

	/**
	 * 共通のサービスリソース情報が設定されているリソースバンドル
	 * 
	 * @uml.property name="commonBundles"
	 * @uml.associationEnd 
	 * @uml.property name="commonBundles" multiplicity="(0 1)" qualifier="locale:java.util.Locale
	 * commonBundle:java.util.ResourceBundle"
	 */
	private Map commonBundles = new AnythingHashMap();


    /**
     * サービスプロパティのファイルがあるディレクトリ
     */
    private String propertyFileDir;

    /**
     * 再設定可能フラグ
     */
    private boolean dynamic;

    /**
     * TextFileServicePropertyHandlerを新規に生成します。
     */
    public TextFileServicePropertyHandler() {
        setPropertyFileDir(null);
        setDynamic(false);
    }

	/**
	 * リソースバンドルのプレフィックスを設定します。
	 * 
	 * @param bundlePrefix リソースバンドルのプレフィックス
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
	 * 
	 * @uml.property name="bundlePrefix"
	 */
	private String getBundlePrefix() {
		return this.bundlePrefix;
	}


    /**
     * アプリケーションごとのリソースバンドルの集合を設定します。
     *
     * @param applicationBundles アプリケーションごとのリソースバンドルの集合
     * @since 3.2
     */
    private void setApplicationBundles(Map applicationBundles) {
        this.bundles = applicationBundles;
    }

    /**
     * アプリケーションIDとロケールに一致するリソースバンドルを登録します。
     *
     * @param application アプリケーションID
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 4.2
     */
    private void putApplicationBundle(
        String application,
        ResourceBundle bundle) {

        putApplicationBundle(application, bundle, (Locale) null);
    }

    /**
     * アプリケーションIDとロケールに一致するリソースバンドルを登録します。
     *
     * @param application アプリケーションID
     * @param リソースバンドル
     * @param locale ロケール
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 4.2
     */
    private void putApplicationBundle(
        String application,
        ResourceBundle bundle,
        Locale locale) {

        Map appMap = (Map)this.bundles.get(application);
        if (appMap == null) {
            appMap = new AnythingHashMap();
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
     * @param application アプリケーションID
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 4.2
     */
    private ResourceBundle getApplicationBundle(String application) {
        return getApplicationBundle(application, (Locale) null);
    }

    /**
     * アプリケーションIDとロケールからリソースバンドルを取得します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 4.2
     */
    private ResourceBundle getApplicationBundle(
        String application,
        Locale locale) {
        Map appMap = (Map)this.bundles.get(application);
        if (appMap == null) {
            return null;
        }
        return (ResourceBundle)appMap.get(locale);
    }

    /**
     * 共通のリソースバンドルを設定します。
     *
     * @param commonBundles 共通のリソースバンドル
     * @since 3.2
     */
    private void putCommonBundle(ResourceBundle commonBundle) {
        putCommonBundle(commonBundle, (Locale) null);
    }

    /**
     * 共通のリソースバンドルを設定します。
     *
     * @param commonBundles 共通のリソースバンドル
     * @param locale ロケール
     * @since 4.2
     */
    private void putCommonBundle(ResourceBundle commonBundle, Locale locale) {
        this.commonBundles.put(locale, commonBundle);
    }

    /**
     * 共通のリソースバンドルを取得します。
     *
     * @return 共通のリソースバンドル
     * @throws ServicePropertyException 共通のリソースバンドル取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle getCommonBundle() throws ServicePropertyException {
        return getCommonBundle((Locale) null);
    }

    /**
     * 共通のリソースバンドルを取得します。
     *
     * @param locale ロケール
     * @return 共通のリソースバンドル
     * @throws ServicePropertyException 共通のリソースバンドル取得時に例外が発生
     * @since 4.2
     */
    private ResourceBundle getCommonBundle(Locale locale)
        throws ServicePropertyException {

        if (isDynamic()) {
            try {
                return createCommonBundle(locale);
            } catch (Exception e) {
                throw new ServicePropertyException(e.getMessage(), e);
            }
        } else {
            ResourceBundle result = null;
            synchronized (this.commonBundles) {
                result = (ResourceBundle)this.commonBundles.get(locale);
                if (result == null) {
                    try {
                        result = createCommonBundle(locale);
                    } catch (Exception e) {
                        throw new ServicePropertyException(e.getMessage(), e);
                    }
                    putCommonBundle(result, locale);
                }
            }
            return result;
        }
    }

    /**
     * 共通のリソースバンドルを生成します。
     *
     * @return 共通のリソースバンドル
     * @throws 共通のリソースバンドルの生成に失敗
     */
    private ResourceBundle createCommonBundle()
        throws PropertyHandlerException {
        return createCommonBundle((Locale) null);
    }

    /**
     * 共通のリソースバンドルを生成します。
     *
     * @param locale ロケール
     * @return 共通のリソースバンドル
     * @throws 共通のリソースバンドルの生成に失敗
     */
    private ResourceBundle createCommonBundle(Locale locale)
        throws PropertyHandlerException {
        try {
            return createPropertyResourceBundle(
                getPropertyFileDir() + File.separator + getBundlePrefix(),
                locale);
        } catch (PropertyHandlerException e) {
            throw e;
        } catch (Exception e) {
            throw new PropertyHandlerException(e.getMessage(), e);
        }
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを取得します。
     *
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle getResourceBundle(String application)
        throws ServicePropertyException {
        return getResourceBundle(application, (Locale) null);
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを取得します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
     * @since 4.2
     */
    private ResourceBundle getResourceBundle(String application, Locale locale)
        throws ServicePropertyException {

        if (isDynamic()) {
            return createResourceBundle(application, locale);
        } else {
            ResourceBundle result;
            synchronized (this.bundles) {
                result = getApplicationBundle(application, locale);
                if (result == null) {
                    result = createResourceBundle(application, locale);
                    putApplicationBundle(application, result, locale);
                }
            }
            return result;
        }
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     *
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle createResourceBundle(String application)
        throws ServicePropertyException {
        return createResourceBundle(application, (Locale) null);
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
     * @since 4.2
     */
    private ResourceBundle createResourceBundle(
        String application,
        Locale locale)
        throws ServicePropertyException {

        try {

            String path = getPropertyPackage( application );
            String applicationId = getApplicationID( application );

            return createPropertyResourceBundle(
                getPropertyFileDir()
                    + File.separator
                    + path
                    + getBundlePrefix()
                    + "_"
                    + applicationId,
                locale);

        } catch (Exception e) {
            throw new ServicePropertyException(e.getMessage(), e);
        }
    }

    /**
     * パスで指定されたリソースバンドルを生成します。
     *
     * @param path 指定するファイルのパス
     * @return ファイルのパスに該当するリソースバンドル
     * @throws PropertyHandlerException リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle createPropertyResourceBundle(String path)
        throws PropertyHandlerException {
        return createPropertyResourceBundle(path, (Locale) null);
    }

    /**
     * パスで指定されたリソースバンドルを生成します。
     *
     * @param path 指定するファイルのパス
     * @param locale ロケール
     * @return ファイルのパスに該当するリソースバンドル
     * @throws PropertyHandlerException リソースバンドルの取得時に例外が発生
     * @since 4.2
     */
    private ResourceBundle createPropertyResourceBundle(
        String path,
        Locale locale)
        throws PropertyHandlerException {

        ResourceBundle bundle = null;
        try {
            if (locale == null) {
                bundle = new FileResourceBundle(path);
            } else {
                bundle = new FileResourceBundle(path, locale);
            }
        } catch (IOException e) {
            throw new PropertyHandlerException(e.getMessage());
        } catch (MissingResourceException e) {
            throw new PropertyHandlerException(e.getMessage());
        }

        return (ResourceBundle)bundle;
    }

    /**
     * プロパティハンドラを初期化します。
     *
     * @param params 初期パラメータ
     * @throws PropertyHandlerException プロパティハンドラの初期化時に例外が発生
     */
    public void init(PropertyParam[] params) throws PropertyHandlerException {
        String bundleName = null;
        String fileDir = null;
        String dynamic = null;

        // パラメータのパース
        for (int i = 0; i < params.length; i++) {
            if (params[i]
                .getName()
                .equals(
                    DefaultServicePropertyHandler.DEFAULT_BUNDLE_NAME_PARAM)) {
                // リソースバンドルのファイル名の場合
                bundleName = params[i].getValue();
            } else if (params[i].getName().equals(PARAM_FILE_DIR)) {
                // ファイルディレクトリの場合
                fileDir = params[i].getValue();
            } else if (params[i].getName().equals(PARAM_DYNAMIC)) {
                // 再設定可能フラグの場合
                dynamic = params[i].getValue();
            }
        }

        // リソースバンドルプレフィックスの設定
        if (bundleName == null) {
            bundleName = DefaultServicePropertyHandler.DEFAULT_BUNDLE_NAME;
        }
        setBundlePrefix(bundleName);

        // 再設定可能フラグの設定
        Boolean dummyDynamic = new Boolean(dynamic);
        setDynamic(dummyDynamic.booleanValue());

        // プロパティファイルディレクトリの必須チェック
        if (fileDir == null) {
            String message = null;
            try {
                message =
                    java
                        .util
                        .ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("TextFileEventPropertyHandler.param.FileDirNotFound");
            } catch (MissingResourceException e) {
            }
            throw new PropertyHandlerException(
                message + " : " + PARAM_FILE_DIR);
        }
        this.propertyFileDir = fileDir;

        // 再設定不可の場合プロパティハンドラを設定
        if (!isDynamic()) {
            putCommonBundle(createCommonBundle());
        }
    }

	/**
	 * プロパティファイルがあるディレクトリを設定します。
	 * 
	 * @param propertyFileDir  プロパティファイルがあるディレクトリ
	 * 
	 * @uml.property name="propertyFileDir"
	 */
	private void setPropertyFileDir(String propertyFileDir) {
		this.propertyFileDir = propertyFileDir;
	}

	/**
	 * プロパティファイルがあるディレクトリを取得します。
	 * 
	 * @return プロパティファイルがあるディレクトリ
	 * 
	 * @uml.property name="propertyFileDir"
	 */
	private String getPropertyFileDir() {
		return this.propertyFileDir;
	}

	/**
	 * 再設定可能／不可能を設定します。
	 * 
	 * @param dynamic true 再設定可能、false 再設定不可
	 * 
	 * @uml.property name="dynamic"
	 */
	private void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

    /**
     * 再設定可能かどうかをチェックします。
     *
     * @return true 再設定可能、false 再設定不可
     */
    public boolean isDynamic() {
        return this.dynamic;
    }

    /**
     * クライアントのエンコードを取得します。
     *
     * @return クライアントのエンコーディング
     * @throws ServicePropertyException クライアントのエンコードの取得時に例外が発生
     */
    public String getClientEncoding() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil.getClientEncoding(
            getCommonBundle(new Locale("", "")));
    }

    /**
     * クライアントのロケールを取得します。
     *
     * @return クライアントのロケール
     * @throws ServicePropertyException クライアントのロケールの取得時に例外が発生
     */
    public Locale getClientLocale() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil.getClientLocale(
            getCommonBundle(new Locale("", "")));
    }

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まる場合にこのメソッドを使用します。
     * このメソッドは{@link #getNextPagePath(String, String, Locale) getNextPagePath(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getNextPagePath(String, String, Locale)}を使用してください。
     */
    public String getNextPagePath(String application, String service)
        throws ServicePropertyException {
        return getNextPagePath(application, service, (Locale) null);
    }

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まる場合にこのメソッドを使用します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getNextPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil.getNextPagePath(
            getResourceBundle(application, locale),
            application,
            service);
    }

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まらない場合にこのメソッドを使用します。<CODE>key<CODE>が<CODE>null<CODE>の場合、{@link #getNextPagePath(String, String)}と同じ動作になります。
     * このメソッドは{@link #getNextPagePath(String, String, String, Locale) getNextPagePath(application, service, key, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getNextPagePath(String, String, String, Locale)}を使用してください。
     */
    public String getNextPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException {
        return getNextPagePath(application, service, key, (Locale) null);
    }

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まらない場合にこのメソッドを使用します。<CODE>key<CODE>が<CODE>null<CODE>の場合、{@link #getNextPagePath(String, String)}と同じ動作になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getNextPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil.getNextPagePath(
            getResourceBundle(application, locale),
            application,
            service,
            key);
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String, String)}で取得されるページを返します。
     * このメソッドは{@link #getInputErrorPagePath(String, String, String, Locale) getInputErrorPagePath(application, service, key, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getInputErrorPagePath(String, String, String, Locale)}を使用してください。
     */
    public String getInputErrorPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException {

        return getInputErrorPagePath(application, service, key, (Locale) null);
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String, String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getInputErrorPagePath(
            getCommonBundle(locale),
            getResourceBundle(application, locale),
            application,
            service,
            key);
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String)}で取得されるページを返します。
     * このメソッドは{@link #getInputErrorPagePath(String, String, Locale) getInputErrorPagePath(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getInputErrorPagePath(String, String, Locale)}を使用してください。
     */
    public String getInputErrorPagePath(String application, String service)
        throws ServicePropertyException {
        return getInputErrorPagePath(application, service, (Locale) null);
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getInputErrorPagePath(
            getCommonBundle(locale),
            getResourceBundle(application, locale),
            application,
            service);
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath()}で取得されるページを返します。
     * このメソッドは{@link #getInputErrorPagePath(String, Locale) getInputErrorPagePath(application, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getInputErrorPagePath(String, Locale)}を使用してください。
     */
    public String getInputErrorPagePath(String application)
        throws ServicePropertyException {

        return getInputErrorPagePath(application, (Locale) null);
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath()}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(String application, Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getInputErrorPagePath(
            getCommonBundle(locale),
            getResourceBundle(application, locale),
            application);
    }

    /**
     * 入力例外時のページのパスを取得します。
     * このメソッドは{@link #getInputErrorPagePath(Locale) getInputErrorPagePath((java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getInputErrorPagePath(Locale)}を使用してください。
     */
    public String getInputErrorPagePath() throws ServicePropertyException {
        return getInputErrorPagePath((Locale) null);
    }

    /**
     * 入力例外時のページのパスを取得します。
     *
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getInputErrorPagePath(
            getCommonBundle(locale));
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath(String, String)}で取得されるページを返します。
     * このメソッドは{@link #getServiceErrorPagePath(String, String, String, Locale) getServiceErrorPagePath(application, service, key, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceErrorPagePath(String, String, String, Locale)}を使用してください。
     */
    public String getServiceErrorPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException {

        return getServiceErrorPagePath(
            application,
            service,
            key,
            (Locale) null);
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath(String, String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
            .getServiceErrorPagePath(
            getCommonBundle(locale),
            getResourceBundle(application, locale),
            application,
            service,
            key);
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath(String)}で取得されるページを返します。
     * このメソッドは{@link #getServiceErrorPagePath(String, String, Locale) getServiceErrorPagePath(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceErrorPagePath(String, String, Locale)}を使用してください。
     */
    public String getServiceErrorPagePath(String application, String service)
        throws ServicePropertyException {

        return getServiceErrorPagePath(application, service, (Locale) null);
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath(String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
            .getServiceErrorPagePath(
            getCommonBundle(locale),
            getResourceBundle(application, locale),
            application,
            service);
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath()}で取得されるページを返します。
     * このメソッドは{@link #getServiceErrorPagePath(String, Locale) getServiceErrorPagePath(application, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceErrorPagePath(String, Locale)}を使用してください。
     */
    public String getServiceErrorPagePath(String application)
        throws ServicePropertyException {

        return getServiceErrorPagePath(application, (Locale) null);
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath()}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(String application, Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
            .getServiceErrorPagePath(
            getCommonBundle(locale),
            getResourceBundle(application, locale),
            application);
    }

    /**
     * 処理例外時のページのパスを取得します。
     * このメソッドは{@link #getServiceErrorPagePath(Locale) getServiceErrorPagePath((java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceErrorPagePath(Locale)}を使用してください。
     */
    public String getServiceErrorPagePath() throws ServicePropertyException {
        return getServiceErrorPagePath((Locale) null);
    }

    /**
     * 処理例外時のページのパスを取得します。
     *
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
            .getServiceErrorPagePath(
            getCommonBundle(locale));
    }

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String, String)}で取得されるページを返します。
     * このメソッドは{@link #getSystemErrorPagePath(String, String, String, Locale) getSystemErrorPagePath(application, service, key, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getSystemErrorPagePath(String, String, String, Locale)}を使用してください。
     */
    public String getSystemErrorPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException {

        return getSystemErrorPagePath(application, service, key, (Locale) null);
    }

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String, String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getSystemErrorPagePath(
            getCommonBundle(locale),
            getResourceBundle(application, locale),
            application,
            service,
            key);
    }

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String)}で取得されるページを返します。
     * このメソッドは{@link #getSystemErrorPagePath(String, String, Locale) getSystemErrorPagePath(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getSystemErrorPagePath(String, String, Locale)}を使用してください。
     */
    public String getSystemErrorPagePath(String application, String service)
        throws ServicePropertyException {

        return getSystemErrorPagePath(application, service, (Locale) null);
    }

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getSystemErrorPagePath(
            getCommonBundle(locale),
            getResourceBundle(application, locale),
            application,
            service);
    }

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath()}で取得されるページを返します。
     * このメソッドは{@link #getSystemErrorPagePath(String, Locale) getSystemErrorPagePath(application, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getSystemErrorPagePath(String, Locale)}を使用してください。
     */
    public String getSystemErrorPagePath(String application)
        throws ServicePropertyException {

        return getSystemErrorPagePath(application, (Locale) null);
    }

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath()}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(String application, Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getSystemErrorPagePath(
            getCommonBundle(locale),
            getResourceBundle(application, locale),
            application);
    }

    /**
     * システム例外時のページのパスを取得します。
     * このメソッドは{@link #getSystemErrorPagePath(Locale) getSystemErrorPagePath((java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getSystemErrorPagePath(Locale)}を使用してください。
     */
    public String getSystemErrorPagePath() throws ServicePropertyException {
        return getSystemErrorPagePath((Locale) null);
    }

    /**
     * システム例外時のページのパスを取得します。
     *
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getSystemErrorPagePath(
            getCommonBundle(locale));
    }

    /**
     * サービスコントローラのクラス名を取得します。
     * 該当するサービスコントローラが存在しない場合、nullを返します。
     * このメソッドは{@link #getServiceControllerName(String, String, Locale) getServiceControllerName(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return サービスコントローラのクラス名、存在しない場合はnull
     * @throws ServicePropertyException サービスコントローラのクラス名の取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceControllerName(String, String, Locale)}を使用してください。
     */
    public String getServiceControllerName(String application, String service)
        throws ServicePropertyException {

        return getServiceControllerName(application, service, (Locale) null);
    }

    /**
     * サービスコントローラのクラス名を取得します。
     * 該当するサービスコントローラが存在しない場合、nullを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return サービスコントローラのクラス名、存在しない場合はnull
     * @throws ServicePropertyException サービスコントローラのクラス名の取得時に例外が発生
     * @since 4.2
     */
    public String getServiceControllerName(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil
            .getServiceControllerName(
            getResourceBundle(application, locale),
            application,
            service);
    }

    /**
     * トランジションのクラス名を取得します。
     * 指定されたアプリケーションIDとサービスIDに対応するトランジション名を取得します。
     * 特に設定されていない場合、nullが返ります。
     * このメソッドは{@link #getTransitionName(String, String, Locale) getTransitionName(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return トランジションのクラス名、設定されていない場合はnull
     * @throws ServicePropertyException トランジションのクラス名の取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getTransitionName(String, String, Locale)}を使用してください。
     */
    public String getTransitionName(String application, String service)
        throws ServicePropertyException {

        return getTransitionName(application, service, (Locale) null);
    }

    /**
     * トランジションのクラス名を取得します。
     * 指定されたアプリケーションIDとサービスIDに対応するトランジション名を取得します。
     * 特に設定されていない場合、nullが返ります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return トランジションのクラス名、設定されていない場合はnull
     * @throws ServicePropertyException トランジションのクラス名の取得時に例外が発生
     * @since 4.2
     */
    public String getTransitionName(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {

        return ResourceBundleServicePropertyHandlerUtil.getTransitionName(
            getResourceBundle(application, locale),
            application,
            service);
    }

    /**
     * サービスサーブレットのパスを取得します。
     *
     * @return サービスサーブレットのパス
     * @throws ServicePropertyException サービスサーブレットのパスの取得時に例外が発生
     */
    public String getServiceServletPath() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil.getServiceServletPath(
            getCommonBundle(new Locale("", "")));
    }

    /**
     * コンテキストパスを取得します。
     *
     * @return コンテキストパス
     * @throws ServicePropertyException コンテキストパスの取得時に例外が発生
     * @deprecated このメソッドではなく、javax.servlet.http.HttpRequestのgetContextPathを使用するようにしてください。
     */
    public String getContextPath() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil.getContextPath(
            getCommonBundle(new Locale("", "")));
    }

    /**
     * アプリケーションIDに該当するパラメータ名を取得します。
     * 設定されていない場合、{@link DefaultServicePropertyHandler#DEFAULT_APPLICATION_PARAMETER}で定義されている値を返します。
     *
     * @return アプリケーションIDに該当するパラメータ名
     * @throws ServicePropertyException アプリケーションIDに該当するパラメータ名の取得時に例外が発生
     * @deprecated この実装は廃止されました。
     */
    public String getApplicationParamName() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
            .getApplicationParamName(
            getCommonBundle(new Locale("", "")));
    }

    /**
     * サービスIDに該当するパラメータ名を取得します。
     * 設定されていない場合、{@link DefaultServicePropertyHandler#DEFAULT_SERVICE_PARAMETER}で定義されている値を返します。
     *
     * @return サービスIDに該当するパラメータ名
     * @throws ServicePropertyException サービスIDに該当するパラメータ名の取得時に例外が発生
     * @deprecated この実装は廃止されました。
     */
    public String getServiceParamName() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil.getServiceParamName(
            getCommonBundle(new Locale("", "")));
    }

    /**
     * 例外ページに遷移するときにjavax.servlet.http.HttpServletRequestに例外情報を属性として追加する場合の属性名を取得します。
     *
     * @return 例外の属性名
     * @throws ServicePropertyException 属性名の取得時に例外が発生
     */
    public String getExceptionAttributeName() throws ServicePropertyException {
        try {
            return ResourceBundleServicePropertyHandlerUtil
                .getExceptionAttributeName(
                getCommonBundle(new Locale("", "")));
        } catch (MissingResourceException e) {
            return DEFAULT_EXCEPTION_ATTRIBUTE;
        }
    }

    /**
     * ログインユーザが使用するエンコードを保存しておくときの属性名を取得します。
     * 設定されていない場合、{@link #DEFAULT_ENCODING_ATTRIBUTE}で定義されている値を返します。
     *
     * @return エンコードの属性名
     * @throws ServicePropertyException 属性名の取得時に例外が発生
     */
    public String getEncodingAttributeName() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
            .getEncodingAttributeName(
            getCommonBundle(new Locale("", "")));
    }

    /**
     * ログインユーザが使用するロケールを保存しておくときの属性名を取得します。
     * 設定されていない場合、{@link #DEFAULT_LOCALE_ATTRIBUTE}で定義されている値を返します。
     *
     * @return ロケールの属性名
     * @throws ServicePropertyException 属性名の取得時に例外が発生
     * @since 4.2
     */
    public String getLocaleAttributeName() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil.getLocaleAttributeName(
            getCommonBundle(new Locale("", "")));
    }

    /**
	 * propertiesファイルが存在するパッケージを取得します。
	 * パッケージ化されていない場合は空文字を返却します。
	 *  
	 * @param application
	 * @return パッケージ
     * @since 2004.09.13
	 */
    private String getPropertyPackage( String application ) {
        String[] paramAry = application.split("[.]");
		StringBuffer buf = new StringBuffer();
		if ( paramAry.length > 1 ) {
			for ( int i = 0; i < paramAry.length - 1; i++ ) {
			    buf.append(File.separator);
			    buf.append(paramAry[i]);
			}
		    buf.append(File.separator);
		}
        return buf.toString();
	}

    /**
	 * アプリケーションIDを取得します。
	 * 
	 * @param application
	 * @return アプリケーションID
     * @since 2004.09.13
	 */
    private String getApplicationID( String application ) {
        String[] paramAry = application.split("[.]");
	    String id = paramAry[paramAry.length - 1];
        return id;
	}
    
    /**
     * キーとしてnullを持てるHashMapです。
     *
     * @author INTRAMART
     * @since 4.2
     */
    private class AnythingHashMap extends HashMap {

        /**
         * キーがnullの時の値
         */
        private Object nullObject = new Object() {
            public boolean equals(Object obj) {
                return obj == this;
            }
        };

        /** 
         * 指定のキーをマップする値を返します。
         * 
         * @param key 関連付けられた値が返されるキー
         */
        public Object get(Object key) {
            if (key == null) {
                return super.get(nullObject);
            }
            return super.get(key);
        }

        /**
         * マップ内の指定のキーと関連付けます。
         * 
         * @param key 指定された値が関連付けられるキー
         * @param value 指定されたキーに関連付けられる値
         */
        public Object put(Object key, Object value) {
            if (key == null) {
                Object result = super.get(this.nullObject);
                super.put(this.nullObject, value);
                return result;
            }
            return super.put(key, value);
        }
    }
}
