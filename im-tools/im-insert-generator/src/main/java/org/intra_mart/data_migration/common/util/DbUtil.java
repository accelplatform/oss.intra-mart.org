/*
 * DbUtil.java
 *
 * Created on 2005/05/17,  18:30:35
 */
package org.intra_mart.data_migration.common.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * 
 * @author intra-mart
 * 
 */
public class DbUtil {
    /**
     * コンストラクタ
     */
    private DbUtil() {
    }

    /**
     * {@link PreparedStatement} を生成します。
     * 
     * @param connection
     *            java.sql.Connection
     * @param sql
     *            SQL文
     * @param parameters
     *            パラメータのリスト
     * @return PreparedStatement
     */
    public static PreparedStatement prepare(Connection connection, String sql,
            List parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        setParameter(statement, parameters);
        return statement;
    }

    /**
     * SQL文で使用するパラメータを設定します。
     * 
     * @param statement
     *            java.sql.PreparedStatement
     * @param parameters
     *            パラメータのリスト
     * @throws SQLException
     */
    public static void setParameter(PreparedStatement statement, List parameters)
            throws SQLException {
        if (parameters == null) {
            return;
        }
        Iterator it = parameters.iterator();
        int column = 1;
        while (it.hasNext()) {
            statement.setObject(column, it.next());
            column++;
        }
    }

    /**
     * 検索を実行して結果をリスト形式にして返します。
     * 
     * @param statement
     *            java.sql.PreparedStatement
     * @return java.sql.ResultSet
     * @throws SQLException
     *             検索に失敗した場合
     */
    public static ResultSet executeQuery(PreparedStatement statement)
            throws SQLException {
        return statement.executeQuery();
    }

    /**
     * {@link Statement} および {@link ResultSet} を終了します。
     * 
     * @param statement
     *            java.sql.Statement
     * @param resultSet
     *            java.sql.ResultSet
     * @throws SQLException
     *             終了に失敗した場合
     */
    public static void close(Statement statement, ResultSet resultSet)
            throws SQLException {
        close(resultSet);
        close(statement);
    }

    /**
     * {@link PreparedStatement} および {@link ResultSet} を終了します。
     * 
     * @param statement
     *            java.sql.PreparedStatement
     * @param resultSet
     *            java.sql.ResultSet
     * @throws SQLException
     *             終了に失敗した場合
     */
    public static void close(PreparedStatement statement, ResultSet resultSet)
            throws SQLException {
        close(resultSet);
        close(statement);
    }
    
    /**
     * {@link Connection} を終了します。
     * 
     * @param connection
     *            java.sql.Connection
     * @throws SQLException
     */
    public static void close(Connection connection)
            throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * {@link Statement} を終了します。
     * 
     * @param statement
     *            java.sql.Statement
     * @throws SQLException
     *             終了に失敗した場合
     */
    public static void close(Statement statement)
            throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }
    
    /**
     * {@link PreparedStatement} を終了します。
     * 
     * @param statement
     *            java.sql.PreparedStatement
     * @throws SQLException
     *             終了に失敗した場合
     */
    public static void close(PreparedStatement statement)
            throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }

    /**
     * {@link ResultSet} を終了します。
     * 
     * @param resultSet
     *            java.sql.ResultSet
     * @throws SQLException
     *             終了に失敗した場合
     */
    public static void close(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
    }
}