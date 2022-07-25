package org.intra_mart.framework.extension.seasar.service;

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
import org.intra_mart.framework.base.service.container.ServiceContainer;
import org.intra_mart.framework.extension.seasar.util.ComponentNameCreator;
import org.intra_mart.framework.extension.seasar.util.ComponentUtil;
import org.intra_mart.framework.system.exception.IMContainerException;
import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyManager;
import org.intra_mart.framework.system.property.PropertyManagerException;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * S2ServiceContainer　は　Seasar2　と連携しオブジェクトを生成します。<br>
 * S2ServiceContainerはオブジェクト生成時にS2Containerから取得します。
 * コンポーネントが登録されていない場合コンポーネントを自動的に登録し、
 * 他のコンポーネントとバインドされたオブジェクトを返します。<br>
 * 自動的にS2Containerに登録されるオブジェクトは以下の通りです。<br>
 * <br>
 * ServiceController<br>
 * Transition<br>
 * <br>
 * これらのオブジェクトは全て新規に生成されたものです。
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class S2ServiceContainer implements ServiceContainer {

    /**
     * サービスプロパティハンドラ
     */
    private ServicePropertyHandler servicePropertyHandler;

    private S2Container container;
    
	/**
	 * S2ServiceContainerを生成します。
	 */
	public S2ServiceContainer() {
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
            } catch (MissingResourceException ignore) {
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
            } catch (MissingResourceException ignore) {
            }
            throw new IMContainerException(
                message + " : " + SERVICE_PROPERTY_HANDLER_KEY,
                e);
        }
        
        this.container = SingletonS2ContainerFactory.getContainer();
	}
	
	public ServiceController getServiceController(String application,
			String service, Locale locale) throws ServicePropertyException,
			ServiceControllerException {
        String name = null;
        ServiceController controller = null;
        name =
            this.servicePropertyHandler.getServiceControllerName(
                application,
                service,
                locale);
        if (name == null) {
        	return null;
        }
        try{
        	Class clazz = Class.forName(name);
    		String componentKey = ComponentNameCreator.createControllerName(application, service);
    		ComponentUtil.register(this.container, clazz, componentKey, InstanceDefFactory.PROTOTYPE);
    		controller = (ServiceController)container.getComponent(componentKey);
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
	    return controller;
	}

	public ServicePropertyHandler getServicePropertyHandler() {
		return this.servicePropertyHandler;
	}

	public Transition getTransition(String application, String service, Locale locale) throws ServicePropertyException, TransitionException {
        String name = null;
        Transition transition = null;

        // 指定されたアプリケーションとサービスからトランジションを生成
        name =
            this.servicePropertyHandler.getTransitionName(
                application,
                service,
                locale);
        if (name == null) {
            return new DefaultTransition();
        }
        try {
        	Class clazz = Class.forName(name);
    		String componentKey = ComponentNameCreator.createTransitionName(application, service);
        	ComponentUtil.register(this.container, clazz, componentKey, InstanceDefFactory.PROTOTYPE);
    		transition = (Transition)container.getComponent(componentKey);
        } catch (Exception e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ServiceManager.FailedToCreateTransition");
            } catch (MissingResourceException ignore) {
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
        return transition;
	}
}
