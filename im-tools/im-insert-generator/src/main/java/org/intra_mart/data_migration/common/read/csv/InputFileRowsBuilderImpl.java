/*
 * FileSheet.java
 *
 * Created on 2005/05/17,  14:07:43
 */
package org.intra_mart.data_migration.common.read.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.intra_mart.data_migration.common.read.ColumnsImpl;
import org.intra_mart.data_migration.common.read.CreateRowsException;
import org.intra_mart.data_migration.common.read.Rows;
import org.intra_mart.data_migration.common.read.RowsBuilder;
import org.intra_mart.data_migration.common.read.RowsImpl;


/**
 * CSV形式のファイルから表形式データを生成します。
 * 
 * @author intra-mart
 * 
 */
public class InputFileRowsBuilderImpl implements RowsBuilder {

    /**
     * コンストラクタ
     */
    public InputFileRowsBuilderImpl() {
    }

    /**
     * 指定されたデータソースから表形式データを作成します。<br>
     * 
     * パラメータには {@link InputStream} を指定して下さい。
     * 
     * @param source データソース
     * @return 表形式データ
     */
    public Rows create(Object source) throws CreateRowsException {
        BufferedReader reader = null; 
        InputStreamReader input = null;
        try {
            InputStream stream = (InputStream) source;
            input = new InputStreamReader(stream);
            reader = new BufferedReader(input);
            List rows = new ArrayList();
            String line;
            while ((line = reader.readLine()) != null) {
                int from = 0;
                int to = 0;
                List list = new ArrayList();
                // 単純にカンマ区切りで読込む
                while ((to = line.indexOf('\t', from)) != -1) {
                    list.add(line.substring(from, to));
                    from = to + 1;
                }
                list.add(line.substring(from));
                rows.add(new ColumnsImpl((String[]) list.toArray(new String[list.size()])));
            }
            return new RowsImpl(rows);
        } catch (IOException e) {
            throw new CreateRowsException(e);
        } finally {
            try {
                reader.close();
                input.close();
            } catch (IOException e) {
            }
        }
    }
}