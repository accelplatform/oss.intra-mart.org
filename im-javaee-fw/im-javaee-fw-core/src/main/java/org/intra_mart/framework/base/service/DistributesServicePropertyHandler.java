/*
 * DistributesServicePropertyHandler.java
 *
 * Created on 2004/09/15, 14:15
 */

package org.intra_mart.framework.base.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;

/**
 * アプリケーションごとに異なるサービスプロパティハンドラを使用するサービスプロパティハンドラです。 <BR>
 * 
 * 通常時に使用されるサービスプロパティハンドラは <code>default.class</code> で示されるパラメータ名で指定します。
 * ここで指定されるサービスプロパティハンドラに渡す初期値は、 <code>default.param.<i>パラメータ名</i></code>
 * で示されるパラメータ名で指定します。
 * 
 * ある特定のアプリケーションに対して特定のサービスプロパティハンドラを割り当てたい場合、
 * <code>application.class.<I>アプリケーションID</I></code> で示されるパラメータ名で指定します。
 * ここで指定されるサービスプロパティハンドラに渡す初期値は、
 * <code>application.param.<I>アプリケーションID</I>.<i>パラメータ名</i></code>
 * で示されるパラメータ名で指定します。
 * 
 * ファイルが存在するディレクトリ名は {@link #PARAM_FILE_DIR}で示されるパラメータ名で指定します。 <BR>
 * {@link #PARAM_DYNAMIC}で示されるパラメータにtrueを指定した場合
 * アプリケーション実行時にもプロパティの変更を動的に反映させることが可能となりますが、
 * パラメータの取得時に毎回ファイル操作を行うためパフォーマンスに悪影響を与える可能性があります。
 * このオプションは、開発時やデバッグ時のようにパラメータを頻繁に変更する必要がある場合のみtrueとし、 通常はfalseに設定しておいてください。
 * 
 * @author INTRAMART
 * @since 5.0
 */
