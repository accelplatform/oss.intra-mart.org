/*
 * GetAllItemsEventListener.java
 *
 * Created on 2002/02/21, 15:20
 */

package sample.shopping.model.event;

import java.util.Collection;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.base.event.StandardEventListener;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import sample.shopping.model.data.SampleItemDAOIF;

/**
 * 商品情報全件取得イベントリスナーです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class GetAllItemsEventListener extends StandardEventListener {

    /**
     * 商品情報全件取得イベントに対する処理です。
     *
     * @param event イベント
     * @return 商品情報群
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    protected EventResult fire(Event event)
        throws SystemException, ApplicationException {
        //DAO取得
        SampleItemDAOIF dao =
            (SampleItemDAOIF)getDAO(event.getApplication(),
                "sample_shopping",null);

        //select処理
        Collection items = dao.select();

        //処理結果格納
        GetAllItemsEventResult eventResult = new GetAllItemsEventResult();
        eventResult.setItems(items);

        return eventResult;
    }
}
