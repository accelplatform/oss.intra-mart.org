/*
 * ResultSetSheet.java
 *
 * Created on 2005/05/17,  16:38:34
 */
package org.intra_mart.data_migration.common.read.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.intra_mart.data_migration.common.read.ColumnsImpl;
import org.intra_mart.data_migration.common.read.CreateRowsException;
import org.intra_mart.data_migration.common.read.Rows;
import org.intra_mart.data_migration.common.read.RowsBuilder;
import org.intra_mart.data_migration.common.read.RowsImpl;


/**
 * {@link ResultSet} から表形式データを生成します。
 *
 * @author intra-mart
 * 
 */
public class ResultSetRowsBuilderImpl implements RowsBuilder {
    /**
     * コンストラクタ
     */
    ResultSetRowsBuilderImpl() {
    }

    /**
     * 指定されたデータソースから表形式データを作成します。<br>
     * 
     * パラメータには {@link java.sql.ResultSet} を指定して下さい。
     * 
     * @param source データソース
     * @return 表形式データ
     */
    public Rows create(Object source) throws CreateRowsException {
        try {
            ResultSet resultSet = (ResultSet) source;
            List rows = new ArrayList();
            ResultSetMetaData meta = resultSet.getMetaData();
            while (resultSet.next()) {
                List list = new ArrayList();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    Object value = resultSet.getObject(i);
                    list.add(value);
                }
                rows.add(new ColumnsImpl((Object[]) list.toArray(new Object[list.size()])));
            }
            return new RowsImpl(rows);
        } catch (Exception e) {
            throw new CreateRowsException(e);
        }
    }
}
