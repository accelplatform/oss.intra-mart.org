/*
 * StandardEventListenerFactory.java
 *
 * Created on 2001/12/05, 18:37
 */

package org.intra_mart.framework.base.event;

import java.util.ResourceBundle;

import java.util.MissingResourceException;

/**
 * 通常のJavaクラスのイベントリスナを生成します。
 * このファクトリで指定できる初期パラメータは以下のとおりです。<BR>
 * <TABLE border="1">
 *     <TR>
 *         <TH>パラメータ名</TH>
 *         <TH>値</TH>
 *     </TR>
 *     <TR>
 *         <TD>listener</TD>
 *         <TD>生成するイベントリスナ</TD>
 *     </TR>
 * </TABLE>
 *
 * @author INTRAMART
 * @version 1.0
 */
public class StandardEventListenerFactory implements EventListenerFactory {

    /**
     * イベントリスナのパラメータ名
     */
    private static final String PARAM_LISTENER = "listener";

    /**
     * 生成するStandardEventListenerのクラス名
     */
    String listenerName;

    /**
     * StandardEventListenerFactoryを新規に生成します。
     */
    public StandardEventListenerFactory() {
    }

    /**
     * イベントリスナを生成します。
     * 初期パラメータで指定されたクラスをイベントリスナとして生成します。
     * 初期パラメータで指定されたクラスは{@link StandardEventListener}を継承している必要があります。
     *
     * @param event イベント
     * @return イベントリスナ
     * @throws EventListenerException イベントリスナの生成に失敗
     */
    public EventListener create(Event event) throws EventListenerException {
        Object listenerObject = null;
        StandardEventListener listener = null;

        try {
            listenerObject = Class.forName(this.listenerName).newInstance();
        } catch (Exception e) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.event.i18n").getString("Common.FailedToCreateEventListener");
            } catch (MissingResourceException ex) {
            }
            if (event == null) {
                throw new EventListenerException(message + " : class = " + this.listenerName + ", event is null", e);
            } else {
                throw new EventListenerException(message + " : class = " + this.listenerName + ", application = " + event.getApplication() + ", key = " + event.getKey(), e);
            }
        }
        if (listenerObject instanceof StandardEventListener) {
            listener = (StandardEventListener)listenerObject;
        } else {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.event.i18n").getString("Common.EventListenerExtended");
            } catch (MissingResourceException e) {
            }
            if (event == null) {
                throw new EventListenerException(message + " : class = " + this.listenerName + ", factory = " + this.getClass().getName() + ", event is null");
            } else {
                throw new EventListenerException(message + " : class = " + this.listenerName + ", factory = " + this.getClass().getName() + ", application = " + event.getApplication() + ", key = " + event.getKey());
            }
        }

        return listener;
    }

    /**
     * 初期パラメータを設定します。
     * 複数のパラメータが設定される場合、その順番は保証されません。
     *
     * @param name パラメータ名
     * @param value パラメータの値
     * @throws EventListenerException パラメータの設定時に例外が発生
     */
    public void initParam(String name, String value) throws EventListenerException {

        if (name != null) {
            if (name.equals(PARAM_LISTENER)) {
                // パラメータで指定されたイベントリスナの生成
                this.listenerName = value;
            }
        }
    }
}
