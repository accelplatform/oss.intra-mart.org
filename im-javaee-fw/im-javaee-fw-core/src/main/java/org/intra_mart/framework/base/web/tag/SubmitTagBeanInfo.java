/*
 * SubmitTagBeanInfo.java
 *
 * Created on 2002/12/12, 18:46
 */

package org.intra_mart.framework.base.web.tag;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import java.beans.IntrospectionException;

/**
 * SubmitTagのBeanInfoです。
 *
 * @author INTRAMART
 * @since 4.0
 */
public class SubmitTagBeanInfo extends SimpleBeanInfo {

    /**
     * SubmitTagBeanInfoを新規に生成します。
     */
    public SubmitTagBeanInfo() {
    }

    /**
     * PropertyDescriptorを取得します。
     *
     * @return PropertyDescriptor
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] properties = new PropertyDescriptor[23];

        try {
            properties[0]  = new PropertyDescriptor("id", SubmitTag.class, "getId", "setId");
            properties[1]  = new PropertyDescriptor("application", SubmitTag.class, "getApplication", "setApplication");
            properties[2]  = new PropertyDescriptor("service", SubmitTag.class, "getService", "setService");
            properties[3]  = new PropertyDescriptor("name", SubmitTag.class, "getName", "setName");
            properties[4]  = new PropertyDescriptor("submitclass", SubmitTag.class, "getSubmitclass", "setSubmitclass");
            properties[5]  = new PropertyDescriptor("class", SubmitTag.class, "getSubmitclass", "setSubmitclass");
            properties[6]  = new PropertyDescriptor("lang", SubmitTag.class, "getLang", "setLang");
            properties[7]  = new PropertyDescriptor("style", SubmitTag.class, "getStyle", "setStyle");
            properties[8]  = new PropertyDescriptor("title", SubmitTag.class, "getTitle", "setTitle");
            properties[9]  = new PropertyDescriptor("onclick", SubmitTag.class, "getOnclick", "setOnclick");
            properties[10] = new PropertyDescriptor("ondblclick", SubmitTag.class, "getOndblclick", "setOndblclick");
            properties[11] = new PropertyDescriptor("onmousedown", SubmitTag.class, "getOnmousedown", "setOnmousedown");
            properties[12] = new PropertyDescriptor("onmouseup", SubmitTag.class, "getOnmouseup", "setOnmouseup");
            properties[13] = new PropertyDescriptor("onmouseover", SubmitTag.class, "getOnmouseover", "setOnmouseover");
            properties[14] = new PropertyDescriptor("onmousemove", SubmitTag.class, "getOnmousemove", "setOnmousemove");
            properties[15] = new PropertyDescriptor("onmouseout", SubmitTag.class, "getOnmouseout", "setOnmouseout");
            properties[16] = new PropertyDescriptor("onkeypress", SubmitTag.class, "getOnkeypress", "setOnkeypress");
            properties[17] = new PropertyDescriptor("onkeydown", SubmitTag.class, "getOnkeydown", "setOnkeydown");
            properties[18] = new PropertyDescriptor("onkeyup", SubmitTag.class, "getOnkeyup", "setOnkeyup");
            properties[19] = new PropertyDescriptor("dir", SubmitTag.class, "getDir", "setDir");
            properties[20] = new PropertyDescriptor("value", SubmitTag.class, "getValue", "setValue");
            properties[21] = new PropertyDescriptor("onblur", SubmitTag.class, "getOnblur", "setOnblur");
            properties[22] = new PropertyDescriptor("onfocus", SubmitTag.class, "getOnfocus", "setOnfocus");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
