package sample.shopping.controller.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.intra_mart.framework.base.service.DefaultTransition;
import org.intra_mart.framework.base.service.TransitionException;

/**
 * 商品購入時のトランジションです。
 *
 * @author NTTDATA intra-mart TY.
 * @version 1.0
 */
public class AddItemTransition extends DefaultTransition {
    public AddItemTransition() {
        super();
    }

    /**
     * セッション上にあるカート情報をセットします。
     *
     * @throws TransitionException 情報設定時に例外が発生
     */
    public void setInformation() throws TransitionException {
        HttpServletRequest request = getRequest();
        String itemCd = request.getParameter("item_cd");
        String amount = request.getParameter("nm_" + itemCd);
        int oldAmount;
        int newAmount;
        Integer sumAmount;
        HashMap datas;
        HttpSession session;

        session = request.getSession(false);
        if (session == null) {
            throw new TransitionException();
        }
        datas = (HashMap)session.getAttribute("IFW_CART_ITEMS");

        if (datas == null) {
            datas = new HashMap();
        }

        try {
            if (datas.containsKey(itemCd)) {
                // セッションデータ上に選択された itemCd がある場合
                oldAmount = ((Integer)datas.get(itemCd)).intValue();
                newAmount = (new Integer(amount)).intValue();

                sumAmount = new Integer(oldAmount + newAmount);
            } else {
                // セッションデータ上に選択された itemCd がない場合
                sumAmount = new Integer(amount);
            }
        } catch (NumberFormatException nfe) {
            sumAmount = new Integer(0);
        }

        datas.put(itemCd, sumAmount);

        try {
            session.setAttribute("IFW_CART_ITEMS", datas);
        } catch (Exception e) {
            throw new TransitionException(e.getMessage(), e);
        }
    }
}