public class DistributesServicePropertyHandler implements
        ServicePropertyHandler {

    /**
     * デフォルトのリソースバンドル名のプレフィックス
     */
    public static final String DEFAULT_BUNDLE_NAME = "ServiceConfig";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * デフォルト
     */
    public static final String PARAM_DEFAULT = "default";

    /**
     * アプリケーション
     */
    public static final String PARAM_APPLICATION = "application";

    /**
     * パラメータ
     */
    public static final String PARAM_APPLICATION_PARAM = "param";

    /**
     * クラス
     */
    public static final String PARAM_APPLICATION_CLASS = "class";

    /**
     * プロパティファイルのファイルパスのパラメータ名
     */
    public static final String PARAM_FILE_DIR = "file_dir";

    /**
     * プロパティファイルの再読み込み可能のパラメータ名
     */
    public static final String PARAM_DYNAMIC = "dynamic";

    /**
     * プロパティファイルのバンドルのパラメータ名
     */
    public static final String PARAM_BUNDLE = "bundle";

    /**
     * データリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

    /**
     * アプリケーションごとのハンドラ情報が設定されているリソースバンドル
     */
    private Map bundles = new HashMap();

    /**
     * 共通のサービスリソース情報が設定されているリソースバンドル
     */
    private Map commonBundle = new HashMap();

    /**
     * DefaultServicePropertyHandlerを新規に生成します。
     */
    public DistributesServicePropertyHandler() {
        setBundlePrefix(null);
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
     * アプリケーションバンドルからハンドラのリソース情報を取得します。
     * 
     * @return ハンドラのリソース情報
     */
    private Object getApplicationBundle(String application) {
        Object obj = this.bundles.get(application);
        if (obj == null) {
            obj = this.bundles.get(PARAM_DEFAULT);
        }
        return obj;
    }

    /**
     * 共通のリソースバンドルを設定します。
     * 
     * @param commonBundle 共通のリソースバンドル
     */
    private void setCommonBundle(ResourceBundle commonBundle) {
        setCommonBundle(commonBundle, (Locale) null);
    }

    /**
     * 共通のリソースバンドルを設定します。
     * 
     * @param locale ロケール
     * @param commonBundle 共通のリソースバンドル
     */
    private void setCommonBundle(ResourceBundle commonBundle, Locale locale) {
        this.commonBundle.put(locale, commonBundle);
    }

    /**
     * 共通のリソースバンドルを取得します。
     * 
     * @return 共通のリソースバンドル
     * @throws ServicePropertyException 共通のリソースバンドル取得時に例外が発生
     */
    private ResourceBundle getCommonBundle() throws ServicePropertyException {
        return getCommonBundle(null);
    }

    /**
     * 共通のリソースバンドルを取得します。
     * 
     * @return 共通のリソースバンドル
     * @throws ServicePropertyException 共通のリソースバンドル取得時に例外が発生
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
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
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
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     * 
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
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
     * propertiesファイルが存在するパッケージを取得します。 パッケージ化されていない場合は空文字を返却します。
     * 
     * @param application
     * @return パッケージ
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
     */
    private String getApplicationID(String application) {

        String[] paramAry = application.split("[.]");
        String id = paramAry[paramAry.length - 1];

        return id;
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
            // デフォルトのHandlerクラスを取得します。
            getDefaultHandler(params);

            // アプリケーション毎のHandlerクラスを取得します。
            getApplicationHandler(params);

        }

        if (bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        }

        setBundlePrefix(bundleName);

    }

    /**
     * DefaultのHandler情報を取得します。
     * 
     * @param params 初期パラメータ
     */
    private void getDefaultHandler(PropertyParam[] params)
            throws PropertyHandlerException {

        List keys = new ArrayList();
        HashMap values = new HashMap();

        try {
            // "default"で絞り込みます。
            for (int i = 0; i < params.length; i++) {
                if (params[i].getName().indexOf(PARAM_DEFAULT) != -1) {
                    keys.add(params[i].getName());
                    values.put(params[i].getName(), params[i].getValue());
                }
            }

            setApplicationBundles(PARAM_DEFAULT, keys, values);

        } catch (Exception e) {
            throw new PropertyHandlerException();
        }
    }

    /**
     * Application毎のHandler情報を取得します。
     * 
     * @param params 初期パラメータ
     */
    private void getApplicationHandler(PropertyParam[] params)
            throws PropertyHandlerException {

        List keys = new ArrayList();
        Map values = new HashMap();

        // "application"で絞り込みます。
        for (int i = 0; i < params.length; i++) {
            if (params[i].getName().indexOf(PARAM_APPLICATION) != -1) {
                keys.add(params[i].getName());
                values.put(params[i].getName(), params[i].getValue());
            }
        }

        // applicationIDのリストを取得します
        String[] appIds = getApplicationIdList(keys);

        // applicationIDをもとに、class、propertyを取得します
        for (int i = 0; i < appIds.length; i++) {

            setApplicationBundles(appIds[i], keys, values);

        }

    }

    /**
     * Handlerクラスを生成し、Mapに保持します。
     * 
     * @param id
     * @param list
     * @param map
     */
    private void setApplicationBundles(String id, List keys, Map values)
            throws PropertyHandlerException {

        // ハンドラクラス名を取得します。
        String className = getApplicationHandlerClass(id, keys, values);

        Object obj = null;
        try {
            // クラスを生成します。
            obj = Class.forName(className).newInstance();
        } catch (Exception ex) {
            throw new PropertyHandlerException();
        }

        // パラメータを取得します。
        PropertyParam[] params = getApplicationHandlerProperty(id, keys, values);

        // Handlerクラスを生成しMapに保持します。
        Map returnMap = new HashMap();
        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            handler.init(params);

            // Mapに保持します。
            this.bundles.put(id, handler);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            handler.init(params);
            // Mapに保持します。
            this.bundles.put(id, handler);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            handler.init(params);
            // Mapに保持します。
            this.bundles.put(id, handler);

        }

    }

    /**
     * ApplicationIDのリストを取得します
     * 
     * @param params 初期パラメータ
     */
    private String[] getApplicationIdList(List keys) {

        // applicationIDのリストを取得します
        HashSet set = new HashSet();
        for (int i = 0; i < keys.size(); i++) {
            String str = (String) keys.get(i);
            String[] paramArray = str.split("[.]");
            set.add(getApplications(paramArray));
        }

        return (String[]) set.toArray(new String[0]);

    }

    /**
     * ApplicationIDのリストを取得します
     * 
     * @param params 初期パラメータ
     */
    private String getApplications(String[] paramArray) {

        String aplid = new String();
        int lastpos = 0;
        int firstpos = 0;

        if (paramArray[0].equals(PARAM_APPLICATION)) {
            firstpos = 1;
        }

        if (paramArray[paramArray.length - 1].equals(PARAM_APPLICATION_CLASS)) {
            lastpos = 1;
        } else if (paramArray[paramArray.length - 2]
                .equals(PARAM_APPLICATION_PARAM)
                && (paramArray[paramArray.length - 1].equals(PARAM_DYNAMIC)
                        || paramArray[paramArray.length - 1]
                                .equals(PARAM_FILE_DIR) || paramArray[paramArray.length - 1]
                        .equals(PARAM_BUNDLE))) {
            lastpos = 2;
        }

        for (int i = firstpos; i < paramArray.length - lastpos; i++) {
            if (i == firstpos) {
                aplid += paramArray[i];
            } else {
                aplid += "." + paramArray[i];
            }
        }

        return aplid;

    }

    /**
     * Handlerクラス名を取得します。
     * 
     * @param applicationId
     * @param list
     * @param map
     * @return
     */
    private String getApplicationHandlerClass(String id, List list, Map map) {

        Iterator listite = list.iterator();
        String name = new String();

        // applicationIdが一致するものだけ取り出します
        while (listite.hasNext()) {

            String key = (String) listite.next();

            // "<applicationID>.class"の値を取得します。
            if (key.indexOf(id + "." + PARAM_APPLICATION_CLASS) != -1) {
                name = (String) map.get(key);
            }

        }

        return name;

    }

    /**
     * Handlerに引き渡すパラメータを取得します。
     * 
     * @param applicationId
     * @param list
     * @param map
     * @return
     */
    private PropertyParam[] getApplicationHandlerProperty(String applicationId,
            List list, Map map) {

        Iterator iterator = list.iterator();
        List keys = new ArrayList();
        Map properties = new HashMap();

        while (iterator.hasNext()) {

            String key = (String) iterator.next();

            // "<applicationID>.param.dynamic"の値を取得します。
            if (key.indexOf(applicationId + "." + PARAM_APPLICATION_PARAM + "."
                    + PARAM_DYNAMIC) != -1) {
                keys.add(PARAM_DYNAMIC);
                properties.put(PARAM_DYNAMIC, (String) map.get(key));

                // "<applicationID>.param.file_dir"の値を取得します。
            } else if (key.indexOf(applicationId + "."
                    + PARAM_APPLICATION_PARAM + "." + PARAM_FILE_DIR) != -1) {
                keys.add(PARAM_FILE_DIR);
                properties.put(PARAM_FILE_DIR, (String) map.get(key));

                // "<applicationID>.param.bundle"の値を取得します。
            } else if (key.indexOf(applicationId + "."
                    + PARAM_APPLICATION_PARAM + "." + PARAM_BUNDLE) != -1) {
                keys.add(PARAM_BUNDLE);
                properties.put(PARAM_BUNDLE, (String) map.get(key));
            }

        }

        // PropertyParamを生成します。
        PropertyParam[] param = new PropertyParam[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            param[i] = new PropertyParam();
            param[i].setName((String) keys.get(i));
            param[i].setValue((String) properties.get(keys.get(i)));
        }

        return param;

    }

    /**
     * プロパティの動的読み込みが可能かどうか調べます。 このクラスではこのメソッドは常にfalseを返します。
     * 
     * @return 常にfalse
     * @throws ServicePropertyException チェック時に例外が発生
     */
    public boolean isDynamic() throws ServicePropertyException {
        return false;
    }

    /**
     * クライアントのエンコードを取得します。
     * 
     * @return クライアントのエンコーディング
     * @throws ServicePropertyException クライアントのエンコードの取得時に例外が発生
     */
    public String getClientEncoding() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getClientEncoding(getCommonBundle(new Locale("", "")));
    }

    /**
     * クライアントのエンコードを取得します。
     * 
     * @return クライアントのエンコーディング
     * @throws ServicePropertyException クライアントのエンコードの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getInputErrorPagePath(String application, String service,
            String key, Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getInputErrorPagePath(application, service, key,
                    locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getInputErrorPagePath(application, service, key,
                    locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getInputErrorPagePath(application, service, key,
                    locale);

        }

        return ret;
    }

    /**
     * 入力例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getInputErrorPagePath(String)}で取得されるページを返します。 このメソッドは
     * {@link #getInputErrorPagePath(String, String, Locale) getInputErrorPagePath(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getInputErrorPagePath(String application, String service,
            Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getInputErrorPagePath(application, service, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getInputErrorPagePath(application, service, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getInputErrorPagePath(application, service, locale);

        }

        return ret;
    }

    /**
     * 入力例外時のページのパスを取得します。 該当するページのパスが取得できない場合、 {@link #getInputErrorPagePath()}
     * で取得されるページを返します。 このメソッドは
     * {@link #getInputErrorPagePath(String, Locale) getInputErrorPagePath(application, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getInputErrorPagePath(String application, Locale locale)
            throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getInputErrorPagePath(application, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getInputErrorPagePath(application, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getInputErrorPagePath(application, locale);

        }

        return ret;
    }

    /**
     * 入力例外時のページのパスを取得します。 このメソッドは
     * {@link #getInputErrorPagePath(Locale) getInputErrorPagePath((java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getInputErrorPagePath(Locale)}を使用してください。
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
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getNextPagePath(String application, String service,
            Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getNextPagePath(application, service, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getNextPagePath(application, service, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getNextPagePath(application, service, locale);

        }

        return ret;
    }

    /**
     * 遷移先のページのパスを取得します。 サービスから遷移先が一意に決まらない場合にこのメソッドを使用します。 <CODE>key <CODE>が
     * <CODE>null <CODE>の場合、 {@link #getNextPagePath(String, String)}
     * と同じ動作になります。 このメソッドは
     * {@link #getNextPagePath(String, String, String, Locale) getNextPagePath(application, service, key, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getNextPagePath(String application, String service,
            String key, Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getNextPagePath(application, service, key, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getNextPagePath(application, service, key, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getNextPagePath(application, service, key, locale);

        }

        return ret;
    }

    /**
     * サービスコントローラのクラス名を取得します。 該当するサービスコントローラが存在しない場合、nullを返します。 このメソッドは
     * {@link #getServiceControllerName(String, String, Locale) getServiceControllerName(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application アプリケーションID
     * @param service サービスID
     * @return サービスコントローラのクラス名、存在しない場合はnull
     * @throws ServicePropertyException サービスコントローラのクラス名の取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return サービスコントローラのクラス名、存在しない場合はnull
     * @throws ServicePropertyException サービスコントローラのクラス名の取得時に例外が発生
     */
    public String getServiceControllerName(String application, String service,
            Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler
                    .getServiceControllerName(application, service, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler
                    .getServiceControllerName(application, service, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler
                    .getServiceControllerName(application, service, locale);

        }

        return ret;
    }

    /**
     * 処理例外時のページのパスを取得します。 このメソッドは
     * {@link #getServiceErrorPagePath(String, String, String, Locale) getServiceErrorPagePath(application, service, key, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getServiceErrorPagePath(String application, String service,
            String key, Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getServiceErrorPagePath(application, service, key,
                    locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getServiceErrorPagePath(application, service, key,
                    locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getServiceErrorPagePath(application, service, key,
                    locale);

        }

        return ret;

    }

    /**
     * 処理例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getServiceErrorPagePath(String)}で取得されるページを返します。 このメソッドは
     * {@link #getServiceErrorPagePath(String, String, Locale) getServiceErrorPagePath(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getServiceErrorPagePath(String application, String service,
            Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getServiceErrorPagePath(application, service, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getServiceErrorPagePath(application, service, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getServiceErrorPagePath(application, service, locale);

        }

        return ret;
    }

    /**
     * 処理例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getServiceErrorPagePath()}で取得されるページを返します。 このメソッドは
     * {@link #getServiceErrorPagePath(String, Locale) getServiceErrorPagePath(application, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getServiceErrorPagePath(String application, Locale locale)
            throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getServiceErrorPagePath(application, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getServiceErrorPagePath(application, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getServiceErrorPagePath(application, locale);

        }

        return ret;
    }

    /**
     * 処理例外時のページのパスを取得します。 このメソッドは
     * {@link #getServiceErrorPagePath(Locale) getServiceErrorPagePath((java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getServiceErrorPagePath(Locale)}
     *             を使用してください。
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getSystemErrorPagePath(String application, String service,
            String key, Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getSystemErrorPagePath(application, service, key,
                    locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getSystemErrorPagePath(application, service, key,
                    locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getSystemErrorPagePath(application, service, key,
                    locale);

        }

        return ret;
    }

    /**
     * システム例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getSystemErrorPagePath(String)}で取得されるページを返します。 このメソッドは
     * {@link #getSystemErrorPagePath(String, String, Locale) getSystemErrorPagePath(application, service, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getSystemErrorPagePath(String application, String service,
            Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getSystemErrorPagePath(application, service, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getSystemErrorPagePath(application, service, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getSystemErrorPagePath(application, service, locale);

        }

        return ret;
    }

    /**
     * システム例外時のページのパスを取得します。 該当するページのパスが取得できない場合、
     * {@link #getSystemErrorPagePath()}で取得されるページを返します。 このメソッドは
     * {@link #getSystemErrorPagePath(String, Locale) getSystemErrorPagePath(application, (java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
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
     * @param application アプリケーションID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public String getSystemErrorPagePath(String application, Locale locale)
            throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getSystemErrorPagePath(application, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getSystemErrorPagePath(application, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getSystemErrorPagePath(application, locale);

        }

        return ret;
    }

    /**
     * システム例外時のページのパスを取得します。 このメソッドは
     * {@link #getSystemErrorPagePath(Locale) getSystemErrorPagePath((java.util.Locale)null)}
     * を呼んだときと同じ結果になります。
     * 
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく {@link #getSystemErrorPagePath(Locale)}を使用してください。
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
     * @param application アプリケーションID
     * @param service サービスID
     * @return トランジションのクラス名、設定されていない場合はnull
     * @throws ServicePropertyException トランジションのクラス名の取得時に例外が発生
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
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return トランジションのクラス名、設定されていない場合はnull
     * @throws ServicePropertyException トランジションのクラス名の取得時に例外が発生
     */
    public String getTransitionName(String application, String service,
            Locale locale) throws ServicePropertyException {

        String ret = new String();

        Object obj = getApplicationBundle(application);

        if (obj instanceof DefaultServicePropertyHandler) {

            DefaultServicePropertyHandler handler = (DefaultServicePropertyHandler) obj;
            ret = handler.getTransitionName(application, service, locale);

        } else if (obj instanceof TextFileServicePropertyHandler) {

            TextFileServicePropertyHandler handler = (TextFileServicePropertyHandler) obj;
            ret = handler.getTransitionName(application, service, locale);

        } else if (obj instanceof XmlServicePropertyHandler) {

            XmlServicePropertyHandler handler = (XmlServicePropertyHandler) obj;
            ret = handler.getTransitionName(application, service, locale);

        }

        return ret;
    }

    /**
     * サービスサーブレットのパスを取得します。
     * 
     * @return サービスサーブレットのパス
     * @throws ServicePropertyException サービスサーブレットのパスの取得時に例外が発生
     */
    public String getServiceServletPath() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getServiceServletPath(getCommonBundle(new Locale("", "")));
    }

    /**
     * コンテキストパスを取得します。
     * 
     * @return コンテキストパス
     * @throws ServicePropertyException コンテキストパスの取得時に例外が発生
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
     * @throws ServicePropertyException アプリケーションIDに該当するパラメータ名の取得時に例外が発生
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
     * @throws ServicePropertyException サービスIDに該当するパラメータ名の取得時に例外が発生
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
     * @throws ServicePropertyException 属性名の取得時に例外が発生
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
     * @throws ServicePropertyException 属性名の取得時に例外が発生
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
     * @throws ServicePropertyException 属性名の取得時に例外が発生
     */
    public String getLocaleAttributeName() throws ServicePropertyException {
        return ResourceBundleServicePropertyHandlerUtil
                .getLocaleAttributeName(getCommonBundle(new Locale("", "")));
    }

}