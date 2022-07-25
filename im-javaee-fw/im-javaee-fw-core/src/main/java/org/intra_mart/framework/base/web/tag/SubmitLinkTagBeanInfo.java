/*
 * SubmitLinkTagBeanInfo.java
 *
 * Created on 2003/07/22, 18:20
 */

package org.intra_mart.framework.base.web.tag;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import java.beans.IntrospectionException;

/**
 * SubmitLinkTagのBeanInfoです。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class SubmitLinkTagBeanInfo extends SimpleBeanInfo {

    /**
     * SubmitLinkTagBeanInfoを新規に生成します。
     */
    public SubmitLinkTagBeanInfo() {
    }

    /**
     * PropertyDescriptorを取得します。
     *
     * @return PropertyDescriptor
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] properties = new PropertyDescriptor[26];

        try {
            properties[0]  = new PropertyDescriptor("id", SubmitLinkTag.class, "getId", "setId");
            properties[1]  = new PropertyDescriptor("application", SubmitLinkTag.class, "getApplication", "setApplication");
            properties[2]  = new PropertyDescriptor("service", SubmitLinkTag.class, "getService", "setService");
            properties[3]  = new PropertyDescriptor("accesskey", SubmitLinkTag.class, "getAccesskey", "setAccesskey");
            properties[4]  = new PropertyDescriptor("coords", SubmitLinkTag.class, "getCoords", "setCoords");
            properties[5]  = new PropertyDescriptor("dir", SubmitLinkTag.class, "getDir", "setDir");
            properties[6]  = new PropertyDescriptor("lang", SubmitLinkTag.class, "getLang", "setLang");
            properties[7]  = new PropertyDescriptor("class", SubmitLinkTag.class, "getLinkclass", "setLinkclass");
            properties[8]  = new PropertyDescriptor("name", SubmitLinkTag.class, "getName", "setName");
            properties[9]  = new PropertyDescriptor("onblur", SubmitLinkTag.class, "getOnblur", "setOnblur");
            properties[10] = new PropertyDescriptor("onclick", SubmitLinkTag.class, "getOnclick", "setOnclick");
            properties[11] = new PropertyDescriptor("ondblclick", SubmitLinkTag.class, "getOndblclick", "setOndblclick");
            properties[12] = new PropertyDescriptor("onfocus", SubmitLinkTag.class, "getOnfocus", "setOnfocus");
            properties[13] = new PropertyDescriptor("onkeydown", SubmitLinkTag.class, "getOnkeydown", "setOnkeydown");
            properties[14] = new PropertyDescriptor("onkeypress", SubmitLinkTag.class, "getOnkeypress", "setOnkeypress");
            properties[15] = new PropertyDescriptor("onkeyup", SubmitLinkTag.class, "getOnkeyup", "setOnkeyup");
            properties[16] = new PropertyDescriptor("onmousedown", SubmitLinkTag.class, "getOnmousedown", "setOnmousedown");
            properties[17] = new PropertyDescriptor("onmousemove", SubmitLinkTag.class, "getOnmousemove", "setOnmousemove");
            properties[18] = new PropertyDescriptor("onmouseout", SubmitLinkTag.class, "getOnmouseout", "setOnmouseout");
            properties[19] = new PropertyDescriptor("onmouseover", SubmitLinkTag.class, "getOnmouseover", "setOnmouseover");
            properties[20] = new PropertyDescriptor("onmouseup", SubmitLinkTag.class, "getOnmouseup", "setOnmouseup");
            properties[21] = new PropertyDescriptor("shape", SubmitLinkTag.class, "getShape", "setShape");
            properties[22] = new PropertyDescriptor("style", SubmitLinkTag.class, "getStyle", "setStyle");
            properties[23] = new PropertyDescriptor("tabindex", SubmitLinkTag.class, "getTabindex", "setTabindex");
            properties[24] = new PropertyDescriptor("title", SubmitLinkTag.class, "getTitle", "setTitle");
            properties[25] = new PropertyDescriptor("form", SubmitLinkTag.class, "getForm", "setForm");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
