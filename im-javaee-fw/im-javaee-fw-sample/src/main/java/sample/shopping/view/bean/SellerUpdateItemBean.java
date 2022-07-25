/*
 * SellerUpdateItemBean.java
 *
 * Created on 2002/02/26, 19:59
 */

package sample.shopping.view.bean;

import org.intra_mart.framework.base.event.EventException;
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
 * Seller側の商品情報更新画面を表示するヘルパークラスです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class SellerUpdateItemBean extends HelperBean {

    /**
     * 商品コード
     */
    private String code;

    /**
     * 商品情報（オリジナル）
     */
    private SampleItemModelObject modelItem;

    /**
     * 商品情報
     */
    private SampleItemViewObject item;

    /**
     * <CODE>SellerUpdateItemBean/CODE>を構築します。
     *
     * @throws HelperBeanException HelperBean内で例外が発生
     */
    public SellerUpdateItemBean() throws HelperBeanException {
        setCode(null);
        setItem(null);
        this.modelItem = null;
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

    /**
     * 商品情報を格納します。
     *
     * @param viewItem 商品情報
     */
    public void setItem(SampleItemViewObject viewItem) {
        this.item = viewItem;
    }

    /**
     * 商品情報（オリジナル）を返却します。
     *
     * @return 商品情報（オリジナル）
     * @throws HelperBeanException HelperBean内で例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws EventException イベント生成時に例外が発生
     * @throws AlreadyDeletedException 商品はすでに削除済み
     */
    public SampleItemModelObject getModelItem()
        throws
            HelperBeanException,
            EventException,
            SystemException,
            ApplicationException,
            AlreadyDeletedException {

        // 商品が未取得の場合
        if (this.modelItem == null) {
            //更新を行う商品コードを格納
            setCode(getRequest().getParameter("item_cd"));

            //商品情報1件取得イベント作成
            GetItemEvent event =
                (GetItemEvent)createEvent("sample.shopping.conf.shopping", "get_item");

            //更新を行う商品コードをイベントに渡す
            event.setCode(this.code);

            //商品情報1件を格納したイベントリザルトを取得
            GetItemEventResult eventResult =
                (GetItemEventResult)dispatchEvent(event);

            //返却値格納
            this.modelItem = eventResult.getItem();
            if (this.modelItem == null) {
                // 商品がすでに削除されている場合エラー
                AlreadyDeletedException e =
                    new AlreadyDeletedException("指定された商品はすでに削除されています。");
                e.setCode(getCode());
                e.setKey("seller_list");
                throw e;
            }
        }

        return this.modelItem;
    }

    /**
     * 商品情報を返却します。
     *
     * @return 商品情報
     * @throws HelperBeanException HelperBean内で例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws EventException イベント生成時に例外が発生
     */
    public SampleItemViewObject getItem()
        throws
            HelperBeanException,
            ApplicationException,
            SystemException,
            EventException {

        //商品情報群をまだ取得していないときの処理
        if (this.item == null) {
            //商品情報を取得
            SampleItemModelObject modelItem = getModelItem();

            SampleItemViewObject viewItem = new SampleItemViewObject();

            viewItem.setCode(modelItem.getCode());
            viewItem.setName(modelItem.getName());
            viewItem.setSimpleNote(modelItem.getSimpleNote());
            viewItem.setDetailNote(modelItem.getDetailNote());
            viewItem.setImagePath(modelItem.getImagePath());
            try {
                viewItem.setPrice(Integer.toString(modelItem.getPrice()));
            } catch (Exception e) {
                throw new HelperBeanException(e.getMessage(), e);
            }

            setItem(viewItem);
        }

        return this.item;
    }
}
