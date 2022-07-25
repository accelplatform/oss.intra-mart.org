/*
 * EventManager.java
 *
 * Created on 2001/11/30, 15:55
 */

package org.intra_mart.framework.base.event;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.event.container.EventContainer;
import org.intra_mart.framework.base.event.container.factory.EventContainerFactory;
import org.intra_mart.framework.base.service.ServiceManager;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.IMContainerException;
import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.log.LogConstant;
import org.intra_mart.framework.system.log.LogManager;

/**
 * イベントマネージャです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EventManager {

    /**
     * イベントフレームワークのログのプレフィックス
     */
    static String LOG_HEAD = "[J2EE][Event]";

    /**
     * イベントプロパティハンドラのキー
     */
    public static final String EVENT_PROPERTY_HANDLER_KEY = EventContainer.EVENT_PROPERTY_HANDLER_KEY;

    /**
     * データマネージャ取得フラグ
     */
    private static Boolean managerFlag = new Boolean(false);

    /**
     * イベントマネージャ
     */
    private static EventManager manager;

    /**
     * イベントコンテナ
     */
    private EventContainer eventContainer;

    /**
     * イベントマネージャを取得します。
     *
     * @return イベントマネージャ
     * @throws EventManagerException イベントマネージャの生成時に例外が発生
     */
    public static EventManager getEventManager() throws EventManagerException {
        if (!managerFlag.booleanValue()) {
            synchronized (managerFlag) {
                if (!managerFlag.booleanValue()) {
                    try {
                        manager = new EventManager();
                    } catch (EventManagerException e) {
                        String message = null;
                        try {
                            message =
                                ResourceBundle
                                    .getBundle("org.intra_mart.framework.base.event.i18n")
                                    .getString("EventManager.FailedToCreateManager");
                        } catch (MissingResourceException ex) {
                        }
                        LogManager.getLogManager().getLogAgent().sendMessage(
                            EventManager.class.getName(),
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
                                .getBundle("org.intra_mart.framework.base.event.i18n")
                                .getString("EventManager.SuccessedToCreateManager");
                    } catch (MissingResourceException e) {
                    }
                    LogManager.getLogManager().getLogAgent().sendMessage(
                        EventManager.class.getName(),
                        LogConstant.LEVEL_INFO,
                        LOG_HEAD + message);
                }
            }
        }

        return manager;
    }

    /**
     * EventManagerを生成するコンストラクタです。
     * このコンストラクタは明示的に呼び出すことはできません。
     *
     * @throws EventManagerException サービスマネージャの生成に失敗した
     */
    private EventManager() throws EventManagerException {
    	try {
			this.eventContainer = (EventContainer)new EventContainerFactory().create();
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("EventManager.SuccessedToCreateContainer");
            } catch (MissingResourceException ignore) {
            }
            LogManager.getLogManager().getLogAgent().sendMessage(
                    ServiceManager.class.getName(),
                    LogConstant.LEVEL_INFO,
                    LOG_HEAD + message + " - " + this.eventContainer.getClass().getName());
		} catch (IMContainerException e) {
			throw new EventManagerException(e.getMessage(), e);
		}
    }

    /**
     * イベントプロパティハンドラを取得します。
     *
     * @return イベントプロパティハンドラ
     */
    public EventPropertyHandler getEventPropertyHandler() {
        return this.eventContainer.getEventPropertyHandler();
    }
    
    /**
     * パラメータで指定された内容に該当するイベントを生成します。
     * 該当するイベントが{@link EventPropertyHandler}のパラメータで設定されていない場合、{@link EmptyEvent}を返します。
     * 
     * @param application アプリケーション名
     * @param key キー
     * @param info ログインユーザ情報
     * @return パラメータに該当するイベント
     * @throws EventException イベント生成に失敗
     * @throws EventPropertyException イベントプロパティの取得に失敗
     * @since 5.0
     */
    public Event createEvent(
            String application,
            String key)
            throws EventPropertyException, EventException {

    	// イベントオブジェクトを生成
    	Event event = this.eventContainer.createEvent(application, key);

        // アプリケーション名とキーの設定
        event.setApplication(application);
        event.setKey(key);

        return event;
    }

    /**
     * イベントに対する処理を実行します。
     * イベントを渡されると、イベントに関連する{@link EventListenerFactory}から
     * {@link EventListener}を生成し、{@link EventListener}に処理を依頼します。
     * <code>event</code>には必ず{@link #createEvent(String, String, String, String)}で取得したイベントを設定してください。
     * このメソッドは{@link #dispatch(Event, boolean) dispatch(event, false)}を呼び出したときと同じ効果があります。
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws EventException 不正なイベントが渡された
     * @throws SystemException イベント処理時にシステム例外が発生
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     */
    public EventResult dispatch(Event event)
        throws EventException, SystemException, ApplicationException {
        return dispatch(event, false);
    }

    /**
     * イベントに対する処理を実行します。
     * イベントを渡されると、イベントに関連する{@link EventListenerFactory}から
     * {@link EventListener}を生成し、{@link EventListener}に処理を依頼します。
     * <code>event</code>には必ず{@link #createEvent(String, String, String, String)}で取得したイベントを設定してください。
     * <BR>{@link EventListener}に処理を依頼する際、{@link EventListener#setInTransaction(boolean)}メソッドが呼ばれます。
     * このときに<code>nest</code>で指定された値が渡されます。
     *
     * @param event イベント
     * @param transaction トランザクションの中で実行されているかどうかを示すフラグ（トランザクション内の場合true、トランザクション外の場合false）
     * @return イベント処理結果
     * @throws EventException 不正なイベントが渡された
     * @throws SystemException イベント処理時にシステム例外が発生
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @since 4.2
     */
    public EventResult dispatch(Event event, boolean transaction)
        throws EventException, SystemException, ApplicationException {
        return this.eventContainer.dispatch(event, transaction);
    }
}
