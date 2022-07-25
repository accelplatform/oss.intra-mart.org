package org.intra_mart.framework.base.service.container;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.service.DefaultTransition;
import org.intra_mart.framework.base.service.ServiceController;
import org.intra_mart.framework.base.service.ServiceControllerException;
import org.intra_mart.framework.base.service.ServicePropertyException;
import org.intra_mart.framework.base.service.ServicePropertyHandler;
import org.intra_mart.framework.base.service.Transition;
import org.intra_mart.framework.base.service.TransitionException;
import org.intra_mart.framework.system.exception.IMContainerException;
import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyManager;
import org.intra_mart.framework.system.property.PropertyManagerException;

/**
 * ServiceContainerの標準的な実装クラスです。
 * 
 * @author INTRAMART
 * @6.0
 */
public class ServiceContainerImpl implements ServiceContainer {

    /**
     * サービスプロパティハンドラ
     */
    private ServicePropertyHandler servicePropertyHandler;

	/**
	 * ServiceContainerImplを生成します。
	 */
	public ServiceContainerImpl() {
	}
	
	/**
	 * ServiceContainerを初期化します。
	 * 
	 * @throws IMContainerException
	 */
	public void init () throws IMContainerException {
        PropertyManager propertyManager;

        // プロパティマネージャの取得
        try {
            propertyManager = PropertyManager.getPropertyManager();
        } catch (PropertyManagerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ServiceManager.FailedToGetPropertyManager");
            } catch (MissingResourceException ex) {
            }
            throw new IMContainerException(message, e);
        }

        // サービスプロパティハンドラの取得
        try {
            this.servicePropertyHandler =
                (ServicePropertyHandler)propertyManager.getPropertyHandler(
                    SERVICE_PROPERTY_HANDLER_KEY);
        } catch (PropertyHandlerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ServiceManager.FailedToGetServicePropertyHandler");
            } catch (MissingResourceException ex) {
            }
            throw new IMContainerException(
                message + " : " + SERVICE_PROPERTY_HANDLER_KEY,
                e);
        }
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
     */
	public ServiceController getServiceController(String application, String service, Locale locale) throws ServicePropertyException, ServiceControllerException {
        String name = null;
        Object controllerObject = null;
        ServiceController controller = null;

        // 指定されたアプリケーションとサービスからサービスコントローラを生成
        name =
            this.servicePropertyHandler.getServiceControllerName(
                application,
                service,
                locale);
        if (name == null) {
            controller = null;
        } else {
            try {
                controllerObject = Class.forName(name).newInstance();
            } catch (Exception e) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ServiceManager.FailedToCreateController");
                } catch (MissingResourceException ex) {
                }
                throw new ServiceControllerException(
                    message
                        + " : controller class = "
                        + name
                        + ", application = "
                        + application
                        + ", service = "
                        + service
                        + ", locale = "
                        + locale,
                    e);
            }
            if (controllerObject instanceof ServiceController) {
                controller = (ServiceController)controllerObject;
            } else {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ServiceManager.ControllerImplemented");
                } catch (MissingResourceException ex) {
                }
                throw new ServiceControllerException(
                    message
                        + " : controller class = "
                        + name
                        + ", application = "
                        + application
                        + ", service = "
                        + service
                        + ", locale = "
                        + locale);
            }
        }
        return controller;
	}

    /**
     * サービスプロパティハンドラを取得します。
     *
     * @return サービスプロパティハンドラ
     */
	public ServicePropertyHandler getServicePropertyHandler() {
		return this.servicePropertyHandler;
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
     */
	public Transition getTransition(String application, String service, Locale locale) throws ServicePropertyException, TransitionException {
        String name = null;
        Object transitionObject = null;
        Transition transition = null;

        // 指定されたアプリケーションとサービスからトランジションを生成
        name =
            this.servicePropertyHandler.getTransitionName(
                application,
                service,
                locale);
        if (name == null) {
            transition = new DefaultTransition();
        } else {
            try {
                transitionObject = Class.forName(name).newInstance();
            } catch (Exception e) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ServiceManager.FailedToCreateTransition");
                } catch (MissingResourceException ex) {
                }
                throw new TransitionException(
                    message
                        + " : transition class = "
                        + name
                        + ", application = "
                        + application
                        + ", service = "
                        + service
                        + ", locale = "
                        + locale,
                    e);
            }
            if (transitionObject instanceof Transition) {
                transition = (Transition)transitionObject;
            } else {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ServiceManager.TransitionExtended");
                } catch (MissingResourceException ex) {
                }
                throw new TransitionException(
                    message
                        + " : transition class = "
                        + name
                        + ", application = "
                        + application
                        + ", service = "
                        + service
                        + ", locale = "
                        + locale);
            }
        }

        return transition;
	}
}
