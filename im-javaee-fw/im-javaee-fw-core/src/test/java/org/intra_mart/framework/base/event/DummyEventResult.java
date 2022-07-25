/*
 * DummyEventResult.java
 *
 * Created on 2001/12/06, 17:06
 */

package org.intra_mart.framework.base.event;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class DummyEventResult implements EventResult {

    private String value;

    /** Creates new DummyEventResult */
    public DummyEventResult() {
        setValue("");
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
