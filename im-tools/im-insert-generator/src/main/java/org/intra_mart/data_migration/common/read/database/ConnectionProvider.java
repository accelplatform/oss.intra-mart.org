/*
 * ConnectionProvider.java
 *
 * Created on 2005/05/17,  13:17:49
 */
package org.intra_mart.data_migration.common.read.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBCのコネクションを取得します。
 *
 * @author intra-mart
 * 
 */
public abstract class ConnectionProvider {

    /**
     * {@link Connection} を返します。
     * 
     * @param input DB接続設定ファイル名
     * @return java.sql.Connection
     * @throws IOException DB接続設定ファイルの取得に失敗した場合
     * @throws ClassNotFoundException JDBCドライバの取得に失敗した場合
     * @throws SQLException コネクションの取得に失敗した場合
     */
    protected Connection getConnection(String input) throws IOException,
            SQLException, ClassNotFoundException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(input));
        
        Connection connection;
        Class.forName(prop.getProperty("jdbc.driver"));
        connection = DriverManager.getConnection(prop.getProperty("jdbc.url"),
                prop.getProperty("jdbc.user"), prop
                        .getProperty("jdbc.password"));
        return connection;
    }
}
