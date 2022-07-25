/*
 * Genarator.java
 *
 * Created on 2006/03/07,  15:52:39
 */
package org.intra_mart.data_migration.generator;

import org.intra_mart.data_migration.common.MigrationException;


/**
 * データ生成オブジェクトのインターフェースです。
 * 
 * @author intra-mart
 * 
 */
public interface Generator {
	
    /**
     * SQLデータの生成を行います。
     * 
     * @param context 初期コンテキスト
     * @throws MigrationException 移行データ作成時に何らかの例外が発生した場合
     */
    public void execute(GeneratorContext context) throws GenerateException;
}