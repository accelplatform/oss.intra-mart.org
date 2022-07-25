/*
 * GenericEJBEventListenerAgentBean.java
 *
 * Created on 2002/02/25, 14:25
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.log.LogConstant;
import org.intra_mart.framework.system.log.LogManager;

import javax.ejb.EJBException;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * EJBを利用する汎用的なイベントリスナーのBeanです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class GenericEJBEventListenerAgentBean
    extends EJBEventListenerAgentBean {

    /**
     * GenericEJBEventListenerAgentBeanを新規に生成します。
     */
    public GenericEJBEventListenerAgentBean() {
        super();
    }

    /**
     * ホームインタフェースの引数なしのcreate()メソッドに対応します。
     */
    public void ejbCreate() {
    }

    /**
     * イベント処理を実行します。
     * listenerNameは{@link StandardEventListener}を継承したクラスであり、引数を持たないコンストラクタが存在する必要があります。
     *
     * @return イベント処理結果
     * @param event イベント
     * @param listenerName リスナーのクラス名
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @throws SystemException システム処理時にアプリケーション例外が発生
     * @throws EJBException システムレベルのエラー
     */
    public EventResult execute(Event event, String listenerName)
        throws ApplicationException, SystemException, EJBException {
        	
        StandardEventListener listener = null;
        EventResult result = null;

        // イベントリスナの生成
        try {
            listener =
                (StandardEventListener)Class
                    .forName(listenerName)
                    .newInstance();
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }

        // イベントの実行
        try {
            listener.setInTransaction(true);
            result = listener.execute(event);
        } catch (ApplicationException e) {
            LogManager.getLogManager().getLogAgent().sendMessage(
                EventManager.class.getName(),
                LogConstant.LEVEL_ERROR,
                EventManager.LOG_HEAD + e.getMessage(),
                e);
            try {
                this.sessionContext.setRollbackOnly();
            } catch (IllegalStateException ex) {
                LogManager.getLogManager().getLogAgent().sendMessage(
                    EventManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    EventManager.LOG_HEAD + ex.getMessage(),
                    ex);
            }
            throw e;
        } catch (SystemException e) {
            LogManager.getLogManager().getLogAgent().sendMessage(
                EventManager.class.getName(),
                LogConstant.LEVEL_ERROR,
                EventManager.LOG_HEAD + e.getMessage(),
                e);
            try {
                this.sessionContext.setRollbackOnly();
            } catch (IllegalStateException ex) {
                LogManager.getLogManager().getLogAgent().sendMessage(
                    EventManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    EventManager.LOG_HEAD + ex.getMessage(),
                    ex);
            }
            throw e;
        } catch (Exception e) {
            LogManager.getLogManager().getLogAgent().sendMessage(
                EventManager.class.getName(),
                LogConstant.LEVEL_ERROR,
                EventManager.LOG_HEAD + e.getMessage(),
                e);
            try {
                this.sessionContext.setRollbackOnly();
            } catch (IllegalStateException ex) {
                LogManager.getLogManager().getLogAgent().sendMessage(
                    EventManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    EventManager.LOG_HEAD + ex.getMessage(),
                    ex);
            }
            throw new SystemException(e.getMessage(), e);
        }

        return result;
    }
}
