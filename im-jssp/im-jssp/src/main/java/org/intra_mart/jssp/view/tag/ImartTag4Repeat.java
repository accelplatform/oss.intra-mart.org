package org.intra_mart.jssp.view.tag;

import org.intra_mart.jssp.script.ScriptScope;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * &lt;IMART type="repeat"&gt; タグ。<br/>
 * <br/>
 * タグに挟まれた範囲内（ネスト部分）を指定の回数だけ繰り返し処理します。<br/>
 * 属性 list に指定されたオブジェクト(or 配列)の要素数に 等しい回数だけ繰り返し処理を実行します。<br/>
 * 属性 item に指定された変数名に該当する変数に対して、繰り返し毎に list から順次取得したデータを代入します。<br/>
 * 属性 index に指定された変数名に該当する変数に対して、繰り返し毎に list から順次取得したプロパティ名称(or 要素番号)を代入します。<br/>
 * 属性 min に動作開始インデックスを指定した場合、それ以前に取得できた値は 無視されて、指定インデックス以降のデータのみを繰り返し処理の対象とします。<br/>
 * 属性 min を未指定にした場合のデフォルト動作は、0 を指定した場合と同様です。<br/>
 * 属性 max に動作終了インデックスを指定した場合、それ以後に取得できた値は 無視されて、指定インデックス以前のデータのみを繰り返し処理の対象とします。<br/>
 * 属性 max を未指定にした場合のデフォルト動作は、list に渡されたオブジェクト内に格納されているデータのうち、順次取得可能な最終データまでが処理対象となります。<br/>
 * 属性 action に対して、対応するファンクション・コンテナ内に定義されている関数名称を文字列として指定すると、繰り返し処理毎に実行されます。<br/>
 * 関数の実行タイミングは、繰り返し処理の最初(タグに挟まれた範囲内の実行前)です。<br/>
 * 指定関数は、繰り返し処理の開始から終了までに、繰り返し回数に 等しい回数だけ実行されます。<br/>
 */
public class ImartTag4Repeat implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "repeat";
	}

	/**************************************************************************
	 * &lt;IMART type="repeat"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】繰り返し処理
	 *                  list  : 繰り返し処理をする配列
	 *                  item  : 繰り返し処理中の配列内該当要素
	 *                  index : 繰り返し処理中の配列要素番号
	 *                  min   : 繰り返し処理対象の最小要素番号
	 *                  max   : 繰り返し処理対象の最大要素番号
	 *                  action: 繰り返し毎に動作する関数
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		Context cx = Context.getCurrentContext();
		ScriptableObject scope = ScriptScope.current();
		
		StringBuffer buf = new StringBuffer();
		ScriptableObject list;
		String item = null;
		String index = null;
		Function action = null;
		int min, max;
		Object value;

		// リストの確認
		value = oAttr.get("list", null);
		if(value instanceof ScriptableObject){
			list = (ScriptableObject) value; 
		}
		else{
			return ""; 
		}

		// item 属性の定義
		value = oAttr.get("item", null);
		boolean bItem;
		if(bItem = (value instanceof String)){
			item = (String) value; 
		}

		// index 属性の確認
		value = oAttr.get("index", null);
		boolean bIndex;
		if(bIndex = (value instanceof String)){
			index = (String) value; 
		}

		// action 関数の確認
		value = oAttr.get("action", null);
		boolean bAction;
		Object[] actionArgs = { oAttr };
		if(bAction = (value instanceof String)){
			value = scope.get((String) value, null);
			if(bAction = (value instanceof Function)){
				action = (Function) value;
			}
		}

		// プロパティの抽出
		Object[] params = list.getIds();
		// min 属性の確認
		value = oAttr.get("min", null);
		if(value instanceof String){
			try{
				min = Math.max(Integer.parseInt((String) value), 0);
			}
			catch(NumberFormatException nfe){
				min = 0;
			}
		}
		else if(value instanceof Number){
			min = Math.max(((Number) value).intValue(), 0);
		}
		else{
			min = 0;
		}
		// max 属性の確認
		value = oAttr.get("max", null);
		if(value instanceof String){
			try{
				max = Math.min(Integer.parseInt((String) value), params.length);
			}
			catch(NumberFormatException nfe){
				max = params.length;
			}
		}
		else if(value instanceof Number){
			max = Math.min(((Number) value).intValue(), params.length);
		}
		else{
			max = params.length;
		}

		// くるくる実行
		for(int idx = min; idx < max; idx++){
			// index の設定
			if(bIndex){
				scope.defineProperty(index, params[idx], ScriptableObject.EMPTY);
			}

			// item の設定
			if(bItem){
				if(params[idx] instanceof String){
					value = list.get((String) params[idx], null);
					if(value != ScriptableObject.NOT_FOUND){
						scope.defineProperty(item, value, ScriptableObject.EMPTY);
					}
					else{
						scope.defineProperty(item, Undefined.instance, ScriptableObject.EMPTY);
					}
				}
				else if(params[idx] instanceof Number){
					value = list.get(((Number) params[idx]).intValue(), null);
					if(value != ScriptableObject.NOT_FOUND){
						scope.defineProperty(item, value, ScriptableObject.EMPTY);
					}
					else{
						scope.defineProperty(item, Undefined.instance, ScriptableObject.EMPTY);
					}
				}
			}

			// action 関数の起動
			if(bAction){
//				action.call(cx, scope, thisObj, act_arg);
				action.call(cx, scope, scope, actionArgs);
			}

			// ソースの生成
			buf.append(((InnerTextObject) oInner).execute());
		}

		return buf.toString();
	}

}
