/*
 * DummyMessageEventTrigger10.java
 *
 * Created on 2004/02/17, 20:25:59
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.base.data.DataAccessController;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * 
 *
 * @author intra-mart
 * @since 4.3
 */
public class DummyMessageEventTrigger10 implements EventTrigger {

    /**
     * @param event
     * @param controller
     * @throws SystemException
     * @throws ApplicationException
     */
    public void fire(Event event, DataAccessController controller)
        throws SystemException, ApplicationException {

        TestMessageEvent messageEvent = (TestMessageEvent)event;
        messageEvent.putMessage("trigger message 10");
    }
}
