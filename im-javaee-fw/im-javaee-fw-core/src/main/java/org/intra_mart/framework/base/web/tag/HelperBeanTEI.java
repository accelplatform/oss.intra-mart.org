/*
 * HelperBeanTEI.java
 *
 * Created on 2002/07/15, 11:09
 */

package org.intra_mart.framework.base.web.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * HelperBeanタグの拡張情報です。
 *
 * @author INTRAMART
 * @since 3.2
 */
public class HelperBeanTEI extends TagExtraInfo {

    /**
     * HelperBeanTEIを新規に生成します。
     */
    public HelperBeanTEI() {
        super();
    }

    /**
     * スクリプティング変数に関連付けたHelperBeanの情報を取得します。
     * 属性<code>id</code>で指定された変数に属性<code>class</code>（または<code>helperclass</code>）で指定されたHelperBeanを割り当てます。
     *
     * @param tagData タグデータ
     * @return スクリプティング変数の情報
     */
    public VariableInfo[] getVariableInfo(TagData tagData) {
        VariableInfo info =
            new VariableInfo(
                tagData.getId(),
                tagData.getAttributeString("class"),
                true,
                VariableInfo.AT_BEGIN);
        VariableInfo[] result = { info };

        return result;
    }

    /**
     * HelperBeanタグに指定された内容の正当性を検証します。
     * 検証内容は以下のとおりです。<BR>
     * <UL>
     * <LI>class属性またはhelperclass属性のいずれかのみが設定されている
     * </UL>
     *
     * @param tagData タグ情報
     * @return タグが正当な場合true、それ以外の場合はfalse
     * @since 4.0
     */
    public boolean isValid(TagData tagData) {
        Object helperClassObject = tagData.getAttribute("class");
        Object helperClassDeprecatedObject =
            tagData.getAttribute("helperclass");

        // どちらも指定されていない場合検査失敗
        if (helperClassObject == null && helperClassDeprecatedObject == null) {
            return false;
        }

        // どちらも指定されている場合検査失敗
        if (helperClassObject != null && helperClassDeprecatedObject != null) {
            return false;
        }

        // 空文字の場合失敗
        // 実行時評価の場合失敗
        if (helperClassObject != null) {
            if (helperClassObject.equals(TagData.REQUEST_TIME_VALUE)) {
                return false;
            } else {
                String helperClass = tagData.getAttributeString("class");
                if (helperClass.trim().equals("")) {
                    return false;
                }
            }
        }
        if (helperClassDeprecatedObject != null) {
            if (helperClassDeprecatedObject
                .equals(TagData.REQUEST_TIME_VALUE)) {
                return false;
            } else {
                String helperClassDeprecated =
                    tagData.getAttributeString("helperclass");
                if (helperClassDeprecated.trim().equals("")) {
                    return false;
                }
            }
        }

        return true;
    }
}
