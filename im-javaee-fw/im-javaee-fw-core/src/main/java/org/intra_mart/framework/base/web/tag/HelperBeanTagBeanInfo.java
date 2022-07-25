/*
 * HelperBeanTagBeanInfo.java
 *
 * Created on 2002/12/12, 17:31
 */

package org.intra_mart.framework.base.web.tag;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import java.beans.IntrospectionException;

/**
 * HelperBeanTagのBeanInfoです。
 *
 * @author INTRAMART
 * @since 4.0
 */
public class HelperBeanTagBeanInfo extends SimpleBeanInfo {

    /**
     * HelperBeanTagBeanInfoを新規に生成します。
     */
    public HelperBeanTagBeanInfo() {
    }

    /**
     * PropertyDescriptorを取得します。
     *
     * @return PropertyDescriptor
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] properties = new PropertyDescriptor[3];

        try {
            properties[0]  = new PropertyDescriptor("id", HelperBeanTag.class, "getId", "setId");
            properties[1]  = new PropertyDescriptor("helperclass", HelperBeanTag.class, "getHelperclass", "setHelperclass");
            properties[2]  = new PropertyDescriptor("class", HelperBeanTag.class, "getHelperclass", "setHelperclass");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
