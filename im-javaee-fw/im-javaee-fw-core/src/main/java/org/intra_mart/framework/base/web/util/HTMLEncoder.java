/*
 * HTMLEncoder.java
 *
 * Created on 2002/03/12, 19:08
 */

package org.intra_mart.framework.base.web.util;

/**
 * 文字列をHTML形式で表示するための変換を行うユーティリティクラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class HTMLEncoder {

    /**
     * キャプションを変換します。
     * HTML中で特殊な扱いを受ける文字（&gt;,&lt;等）を変換します。<BR>
     * ここでは以下の変換が行われます。<BR>
     * <TABLE border="1">
     *    <TR>
     *        <TH>変換前</TH>
     *        <TH>変換後</TH>
     *    </TR>
     *    <TR>
     *        <TD>&gt;</TD>
     *        <TD>&amp;gt;</TD>
     *    </TR>
     *    <TR>
     *        <TD>&lt;</TD>
     *        <TD>&amp;lt;</TD>
     *    </TR>
     *    <TR>
     *        <TD>&amp;</TD>
     *        <TD>&amp;amp;</TD>
     *    </TR>
     *    <TR>
     *        <TD>&quot;</TD>
     *        <TD>&amp;quot;</TD>
     *    </TR>
     *    <TR>
     *        <TD>(半角空白)</TD>
     *        <TD>&amp;nbsp;</TD>
     *    </TR>
     *    <TR>
     *        <TD>(改行)(*)</TD>
     *        <TD>&lt;BR&gt;</TD>
     *    </TR>
     * </TABLE><BR>
     * (*)変換ルールは以下のとおりです。<BR>
     * &quot;\n&quot; -&gt; &quot;\n&quot;<BR>
     * &quot;\r&quot; -&gt; &quot;\n&quot;<BR>
     * ただし、&quot;\r&quot;と&quot;\n&quot;が連続して続く箇所は&quot;\r\n&quot; -&gt; &quot;\n&quot;となります。
     *
     * @param caption 変換前のキャプション
     * @return 変換後のキャプション
     */
    public static String encodeCaption(String caption) {
        StringBuffer srcBuffer = null;
        String src = null;
        StringBuffer resultBuffer = null;
        String result = null;
        char token = 0;
        int length = 0;
        int beginIndex = 0;
        int endIndex = 0;

        if (caption != null) {
            // \r\n -> \n に変換
            srcBuffer = new StringBuffer();
            beginIndex = 0;
            endIndex = 0;
            length = caption.length();
            while (beginIndex < length && (endIndex = caption.indexOf("\r\n", beginIndex)) != -1) {
                if (beginIndex < endIndex) {
                    srcBuffer.append(caption.substring(beginIndex, endIndex));
                }
                srcBuffer.append("\n");
                beginIndex = endIndex + 2;
            }
            if (beginIndex < length) {
                srcBuffer.append(caption.substring(beginIndex));
            }
            src = new String(srcBuffer);

            // \r -> \n に変換
            srcBuffer = new StringBuffer();
            beginIndex = 0;
            endIndex = 0;
            length = src.length();
            while (beginIndex < length && (endIndex = src.indexOf("\r", beginIndex)) != -1) {
                if (beginIndex < endIndex) {
                    srcBuffer.append(src.substring(beginIndex, endIndex));
                }
                srcBuffer.append("\n");
                beginIndex = endIndex + 1;
            }
            if (beginIndex < length) {
                srcBuffer.append(src.substring(beginIndex));
            }
            src = new String(srcBuffer);

            // 変換
            resultBuffer = new StringBuffer();
            length = src.length();
            for (int i = 0; i < length; i++) {
                token = src.charAt(i);
                if (token == '<') {
                    resultBuffer.append("&lt;"); 
                } else if (token == '>') {
                    resultBuffer.append("&gt;"); 
                } else if (token == '&') {
                    resultBuffer.append("&amp;"); 
                } else if (token == '\"') {
                    resultBuffer.append("&quot;"); 
                } else if (token == ' ') {
                    resultBuffer.append("&nbsp;"); 
                } else if (token == '\n') {
                    resultBuffer.append("<BR>"); 
                } else {
                    resultBuffer.append(token); 
                }
            }
            result = new String(resultBuffer);
        }

        return result;
    }

    /**
     * &lt;TEXTAREA&gt;タグまたはvalue属性に入る文字列を変換します。
     * ここでは以下の変換が行われます。<BR>
     * <TABLE border="1">
     *    <TR>
     *        <TH>変換前</TH>
     *        <TH>変換後</TH>
     *    </TR>
     *    <TR>
     *        <TD>&gt;</TD>
     *        <TD>&amp;gt;</TD>
     *    </TR>
     *    <TR>
     *        <TD>&lt;</TD>
     *        <TD>&amp;lt;</TD>
     *    </TR>
     *    <TR>
     *        <TD>&amp;</TD>
     *        <TD>&amp;amp;</TD>
     *    </TR>
     *    <TR>
     *        <TD>&quot;</TD>
     *        <TD>&amp;quot;</TD>
     *    </TR>
     * </TABLE>
     *
     * @param value 変換前の値
     * @return 変換後の値
     */
    public static String encodeValue(String value) {
        StringBuffer resultBuffer = null;
        String result = null;
        int length = 0;
        char token = 0;

        if (value != null) {
            // 変換
            resultBuffer = new StringBuffer();
            length = value.length();
            for (int i = 0; i < length; i++) {
                token = value.charAt(i);
                if (token == '<') {
                    resultBuffer.append("&lt;"); 
                } else if (token == '>') {
                    resultBuffer.append("&gt;"); 
                } else if (token == '&') {
                    resultBuffer.append("&amp;"); 
                } else if (token == '\"') {
                    resultBuffer.append("&quot;"); 
                } else {
                    resultBuffer.append(token); 
                }
            }
            result = new String(resultBuffer);
        }

        return result;
    }
}
