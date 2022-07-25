/*
 * BuyerOrderServiceController.java
 *
 * Created on 2002/03/19, 18:09
 */

package sample.shopping.controller.service;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.intra_mart.framework.base.service.ServiceResult;
import org.intra_mart.framework.base.service.RequestException;
import org.intra_mart.framework.base.service.ServiceControllerImple;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.message.MessageManager;
import sample.shopping.model.event.GetItemEvent;
import sample.shopping.model.event.GetItemEventResult;
import sample.shopping.model.object.SampleItemModelObject;
import sample.shopping.model.event.AlreadyDeletedException;

/**
 * 商品を注文するときのサービスコントローラです。
 *
 * @author NTTDATA intra-mart.
 * @version 1.0
 */
public class BuyerOrderServiceController extends ServiceControllerImple {

    private HashMap datas = null;

    /**
     * 入力内容をチェックします。
     * カートの中に商品が入っているかをチェックします。
     *
     * @throws RequestException 入力内容に誤りがある
     * @throws SystemException チェック時にシステム例外が発生
     *
     */
    public void check() throws RequestException, SystemException {
        HttpSession session = null;
        String errorStr = null;
        HttpServletRequest request = getRequest();
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
     * 商品情報チェック処理を実行します。
     *
     * @return 処理結果(null)
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     */
    public ServiceResult service()
        throws SystemException, ApplicationException {

        if (datas != null) {
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
                if (item == null) {
                    // 商品がすでに削除されている場合エラー
                    AlreadyDeletedException e =
                        new AlreadyDeletedException("指定された商品はすでに削除されています。");
                    e.setCode(itemCd);
                    e.setKey("menu_to_buyer_list");
                    throw e;
                }
            }
        }
        return null;
    }

}
