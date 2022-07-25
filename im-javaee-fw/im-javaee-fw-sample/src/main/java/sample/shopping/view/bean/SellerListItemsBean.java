/*
 * SellerListItemsBean.java
 *
 * Created on 2002/02/21, 13:55
 */

package sample.shopping.view.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventException;
import org.intra_mart.framework.base.web.bean.HelperBean;
import org.intra_mart.framework.base.web.bean.HelperBeanException;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import sample.shopping.model.event.GetAllItemsEventResult;
import sample.shopping.model.object.SampleItemModelObject;
import sample.shopping.view.object.SampleItemViewObject;

/**
 * Seller側の商品一覧画面を表示するヘルパークラスです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class SellerListItemsBean extends HelperBean {

    /**
     * 商品情報群
     */
    private Collection items;

    /**
     * <CODE>SellerListItemsBean</CODE>を構築します。
     *
     * @throws HelperBeanException HelperBean内で例外が発生
     */
    public SellerListItemsBean() throws HelperBeanException {
        setItems(new ArrayList());
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
     * 商品情報群を取得します。
     *
     * @throws HelperBeanException HelperBean内で例外が発生
     * @throws EventException イベント生成時に例外が発生
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     * @return 商品情報群
     */
    public Collection getItems()
        throws
            HelperBeanException,
            EventException,
            SystemException,
            ApplicationException {

        //商品情報群をまだ取得していないときの処理
        if (this.items.isEmpty()) {
            //商品情報全件取得イベント作成
            Event event = createEvent("sample.shopping.conf.shopping", "get_all_items");

            //商品情報全件を格納したイベントリザルトを取得
            GetAllItemsEventResult eventResult =
                (GetAllItemsEventResult)dispatchEvent(event);

            //返却値格納
            Iterator iterator = eventResult.getItems().iterator();
            while (iterator.hasNext()) {
                SampleItemModelObject modelItem =
                    (SampleItemModelObject)iterator.next();
                SampleItemViewObject viewItem = new SampleItemViewObject();

                viewItem.setCode(modelItem.getCode());
                viewItem.setName(modelItem.getName());
                viewItem.setPrice(Integer.toString(modelItem.getPrice()));
                viewItem.setImagePath(modelItem.getImagePath());

                if (modelItem.getSimpleNote() == null) {
                    viewItem.setSimpleNote("");
                } else {
                    viewItem.setSimpleNote(modelItem.getSimpleNote());
                }
                if (modelItem.getDetailNote() == null) {
                    viewItem.setSimpleNote("");
                } else {
                    viewItem.setDetailNote(modelItem.getDetailNote());
                }
                this.items.add(viewItem);
            }
        }
        return this.items;
    }

    /**
     * 商品情報群を取得します。(Struts)
     *
     * @throws HelperBeanException HelperBean内で例外が発生
     * @throws EventException イベント生成時に例外が発生
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     * @return 商品情報群
     */
    public Collection getStrutsItems()
        throws
            HelperBeanException,
            EventException,
            SystemException,
            ApplicationException {

        //商品情報群をまだ取得していないときの処理
        if (this.items.isEmpty()) {
            //商品情報全件取得イベント作成
            Event event = createEvent("sample.shopping.conf.shopping", "get_all_items");

            //商品情報全件を格納したイベントリザルトを取得
            GetAllItemsEventResult eventResult =
                (GetAllItemsEventResult)dispatchEvent(event);

            //返却値格納
            Iterator iterator = eventResult.getItems().iterator();
            while (iterator.hasNext()) {
                SampleItemModelObject modelItem =
                    (SampleItemModelObject)iterator.next();
                SampleItemViewObject viewItem = new SampleItemViewObject();

                viewItem.setCode(modelItem.getCode());
                viewItem.setName(modelItem.getName());
                viewItem.setPrice(Integer.toString(modelItem.getPrice()));
                viewItem.setImagePath(
                    getRequest().getContextPath()
                        + "/"
                        + modelItem.getImagePath());

                if (modelItem.getSimpleNote() == null) {
                    viewItem.setSimpleNote("");
                } else {
                    viewItem.setSimpleNote(modelItem.getSimpleNote());
                }
                if (modelItem.getDetailNote() == null) {
                    viewItem.setSimpleNote("");
                } else {
                    viewItem.setDetailNote(modelItem.getDetailNote());
                }
                this.items.add(viewItem);
            }
        }
        return this.items;
    }
}
