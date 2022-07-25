/*
 * TestMessageEvent.java
 *
 * Created on 2004/02/17, 20:30:06
 */

package org.intra_mart.framework.base.event;

/**
 * 
 *
 * @author intra-mart
 * @since 4.3
 */
public class TestMessageEvent extends Event {

    private String message = "";

    public String getMessage() {
        return this.message;
    }

    public void putMessage(String message) {
        this.message += (message + "/");
    }
}
