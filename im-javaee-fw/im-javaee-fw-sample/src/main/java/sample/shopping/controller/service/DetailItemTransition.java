package sample.shopping.controller.service;

import javax.servlet.http.HttpServletRequest;

import org.intra_mart.framework.base.service.DefaultTransition;
import org.intra_mart.framework.base.service.TransitionException;

/**
 * 商品詳細画面表示時のトランジションです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class DetailItemTransition extends DefaultTransition {

    public DetailItemTransition() {
        super();
    }

    /**
     * 次画面に渡す情報をセットします
     *
     * @throws TransitionException 情報設定時に例外が発生
     */
    public void setInformation() throws TransitionException {
        HttpServletRequest request = getRequest();

        DetailItemServiceResult result = (DetailItemServiceResult)getResult();

        String itemCd = result.getItemCd();
        String key = result.getKey();

        request.setAttribute("item_cd", itemCd);
        request.setAttribute("key", key);
    }
}
