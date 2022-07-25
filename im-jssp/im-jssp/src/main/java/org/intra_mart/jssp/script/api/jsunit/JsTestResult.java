package org.intra_mart.jssp.script.api.jsunit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * JsUnitテストケース結果クラス。
 */
public class JsTestResult implements JsTestTranceform, JsTestCounter, Serializable {

	private String comment;

	private Map<String, JsTestFunctionResult> functionMap = new HashMap<String, JsTestFunctionResult>();

	private List<JsTestFunctionResult> functions = new ArrayList<JsTestFunctionResult>();

	private String path;

	/**
	 * コンストラクタ
	 * 
	 * @param comment
	 *            コメント
	 * @param path
	 *            ソースパス
	 */
	public JsTestResult(String comment, String path) {
		super();
		this.comment = comment;
		this.path = path;
	}

	/**
	 * JsUnitテスト関数結果の保持クラスを追加します。
	 * 
	 * @param result
	 *            JsUnitテスト関数評価結果クラス
	 */
	public void addFunctionResult(JsTestFunctionResult result) {
		this.functions.add(result);
		this.functionMap.put(result.getName(), result);
	}

	/**
	 * コメントを取得します。
	 * 
	 * @return コメント
	 */
	public String getComment() {
		return comment;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getErrorCount()
	 */
	public int getErrorCount() {
		int result = 0;
		JsTestFunctionResult[] funcions = getFunctions();

		for (int i = 0; i < funcions.length; i++) {
			result += funcions[i].getErrorCount();
		}
		return result;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getFailureCount()
	 */
	public int getFailureCount() {
		int result = 0;
		JsTestFunctionResult[] funcions = getFunctions();

		for (int i = 0; i < funcions.length; i++) {
			result += funcions[i].getFailureCount();
		}
		return result;
	}

	/**
	 * 関数名より、JsUnitテスト関数結果クラスを取得します。<br>
	 * <br>
	 * 存在しない場合は、新規に作成して、返却します。
	 * 
	 * @param name
	 *            関数名
	 * @return JsUnitテスト関数評価結果クラス
	 */
	public JsTestFunctionResult getFunctionResult(String name) {
		JsTestFunctionResult result = (JsTestFunctionResult) this.functionMap.get(name);
		if (result == null) {
			result = newFunctionResult(name);
		}
		return result;
	}

	/**
	 * すべてのJsUnitテスト関数結果クラスを取得します。
	 * 
	 * @return JsUnitテスト関数結果クラスの配列
	 */
	public JsTestFunctionResult[] getFunctions() {
		return (JsTestFunctionResult[]) this.functions.toArray(new JsTestFunctionResult[this.functions.size()]);
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
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getTotal()
	 */
	public int getTotal() {
		int result = 0;
		JsTestFunctionResult[] funcions = getFunctions();

		for (int i = 0; i < funcions.length; i++) {
			result += funcions[i].getTotal();
		}
		return result;
	}

	/**
	 * 新規にJsUnitテスト関数結果クラスを作成します。
	 * 
	 * @param name
	 *            関数名
	 * @return JsUnitテスト関数結果クラス
	 */
	public JsTestFunctionResult newFunctionResult(String name) {
		JsTestFunctionResult result = new JsTestFunctionResult(name);
		addFunctionResult(result);
		return result;
	}

	/**
	 * ソースパスを設定します。
	 * 
	 * @param path
	 *            ソースパス
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestTranceform#tranceform(org.w3c.dom.Element)
	 */
	public Element tranceform(Element parent) {
		Element root = parent.getOwnerDocument().createElement(TEST_NODE_NAME);
		parent.appendChild(root);
		if (getComment() != null && getComment().length() > 0) {
			root.setAttribute(COMMENT_ATTR_NAME, getComment());
		}
		root.setAttribute(PATH_ATTR_NAME, getPath());
		root.setAttribute(TOTAL_ATTR_NAME, String.valueOf(getTotal()));
		root.setAttribute(FAILURE_ATTR_NAME, String.valueOf(getFailureCount()));
		root.setAttribute(ERROR_ATTR_NAME, String.valueOf(getErrorCount()));
		root.setAttribute(TIME_ATTR_NAME, String.valueOf(getTime()));

		JsTestFunctionResult[] funcions = getFunctions();

		for (int i = 0; i < funcions.length; i++) {
			funcions[i].tranceform(root);
		}

		return root;
	}

	/* (非 Javadoc)
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getTime()
	 */
	public long getTime() {
		long result = 0;
		JsTestFunctionResult[] funcions = getFunctions();

		for (int i = 0; i < funcions.length; i++) {
			result += funcions[i].getTime();
		}
		return result;
	}

}
