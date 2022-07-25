/*
 * GetItemEventResult.java
 *
 * Created on 2002/02/26, 21:07
 */

package sample.shopping.model.event;

import org.intra_mart.framework.base.event.EventResult;
import sample.shopping.model.object.SampleItemModelObject;

/**
 * 商品情報1件取得イベントリザルトです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class GetItemEventResult implements EventResult {

    /**
     * 商品情報
     */
    SampleItemModelObject item;

    /**
     * <CODE>GetItemEventResult</CODE>を構築します。
     */
    public GetItemEventResult() {
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
