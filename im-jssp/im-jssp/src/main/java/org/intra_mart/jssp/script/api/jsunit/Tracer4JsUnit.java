package org.intra_mart.jssp.script.api.jsunit;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;
import org.mozilla.javascript.debug.Debugger;

/**
 * JsUnit用のデバッガー
 */
public class Tracer4JsUnit implements Debugger {

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.javascript.debug.Debugger#getFrame(jp.co.intra_mart.system.javascript.Context,
	 *      jp.co.intra_mart.system.javascript.debug.DebuggableScript)
	 */
	public DebugFrame getFrame(Context cx, DebuggableScript fnOrScript) {

		// JsUnit用のデバッグフレームを生成
		return new DebugFrame4JsUnit(fnOrScript);

	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.intra_mart.system.javascript.debug.Debugger#handleCompilationDone(jp.co.intra_mart.system.javascript.Context,
	 *      jp.co.intra_mart.system.javascript.debug.DebuggableScript,
	 *      java.lang.String)
	 */
	public void handleCompilationDone(Context cx, DebuggableScript fnOrScript, String source) {
		// 何もしません
	}

	/** ThreadLocal : 関数 */
	private static ThreadLocal<String> threadLoacal4Function = new ThreadLocal<String>();

	/** ThreadLocal : ソース */
	private static ThreadLocal<String> threadLoacal4Source = new ThreadLocal<String>();

	/** ThreadLocal : 行番号 */
	private static ThreadLocal<Integer> threadLoacal4Line = new ThreadLocal<Integer>();

	/** ThreadLocal : 現在のテスト結果 */
	private static ThreadLocal<Object> threadLoacal4Result = new ThreadLocal<Object>();

	/**
	 * 現在の行番号を設定します。
	 * 
	 * @param line
	 *            行番号
	 */
	public static void setLine(int line) {
		threadLoacal4Line.set(new Integer(line));
	}

	/**
	 * 現在実行中の行番号を取得します。
	 * 
	 * @return 現在の行番号
	 */
	public static int getLine() {
		Integer i = (Integer) threadLoacal4Line.get();
		if (i != null) {
			return i.intValue();
		} else {
			return -1;
		}
	}

	/**
	 * 現在の関数名を設定します。
	 * 
	 * @param name
	 *            関数名
	 */
	public static void setFunction(String name) {
		threadLoacal4Function.set(name);
	}

	/**
	 * 現在実行中の関数を取得します。
	 * 
	 * @return 関数名
	 */
	public static String getFunction() {
		String name = (String) threadLoacal4Function.get();
		if (name != null) {
			return name;
		} else {
			return "";
		}
	}

	/**
	 * 現在のソースを設定します。
	 * 
	 * @param src
	 *            ソース
	 */
	public static void setSource(String src) {
		threadLoacal4Source.set(src);
	}

	/**
	 * 現在実行中のソースを取得します。
	 * 
	 * @return 現在のソース
	 */
	public static String getSource() {
		String src = (String) threadLoacal4Source.get();
		if (src != null) {
			return src;
		} else {
			return "";
		}
	}

	/**
	 * 現在のテスト結果を設定します。
	 * 
	 * @param result
	 *            テスト結果オブジェクト
	 */
	public static void setCurrentResult(Object result) {
		threadLoacal4Result.set(result);
	}

	/**
	 * 現在のテスト結果を取得します。
	 * 
	 * @return テスト結果オブジェクト
	 */
	public static Object getCurentResult() {
		return threadLoacal4Result.get();
	}

}
