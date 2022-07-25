/*
 * EJBEventListenerFactory.java
 *
 * Created on 2002/02/25, 13:28
 */

package org.intra_mart.framework.base.event;

/**
 * EJBEventListenerFactoryを生成します。
 * イベントリスナファクトリの設定では、以下の初期パラメータの設定が必須です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class EJBEventListenerFactory implements EventListenerFactory {

    /**
     * ホームインタフェースを取得するときのJNDI名のパラメータ名
     */
    public static final String HOME_PARAM = "home";

    /**
     * EJBEventListenerAgentHomeのJNDI名
     */
    private String jndiName;

    /**
     * EJBEventListenerFactoryを新規に生成します。
     */
    public EJBEventListenerFactory() {
        setJNDIName(null);
    }

    /**
     * EJBEventListenerAgentHomeのJNDI名を設定します。
     *
     * @param jndiName EJBEventListenerAgentHomeのJNDI名
     */
    protected void setJNDIName(String jndiName) {
        this.jndiName = jndiName;
    }

    /**
     * EJBEventListenerAgentHomeのJNDI名を取得します。
     *
     * @return EJBEventListenerAgentHomeのJNDI名
     */
    public String getJNDIName() {
        return this.jndiName;
    }
}
