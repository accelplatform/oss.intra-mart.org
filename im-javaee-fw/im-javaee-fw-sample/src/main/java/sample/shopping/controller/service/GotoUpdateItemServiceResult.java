/*
 * GotoUpdateItemServiceResult.java
 *
 * Created on 2002/03/11, 11:20
 */

package sample.shopping.controller.service;

import org.intra_mart.framework.base.service.ServiceResult;

/**
 * 商品情報更新画面表示時のサービスリザルトです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class GotoUpdateItemServiceResult implements ServiceResult {

    /**
     * 商品コード
     */
    private String code;

    /**
     * GotoUpdateItemServiceResultを初期化します。
     *
     */
    public GotoUpdateItemServiceResult() {
        setCode(null);
    }

    /**
     * 商品コードをセットします
     *
     * @param code 商品コード
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 商品コードを返却します
     *
     * @return 商品コード
     */
    public String getCode() {
        return this.code;
    }
}
