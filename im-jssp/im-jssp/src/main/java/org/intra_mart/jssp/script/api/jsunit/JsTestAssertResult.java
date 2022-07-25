package org.intra_mart.jssp.script.api.jsunit;

import java.io.Serializable;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * JsUnitテスト評価結果クラス。
 */
public class JsTestAssertResult implements JsTestTranceform, Serializable {

	private String comment;

	private int lineNumber;

	private String message;

	private String path;

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
	public JsTestAssertResult(String message, String path, int lineNumber) {
		this("", message, path, lineNumber);
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
	public JsTestAssertResult(String comment, String message, String path, int lineNumber) {
		super();
		this.comment = comment;
		this.path = path;
		this.message = message;
		this.lineNumber = lineNumber;
	}

	/**
	 * コメントを取得します。
	 * 
	 * @return コメント
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * 行番号を取得します。
	 * 
	 * @return 行番号
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * メッセージを取得します。
	 * 
	 * @return メッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * ソースパスを取得します。
	 * 
	 * @return ソースパス
	 */
	public String getPath() {
		return path;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestTranceform#tranceform(org.w3c.dom.Element)
	 */
	public Element tranceform(Element parent) {
		Element root = parent.getOwnerDocument().createElement(ASSERTION_NODE_NAME);
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
