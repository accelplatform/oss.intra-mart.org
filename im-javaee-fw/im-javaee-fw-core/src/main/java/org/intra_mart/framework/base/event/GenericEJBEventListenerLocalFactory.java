/*
 * GenericEJBEventListnerLocalFactory.java
 */
package org.intra_mart.framework.base.event;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.intra_mart.framework.base.event.EJBEventListenerFactory;
import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventListener;
import org.intra_mart.framework.base.event.EventListenerException;

/**
 * GenericEJBEventListnerLocalを新規に生成します。
 * 
 * @version 1.0
 * @since 5.0
 */
public class GenericEJBEventListenerLocalFactory extends EJBEventListenerFactory {

    /**
     * イベントリスナーのクラス名のパラメータ名
     */
    public static final String LISTENER_PARAM = "listener";

    /**
     * GenericEJBEventListnerLocal
     */
    private GenericEJBEventListenerLocal listener;

    /**
     * 実際に処理を行うStandardEventListenerのクラス名
     */
    private String listenerName;

    /**
     * GenericEJBEventListnerLocalFactoryを新規に生成します。
     */
    public GenericEJBEventListenerLocalFactory() {
        super();
    }

    /**
     * イベントリスナを生成します。
     *
     * @param event イベント
     * @return イベントリスナ
     * @throws EventListenerException イベントリスナの生成に失敗
     */
    public EventListener create(Event event) throws EventListenerException {
        if (this.listener == null) {
            synchronized(this) {
                if (this.listener == null) {
                    InitialContext context;
                    GenericEJBEventListenerAgentLocalHome home;

                    try {
                        context = new InitialContext();
                        home = (GenericEJBEventListenerAgentLocalHome)context.lookup(getJNDIName());
                    } catch (NamingException e) {
                        throw new EventListenerException(e.getMessage(), e);
                    } catch (ClassCastException e) {
                        throw new EventListenerException(e.getMessage(), e);
                    }
                    this.listener = new GenericEJBEventListenerLocal(home, getListenerName());
                }
            }
        }

        return this.listener;
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
        if (name.equals(HOME_PARAM)) {
            if (value == null || value.equals("")) {
                throw new EventListenerException("Initialize parameter is illegal. : " + HOME_PARAM);
            }
            setJNDIName(value);
        } else if (name.equals(LISTENER_PARAM)) {
            if (value == null || value.equals("")) {
                throw new EventListenerException("Initialize parameter is illegal. : " + LISTENER_PARAM);
            }
            setListenerName(value);
        }
    }

    /**
     * EJBEventListenerAgentBeanのクラス名を設定します。
     *
     * @param listenerName EJBEventListenerAgentBeanのクラス名
     */
    protected void setListenerName(String listenerName) {
        this.listenerName = listenerName;
    }

    /**
     * EJBEventListenerAgentBeanのクラス名を取得します。
     *
     * @return EJBEventListenerAgentBeanのクラス名
     */
    public String getListenerName() {
        return this.listenerName;
    }
}
