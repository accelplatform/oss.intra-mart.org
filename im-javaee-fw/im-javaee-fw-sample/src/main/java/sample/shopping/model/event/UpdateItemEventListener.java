/*
 * UpdateItemEventListener.java
 *
 * Created on 2002/02/26, 17:21
 */

package sample.shopping.model.event;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.base.event.StandardEventListener;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.message.MessageManager;
import sample.shopping.model.data.ItemNotExistException;
import sample.shopping.model.data.SampleItemDAOIF;
import sample.shopping.model.object.SampleItemModelObject;

/**
 * 商品情報更新イベントリスナーです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class UpdateItemEventListener extends StandardEventListener {

    /**
     * 商品情報更新イベントに対する処理です。
     *
     * @param event イベント
     * @return null
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    protected EventResult fire(Event event)
        throws SystemException, ApplicationException {

        //商品情報取得
        SampleItemModelObject modelItem = ((UpdateItemEvent)event).getItem();

        //DAO取得
        SampleItemDAOIF dao =
            (SampleItemDAOIF)getDAO(event.getApplication(),
                "sample_shopping",
                null);

        //update処理
        //商品情報が削除されていたときは、AlreadyDeletedExceptionを投げる
        try {
            dao.update(modelItem);
        } catch (ItemNotExistException inee) {
            //message = データの更新に失敗しました。
            String errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.ITEM.NOT.FOUND.ERR");
            AlreadyDeletedException ade =
                new AlreadyDeletedException(errorStr, inee);
            ade.setCode(inee.getCode());
            throw ade;
        }

        return null;
    }
}
