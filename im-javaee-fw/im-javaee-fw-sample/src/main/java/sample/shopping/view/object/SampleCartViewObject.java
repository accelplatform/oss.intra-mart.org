package sample.shopping.view.object;

import sample.shopping.model.object.SampleItemModelObject;
import sample.shopping.util.CurrencyView;

/**
 * カート情報を画面用に保持するクラスです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class SampleCartViewObject {
    private SampleItemViewObject viewItem;
    private SampleItemModelObject modelItem;
    private int amount;
    private long sumPrice;
    private boolean isValid;

    /**
     * SampleCartViewObjectを初期化します。
     */
    public SampleCartViewObject() {
        setItem(null);
        setAmount(0);
        setSumPrice(0);
    }

    /**
     * 商品情報をセットします。
     *
     * @param item 商品情報
     */
    public void setItem(SampleItemModelObject item) {
        this.modelItem = item;

        if (this.modelItem != null) {
            this.viewItem = new SampleItemViewObject(item);

            int price = this.modelItem.getPrice();
            setSumPrice(price * this.amount);
        }
    }

    /**
     * 商品情報を返却します。
     *
     * @return 商品情報
     */
    public SampleItemViewObject getItem() {
        return this.viewItem;
    }

    /**
     * 商品の個数をセットします。
     *
     * @pram amount 商品の個数
     */
    public void setAmount(int amount) {
        this.amount = amount;

        if (this.modelItem != null) {
            int price = this.modelItem.getPrice();
            setSumPrice(price * this.amount);
        }
    }

    /**
     * 商品の個数を返却します。
     *
     * @return 商品の個数
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * 商品の合計金額を返却します(単価×個数)。
     *
     * @return 商品の合計金額
     */
    public String getSumPrice() {
        return Long.toString(this.sumPrice);
    }

    public String getSumPriceView() {
        return CurrencyView.getString(Long.toString(this.sumPrice));
    }

    public void setIsValid(boolean flg) {
        this.isValid = flg;
    }

    public boolean getIsValid() {
        return this.isValid;
    }

    /**
     * 商品の合計金額をセットします。
     *
     * @param sumPrice 商品の合計金額
     */
    private void setSumPrice(long sumPrice) {
        this.sumPrice = sumPrice;
    }
}
