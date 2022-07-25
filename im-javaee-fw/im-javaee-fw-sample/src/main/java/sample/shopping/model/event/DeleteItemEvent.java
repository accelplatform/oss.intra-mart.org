/*
 * DeleteItemEvent.java
 *
 * Created on 2002/02/26, 19:04
 */

package sample.shopping.model.event;

import org.intra_mart.framework.base.event.Event;

/**
 * 商品情報削除イベントです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class DeleteItemEvent extends Event {

    /**
     * 商品コード
     */
    private String code;

    /**
     * <CODE>DeleteItemEvent</CODE>を構築します。
     */
    public DeleteItemEvent() {
        setCode(null);
    }

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
     *
     * @return 商品コード
     */
    public String getCode() {
        return this.code;
    }
}
