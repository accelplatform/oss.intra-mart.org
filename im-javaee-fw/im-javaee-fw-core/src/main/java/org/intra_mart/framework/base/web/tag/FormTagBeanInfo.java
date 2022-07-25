/*
 * FormTagBeanInfo.java
 *
 * Created on 2002/12/12, 16:09
 */

package org.intra_mart.framework.base.web.tag;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import java.beans.IntrospectionException;

/**
 * FormTagのBeanInfoです。
 *
 * @author INTRAMART
 * @since 4.0
 */
public class FormTagBeanInfo extends SimpleBeanInfo {

    /**
     * FormTagBeanInfoを新規に生成します。
     */
    public FormTagBeanInfo() {
    }

    /**
     * PropertyDescriptorを取得します。
     *
     * @return PropertyDescriptor
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] properties = new PropertyDescriptor[28];

        try {
            properties[0]  = new PropertyDescriptor("id", FormTag.class, "getId", "setId");
            properties[1]  = new PropertyDescriptor("application", FormTag.class, "getApplication", "setApplication");
            properties[2]  = new PropertyDescriptor("service", FormTag.class, "getService", "setService");
            properties[3]  = new PropertyDescriptor("method", FormTag.class, "getMethod", "setMethod");
            properties[4]  = new PropertyDescriptor("enctype", FormTag.class, "getEnctype", "setEnctype");
            properties[5]  = new PropertyDescriptor("accept", FormTag.class, "getAccept", "setAccept");
            properties[6]  = new PropertyDescriptor("name", FormTag.class, "getName", "setName");
            properties[7]  = new PropertyDescriptor("onsubmit", FormTag.class, "getOnsubmit", "setOnsubmit");
            properties[8]  = new PropertyDescriptor("onreset", FormTag.class, "getOnreset", "setOnreset");
            properties[9]  = new PropertyDescriptor("acceptcharset", FormTag.class, "getAcceptcharset", "setAcceptcharset");
            properties[10] = new PropertyDescriptor("accept-charset", FormTag.class, "getAcceptcharset", "setAcceptcharset");
            properties[11] = new PropertyDescriptor("formclass", FormTag.class, "getFormclass", "setFormclass");
            properties[12] = new PropertyDescriptor("class", FormTag.class, "getFormclass", "setFormclass");
            properties[13] = new PropertyDescriptor("lang", FormTag.class, "getLang", "setLang");
            properties[14] = new PropertyDescriptor("style", FormTag.class, "getStyle", "setStyle");
            properties[15] = new PropertyDescriptor("title", FormTag.class, "getTitle", "setTitle");
            properties[16] = new PropertyDescriptor("target", FormTag.class, "getTarget", "setTarget");
            properties[17] = new PropertyDescriptor("onclick", FormTag.class, "getOnclick", "setOnclick");
            properties[18] = new PropertyDescriptor("ondblclick", FormTag.class, "getOndblclick", "setOndblclick");
            properties[19] = new PropertyDescriptor("onmousedown", FormTag.class, "getOnmousedown", "setOnmousedown");
            properties[20] = new PropertyDescriptor("onmouseup", FormTag.class, "getOnmouseup", "setOnmouseup");
            properties[21] = new PropertyDescriptor("onmouseover", FormTag.class, "getOnmouseover", "setOnmouseover");
            properties[22] = new PropertyDescriptor("onmousemove", FormTag.class, "getOnmousemove", "setOnmousemove");
            properties[23] = new PropertyDescriptor("onmouseout", FormTag.class, "getOnmouseout", "setOnmouseout");
            properties[24] = new PropertyDescriptor("onkeypress", FormTag.class, "getOnkeypress", "setOnkeypress");
            properties[25] = new PropertyDescriptor("onkeydown", FormTag.class, "getOnkeydown", "setOnkeydown");
            properties[26] = new PropertyDescriptor("onkeyup", FormTag.class, "getOnkeyup", "setOnkeyup");
            properties[27] = new PropertyDescriptor("dir", FormTag.class, "getDir", "setDir");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
