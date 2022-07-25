/*
 * EventListenerInfo.java
 *
 * Created on 2001/11/29, 19:05
 */

package org.intra_mart.framework.base.event;

/**
 * イベントトリガの情報です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EventTriggerInfo {

    /**
     * ソート番号
     */
    private int number;

    /**
     * イベントトリガ名
     */
    private String name;

    /**
     * EventTriggerInfoを新規に生成します。
     */
    public EventTriggerInfo() {
        setNumber(0);
        setName("");
    }

    /**
     * ソート番号を設定します。
     *
     * @param number ソート番号
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * ソート番号を取得します。
     *
     * @return ソート番号
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * イベントトリガ名を設定します。
     *
     * @param name イベントトリガ名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * イベントトリガ名を取得します。
     *
     * @return イベントトリガ名
     */
    public String getName() {
        return this.name;
    }
}
