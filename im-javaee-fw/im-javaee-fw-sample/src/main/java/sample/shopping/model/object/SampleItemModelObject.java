package sample.shopping.model.object;

import java.io.Serializable;

/**
 * 注文情報をモデル用に保持するクラスです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class SampleItemModelObject implements Serializable {
    private String code;
    private String name;
    private int price;
    private String simpleNote;
    private String detailNote;
    private String imagePath;

    /**
     * SampleItemModelObjectを初期化します。
     *
     */
    public SampleItemModelObject() {
        setCode(null);
        setName(null);
        setPrice(0);
        setSimpleNote(null);
        setDetailNote(null);
        setImagePath(null);
    }

    /**
     * 商品コードをセットします。
     *
     * @param cd 商品コード
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

    /**
     * 商品名をセットします。
     *
     * @param name 商品名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 商品名を返却します。
     *
     * @return 商品名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 商品の単価をセットします。
     *
     * @param price 商品の単価
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * 商品の単価を返却します。
     *
     * @return 商品の単価
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * 商品の簡単な説明をセットします。
     *
     * @param simple_note 商品の簡単な説明
     */
    public void setSimpleNote(String simpleNote) {
        this.simpleNote = simpleNote;
    }

    /**
     * 商品の簡単な説明を返却します。
     *
     * @return 商品の簡単な説明
     */
    public String getSimpleNote() {
        return this.simpleNote;
    }

    /**
     * 商品の詳細な説明をセットします。
     *
     * @param detail_note 商品の詳細な説明
     */
    public void setDetailNote(String detailNote) {
        this.detailNote = detailNote;
    }

    /**
     * 商品の詳細な説明を返却します。
     *
     * @return 商品の詳細な説明
     */
    public String getDetailNote() {
        return this.detailNote;
    }

    /**
     * 商品の画像パスをセットします。
     *
     * @param image_path 商品の画像パス
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 商品の画像パスを返却します。
     *
     * @return 商品の画像パス
     */
    public String getImagePath() {
        return this.imagePath;
    }
}
