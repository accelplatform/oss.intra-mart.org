package org.intra_mart.framework.base.event.container;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventException;
import org.intra_mart.framework.base.event.EventPropertyException;
import org.intra_mart.framework.base.event.EventPropertyHandler;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.system.container.IMContainer;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;


/**
 * イベントコンテナのインターフェースです。<br>
 * イベントコンテナは{@link org.intra_mart.framework.base.event.EventManager}の振る舞いを決定します。
 * EventManagerではイベントコンテナを保管し、その挙動はイベントコンテナの実装クラスに依存します。
 * イベントコンテナの実装は通常{@link org.intra_mart.framework.base.event.container.factory.EventContainerFactory}によって生成されます。
 *
 * @author INTRAMART
 * @since 6.0
 */
public interface EventContainer extends IMContainer{
	
    /**
     * イベントプロパティハンドラのキー
     */
    public static final String EVENT_PROPERTY_HANDLER_KEY = "event";

    /**
     * イベントプロパティハンドラを取得します。
     *
     * @return イベントプロパティハンドラ
     */
    public EventPropertyHandler getEventPropertyHandler();

    /**
     * パラメータで指定された内容に該当するイベントを生成します。
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
            throws EventPropertyException, EventException;

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
   public EventResult dispatch(Event event, boolean transaction)
       throws EventException, SystemException, ApplicationException;
}
