package org.intra_mart.common.aid.jdk.javax.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * DOMノード操作クラス。<BR>
 * <BR>
 * DOMノードを操作するユーティリティクラス。<BR>
 */
public class XmlNode {

    /**
     * ノード <BR>
     */
    private Node node = null;

    /**
     * 親ノード <BR>
     */
    private Node parent_node = null;

    /**
     * ノードのテキスト <BR>
     */
    private Text text = null;

    /**
     * ノードのURL <BR>
     */
    private String uri = null;

    /**
     * ノードの値 <BR>
     */
    private String value = null;

    /**
     * コンストラクタ。 <BR>
     * <BR>
     * 属性ノードからインスタンスを生成します。
     * 
     * @param attr
     *            属性ノード
     */
    public XmlNode(Attr attr) {
        this.node = attr;
    }

    /**
     * コンストラクタ。 <BR>
     * <BR>
     * ドキュメントノードからインスタンスを生成します。
     * 
     * @param document
     *            ドキュメントノード
     */
    public XmlNode(Document document) {
        this.node = document;
    }

    /**
     * コンストラクタ。 <BR>
     * <BR>
     * エレメントノードからインスタンスを生成します。
     * 
     * @param element
     *            エレメントノード
     */
    public XmlNode(Element element) {
        this.node = element;

    }

    /**
     * コンストラクタ。 <BR>
     * <BR>
     * ノードからインスタンスを生成します。
     * 
     * @param node
     *            ノード
     */
    public XmlNode(Node node) {
        this.node = node;
    }

    /**
     * コンストラクタ。 <BR>
     * <BR>
     * 親ノードとノードからインスタンスを生成します。
     * 
     * @param parent
     *            親ノード
     * @param node
     *            ノード
     */
    public XmlNode(Node parent, Node node) {
        this.parent_node = parent;
        this.node = node;
    }

    /**
     * 属性を追加します。 <BR>
     * <BR>
     * このノードに属性を追加します。 <BR>
     * ノードに属性が既にある場合は、上書きされます。 <BR>
     * ノードがエレメントでない場合は、追加されません。
     * 
     * @param name
     *            属性名
     * @param value
     *            値
     * @return 追加および変更された属性のXmlNode <BR>
     *         追加できなかった場合はnull
     */
    public XmlNode addAttr(String name, String value) {

        Attr attr = null; // 追加する属性

        if (getNode() instanceof Element) {
            Element element = (Element) getNode();
            attr = element.getAttributeNode(name);

            if (attr == null) {
                attr = element.getOwnerDocument().createAttribute(name);
                attr.setValue(value);
                attr = element.setAttributeNode(attr);
            } else {
                attr.setValue(value);
            }
            return new XmlNode(attr);
        }

        return null;

    }

    /**
     * エレメントを追加します。 <BR>
     * <BR>
     * このノードにエレメントを追加します。 <BR>
     * このノードがドキュメントまたはエレメントである場合に、エレメントを追加します。
     * 
     * @param name
     *            追加するエレメント名
     * @return 追加されたエレメントのXmlNode <BR>
     *         追加できなかった場合はnull
     */
    public XmlNode addNode(String name) {

        if ((getNode() instanceof Document) || (getNode() instanceof Element)) {
            Node res = getNode().appendChild(getNode().getOwnerDocument().createElement(name));
            return new XmlNode(res);
        }
        return null;
    }

    /**
     * 相対パスのノードにエレメントを追加します。 <BR>
     * <BR>
     * 相対パスのノードがドキュメントまたはエレメントである場合に、エレメントを追加します。
     * 
     * @param name
     *            追加するエレメント名
     * @param targetPath
     *            相対パス
     * @return 追加されたエレメントのXmlNode <BR>
     *         追加できなかった場合はnull
     */
    public XmlNode addNode(String name, String targetPath) {

        if ((getNode() instanceof Document) || (getNode() instanceof Element)) {
            XmlNode targetNode = this.lookup(targetPath);
            if (targetNode != null) {
                Node res = targetNode.getNode().appendChild(getNode().getOwnerDocument().createElement(name));
                return new XmlNode(res);
            }
        }
        return null;
    }

