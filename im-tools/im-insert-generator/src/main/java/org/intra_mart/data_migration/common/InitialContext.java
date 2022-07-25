/*
 * InitialContext.java
 *
 * Created on 2005/05/17,  9:59:29
 */
package org.intra_mart.data_migration.common;

import java.util.logging.Logger;

/**
 * 初期コンテキストを管理します。
 *
 * @author intra-mart
 * 
 */
public interface InitialContext {
    
    /**
     * 入力ファイルを返します。
     * 
     * @return 入力ファイル
     */
    public String getInput();
    
    /**
     * 出力ファイルを返します。
     * 
     * @return 出力ファイル
     */
    public String getOutput();
    
    /**
     * ロガーを返します。
     * 
     * @return ロガー
     */
    public Logger getLogger();

}
