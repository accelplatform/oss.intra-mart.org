package sample.shopping.view.object;

import org.intra_mart.framework.base.web.util.HTMLEncoder;
import sample.shopping.model.object.SampleItemModelObject;
import sample.shopping.util.CurrencyView;

/**
 * 商品情報を画面用に表現するクラスです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class SampleItemViewObject {
    private String code;
    private String name;
    private String price;
    private String simpleNote;
    private String detailNote;
    private String imagePath;

    /**
     * SampleItemViewObjectを初期化します。
     *
     */
    public SampleItemViewObject() {
        setCode(null);
        setName(null);
        setPrice(null);
        setSimpleNote(null);
        setDetailNote(null);
        setImagePath(null);
    }

    /**
     * SampleItemModelObjectのインスタンスをもとに
     * SampleItemViewObjectを初期化します。
     *
     * @param item SampleItemModelObjectのインスタンス
     */
    public SampleItemViewObject(SampleItemModelObject item) {
        setCode(item.getCode());
        setName(item.getName());
        setPrice(Integer.toString(item.getPrice()));
        setSimpleNote(item.getSimpleNote());
        setDetailNote(item.getDetailNote());
        setImagePath(item.getImagePath());
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
     * @param mode 変換モード
     * @return 商品コード
     */
    public String getCode(String mode) {
        return encode(this.code, mode);
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
     * 商品コードを返却します。
     *
     * @return 商品コード
     */
    public String getValueCode() {
        return encode(this.code, "value");
    }

    /**
     * 商品コードを返却します。
     *
     * @return 商品コード
     */
    public String getCaptionCode() {
        return encode(this.code, "caption");
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
     * @param mode 変換モード
     * @return 商品名
     */
    public String getName(String mode) {
        return encode(this.name, mode);
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
     * 商品名を返却します。
     *
     * @return 商品名
     */
    public String getValueName() {
        return encode(this.name, "value");
    }

    /**
     * 商品名を返却します。
     *
     * @return 商品名
     */
    public String getCaptionName() {
        return encode(this.name, "caption");
    }

    /**
     * 商品の単価をセットします。
     *
     * @param price 商品の単価
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * 商品の単価を返却します。
     *
     * @param mode 変換モード
     * @return 商品の単価
     */
    public String getPrice(String mode) {
        return encode(this.price, mode);
    }

    /**
     * 商品の単価を返却します。
     *
     * @return 商品の単価
     */
    public String getPrice() {
        return this.price;
    }

    /**
     * 商品の単価を返却します。
     *
     * @return 商品の単価
     */
    public String getValuePrice() {
        return encode(this.price, "value");
    }

    /**
     * 商品の単価を返却します。
     *
     * @return 商品の単価
     */
    public String getCaptionPrice() {
        return encode(this.price, "caption");
    }

    /**
     * 商品の単価(カンマ区切り)を返却します。
     *
     * @return 商品の単価
     */
    public String getPriceView() {
        return CurrencyView.getString(this.price);
    }

    /**
     * 商品の単価(カンマ区切り)を返却します。
     *
     * @return 商品の単価
     */
    public String getValuePriceView() {
        return encode(CurrencyView.getString(this.price), "value");
    }

    /**
     * 商品の単価(カンマ区切り)を返却します。
     *
     * @return 商品の単価
     */
    public String getCaptionPriceView() {
        return encode(CurrencyView.getString(this.price), "caption");
    }

    /**
     * 商品の単価(カンマ区切り)を返却します。
     *
     * @param mode 変換モード
     * @return 商品の単価
     */
    public String getPriceView(String mode) {
        return encode(CurrencyView.getString(this.price), mode);
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
     * @param mode 変換モード
     * @return 商品の簡単な説明
     */
    public String getSimpleNote(String mode) {
        return encode(this.simpleNote, mode);
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
     * 商品の簡単な説明を返却します。
     *
     * @return 商品の簡単な説明
     */
    public String getValueSimpleNote() {
        return encode(this.simpleNote, "value");
    }

    /**
     * 商品の簡単な説明を返却します。
     *
     * @return 商品の簡単な説明
     */
    public String getCaptionSimpleNote() {
        return encode(this.simpleNote, "caption");
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
     * @param mode 変換モード
     * @return 商品の詳細な説明
     */
    public String getDetailNote(String mode) {
        return encode(this.detailNote, mode);
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
     * 商品の詳細な説明を返却します。
     *
     * @return 商品の詳細な説明
     */
    public String getValueDetailNote() {
        return encode(this.detailNote, "value");
    }

    /**
    * 商品の詳細な説明を返却します。
    *
    * @return 商品の詳細な説明
    */
    public String getCaptionDetailNote() {
        return encode(this.detailNote, "caption");
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
     * @param mode 変換モード
     * @return 商品の画像パス
     */
    public String getImagePath(String mode) {
        return encode(this.imagePath, mode);
    }

    /**
     * 商品の画像パスを返却します。
     *
     * @return 商品の画像パス
     */
    public String getImagePath() {
        return this.imagePath;
    }

    /**
     * 商品の画像パスを返却します。
     *
     * @return 商品の画像パス
     */
    public String getValueImagePath() {
        return encode(this.imagePath, "value");
    }

    /**
     * 商品の画像パスを返却します。
     *
     * @return 商品の画像パス
     */
    public String getCaptionImagePath() {
        return encode(this.imagePath, "caption");
    }

    /**
     * 文字列をHTML変換するメソッド
     *
     * @param data 変換対象文字列
     * @param mode 変換する時のモード<BR>
     *             caption: キャプションモード<BR>
     *             value: value属性に入る値
     * @return 変換された文字列
     */
    private String encode(String data, String mode) {
        if (mode.equals("caption")) {
            data = HTMLEncoder.encodeCaption(data);
        } else if (mode.equals("value")) {
            data = HTMLEncoder.encodeValue(data);
        }

        return data;
    }
}
