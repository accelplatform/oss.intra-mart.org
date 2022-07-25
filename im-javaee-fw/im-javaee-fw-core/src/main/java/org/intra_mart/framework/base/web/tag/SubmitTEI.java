/*
 * SubmitTEI.java
 *
 * Created on 2002/12/12, 19:46
 */

package org.intra_mart.framework.base.web.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;

/**
 * Submitタグの拡張情報です。
 *
 * @author INTRAMART
 * @since 4.0
 */
public class SubmitTEI extends TagExtraInfo {

    /**
     * SubmitTEIを新規に生成します。
     */
    public SubmitTEI() {
    }

    /**
     * Linkタグに指定された内容の正当性を検証します。
     * 検証内容は以下のとおりです。<BR>
     * <UL>
     * <LI>class属性またはsubmitclass属性がある場合、いずれかのみが設定されている
     * </UL>
     *
     * @param tagData タグ情報
     * @return タグが正当な場合true、それ以外の場合はfalse
     */
    public boolean isValid(TagData tagData) {
        Object submitClass = tagData.getAttribute("class");
        Object submitClassDeprecated = tagData.getAttribute("submitclass");

        // 属性classがどちらも指定されている場合検査失敗
        if (submitClass != null && submitClassDeprecated != null) {
            return false;
        }

        return true;
    }
}
