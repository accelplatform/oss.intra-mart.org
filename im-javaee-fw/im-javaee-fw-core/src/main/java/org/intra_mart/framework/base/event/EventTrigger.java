/*
 * EventTrigger.java
 *
 * Created on 2001/12/04, 11:44
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.base.data.DataAccessController;

import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.exception.ApplicationException;

/**
 * イベントトリガです。
 * このインタフェースを実装するクラスでは、定数(static final)以外のフィールドは定義しないでください。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface EventTrigger {

    /**
     * イベントに対する処理を行います。
     * トランザクションは<CODE>controller</CODE>で管理されています。
     * このメソッドを実装するとき、内部でトランザクションのロールバックやコミットを行わないようにしてください。
     * メソッドが失敗した場合、またはロールバックを行いたい場合は、ApplicationExceptionのサブクラスを例外としてthrowしてください。
     *
     * @param event イベント
     * @param controller データアクセスコントローラ
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    public void fire(Event event, DataAccessController controller) throws SystemException, ApplicationException;
}
