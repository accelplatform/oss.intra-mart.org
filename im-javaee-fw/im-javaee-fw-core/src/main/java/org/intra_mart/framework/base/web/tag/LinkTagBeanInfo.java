/*
 * LinkTagBeanInfo.java
 *
 * Created on 2002/12/12, 16:53
 */

package org.intra_mart.framework.base.web.tag;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import java.beans.IntrospectionException;

/**
 * LinkTagのBeanInfoです。
 *
 * @author INTRAMART
 * @since 4.0
 */
public class LinkTagBeanInfo extends SimpleBeanInfo {

    /**
     * LinkTagBeanInfoを新規に生成します。
     */
    public LinkTagBeanInfo() {
    }

    /**
     * PropertyDescriptorを取得します。
     *
     * @return PropertyDescriptor
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] properties = new PropertyDescriptor[32];

        try {
            properties[0]  = new PropertyDescriptor("id", LinkTag.class, "getId", "setId");
            properties[1]  = new PropertyDescriptor("application", LinkTag.class, "getApplication", "setApplication");
            properties[2]  = new PropertyDescriptor("service", LinkTag.class, "getService", "setService");
            properties[3]  = new PropertyDescriptor("accesskey", LinkTag.class, "getAccesskey", "setAccesskey");
            properties[4]  = new PropertyDescriptor("charset", LinkTag.class, "getCharset", "setCharset");
            properties[5]  = new PropertyDescriptor("coords", LinkTag.class, "getCoords", "setCoords");
            properties[6]  = new PropertyDescriptor("dir", LinkTag.class, "getDir", "setDir");
            properties[7]  = new PropertyDescriptor("hreflang", LinkTag.class, "getHreflang", "setHreflang");
            properties[8]  = new PropertyDescriptor("lang", LinkTag.class, "getLang", "setLang");
            properties[9]  = new PropertyDescriptor("linkclass", LinkTag.class, "getLinkclass", "setLinkclass");
            properties[10] = new PropertyDescriptor("class", LinkTag.class, "getLinkclass", "setLinkclass");
            properties[11] = new PropertyDescriptor("name", LinkTag.class, "getName", "setName");
            properties[12] = new PropertyDescriptor("onblur", LinkTag.class, "getOnblur", "setOnblur");
            properties[13] = new PropertyDescriptor("onclick", LinkTag.class, "getOnclick", "setOnclick");
            properties[14] = new PropertyDescriptor("ondblclick", LinkTag.class, "getOndblclick", "setOndblclick");
            properties[15] = new PropertyDescriptor("onfocus", LinkTag.class, "getOnfocus", "setOnfocus");
            properties[16] = new PropertyDescriptor("onkeydown", LinkTag.class, "getOnkeydown", "setOnkeydown");
            properties[17] = new PropertyDescriptor("onkeypress", LinkTag.class, "getOnkeypress", "setOnkeypress");
            properties[18] = new PropertyDescriptor("onkeyup", LinkTag.class, "getOnkeyup", "setOnkeyup");
            properties[19] = new PropertyDescriptor("onmousedown", LinkTag.class, "getOnmousedown", "setOnmousedown");
            properties[20] = new PropertyDescriptor("onmousemove", LinkTag.class, "getOnmousemove", "setOnmousemove");
            properties[21] = new PropertyDescriptor("onmouseout", LinkTag.class, "getOnmouseout", "setOnmouseout");
            properties[22] = new PropertyDescriptor("onmouseover", LinkTag.class, "getOnmouseover", "setOnmouseover");
            properties[23] = new PropertyDescriptor("onmouseup", LinkTag.class, "getOnmouseup", "setOnmouseup");
            properties[24] = new PropertyDescriptor("rel", LinkTag.class, "getRel", "setRel");
            properties[25] = new PropertyDescriptor("rev", LinkTag.class, "getRev", "setRev");
            properties[26] = new PropertyDescriptor("shape", LinkTag.class, "getShape", "setShape");
            properties[27] = new PropertyDescriptor("style", LinkTag.class, "getStyle", "setStyle");
            properties[28] = new PropertyDescriptor("tabindex", LinkTag.class, "getTabindex", "setTabindex");
            properties[29] = new PropertyDescriptor("title", LinkTag.class, "getTitle", "setTitle");
            properties[30] = new PropertyDescriptor("type", LinkTag.class, "getType", "setType");
            properties[31] = new PropertyDescriptor("target", LinkTag.class, "getTarget", "setTarget");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
