/*
 * ShoppingCartLineItemModelObject.java
 *
 * Created on 2002/08/30, 12:00
 */

package sample.shopping.model.object;

import java.io.Serializable;

/**
 * ショッピングカート内の商品明細のモデルオブジェクトです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class ShoppingCartLineItemModelObject implements Serializable {

    /**
     * 商品ID
     */
    private String itemID;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品単価
     */
    private int unitPrice;

    /**
     * 数量
     */
    private int quantity;

    /**
     * ShoppingCartLineItemModelObjectを新規に生成します。
     */
    public ShoppingCartLineItemModelObject() {
        setItemID(null);
        setItemName(null);
        setUnitPrice(0);
        setQuantity(0);
    }

    /**
     * 商品IDを設定します。
     *
     * @param itemID 商品ID
     */
    public void setItemID(java.lang.String itemID) {
        this.itemID = itemID;
    }

    /**
     * 商品IDを取得します。
     *
     * @return 商品ID
     */
    public java.lang.String getItemID() {
        return this.itemID;
    }

    /**
     * 商品名を設定します。
     *
     * @param itemName 商品名
     */
    public void setItemName(java.lang.String itemName) {
        this.itemName = itemName;
    }

    /**
     * 商品名を取得します。
     *
     * @return 商品名
     */
    public java.lang.String getItemName() {
        return this.itemName;
    }

    /**
     * 数量を設定します。
     *
     * @param quantity 数量
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * 数量を取得します。
     *
     * @return 数量
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * 商品単価を設定します。
     *
     * @param unitPrice 商品単価
     */
    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 商品単価を取得します。
     *
     * @return 商品単価
     */
    public int getUnitPrice() {
        return this.unitPrice;
    }

    /**
     * 明細金額を取得します。
     *
     * @return 明細金額
     */
    public int getLinePrice() {
        return getUnitPrice() * getQuantity();
    }
}
