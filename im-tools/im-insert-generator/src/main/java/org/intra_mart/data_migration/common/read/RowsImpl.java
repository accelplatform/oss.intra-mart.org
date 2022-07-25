/*
 * GenericSheet.java
 *
 * Created on 2005/05/17,  16:06:08
 */
package org.intra_mart.data_migration.common.read;

import java.util.ArrayList;
import java.util.List;

/**
 * 表形式データの行情報の取り扱いの実装です。
 *
 * @author intra-mart
 * 
 */
public class RowsImpl implements Rows {
    /**
     * 行データを格納するリスト
     */
    private List rows = null;

    /**
     * コンストラクタ
     * <br>
     * 引数には列情報 {@link Columns} を要素にもつリストを設定して下さい。
     * 
     * @param list 列情報を要素にもつリスト
     */
    public RowsImpl(List list) {
        this.rows = (list != null) ? list : new ArrayList();
    }

    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.util.data.Rows#getObject(int, int)
     */
    public Object getObject(int rowIndex, int columnIndex) {
        return getColumns(rowIndex).getObject(columnIndex);
    }

    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.util.data.Rows#getString(int, int)
     */
    public String getString(int rowIndex, int columnIndex) {
        Object value = this.getObject(rowIndex, columnIndex);
        return (value == null) ? null : value.toString();
    }

    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.util.data.Rows#getRow(int)
     */
    public Columns getColumns(int index) {
        return (index < 1 || rows.size() < index) ? new ColumnsImpl(null)
                : (Columns) rows.get(index - 1);
    }

    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.util.data.Rows#size()
     */
    public int size() {
        return this.rows.size();
    }
}
