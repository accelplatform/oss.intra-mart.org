/*
 * StandardEventListener.java
 *
 * Created on 2001/12/04, 18:33
 */

package org.intra_mart.framework.base.event;

import java.util.ResourceBundle;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.intra_mart.framework.base.data.DataManager;
import org.intra_mart.framework.base.data.DataAccessController;
import org.intra_mart.framework.system.log.LogConstant;
import org.intra_mart.framework.system.log.LogManager;

import java.util.MissingResourceException;

import org.intra_mart.framework.base.data.DataConnectException;
import org.intra_mart.framework.base.data.DataConnectorException;
import org.intra_mart.framework.base.data.DataPropertyException;
import org.intra_mart.framework.base.data.DAOException;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.IllegalSystemException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * 汎用的なビジネスロジック用のクラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class StandardEventListener implements EventListener {

    /**
     * データアクセスコントローラ
     */
    private DataAccessController dataAccessController;

    /**
     * トランザクション
     */
    private boolean inTransaction = false;

    /**
     * このイベントリスナで使用するデータアクセスコントローラです。
     * このクラスを継承する場合、メソッド{@link #fire(org.intra_mart.framework.base.event.Event)}内ではデータアクセスコントローラの取得にはこのメソッドを使うようにしてください。
     *
     * @return データアクセスコントローラ
     */
    protected DataAccessController getDataAccessController() {
        return this.dataAccessController;
    }

    /**
     * DAOを取得します。
     * キーと接続情報で指定されたDAOを取得します。
     *
     * @param application アプリケーションID
     * @param key DAOのキー
     * @param connect 接続情報
     * @return DAO
     * @throws DataPropertyException プロパティの取得に失敗
     * @throws DataConnectorException データコネクタの取得に失敗
     * @throws DAOException DAOの取得に失敗
     * @throws DataConnectException データリソースとの接続に失敗
     */
    protected Object getDAO(String application, String key, String connect)
        throws
            DataPropertyException,
            DataConnectorException,
            DAOException,
            DataConnectException {
        return getDataAccessController().getDAO(application, key, connect);
    }

    /**
     * 処理を実行します。
     * このクラスでは以下のような順番で処理を行います。
     * <OL>
     * <LI>ユーザトランザクションの開始
     * <LI>イベントトリガの実行(前処理)
     * <LI>イベント処理の実行({@link #fire(org.intra_mart.framework.base.event.Event)})
     * <LI>イベントトリガの実行(後処理)
     * <LI>簡易トランザクションのコミット({@link org.intra_mart.framework.base.data.DataAccessController#commit()})
     * <LI>ユーザトランザクションのコミット
     * </OL>
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @throws SystemException システム処理時にアプリケーション例外が発生
     */
    public EventResult execute(Event event)
        throws SystemException, ApplicationException {
        DataManager manager = DataManager.getDataManager();
        EventTriggerList triggerList = null;
        EventTriggerList postTriggerList = null;
        EventResult result = null;
        InitialContext ic = null;
        UserTransaction ut = null;

        // データアクセスコントローラの取得
        this.dataAccessController = manager.getDataAccessController();

        // イベントトリガ（前処理）の取得
        try {
            triggerList =
                new EventTriggerList(event.getApplication(), event.getKey());
        } catch (Throwable e) {
            // エラーログに出力
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
        } catch (Throwable e) {
            // エラーログに出力
            LogManager.getLogManager().getLogAgent().sendMessage(
                EventManager.class.getName(),
                LogConstant.LEVEL_ERROR,
                EventManager.LOG_HEAD + e.getMessage(),
                e);
            postTriggerList = null;
        }

        // トランザクションの開始
        if (!this.inTransaction) {
            try {
                ic = new InitialContext();
                ut = (UserTransaction)ic.lookup("java:comp/UserTransaction");
                ut.begin();
            } catch (Throwable e) {
                try {
                    this.dataAccessController.release();
                } catch (Throwable ex) {
                    // エラーログに出力
                    LogManager.getLogManager().getLogAgent().sendMessage(
                        EventManager.class.getName(),
                        LogConstant.LEVEL_ERROR,
                        EventManager.LOG_HEAD + ex.getMessage(),
                        ex);
                }
                throw new SystemException(e.getMessage(), e);
            }
        }

        try {
            // 前イベントトリガの実行
            if (triggerList != null) {
                triggerList.fireAll(event, this.dataAccessController);
            }

            try {
                // イベント処理の実行
                result = fire(event);

                // 後イベントトリガの実行
                if (postTriggerList != null) {
                    postTriggerList.fireAll(event, this.dataAccessController);
                }

                // トランザクションのコミット
                this.dataAccessController.commit();
            } catch (Throwable e) {
                // 例外が発生した場合、ロールバックする
                try {
                    this.dataAccessController.rollback();
                } catch (Throwable ex) {
                    // コミット時の例外をエラーログに出力
                    LogManager.getLogManager().getLogAgent().sendMessage(
                        EventManager.class.getName(),
                        LogConstant.LEVEL_ERROR,
                        EventManager.LOG_HEAD + ex.getMessage(),
                        ex);
                }
                throw e;
            }

            // ユーザトランザクションのコミット
            if (!this.inTransaction) {
                ut.commit();
            }
        } catch (Throwable e) {
            // ユーザトランザクションのロールバック
            if (!this.inTransaction) {
                try {
                    ut.rollback();
                } catch (Throwable ex) {
                    // ロールバック時の例外をエラーログに出力
                    LogManager.getLogManager().getLogAgent().sendMessage(
                        EventManager.class.getName(),
                        LogConstant.LEVEL_ERROR,
                        EventManager.LOG_HEAD + ex.getMessage(),
                        ex);
                }
            }
            if (e instanceof SystemException) {
                throw (SystemException)e;
            } else if (e instanceof ApplicationException) {
                throw (ApplicationException)e;
            } else {
                // SystemExceptionとApplicationException以外はシステム異常とする
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.event.i18n")
                            .getString("Common.UnsupposedExceptionExecuteEvent");
                } catch (MissingResourceException ex) {
                }
                throw new IllegalSystemException(message, e);
            }
        } finally {
            // データアクセスコントローラのリリース
            try {
                this.dataAccessController.release();
            } catch (Throwable e) {
                // リリース時の例外をエラーログに出力
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
     * @return イベント処理結果
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    protected abstract EventResult fire(Event event)
        throws SystemException, ApplicationException;

    /**
     * このイベントリスナがトランザクションの中で実行されているかどうかの情報を設定します。
     *
     * @param transaction トランザクションの中で実行されている場合：true、そうでない場合：false
     * @since 4.2
     */
    public void setInTransaction(boolean transaction) {
        this.inTransaction = transaction;
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
