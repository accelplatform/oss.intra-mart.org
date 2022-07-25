/*
 * GetItemEventListener.java
 *
 * Created on 2002/02/26, 20:59
 */

package sample.shopping.model.event;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.base.event.StandardEventListener;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import sample.shopping.model.data.SampleItemDAOIF;
import sample.shopping.model.object.SampleItemModelObject;

/**
 * 商品情報1件取得イベントリスナーです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class GetItemEventListener extends StandardEventListener {

    /**
     * 商品情報1件取得イベントに対する処理です。
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
        SampleItemModelObject modelItem =
            dao.select(((GetItemEvent)event).getCode());

        //商品情報格納
        GetItemEventResult eventResult = new GetItemEventResult();
        eventResult.setItem(modelItem);

        return eventResult;
    }
}
