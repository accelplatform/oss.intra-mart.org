/*
 * StandardEJBEventListenerFactory.java
 *
 * Created on 2002/03/24, 17:02
 */

package org.intra_mart.framework.base.event;

import java.util.ResourceBundle;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import java.util.MissingResourceException;
import javax.naming.NamingException;

/**
 * 標準的なEJB用のイベントリスナファクトリです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class StandardEJBEventListenerFactory extends EJBEventListenerFactory {

    /**
     * StandardEJBEventListener
     */
    private StandardEJBEventListener listener;

    /**
     * StandardEJBEventListenerFactoryを新規に生成します。
     */
    public StandardEJBEventListenerFactory() {
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
                    StandardEJBEventListenerAgentHome home;

                    try {
                        context = new InitialContext();
                        home = (StandardEJBEventListenerAgentHome)PortableRemoteObject.narrow(context.lookup(getJNDIName()), StandardEJBEventListenerAgentHome.class);
                    } catch (NamingException e) {
                        throw new EventListenerException(e.getMessage(), e);
                    } catch (ClassCastException e) {
                        throw new EventListenerException(e.getMessage(), e);
                    }
                    this.listener = new StandardEJBEventListener(home);
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
        }
    }
}
