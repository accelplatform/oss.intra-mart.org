/*
 * ThankYouTransition.java
 *
 * Created on 2002/03/19, 17:28
 */

package sample.shopping.controller.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.intra_mart.framework.base.service.DefaultTransition;
import org.intra_mart.framework.base.service.TransitionException;

/**
 * Thank you 画面表示時のトランジションです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class ThankYouTransition extends DefaultTransition {

    /**
     * 購入した商品コードをセッションオブジェクトから削除します。
     *
     * @throws TransitionException 情報設定時に例外が発生
     */
    public void setInformation() throws TransitionException {
        HttpServletRequest request = getRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new TransitionException();
        }
        session.removeAttribute("IFW_CART_ITEMS");

    }
}
