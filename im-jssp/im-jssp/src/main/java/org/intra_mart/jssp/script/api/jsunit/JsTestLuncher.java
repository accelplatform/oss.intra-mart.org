package org.intra_mart.jssp.script.api.jsunit;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.api.JsTestSuite;
import org.intra_mart.jssp.script.api.JsUnit;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.debug.Debugger;

/**
 * JsUnitテスト実行ランチャークラス。
 */
public class JsTestLuncher {
	private static Logger logger = Logger.getLogger();

	private final static String ROOT_COMMENT = "root";

	private final static String PRIFIX_TEST = "test";

	private final static String TEST_SUITE = "defineTestSuite";

	private final static String ONE_TIME_SET_UP_NAME = "oneTimeSetUp";

	private final static String ONE_TIME_TEAR_DOWN_NAME = "oneTimeTearDown";

	private final static String SET_UP_NAME = "setUp";

	private final static String TEAR_DOWN_NAME = "tearDown";

	private Context cx = Context.enter();

	private Set<String> tests = new HashSet<String>();

	/**
	 * コンストラクタ
	 */
	public JsTestLuncher() {
		super();
	}

	/**
	 * テストを実行します。
	 * 
	 * @param path
	 *            テストケースのパス。
	 * @return テスト結果
	 */
	public JsTestSuiteResult execute(String path) {
		Debugger debugger = this.cx.getDebugger();
		int level = this.cx.getOptimizationLevel();
		try {
			// eBuilder(Page Producer)用のデバッガーをセット
			this.cx.setDebugger(new Tracer4JsUnit(), this.cx.getDebuggerContextData());
			this.cx.setOptimizationLevel(-1);

			// テスト結果のルートを作成
			JsTestSuiteResult root = new JsTestSuiteResult(ROOT_COMMENT, path);
			// カレントのテスト結果を初期化
			Tracer4JsUnit.setCurrentResult(null);
			executeChild(root, "", path);
			return root;
		} finally {
			if (debugger != null) {
				this.cx.setDebugger(debugger, this.cx.getDebuggerContextData());
				this.cx.setOptimizationLevel(level);
			}
		}
	}

	/**
	 * 子供のテストケースを実行します。
	 * 
	 * @param parent
	 *            親JsUnitテストスウィート結果クラス
	 * @param src
	 *            対象のJsUnitテストスウィートクラス
	 */
	private void executeChild(JsTestSuiteResult parent, JsTestSuite src) {

		try {
			JsTestSuiteResult testSuitResult = parent.newTestSuiteResult(src.getComment(), src.getPath());
			for (Iterator i = src.iterator(); i.hasNext();) {
				JsTestSuite.Test element = (JsTestSuite.Test) i.next();
				if (element.getTarget() instanceof String) {
					executeChild(testSuitResult, element.getComment(), (String) element.getTarget());
				} else if (element.getTarget() instanceof JsTestSuite) {
					executeChild(testSuitResult, (JsTestSuite) element.getTarget());
				}
			}
		} catch (Exception e) {
			// 何もしない
		}
	}

