/*
 * DummyEvent.java
 *
 * Created on 2001/11/29, 13:50
 */

package org.intra_mart.framework.base.event;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class DummyEvent extends Event {

    private boolean applicationException;

    private boolean systemException;

    public DummyEvent() {
        super();
        setApplicationException(false);
        setSystemException(false);
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
}
