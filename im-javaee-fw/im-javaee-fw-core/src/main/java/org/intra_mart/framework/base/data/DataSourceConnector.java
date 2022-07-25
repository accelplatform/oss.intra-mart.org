/*
 * DataSourceConnector.java
 *
 * Created on 2002/03/19, 11:37
 */

package org.intra_mart.framework.base.data;

import java.sql.Connection;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.naming.InitialContext;

import org.intra_mart.framework.system.log.LogManager;
import org.intra_mart.framework.system.log.LogConstant;

import java.sql.SQLException;
import java.util.MissingResourceException;
import javax.naming.NamingException;

/**
 * DataSourceに関連するデータコネクタです。
 * このデータコネクタはJava Transaction API(JTA)のUserTransactionの中で使うことが前提となっています。
 * また、{@link #KEY_JNDI_NAME}で指定されるパラメータにDataSourceのJNDI名を必ず指定してください。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DataSourceConnector extends DBConnector {

    /**
     * DataSourceのJNDI名のキー
     */
    public static final String KEY_JNDI_NAME = "jndi";

    /**
     * DataSourceConnectorを新規に生成します。
     */
    public DataSourceConnector() {
        super();
    }

    /**
     * コミットします。
     * 実際にはこのクラスではこのメソッドは何もしません。
     *
     * @throws DataConnectException コミットに失敗
     */
    public void commit() throws DataConnectException {
        super.commit();
    }

    /**
     * ロールバックします。
     * 実際にはこのクラスではこのメソッドは何もしません。
     *
     * @throws DataConnectException ロールバックに失敗
     */
    public void rollback() throws DataConnectException {
        super.rollback();
    }

    /**
     * データストアの資源を解放します。
     *
     * @throws DataConnectException 資源開放に失敗
     */
    public void release() throws DataConnectException {
        Iterator connections;
        Connection connection;
        DataConnectException exception = null;

        if (this.resources != null) {
            connections = this.resources.values().iterator();
            while (connections.hasNext()) {
                connection = (Connection)connections.next();
                try {
                    connection.close();
                } catch (Exception e) {
                    LogManager.getLogManager().getLogAgent().sendMessage(
                        DataSourceConnector.class.getName(),
                        LogConstant.LEVEL_ERROR,
                        DataManager.LOG_HEAD + e.getMessage(),
                        e);
                    String message = null;
                    try {
                        message =
                            ResourceBundle.getBundle(
                                "org.intra_mart.framework.base.data.i18n")
                                    .getString(
                                "Common.FailedToReleaseResource");
                    } catch (MissingResourceException ex) {
                    }
                    exception = new DataConnectException(message, e);
                }
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * 接続先のリソースを追加します。
     *
     * @return コネクション
     * @param resource リソース
     * @param params リソースのパラメータ
     * @throws DataConnectException 接続に失敗 */
    protected Connection putResource(String resource, ResourceParam[] params)
        throws DataConnectException {
        String key;
        String value;
        String jndiName = null;
        Connection connection = null;
        InitialContext context = null;
        DataSource dataSource = null;

        // パラメータの取得
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                key = params[i].getName();
                value = params[i].getValue();
                if (key.equals(KEY_JNDI_NAME)) {
                    // DataSourceのJNDI名の場合
                    jndiName = value;
                }
            }
        }
        if (jndiName == null || jndiName.trim().equals("")) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "DataSourceConnector.LookupNameNotDeclared");
            } catch (MissingResourceException e) {
            }
            throw new DataConnectException(
                message + " : resource = " + resource);
        }

        // コネクションの追加
        try {
            context = new InitialContext();
        } catch (NamingException e) {
            throw new DataConnectException(e.getMessage(), e);
        }
        try {
            dataSource = (DataSource)context.lookup(jndiName);
        } catch (NamingException e) {
            throw new DataConnectException(e.getMessage(), e);
        }
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataConnectException(e.getMessage(), e);
        }
        try {
            this.resources.put(resource, connection);
        } catch (Exception e) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "Common.FailedToAddConnection");
            } catch (MissingResourceException ex) {
            }
            throw new DataConnectException(
                message + " : resource = " + resource,
                e);
        }

        return connection;
    }
}
