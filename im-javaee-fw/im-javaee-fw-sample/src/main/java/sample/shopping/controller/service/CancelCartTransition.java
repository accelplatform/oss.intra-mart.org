package sample.shopping.controller.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.intra_mart.framework.base.service.DefaultTransition;
import org.intra_mart.framework.base.service.TransitionException;

/**
 * カート情報を取り消す処理のトランジションです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class CancelCartTransition extends DefaultTransition {
    /**
     * CancelCartTransitionを初期化します。
     */
    public CancelCartTransition() {
        super();
    }

    /**
     * 該当するカートの中身の商品情報を消去します。
     *
     * @throws TransitionException 商品情報の消去時に例外が発生
     */
    public void setInformation() throws TransitionException {
        HttpServletRequest request = getRequest();
        String itemCd = request.getParameter("item_cd");
        HttpSession session;
        HashMap datas;

        // セッション情報からカートの中身の情報を取得
        session = request.getSession(false);
        if (session == null) {
            throw new TransitionException();
        }
        datas = (HashMap)session.getAttribute("IFW_CART_ITEMS");

        // カートの中身の取り消し
        datas.remove(itemCd);

        // カート情報をセッションにセット
        try {
            session.setAttribute("IFW_CART_ITEMS", datas);
        } catch (Exception e) {
            throw new TransitionException(e.getMessage(), e);
        }
    }
}
