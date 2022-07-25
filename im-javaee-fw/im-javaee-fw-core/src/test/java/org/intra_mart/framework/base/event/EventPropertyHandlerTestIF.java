/*
 * EventPropertyHandlerTestIF.java
 *
 * Created on 2001/12/05, 19:36
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.property.PropertyHandlerTestIF;

/**
 *
 * @author  intra-mart
 * @version 
 */
public interface EventPropertyHandlerTestIF extends PropertyHandlerTestIF {

    public void testEventName() throws Exception;

    public void testEventNameNothing() throws Exception;

    public void testEventListenerFactoryName() throws Exception;

    public void testEventListenerFactoryNameException() throws Exception;

    public void testEventListenerFactoryOption() throws Exception;

    public void testEventListenerFactoryOptionNothing() throws Exception;

    public void testEventListenerFactoryOptionException() throws Exception;

    public void testEventTriggerInfos() throws Exception;

    public void testEventTriggerInfosException() throws Exception;

    public void testPostEventTriggerInfos() throws Exception;

    public void testPostEventTriggerInfosException() throws Exception;
}
