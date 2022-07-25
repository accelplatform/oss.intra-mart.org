/*
 * FormTEI.java
 *
 * Created on 2002/12/12, 19:06
 */

package org.intra_mart.framework.base.web.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;

/**
 * Formタグの拡張情報です。
 *
 * @author INTRAMART
 * @since 4.0
 */
public class FormTEI extends TagExtraInfo {

    /**
     * FormTEIを新規に生成します。
     */
    public FormTEI() {
    }

    /**
     * Formタグに指定された内容の正当性を検証します。
     * 検証内容は以下のとおりです。<BR>
     * <UL>
     * <LI>class属性またはformclass属性がある場合、いずれかのみが設定されている
     * <LI>acceptcharset属性またはaccept-charset属性がある場合、いずれかのみが設定されている
     * </UL>
     *
     * @param tagData タグ情報
     * @return タグが正当な場合true、それ以外の場合はfalse
     */
    public boolean isValid(TagData tagData) {
        Object formClass = tagData.getAttribute("class");
        Object formClassDeprecated = tagData.getAttribute("formclass");
        Object acceptcharset = tagData.getAttribute("acceptcharset");
        Object acceptcharsetDeprecated = tagData.getAttribute("accept-charset");

        // 属性classがどちらも指定されている場合検査失敗
        if (formClass != null && formClassDeprecated != null) {
            return false;
        }

        // 属性accept-charsetがどちらも指定されている場合検査失敗
        if (acceptcharset != null && acceptcharsetDeprecated != null) {
            return false;
        }

        return true;
    }
}
