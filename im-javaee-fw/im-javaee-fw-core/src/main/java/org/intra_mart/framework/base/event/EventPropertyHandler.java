/*
 * EventPropertyHandler.java
 *
 * Created on 2001/11/29, 13:58
 */

package org.intra_mart.framework.base.event;

import java.util.Collection;

import org.intra_mart.framework.system.property.PropertyHandler;

/**
 * イベントの設定情報に接続するクラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface EventPropertyHandler extends PropertyHandler {

    /**
     * プロパティの動的読み込みが可能かどうか調べます。
     *
     * @return true：プロパティの動的読み込みが可能、false：プロパティの動的読み込み不可
     * @throws EventPropertyException チェック時に例外が発生
     * @since 3.2
     */
    public boolean isDynamic() throws EventPropertyException;

    /**
     * キーに該当するイベントのクラス名を取得します。
     * 該当するイベントが存在しない場合、nullを返します。
     *
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントのクラス名
     * @throws EventPropertyException イベントのクラス名の取得に失敗
     */
    public String getEventName(String application, String key)
        throws EventPropertyException;

    /**
     * キーに該当するイベントリスナファクトリのクラス名を取得します。
     *
     * @param application アプリケーション
     * @param key イベントリスナファクトリのキー
     * @return イベントリスナファクトリのクラス名
     * @throws EventPropertyException イベントリスナファクトリのクラス名の取得に失敗
     */
    public String getEventListenerFactoryName(String application, String key)
        throws EventPropertyException;

    /**
     * キーに該当するイベントリスナファクトリの初期パラメータを取得します。
     *
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントリスナファクトリの初期パラメータ
     * @throws EventPropertyException イベントリスナファクトリの初期パラメータの取得に失敗
     */
    public EventListenerFactoryParam[] getEventListenerFactoryParams(
        String application,
        String key)
        throws EventPropertyException;

    /**
     * キーに該当するイベントのイベントトリガ情報をすべて取得します。
     * <CODE>application</CODE>と<CODE>key</CODE>で定義される{@link EventTrigger}を定義された順番でソートしたCollectionとして取得します。
     *
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントトリガ情報のコレクション
     * @throws EventPropertyException イベントトリガ情報の取得に失敗
     * @see EventListener
     */
    public Collection getEventTriggerInfos(String application, String key)
        throws EventPropertyException;

    /**
     * キーに該当するイベントのイベントトリガ情報をすべて取得します。
     * ここで取得されるイベントトリガはイベントの処理後に実行されます。
     * <CODE>application</CODE>と<CODE>key</CODE>で定義される{@link EventTrigger}を定義された順番でソートしたCollectionとして取得します。
     *
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントトリガ情報のコレクション
     * @throws EventPropertyException イベントトリガ情報の取得に失敗
     * @see EventListener
     */
    public Collection getPostEventTriggerInfos(String application, String key)
        throws EventPropertyException;
}
