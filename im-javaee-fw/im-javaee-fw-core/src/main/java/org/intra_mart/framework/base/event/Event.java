/*
 * Event.java
 *
 * Created on 2001/11/29, 13:18
 */

package org.intra_mart.framework.base.event;

import java.io.Serializable;

/**
 * 処理を実行するための情報です。
 * 処理は{@link EventListener}で行われますが、その際に渡される情報を{@link Event}のサブクラスで定義します。
 * {@link Event}のサブクラスを生成する場合はコンストラクタを使用せず、変わりに{@link EventManager#createEvent(String, String, String, String)}を使用してください。<BR>
 * <B>注意：</B><BR>
 * このクラスのサブクラスでは以下のフィールドは定義しないでください。
 * <UL>
 * <LI>applicationフィールド
 * <LI>keyフィールド
 * <LI>loginUserIDフィールド
 * <LI>loginGroupIDフィールド
 * </UL>
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class Event implements Serializable {

    /**
     * アプリケーション
     */
    private String application;

    /**
     * イベントキー
     */
    private String key;

    /**
     * イベントを新規に生成します。
     */
    public Event() {
        setKey(null);
        setApplication(null);
    }

    /**
     * イベントキーを設定します。
     * イベントキーは基本的に参照専用とするため、このメソッドは同じパッケージ以外からは使用できません。
     *
     * @param key イベントキー
     */
    final void setKey(String key) {
        this.key = key;
    }

    /**
     * イベントキーを取得します。
     *
     * @return イベントキー
     */
    public final String getKey() {
        return this.key;
    }

    /**
     * アプリケーションを設定します。
     * アプリケーションは基本的に参照専用とするため、このメソッドは同じパッケージ以外からは使用できません。
     *
     * @param application アプリケーション
     */
    final void setApplication(String application) {
        this.application = application;
    }
    
    /**
     * アプリケーションを取得します。
     *
     * @return アプリケーション
     */
    public final String getApplication() {
        return this.application;
    }
}
