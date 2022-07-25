package org.intra_mart.jssp.view.tag;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.intra_mart.jssp.util.ValueObject;
import org.intra_mart.jssp.view.ViewScope;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * &lt;IMART&gt; ～ &lt;/IMART&gt; を解析＆管理＆実行するクラス
 */
public class ImartTagAnalyzer implements Serializable {

	/** 属性値種別 */
	private static enum TYPE {
		/** 動的接続変数 */
		DYNAMIC,
		/** 静的接続変数 */
		STATIC,
		/** 単一属性 */
		SINGLE
	}

	/** 関数引数 */
	public static final Object[] FUNC_ARGS = new Object[0];

	/** タグ属性 */
	private final Map<String, Object> value = new HashMap<String, Object>();
	/** 属性の種別 */
	private final Map<String, TYPE> attribute = new HashMap<String, TYPE>();

	/** タグに挟まれた部分 */
	private ViewScope inner;

	/** Empty Tag判定 */
	private boolean isEmptyTag = false;

	/**
	 * コンストラクタ.<br /> IMARTタグの解析を行います.<br />
	 * 引数inで指定するソースはViewScope#TAG_NAMEで始まっている必要があります.
	 * 
	 * @param in
	 *            ソース
	 */
	public ImartTagAnalyzer(final Reader in) {
		super();

		// 解析
		analyze(in);

		if (isEmptyTag) {
			// <IMART... />の為、空のinnerを登録
			// singletonな空のViewScopeを利用
			inner = ViewScope.EMPTY;
		} else {
			inner = new ViewScope(in);
		}
	}

	/**
	 * &lt;IMART&gt; タグの解析を開始する
	 * 
	 * @param in
	 *            ソース
	 */
	private void analyze(final Reader in) {
		try {
			// タグ名のスキップ
			for (int i = 0; i < ViewScope.TAG_NAME.length(); i++) {
				// I M A R T
				in.read();
			}

			// 属性値解析
			analyzeAttribute(in);
		} catch (final IOException ioe) {
		} catch (final NullPointerException npe) {
		}
	}

	/**
	 * &lt;IMART&gt; タグの属性の解析をする
	 * 
	 * @param in
	 *            ソース
	 * @throws NullPointerException
	 * @throws IOException
	 *             ストリームの読込エラー時
	 */
	private void analyzeAttribute(final Reader in) throws NullPointerException, IOException{
		final StringBuilder buf = new StringBuilder();
		int chr;
		String sName = null;
		boolean bEqual = false;

		while((chr = in.read()) != -1){
			if(Character.isWhitespace((char)chr)){
				// 空白スキップ
				continue;
			}

			// トークンの抽出
			if(chr == (int)'\"'){
				// 属性値の場合: 終端"までのトークンを抽出
				buf.setLength(0);
				while(true){
					chr = in.read();
					if(chr == (int)'\\'){
						buf.append((char) chr);
						chr = in.read();
					}
					else if(chr == (int)'\"'){ break; }	// 文字列定数終端
					if(chr == -1){ break; }

					buf.append((char) chr);
				}

				// 値の登録
				if(sName != null){
					if(bEqual){
						value.put(sName, buf.toString());
						attribute.put(sName, TYPE.STATIC);
						sName = null;
					}
					sName = null;	// 初期化
				}
			}
			else{
				// 属性名/動的接続変数の抽出
				buf.setLength(0);
				
				// トークン抽出
				while(!Character.isWhitespace((char)chr) && chr != -1 && chr != (int)'=' && chr != (int)'>'){
					// 閉じタグ判定 <IMART ... />
					if(chr == (int)'/') {
						in.mark(1);
						if(in.read() == (int)'>') {
							// '/>'
							isEmptyTag = true;
							break;
						}else {
							in.reset();
						}
					}

					buf.append((char) chr);
					chr = in.read();
				}

				// 名前チェック
				if(sName != null && bEqual){
					// 動的接続変数の場合
					// 変数名の解析（"." で分割）
					final List<String> variable = new ArrayList<String>();
					final Reader input = new StringReader(buf.toString());
					int code;
					buf.setLength(0);
					while((code = input.read()) != -1){
						if(code == (int) '.'){
							variable.add(buf.toString());
							buf.setLength(0);
						}
						else{
							buf.append((char) code);
						}
					}
					variable.add(buf.toString());
					value.put(sName, variable);
					attribute.put(sName, TYPE.DYNAMIC);
					sName = null;
				}
				else{
					// 単独属性の場合 (例: <IMART type="text" readonly></IMART>
					if(buf.length() != 0){
						sName = buf.toString();
						value.put(sName, sName);				// 名前登録
						attribute.put(sName, TYPE.SINGLE);	// 単独属性判定
						bEqual = false;
					}
				}
			}

			if(chr == (int)'='){
				if(sName != null && bEqual == false){ bEqual = true; }
				else{ sName = null; }
			}
			else if(chr == (int)'>'){ break; }	// タグ終了位置
			
			if(isEmptyTag) { break; } // EmptyTagと認識された場合
		} // End of While
	}

	/**
	 * &lt;IMART&gt; タグの実行をする
	 * 
	 * @param cx
	 *            現在の実行環境コンテキスト
	 * @param scope
	 *            現在の実行環境の変数スコープデータ
	 * @return ＨＴＭＬソース（文字列）
	 * @throws JavaScriptException
	 */
	@SuppressWarnings("unchecked")
	public String execute(final Context cx, final Scriptable scope) throws JavaScriptException {
		// 属性値オブジェクト(oAttr)の作成
		final ScriptableObject attr = new ValueObject();

		// 値の抽出
		for (final Map.Entry<String, TYPE> item : attribute.entrySet()) {
			try {
				final String key = (String) item.getKey();

				// 値と属性の取得
				Object data = value.get(key);

				switch (item.getValue()) {
				case STATIC:
					// 静的データ
					attr.defineProperty(key, ImartObject.getConstant(cx, scope, data, data), ScriptableObject.EMPTY);
					break;
				case SINGLE:
					// 単一属性
					attr.defineProperty(key, Boolean.TRUE, ScriptableObject.EMPTY);
					break;
				case DYNAMIC:
					// 動的データ
					final Iterator<String> words = ((List<String>) data).iterator();
					data = scope;
					while (words.hasNext()) {
						data = ((ScriptableObject) data).get(words.next(), null);
						if (!(data instanceof ScriptableObject)) {
							break;
						}
					}
					if (words.hasNext() || (data == scope) || (data == ScriptableObject.NOT_FOUND)) {
						data = Undefined.instance;
					}
					if (data instanceof Function) {
						attr.defineProperty(key, ((Function) data).call(cx, scope, null, FUNC_ARGS), ScriptableObject.EMPTY);
					} else {
						attr.defineProperty(key, data, ScriptableObject.EMPTY);
					}
					break;
				}
			} catch (final NoSuchElementException nsee) {
				// 属性値取得エラー
			} catch (final JavaScriptException jse) {
				// バインド関数実行時エラー
			}
		}

		// タグ処理関数の実行
		return ImartObject.callFunction(cx, scope, attr, new InnerTextObject(inner));
	}

	/**
	 * &lt;IMART&gt; タグ属性解析結果の表示メソッド<br>
	 * タグの解析結果を標準出力に表示するデータの確認用メソッド
	 */
	public void print() {
		System.out.println(value.toString());
		System.out.println(attribute.toString());
		System.out.println("[" + inner.hashCode() + "]-------------------------------------------------");
		inner.print();
	}
}

/* End of File */