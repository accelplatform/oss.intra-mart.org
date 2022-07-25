/*
 * DatabaseReader.java
 *
 * Created on 2005/05/17,  18:26:58
 */
package org.intra_mart.data_migration.common.read.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.intra_mart.data_migration.common.read.CreateRowsException;
import org.intra_mart.data_migration.common.read.Rows;
import org.intra_mart.data_migration.common.read.RowsBuilder;

import org.intra_mart.data_migration.common.util.DbUtil;

/**
 * データベースから表形式データを生成します。
 * 
 * @author intra-mart
 * 
 */
public abstract class DatabaseReader extends ConnectionProvider {
    /**
     * 表形式データ生成クラス
     */
    private RowsBuilder builder = new ResultSetRowsBuilderImpl();
    
    /**
     * データベースを読込みます。
     * 
     * @param connection
     *            JDBCコネクション
     * @param sql
     *            SQL文
     * @param parameters
     *            SQLに設定する検索パラメータ
     * @return 表形式のデータ
     * @throws CreateRowsException 表形式データの生成に失敗した場合
     * @throws SQLException データベースで何らかの例外が発生した場合
     */
    protected Rows read(Connection connection, String sql, List parameters)
            throws CreateRowsException, SQLException {

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = DbUtil.prepare(connection, sql, parameters);
            resultSet = DbUtil.executeQuery(statement);
            return builder.create(resultSet);
        } finally {
            DbUtil.close(statement, resultSet);
        }
    }
    
    /**
     * {@link RowsBuilder} を設定します。
     * 
     * @param builder RowsBuilder
     */
    protected void setRowsBuilder(RowsBuilder builder) {
        this.builder = builder;
    }
}