/*
 * Rows.java
 *
 * Created on 2005/05/17,  13:50:00
 */
package org.intra_mart.data_migration.common.read;

/**
 * 表形式データの行情報を取り扱います。
 * <br>
 * 各行には列情報 {@link Columns} が設定されています。
 *
 * @author intra-mart
 * 
 * @see Columns
 */
public interface Rows {
    /**
     * 指定された座標に存在するデータを返します。<br>
     * 
     * 行番号、列番号ともに <code>1</code> から始まります。<br>
     * 
     * 指定された座標が存在しない場合は <code>null</code> を返します。
     * 
     * @param rowIndex 行番号
     * @param columnIndex 列番号
     * @return 値
     */
    public Object getObject(int rowIndex, int columnIndex);

    /**
     * 指定された座標に存在するデータを文字列に変換して返します。<br>
     * 
     * 行番号、列番号ともに <code>1</code> から始まります。<br>
     * 
     * 指定された座標が存在しない場合は <code>null</code> を返します。
     * 
     * @param rowIndex 行番号
     * @param columnIndex 列番号
     * @return 値
     */
    public String getString(int rowIndex, int columnIndex);

    /**
     * 指定された行番号に存在する行データ {@link Columns} を取得します。
     * 
     * @param index 行番号
     * @return 行データ
     */
    public Columns getColumns(int index);

    /**
     * 保持している行数を返します。
     * 
     * @return 行数
     */
    public int size();
}
