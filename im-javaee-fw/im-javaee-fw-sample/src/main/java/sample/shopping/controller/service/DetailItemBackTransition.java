package sample.shopping.controller.service;

import javax.servlet.http.HttpServletRequest;

import org.intra_mart.framework.base.service.DefaultTransition;
import org.intra_mart.framework.base.service.ServicePropertyException;
import org.intra_mart.framework.base.service.TransitionException;

/**
 * 商品詳細情報画面で戻るボタンが押された時に生成されるトランジションです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class DetailItemBackTransition extends DefaultTransition {

    /**
     * DetailItemBackTransitionを初期化します。
     */
    public DetailItemBackTransition() {
        super();
    }

    /**
     * 次の遷移先を決定します。
     *
     * @return 次遷移先のパス
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public String getNextPage()
        throws ServicePropertyException, TransitionException {

        HttpServletRequest request = getRequest();
        // 次ページをあらわすキーを取得
        String key = request.getParameter("key");

        // キーから次遷移先ページを取得して返却
        return getNextPagePath(key);
    }
}
