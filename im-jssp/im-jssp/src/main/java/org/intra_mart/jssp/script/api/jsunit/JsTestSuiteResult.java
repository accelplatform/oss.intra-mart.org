package org.intra_mart.jssp.script.api.jsunit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

/**
 * JsUnitテストスウィート結果クラス
 */
public class JsTestSuiteResult implements JsTestTranceform, JsTestCounter, Serializable {

	private List<Object> allTests = new ArrayList<Object>();

	private String comment = "";

	private List<JsTestErrorAssertResult> errors = new ArrayList<JsTestErrorAssertResult>();

	private String path = "";

	private List<JsTestResult> tests = new ArrayList<JsTestResult>();

	private List<JsTestSuiteResult> testSuites = new ArrayList<JsTestSuiteResult>();

	/**
	 * コンストラクタ
	 * 
	 * @param comment
	 *            コメント
	 * @param path
	 *            ソースパス
	 */
	public JsTestSuiteResult(String comment, String path) {
		super();
		this.path = path;
		this.comment = comment;
	}

	/**
	 * JsUnitテストエラー結果を追加します。
	 * 
	 * @param result
	 *            JsUnitテストエラー結果クラス
	 */
	public void addErrerAssertResult(JsTestErrorAssertResult result) {
		this.errors.add(result);
	}

	/**
	 * JsUnitテスト結果を保管します。
	 * 
	 * @param result
	 *            JsUnitテスト結果クラス
	 */
	public void addTestResult(JsTestResult result) {
		this.tests.add(result);
		this.allTests.add(result);
	}

