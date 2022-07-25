package sample.shopping.controller.service;

import org.intra_mart.framework.base.service.ServiceResult;

/**
 * 商品詳細画面表示時のサービスリザルトです。
 *
 * @author NTTDATA intra-mart TY.
 * @version 1.0
 */
public class DetailItemServiceResult implements ServiceResult {

    private String key;
    private String itemCd;

    /**
     * DetailItemServiceResultを初期化します。
     *
     */
    public DetailItemServiceResult() {
        super();
    }

    /**
     * 商品コードをセットします。
     *
     * @param itemCd 商品コード
     */
    public void setItemCd(String itemCd) {
        this.itemCd = itemCd;
    }

    /**
     * 商品コードを返却します。
     *
     * @return 商品コード
     */
    public String getItemCd() {
        return this.itemCd;
    }

    /**
     * 画面戻り時の戻り先画面をあらわすキーをセットします。
     *
     * @param key 画面のキー
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 画面戻り時の戻り先画面をあらわすキーを返却します。
     *
     * @return 画面のキー
     */
    public String getKey() {
        return this.key;
    }
}
