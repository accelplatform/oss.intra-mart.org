/*
 * InsertItemEvent.java
 *
 * Created on 2002/02/26, 17:07
 */

package sample.shopping.model.event;

import org.intra_mart.framework.base.event.Event;
import sample.shopping.model.object.SampleItemModelObject;

/**
 * 商品情報登録イベントです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class InsertItemEvent extends Event {

    /**
     * 商品情報
     */
    private SampleItemModelObject item;

    /**
     * <CODE>InsertItemEvent</CODE>を構築します。
     */
    public InsertItemEvent() {
        super();
        setItem(null);
    }

    /**
     * 商品情報を格納します。
     *
     * @param item 商品情報
     */
    public void setItem(SampleItemModelObject item) {
        this.item = item;
    }

    /**
     * 商品情報を返却します。
     *
     * @return 商品情報
     */
    public SampleItemModelObject getItem() {
        return this.item;
    }
}
