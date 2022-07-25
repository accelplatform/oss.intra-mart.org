/*
 * EventTriggerAdapter.java
 *
 * Created on 2002/03/21, 17:33
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.base.data.DataAccessController;

import org.intra_mart.framework.base.data.DataPropertyException;
import org.intra_mart.framework.base.data.DataConnectorException;
import org.intra_mart.framework.base.data.DAOException;
import org.intra_mart.framework.base.data.DataConnectException;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * DAOを取得するメソッドを持ったイベントトリガです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class EventTriggerAdapter implements EventTrigger {

    /**
     * データアクセスコントローラ
     */
    private DataAccessController controller;

    /**
     * EventTriggerAdapterを新規に生成します。
     */
    public EventTriggerAdapter() {
        this.controller = null;
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
    protected Object getDAO(String application, String key, String connect) throws DataPropertyException, DataConnectorException, DAOException, DataConnectException {
        return this.controller.getDAO(application, key, connect);
    }

    /**
     * イベントに対する処理を行います。
     * トランザクションは<CODE>controller</CODE>で管理されています。
     * このメソッドは{@link #fire(org.intra_mart.framework.base.event.Event)}メソッドを呼ぶように実装されています。
     *
     * @param event イベント
     * @param controller データアクセスコントローラ
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    public void fire(Event event, DataAccessController controller) throws SystemException, ApplicationException {
        this.controller = controller;
        fire(event);
    }

    /**
     * イベントに対する処理を行います。
     * このメソッドを実装するとき、内部でトランザクションのロールバックやコミットを行わないようにしてください。
     * メソッドが失敗した場合、またはロールバックを行いたい場合は、ApplicationExceptionのサブクラスを例外としてthrowしてください。
     *
     * @param event イベント
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */    
    protected abstract void fire(Event event) throws SystemException, ApplicationException;
}
