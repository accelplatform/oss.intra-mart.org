/*
 * JDBCConnector.java
 *
 * Created on 2001/11/21, 19:14
 */

package org.intra_mart.framework.base.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.log.LogManager;
import org.intra_mart.framework.system.log.LogConstant;

import java.sql.SQLException;
import java.util.MissingResourceException;

/**
 * JDBCを利用するデータコネクタです。
 *
 * @author INTRAMART
 * @version 1.0
 * @deprecated JDBCを直接扱わず、DataSourceを使用するようにしてください。
 * @see DataSourceConnector
 */
public class JDBCConnector extends DBConnector {

    /** データベースドライバのキー */
    public static final String KEY_DRIVER = "driver";

    /** データベースURLのキー */
    public static final String KEY_URL = "url";

    /** データベース接続ユーザ名のキー */
    public static final String KEY_USERNAME = "username";

    /** データベース接続ユーザパスワードのキー */
    public static final String KEY_PASSWORD = "password";

    /**
     * 新規のJDBCConnectorを生成します。
     */
    public JDBCConnector() {
        super();
    }

    /**
     * コミットします。
     *
     * @throws DataConnectException コミットに失敗
     */
    public void commit() throws DataConnectException {
        Iterator connections;
        Connection connection;
        DataConnectException exception = null;

        if (this.resources != null) {
            connections = this.resources.values().iterator();
            while (connections.hasNext()) {
                connection = (Connection)connections.next();
                try {
                    connection.commit();
                } catch (Exception e) {
                    LogManager.getLogManager().getLogAgent().sendMessage(
                        JDBCConnector.class.getName(),
                        LogConstant.LEVEL_ERROR,
                        DataManager.LOG_HEAD + e.getMessage(),
                        e);
                    String message = null;
                    try {
                        message =
                            ResourceBundle.getBundle(
                                "org.intra_mart.framework.base.data.i18n")
                                    .getString(
                                "Common.FailedToCommit");
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
     * ロールバックします。
     *
     * @throws DataConnectException ロールバックに失敗
     */
    public void rollback() throws DataConnectException {
        Iterator connections;
        Connection connection;
        DataConnectException exception = null;

        if (this.resources != null) {
            connections = this.resources.values().iterator();
            while (connections.hasNext()) {
                connection = (Connection)connections.next();
                try {
                    connection.rollback();
                } catch (Exception e) {
                    LogManager.getLogManager().getLogAgent().sendMessage(
                        JDBCConnector.class.getName(),
                        LogConstant.LEVEL_ERROR,
                        DataManager.LOG_HEAD + e.getMessage(),
                        e);
                    String message = null;
                    try {
                        message =
                            ResourceBundle.getBundle(
                                "org.intra_mart.framework.base.data.i18n")
                                    .getString(
                                "Common.FailedToRollBack");
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
                        JDBCConnector.class.getName(),
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
     * @param resource リソース
     * @param params リソースのパラメータ
     * @throws DataConnectException 接続に失敗
     * @return リソース
     */
    protected Connection putResource(String resource, ResourceParam[] params)
        throws DataConnectException {

        String key;
        String value;
        String driver = null;
        String url = null;
        String username = null;
        String password = null;
        Connection connection = null;

        // パラメータの取得
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                key = params[i].getName();
                value = params[i].getValue();
                if (key.equals(KEY_DRIVER)) {
                    // ドライバの場合
                    driver = value;
                } else if (key.equals(KEY_URL)) {
                    // URLの場合
                    url = value;
                } else if (key.equals(KEY_USERNAME)) {
                    // ユーザ名の場合
                    username = value;
                } else if (key.equals(KEY_PASSWORD)) {
                    // パスワードの場合
                    password = value;
                }
            }
        }

        // コネクションの追加
        try {
            Class.forName(driver);
        } catch (Exception e) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "JDBCConnector.FailedToLoadJDBCDriver");
            } catch (MissingResourceException ex) {
            }
            throw new DataConnectException(
                message + " : driver class = " + driver,
                e);
        }
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "Common.FailedToGetConnection");
            } catch (MissingResourceException ex) {
            }
            throw new DataConnectException(message + " : url = " + url, e);
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "JDBCConnector.FailedToSetAutoCommit");
            } catch (MissingResourceException ex) {
            }
            throw new DataConnectException(message + " : url = " + url, e);
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
                message + " : url = " + url + ", resource = " + resource,
                e);
        }

        return connection;
    }
}