	/**
	 * 子供のテストケースを実行します。
	 * 
	 * @param parent
	 *            親JsUnitテストスウィート結果クラス
	 * @param comment
	 *            コメント
	 * @param src
	 *            ソースパス
	 */
	private void executeChild(JsTestSuiteResult parent, String comment, String src) {

		ScriptScope scope = null;

		if (tests.contains(src)) {
			return;
		} else {
			tests.add(src);
		}

		try {
			Tracer4JsUnit.setCurrentResult(parent);
			scope = (ScriptScope) JsUnit.jsStaticFunction_loadScriptModule((String) src);
			Object function = scope.get(TEST_SUITE, scope);

			// テストスイートファイル
			if (function instanceof Function) {
				Object result = ((Function) function).call(this.cx, scope, scope, new Object[0]);
				if (result instanceof JsTestSuite) {
					JsTestSuite testSuit = (JsTestSuite) result;
					testSuit.setPath(src);
					executeChild(parent, testSuit);
				}
			}
			// 普通のテストファイル
			else {
				try {
					JsTestResult currentResult = parent.newTestResult(comment, src);
					Tracer4JsUnit.setCurrentResult(currentResult);

					Object oneTimeSetUpFunc = scope.get(ONE_TIME_SET_UP_NAME, scope);
					if (oneTimeSetUpFunc instanceof Function) {
						Tracer4JsUnit.setSource(src);
						Tracer4JsUnit.setFunction(ONE_TIME_SET_UP_NAME);
						((Function) oneTimeSetUpFunc).call(this.cx, scope, scope, new Object[0]);
					}

					try {
						Object setUpFunc = scope.get(SET_UP_NAME, scope);
						if (!(setUpFunc instanceof Function)) {
							setUpFunc = null;
						}

						Object tearDownFunc = scope.get(TEAR_DOWN_NAME, scope);
						if (!(tearDownFunc instanceof Function)) {
							tearDownFunc = null;
						}

						Object[] ids = scope.getAllIds();
						for (int i = 0; i < ids.length; i++) {
							String name = (String) ids[i];
							Object target = scope.get(name, scope);
							Tracer4JsUnit.setSource(src);
							if (target instanceof Function && name.startsWith(PRIFIX_TEST)) {
								if (setUpFunc != null) {
									Tracer4JsUnit.setSource(src);
									Tracer4JsUnit.setFunction(SET_UP_NAME);
									((Function) setUpFunc).call(this.cx, scope, scope, new Object[0]);
								}

								Tracer4JsUnit.setSource(src);
								Tracer4JsUnit.setFunction(name);
								JsTestFunctionResult currentFunctionResult = currentResult.getFunctionResult(name);
								Date startDate = new Date();
								((Function) target).call(this.cx, scope, scope, new Object[0]);
								Date endDate = new Date();
								currentFunctionResult.setTime(endDate.getTime() - startDate.getTime());

								if (tearDownFunc != null) {
									Tracer4JsUnit.setSource(src);
									Tracer4JsUnit.setFunction(TEAR_DOWN_NAME);
									((Function) tearDownFunc).call(this.cx, scope, scope, new Object[0]);
								}
							}
						}
					} catch (Exception ex) {
						addTestResult(ex, null);
						logger.debug(ex.getMessage(), ex);
					}

					Object oneTimeTearDownFunc = scope.get(ONE_TIME_TEAR_DOWN_NAME, scope);
					if (oneTimeTearDownFunc instanceof Function) {
						Tracer4JsUnit.setSource(src);
						Tracer4JsUnit.setFunction(ONE_TIME_TEAR_DOWN_NAME);
						((Function) oneTimeTearDownFunc).call(this.cx, scope, scope, new Object[0]);
					}
				} catch (Exception ex) {
					addTestResult(ex, null);
					logger.debug(ex.getMessage(), ex);
				}
			}
		} catch (Exception ex) {
			addTestResult(ex, src);
			logger.debug(ex.getMessage(), ex);
		}
	}
	
		
	/**
	 * @param ex
	 */
	private void addTestResult(Exception ex, String src){
		if(src == null){
			src = Tracer4JsUnit.getSource();
		}
		
		JsTestErrorAssertResult error = 
				new JsTestErrorAssertResult(JsUnit.getBundle().getString("assert.error.title"),
											ex.toString(), 
											src, 
											Tracer4JsUnit.getLine());

		Object obj = Tracer4JsUnit.getCurentResult();
		if (obj instanceof JsTestResult) {
			JsTestResult testResult = (JsTestResult) obj;
			if (testResult != null) {
				JsTestFunctionResult result = testResult.getFunctionResult(Tracer4JsUnit.getFunction());
				if (result != null) {
					result.addErrerAssertResult(error);
				}
			}
		}
		else if (obj instanceof JsTestSuiteResult) {
			JsTestSuiteResult testSuitResult = (JsTestSuiteResult) obj;
			testSuitResult.addErrerAssertResult(error);
		}
	}
}
