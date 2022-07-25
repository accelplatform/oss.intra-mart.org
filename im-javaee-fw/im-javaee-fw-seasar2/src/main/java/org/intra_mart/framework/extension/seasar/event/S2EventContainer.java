package org.intra_mart.framework.extension.seasar.event;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.event.EmptyEvent;
import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventException;
import org.intra_mart.framework.base.event.EventListener;
import org.intra_mart.framework.base.event.EventListenerException;
import org.intra_mart.framework.base.event.EventListenerFactory;
import org.intra_mart.framework.base.event.EventListenerFactoryParam;
import org.intra_mart.framework.base.event.EventPropertyException;
import org.intra_mart.framework.base.event.EventPropertyHandler;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.base.event.container.EventContainer;
import org.intra_mart.framework.extension.seasar.util.ComponentNameCreator;
import org.intra_mart.framework.extension.seasar.util.ComponentUtil;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.IMContainerException;
import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyManager;
import org.intra_mart.framework.system.property.PropertyManagerException;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * S2EventContainer　は　Seasar2　と連携しオブジェクトを生成します。<br>
 * S2EventContainerは要求されたEventオブジェクトをS2Containerから取得し、未登録のEventオブジェクトは自動的に登録します。<br>
 * このときS2Containerから返されるEventオブジェクトは全て新規に生成されたものです。
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class S2EventContainer implements EventContainer {

    /**
     * イベントプロパティハンドラ
     */
    private EventPropertyHandler eventPropertyHandler;

    /**
     * イベントリスナファクトリの集合
     */
    private Map factories;

    private S2Container container;

	public Event createEvent(String application, String key)
			throws EventPropertyException, EventException {
        String name = null;
        Event event = null;

        // 指定されたアプリケーションとキーからイベントを生成
        name = this.eventPropertyHandler.getEventName(application, key);
        if (name == null) {
            return new EmptyEvent();
        }
        try {
        	Class clazz = Class.forName(name);
        	String componentKey = ComponentNameCreator.createEventName(application, key);
        	ComponentUtil.register(this.container, clazz, componentKey, InstanceDefFactory.PROTOTYPE);
            event = (Event)container.getComponent(componentKey);
        } catch (Exception e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("EventManager.FailedToCreateEvent");
            } catch (MissingResourceException ex) {
            }
            throw new EventException(
                message
                    + " : event class = "
                    + name
                    + ", application = "
                    + application
                    + ", key = "
                    + key,
                e);
        }

        return event;
	}

    public EventResult dispatch(Event event, boolean transaction) throws EventException, SystemException, ApplicationException {
	    EventListenerFactory factory;
	    EventListener listener;
	
	    // イベントリスナファクトリの取得
	    factory = getEventListenerFactory(event);
	
	    // イベントリスナの生成
	    listener = factory.create(event);
	
	    // トランザクションの設定
	    listener.setInTransaction(transaction);
	
	    // 処理実行・結果の取得
	    return listener.execute(event);
	}

	public EventPropertyHandler getEventPropertyHandler() {
		return this.eventPropertyHandler;
	}

	public void init() throws IMContainerException {
    	PropertyManager propertyManager;

        // プロパティマネージャの取得
        try {
            propertyManager = PropertyManager.getPropertyManager();
        } catch (PropertyManagerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("EventManager.FailedToGetPropertyManager");
            } catch (MissingResourceException ex) {
            }
            throw new IMContainerException(message, e);
        }

        // イベントプロパティハンドラの取得
        try {
            this.eventPropertyHandler =
                (EventPropertyHandler)propertyManager.getPropertyHandler(
                    EVENT_PROPERTY_HANDLER_KEY);
        } catch (PropertyHandlerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("EventManager.FailedToGetEventPropertyHandler");
            } catch (MissingResourceException ex) {
            }
            throw new IMContainerException(
                message + " : " + EVENT_PROPERTY_HANDLER_KEY,
                e);
        }

        // イベントリスナファクトリの集合の初期化
        this.factories = new HashMap();

        this.container = SingletonS2ContainerFactory.getContainer();
	}

    /**
     * 指定されたイベントに該当するイベントリスナファクトリを取得します。
     * 通常はこのメソッドを使用する必要はありません。{@link #createEvent(String, String, String)}と{@link #dispatch(org.intra_mart.framework.base.event.Event)}を利用してください。
     *
     * @param event イベント
     * @return イベントリスナファクトリ
     * @throws EventException 不正なイベントが渡された
     */
    private EventListenerFactory getEventListenerFactory(Event event)
        throws EventException {
        EventListenerFactory factory = null;

        // イベントが適正なものかどうかチェック
        String eventApp = event.getApplication();
        String eventKey = event.getKey();
        if (eventApp == null
            || eventApp.equals("")
            || eventKey == null
            || eventKey.equals("")) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("Common.IllegalEvent");
            } catch (MissingResourceException e) {
            }
            throw new EventException(
                message
                    + " : event class = "
                    + event.getClass().getName()
                    + ", application = "
                    + eventApp
                    + ", key = "
                    + eventKey);
        }

        String factoryKey = event.getApplication() + "." + event.getKey();
        boolean dynamic = false;
        try {
            dynamic = getEventPropertyHandler().isDynamic();
        } catch (EventPropertyException e) {
            throw new EventException(e.getMessage(), e);
        }
        if (dynamic) {
            factory = createEventListenerFactory(event);
        } else {
            synchronized (this.factories) {
                factory = (EventListenerFactory)this.factories.get(factoryKey);
                if (factory == null) {
                    factory = createEventListenerFactory(event);
                    this.factories.put(factoryKey, factory);
                }
            }
        }

        return factory;
    }

    private EventListenerFactory createEventListenerFactory(Event event)
        throws EventException {
        String factoryName = null;
        Object factoryObject = null;
        EventListenerFactory factory = null;
        EventListenerFactoryParam[] params = null;

        // イベントリスナファクトリの生成
        try {
            factoryName =
                getEventPropertyHandler().getEventListenerFactoryName(
                    event.getApplication(),
                    event.getKey());
        } catch (EventPropertyException e) {
            throw new EventException(e.getMessage(), e);
        }
        try {
            factoryObject = Class.forName(factoryName).newInstance();
        } catch (Exception e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("EventManager.FailedToCreateFactory");
            } catch (MissingResourceException ex) {
            }
            throw new EventException(
                message
                    + " : factory class = "
                    + factoryName
                    + ", application = "
                    + event.getApplication()
                    + ", key = "
                    + event.getKey(),
                e);
        }
        if (factoryObject instanceof EventListenerFactory) {
            factory = (EventListenerFactory)factoryObject;
        } else {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("EventManager.FactoryImplemented");
            } catch (MissingResourceException e) {
            }
            throw new EventException(
                message
                    + " : factory class = "
                    + factoryName
                    + ", application = "
                    + event.getApplication()
                    + ", key = "
                    + event.getKey());
        }

        // イベントリスナファクトリの初期パラメータ設定
        try {
            params =
                getEventPropertyHandler().getEventListenerFactoryParams(
                    event.getApplication(),
                    event.getKey());
        } catch (EventPropertyException e) {
            throw new EventException(e.getMessage(), e);
        }
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                try {
                    factory.initParam(
                        params[i].getName(),
                        params[i].getValue());
                } catch (EventListenerException e) {
                    throw new EventException(e.getMessage(), e);
                }
            }
        }

        return factory;
    }
}
