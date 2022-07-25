/*
 * MessageTEI.java
 *
 * Created on 2003/08/13, 20:00
 */

package org.intra_mart.framework.base.web.tag;

import java.util.StringTokenizer;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;

/**
 * Messageタグの拡張情報です。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class MessageTEI extends TagExtraInfo {

    /**
     * Messageタグに指定された内容の正当性を検証します。
     * 検証内容は以下のとおりです。<BR>
     * <UL>
     * <LI>locale属性が指定されている場合、書式が<I>言語</I>_<I>国</I>[_<I>バリアント</I>]となっていること<BR>
     * ロケール文字列のアンダーバー(_)はハイフン(-)であってもかまいません。
     * </UL>
     * locale属性が指定されていない場合、trueが返されます。
     *
     * @param tagData タグ情報
     * @return タグが正当な場合true、それ以外の場合はfalse
     */
    public boolean isValid(TagData tagData) {
    	// ロケールを設定していない場合は何もしない
        Object obj = tagData.getAttribute("locale");
        if (obj == null) {
            return true;
        }

		// ロケールが実行時評価の場合は何もしない
        if (obj.equals(TagData.REQUEST_TIME_VALUE)) {
            return true;
        }

        String locale = tagData.getAttributeString("locale");
        StringTokenizer tokenizer = new StringTokenizer(locale, "-_");

        return tokenizer.countTokens() > 0;
    }
}
