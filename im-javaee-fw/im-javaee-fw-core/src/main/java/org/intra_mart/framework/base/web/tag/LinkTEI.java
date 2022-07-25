/*
 * LinkTEI.java
 *
 * Created on 2002/12/12, 19:42
 */

package org.intra_mart.framework.base.web.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;

/**
 * Linkタグの拡張情報です。
 *
 * @author INTRAMART
 * @since 4.0
 */
public class LinkTEI extends TagExtraInfo {

    /**
     * LinkTEIを新規に生成します。
     */
    public LinkTEI() {
    }

    /**
     * Linkタグに指定された内容の正当性を検証します。
     * 検証内容は以下のとおりです。<BR>
     * <UL>
     * <LI>class属性またはlinkclass属性がある場合、いずれかのみが設定されている
     * </UL>
     *
     * @param tagData タグ情報
     * @return タグが正当な場合true、それ以外の場合はfalse
     */
    public boolean isValid(TagData tagData) {
        Object linkClass = tagData.getAttribute("class");
        Object linkClassDeprecated = tagData.getAttribute("linkclass");

        // 属性classがどちらも指定されている場合検査失敗
        if (linkClass != null && linkClassDeprecated != null) {
            return false;
        }

        return true;
    }
}
