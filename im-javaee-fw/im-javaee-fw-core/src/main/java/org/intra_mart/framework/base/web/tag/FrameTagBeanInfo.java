/*
 * FrameTagBeanInfo.java
 *
 * Created on 2002/12/12, 17:20
 */

package org.intra_mart.framework.base.web.tag;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import java.beans.IntrospectionException;

/**
 * FrameTagのBeanInfoです。
 *
 * @author INTRAMART
 * @since 4.0
 */
public class FrameTagBeanInfo extends SimpleBeanInfo {

    /**
     * FrameTagBeanInfoを新規に生成します。
     */
    public FrameTagBeanInfo() {
    }

    /**
     * PropertyDescriptorを取得します。
     *
     * @return PropertyDescriptor
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] properties = new PropertyDescriptor[14];

        try {
            properties[0]  = new PropertyDescriptor("id", FrameTag.class, "getId", "setId");
            properties[1]  = new PropertyDescriptor("application", FrameTag.class, "getApplication", "setApplication");
            properties[2]  = new PropertyDescriptor("service", FrameTag.class, "getService", "setService");
            properties[3]  = new PropertyDescriptor("frameclass", FrameTag.class, "getFrameclass", "setFrameclass");
            properties[4]  = new PropertyDescriptor("class", FrameTag.class, "getFrameclass", "setFrameclass");
            properties[5]  = new PropertyDescriptor("frameborder", FrameTag.class, "getFrameborder", "setFrameborder");
            properties[6]  = new PropertyDescriptor("longdesc", FrameTag.class, "getLongdesc", "setLongdesc");
            properties[7]  = new PropertyDescriptor("marginheight", FrameTag.class, "getMarginheight", "setMarginheight");
            properties[8]  = new PropertyDescriptor("marginwidth", FrameTag.class, "getMarginwidth", "setMarginwidth");
            properties[9]  = new PropertyDescriptor("name", FrameTag.class, "getName", "setName");
            properties[10] = new PropertyDescriptor("noresize", FrameTag.class, "getNoresize", "setNoresize");
            properties[11] = new PropertyDescriptor("scrolling", FrameTag.class, "getScrolling", "setScrolling");
            properties[12] = new PropertyDescriptor("style", FrameTag.class, "getStyle", "setStyle");
            properties[13] = new PropertyDescriptor("title", FrameTag.class, "getTitle", "setTitle");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
