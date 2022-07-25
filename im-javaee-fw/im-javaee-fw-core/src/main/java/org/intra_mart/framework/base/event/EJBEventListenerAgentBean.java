/*
 * EJBEventListenerAgentBean.java
 *
 * Created on 2002/02/25, 19:29
 */

package org.intra_mart.framework.base.event;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.ejb.EJBException;
import java.rmi.RemoteException;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * Session Beanを利用したイベントリスナの抽象クラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class EJBEventListenerAgentBean implements SessionBean {

    /**
     * セッションコンテキスト
     */
    protected SessionContext sessionContext;

    /**
     * EJBEventListenerAgentBeanを新規に生成します。
     */
    public EJBEventListenerAgentBean() {
        this.sessionContext = null;
    }

    /**
     * 関連するSessionContextを設定します。
     * コンテナはインスタンス生成後にこのメソッドを呼び出します。
     *
     * @param sessionContext SessionContextインタフェース
     */
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    /**
     * Session Beanのが終了する前にEJBコンテナが呼び出すメソッドです。
     *
     * @throws EJBException システムレベルのエラー
     * @throws RemoteException EJB 1.0以前との互換性のための例外
     */
    public void ejbRemove() throws EJBException {
    }

    /**
     * &quot;passive&quot;状態の時にインスタンスが活性化される時に呼ばれるメソッドです。
     * ejbPassivate()メソッドで先にリリースされたすべてのリソースを再取得します。
     *
     * @throws EJBException システムレベルのエラー
     * @throws RemoteException EJB 1.0以前との互換性のための例外
     */
    public void ejbActivate() throws EJBException {
    }

    /**
     * インスタンスが&quot;passive&quot;状態に移る前に呼ばれるメソッドです。
     * ejbActivate()メソッドで再取得されるリソースをすべてリリースします。
     *
     * @throws EJBException システムレベルのエラー
     * @throws RemoteException EJB 1.0以前との互換性のための例外
     */
    public void ejbPassivate() throws EJBException {
    }

    /**
     * イベント処理を実行します。
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws EventManagerException イベントマネージャの生成に失敗
     * @throws EventException イベント処理実行に失敗
     * @throws SystemException イベント処理時にシステム例外が発生
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @since 4.2
     */
    protected EventResult dispatchEvent(Event event)
        throws
            EventManagerException,
            EventException,
            SystemException,
            ApplicationException {

        return EventManager.getEventManager().dispatch(event, true);
    }
}
