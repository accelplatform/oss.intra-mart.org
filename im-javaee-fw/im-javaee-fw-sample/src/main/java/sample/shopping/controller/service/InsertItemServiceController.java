/*
 * InsertItemServiceController.java
 *
 * Created on 2002/02/25, 15:58
 */

package sample.shopping.controller.service;

import org.intra_mart.framework.base.service.RequestException;
import org.intra_mart.framework.base.service.ServiceControllerImple;
import org.intra_mart.framework.base.service.ServiceResult;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.message.MessageManager;
import sample.shopping.model.event.InsertItemEvent;
import sample.shopping.model.object.SampleItemModelObject;

/**
 * 商品情報登録時のサービスコントローラです。
 *
 * @author  NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class InsertItemServiceController extends ServiceControllerImple {

    /**
     * 入力内容をチェックします。
     * リクエスト内の文字列長のチェックを行います。
     *
     * @throws RequestException 入力内容に誤りがある
     * @throws SystemException チェック時にシステム例外が発生
     *
     */
    public void check() throws RequestException, SystemException {

        String requestValue = null;
        String errorStr = null;

        //商品コードの文字列長チェック
        requestValue = getRequest().getParameter("item_cd");
        if (requestValue.equals("")) {
            //message = 必須項目が未入力です。
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.REQUIRED.INPUT.ERR");
            ValueLengthException e = new ValueLengthException(errorStr);
            e.setParameterName("商品コード");
            throw e;
        } else if (requestValue.length() > 20) {
            //message = 入力範囲を越えています。
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.INPUT.LENGTH.ERR");
            ValueLengthException e = new ValueLengthException(errorStr);
            e.setParameterName("商品コード");
            throw e;
        }

        //商品名の文字列長チェック
        requestValue = getRequest().getParameter("name");
        if (requestValue.equals("")) {
            //message = 必須項目が未入力です。
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.REQUIRED.INPUT.ERR");
            ValueLengthException e = new ValueLengthException(errorStr);
            e.setParameterName("商品名");
            throw e;
        } else if ((requestValue.length() * 2) > 128) {
            //message = 入力範囲を越えています。
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.INPUT.LENGTH.ERR");
            ValueLengthException e = new ValueLengthException(errorStr);
            e.setParameterName("商品名");
            throw e;
        }

        //価格の文字列長チェック
        requestValue = getRequest().getParameter("price");
        try {
            if (requestValue.length() > 9) {
                //message = 入力範囲を越えています。
                errorStr =
                    MessageManager.getMessageManager().getMessage(
                        "B_FW_SAMPLE.INPUT.LENGTH.ERR");
                ValueLengthException e = new ValueLengthException(errorStr);
                e.setParameterName("価格");
                throw e;
            } else if (
                (requestValue.indexOf("0") == 0)
                    || (requestValue.indexOf("-") == 0)) {
                //message = 正の整数を入力対象としています。
                errorStr =
                    MessageManager.getMessageManager().getMessage(
                        "B_FW_SAMPLE.NUMBER.FORMAT.ERR");
                ValueLengthException e = new ValueLengthException(errorStr);
                e.setParameterName("価格");
                throw e;
            }
            Integer.parseInt(requestValue);
        } catch (NumberFormatException nfe) {
            //message = 正の整数を入力対象としています。
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.NUMBER.FORMAT.ERR");
            ValueLengthException e = new ValueLengthException(errorStr);
            e.setParameterName("価格");
            throw e;
        }

        //簡易説明の文字列長チェック
        requestValue = getRequest().getParameter("simple_note");
        if ((requestValue.length() * 2) > 255) {
            //message = 入力範囲を越えています。
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.INPUT.LENGTH.ERR");
            ValueLengthException e = new ValueLengthException(errorStr);
            e.setParameterName("簡易説明");
            throw e;
        }

        //詳細説明の文字列長チェック
        requestValue = getRequest().getParameter("detail_note");
        if ((requestValue.length() * 2) > 255) {
            //message = 入力範囲を越えています。
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.INPUT.LENGTH.ERR");
            ValueLengthException e = new ValueLengthException(errorStr);
            e.setParameterName("詳細説明");
            throw e;
        }

        //画像パスの文字列長チェック
        requestValue = getRequest().getParameter("image_path");
        if (requestValue.equals("")) {
            //message = 必須項目が未入力です。
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.REQUIRED.INPUT.ERR");
            ValueLengthException e = new ValueLengthException(errorStr);
            e.setParameterName("画像パス");
            throw e;
        } else if (requestValue.length() > 255) {
            //message = 入力範囲を越えています。
            errorStr =
                MessageManager.getMessageManager().getMessage(
                    "B_FW_SAMPLE.INPUT.LENGTH.ERR");
            ValueLengthException e = new ValueLengthException(errorStr);
            e.setParameterName("画像パス");
            throw e;
        }
    }

    /**
     * 商品情報登録処理を実行します。
     *
     * @return 処理結果(null)
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     */
    public ServiceResult service()
        throws SystemException, ApplicationException {

        //イベント取得
        InsertItemEvent event =
            (InsertItemEvent)createEvent("sample.shopping.conf.shopping", "insert_item");

        //イベントに商品情報を格納
        SampleItemModelObject modelObject = new SampleItemModelObject();
        modelObject.setCode(getRequest().getParameter("item_cd"));
        modelObject.setName(getRequest().getParameter("name"));
        modelObject.setSimpleNote(getRequest().getParameter("simple_note"));
        modelObject.setDetailNote(getRequest().getParameter("detail_note"));
        modelObject.setImagePath(getRequest().getParameter("image_path"));
        //priceの値が数値に変換できない場合は、ApplicationExceptionをthrowする
        try {
            modelObject.setPrice(
                Integer.parseInt(getRequest().getParameter("price")));
        } catch (NumberFormatException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
        event.setItem(modelObject);

        //イベント発行
        dispatchEvent(event);

        return null;
    }
}
