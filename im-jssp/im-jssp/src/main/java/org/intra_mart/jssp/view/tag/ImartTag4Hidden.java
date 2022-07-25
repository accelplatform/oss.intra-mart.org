package org.intra_mart.jssp.view.tag;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="hidden"&gt; タグ。<br/>
 * <br/>
 * フォームサブミット時のリクエスト引数となる隠しフィールドを作成します。<br/>
 * 属性名がＵＲＬ引数の名称となり、属性値がＵＲＬ引数名称にマップされる値となります。<br/>
 * <br/>
 * ブラウザからの受取時は、<pre><b>request.属性名</b></pre> で取得可能です。<br/>
 * (request は、ファンクション・コンテナ実行時の関数引数です)<br/>
 * <br/>
 * ここでの指定は、ＵＲＬ引数となるため、属性名および属性値には定数または文字列型の値を指定するようにしてください。<br/>
 * (文字列以外の値が指定された場合の動作に関しては保証外)<br/>
 * <br/>
 * 属性名と属性値は、複数個の同時指定が可能です。<br/>
 * (指定する属性値が１組の場合は、 &lt;IMART type="input" style="hidden"&gt; を利用して指定することもできます)<br/>
 */
public class ImartTag4Hidden implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "hidden";
	}
	
	/*******************************************************************************
	 * &lt;IMART type="hidden"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】&lt;INPUT type="hidden"&gt; タグ
	 *                  その他 : hidden タグ名称と値
	 *                           属性名がＵＲＬ引数の名称で属性値が対応する値
	 *                           という関係になる。
	 *                           ブラウザからの受取時は request.属性名 という
	 *                           形式で取得可能。
	 *                           属性名および属性値は、すべて文字列型で指定。
	 *                           属性名と属性値の組合わせは複数指定可能。
	 * </pre>
	 *******************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner){
		StringBuffer buf = new StringBuffer();

		// その他任意の属性指定
		oAttr.delete("type");
		Object[] params = oAttr.getIds();
		for(int idx = 0; idx < params.length; idx++){
			buf.append("<INPUT type=\"hidden\" name=\"");
			buf.append((String) params[idx]);
			buf.append("\" value=\"");
			buf.append(ScriptRuntime.toString(oAttr.get((String) params[idx], null)));
			buf.append("\">");
		}

		return buf.toString();
	}

}
