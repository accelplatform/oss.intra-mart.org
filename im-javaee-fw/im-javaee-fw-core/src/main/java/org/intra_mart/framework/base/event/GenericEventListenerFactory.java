/*
 * GenericEventListenerFactory.java
 *
 * Created on 2001/12/10, 15:26
 */

package org.intra_mart.framework.base.event;

import java.util.ResourceBundle;

import java.util.MissingResourceException;

/**
 * {@link GenericEventListener}を生成するイベントリスナファクトリです。
 * このファクトリで指定できる初期パラメータは以下のとおりです。<BR>
 * <TABLE border="1">
 *     <TR>
 *         <TH>パラメータ名</TH>
 *         <TH>値</TH>
 *     </TR>
 *     <TR>
 *         <TD>listener</TD>
 *         <TD>包含するイベントリスナ</TD>
 *     </TR>
 * </TABLE>
 *
 * @author INTRAMART
 * @version 1.0
 */
public class GenericEventListenerFactory implements EventListenerFactory {

    /**
     * 包含するStandardEventListenerのクラス名のパラメータ名
     */
    private static final String PARAM_LISTENER = "listener";

    /**
     * 包含するStandardEventListenerのクラス名
     */
    String listenerName;

    /**
     * GenericEventListenerFactoryを新規に生成します。
     */
    public GenericEventListenerFactory() {
        this.listenerName = null;
    }

    /**
     * イベントリスナを生成します。
     *
     * @param event イベント
     * @return イベントリスナ
     * @throws EventListenerException イベントリスナの生成に失敗
     */
    public EventListener create(Event event) throws EventListenerException {
        GenericEventListener result = null;
        Object listenerObject = null;
        StandardEventListener listener = null;

        // イベントリスナの新規生成
        result = new GenericEventListener();

        // イベントリスナの設定
        if (this.listenerName == null) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.event.i18n").getString("GenericEventListenerFactory.IncludedListenerNotDeclared");
            } catch (MissingResourceException e) {
            }
            if (event == null) {
                throw new EventListenerException(message + ", event is null");
            } else {
                throw new EventListenerException(message + ", application = " + event.getApplication() + ", key = " + event.getKey());
            }
        }
        try {
            listenerObject = Class.forName(this.listenerName).newInstance();
        } catch (Exception e) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.event.i18n").getString("Common.FailedToCreateEventListener");
            } catch (MissingResourceException ex) {
            }
            if (event == null) {
                throw new EventListenerException(message + " : listener = " + this.listenerName + ", event is null", e);
            } else {
                throw new EventListenerException(message + " : listener = " + this.listenerName + ", application = " + event.getApplication() + ", key = " + event.getKey(), e);
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
                throw new EventListenerException(message + " : listener = " + this.listenerName + ", event is null");
            } else {
                throw new EventListenerException(message + " : listener = " + this.listenerName + ", application = " + event.getApplication() + ", key = " + event.getKey());
            }
        }
        result.setListener(listener);

        // イベントリスナを返す
        return result;
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
