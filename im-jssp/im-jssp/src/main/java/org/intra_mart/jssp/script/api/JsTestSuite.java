package org.intra_mart.jssp.script.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * im-JsUnitのテストスイートオブジェクト。<BR>
 * JavaScriptのユニットテスト環境でテストケースを集約するオブジェクトです。<br>
 * <br>
 * テストスイートは、defineTestSuite関数内で使用します。<br>
 * <PRE>
 * function defineTestSuite() {
 * 	
 * 	var suite = new JsTestSuite("テストの集まり");
 * 	suite.addTest("１つ目のテストです。","test2");
 * 	suite.addTest("２つ目のテストです。","test3");
 * 	suite.addTest("３つ目のテストです。","test_suite2");
 * 	return suite;
 * }
 * </PRE>
 * @name JsTestSuite
 * @scope public
 *
 */
public class JsTestSuite extends ScriptableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2180137890679689166L;

	public static class Test {
		private String comment = new String();

		private Object target;

		/**
		 * テストケースクラス
		 * 
		 * @param comment
		 *            コメント
		 * @param target
		 *            テストターゲット(ソースパスかテストスウィートのいずれか)
		 */
		public Test(String comment, Object target) {
			super();
			this.comment = comment;
			this.target = target;
		}

		/**
		 * コメントを取得します。
		 * 
		 * @return コメント
		 */
		public String getComment() {
			if (comment != null) {
				return comment;
			} else {
				return new String();
			}
		}

		/**
		 * テストターゲットを取得します。
		 * 
		 * @return
		 */
		public Object getTarget() {
			return target;
		}

	}

	/**
	 * コンストラクタ。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            String コメント
	 * @return JsTestSuiteオブジェクト
	 */
	public static Object jsConstructor(Context cx, java.lang.Object[] args, Function ctorObj, boolean inNewExpr) {
		// JSから "new "付きで呼ばれたか？
		if (!inNewExpr) {
			return null;
		}

		try {
			if (args.length == 1 && args[0] instanceof String) {
				return new JsTestSuite((String) args[0]);
			} else {
				return new JsTestSuite("");
			}
		} catch (Exception e) {
			return null;
		}
	}

	private String comment = new String();

	private List<Test> list = new ArrayList<Test>();

	private String path = new String();

	/**
	 * JAVAコンストラクタ。
	 * 
	 * @scope private
	 */
	public JsTestSuite() {
		super();
	}

	/**
	 * JAVAコンストラクタ。
	 * 
	 * @scope private
	 */
	public JsTestSuite(String comment) {
		super();
		this.comment = comment;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.javascript.Scriptable#getClassName()
	 */
	public String getClassName() {
		return "JsTestSuite";
	}

	/**
	 * コメントを取得します。
	 * 
	 * @return コメント
	 */
	public String getComment() {
		if (this.comment != null) {
			return this.comment;
		} else {
			return new String();
		}
	}

	/**
	 * ソースパスを取得します。
	 * 
	 * @return ソースパス
	 */
	public String getPath() {
		if (this.path != null) {
			return this.path;
		} else {
			return new String();
		}
	}

	/**
	 * テストスウィートに含まれるテストケースの反復子を取得します。<br>
	 * 型は{@link JsTestSuite.Test}です。
	 * 
	 * @return テストケースの反復子
	 */
	public Iterator iterator() {
		return list.iterator();
	}

	/**
	 * テストを追加します。
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param test
	 *            Object テストパスまたはJsTestSuit
	 */
	public void jsFunction_addTest(Object value1, Object value2) {
		String comment = "";
		// 引数１つ
		if (value2 instanceof Undefined) {
			if (value1 instanceof String) {
				list.add(new Test(comment, value1));
			} else if (value1 instanceof JsTestSuite) {
				list.add(new Test(comment, value1));
			}
		}
		// 引数２つ
		else {
			if (value1 instanceof String) {
				comment = (String) value1;
			}

			if (value2 instanceof String) {
				list.add(new Test(comment, value2));
			} else if (value2 instanceof JsTestSuite) {
				list.add(new Test(comment, value2));
			}
		}
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

}
