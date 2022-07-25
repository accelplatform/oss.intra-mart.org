package org.intra_mart.jssp.view.tag;

import org.intra_mart.jssp.script.ScriptScope;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * &lt;IMART type="loop"&gt; タグ。<br/>
 * <br/>
 * タグに挟まれている範囲内のソースを繰り返し処理します。<br/>
 * 繰り返し処理は、属性 count に指定されている回数だけ行います。 <br/>
 * (属性 count に、整数字列以外の指定がされた場合の動作は保証外)<br/>
 * <br/>
 * 属性 index に、ファンクション・コンテナ内で定義してある変数名称を指定する事で、
 * 何回目の繰り返しかを表す数値を受け取る事が出来ます。<br/>
 * (周回数は、0 から始まる整数値で、周回ごとに 1 ずつインクリメントされます。)<br/>
 * <br/>
 * 属性 action に対して、対応するファンクション・コンテナ内に定義されている関数名称を文字列として指定すると、
 * 繰り返し処理毎に実行されます。<br/>
 * 実行タイミングは、繰り返し処理の最初(タグに挟まれた範囲内の実行前)です。<br/>
 * 指定関数は、繰り返し処理の開始から終了までに、繰り返し回数に 等しい回数だけ実行されます。<br/>
 */
public class ImartTag4Loop implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "loop";
	}

	/**************************************************************************
	 * &lt;IMART type="loop"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】任意タグ生成
	 *                  count: くり返し回数
	 *                  index: 周回番号の通知先変数名
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {

		Context cx = Context.getCurrentContext();
		ScriptableObject scope = ScriptScope.current();
		
		boolean isReport = oAttr.has("index", null);
		String propertyName4index = ScriptRuntime.toString(oAttr.get("index", null));

		int maxCounnt = ScriptRuntime.toInt32(oAttr.get("count", null));
		
		Function action = null;

		// action 関数の確認
		boolean bAction = false;
		Object functionName = oAttr.get("action", null);
		
		if(functionName instanceof String){
			Object function = scope.get((String) functionName, null);
			
			if(function instanceof Function){
				bAction = true;
				action = (Function) function;
			}
		}

		// くるくる実行
		StringBuffer buf = new StringBuffer();		
		for(int idx = 0; idx < maxCounnt; idx++){
			
			// index の設定
			if(isReport){
				scope.defineProperty(propertyName4index, new Integer(idx), ScriptableObject.EMPTY);
			}

			// action 関数の起動
			if(bAction){
				Object[] actionArg = { oAttr };
				
//				action.call(cx, scope, thisObj, actionArg);
				action.call(cx, scope, scope, actionArg);
			}

			buf.append(((InnerTextObject) oInner).execute());
		}

		return buf.toString();
	}

}
