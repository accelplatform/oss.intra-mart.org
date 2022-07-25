/*
 * InsertItemServiceController.java
 *
 * Created on 2002/02/25, 15:58
 */

package sample.shopping.controller.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.intra_mart.framework.base.service.RequestException;
import org.intra_mart.framework.base.service.ServiceControllerImple;
import org.intra_mart.framework.base.service.ServiceResult;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.message.MessageManager;
import sample.shopping.model.event.GetItemEvent;
import sample.shopping.model.event.GetItemEventResult;
import sample.shopping.model.object.SampleItemModelObject;

/**
 * 商品注文時のサービスコントローラです
 *
 * @author  NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class ThankYouServiceController extends ServiceControllerImple {

    /**
     * 入力内容をチェックします。
     * カートの中に商品が入っているかをチェックします。
     *
     * @throws RequestException 入力内容に誤りがある
     * @throws SystemException チェック時にシステム例外が発生
     *
     */
    public void check() throws RequestException, SystemException {
        HashMap datas = null;
        HttpServletRequest request = getRequest();
        HttpSession session = null;
        String errorStr = null;

        session = request.getSession(false);
        datas = (HashMap)session.getAttribute("IFW_CART_ITEMS");

        if ((datas == null) || (datas.isEmpty())) {
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.CART.EMPTY.ERR");
            throw new CartEmptyException(errorStr);
        }
    }

    /**
     * 注文情報を登録するためにカートの内容をチェックします
     *
     * @return 処理結果(null)
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     */
    public ServiceResult service()
        throws SystemException, ApplicationException {

        HashMap datas = null;
        HttpServletRequest request = getRequest();
        HttpSession session = null;
        String errorStr = null;
        Vector itemList = new Vector();

        session = request.getSession(false);
        datas = (HashMap)session.getAttribute("IFW_CART_ITEMS");

        // キーセットの取得
        Collection keys = datas.keySet();
        Iterator iter = keys.iterator();

        while (iter.hasNext()) {
            String itemCd = (String)iter.next();
            SampleItemModelObject item = null;

            // イベントの取得
            GetItemEvent event =
                (GetItemEvent)createEvent("sample.shopping.conf.shopping", "get_item");

            // item_cdのセット
            event.setCode(itemCd);

            // イベントリザルトの取得
            GetItemEventResult eventResult =
                (GetItemEventResult)dispatchEvent(event);

            // 商品情報の取得
            item = (SampleItemModelObject)eventResult.getItem();

            if (item != null) {
                itemList.add(item);
            }
        }

        // アイテムリストが空の時
        if (itemList.isEmpty()) {
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.CART.ITEM_MISSING.ERR");
            throw new ItemMissingException(errorStr);
        }

        return null;
    }
}
