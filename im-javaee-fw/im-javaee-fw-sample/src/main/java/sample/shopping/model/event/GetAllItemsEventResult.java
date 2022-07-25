/*
 * GetAllItemsEventResult.java
 *
 * Created on 2002/02/21, 15:24
 */

package sample.shopping.model.event;

import java.util.Collection;

import org.intra_mart.framework.base.event.EventResult;

/**
 * 商品情報全件取得イベントリザルトです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class GetAllItemsEventResult implements EventResult {

    /**
     * 商品情報群
     */
    private Collection items;

    /**
     * <CODE>GetAllItemsEventResult</CODE>を構築します。
     */
    public GetAllItemsEventResult() {
        super();
        setItems(null);
    }

    /**
     * 商品情報群を格納します。
     *
     * @param items 商品情報群
     */
    public void setItems(Collection items) {
        this.items = items;
    }

    /**
     * 商品情報群を返却します。
     *
     * @return 商品情報群
     */
    public Collection getItems() {
        return this.items;
    }
}
