/*
 * I18nAttributes.java
 *
 * Created on 2002/01/28, 15:49
 */

package org.intra_mart.framework.base.web.tag.attribute;

import java.io.IOException;

/**
 * 国際化の属性です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class I18nAttributes extends GenericAttributes {

    /**
     * lang
     */
    private String lang;

    /**
     * dir
     */
    private String dir;

    /**
     * I18nAttributesを新規に生成します。
     */
    public I18nAttributes() {
        super();
        setLang(null);
        setDir(null);
    }

    /**
     * 属性<CODE>lang</CODE>の値を取得します。
     *
     * @return lang
     */
    public String getLang() {
        return this.lang;
    }

    /**
     * 属性<CODE>lang</CODE>の値を設定します。
     *
     * @param lang lang
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * 属性<CODE>dir</CODE>の値を取得します。
     *
     * @return dir
     */
    public String getDir() {
        return this.dir;
    }

    /**
     * 属性<CODE>dir</CODE>の値を取得します。
     *
     * @param dir dir
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * 属性の内容を表示します。
     *
     * @throws IOException 出力中に例外が発生
     */
    public void printAttributes() throws IOException {
        // lang
        getTagWriter().printAttribute("lang", getLang());

        // dir
        getTagWriter().printAttribute("dir", getDir());
    }
}
