/*
 * GotoUpdateItemServiceController.java
 *
 * Created on 2002/03/11, 11:19
 */

package sample.shopping.controller.service;

import org.intra_mart.framework.base.service.ServiceControllerImple;
import org.intra_mart.framework.base.service.ServiceResult;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * 商品情報更新画面表示時のサービスコントローラです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class GotoUpdateItemServiceController extends ServiceControllerImple {

    /**
     * 更新を行う商品コードを次画面に渡します。
     *
     * @return 更新を行う商品コードが格納されたGotoUpdateItemServiceResultオブジェクト
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     */
    public ServiceResult service()
        throws SystemException, ApplicationException {

        String updateCode = getRequest().getParameter("item_cd");

        //GotoUpdateItemServiceResultに更新を行う商品コードを格納
        GotoUpdateItemServiceResult serviceResult =
            new GotoUpdateItemServiceResult();
        serviceResult.setCode(updateCode);

        return serviceResult;
    }
}
