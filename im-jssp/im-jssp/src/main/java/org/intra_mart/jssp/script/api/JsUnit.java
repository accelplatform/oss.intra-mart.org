package org.intra_mart.jssp.script.api;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.api.jsunit.JsTestAssertResult;
import org.intra_mart.jssp.script.api.jsunit.JsTestErrorAssertResult;
import org.intra_mart.jssp.script.api.jsunit.JsTestFunctionResult;
import org.intra_mart.jssp.script.api.jsunit.JsTestLuncher;
import org.intra_mart.jssp.script.api.jsunit.JsTestResult;
import org.intra_mart.jssp.script.api.jsunit.Tracer4JsUnit;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.intra_mart.jssp.util.JsUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.w3c.dom.Document;

/**
 * im-JsUnit オブジェクト。
 * JavaScriptのユニットテスト環境を提供するオブジェクトです。
 * @name JsUnit
 * @scope public 
 *
 */
public class JsUnit extends ScriptableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2759022611624153999L;
	
	private static ResourceBundle bundle = ResourceBundle.getBundle("org/intra_mart/jssp/script/api/js-unit");

	/**
	 * 引数オブジェクト。
	 */
	private static class JsUnitArgs {
		private String comment;

		private Object value1;

		private Object value2;

		/**
		 * コンストラクタ
		 * 
		 * @param comment
		 *            コメント
		 * @param value1
		 *            値１
		 * @param value2
		 *            値２
		 */
		public JsUnitArgs(Object comment, Object value1, Object value2) {
			super();
			if (comment != null) {
				this.comment = comment.toString();
			} else {
				this.comment = "";
			}

			this.value1 = value1;

			this.value2 = value2;
		}

		/**
		 * コメントを取得します。
		 * 
		 * @return
		 */
		public String getComment() {
			return comment;
		}

		/**
		 * 値１を取得します。
		 * 
		 * @return
		 */
		public Object getValue1() {
			return value1;
		}

		/**
		 * 値２を取得します。
		 * 
		 * @return
		 */
		public Object getValue2() {
			return value2;
		}
		
		/**
		 *  値の型を解析し、適切な型に変換します。
		 */
		public void parseValues() {
			if (this.value1 instanceof Number) {
				this.value1 = new Double(((Number)this.value1).doubleValue());
			}
			else if (JsUtil.isDate(this.value1)) {
				this.value1 = JsUtil.toDate(this.value1, null);
			}
			
			if (this.value2 instanceof Number) {
				this.value2 = new Double(((Number)this.value2).doubleValue());
			}
			else if (JsUtil.isDate(this.value2)) {
				this.value2 = JsUtil.toDate(this.value2, null);
			}
		}
		
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.javascript.Scriptable#getClassName()
	 */
	public String getClassName() {
		return "JsUnit";
	}

	/**
	 * JAVAコンストラクタ。
	 * 
	 * @scope private
	 */
	public JsUnit() {
		super();
	}

	/**
	 * 評価します。<br>
	 * 
	 * @param value
	 *            評価値
	 * @param comment
	 *            コメント
	 * @param message
	 *            失敗メッセージ
	 */
	private static void executeAssert(boolean value, String comment, String message) {
		JsTestFunctionResult result = getFunctionResult();
		if (result != null) {
			if (!value) {
				result.addAssertResult(new JsTestAssertResult(comment, message, Tracer4JsUnit.getSource(), Tracer4JsUnit.getLine()));
			} else {
				result.addTotal();
			}
		}
	}

	/**
	 * エラー出力。<br>
	 * 
	 * @param value
	 *            評価値
	 * @param comment
	 *            コメント
	 * @param message
	 *            失敗メッセージ
	 */
	private static void executeErrorAssert(String comment, String message) {
		JsTestFunctionResult result = getFunctionResult();
		if (result != null) {
			result.addErrerAssertResult(new JsTestErrorAssertResult(comment, message, Tracer4JsUnit.getSource(), Tracer4JsUnit.getLine()));
		}
	}

	/**
	 * 関数の引数チェックを行います。
	 * 
	 * @param args
	 *            引数の配列
	 * @param count
	 *            最低限必要な引数の数
	 * @return 引数オブジェクト
	 */
	private static JsUnitArgs checkArguments(Object[] args, int count) {
		// コメントなし
		if (args.length == count) {
			if (count == 1) {
				return new JsUnitArgs("", args[0], null);
			} else {
				return new JsUnitArgs("", args[0], args[1]);
			}
		}
		// コメントあり
		else if (args.length == count + 1) {
			if (count == 1) {
				return new JsUnitArgs(args[0], args[1], null);
			} else {
				return new JsUnitArgs(args[0], args[1], args[2]);
			}
		}
		// それ以外はエラー
		else {
			executeErrorAssert(bundle.getString("assert.error.title"), bundle.getString("assert.error.args"));
			return null;
		}
	}

	/**
	 * JavaScriptのモジュールを取得します。<br>
	 * 取得したモジュールより各関数を実行することが可能です。<br>
	 * テスト対象のモジュールを取得する場合に用います。<br>
	 * <br>
	 * 取得に失敗した場合は、nullを返却します。<br>
	 * <br>
	 * 引数のソースパスには、テスト対象のファイルのパスを指定します。<br>
	 * 拡張子(jssp)は付けません。<br>
	 * <br>
	 * 例 system/source.jsを指定する場合<br>
	 * <b>system/source</b><br>
	 * 
	 * @scope public
	 * @param src
	 *            String ソースパス
	 * @return Object JSファイルのオブジェクト
	 * @throws Exception
	 */
	public static Object jsStaticFunction_loadScriptModule(String src) throws Exception {
		if (src != null) {
			ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
			ScriptScope scriptScope = builder.getScriptScope(src);
			return scriptScope;
		} else {
			return null;
		}
	}

	/**
	 * 評価値がTrueであることをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value
	 *            Boolean 評価値
	 */
	public static void jsStaticFunction_assert(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 1);
		boolean result = false;
		if (jsUnitArgs != null) {
			if (jsUnitArgs.getValue1() instanceof Boolean) {
				result = ((Boolean) jsUnitArgs.getValue1()).booleanValue();
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.true"));
			} else {
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.not.boolean.type"));
			}
		}
	}

	/**
	 * 評価値がTrueであることをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value
	 *            Boolean 評価値
	 */
	public static void jsStaticFunction_assertTrue(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 1);
		boolean result = false;
		if (jsUnitArgs != null) {
			if (jsUnitArgs.getValue1() instanceof Boolean) {
				result = ((Boolean) jsUnitArgs.getValue1()).booleanValue();
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.true"));
			} else {
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.not.boolean.type"));
			}
		}
	}

	/**
	 * 評価値がFalseであることをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value
	 *            Boolean 評価値
	 */
	public static void jsStaticFunction_assertFalse(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 1);
		boolean result = false;
		if (jsUnitArgs != null) {
			if (jsUnitArgs.getValue1() instanceof Boolean) {
				result = !(((Boolean) jsUnitArgs.getValue1()).booleanValue());
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.false"));
			} else {
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.not.boolean.type"));
			}
		}
	}

	/**
	 * 評価値と期待値が同じであることをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value1
	 *            Object 期待値
	 * @param value2
	 *            Object 評価値
	 */
	public static void jsStaticFunction_assertEquals(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 2);
		if (jsUnitArgs != null) {
			boolean result = false;
			jsUnitArgs.parseValues();
			if (jsUnitArgs.getValue1() == jsUnitArgs.getValue2()) {
				result = true;
			} else if (jsUnitArgs.getValue1() != null) {
				result = jsUnitArgs.getValue1().equals(jsUnitArgs.getValue2());
			}
			String msg = createMessage(bundle.getString("assert.msg.equals"), new Object[] { jsUnitArgs.getValue1(),jsUnitArgs.getValue2()});
			executeAssert(result, jsUnitArgs.getComment(), msg);
		}
	}

	/**
	 * 評価値と期待値が同じでないことをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value1
	 *            Object 期待値
	 * @param value2
	 *            Object 評価値
	 */
	public static void jsStaticFunction_assertNotEquals(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 2);
		if (jsUnitArgs != null) {
			boolean result = true;
			jsUnitArgs.parseValues();
			if (jsUnitArgs.getValue1() == jsUnitArgs.getValue2()) {
				result = false;
			} else if (jsUnitArgs.getValue1() != null) {
				result = !jsUnitArgs.getValue1().equals(jsUnitArgs.getValue2());
			}
			String msg = createMessage(bundle.getString("assert.msg.not.equals"), new Object[] { jsUnitArgs.getValue1(),jsUnitArgs.getValue2()});
			executeAssert(result, jsUnitArgs.getComment(), msg);
		}
	}

	/**
	 * 評価値がNullであることをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value
	 *            Object 評価値
	 */
	public static void jsStaticFunction_assertNull(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 1);
		if (jsUnitArgs != null) {
			executeAssert(jsUnitArgs.getValue1() == null, jsUnitArgs.getComment(), bundle.getString("assert.msg.null"));
		}
	}

	/**
	 * 評価値がNullでないことをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value
	 *            Object 評価値
	 */
	public static void jsStaticFunction_assertNotNull(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 1);
		if (jsUnitArgs != null) {
			executeAssert(jsUnitArgs.getValue1() != null, jsUnitArgs.getComment(), bundle.getString("assert.msg.not.null"));
		}
	}

	/**
	 * 評価値がNaNであることをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value
	 *            Number 評価値
	 */
	public static void jsStaticFunction_assertNaN(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 1);
		if (jsUnitArgs != null) {
			boolean result = false;
			if (jsUnitArgs.getValue1() instanceof Double) {
				result = Double.isNaN(((Double) jsUnitArgs.getValue1()).doubleValue());
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.nan"));
			} else {
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.not.number.type"));
			}
		}
	}

	/**
	 * 評価値がNaNでないことをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value
	 *            Number 評価値
	 */
	public static void jsStaticFunction_assertNotNaN(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 1);
		if (jsUnitArgs != null) {
			boolean result = false;
			if (jsUnitArgs.getValue1() instanceof Double) {
				result = !(Double.isNaN(((Double) jsUnitArgs.getValue1()).doubleValue()));
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.not.nan"));
			} else {
				executeAssert(result, jsUnitArgs.getComment(), bundle.getString("assert.msg.not.number.type"));
			}
		}
	}

	/**
	 * 評価値がUndefinedであることをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value
	 *            Object 評価値
	 */
	public static void jsStaticFunction_assertUndefined(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 1);
		if (jsUnitArgs != null) {
			executeAssert(jsUnitArgs.getValue1() instanceof Undefined, jsUnitArgs.getComment(), bundle.getString("assert.msg.undefined"));
			return;
		}
	}

	/**
	 * 評価値がUndefinedでないことをチェックします。 <br>
	 * <br>
	 * 
	 * @scope public
	 * @param comment
	 *            ?String テストのコメント
	 * @param value
	 *            Object 評価値
	 */
	public static void jsStaticFunction_assertNotUndefined(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
		JsUnitArgs jsUnitArgs = checkArguments(args, 1);
		if (jsUnitArgs != null) {
			executeAssert(!(jsUnitArgs.getValue1() instanceof Undefined), jsUnitArgs.getComment(), bundle.getString("assert.msg.not.undefined"));
		}
	}

	/**
	 * テストを実行します。 <br>
	 * <br>
	 * テストファイルの実行に失敗した場合（ファイルが存在しない場合など）は、nullを返却します。<br>
	 * <br>
	 * 引数のソースパスには、テストケースを記述したファイルのパスを指定します。<br>
	 * 拡張子(jssp)は付けません。<br>
	 * <br>
	 * 例 system/test.jsを指定する場合<br>
	 * <b>system/test</b><br>
	 * 
	 * @scope public
	 * @param src
	 *            String ソースパス
	 * @param xsl
	 *            ?String xslスタイルシートパス
	 * @return String テスト結果情報(xml)
	 */
	public static String jsStaticFunction_execute(String src, String xsl) {
		try {
			if (xsl == null || xsl.length() == 0) {
				xsl = null;
			}
			Document doc = new JsTestLuncher().execute(src).getDocument(xsl);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(bos));
			return bos.toString("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * カレントのテスト結果保存オブジェクトを取得します。
	 * 
	 * @return カレントのテスト結果保存オブジェクト
	 */
	private static JsTestFunctionResult getFunctionResult() {
		JsTestResult result = (JsTestResult) Tracer4JsUnit.getCurentResult();
		if (result != null) {
			return result.getFunctionResult(Tracer4JsUnit.getFunction());
		} else {
			return null;
		}
	}

	/**
	 * リソースを取得します。
	 * 
	 * @return バンドル
	 */
	public static ResourceBundle getBundle() {
		return bundle;
	}
	
	/**
	 * メッセージ引数を挿入してメッセージを作成します。<br>
	 * メッセージ引数の値がNULLのものは、文字列のnullに変更します。
	 * @param message メッセージ
	 * @param args メッセージ引数
	 * @return
	 */
	private static String createMessage(String message,Object[] args ){
		String[] tmpArgs = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			if(args[i] != null) {
				tmpArgs[i] = args[i].toString();
			}
			else {
				tmpArgs[i] = "null";
			}
		}
		return MessageFormat.format(message, (Object[])tmpArgs);
	}

}
