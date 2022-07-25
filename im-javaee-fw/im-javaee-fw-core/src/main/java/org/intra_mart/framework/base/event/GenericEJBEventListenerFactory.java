/*
 * GenericEJBEventListenerFactory.java
 *
 * Created on 2002/02/25, 17:18
 */

package org.intra_mart.framework.base.event;

import java.util.ResourceBundle;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import java.util.MissingResourceException;

/**
 * GenericEJBEventListenerを生成します。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class GenericEJBEventListenerFactory extends EJBEventListenerFactory {

    /**
     * イベントリスナーのクラス名のパラメータ名
     */
    public static final String LISTENER_PARAM = "listener";

    /**
     * GenericEJBEventListener
     */
    private GenericEJBEventListener listener;

    /**
     * 実際に処理を行うStandardEventListenerのクラス名
     */
    private String listenerName;

    /**
     * GenericEJBEventListenerFactoryを新規に生成します。
     */
    public GenericEJBEventListenerFactory() {
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
                    GenericEJBEventListenerAgentHome home;

                    try {
                        context = new InitialContext();
                        home = (GenericEJBEventListenerAgentHome)PortableRemoteObject.narrow(context.lookup(getJNDIName()), GenericEJBEventListenerAgentHome.class);
                    } catch (NamingException e) {
                        throw new EventListenerException(e.getMessage(), e);
                    } catch (ClassCastException e) {
                        throw new EventListenerException(e.getMessage(), e);
                    }
                    this.listener = new GenericEJBEventListener(home, getListenerName());
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
                String message = null;
                try {
                    message = ResourceBundle.getBundle("org.intra_mart.framework.base.event.i18n").getString("Common.IllegalInitParameter");
                } catch (MissingResourceException e) {
                }
                throw new EventListenerException(message + " : " + HOME_PARAM);
            }
            setJNDIName(value);
        } else if (name.equals(LISTENER_PARAM)) {
            if (value == null || value.equals("")) {
                String message = null;
                try {
                    message = ResourceBundle.getBundle("org.intra_mart.framework.base.event.i18n").getString("Common.IllegalInitParameter");
                } catch (MissingResourceException e) {
                }
                throw new EventListenerException(message + " : " + LISTENER_PARAM);
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
