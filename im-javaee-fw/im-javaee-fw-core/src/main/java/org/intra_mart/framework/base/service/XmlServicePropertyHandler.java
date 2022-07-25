/*
 * XmlServicePropertyHandler.java
 *
 * Created on 2002/01/07, 14:15
 */
package org.intra_mart.framework.base.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;
import org.intra_mart.framework.util.XMLDocumentProducer;

import org.xml.sax.SAXException;

/**
 * サービスの設定情報に接続するプロパティハンドラです。 <BR>
 * 
 * XML形式で記述されたプロパティ情報を読み取ります。XMLファイルはアプリケーション毎に分割されます。この場合のファイル名は「 <I>プレフィックス
 * </I>_ <I>アプリケーションID </I>.xml」です。また、アプリケーションに依存しない「 <I>プレフィックス </I>.xml」があります。
 * <BR>
 * XMLファイルのプレフィックスは
 * {@link org.intra_mart.framework.system.property.PropertyManager#getPropertyHandlerParams(String)}
 * でキーに {@link ServiceManager#SERVICE_PROPERTY_HANDLER_KEY}
 * を指定したときに取得されるパラメータのうち {@link #DEFAULT_BUNDLE_NAME}で取得されるものとなります。
 * 指定しなかった場合のプロパティファイルのプレフィックスは {@link #DEFAULT_BUNDLE_NAME_PARAM}で示されるものです。
 * <BR>
 * プロパティの検索順は、アプリケーションごとのプロパティファイル「 <I>プレフィックス </I>_ <I>アプリケーションID
 * </I>.xml」が優先されます。該当するプロパティが見つからない場合、引き続いて「 <I>プレフィックス </I>.xml」を検索します。
 * 
 * @author INTRAMART
 * @since 5.0
 */
public class XmlServicePropertyHandler implements ServicePropertyHandler {

    /**
     * デフォルトのリソースバンドル名のプレフィックス
     */
    public static final String DEFAULT_BUNDLE_NAME = "service-config";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * プロパティファイルの再読み込み可能のパラメータ名
     */
    public static final String PARAM_DYNAMIC = "dynamic";

    /**
     * データリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String xmlPrefix;

    /**
     * 再設定可能フラグ
     */
    private boolean dynamic;

    /**
     * アプリケーションごとのサービスリソース情報の集合
     */
    private Map models = new HashMap();

    /**
     * 共通のサービスリソース情報の集合
     */
    private Map commonServices = new HashMap();

    /**
     * アプリケーションごとのサービス情報が設定されるモデルを作成するオブジェクト
     */
    private ServiceConfigModelProducer producer;

    private final Locale END_LOCALE = new Locale("", "");

    /**
     * XmlServicePropertyHandlerを新規に生成します。
     */
    public XmlServicePropertyHandler() {
        setXMLPrefix(null);
        producer = new ServiceConfigModelProducer();
    }

    /**
     * リソースバンドルのプレフィックスを設定します。
     * 
     * @param xmlPrefix リソースバンドルのプレフィックス
     */
    private void setXMLPrefix(String xmlPrefix) {
        this.xmlPrefix = xmlPrefix;
    }

    /**
     * リソースバンドルのプレフィックスを取得します。
     * 
     * @return リソースバンドルのプレフィックス
     */
    private String getXMLPrefix() {
        return this.xmlPrefix;
    }

    /**
     * アプリケーションごとのリソースモデルの集合を設定します。
     * 
     * @param applicationBundles アプリケーションごとのリソースバンドルの集合
     */
    private void setApplicationModels(Map applicationModels) {
        this.models = applicationModels;
    }

    /**
     * アプリケーションIDとロケールからリソースモデルを設定します。
     * 
     * @param application アプリケーションID
     * @return アプリケーションごとのリソースバンドルの集合
     */
    private void setApplicationSerivceModel(String application,
            ServiceModel model) {

        setApplicationServiceModel(application, model, (Locale) null);
    }

