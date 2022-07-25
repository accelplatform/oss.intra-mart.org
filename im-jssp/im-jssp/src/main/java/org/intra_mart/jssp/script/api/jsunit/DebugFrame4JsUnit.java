package org.intra_mart.jssp.script.api.jsunit;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;

/**
 * JsUnit用のトレースフレーム
 */
class DebugFrame4JsUnit implements DebugFrame {

	/** デバッグ情報 */
	private DebuggableScript fnOrScript;

	/**
	 * JsUnit用のデバッグフレームの生成
	 * 
	 * @param fnOrScript
	 *            デバッグ情報
	 */
	DebugFrame4JsUnit(DebuggableScript fnOrScript) {

		this.fnOrScript = fnOrScript;

		// ソース名、関数名取得
		Tracer4JsUnit.setSource(fnOrScript.getSourceName());

	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.javascript.debug.DebugFrame#onEnter(jp.co.intra_mart.system.javascript.Context,
	 *      jp.co.intra_mart.system.javascript.Scriptable,
	 *      jp.co.intra_mart.system.javascript.Scriptable, java.lang.Object[])
	 */
	public void onEnter(Context cx, Scriptable activation, Scriptable thisObj, Object[] args) {

	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.javascript.debug.DebugFrame#onExceptionThrown(jp.co.intra_mart.system.javascript.Context,
	 *      java.lang.Throwable)
	 */
	public void onExceptionThrown(Context cx, Throwable ex) {
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.javascript.debug.DebugFrame#onExit(jp.co.intra_mart.system.javascript.Context,
	 *      boolean, java.lang.Object)
	 */
	public void onExit(Context cx, boolean byThrow, Object resultOrException) {
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.javascript.debug.DebugFrame#onLineChange(jp.co.intra_mart.system.javascript.Context,
	 *      int)
	 */
	public void onLineChange(Context cx, int lineNumber) {

		Object obj = Tracer4JsUnit.getCurentResult();
		if (obj instanceof JsTestResult) {
			if(!Tracer4JsUnit.getFunction().equals(this.fnOrScript.getFunctionName())){
				return;
			}
		}

		// 現在の関数情報の取得
		Tracer4JsUnit.setSource(this.fnOrScript.getSourceName());
		Tracer4JsUnit.setFunction(this.fnOrScript.getFunctionName());
		Tracer4JsUnit.setLine(lineNumber);
	}

}