	/**
	 * JsUnitテストスウィート結果クラスを保管します。
	 * 
	 * @param result
	 *            JsUnitテストスウィート結果クラス
	 */
	public void addTestSuiteResult(JsTestSuiteResult result) {
		this.testSuites.add(result);
		this.allTests.add(result);
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
	 * テスト結果のDOMを取得します。
	 * 
	 * @return テスト結果のDOM
	 */
	public Document getDocument() {
		return getDocument(null);
	}

	/**
	 * テスト結果のDOMを取得します。<br>
	 * <br>
	 * 引数のパスに対するXSLディレクティブを追加します。
	 * 
	 * @param xsl
	 *            xslファイルパス
	 * @return テスト結果のDOM
	 */
	public Document getDocument(String xsl) {

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document doc = documentBuilder.newDocument();
			// XSLスタイルシートが設定されている！！
			if (xsl != null) {
				ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"" + xsl + "\"");
				doc.appendChild(pi);
			}
			Element root = doc.createElement(ROOT_NODE_NAME);
			doc.appendChild(root);
			if (getComment() != null && getComment().length() > 0) {
				root.setAttribute(COMMENT_ATTR_NAME, getComment());
			}
			root.setAttribute(PATH_ATTR_NAME, getPath());
			root.setAttribute(TOTAL_ATTR_NAME, String.valueOf(getTotal()));
			root.setAttribute(FAILURE_ATTR_NAME, String.valueOf(getFailureCount()));
			root.setAttribute(ERROR_ATTR_NAME, String.valueOf(getErrorCount()));
			root.setAttribute(TIME_ATTR_NAME, String.valueOf(getTime()));

			for (Iterator iter = this.errors.iterator(); iter.hasNext();) {
				Object element = (Object) iter.next();

				// テストケースの場合
				if (element instanceof JsTestTranceform) {
					((JsTestTranceform) element).tranceform(root);
				}
			}
			for (Iterator iter = iterator(); iter.hasNext();) {
				Object element = (Object) iter.next();

				// テストケースの場合
				if (element instanceof JsTestTranceform) {
					((JsTestTranceform) element).tranceform(root);
				}
			}
			return doc;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getErrorCount()
	 */
	public int getErrorCount() {
		int result = this.errors.size();
		for (Iterator iter = iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			// テストケースの場合
			if (element instanceof JsTestCounter) {
				result += ((JsTestCounter) element).getErrorCount();
			}
		}
		return result;
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
		int result = 0;
		for (Iterator iter = iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			// テストケースの場合
			if (element instanceof JsTestCounter) {
				result += ((JsTestCounter) element).getFailureCount();
			}
		}
		return result;
	}

	/**
	 * ソースパスを取得します。
	 * 
	 * @return ソースパス
	 */
	public String getPath() {
		return path;
	}

	/**
	 * すべてのJsUnitテスト結果クラスを取得します。
	 * 
	 * @return JsUnitテスト結果クラスの配列
	 */
	public JsTestResult[] getTests() {
		return (JsTestResult[]) this.tests.toArray(new JsTestResult[this.tests.size()]);
	}

	/**
	 * すべてのJsUnitテストスウィート結果クラスを取得します。
	 * 
	 * @return JsUnitテストスウィート結果クラスの配列
	 */
	public JsTestSuiteResult[] getTestSuites() {
		return (JsTestSuiteResult[]) this.testSuites.toArray(new JsTestSuiteResult[this.testSuites.size()]);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getTotal()
	 */
	public int getTotal() {
		int result = this.errors.size();
		for (Iterator iter = iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			// テストケースの場合
			if (element instanceof JsTestCounter) {
				result += ((JsTestCounter) element).getTotal();
			}
		}
		return result;
	}

	/**
	 * すべての結果クラスの反復子を取得します。<br>
	 * この反復子には、JsUnitテスト結果クラスとJsUnitテストスウィート結果クラスが含まれます、
	 * 
	 * @return 結果クラスの反復子
	 */
	public Iterator iterator() {
		return this.allTests.iterator();
	}

	/**
	 * JsUnitテスト結果クラスを作成します。
	 * 
	 * @param comment
	 *            コメント
	 * @param path
	 *            ソースパス
	 * @param result
	 *            JsUnitテスト結果クラス
	 */
	public JsTestResult newTestResult(String comment, String path) {
		JsTestResult result = new JsTestResult(comment, path);
		addTestResult(result);
		return result;
	}

	/**
	 * JsUnitテストスウィート結果クラスを作成します。
	 * 
	 * @param comment
	 *            コメント
	 * @param path
	 *            ソースパス
	 * @param result
	 *            JsUnitテストスウィート結果クラス
	 */
	public JsTestSuiteResult newTestSuiteResult(String comment, String path) {
		JsTestSuiteResult result = new JsTestSuiteResult(comment, path);
		addTestSuiteResult(result);
		return result;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestTranceform#tranceform(org.w3c.dom.Element)
	 */
	public Element tranceform(Element parent) {
		Element root = parent.getOwnerDocument().createElement(SUIT_NODE_NAME);
		parent.appendChild(root);
		if (getComment() != null && getComment().length() > 0) {
			root.setAttribute(COMMENT_ATTR_NAME, getComment());
		}
		root.setAttribute(PATH_ATTR_NAME, getPath());
		root.setAttribute(TOTAL_ATTR_NAME, String.valueOf(getTotal()));
		root.setAttribute(FAILURE_ATTR_NAME, String.valueOf(getFailureCount()));
		root.setAttribute(ERROR_ATTR_NAME, String.valueOf(getErrorCount()));
		root.setAttribute(TIME_ATTR_NAME, String.valueOf(getTime()));

		for (Iterator iter = this.errors.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			// テストケースの場合
			if (element instanceof JsTestTranceform) {
				((JsTestTranceform) element).tranceform(root);
			}
		}
		for (Iterator iter = iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			// テストケースの場合
			if (element instanceof JsTestTranceform) {
				((JsTestTranceform) element).tranceform(root);
			}
		}
		return root;
	}
	
	/* (非 Javadoc)
	 * @see jp.co.intra_mart.system.session.jsunit.JsTestCounter#getTime()
	 */
	public long getTime() {
		long result = 0;
		for (Iterator iter = iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			// テストケースの場合
			if (element instanceof JsTestCounter) {
				result += ((JsTestCounter) element).getTime();
			}
		}
		return result;
	}

}
