package org.intra_mart.framework.base.event.container;

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
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.IMContainerException;
import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyManager;
import org.intra_mart.framework.system.property.PropertyManagerException;

/**
 * EventContainerの標準的な実装クラスです。
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class EventContainerImpl implements EventContainer {

    /**
     * イベントプロパティハンドラ
     */
    private EventPropertyHandler eventPropertyHandler;

    /**
     * イベントリスナファクトリの集合
     */
    private Map factories;

    /**
	 * EventContainerImplを生成します。
	 */
	public EventContainerImpl() {
	}

	/**
	 * EventContainerを初期化します。
	 * 
	 * @throws IMContainerException
	 */
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
	}
	
    /**
     * イベントプロパティハンドラを取得します。
     *
     * @return イベントプロパティハンドラ
     */
	public EventPropertyHandler getEventPropertyHandler() {
        return this.eventPropertyHandler;
	}

    /**
     * パラメータで指定された内容に該当するイベントを生成します。
     * 該当するイベントが{@link EventPropertyHandler}のパラメータで設定されていない場合、{@link EmptyEvent}を返します。
     * 
     * @param application アプリケーション名
     * @param key キー
     * @return パラメータに該当するイベント
     * @throws EventException イベント生成に失敗
     * @throws EventPropertyException イベントプロパティの取得に失敗
     * @since 6.0
     */
    public Event createEvent(
            String application,
            String key)
            throws EventPropertyException, EventException {
        String name = null;
        Object eventObject = null;
        Event event = null;

        // 指定されたアプリケーションとキーからイベントを生成
        name = this.eventPropertyHandler.getEventName(application, key);
        if (name == null) {
            event = new EmptyEvent();
        } else {
            try {
                eventObject = Class.forName(name).newInstance();
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
            if (eventObject instanceof Event) {
                event = (Event)eventObject;
            } else {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.event.i18n")
                            .getString("EventManager.EventExtended");
                } catch (MissingResourceException ex) {
                }
                throw new EventException(
                    message
                        + " : event class = "
                        + name
                        + ", application = "
                        + application
                        + ", key = "
                        + key);
            }
        }
        return event;
    }

    /**
     * イベントに対する処理を実行します。
     *
     * @param event イベント
     * @param transaction トランザクションの中で実行されているかどうかを示すフラグ（トランザクション内の場合true、トランザクション外の場合false）
     * @return イベント処理結果
     * @throws EventException 不正なイベントが渡された
     * @throws SystemException イベント処理時にシステム例外が発生
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @since 6.0
     */
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
