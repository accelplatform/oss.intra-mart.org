package org.intra_mart.common.aid.jdk.javax.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML解析クラス。<BR>
 * <BR>
 * 様々なストリームからXMLを解析することができる。<BR>
 */
public class DOMBuilder implements DOMInfo {

    /**
     * 解析後のドキュメントノード。<BR>
     */
    private Document document = null;

    /**
     * 暫定のエンティティリゾルバ。<BR>
     * <BR>
     * XMLにDTDの記述がある場合でも、DTDの検証を行わない用にするためのエンティティリゾルバ。<BR>
     * このエンティティリゾルバを用いてXML解析するとDTDの検証を行わない。<BR>
     */
    private static class NullEntityResolver implements EntityResolver {

        /**
         * コンストラクタ。<BR>
         */
        public NullEntityResolver() {
        }

        /**
         * 暫定DTDを取得する。<BR>
         * <BR>
         * すべてのXMLに対して検証結果が正しくなるようなDTDの入力ソースを返却する。<br>
         * @param publicId パブリックID
         * @param systemId システムID
         * @return 暫定DTDの入力ソース
         * @throws SAXException
         * @throws IOException
         * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
         */
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(new StringReader("<!ELEMENT everybody (#PCDATA)>"));
        }

    }

    /**
     * DOMを新規に作成します。<BR>
     * <BR>
     * ルートノードの名前を指定して、新規にDOMを作成します。<br>
     * @param elementName ルートノードの名前
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newDocument(String elementName) throws IOException, SAXException {
        return newDocument(elementName, null);
    }

    /**
     * DOMを新規に作成します。<BR>
     * <BR>
     * ルートノードの名前およびエンティティリゾルバを指定して、新規にDOMを作成します。<br>
     * @param elementName ルートノードの名前
     * @param resolver エンティティリゾルバ
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newDocument(String elementName, EntityResolver resolver) throws IOException, SAXException {
        //ノードを作成する
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        try {
            out.println("<" + elementName + "/>");

            return new DOMBuilder(parseContent(new ByteArrayInputStream(writer.toString().getBytes()), resolver));
        } finally {
            out.close();
        }
    }

    /**
     * DOMを新規に作成します。<BR>
     * <BR>
     * ルートノードの名前,パブリックID,システムIDを指定して、新規にDOMを作成します。<br>
     * @param elementName ルートノードの名前
     * @param publicId パブリックID
     * @param systemId システムID
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newDocument(String elementName, String publicId, String systemId) throws IOException, SAXException {

        return newDocument(elementName, publicId, systemId, null);
    }

    /**
     * DOMを新規に作成します。<BR>
     * <BR>
     * ルートノードの名前,パブリックID,システムIDおよびエンティティリゾルバを指定して、新規にDOMを作成します。<br>
     * @param elementName ルートノードの名前
     * @param publicId パブリックID
     * @param systemId システムID
     * @param resolver エンティティリゾルバ
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newDocument(String elementName, String publicId, String systemId, EntityResolver resolver) throws IOException,
            SAXException {

        //ドキュメントタイプを挿入したノードを作成する
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        try {
            out.println("<!DOCTYPE " + elementName + " PUBLIC");
            out.println("  \"" + publicId + "\"");
            out.println("  \"" + systemId + "\">");
            out.println();
            out.println("<" + elementName + "/>");

            return new DOMBuilder(parseContent(new ByteArrayInputStream(writer.toString().getBytes()), resolver));
        } finally {
            out.close();
        }
    }

    /**
     * ドキュメントからDOM情報を取得します。<BR>
     * <BR>
     * 引数のドキュメントからDOM情報を作成します。<br>
     * @param doc ドキュメントノード
     * @return 作成されたDOM情報
     */
    public static DOMInfo newInstance(Document doc) {
        return new DOMBuilder(doc);
    }

    /**
     * ファイルからXMLを解析してDOM情報を作成します。<BR>
     * <BR>
     * 引数のファイルからXMLを解析してDOM情報を作成します。<br>
     * @param path 解析を行うXMLファイルののフルパス
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newInstance(String path) throws IOException, SAXException {
        return new DOMBuilder(load(path));
    }

    /**
     * ファイルからXMLを解析してDOM情報を作成します。<BR>
     * <BR>
     * 引数のファイルからXMLを解析してDOM情報を作成します。<br>
     * エンティティリゾルバでDTDの検証を行います。
     * @param path 解析を行うXMLファイルののフルパス
     * @param resolver エンティティリゾルバ
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newInstance(String path, EntityResolver resolver) throws IOException, SAXException {
        return new DOMBuilder(load(path, resolver));
    }

    /**
     * ファイルクラスからXMLを解析してDOM情報を作成します。<BR>
     * <BR>
     * 引数のファイルクラスからXMLを解析してDOM情報を作成します。<br>
     * @param file ファイルクラス
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newInstance(File file) throws IOException, SAXException {
        return new DOMBuilder(load(file));
    }

    /**
     * ファイルクラスからXMLを解析してDOM情報を作成します。<BR>
     * <BR>
     * 引数のファイルクラスからXMLを解析してDOM情報を作成します。<br>
     * エンティティリゾルバでDTDの検証を行います。
     * @param file ファイルクラス
     * @param resolver エンティティリゾルバ
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newInstance(File file, EntityResolver resolver) throws IOException, SAXException {
        return new DOMBuilder(load(file, resolver));
    }

    /**
     * ストリームからXMLを解析してDOM情報を作成します。<BR>
     * <BR>
     * 引数のストリームからXMLを解析してDOM情報を作成します。<br>
     * @param stream ストリーム
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newInstance(InputStream stream) throws IOException, SAXException {
        return new DOMBuilder(parseContent(stream,null));
    }

    /**
     * ストリームからXMLを解析してDOM情報を作成します。<BR>
     * <BR>
     * 引数のストリームからXMLを解析してDOM情報を作成します。<br>
     * エンティティリゾルバでDTDの検証を行います。
     * @param stream ストリーム
     * @param resolver エンティティリゾルバ
     * @return 作成されたDOM情報
     * @throws IOException
     * @throws SAXException
     */
    public static DOMInfo newInstance(InputStream stream, EntityResolver resolver) throws IOException, SAXException {
        return new DOMBuilder(parseContent(stream, resolver));
    }
    /**
     * ファイルからXMLを解析してドキュメントを取得します。<BR>
     * <BR>
     * ファイルからXMLを解析してドキュメントノードを返却します。<br>
     * @param path 解析を行うXMLファイルののフルパス
     * @return 解析後のドキュメントノード
     * @throws IOException
     * @throws SAXException
     */
    private static Document load(String path) throws IOException, SAXException {
        return load(new File(path));
    }

    /**
     * ファイルからXMLを解析してドキュメントを取得します。<BR>
     * <BR>
     * ファイルからXMLを解析してドキュメントノードを返却します。<br>
     * エンティティリゾルバでDTDの検証を行います。
     * @param path 解析を行うXMLファイルののフルパス
     * @param resolver エンティティリゾルバ
     * @return 解析後のドキュメントノード
     * @throws IOException
     * @throws SAXException
     */
    private static Document load(String path, EntityResolver resolver) throws IOException, SAXException {
        return load(new File(path), resolver);
    }

    /**
     * ファイルクラスからXMLを解析してドキュメントを取得します。<BR>
     * <BR>
     * ファイルクラスからXMLを解析してドキュメントノードを返却します。<br>
     * @param file ファイルクラス
     * @return 解析後のドキュメントノード
     * @throws IOException
     * @throws SAXException
     */
    private static Document load(File file) throws IOException, SAXException {
        return load(file, null);
    }

    /**
     * ファイルクラスからXMLを解析してドキュメントを取得します。<BR>
     * <BR>
     * ファイルクラスからXMLを解析してドキュメントノードを返却します。<br>
     * エンティティリゾルバでDTDの検証を行います。
     * @param file ファイルクラス
     * @param resolver エンティティリゾルバ
     * @return 解析後のドキュメントノード
     * @throws IOException
     * @throws SAXException
     */
    private static Document load(File file, EntityResolver resolver) throws IOException, SAXException {
        return parseContent(new FileInputStream(file), resolver);
    }

    /**
     * ストリームからXMLを解析してドキュメントを取得します。<BR>
     * <BR>
     * ストリームからXMLを解析してドキュメントノードを返却します。<br>
     * エンティティリゾルバでDTDの検証を行います。
     * @param stream 入力ストリーム
     * @param resolver エンティティリゾルバ
     * @return 解析後のドキュメントノード
     * @throws IOException
     * @throws SAXException
     */
    private static Document parseContent(InputStream stream, EntityResolver resolver) throws IOException, SAXException {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;

            builder = factory.newDocumentBuilder();

            if (resolver != null) {
                builder.setEntityResolver(resolver);
            } else {
                builder.setEntityResolver(new NullEntityResolver());
            }

            InputSource src = new InputSource(stream);
            document = builder.parse(src); // 読込＆解析

        } catch (ParserConfigurationException e) {
            throw new IOException("xml Configuration Error!");
        } finally {
            if (stream != null){
                stream.close();
            }
        }

        return document;
    }

    protected DOMBuilder() {
    }

    protected DOMBuilder(Document doc) {
        this.document = doc;
    }

    /**
     * ドキュメントノードの取得。 <BR>
     * <BR>
     * ドキュメントノードを取得します。
     * @return ドキュメントノード
     * 
     * @see org.intra_mart.common.aid.jdk.javax.xml.DOMInfo#getDocumet()
     */
    public Document getDocumet() {
        return document;
    }

    /**
     * ルートノードの取得。 <BR>
     * <BR>
     * ルートのノードを取得します。
     * 
     * @return ルートノード<br>
     * ルートノードが存在しない場合は、null
     * @see org.intra_mart.common.aid.jdk.javax.xml.DOMInfo#getRootNode()
     */
    public Node getRootNode() {
        Node node = null;
        for (node = document.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE)
                break;
        }
        return node;
    }

}