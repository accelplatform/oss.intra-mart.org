/*
 * InsertItemEventListener.java
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
import sample.shopping.model.data.ItemCodeDuplicationException;
import sample.shopping.model.data.SampleItemDAOIF;
import sample.shopping.model.object.SampleItemModelObject;

/**
 * 商品情報登録イベントリスナーです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class InsertItemEventListener extends StandardEventListener {

    /**
     * 商品情報登録イベントに対する処理です。
     *
     * @param event イベント
     * @return null
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    protected EventResult fire(Event event)
        throws SystemException, ApplicationException {
        //商品情報取得
        SampleItemModelObject modelObject = ((InsertItemEvent)event).getItem();

        //DAO取得
        SampleItemDAOIF dao =
            (SampleItemDAOIF)getDAO(event.getApplication(),
                "sample_shopping",null);

        //insert処理
        //重複したコードで登録した場合は、AlreadyExistExceptionを投げる
        try {
            dao.insert(modelObject);
        } catch (ItemCodeDuplicationException icde) {
            //message = 登録できません。同一コードのデータがあります。
            String errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.CODE.DUPLICATION.ERR");
            AlreadyExistException aee =
                new AlreadyExistException(errorStr, icde);
            aee.setCode(icde.getCode());
            throw aee;
        }
        return null;
    }
}
