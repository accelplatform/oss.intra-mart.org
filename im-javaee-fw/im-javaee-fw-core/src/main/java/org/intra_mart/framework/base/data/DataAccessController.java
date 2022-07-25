/*
 * DataAccessController.java
 *
 * Created on 2001/11/08, 14:15
 */

package org.intra_mart.framework.base.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import java.util.MissingResourceException;

/**
 * データアクセスを制御します。
 * DAOの取得、コネクタの管理等を行います。
 * このクラスを利用して簡易トランザクションを実現することも可能ですが、この方法は推奨されません。
 * トランザクションはできる限りJava Transaction API(JTA)のUserTransactionを利用して行ってください。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DataAccessController {

    /**
     * データコネクタの集合
     */
    private Map connectors;

    /**
     * データプロパティハンドラ
     */
    private DataPropertyHandler handler;

    /**
     * データアクセスコントローラを生成します。
     *
     * @param handler データプロパティハンドラ
     */
    public DataAccessController(DataPropertyHandler handler) {
        this.connectors = new HashMap();
        this.handler = handler;
    }
    
    /**
     * DataAccessControllerがキャッシュしているDataConnectorの一覧を取得します。
     * 
     * @return　DataConnector
     */
    protected Map getDataConnectors() {
    	return this.connectors;
    }
    
    /**
     * データプロパティハンドラを取得します。
     *
     * @return データプロパティハンドラ
     * @since 6.0
     */
    public DataPropertyHandler getDataPropertyHandler() {
    	return this.handler;
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
    public Object getDAO(String application, String key, String connect) throws DataPropertyException, DataConnectorException, DAOException, DataConnectException {
        Object connectorObject = null;
        DataConnector connector = null;
        String connectorName = null;
        String connectorClassName = null;
        String resource = null;
        String daoName = null;
        Object daoObject = null;
        DAO result = null;

        // データコネクタ名の取得
        connectorName = handler.getConnectorName(application, key, connect);

        // データコネクタの検索
        if (connectorName == null || connectorName.equals("")) {
            connector = null;
        } else {
            connector = (DataConnector)this.connectors.get(connectorName);
            if (connector == null) {
                // データコネクタが存在しない場合、新規に登録
                connectorClassName = handler.getConnectorClassName(connectorName);
                try {
                    connectorObject = Class.forName(connectorClassName).newInstance();
                } catch (Exception e) {
                    String message = null;
                    try {
                        message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DataAccessController.FailedToCreateDataConnector");
                    } catch (MissingResourceException ex) {
                    }
                    throw new DataConnectorException(message + " : connector name = " + connectorName + ", class = " + connectorClassName + ", application = " + application + ", key = " + key + ", connect = " + connect, e);
                }
                if (connectorObject instanceof DataConnector) {
                    connector = (DataConnector)connectorObject;
                } else {
                    String message = null;
                    try {
                        message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DataAccessController.DataConnectorNotExtended");
                    } catch (MissingResourceException e) {
                    }
                    throw new DataConnectorException(message + " : class = " + connectorClassName + ", application = " + application + ", key = " + key + ", connect = " + connect);
                }
                connector.setDataPropertyHandler(handler);
                this.connectors.put(connectorName, connector);
            }

            // データリソースの取得
            resource = handler.getConnectorResource(connectorName);
        }

        // DAOの生成
        daoName = handler.getDAOName(application, key, connect);
        try {
            daoObject = Class.forName(daoName).newInstance();
        } catch (Exception e) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DataAccessController.FailedToCreateDAO");
            } catch (MissingResourceException ex) {
            }
            throw new DAOException(message + " : class = " + daoName + ", application = " + application + ", key = " + key + ", connect = " + connect, e);
        }
        if (daoObject instanceof DAO) {
            result = (DAO)daoObject;
        } else {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DataAccessController.DAONotImplemented");
            } catch (MissingResourceException e) {
            }
            throw new DataConnectorException(message + " : class = " + daoName + ", application = " + application + ", key = " + key + ", connect = " + connect);
        }
        result.setConnectInfo(connector, resource, key, connect);

        return result;
    }

    /**
     * すべてのデータコネクタをコミットします。
     * このメソッドはこのクラスの{@link #getDAO(String, String, String)}で取得されたDAOに関連するデータコネクタの{@link DataConnector#commit()}を順に呼び出すことで簡易的なトランザクションを実現しています。
     * そのため、複数のデータベースにアクセスしたトランザクションの場合は2-Phaseコミットほど堅牢なトランザクションにはならない場合があります。
     *
     * @throws DataConnectException コミットに失敗
     */
    public void commit() throws DataConnectException {
        Iterator connectorIterator = connectors.values().iterator();
        DataConnector connector;
        DataConnectException exception = null;

        while (connectorIterator.hasNext()) {
            connector = (DataConnector)connectorIterator.next();
            try {
                connector.commit();
            } catch (DataConnectException e) {
                exception = e;
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * すべてのデータコネクタをロールバックします。
     * このメソッドはこのクラスの{@link #getDAO(String, String, String)}で取得されたDAOに関連するデータコネクタの{@link DataConnector#rollback()}を順に呼び出すことで簡易的なトランザクションを実現しています。
     * そのため、複数のデータベースにアクセスしたトランザクションの場合は2-Phaseコミットほど堅牢なトランザクションにはならない場合があります。
     *
     * @throws DataConnectException ロールバックに失敗
     */
    public void rollback() throws DataConnectException {
        Iterator connectorIterator = connectors.values().iterator();
        DataConnector connector;
        DataConnectException exception = null;

        while (connectorIterator.hasNext()) {
            connector = (DataConnector)connectorIterator.next();
            try {
                connector.rollback();
            } catch (DataConnectException e) {
                exception = e;
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * すべてのデータストアの資源を解放します。
     * このメソッドはこのクラスの{@link #getDAO(String, String, String)}で取得されたDAOに関連するデータコネクタの{@link DataConnector#release()}を順に呼び出しています。
     *
     * @throws DataConnectException 資源開放に失敗
     */
    public void release() throws DataConnectException {
        Iterator connectorIterator = connectors.values().iterator();
        DataConnector connector;
        DataConnectException exception = null;

        while (connectorIterator.hasNext()) {
            connector = (DataConnector)connectorIterator.next();
            try {
                connector.release();
            } catch (DataConnectException e) {
                exception = e;
            }
        }
        this.connectors.clear();

        if (exception != null) {
            throw exception;
        }
    }
}
