/*
 * 作成日: 2003/12/22
 */
package org.intra_mart.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * XmlNodeを取得するクラスです。
 * @author INTRAMART
 */
public class XMLDocumentProducer {

    /**
     * プロパティハンドラを初期化します。
     *
     * @param fileName xmlファイルの名前
     */
    public Document getDocument(String fileName)
        throws
            ParserConfigurationException,
            SAXException,
            IOException,
            IllegalArgumentException {
        if (fileName.lastIndexOf(".xml") == -1) {
            fileName = fileName + ".xml";
        }
        
        Document document = null;
        DocumentBuilderFactory dfb = DocumentBuilderFactory.newInstance();
        dfb.setValidating(false);
        DocumentBuilder db;
        db = dfb.newDocumentBuilder();

        // マルチスレッド対応
        ClassLoader classLoader = XMLDocumentProducer.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        
        if (inputStream == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            inputStream = classLoader.getResourceAsStream(fileName);
        }

        document = db.parse(inputStream);
        return document;
    }

    /**
     * ノードを取得します。
     * @param doc_node ドキュメントノード
     * @return Node ノード
     */
    public Node getRoot(Document doc_node) {
        Node node = null;
        for (node = doc_node.getFirstChild();
            node != null;
            node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                break;
            }
        }
        return node;
    }

    /**
     * ファイルが存在するか調べます。
     * @param name アプリケーション名
     * @param locale ロケール
     * @return boolean ファイルの存在有無
     */
    public synchronized static boolean isFileExist(String name, Locale locale) {
        boolean result = false;
        String fileName = getFileName(name, locale);

        ClassLoader classLoader = XMLDocumentProducer.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            inputStream = classLoader.getResourceAsStream(fileName);
        }

        if (inputStream != null) {
            result = true;
        }
        return result;
    }

    /**
     * ファイル名を取得します。
     * @param name アプリケーション名
     * @param locale ロケール
     * @return String ファイル名
     */
    public static String getFileName(String name, Locale locale) {
    	StringBuffer sb = new StringBuffer();
        sb.append(name);
        if (locale != null) {
            if (!locale.getLanguage().equals("")) {
                sb.append("-");
                sb.append(locale.getLanguage());
            }
            if (!locale.getCountry().equals("")) {
                sb.append("_");
                sb.append(locale.getCountry());
            }
            if (!locale.getVariant().equals("")) {
                sb.append("_");
                sb.append(locale.getVariant());
            }
        }
        sb.append(".xml");
        return sb.toString().replace('\\', '/');
    }
}
