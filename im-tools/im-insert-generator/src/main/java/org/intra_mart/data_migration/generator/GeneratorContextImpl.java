package org.intra_mart.data_migration.generator;

import java.util.logging.Logger;

public class GeneratorContextImpl implements GeneratorContext {

    /**
     * テーブル名
     */
	private String tableName = null;
    /**
     * 入力ファイル名
     */
	private String input = null;
    /**
     * 出力ディレクトリ名
     */
	private String output = null;
    /**
     * ロガー
     */
	private Logger logger = null;
    
    /**
     * コンストラクタ
     * 
     * @param tableName テーブル名
     * @param input 入力ファイル
     * @param output 出力ディレクトリ
     * @param logger ロガー
     */
    public GeneratorContextImpl(String tableName, String input, String output, Logger logger) {
    	this.tableName = tableName;
        this.input = input;
        this.output = output;
        this.logger = logger;
    }
    
    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.InitialContext#getInput()
     */
    public String getInput() {
        return this.input;
    }

    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.InitialContext#getOutput()
     */
    public String getOutput() {
        return this.output;
    }

    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.InitialContext#getLogger()
     */
    public Logger getLogger() {
        return this.logger;
    }

    /* (非 Javadoc)
     * @see jp.co.intra_mart.data_migration.common.GenaratorContext#getTableName()
     */
	public String getTableName() {
        return this.tableName;
	}
}
