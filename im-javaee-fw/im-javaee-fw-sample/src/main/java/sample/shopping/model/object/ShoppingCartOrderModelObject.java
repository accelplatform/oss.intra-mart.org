/*
 * ShoppingCartOrderModelObject.java
 *
 * Created on 2002/08/30, 12:00
 */

package sample.shopping.model.object;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * ワークフローに申請する申請情報のモデルオブジェクトです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class ShoppingCartOrderModelObject implements Serializable {

    /**
     * ログインユーザID
     */
    private String loginUserID;

    /**
     * ログイングループID
     */
    private String loginGroupID;

    /**
     * 注文明細
     */
    private Collection lineItems;

    /**
     * 顧客情報
     */
    private ShoppingCartCustomerModelObject customer;

    /**
     * 支払方法
     */
    private String payMethod;

    /**
     * ShoppingCartOrderModelObjectを新規に生成します。
     */
    public ShoppingCartOrderModelObject() {
        setLoginUserID(null);
        setLoginGroupID(null);
        setLineItems(null);
    }

    /**
     * ログインユーザIDを設定します。
     *
     * @param loginUserID ログインユーザID
     */
    public void setLoginUserID(String loginUserID) {
        this.loginUserID = loginUserID;
    }

    /**
     * ログインユーザIDを取得します。
     *
     * @return ログインユーザID
     */
    public String getLoginUserID() {
        return this.loginUserID;
    }

    /**
     * ログイングループIDを設定します。
     *
     * @param loginGroupID ログイングループID
     */
    public void setLoginGroupID(String loginGroupID) {
        this.loginGroupID = loginGroupID;
    }

    /**
     * ログイングループIDを取得します。
     *
     * @return ログイングループID
     */
    public String getLoginGroupID() {
        return this.loginGroupID;
    }

    /**
     * 注文明細を設定します。
     *
     * @param lineItems 注文明細
     */
    public void setLineItems(Collection lineItems) {
        this.lineItems = lineItems;
    }

    /**
     * 注文明細を取得します。
     *
     * @return 注文明細
     */
    public Collection getLineItems() {
        return this.lineItems;
    }

    /**
     * 合計金額を取得します。
     *
     * @return 合計金額
     */
    public int getTotalPrice() {
        Iterator items = null;
        ShoppingCartLineItemModelObject lineItem = null;
        int totalPrice = 0;

        if (getLineItems() == null) {
            totalPrice = 0;
        } else {
            items = getLineItems().iterator();
            while (items.hasNext()) {
                lineItem = (ShoppingCartLineItemModelObject)items.next();
                totalPrice += lineItem.getLinePrice();
            }
        }

        return totalPrice;
    }

    /**
     * 顧客情報を設定します。
     *
     * @param customer 顧客情報
     */
    public void setCustomer(ShoppingCartCustomerModelObject customer) {
        this.customer = customer;
    }

    /**
     * 顧客情報を取得します。
     *
     * @return 顧客情報
     */
    public ShoppingCartCustomerModelObject getCustomer() {
        return this.customer;
    }

    /**
     * 支払方法を設定します。
     *
     * @param payMethod 支払方法
     */
    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    /**
     * 支払方法を取得します。
     *
     * @return 支払方法
     */
    public String getPayMethod() {
        return this.payMethod;
    }
}
