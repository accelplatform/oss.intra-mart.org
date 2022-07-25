package org.intra_mart.jssp.script.api.jsunit;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * JsUnitテストエラー結果クラス。
 */
public class JsTestErrorAssertResult extends JsTestAssertResult {

	/**
	 * コンストラクタ
	 * 
	 * @param message
	 *            メッセージ
	 * @param path
	 *            ソースパス
	 * @param lineNumber
	 *            行番号
	 */
	public JsTestErrorAssertResult(String message, String path, int lineNumber) {
		super(message, path, lineNumber);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param comment
	 *            コメント
	 * @param message
	 *            メッセージ
	 * @param path
	 *            ソースパス
	 * @param lineNumber
	 *            行番号
	 */
	public JsTestErrorAssertResult(String comment, String message, String path, int lineNumber) {
		super(comment, message, path, lineNumber);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestTranceform#tranceform(org.w3c.dom.Element)
	 */
	public Element tranceform(Element parent) {
		Element root = parent.getOwnerDocument().createElement(ERROR_ASSERTION_NODE_NAME);
		parent.appendChild(root);
		if (getComment() != null && getComment().length() > 0) {
			root.setAttribute(COMMENT_ATTR_NAME, getComment());
		}
		root.setAttribute(LINE_ATTR_NAME, String.valueOf(getLineNumber()));
		Text text = parent.getOwnerDocument().createTextNode(getMessage());
		root.appendChild(text);
		return root;
	}

}
