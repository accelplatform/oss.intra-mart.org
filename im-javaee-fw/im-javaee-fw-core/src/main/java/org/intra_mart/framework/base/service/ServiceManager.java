/*
 * ServiceManager.java
 *
 * Created on 2001/12/17, 14:31
 */

package org.intra_mart.framework.base.service;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.intra_mart.framework.base.service.container.ServiceContainer;
import org.intra_mart.framework.base.service.container.factory.ServiceContainerFactory;
import org.intra_mart.framework.system.exception.IMContainerException;
import org.intra_mart.framework.system.log.LogConstant;
import org.intra_mart.framework.system.log.LogManager;
import org.intra_mart.framework.util.NameUtil;

/**
 * サービスマネージャです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ServiceManager {

    /**
     * サービスフレームワークのログのプレフィックス
     */
    static String LOG_HEAD = "[J2EE][Service]";

    /**
     * サービスプロパティハンドラのキー
     */
    public static final String SERVICE_PROPERTY_HANDLER_KEY = ServiceContainer.SERVICE_PROPERTY_HANDLER_KEY;

    /**
     * 拡張子を指定するキー
     * @since 5.0
     * @see #getExtesion()
     */
    public static final String SERVICE_EXTENSION_KEY = "org.intra_mart.framework.base.service.ServiceManager.extesion";

    /**
     * サービスフレームワークをコールするためのデフォルトの拡張子
     * @since 5.0
     * @see #getExtesion()
     */
    public static final String DEFAULT_SERVICE_EXTENSION = "service";

    /**
     * サービスマネージャ取得フラグ
     */
    private static Boolean managerFlag = new Boolean(false);

    /**
     * サービスマネージャ
     */
    private static ServiceManager manager;

    /**
     * サービスコンテナ
     */
    private ServiceContainer serviceContainer;

    /**
     * サービスマネージャを取得します。
     *
     * @return サービスマネージャ
     * @throws ServiceManagerException サービスマネージャの生成に失敗した
     */
    public static ServiceManager getServiceManager()
        throws ServiceManagerException {
        if (!managerFlag.booleanValue()) {
            synchronized (managerFlag) {
                if (!managerFlag.booleanValue()) {
                    try {
                        manager = new ServiceManager();
                    } catch (ServiceManagerException e) {
                        String message = null;
                        try {
                            message =
                                ResourceBundle
                                    .getBundle("org.intra_mart.framework.base.service.i18n")
                                    .getString("ServiceManager.FailedToCreateManager");
                        } catch (MissingResourceException ex) {
                        }
                        LogManager.getLogManager().getLogAgent().sendMessage(
                            ServiceManager.class.getName(),
                            LogConstant.LEVEL_ERROR,
                            LOG_HEAD + message,
                            e);
                        throw e;
                    }
                    managerFlag = new Boolean(true);
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.service.i18n")
                                .getString("ServiceManager.SuccessedToCreateManager");
                    } catch (MissingResourceException ex) {
                    }
                    LogManager.getLogManager().getLogAgent().sendMessage(
                        ServiceManager.class.getName(),
                        LogConstant.LEVEL_INFO,
                        LOG_HEAD + message);
                }
            }
        }

        return manager;
    }

    /**
     * ServiceManagerを生成するコンストラクタです。
     * このコンストラクタは明示的に呼び出すことはできません。
     *
     * @throws ServiceManagerException サービスマネージャの生成に失敗した
     */
    private ServiceManager() throws ServiceManagerException {
    	try {
			this.serviceContainer = (ServiceContainer)new ServiceContainerFactory().create();
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ServiceManager.SuccessedToCreateContainer");
            } catch (MissingResourceException ignore) {
            }
            LogManager.getLogManager().getLogAgent().sendMessage(
                    ServiceManager.class.getName(),
                    LogConstant.LEVEL_INFO,
                    LOG_HEAD + message + " - " + this.serviceContainer.getClass().getName());
		} catch (IMContainerException e) {
			throw new ServiceManagerException(e.getMessage(), e);
		}
    }

    /**
     * サービスプロパティハンドラを取得します。
     *
     * @return サービスプロパティハンドラ
     */
    public ServicePropertyHandler getServicePropertyHandler() {
        return this.serviceContainer.getServicePropertyHandler();
    }

    /**
     * サービスコントローラを取得します。
     * 該当するサービスコントローラが存在しない場合、nullを返します。
     * このメソッドは{@link #getServiceController(java.lang.String, java.lang.String, java.util.Locale) getServiceController(application, service, null)}を呼んだときと同じ結果を返します。
     *
     * @param application アプリケーション
     * @param service サービス
     * @return サービスコントローラ、存在しない場合はnull
     * @throws ServicePropertyException サービスプロパティの取得時に例外が発生
     * @throws ServiceControllerException サービスコントローラ取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceController(java.lang.String, java.lang.String, java.util.Locale)}を使用してください。
     */
    public ServiceController getServiceController(
        String application,
        String service)
        throws ServicePropertyException, ServiceControllerException {

        return this.serviceContainer.getServiceController(application, service, null);
    }

    /**
     * サービスコントローラを取得します。
     * 指定されたアプリケーションID、サービスID、ロケールに該当するサービスコントローラを取得します。
     * 該当するサービスコントローラが存在しない場合、nullを返します。
     *
     * @param application アプリケーション
     * @param service サービス
     * @param locale ロケール
     * @return サービスコントローラ、存在しない場合はnull
     * @throws ServicePropertyException サービスプロパティの取得時に例外が発生
     * @throws ServiceControllerException サービスコントローラ取得時に例外が発生
     * @since 4.2
     */
    public ServiceController getServiceController(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException, ServiceControllerException {

        return this.serviceContainer.getServiceController(application, service, locale);
    }

    /**
     * トランジションを取得します。
     * 該当するトランジションが存在しない場合、{@link DefaultTransition}を返します。
     * このメソッドは{@link #getTransition(java.lang.String, java.lang.String, java.util.Locale) getTransition(application, service, null)}を呼んだときと同じ結果を返します。
     *
     * @param application アプリケーション
     * @param service サービス
     * @return トランジション
     * @throws ServicePropertyException サービスプロパティの取得時に例外が発生
     * @throws TransitionException トランジション取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getTransition(java.lang.String, java.lang.String, java.util.Locale)}を使用してください。
     */
    public Transition getTransition(String application, String service)
        throws ServicePropertyException, TransitionException {

        return this.serviceContainer.getTransition(application, service, null);
    }

    /**
     * トランジションを取得します。
     * 指定されたアプリケーションID、サービスID、ロケールに該当するトランジションを取得します。
     * 該当するトランジションが存在しない場合、{@link DefaultTransition}を返します。
     *
     * @param application アプリケーション
     * @param service サービス
     * @param locale ロケール
     * @return トランジション
     * @throws ServicePropertyException サービスプロパティの取得時に例外が発生
     * @throws TransitionException トランジション取得時に例外が発生
     * @since 4.2
     */
    public Transition getTransition(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException, TransitionException {

    	Transition transition = this.serviceContainer.getTransition(application, service, locale);

    	// トランジションの各種情報の設定
    	if (transition != null) {
    		transition.setServiceManager(this);
            transition.setApplication(application);
            transition.setService(service);
    	}
        return transition;
    }

    /**
     * 現在のリクエストに対するアプリケーションIDを取得します。
     *
     * @param request リクエスト
     * @param response レスポンス
     * @return 現在のリクエストに対するアプリケーションID
     * @throws ServicePropertyException アプリケーションID取得時に例外が発生
     * @since 4.2
     */
    public String getApplication(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServicePropertyException {
    	
    	String application = null;
    	String baseString;
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
        	if(servletPath.charAt(0) == '/') {
                baseString = servletPath.substring(1, servletPath.lastIndexOf('.'));
        	} else {
                baseString = servletPath.substring(0, servletPath.lastIndexOf('.'));
        	}
        } else {
            baseString = pathInfo;
        }
        String[] infoString = baseString.split(ServiceServlet.REQUEST_SEPARATOR, -1);
        
        if(infoString.length == 0) {
        	return null;
        }
        
		if(NameUtil.isValidName(infoString[0])) {
			application = infoString[0];
		}
		return application;
    }

    /**
     * 現在のリクエストに対するサービスIDを取得します。
     *
     * @param request リクエスト
     * @param response レスポンス
     * @return 現在のリクエストに対するサービスID
     * @throws ServicePropertyException サービスID取得時に例外が発生
     * @since 4.2
     */
    public String getService(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServicePropertyException {
    	
    	String service = null;
    	String baseString;
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
        	if(servletPath.charAt(0) == '/') {
                baseString = servletPath.substring(1, servletPath.lastIndexOf('.'));
        	} else {
                baseString = servletPath.substring(0, servletPath.lastIndexOf('.'));
        	}
        } else {
            baseString = pathInfo;
        }
        
        String[] infoString = baseString.split(ServiceServlet.REQUEST_SEPARATOR, -1);
        if(infoString.length < 2) {
        	return null;
        }
		if(NameUtil.isValidName(infoString[1])) {
			service = infoString[1];
		}
		return service;
    }

    /**
     * 現在のリクエストに対するロケールを取得します。
     * ロケールの取得は以下の優先順位に従います。
     * <OL>
     * <LI>セッション内の{@link ServicePropertyHandler#getLocaleAttributeName()}で取得される属性名で登録されたロケール
     * <LI>{@link ServicePropertyHandler#getClientLocale()}で取得されるロケール
     * <LI>{@link javax.servlet.ServletRequest#getLocale()}で取得されるロケール
     * <LI>{@link java.util.Locale#getDefault()}で取得されるロケール
     * </OL>
     *
     * @param request リクエスト
     * @param response レスポンス
     * @return ロケール
     * @throws ServicePropertyException サービスプロパティの取得時に例外が発生
     * @since 4.2
     */
    public Locale getLocale(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServicePropertyException {

        Locale locale = null;

        // セッション内のロケールを取得
        HttpSession session = request.getSession(false);
        if (session != null) {
            String localeAttribute =
                getServicePropertyHandler().getLocaleAttributeName();
            locale = (Locale)session.getAttribute(localeAttribute);
        }

        // ServicePropertyHandler.getClientLocale()でロケールを取得
        if (locale == null) {
            locale = getServicePropertyHandler().getClientLocale();
        }

        // ServletRequest.getLocale()でロケールを取得
        if (locale == null) {
            locale = request.getLocale();
        }

        // Locale.getDefault()でロケールを取得
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * 現在のリクエストに対するエンコーディングを取得します。
     * エンコーディングの取得は以下の優先順位に従います。
     * <OL>
     * <LI>セッション内の{@link ServicePropertyHandler#getEncodingAttributeName()}で取得される属性名で登録されたエンコーディング
     * <LI>{@link ServicePropertyHandler#getClientEncoding()}で取得されるエンコーディング
     * <LI>{@link javax.servlet.ServletRequest#getCharacterEncoding()}で取得されるエンコーディング
     * <LI>null
     * </OL>
     *
     * @param request リクエスト
     * @param response レスポンス
     * @return エンコーディング
     * @throws ServicePropertyException サービスプロパティの取得時に例外が発生
     * @since 4.2
     */
    public String getEncoding(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServicePropertyException {

        String encoding = null;

        // セッションからエンコーディングを取得
        HttpSession session = request.getSession(false);
        if (session != null) {
            String encodingAttribute =
                getServicePropertyHandler().getEncodingAttributeName();
            encoding = (String)session.getAttribute(encodingAttribute);
        }

        // サービスプロパティハンドラからエンコーディングを取得
        if (encoding == null) {
            encoding = getServicePropertyHandler().getClientEncoding();
        }

        // ServletRequest.getCharacterEncodingでエンコーディングを取得
        if (encoding == null) {
            encoding = request.getCharacterEncoding();
        }

        return encoding;
    }

    /**
     * サービスフレームワークを呼び出すための拡張子を取得します。<BR>
     * {@link #SERVICE_EXTESION_KEY}をキーとしたシステムプロパティを検索し、
     * 存在しない場合は{@link #DEFAULT_SERVICE_EXT}を返却します。
     *
     * @return サービスフレームワークを呼び出すための拡張子 
     * @since 5.0
     */
    public String getExtesion() throws ServiceManagerException {
    	return System.getProperty(SERVICE_EXTENSION_KEY, DEFAULT_SERVICE_EXTENSION);
    }
}
