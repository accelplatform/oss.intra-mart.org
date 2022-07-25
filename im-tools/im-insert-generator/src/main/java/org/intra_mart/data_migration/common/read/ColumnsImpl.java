/*
 * ColumnsImpl.java
 *
 * Created on 2005/05/17,  14:08:03
 */
package org.intra_mart.data_migration.common.read;

/**
 * 表形式データの列情報の取り扱いの実装です。
 *
 * @author intra-mart
 * 
 */
public class ColumnsImpl implements Columns {
    /**
     * 列値を格納する配列
     */
    private Object[] values = new Object[0];
    
    /**
     * コンストラクタ
     * 
     * @param values 列情報として設定する値の配列
     */
    public ColumnsImpl(Object[] values) {
        if (values != null) {
            this.values = new Object[values.length];
            System.arraycopy(values, 0, this.values, 0, values.length);
        }
    }
    
    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.util.data.Columns#getObject(int)
     */
    public Object getObject(int index) {
        return (index < 1 || this.values.length < index) ? null : this.values[index - 1];
    }

    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.read.Columns#getString(int)
     */
    public String getString(int index) {
        Object value = this.getObject(index);
        return (value == null) ? null : value.toString();
    }
    
    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.util.data.Columns#size()
     */
    public int size() {
        return this.values.length;
    }
}