    /**
     * 属性を削除します。 <BR>
     * <BR>
     * このノードの属性を削除します。 <BR>
     * 
     * @param name
     *            属性名
     * @return 削除された属性のXmlNode <BR>
     *         削除できなかった場合はnull
     */
    public XmlNode delAttr(String name) {

        if (getNode() instanceof Element) {
            Element element = (Element) getNode();
            Attr attr = element.getAttributeNode(name);
            if (attr != null) {
                element.removeAttributeNode(attr);
                return new XmlNode(attr);
            }
        }
        return null;

    }

    /**
     * このノードを削除します。 <BR>
     * <BR>
     * 
     * @return 削除できた場合はこのノードのXmlNode <BR>
     *         削除できなかった場合はnull
     */
    public XmlNode delNode() {

        Node parent = getNode().getParentNode();
        if ((parent instanceof Document) || (parent instanceof Element)) {
            if (parent.removeChild(getNode()) == null) {
                return null;
            }
        }
        return this;
    }

    /**
     * 相対パスのノードを削除します。 <BR>
     * <BR>
     * 相対パスのノードが存在する場合、そのノードを削除します。
     * 
     * @param targetPath
     *            相対パス
     * @return 削除されたノードのXmlNode <BR>
     *         削除できなかった場合はnull
     */
    public XmlNode delNode(String targetPath) {

        if ((getNode() instanceof Document) || (getNode() instanceof Element)) {

            XmlNode res = lookup(targetPath);

            if ((res != null) && (getNode().removeChild(res.getNode()) != null)) {
                return res;
            }
        }

        return null;
    }

    /**
     * ノードを削除します。 <BR>
     * <BR>
     * 
     * @param child
     *            削除するノード
     * @return 削除されたノードのXmlNode <BR>
     *         削除できなかった場合はnull
     */
    public XmlNode delNode(XmlNode child) {

        if ((getNode() instanceof Document) || (getNode() instanceof Element)) {

            if ((child != null) && (getNode().removeChild(child.getNode()) != null)) {
                return child;
            }
        }

        return null;
    }

    /**
     * 属性および子ノードの一覧を取得します。<BR>
     * <BR>
     * 属性および子ノードの一覧を取得します。<BR>
     * @return 属性および子ノードの配列 (XmlNode)
     */
    public XmlNode[] getChildNode() {

        List result = new ArrayList();

        // まず属性の一覧を取得。
        NamedNodeMap alist = getNode().getAttributes();
        if (alist != null) {
            if (alist.getLength() > 0) {
                for (int i = 0; i < alist.getLength(); ++i) {
                    XmlNode cn = new XmlNode(getNode(), alist.item(i));
                    result.add(cn);
                }
            }
        }

        // 次に子供の一覧を取得。
        if (getNode().hasChildNodes()) {
            NodeList nlist = getNode().getChildNodes();
            if (nlist.getLength() == 1 && nlist.item(0) instanceof Text) {
                result.add(this);
            }
            for (int i = 0; i < nlist.getLength(); ++i) {
                XmlNode cn = new XmlNode(nlist.item(i));
                if (cn.getNode() instanceof Element) {
                    result.add(cn);
                }
            }
        }
        return (XmlNode[]) result.toArray(new XmlNode[result.size()]);
    }

    /**
     * すべての属性および子ノードを取得します。<BR>
     * <BR>
     * このノードのすべての属性および子ノードを取得します。<BR>
     * すべての階層のノードを取得して、引数のリストに保管します。
     * @param list ノードのリストを保管するリスト
     */
    private void getChildNodeList(List list) {

        NamedNodeMap alist = getNode().getAttributes();
        if (alist != null) {
            if (alist.getLength() > 0) {
                for (int i = 0; i < alist.getLength(); ++i) {
                    XmlNode cn = new XmlNode(getNode(), alist.item(i));
                    list.add(cn);
                }
            }
        }

        if (getNode().hasChildNodes()) {
            NodeList nlist = getNode().getChildNodes();
            if (nlist.getLength() == 1 && nlist.item(0) instanceof Text) {
                list.add(this);
            }
            for (int i = 0; i < nlist.getLength(); ++i) {
                XmlNode cn = new XmlNode(nlist.item(i));
                if (cn.getNode() instanceof Element) {
                    cn.getChildNodeList(list);
                }
            }
        }
    }

