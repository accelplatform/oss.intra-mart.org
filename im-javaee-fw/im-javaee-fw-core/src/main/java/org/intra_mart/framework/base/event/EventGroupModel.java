/*
 * 作成日: 2003/12/23
 */
package org.intra_mart.framework.base.event;

import java.util.Collection;

/**
 * Event情報を管理するクラスです。
 * @author INTRAMART
 * @version 1.0
 */
public class EventGroupModel {

    public static final String ID = "event-group";
    public static final String P_ID_EVENT_KEY = "event-key";
    public static final String P_ID_EVENT_NAME = "event-class";
    public static final String P_ID_PRE_TRIGGER = "pre-trigger";
    public static final String P_ID_POST_TRIGGER = "post-trigger";
    public static final String P_ID_TRIGGER_CLASS = "trigger-class";

    /**
     * イベントキー
     */
    private String eventKey;

    /**
     * イベントクラス
     */
    private String eventName;

    /**
     * イベント前トリガー情報
     */
    private Collection preTriggerInfos;

    /**
     * イベント後トリガー情報
     */
    private Collection postTriggerInfos;
    
    /**
     * イベントファクトリ
     */
    private EventFactoryModel eventFactory;

    /**
     * イベント前トリガ情報を設定します。 
     * @param triggerInfos
     */
    void setPreTriggerInfos(Collection preTriggerInfos) {
        this.preTriggerInfos = preTriggerInfos;
    }

    /**
     * イベント前トリガ情報を取得します。 
     * @return Collection イベントトリガ情報
     */
    Collection getPreTriggerInfos() {
        return preTriggerInfos;
    }

    /**
     * イベント後トリガ情報を設定します。 
     * @param triggerInfos
     */
    void setPostTriggerInfos(Collection postTriggerInfos) {
        this.postTriggerInfos = postTriggerInfos;
    }

    /**
     * イベント後トリガ情報を取得します。 
     * @return Collection イベントトリガ情報
     */
    Collection getPostTriggerInfos() {
        return postTriggerInfos;
    }
    
    /**
	 * イベント名を設定します。
	 * @param eventName
	 */
    void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * イベント名を取得します。
     * @return イベント名
     */
    String getEventName() {
        return eventName;
    }

    /**
     * イベントキーを設定します。 
     * @param eventKey
     */
    void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    /**
     * イベントキーを取得します。 
     * @return イベントキー
     */
    String getEventKey() {
        return eventKey;
    }

    /**
     * イベントファクトリを設定します。 
     * @param eventFactory
     */
    void setEventFactory(EventFactoryModel eventFactory) {
        this.eventFactory = eventFactory;
    }

    /**
     * イベントファクトリを取得します。 
     * @return イベントファクトリ
     */
    EventFactoryModel getEventFactory() {
        return eventFactory;
    }
}
