/*
 * DeleteItemServiceController.java
 *
 * Created on 2002/02/26, 18:52
 */

package sample.shopping.controller.service;

import org.intra_mart.framework.base.service.ServiceControllerImple;
import org.intra_mart.framework.base.service.ServiceResult;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import sample.shopping.model.event.DeleteItemEvent;

/**
 * 商品情報削除時のサービスコントローラです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class DeleteItemServiceController extends ServiceControllerImple {

    /**
     * 商品情報削除処理を実行します。
     *
     * @return null
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     */
    public ServiceResult service()
        throws SystemException, ApplicationException {

        //イベント作成
        DeleteItemEvent event =
            (DeleteItemEvent)createEvent("sample.shopping.conf.shopping", "delete_item");

        //削除する商品の商品コードをセット
        event.setCode(getRequest().getParameter("item_cd"));

        //イベント発行
        dispatchEvent(event);

        return null;
    }
}
