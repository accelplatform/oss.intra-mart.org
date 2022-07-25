/*
 * CoreAttributes.java
 *
 * Created on 2002/01/28, 15:15
 */

package org.intra_mart.framework.base.web.tag.attribute;

import java.io.IOException;

/**
 * コア属性です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class CoreAttributes extends GenericAttributes {

    /**
     * class
     */
    private String tagClass;

    /**
     * style
     */
    private String style;

    /**
     * title
     */
    private String title;

    /**
     * CoreAttributesを新規に生成します。
     */
    public CoreAttributes() {
        super();
        setTagClass(null);
        setStyle(null);
        setTitle(null);
    }

    /**
     * 属性<CODE>class</CODE>の値を取得します。
     *
     * @return class
     */
    public String getTagClass() {
        return this.tagClass;
    }

    /**
     * 属性<CODE>class</CODE>の値を設定します。
     *
     * @param tagClass class
     */
    public void setTagClass(String tagClass) {
        this.tagClass = tagClass;
    }

    /**
     * 属性<CODE>style</CODE>の値を取得します。
     *
     * @return style
     */
    public String getStyle() {
        return this.style;
    }

    /**
     * 属性<CODE>style</CODE>の値を設定します。
     *
     * @param style style
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * 属性<CODE>title</CODE>の値を取得します。
     *
     * @return title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * 属性<CODE>title</CODE>の値を設定します。
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 属性の内容を表示します。
     *
     * @throws IOException 出力中に例外が発生
     */
    public void printAttributes() throws IOException {
        // class
        getTagWriter().printAttribute("class", getTagClass());

        // style
        getTagWriter().printAttribute("style", getStyle());

        // title
        getTagWriter().printAttribute("title", getTitle());
    }
}
