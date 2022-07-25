/*
 * Columns.java
 *
 * Created on 2005/05/17,  13:43:35
 */
package org.intra_mart.data_migration.common.read;

/**
 * 表形式データの列情報を取り扱います。
 * <br>
 * 各列に格納されるデータは文字列として管理します。
 *
 * @author intra-mart
 * 
 */
public interface Columns {
    /**
     * 指定された列番号にあるデータを返します<br>
     * 
     * 列番号は <code>1</code> から始まります。<br>
     * 
     * 指定された列番号が存在しない場合は <code>null</code> を返します。
     * 
     * @param index 列番号
     * @return 値
     */
    public Object getObject(int index);
    /**
     * 指定された列番号にあるデータを {@link String} に変換して返します。<br>
     * 
     * 列番号は <code>1</code> から始まります。<br>
     * 
     * 指定された列番号が存在しない場合は <code>null</code> を返します。
     * 
     * @param index 列番号
     * @return 値
     */
    public String getString(int index);
    /**
     * 保持している列数を返します。
     * 
     * @return 列数
     */
    public int size();
}
