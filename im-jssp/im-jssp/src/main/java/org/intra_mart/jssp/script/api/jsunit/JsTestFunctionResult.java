package org.intra_mart.jssp.script.api.jsunit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

/**
 * JsUnitテスト関数結果クラス。
 */
public class JsTestFunctionResult implements JsTestTranceform, JsTestCounter, Serializable {

	private List<JsTestAssertResult> asserts = new ArrayList<JsTestAssertResult>();

	private List<JsTestAssertResult> errors = new ArrayList<JsTestAssertResult>();

	private String name;

	private int total = 0;

	private List<JsTestAssertResult> totals = new ArrayList<JsTestAssertResult>();
	
	private long time = 0;


	/**
	 * 実行時間を取得します。
	 * @return 実行時間
	 */
	public long getTime() {
		return time;
	}

	/**
	 * 実行時間を取得設定します。
	 * @param time 実行時間
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 *            関数名
	 */
	public JsTestFunctionResult(String name) {
		super();
		this.name = name;
	}

	/**
	 * JsUnitテスト評価結果を保管します。
	 * 
	 * @param result
	 *            JsUnitテスト評価結果クラス
	 */
	public void addAssertResult(JsTestAssertResult result) {
		this.totals.add(result);
		this.asserts.add(result);
		addTotal();
	}

	/**
	 * JsUnitテストエラー結果を保管します。
	 * 
	 * @param result
	 *            JsUnitテストエラー結果クラス
	 */
	public void addErrerAssertResult(JsTestErrorAssertResult result) {
		this.totals.add(result);
		this.errors.add(result);
		addTotal();
	}

	/**
	 * 評価をカウントします。
	 */
	public void addTotal() {
		this.total++;
	}

	/**
	 * すべての評価結果（エラーも含む）を取得します。
	 * 
	 * @return 評価結果の配列
	 */
	public JsTestAssertResult[] getAll() {
		return (JsTestAssertResult[]) this.totals.toArray(new JsTestAssertResult[this.asserts.size()]);
	}

	/**
	 * すべての評価結果を取得します。
	 * 
	 * @return 評価結果の配列
	 */
	public JsTestAssertResult[] getAsserts() {
		return (JsTestAssertResult[]) this.asserts.toArray(new JsTestAssertResult[this.asserts.size()]);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getErrorCount()
	 */
	public int getErrorCount() {
		return this.errors.size();
	}

	/**
	 * すべてのエラー結果を取得します。
	 * 
	 * @return エラー結果の配列
	 */
	public JsTestAssertResult[] getErrors() {
		return (JsTestAssertResult[]) this.errors.toArray(new JsTestAssertResult[this.errors.size()]);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getFailureCount()
	 */
	public int getFailureCount() {
		return this.asserts.size();
	}

	/**
	 * 関数名を取得します。
	 * 
	 * @return 関数名
	 */
	public String getName() {
		return name;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getTotal()
	 */
	public int getTotal() {
		return this.total;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestTranceform#tranceform(org.w3c.dom.Element)
	 */
	public Element tranceform(Element parent) {
		Element root = parent.getOwnerDocument().createElement(FUNCTION_NODE_NAME);
		parent.appendChild(root);
		root.setAttribute(NAME_ATTR_NAME, getName());
		root.setAttribute(TOTAL_ATTR_NAME, String.valueOf(getTotal()));
		root.setAttribute(FAILURE_ATTR_NAME, String.valueOf(getFailureCount()));
		root.setAttribute(ERROR_ATTR_NAME, String.valueOf(getErrorCount()));
		root.setAttribute(TIME_ATTR_NAME, String.valueOf(getTime()));

		JsTestAssertResult[] fails = getAll();
		for (int i = 0; i < fails.length; i++) {
			fails[i].tranceform(root);
		}
		return root;
	}
}