    /**
     * アプリケーションIDとロケールからリソースモデルを設定します。
     * 
     * @param application アプリケーションID
     * @param リソースモデル
     * @param locale ロケール
     * @return アプリケーションごとのリソースバンドルの集合
     */
    private void setApplicationServiceModel(String application,
            ServiceModel model, Locale locale) {

        Map appMap = (Map) this.models.get(locale);
        if (appMap == null) {
            appMap = new HashMap();
            this.models.put(locale, appMap);
        }
        appMap.put(application, model);
    }

    /**
     * アプリケーションごとのリソースモデルの集合を取得します。
     * 
     * @return アプリケーションごとのリソースバンドルの集合
     */
    private Map getModels() {
        return this.models;
    }

    /**
     * 共通のリソースモデルを設定します。
     * 
     * @param commonModel 共通のリソースバンドル
     */
    private void setCommonServiceModel(Locale key,
            CommonServiceModel commonService) {
        commonServices.put(key, commonService);
    }

    /**
     * 共通のリソースモデルを取得します。
     * 
     * @return 共通のリソースバンドル
     * @throws ServicePropertyException 共通のリソースバンドル取得時に例外が発生
     */
    private CommonServiceModel getCommonServiceModel()
            throws ServicePropertyException {
        return getCommonServiceModel(new Locale("", ""));
    }

