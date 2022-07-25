/*
 * EventListenerFactory.java
 *
 * Created on 2001/11/30, 16:22
 */

package org.intra_mart.framework.base.event;

/**
 * イベントリスナを生成します。
 * <CODE>EventListenerFactory</CODE>は生成する{@link EventListener}の種別ごとに必要です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface EventListenerFactory {

    /**
     * イベントリスナを生成します。
     *
     * @param event イベント
     * @return イベントリスナ
     * @throws EventListenerException イベントリスナの生成に失敗
     */
    public EventListener create(Event event) throws EventListenerException;

    /**
     * 初期パラメータを設定します。
     * 複数のパラメータが設定される場合、その順番は保証されません。
     *
     * @param name パラメータ名
     * @param value パラメータの値
     * @throws EventListenerException パラメータの設定時に例外が発生
     */
    public void initParam(String name, String value) throws EventListenerException;
}
