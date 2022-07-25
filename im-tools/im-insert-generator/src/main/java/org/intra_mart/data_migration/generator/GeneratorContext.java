package org.intra_mart.data_migration.generator;

import org.intra_mart.data_migration.common.InitialContext;

public interface GeneratorContext extends InitialContext {
    
    /**
     * 出力対象のテーブル名を返します。
     * 
     * @return テーブル名
     */
    public String getTableName();
}
