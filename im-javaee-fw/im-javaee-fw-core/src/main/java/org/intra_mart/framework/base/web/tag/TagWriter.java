/*
 * TagWriter.java
 *
 * Created on 2002/01/28, 18:10
 */

package org.intra_mart.framework.base.web.tag;

import java.util.Collection;
import java.util.Iterator;
import java.net.URLEncoder;

import javax.servlet.jsp.JspWriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * タグの出力を行います。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class TagWriter {

    /**
     * JSP出力子
     */
    private JspWriter writer;

    /**
     * TagWriterを新規に生成します。
     *
     * @param writer JSP出力子
     */
    public TagWriter(JspWriter writer) {
        this.writer = writer;
    }

    /**
     * 複数のパラメータをURLに追加します。
     * <CODE>params</CODE>は{@link FrameworkTagParam}の集合（<CODE>java.util.Collection</CODE>）である必要があります。
     *
     * @param url 元のURL
     * @param params 追加するパラメータ
     * @return パラメータが追加されたURL
     * @throws IOException 出力時に例外が発生
     * @deprecated このメソッドではなく{@link #createURL(String, Collection, String)}を使用してください。
     */
    public static String createURL(String url, Collection params)
        throws IOException {

        return createURL(url, params, null);
    }

    /**
     * 複数のパラメータをURLに追加します。
     * <CODE>params</CODE>は{@link FrameworkTagParam}の集合（<CODE>java.util.Collection</CODE>）である必要があります。
     *
     * @param url 元のURL
     * @param params 追加するパラメータ
     * @param encoding エンコーディング
     * @return パラメータが追加されたURL
     * @throws IOException 出力時に例外が発生
     * @since 4.3
     */
    public static String createURL(
        String url,
        Collection params,
        String encoding)
        throws IOException {

        Iterator paramIterator = null;
        FrameworkTagParam param = null;
        boolean isFirstParameter = true;
        String result = url;

        if (params != null) {
            paramIterator = params.iterator();
            while (paramIterator.hasNext()) {
                param = (FrameworkTagParam)paramIterator.next();
                if (param.getValue() != null && !param.getValue().equals("")) {
                    if (isFirstParameter) {
                        isFirstParameter = false;
                        result += "?";
                    } else {
                        result += "&";
                    }
                    result += encode(param.getName(), encoding)
                        + "="
                        + encode(param.getValue(), encoding);
                }
            }
        }

        return result;
    }

    /**
     * エンコードを行います。
     * エンコーディングが<code>null</code>の場合、システム標準のエンコーディングが使用されます（非推奨）。
     *
     * @param value 変換対象の文字列
     * @param encoding エンコーディング
     * @return 変換された文字列
     * @throws UnsupportedEncodingException
     */
    private static String encode(String value, String encoding)
        throws UnsupportedEncodingException {

        if (encoding == null) {
            // 4.2互換用
            return URLEncoder.encode(value);
        } else {
            return URLEncoder.encode(value, encoding);
        }
    }

    /**
     * URLを出力します。
     * このurlは<CODE>&lt;FORM&gt;</CODE>タグの<CODE>action</CODE>属性、<CODE>&lt;A&gt;</CODE>タグの<CODE>href</CODE>属性、<CODE>&lt;FRAME&gt;</CODE>タグの<CODE>src</CODE>属性などに定義するものと同じです。
     *
     * @param url 出力するURL
     * @throws IOException 出力時に例外が発生
     */
    public void printURL(String url) throws IOException {
        this.writer.print(url);
    }

    /**
     * 属性を出力します。
     * タグの属性として<CODE>属性名=&quot;属性値&quot;</CODE>の形式で出力します。
     * 属性値がnullまたは空文字列である場合、何も出力しません。
     *
     * @param name 属性名
     * @param value 属性値
     * @throws IOException 出力時に例外が発生
     */
    public void printAttribute(String name, String value) throws IOException {
        if (value != null && !value.equals("")) {
            this.writer.print(" " + name + "=\"" + value + "\"");
        }
    }
}
