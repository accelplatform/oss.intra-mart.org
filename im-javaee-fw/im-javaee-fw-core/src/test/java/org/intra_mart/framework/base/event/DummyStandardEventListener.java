/*
 * DummyStandardEventListener.java
 *
 * Created on 2001/12/06, 17:19
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class DummyStandardEventListener extends StandardEventListener {

    /** Creates new DummyStandardEventListener */
    public DummyStandardEventListener() {
        super();
    }

    /**
     * イベントに対する処理です。
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    public EventResult fire(Event event) throws SystemException, ApplicationException {
        DummyEvent dummyEvent = (DummyEvent)event;
        DummyEventResult result;

        if (dummyEvent.isApplicationException()) {
            throw new ApplicationException() {};
        }
        if (dummyEvent.isSystemException()) {
            throw new SystemException() {};
        }
        result = new DummyEventResult();
        result.setValue("application = " + event.getApplication() + " : key = " + event.getKey());

        return result;
    }
}
