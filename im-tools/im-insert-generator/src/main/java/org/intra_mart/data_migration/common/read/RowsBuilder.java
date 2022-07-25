/*
 * RowsBuilder.java
 *
 * Created on 2005/05/20,  13:29:00
 */
package org.intra_mart.data_migration.common.read;

/**
 * 表形式データの生成を行います。
 *
 * @author intra-mart
 * 
 */
public interface RowsBuilder {
    /**
     * 指定されたデータソースから表形式データを生成します。<br>
     * 
     * @param source データソース
     * @return 表形式データ
     * @throws CreateRowsException 表形式データの生成に失敗した場合
     */
    public Rows create(Object source) throws CreateRowsException;
}