    /**
     * 子ノードの値を数値として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を数値として取得します。
     * @param name 子ノードの名前
     * @return 浮動小数点数として返します。<BR>
     * 値を正しく数値に変換できなかった場合には、“0”(ZERO)を 返します。
     */
    public double getDouble(String name) {
        return getDouble(name,0);
    }

    /**
     * 子ノードの値を数値として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を数値として取得します。
     * @param name 子ノードの名前
     * @param def 初期値
     * @return 浮動小数点数として返します。<BR>
     * 値を正しく数値に変換できなかった場合には、引数の初期値を返します。
     */
    public double getDouble(String name, double def) {
        try {
            return Double.parseDouble(getString(name));
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 子ノードの値を数値として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を数値として取得します。
     * @param name 子ノードの名前
     * @return 浮動小数点数として返します。<BR>
     * 値を正しく数値に変換できなかった場合には、“0”(ZERO)を 返します。
     */
    public float getFloat(String name) {
        return getFloat(name,0);
    }

    /**
     * 子ノードの値を数値として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を数値として取得します。
     * @param name 子ノードの名前
     * @param def 初期値
     * @return 浮動小数点数として返します。<BR>
     * 値を正しく数値に変換できなかった場合には、引数の初期値を返します。
     */
    public float getFloat(String name, float def) {
        try {
            return Float.parseFloat(getString(name));
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 子ノードの値を数値として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を数値として取得します。
     * @param name 子ノードの名前
     * @return 整数として返します。<BR>
     * 値を正しく数値に変換できなかった場合には、“0”(ZERO)を 返します。
     */
    public int getInteger(String name) {
       return getInteger(name,0);
    }

    /**
     * 子ノードの値を数値として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を数値として取得します。
     * @param name 子ノードの名前
     * @param def 初期値
     * @return 整数として返します。<BR>
     * 値を正しく数値に変換できなかった場合には、引数の初期値を返します。
     */
    public int getInteger(String name, int def) {
        try {
            return Integer.parseInt(getString(name));
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 子ノードの値を数値として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を数値として取得します。
     * @param name 子ノードの名前
     * @return 整数として返します。<BR>
     * 値を正しく数値に変換できなかった場合には、“0”(ZERO)を 返します。
     */
    public long getLong(String name) {
            return getLong(name,0);
    }

    /**
     * 子ノードの値を数値として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を数値として取得します。
     * @param name 子ノードの名前
     * @param def 初期値
     * @return 整数として返します。<BR>
     * 値を正しく数値に変換できなかった場合には、引数の初期値を返します。
     */
    public long getLong(String name, long def) {
        try {
            return Long.parseLong(getString(name));
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * このノードのDOMノードオブジェクトを取得します。<BR>
     * <BR>
     * @return DOMノードオブジェクト
     */
    public Node getNode() {
        return this.node;
    }

    /**
     * 下階層からすべてのノードを取得します。<BR>
     * @return 下階層のすべてのノードリスト
     */
    public XmlNode[] getNodeList() {
        List list = new ArrayList();
        getChildNodeList(list);
        return (XmlNode[]) list.toArray(new XmlNode[list.size()]);
    }

    /**
     * ノードの名前を取得します。<BR>
     * <BR>
     * @return ノードの名前
     */
    public String getNodeName() {
        return getNode().getNodeName();
    }

    /**
     * 子ノードの値を文字列として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を文字列として取得します。
     * @param name 子ノードの名前
     * @return 文字列として返します。<BR>
     * 値が存在しなかった場合には、null を返します。
     */
    public String getString(String name) {
        return getString(name, null);
    }

    /**
     * 子ノードの値を文字列として取得します。<BR>
     * <BR>
     * このノードの子ノードの値を文字列として取得します。
     * @param name 子ノードの名前
     * @param def 初期値
     * @return 文字列として返します。<BR>
     * 値が存在しなかった場合には、初期値を返します。
     */
    public String getString(String name, String def) {
        XmlNode cn = lookup(name);
        return (cn != null) ? cn.getValue(def) : def;
    }

    /**
     * このノードのフルパスを取得します。<BR>
     * <BR>
     * このノードのフルパス '/'でつないだパスを取得します。
     * @return ノードのフルパス。<BR>
     */
    public String getURI() {

        Node parent;

        if (uri != null)
            return uri;

        if (getNode() == null) {
            uri = "";
        } else {
            uri = getNode().getNodeName();
            if (getNode() instanceof Attr) {
                Attr at = (Attr) getNode();
                try {
                    parent = at.getOwnerElement();
                } catch (NoSuchMethodError e) {
                    // DOM がレベル１です。親のノードが欲しいな。
                    parent = parent_node;
                }
            } else {
                parent = getNode().getParentNode();
            }
            while (parent != null) {
                uri = parent.getNodeName() + "/" + uri;
                parent = parent.getParentNode();
                if (parent instanceof Document)
                    break;
            }
        }

        return uri;

    }

    /**
     * このノードのフルパスを取得します。<BR>
     * <BR>
     * 引数で指定したノードからのフルパス '/'でつないだパスを取得します。
     * @param root
     * @return 引数で指定したノードからのフルパス。<BR>
     */
    public String getURI(String root) {

        Node parent;
        String node_uri = "";

        if (getNode() != null) {
            node_uri = getNode().getNodeName();
            if (getNode() instanceof Attr) {
                Attr at = (Attr) getNode();
                try {
                    parent = at.getOwnerElement();
                } catch (NoSuchMethodError e) {
                    //DOM がレベル１です。親のノードが欲しいな。
                    parent = parent_node;
                }
            } else {
                parent = getNode().getParentNode();
            }
            while (parent != null) {
                node_uri = parent.getNodeName() + "/" + node_uri;
                if (parent.getNodeName().equals(root))
                    break;
                parent = parent.getParentNode();
                if (parent instanceof Document)
                    break;
            }
        }

        return node_uri;

    }

    /**
     * このノードの値を文字列として取得します。<BR>
     * <BR>
     * このノードの値を文字列として取得します。
     * @return 文字列として返します。<BR>
     * 値が存在しなかった場合には、nullを返します。
     */
    public String getValue() {
        return lookupValue();
    }

    /**
     * このノードの値を文字列として取得します。<BR>
     * <BR>
     * このノードの値を文字列として取得します。
     * @param def 初期値
     * @return 文字列として返します。<BR>
     * 値が存在しなかった場合には、初期値を返します。
     */
    public String getValue(String def) {
        String cont = lookupValue();
        return (cont != null) ? cont : def;
    }

    /**
     * 子ノードの値が偽値であるかチェックします。<BR>
     * <BR>
     * 設定データが偽値である条件は以下の通り。<BR>
     * 値が、“OFF”または“FALSE”の場合。 大文字・小文字の区別なし。<BR>
     * 指定項目が未定義の場合には偽値を返します。<BR>
     * @param name 子ノードの名前
     * @return true：設定データは偽値である false：設定データは真値である。<BR>
     */
    public boolean isFalse(String name) {
        String val = getString(name);
        return (val != null && (val.toUpperCase().equals("OFF") || val.toUpperCase().equals("FALSE")));
    }

    /**
     * 子ノードの値が真値であるかチェックします。<BR>
     * <BR>
     * 設定データが真値である条件は以下の通り。<BR>
     * 値が、“ON”または“TRUE”の場合。 大文字・小文字の区別なし。<BR>
     * 指定項目が未定義の場合には偽値を返します。<BR>
     * @param name 子ノードの名前
     * @return true：設定データは真値である false：設定データは偽値である。<BR>
     */
    public boolean isTrue(String name) {
        String val = getString(name);
        return (val != null && (val.toUpperCase().equals("ON") || val.toUpperCase().equals("TRUE")));
    }

    /**
     * 子ノードの存在をチェックします。<BR>
     * <BR>
     * @param name 子ノードの名前
     * @return true：子ノードが存在する false：子ノードが存在しない。<BR>
     */
    public boolean isValid(String name) {
        return lookup(name) != null;
    }

    /**
     * 子ノードを取得します。<BR>
     * <BR>
     * @param tags 子ノードのパス
     * @return 子ノードのXmlNode。<BR>
     *  存在しない場合は null。<BR>
     */
    public XmlNode lookup(String tags) {
        return lookup(parseTags(tags));
    }

    /**
     * 子ノードを取得します。<BR>
     * <BR>
     * @param tags 子ノードのパスのリスト
     * @return 子ノードのXmlNode。<BR>
     *  存在しない場合は null。<BR>
     */
    public XmlNode lookup(List tags) {

        XmlNode res = null;

        if (getNode() == null)
            return res;
        if (tags.size() == 0)
            return this;

        String tag = (String) tags.iterator().next();
        
        tags.remove(tag);

        if (getNode() instanceof Document) {
            Document doc = (Document) getNode();

            if (doc.hasChildNodes()) {
                NodeList nlist = doc.getChildNodes();
                for (int i = 0; i < nlist.getLength(); ++i) {
                    if (tag.equals(nlist.item(i).getNodeName())) {
                        res = new XmlNode(nlist.item(i));
                        if (tags.size() > 0)
                            res = res.lookup(tags);
                        break;
                    }
                }
            }
        } else if (getNode() instanceof Element) {
            Element ele = (Element) getNode();

            NamedNodeMap alist = ele.getAttributes();
            if (alist != null) {
                if (tags.size() == 0 && alist.getLength() > 0) {
                    Attr at = ele.getAttributeNode(tag);
                    if (at != null)
                        return new XmlNode(at);
                }
            }

            //	      if(tags.size() == 0) {
            //	          Attr at = ele.getAttributeNode(tag);
            //	          if (at != null) return new XmlNode(at);
            //	      }

            if (ele.hasChildNodes()) {
                NodeList nlist = ele.getChildNodes();
                for (int i = 0; i < nlist.getLength(); ++i) {
                    if (tag.equals(nlist.item(i).getNodeName())) {
                        res = new XmlNode(nlist.item(i));
                        if (tags.size() > 0)
                            res = res.lookup(tags);
                        break;
                    }
                }
            }

        }
        return res;

    }

    /**
     * このノードの値を取得します。<BR>
     * <BR>
      * @return 取得した値。<BR>
     * 値が存在しなかった場合には、初期値を返します。
     */
    private String lookupValue() {

        if (value != null)
            return value;

        if ((getNode() instanceof Document) || (getNode() instanceof Element)) {
            if (getNode().hasChildNodes()) {
                NodeList nlist = getNode().getChildNodes();
                for (int i = 0; i < nlist.getLength(); ++i) {
                    Node cn = nlist.item(i);
                    if (cn instanceof Text) {
                        String nv = cn.getNodeValue().trim();
                        if (nv.length() > 0) {
                            value = nv;
                            text = (Text) cn;
                            break;
                        }
                    }
                }
            }
        } else if (getNode() instanceof Attr) {
            Attr atr = (Attr) getNode();
            value = atr.getValue();
            text = null;
        }

        return value;

    }

    /**
     * ノードのパスを配列に変換します。<BR>
     * <BR>
     * '/'つなぎのパスを、配列に変換します。     
     * @param tags ノードのパス
     * @return 取得した値。<BR>
     * 値が存在しなかった場合には、初期値を返します。
     */
    private List parseTags(String tags) {
        List v = new ArrayList();
        if (tags != null) {
            StringTokenizer st = new StringTokenizer(tags, "/");

            while (st.hasMoreTokens())
                v.add(st.nextToken());
        }
        return v;
    }

    /**
     * このノードの値を削除します。<BR>
     * 値がない場合は、処理を行いません。
     */
    public void removeValue() {
        lookupValue();
        if ((getNode() instanceof Document) || (getNode() instanceof Element)) {
            if (text != null) {
                getNode().removeChild(text);
                text = null;
                value = null;
            }
        }

    }

    /**
     * このノードの子ノードのリストを取得します。<BR>
     * <BR>
     * 引数で指定したパスの子ノードのリストを取得します。
     * @param tags ノードのパス
     * @return 子ノードの配列。<BR>
     * 子ノードが存在しなかった場合には、空の配列を返します。
     */
    public XmlNode[] select(String tags) {

        String target;
        XmlNode cn;

        List res = new ArrayList();

        List vtag = parseTags(tags);

        int size = vtag.size();
        if (size > 0) {
            target = (String) vtag.remove(size - 1);
            if ((cn = lookup(vtag)) == null)
                return new XmlNode[0];

        } else
            return new XmlNode[0];

        if (cn.getNode() instanceof Document) {
            Document doc = (Document) cn.getNode();

            if (doc.hasChildNodes()) {
                NodeList nlist = doc.getChildNodes();
                for (int i = 0; i < nlist.getLength(); ++i) {
                    if (target.equals(nlist.item(i).getNodeName())) {
                        res.add(new XmlNode(nlist.item(i)));
                    }
                }
            }
        } else if (cn.getNode() instanceof Element) {
            Element ele = (Element) cn.getNode();

            NamedNodeMap alist = ele.getAttributes();
            if (alist != null) {
                if (alist.getLength() > 0) {
                    Attr at = ele.getAttributeNode(target);
                    if (at != null) {
                        res.add(new XmlNode(at));
                    }
                }
            }

            //          Attr at = (Attr)ele.getAttributeNode(target);
            //          if (at != null) {
            //              res.add(new XmlNode(at));
            //              return res;
            //          }

            if (res.size() == 0 && ele.hasChildNodes()) {
                NodeList nlist = ele.getChildNodes();
                for (int i = 0; i < nlist.getLength(); ++i) {
                    if (target.equals(nlist.item(i).getNodeName())) {
                        res.add(new XmlNode(nlist.item(i)));
                    }
                }
            }

        }

        return (XmlNode[]) res.toArray(new XmlNode[res.size()]);
    }

    /**
     * このノードの名前を変更します。<BR>
     * <BR>
     * @param name 変更する名前
     */
    public void setNodeName(String name) {

        if (getNode() instanceof Element) {

            Element ele = (Element) getNode();

            NodeList list = ele.getChildNodes();

            Node new_node = ele.getOwnerDocument().createElement(name);

            if (list.getLength() > 0) {
                Node items[] = new Node[list.getLength()];
                // なぜか一回、保管しなおさないとうまくいかないみたい(泣)
                for (int i = 0; i < list.getLength(); ++i)
                    items[i] = list.item(i);
                for (int i = 0; i < items.length; ++i)
                    new_node.appendChild(items[i]);
            }
            ele.getParentNode().replaceChild(new_node, ele);
        }
    }

    /**
     * このノードの値を設定します。<BR>
     * <BR>
     * @param str 設定する値。
     */
    public void setValue(String str) {
        lookupValue();
        if ((getNode() instanceof Document) || (getNode() instanceof Element)) {
            if (text == null) {
                text = getNode().getOwnerDocument().createTextNode(str);
                if (getNode().hasChildNodes())
                    getNode().insertBefore(text, getNode().getFirstChild());
                else
                    getNode().appendChild(text);
                value = str;
            } else {
                text.setNodeValue(str);
            }
        } else if (getNode() instanceof Attr) {
            Attr atr = (Attr) getNode();
            atr.setValue(str);
            value = str;
        }

    }

    /**
     * このノードの子ノードの値を設定します。<BR>
     * <BR>
     * @param targetPath 子ノードのパス
     * @param str 設定する値。
     */
    public void setValue(String targetPath, String str) {
        XmlNode cn = lookup(targetPath);
        if (cn != null)
            cn.setValue(str);
    }

}

