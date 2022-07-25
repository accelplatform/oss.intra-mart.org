/*
 * GenericAttributes.java
 *
 * Created on 2002/01/28, 15:08
 */

package org.intra_mart.framework.base.web.tag.attribute;

import org.intra_mart.framework.base.web.tag.TagWriter;

import java.io.IOException;

/**
 * 汎用属性の集合です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class GenericAttributes {

    /**
     * Tag Writer
     */
    private TagWriter writer;

    /**
     * GenericAttributesを新規に生成します。
     */
    public GenericAttributes() {
        setTagWriter(null);
    }

    /**
     * TagWriterを取得します。
     *
     * @return TagWriter
     */
    public TagWriter getTagWriter() {
        return this.writer;
    }

    /**
     * TagWriterを設定します。
     *
     * @param writer TagWriter
     */
    public void setTagWriter(TagWriter writer) {
        this.writer = writer;
    }

    /**
     * 属性の内容を表示します。
     *
     * @throws IOException 出力中に例外が発生
     */
    public abstract void printAttributes() throws IOException;
}
