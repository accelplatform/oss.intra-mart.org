/*
 * GenericEJBEventListnerAgentLocal.java
 */
package org.intra_mart.framework.base.event;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.base.event.StandardEventListener;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * 汎用的なEJB用のイベントリスナのローカルインタフェースです。
 *
 * @version 1.0
 * @since 5.0
 */
public interface GenericEJBEventListenerAgentLocal extends EJBLocalObject {

    /**
     * イベント処理を行います。
     * 実際の処理は<CODE>listenerName</CODE>で定義されたクラスで行われます。
     * <CODE>listenerName</CODE>で指定されるクラスは{@link StandardEventListener}を継承し、引数のないコンストラクタを持つ必要があります。
     *
     * @param event イベント
     * @param listenerName イベントリスナのクラス名
     * @return イベント処理結果
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @throws SystemException システム処理時にアプリケーション例外が発生
     * @throws EJBException システムレベルのエラー
     */
    public EventResult execute(Event event, String listenerName) throws ApplicationException, SystemException, EJBException;
}
