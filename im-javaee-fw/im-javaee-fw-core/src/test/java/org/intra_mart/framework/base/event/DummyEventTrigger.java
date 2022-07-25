/*
 * DummyEventTrigger.java
 *
 * Created on 2001/12/06, 15:43
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.base.data.DataAccessController;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class DummyEventTrigger implements EventTrigger {

    private boolean applicationException;

    private boolean systemException;

    /** Creates new DummyEventTrigger */
    public DummyEventTrigger() {
    }

    public void setApplicationException(boolean flag) {
        this.applicationException = flag;
    }

    public boolean isApplicationException() {
        return this.applicationException;
    }

    public void setSystemException(boolean flag) {
        this.systemException = flag;
    }

    public boolean isSystemException() {
        return this.systemException;
    }

    /**
     * イベントに対する処理を行います。
     *
     * @param event イベント
     * @throws SystemException システム例外が発生
     * @throws ApplicationException アプリケーション例外が発生
     */
    public void fire(Event event, DataAccessController controller)
        throws SystemException, ApplicationException {

        DummyEvent dummy = (DummyEvent)event;

        if (isApplicationException()) {
            throw new ApplicationException() {
            };
        }

        if (isSystemException()) {
            throw new SystemException() {
            };
        }
    }
}
