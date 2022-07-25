/*
 * DBDAO.java
 *
 * Created on 2001/11/22, 15:27
 */

package org.intra_mart.framework.base.data;

import java.sql.Connection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * データベースに特化したDAOです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class DBDAO implements DAO {

    /** データコネクタ */
    private DataConnector connector;

    /** リソース */
    private String resource;

    /** コネクション */
    private Connection connection;

    /** デフォルトのコンストラクタ */
    public DBDAO() {
        super();
        this.connector = null;
        this.resource = null;
        this.connection = null;
    }

    /**
     * データリソースに接続する時の情報を設定します。
     *
     * @param connector データコネクタ
     * @param resource リソース
     * @param key キー
     * @param connect 接続情報
     * @throws DataConnectException 接続に失敗
     */
    public void setConnectInfo(DataConnector connector, String resource, String key, String connect) throws DataConnectException {
        if (connector == null) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DBDAO.DataConnectorRequired");
            } catch (MissingResourceException e) {
            }
            throw new DataConnectException(message);
        }
        if (resource == null) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DBDAO.ResourceRequired");
            } catch (MissingResourceException e) {
            }
            throw new DataConnectException(message);
        }
        this.connector = connector;
        this.resource = resource;
    }

    /**
     * このDAOに関連づけられたコネクションを取得します。
     *
     * @return コネクション
     * @throws DataConnectException コネクションの取得に失敗
     * @throws DataPropertyException データプロパティの取得に失敗
     */
    protected Connection getConnection() throws DataConnectException, DataPropertyException {
        if (this.connection == null) {
            this.connection = ((DBConnector)this.connector).getConnection(this.resource);
        }

        return this.connection;
    }
}
