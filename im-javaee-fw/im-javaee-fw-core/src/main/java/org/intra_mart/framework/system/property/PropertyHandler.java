/*
 * PropertyHandler.java
 *
 * Created on 2001/11/08, 11:04
 */

package org.intra_mart.framework.system.property;

/**
 * プロパティにアクセスするインタフェースです。
 * このインタフェースを実装するすべてのクラスは、引数なしのコンストラクタが必要です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface PropertyHandler {

    /**
     * プロパティハンドラを初期化します。
     *
     * @param params 初期パラメータ
     * @throws PropertyHandlerException プロパティハンドラの初期化時に例外が発生
     */    
    public void init(PropertyParam[] params) throws PropertyHandlerException;
}
