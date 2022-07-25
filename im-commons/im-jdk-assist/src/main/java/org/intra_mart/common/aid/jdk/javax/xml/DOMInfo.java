package org.intra_mart.common.aid.jdk.javax.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * DOM情報インタフェース。<BR>
 * <BR>
 * DOMのドキュメント、ルートノードを取得するためのインターフェース<BR>
 */

public interface DOMInfo {
    /**
     * ドキュメントノードの取得。<BR>
     * <BR>
     * ドキュメントノードを取得します。
     * @return ドキュメントノード
     */
    public Document getDocumet();

    /**
     * ルートノードの取得。<BR>
     * <BR>
     * ルートのノードを取得します。
     * @return ルートノード<BR>
     * 存在しない場合はnull
     */
    public Node getRootNode();
}
