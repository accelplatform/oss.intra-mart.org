/*
 * MigrationManager.java
 *
 * Created on 2005/05/16,  15:52:39
 */
package org.intra_mart.data_migration.common;


/**
 * 移行マネージャのインターフェースです。
 * 
 * @author intra-mart
 * 
 */
public interface MigrationManager {
    /**
     * 移行データの生成を行います。
     * 
     * @param context
     *            初期コンテキスト
     * @throws MigrationException
     *             移行データ作成時に何らかの例外が発生した場合
     */
    public void execute(InitialContext context) throws MigrationException;
}