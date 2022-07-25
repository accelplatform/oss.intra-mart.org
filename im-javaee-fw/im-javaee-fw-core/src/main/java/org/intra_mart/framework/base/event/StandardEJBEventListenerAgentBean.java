/*
 * StandardEJBEventListenerAgentBean.java
 *
 * Created on 2002/02/25, 11:09
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.log.LogConstant;
import org.intra_mart.framework.system.log.LogManager;
import org.intra_mart.framework.base.data.DataManager;
import org.intra_mart.framework.base.data.DataAccessController;

import javax.ejb.EJBException;

import org.intra_mart.framework.base.data.DataManagerException;
import org.intra_mart.framework.base.data.DataConnectException;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * EJBを利用する場合の抽象的なイベントリスナです。
 * メソッド{@link #fire(org.intra_mart.framework.base.event.Event, org.intra_mart.framework.base.data.DataAccessController)}を実装する必要があります。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class StandardEJBEventListenerAgentBean
    extends EJBEventListenerAgentBean {

    /**
     * StandardEJBEventListenerAgentBeanを新規に生成します。
     */
    public StandardEJBEventListenerAgentBean() {
        super();
    }

    /**
     * ホームインタフェースの引数なしのcreate()メソッドに対応します。
     *
     * @since 3.2
     */
    public void ejbCreate() {
    }

    /**
     * イベント処理を実行します。
     * このクラスでは以下のような順番で処理を行います。
     * <OL>
     * <LI>イベントトリガの実行(前処理)
     * <LI>イベント処理の実行({@link #fire(Event, DataAccessController)})
     * <LI>イベントトリガの実行(後処理)
     * <LI>簡易トランザクションのコミット({@link org.intra_mart.framework.base.data.DataAccessController#commit()})
     * </OL>
     * このクラスでは{@link StandardEventListener#execute(Event)}とは違い直接UserTransactionを扱っていません。そのため、このEJBを使うためにはコンテナ管理トランザクション(Container-management Transaction:CMT)を使用してください。
     *
     * @return イベント処理結果
     * @param event イベント
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @throws SystemException システム処理時にアプリケーション例外が発生
     * @throws EJBException システムレベルのエラー
     */
    public EventResult execute(Event event)
        throws ApplicationException, SystemException, EJBException {

        DataManager manager = null;
        DataAccessController dataAccessController = null;
        EventTriggerList triggerList = null;
        EventTriggerList postTriggerList = null;
        EventResult result = null;

        // データアクセスコントローラの取得
        try {
            manager = DataManager.getDataManager();
            dataAccessController = manager.getDataAccessController();
        } catch (DataManagerException e) {
            throw new EJBException(e.getMessage(), e);
        }

        // イベント処理
        try {
            // イベントトリガ（前処理）の取得
            try {
                triggerList =
                    new EventTriggerList(
                        event.getApplication(),
                        event.getKey());
            } catch (Exception e) {
                // エラーログに出力// Debug
                LogManager.getLogManager().getLogAgent().sendMessage(
                    EventManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    EventManager.LOG_HEAD + e.getMessage(),
                    e);
                triggerList = null;
            }

            // イベントトリガ（後処理）の取得
            try {
                postTriggerList =
                    new EventTriggerList(
                        event.getApplication(),
                        event.getKey(),
                        false);
            } catch (Exception e) {
                // エラーログに出力// Debug
                LogManager.getLogManager().getLogAgent().sendMessage(
                    EventManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    EventManager.LOG_HEAD + e.getMessage(),
                    e);
                postTriggerList = null;
            }

            // イベントトリガの実行（前処理）
            if (triggerList != null) {
                triggerList.fireAll(event, dataAccessController);
            }

            // イベント処理の実行
            result = fire(event, dataAccessController);

            // イベントトリガの実行（後処理）
            if (postTriggerList != null) {
                postTriggerList.fireAll(event, dataAccessController);
            }

            dataAccessController.commit();
        } catch (Exception e) {
            LogManager.getLogManager().getLogAgent().sendMessage(
                EventManager.class.getName(),
                LogConstant.LEVEL_ERROR,
                EventManager.LOG_HEAD + e.getMessage(),
                e);
            // 例外が発生した場合、ロールバックする
            try {
                this.sessionContext.setRollbackOnly();
            } catch (IllegalStateException ex) {
                LogManager.getLogManager().getLogAgent().sendMessage(
                    EventManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    EventManager.LOG_HEAD + ex.getMessage(),
                    ex);
            }
            try {
                dataAccessController.rollback();
            } catch (DataConnectException ex) {
                // ログ
                LogManager.getLogManager().getLogAgent().sendMessage(
                    EventManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    EventManager.LOG_HEAD + ex.getMessage(),
                    ex);
            }
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else if (e instanceof ApplicationException) {
                throw (ApplicationException)e;
            } else if (e instanceof SystemException) {
                throw (SystemException)e;
            } else {
                throw new SystemException(e.getMessage(), e);
            }
        } finally {
            // データアクセスコントローラのリリース
            try {
                dataAccessController.release();
            } catch (DataConnectException e) {
                // ログ
                LogManager.getLogManager().getLogAgent().sendMessage(
                    EventManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    EventManager.LOG_HEAD + e.getMessage(),
                    e);
            }
        }

        // イベント処理結果を返す
        return result;
    }

    /**
     * イベントに対する処理です。
     *
     * @param event イベント
     * @param dataAccessController データアクセスコントローラ
     * @return イベント処理結果
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    protected abstract EventResult fire(
        Event event,
        DataAccessController dataAccessController)
        throws SystemException, ApplicationException;
}
