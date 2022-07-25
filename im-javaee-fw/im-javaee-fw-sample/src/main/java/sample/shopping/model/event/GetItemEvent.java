/*
 * GetItemEvent.java
 *
 * Created on 2002/02/26, 20:56
 */

package sample.shopping.model.event;

import org.intra_mart.framework.base.event.Event;

/**
 * 商品情報１件取得イベントです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class GetItemEvent extends Event {

    /**
     * 商品コード
     */
    private String code;

    /**
     * 商品コードを格納します。
     *
     * @param code 商品コード
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 商品コードを返却します。
     * @return 商品コード
     */
    public String getCode() {
        return this.code;
    }
}
