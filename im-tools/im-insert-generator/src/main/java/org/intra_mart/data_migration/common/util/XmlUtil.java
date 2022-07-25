/*
 * XmlUtil.java
 *
 * Created on 2005/05/17,  21:04:06
 */
package org.intra_mart.data_migration.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XML関連のユーティリティです。
 * 
 * @author intra-mart
 * 
 */
public class XmlUtil {

    /**
     * コンストラクタ
     */
    private XmlUtil() {
    }

    /**
     * XMLドキュメントを取得します。
     * 
     * @return XMLドキュメント
     * @throws ParserConfigurationException
     */
    public static Document getDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    /**
     * XMLファイルの読み込みを行います。
     * 
     * @param stream
     *            入力ストリーム
     * @return XMLドキュメント
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document read(InputStream stream)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(stream);
    }

    /**
     * XMLファイルの生成を行います。
     * 
     * @param document
     *            XMLドキュメント
     * @param stream
     *            出力ストリーム
     * @throws TransformerException
     */
    public static void write(Document document, OutputStream stream)
            throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource source = new DOMSource(document);
        // 出力XMLファイルを設定
        StreamResult target = new StreamResult(stream);
        // 変換
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(source, target);
    }
}