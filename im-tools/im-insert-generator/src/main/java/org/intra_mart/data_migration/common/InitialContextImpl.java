/*
 * InitialContextImpl.java
 *
 * Created on 2005/05/17,  10:29:18
 */
package org.intra_mart.data_migration.common;

import java.util.logging.Logger;


/**
 * 初期コンテキストを扱います。
 *
 * @author intra-mart
 * 
 */
public class InitialContextImpl implements InitialContext {

    /**
     * 入力ファイル名
     */
    final String input;
    /**
     * 出力ファイル名
     */
    final String output;
    /**
     * ロガー
     */
    final Logger logger;
    
    /**
     * コンストラクタ
     * 
     * @param input 入力ファイル
     * @param output 出力ファイル
     * @param logger ロガー
     */
    public InitialContextImpl(String input, String output, Logger logger) {
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
}
