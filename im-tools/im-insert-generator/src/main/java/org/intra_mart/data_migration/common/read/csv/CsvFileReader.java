/*
 * CsvProvider.java
 *
 * Created on 2005/05/17,  13:31:10
 */
package org.intra_mart.data_migration.common.read.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.intra_mart.data_migration.common.read.CreateRowsException;
import org.intra_mart.data_migration.common.read.Rows;
import org.intra_mart.data_migration.common.read.RowsBuilder;


/**
 * CSV形式のファイルを読込んで表形式のデータに変換します。
 *
 * @author intra-mart
 * 
 */
public abstract class CsvFileReader {
    /**
     * 表形式データ生成クラス
     */
    private RowsBuilder builder = new InputFileRowsBuilderImpl();

    /**
     * CSVファイルを読込みます。
     * 
     * @param input 元ファイル
     * @return 表形式データ
     * @throws IOException ファイルの読込みに失敗した場合
     */
    protected Rows read(String input) throws CreateRowsException, IOException {
        File inputFile = new File(input);
        FileInputStream stream = new FileInputStream(inputFile.getAbsolutePath());
        return builder.create(stream);
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
