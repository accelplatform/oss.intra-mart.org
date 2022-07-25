package sample.shopping.controller.service;

import javax.servlet.http.HttpServletRequest;

import org.intra_mart.framework.base.service.ServiceControllerImple;
import org.intra_mart.framework.base.service.ServiceResult;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * 商品詳細画面表示時のサービスコントローラです。
 *
 * @author NTTDATA intra-mart TY.
 * @version 1.0
 */
public class DetailItemServiceController extends ServiceControllerImple {

    /**
     * DetailItemServiceControllerを初期化します。
     */
    public DetailItemServiceController() {
        super();
    }

    /**
     * 入力に対する処理を実行します。
     *
     * @return 処理結果
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例が発生
     */
    public ServiceResult service()
        throws SystemException, ApplicationException {

        HttpServletRequest request = getRequest();

        // リクエスト情報の取得(key, item_cd)
        String key = request.getParameter("key");
        String itemCd = request.getParameter("item_cd");

        DetailItemServiceResult serviceResult = new DetailItemServiceResult();

        // 処理結果オブジェクトへ情報をセット
        serviceResult.setItemCd(itemCd);
        serviceResult.setKey(key);

        return serviceResult;
    }
}
