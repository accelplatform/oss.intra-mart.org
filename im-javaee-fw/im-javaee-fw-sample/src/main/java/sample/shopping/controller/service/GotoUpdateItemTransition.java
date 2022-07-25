/*
 * GotoUpdateItemTransition.java
 *
 * Created on 2002/03/11, 11:36
 */

package sample.shopping.controller.service;

import javax.servlet.http.HttpServletRequest;

import org.intra_mart.framework.base.service.DefaultTransition;
import org.intra_mart.framework.base.service.TransitionException;

/**
 * 商品情報更新画面表示時のトランジションです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class GotoUpdateItemTransition extends DefaultTransition {

    /**
     * 更新を行う商品コードをリクエストにセットします。
     *
     * @throws TransitionException トランジションに関連する例外が発生
     */
    public void setInformation() throws TransitionException {

        //リクエスト取得
        HttpServletRequest request = getRequest();

        //サービスリザルト取得
        GotoUpdateItemServiceResult serviceResult =
            (GotoUpdateItemServiceResult)getResult();

        //リクエストに更新を行う商品コードをセット
        try {
            request.setAttribute("item_cd", serviceResult.getCode());
        } catch (Exception e) {
            new TransitionException(e.getMessage(), e);
        }
    }
}
