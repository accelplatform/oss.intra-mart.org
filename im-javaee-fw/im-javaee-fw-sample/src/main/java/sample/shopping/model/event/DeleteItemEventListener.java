/*
 * DeleteItemEventListener.java
 *
 * Created on 2002/02/26, 19:07
 */

package sample.shopping.model.event;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.base.event.StandardEventListener;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import sample.shopping.model.data.SampleItemDAOIF;

/**
 * 商品情報削除イベントリスナーです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class DeleteItemEventListener extends StandardEventListener {

    /**
     * 商品情報削除イベントに対する処理です。
     *
     * @param event イベント
     * @return null
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    protected EventResult fire(Event event)
        throws SystemException, ApplicationException {

        //DAOの取得
        SampleItemDAOIF dao = null;
        try {
            dao =
                (SampleItemDAOIF)getDAO(event.getApplication(),
                    "sample_shopping",null);
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }

        //削除処理実行
        dao.delete(((DeleteItemEvent)event).getCode());

        return null;
    }
}