    /**
     * 共通のリソースモデルを取得します。
     * 
     * @param locale ロケール
     * @return 共通のリソースバンドル
     * @throws ServicePropertyException 共通のリソースバンドル取得時に例外が発生
     */
    private CommonServiceModel getCommonServiceModel(Locale locale)
            throws ServicePropertyException {
        synchronized (commonServices) {
        	CommonServiceModel model = _getCommonServiceModel(locale);
        	if(model == null) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("Xml.FileNotFound");
                } catch (MissingResourceException ex) {
                }
                throw new ServicePropertyException(message);
        	}
            return model;
        }
    }

    /**
     * 共通のリソースモデルを取得します。
     * 
     * @param locale ロケール
     * @return 共通のリソースバンドル
     * @throws ServicePropertyException 共通のリソースバンドル取得時に例外が発生
     */
    private CommonServiceModel _getCommonServiceModel(Locale locale)
            throws ServicePropertyException {
        CommonServiceModel commonService = null;
        if(!isDynamic()) {
            commonService = (CommonServiceModel) commonServices.get(locale);
        }
        if (commonService == null) {
            if (checkCommonServiceFileExist(locale)) {
                commonService = createCommonServiceModel(locale);

                // 多言語対応
                if (!locale.equals(END_LOCALE)) {
                    Locale parentLocale = getParentLocale(locale);
                    CommonServiceModel parent = _getCommonServiceModel(parentLocale);
                    commonService.setParent(parent);
                }
                commonServices.put(locale, commonService);
            } else {
                if (!locale.equals(END_LOCALE)) {
                    Locale parentLocale = getParentLocale(locale);
                    commonService = _getCommonServiceModel(parentLocale);
                } else {
                    // ロケールに情報がnew Locale("","");の状態の時
                    return null;
                }
            }
        }

        return commonService;
    }

    /**
     * 指定されたロケールの共通リソースファイルが存在するか調べる。
     * 
     * @param locale
     */
    private boolean checkCommonServiceFileExist(Locale locale) {
        return XMLDocumentProducer.isFileExist(getXMLPrefix(), locale);
    }

    /**
     * アプリケーションIDで指定されたリソースモデルを生成します。
     * 
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースモデル
     * @throws ServicePropertyException リソースモデルの取得時に例外が発生
     */
    private CommonServiceModel createCommonServiceModel(Locale locale)
            throws ServicePropertyException {
        try {
            CommonServiceModelProducer producer = new CommonServiceModelProducer();
            String fileName = XMLDocumentProducer.getFileName(getXMLPrefix(),
                    locale);
            CommonServiceModel commonModel = producer
                    .createCommonServiceModel(fileName);

            return commonModel;
        } catch (ParserConfigurationException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        } catch (SAXException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        }
    }

    /**
     * アプリケーションIDとロケールからリソースモデルを取得します。
     * 
     * @param application アプリケーションID
     * @return アプリケーションごとのリソースバンドルの集合
     */
    private ServiceConfigModel getSerivceConfigModel(String application)
            throws ServicePropertyException {
        return getServiceConfigModel(application, new Locale("", ""));
    }

    /**
     * アプリケーションIDで指定されたリソースモデルを取得します。
     * 
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
     */
    private ServiceConfigModel getServiceConfigModel(String application)
            throws ServicePropertyException {
        return getServiceConfigModel(application, new Locale("", ""));
    }

    /**
     * アプリケーションIDで指定されたリソースモデルを取得します。
     * 
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
     */
    private ServiceConfigModel getServiceConfigModel(String application,
            Locale locale) throws ServicePropertyException {

        synchronized (this.models) {
        	ServiceConfigModel model = _getServiceConfigModel(application, locale);
            return model;
        }
    }

    /**
     * 
     * @param application
     * @param locale
     * @return
     */
    private ServiceConfigModel _getServiceConfigModel(String application,
            Locale locale) throws ServicePropertyException {

        ServiceConfigModel result = null;
        HashMap appMaps = (HashMap) models.get(locale);
        if (appMaps == null) {
            appMaps = new HashMap();
            models.put(locale, appMaps);
        }
        
        if(!isDynamic()) {
        	result = (ServiceConfigModel) appMaps.get(application);
        }

        if (result == null) {
            // ファイルがある
            if (checkServiceFile(application, locale)) {
                result = createSerivceConfigModel(application, locale);
                //多言語対応
                if (locale == null || !locale.equals(END_LOCALE)) {
                    locale = getParentLocale(locale);
                    ServiceConfigModel parent = _getServiceConfigModel(
                            application, locale);
                    result.setParent(parent);
                }
                appMaps.put(application, result);
            } else {
                // ファイルがない
                if (locale == null || !locale.equals(END_LOCALE)) {
                    locale = getParentLocale(locale);
                    result = _getServiceConfigModel(application, locale);
                } else {
                    return null;
                }
            }
        }
        return result;
    }

    private boolean checkServiceFile(String application, Locale locale) {

        String filename = getPropertyPackage(application) + getXMLPrefix()
                + "-" + getApplicationID(application);
        return XMLDocumentProducer.isFileExist(filename, locale);
    }

    /**
     * アプリケーションIDで指定されたリソースモデルを生成します。
     * 
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDに該当するリソースモデル
     * @throws ServicePropertyException リソースバンドルの取得時に例外が発生
     */
    private ServiceConfigModel createSerivceConfigModel(String application,
            Locale locale) throws ServicePropertyException {
        ServiceConfigModel model = null;

        String fn = getPropertyPackage(application)
				+ getXMLPrefix() + "-"
                + getApplicationID(application);
        String fileName = XMLDocumentProducer.getFileName(fn, locale);

        ServiceConfigModelProducer producer = new ServiceConfigModelProducer();
        try {
            model = producer.createServiceConfigModel(fileName);
        } catch (ParserConfigurationException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        } catch (SAXException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ServicePropertyException(e.getMessage(), e);
        }

        return model;
    }

    /**
     * プロパティハンドラを初期化します。
     * 
     * @param params 初期パラメータ
     * @throws PropertyHandlerException プロパティハンドラの初期化時に例外が発生
     */
    public void init(PropertyParam[] params) throws PropertyHandlerException {
        String bundleName = null;
        String dynamic = null;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].getName().equals(DEFAULT_BUNDLE_NAME_PARAM)) {
                    bundleName = params[i].getValue();
                } else if (params[i].getName().equals(PARAM_DYNAMIC)) {
                    // 再設定可能フラグの場合
                    dynamic = params[i].getValue();
                }
            }
        }
        if (bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        }
        
        // プレフィックスの設定
        setXMLPrefix(bundleName);
        
        // 再設定可能フラグの設定
        Boolean dummyDynamic = new Boolean(dynamic);
        setDynamic(dummyDynamic.booleanValue());
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
     * プロパティの動的読み込みが可能かどうか調べます。 このクラスではこのメソッドは常にfalseを返します。
     * 
     * @return 常にfalse
     * @throws ServicePropertyException チェック時に例外が発生
     */
    public boolean isDynamic() throws ServicePropertyException {
        return this.dynamic;
    }

    /**
     * クライアントのエンコードを取得します。
     * 
     * @return クライアントのエンコーディング
     * @throws ServicePropertyException クライアントのエンコードの取得時に例外が発生
     */
    public String getClientEncoding() throws ServicePropertyException {
        CommonServiceModel model = getCommonServiceModel();
        String encoding = model.getClientEncoding();

        return encoding;
    }

    /**
     * クライアントのエンコードを取得します。
     * 
     * @return クライアントのエンコーディング
     * @throws ServicePropertyException クライアントのエンコードの取得時に例外が発生
     */
    public Locale getClientLocale() throws ServicePropertyException {
        CommonServiceModel model = getCommonServiceModel();
        String localeString = model.getClientLocale();
        Locale locale = null;

        if (localeString != null) {
            locale = getRealLocale(localeString);
        }
        return locale;
    }

    /**
     * 
     * @param localeString
     * @return
     */
    private static Locale getRealLocale(String localeString) {
        StringTokenizer tokenizer = new StringTokenizer(localeString, "-_");
        if (tokenizer.countTokens() == 2) {
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
                message = ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.web.tag.i18n")
                        .getString("MessageTag.LocaleStringIncorrect");
            } catch (MissingResourceException e) {
            }
            throw new IllegalArgumentException(message + " : \"" + localeString
                    + "\"");
        }
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
        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {
            result = serviceConfig.getInputErrorPagePath(service, key);
        }
        if (result == null) {
            try {
                result = getInputErrorPagePath(application, service);
            } catch (ServicePropertyException e) {
            }
        }
        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service + " : key = " + key;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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
        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getInputErrorPagePath(service, key);
        }

        if (result == null) {
            try {
                result = getInputErrorPagePath(application, service, locale);
            } catch (ServicePropertyException e) {
            }
        }

        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service + " : key = " + key;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);
        if (serviceConfig != null) {
            result = serviceConfig.getInputErrorPagePath(service);
        }

        if (result == null) {
            try {
                result = getInputErrorPagePath(application);
            } catch (ServicePropertyException e) {

            }
        }
        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service;

            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getInputErrorPagePath(service);
        }

        if (result == null) {
            try {
                result = getInputErrorPagePath(application, locale);
            } catch (ServicePropertyException e) {
            }
        }
        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {

            result = serviceConfig.getInputErrorPagePath();
        }

        if (result == null) {
            try {
                result = getInputErrorPagePath();
            } catch (ServicePropertyException e) {
            }
        }
        if (result == null) {
            String idMessage = " : application = " + application;

            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        result = serviceConfig.getInputErrorPagePath();

        if (result == null) {
            try {
                result = getInputErrorPagePath(locale);
            } catch (ServicePropertyException e) {

            }
        }

        if (result == null) {
            String idMessage = " : application = " + application;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        CommonServiceModel commonService = getCommonServiceModel();
        String result = null;
        if (commonService != null) {
            result = commonService.getInputErrorPagePath();
        }

        if (result == null) {

            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");

            throw new ServicePropertyException(message);
        }
        return result;
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
        String result = null;

        CommonServiceModel commonService = getCommonServiceModel(locale);
        if (commonService != null) {
            result = commonService.getInputErrorPagePath();
        }

        if (result == null) {
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");
            throw new ServicePropertyException(message);
        }
        return result;

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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {
            result = serviceConfig.getNextPagePath(service);
        }

        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetNextPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application, locale);
        
        if (serviceConfig != null) {
            result = serviceConfig.getNextPagePath(service);
        }

        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetNextPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application);
        if (serviceConfig != null) {
            result = serviceConfig.getNextPagePath(service, key);
        }
        if (result == null) {
            try {
                result = getNextPagePath(application, service);
            } catch (ServicePropertyException e) {

            }
        }

        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service + " : key = " + key;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetNextPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getNextPagePath(service, key);
        }

        if (result == null) {
            try {
                result = getNextPagePath(application, service, locale);
            } catch (ServicePropertyException e) {

            }
        }

        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service + " : key = " + key;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetNextPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {
            result = serviceConfig.getServiceControllerClassName(service);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getServiceControllerClassName(service);
        }

        return result;

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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {
            result = serviceConfig.getServiceErrorPagePath(service, key);
        }
        if (result == null) {
            try {
                result = getServiceErrorPagePath(application, service);
            } catch (ServicePropertyException e) {

            }
        }
        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service + " : key = " + key;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getServiceErrorPagePath(service, key);
        }

        if (result == null) {
            try {
                result = getServiceErrorPagePath(application, service, locale);
            } catch (ServicePropertyException e) {
            }
        }

        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service + " : key = " + key;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {
            result = serviceConfig.getServiceErrorPagePath(service);
        }
        if (result == null) {
            try {
                result = getServiceErrorPagePath(application);
            } catch (ServicePropertyException e) {
            }
        }
        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getServiceErrorPagePath(service);
        }

        if (result == null) {
            try {
                result = getServiceErrorPagePath(application, locale);
            } catch (ServicePropertyException e) {
            }
        }

        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {
            result = serviceConfig.getServiceErrorPagePath();
        }
        if (result == null) {
            try {
                result = getServiceErrorPagePath();
            } catch (ServicePropertyException e) {
            }
        }
        if (result == null) {
            String idMessage = " : application = " + application;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getServiceErrorPagePath();
        }

        if (result == null) {
            try {
                result = getServiceErrorPagePath(locale);
            } catch (ServicePropertyException e) {
            }
        }

        if (result == null) {
            String idMessage = " : application = " + application;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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
        String result = null;

        CommonServiceModel commonService = getCommonServiceModel();
        result = commonService.getServiceErrorPagePath();
        if (result == null) {

            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message);
        }
        return result;
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

        String result = null;

        CommonServiceModel commonService = getCommonServiceModel(locale);
        result = commonService.getServiceErrorPagePath();

        if (result == null) {

            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message);
        }
        return result;
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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {
            result = serviceConfig.getSystemErrorPagePath(service, key);
        }
        if (result == null) {
            try {
                result = getSystemErrorPagePath(application, service);
            } catch (ServicePropertyException e) {
            }
        }
        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service + " : key = " + key;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getSystemErrorPagePath(service, key);
        }

        if (result == null) {
            try {
                result = getSystemErrorPagePath(application, service, locale);
            } catch (ServicePropertyException e) {

            }
        }
        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service + " : key = " + key;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;

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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {
            result = serviceConfig.getSystemErrorPagePath(service);
        }
        if (result == null) {
            try {
                result = getSystemErrorPagePath(application);
            } catch (ServicePropertyException e) {
            }
        }
        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getSystemErrorPagePath(service);
        }

        if (result == null) {
            try {
                result = getSystemErrorPagePath(application, locale);
            } catch (ServicePropertyException e) {
            }
        }

        if (result == null) {
            String idMessage = " : application = " + application
                    + " : service = " + service;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application);

        if (serviceConfig != null) {
            result = serviceConfig.getSystemErrorPagePath();
        }
        if (result == null) {
            try {
                result = getSystemErrorPagePath();
            } catch (ServicePropertyException e) {

            }
        }
        if (result == null) {
            String idMessage = " : application = " + application;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getSystemErrorPagePath();
        }

        if (result == null) {
            try {
                result = getSystemErrorPagePath(locale);
            } catch (ServicePropertyException e) {

            }
        }

        if (result == null) {
            String idMessage = " : application = " + application;
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");

            throw new ServicePropertyException(message + idMessage);
        }
        return result;
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
        String result = null;

        CommonServiceModel commonService = getCommonServiceModel();
        result = commonService.getSystemErrorPagePath();
        if (result == null) {

            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetSystemErrorPagePath");

            throw new ServicePropertyException(message);
        }
        return result;
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
        String result = null;

        CommonServiceModel commonService = getCommonServiceModel(locale);
        if (commonService != null) {
            result = commonService.getSystemErrorPagePath();
        }

        if (result == null) {

            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetSystemErrorPagePath");

            throw new ServicePropertyException(message);
        }
        return result;
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

        String result = null;

        ServiceConfigModel serviceConfig = getServiceConfigModel(application);
        if (serviceConfig != null) {
            result = serviceConfig.getTransitionName(service);
        }
        return result;
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

        String result = null;
        ServiceConfigModel serviceConfig = getServiceConfigModel(application,
                locale);
        if (serviceConfig != null) {
            result = serviceConfig.getTransitionName(service);
        }

        return result;
    }

    /**
     * サービスサーブレットのパスを取得します。
     * 
     * @return サービスサーブレットのパス
     * @throws ServicePropertyException サービスサーブレットのパスの取得時に例外が発生
     */
    public String getServiceServletPath() throws ServicePropertyException {
        CommonServiceModel model = getCommonServiceModel();
        String path = null;
        if (model != null) {
            path = model.getServiceServletPath();
        }

        return path;

    }

    /**
     * コンテキストパスを取得します。
     * 
     * @return コンテキストパス
     * @throws ServicePropertyException コンテキストパスの取得時に例外が発生
     * @deprecated このメソッドではなく、javax.servlet.http.HttpRequestのgetContextPathを使用するようにしてください。
     */
    public String getContextPath() throws ServicePropertyException {
        CommonServiceModel commonModel = getCommonServiceModel();
        String path = null;
        if (commonModel != null) {
            path = commonModel.getContextPath();
        }

        return path;
    }

    /**
     * アプリケーションIDに該当するパラメータ名を取得します。 設定されていない場合、
     * {@link ServicePropertyHandler#DEFAULT_APPLICATION_PARAMETER}
     * で定義されている値を返します。
     * 
     * @return アプリケーションIDに該当するパラメータ名
     * @throws ServicePropertyException アプリケーションIDに該当するパラメータ名の取得時に例外が発生
     * @deprecated この実装は廃止されました。
     */
    public String getApplicationParamName() throws ServicePropertyException {
        CommonServiceModel commonModel = getCommonServiceModel();
        String paramName = null;

        if (commonModel != null) {
            paramName = commonModel.getApplicationParamName();
        }

        if (paramName == null) {
            paramName = ServicePropertyHandler.DEFAULT_APPLICATION_PARAMETER;
        }

        return paramName;
    }

    /**
     * サービスIDに該当するパラメータ名を取得します。 設定されていない場合、
     * {@link ServicePropertyHandler#DEFAULT_SERVICE_PARAMETER}で定義されている値を返します。
     * 
     * @return サービスIDに該当するパラメータ名
     * @throws ServicePropertyException サービスIDに該当するパラメータ名の取得時に例外が発生
     * @deprecated この実装は廃止されました。
     */
    public String getServiceParamName() throws ServicePropertyException {
        CommonServiceModel commonModel = getCommonServiceModel();
        String paramName = null;

        if (commonModel != null) {
            paramName = commonModel.getServiceParamName();
        }

        if (paramName == null) {
            paramName = ServicePropertyHandler.DEFAULT_SERVICE_PARAMETER;
        }

        return paramName;
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
        CommonServiceModel commonModel = getCommonServiceModel();
        String attrName = null;

        if (commonModel != null) {
            attrName = commonModel.getExceptionAttributeName();
        }

        if (attrName == null) {
            attrName = ServicePropertyHandler.DEFAULT_EXCEPTION_ATTRIBUTE;
        }

        return attrName;

    }

    /**
     * 
     * @param params
     * @throws ServicePropertyException
     */
    private void checkCacheParams(PropertyParam[] params)
            throws ServicePropertyException {
        if (!checkParams(params)) {
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetCache");
            throw new ServicePropertyException(message);
        }

    }

    /**
     * 
     * @param params
     * @throws ServicePropertyException
     */
    private void checkConditionParams(PropertyParam[] params)
            throws ServicePropertyException {
        if (!checkParams(params)) {
            String message = ServiceResourceMessage
                    .getResourceString("ResourceBundleServicePropertyHandlerUtil.FailedToGetCacheCondition");
            throw new ServicePropertyException(message);
        }

    }

    /**
     * 
     * @param params
     * @return
     */
    private boolean checkParams(PropertyParam[] params) {
        for (int i = 0; i < params.length; i++) {
            if (!checkParam(params[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * パラメータの整合性チェック
     * 
     * @param param
     * @return
     */
    private boolean checkParam(PropertyParam param) {

        if (param.getName() == null || param.getName().trim().length() == 0) {
            return false;
        }
        if (param.getValue() == null || param.getName().trim().length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * ログインユーザが使用するエンコードを保存しておくときの属性名を取得します。 設定されていない場合、
     * {@link #DEFAULT_ENCODING_ATTRIBUTE}で定義されている値を返します。
     * 
     * @return エンコードの属性名
     * @throws ServicePropertyException 属性名の取得時に例外が発生
     */
    public String getEncodingAttributeName() throws ServicePropertyException {
        CommonServiceModel commonModel = getCommonServiceModel();
        String attrName = null;

        if (commonModel != null) {
            attrName = commonModel.getEncodingAttributeName();
        }

        if (attrName == null) {
            attrName = ServicePropertyHandler.DEFAULT_ENCODING_ATTRIBUTE;
        }

        return attrName;
    }

    /**
     * ログインユーザが使用するロケールを保存しておくときの属性名を取得します。 設定されていない場合、
     * {@link #DEFAULT_LOCALE_ATTRIBUTE }で定義されている値を返します 。
     * 
     * @return ロケールの属性名
     * @throws ServicePropertyException 属性名の取得時に例外が発生
     */
    public String getLocaleAttributeName() throws ServicePropertyException {
        CommonServiceModel commonModel = getCommonServiceModel();
        String attrName = commonModel.getLocaleAttributeName();
        if (attrName == null) {
            return ServicePropertyHandler.DEFAULT_LOCALE_ATTRIBUTE;
        }

        return attrName;
    }

    /**
     * Returns the next locale up the parent hierarchy. E.g. the parent of new
     * Locale("en","us","mac") would be new Locale("en", "us", "").
     * 
     * @param locale the locale
     * @return the parent locale
     */
    Locale getParentLocale(Locale locale) {
        Locale newloc;
        if (locale == null) {
            newloc = new Locale("", "", "");
        } else if (locale.getVariant().equals("")) {
            if (locale.getCountry().equals("")) {
                newloc = new Locale("", "", "");
            } else {
                newloc = new Locale(locale.getLanguage(), "", "");
            }
        } else {
            newloc = new Locale(locale.getLanguage(), locale.getCountry(), "");
        }

        return newloc;
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
}