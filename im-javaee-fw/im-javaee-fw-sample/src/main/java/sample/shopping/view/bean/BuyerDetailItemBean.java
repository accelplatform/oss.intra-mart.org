package sample.shopping.view.bean;

import java.io.Serializable;

import org.intra_mart.framework.base.web.bean.HelperBean;
import org.intra_mart.framework.base.web.bean.HelperBeanException;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import sample.shopping.model.event.AlreadyDeletedException;
import sample.shopping.model.event.GetItemEvent;
import sample.shopping.model.event.GetItemEventResult;
import sample.shopping.model.object.SampleItemModelObject;
import sample.shopping.view.object.SampleItemViewObject;

/**
 * 商品詳細画面表示用のBeanです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class BuyerDetailItemBean extends HelperBean implements Serializable {
    private String itemCd;
    private String key;
    private SampleItemViewObject viewItem;
    private SampleItemViewObject strutsViewItem;

    /**
     * BuyerDetailItemBeanを新規に生成します。
     *
     * @throws HelperBeanException Beanの初期化時に例外発生
     */
    public BuyerDetailItemBean() throws HelperBeanException {
        itemCd = null;
        viewItem = null;
        strutsViewItem = null;
    }

    /**
     * BuyerDetailItemBeanを初期化します。
     *
     * @throws HelperBeanException 初期化時に例外が発生
     */
    public void init() throws HelperBeanException {
        setItemCd((String)getRequest().getAttribute("item_cd"));
        setKey((String)getRequest().getAttribute("key"));
    }

    /**
     * 商品コードをセットします。
     *
     * @param itemCd 商品コード
     */
    private void setItemCd(String itemCd) {
        this.itemCd = itemCd;
    }

    /**
     * 商品コードを取得します。
     *
     * @return 商品コード
     */
    public String getItemCd() {
        return this.itemCd;
    }

    /**
     * 戻り先のページのキーをセットします。
     *
     * @param key 戻り先のページのキー
     */
    private void setKey(String key) {
        this.key = key;
    }

    /**
     * 戻り先のページのキーを取得します。
     *
     * @return 戻り先のページのキー
     */
    public String getKey() {
        return this.key;
    }

    /**
     * 商品コードから商品情報を取得します。
     *
     * @return 表示用の商品情報
     * @throws ApplicationException 商品情報取得時にアプリケーション例外が発生
     * @throws SystemException 商品情報取得時にシステム例外が発生
     */
    public SampleItemViewObject getItem()
        throws ApplicationException, SystemException {

        if (viewItem == null) {
            // GetSelectedItemEventを生成
            GetItemEvent event =
                (GetItemEvent)createEvent("sample.shopping.conf.shopping", "get_item");

            // 商品コードをセット
            event.setCode(this.itemCd);

            // イベント実行
            GetItemEventResult result =
                (GetItemEventResult)dispatchEvent(event);

            // イベント処理結果から商品情報を取得
            SampleItemModelObject modelItem = result.getItem();
            if (modelItem == null) {
                // 商品がすでに削除されている場合エラー
                AlreadyDeletedException e =
                    new AlreadyDeletedException("指定された商品はすでに削除されています。");
                e.setCode(getItemCd());
                e.setKey(getKey());
                throw e;
            }

            this.viewItem = new SampleItemViewObject(modelItem);
        }

        return this.viewItem;
    }

    /**
     * 商品コードから商品情報を取得します。(Struts)
     *
     * @return 表示用の商品情報
     * @throws ApplicationException 商品情報取得時にアプリケーション例外が発生
     * @throws SystemException 商品情報取得時にシステム例外が発生
     */
    public SampleItemViewObject getStrutsItem()
        throws ApplicationException, SystemException {

        if (strutsViewItem == null) {
            // GetSelectedItemEventを生成
            GetItemEvent event =
                (GetItemEvent)createEvent("sample.shopping.conf.shopping", "get_item");

            // 商品コードをセット
            event.setCode(this.itemCd);

            // イベント実行
            GetItemEventResult result =
                (GetItemEventResult)dispatchEvent(event);

            // イベント処理結果から商品情報を取得
            SampleItemModelObject modelItem = result.getItem();
            if (modelItem == null) {
                // 商品がすでに削除されている場合エラー
                AlreadyDeletedException e =
                    new AlreadyDeletedException("指定された商品はすでに削除されています。");
                e.setCode(getItemCd());
                e.setKey(getKey());
                throw e;
            }

            this.strutsViewItem = new SampleItemViewObject(modelItem);
            this.strutsViewItem.setImagePath(
                getRequest().getContextPath()
                    + "/"
                    + this.strutsViewItem.getImagePath());
        }

        return this.strutsViewItem;
    }
}
