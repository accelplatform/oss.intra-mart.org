package org.intra_mart.jssp.view.tag;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="input"&gt; タグ。<br/>
 * <br/>
 * 任意のフォームコントロールを挿入します。<br/>
 * ＨＴＭＬの &lt;INPUT&gt; タグを生成します（ＨＴＭＬリファレンスを参照）。<br/>
 * 属性 style に指定した文字列が &lt;INPUT&gt; タグの type 属性となります。<br/>
 * その他の属性と属性値は、そのまま &lt;INPUT&gt; タグの属性となります。<br/>
 * 属性 style に対して有効な指定値には以下のものがあります。<br/>
 * <br/>
 * <table border="1">
 * 	<tr>
 * 		<th>text</th>
 * 		<td>テキストフィールド</td>
 * 	</tr>
 * 	<tr>
 * 		<th>password</th>
 * 		<td>パスワードフィールド</td>
 * 	</tr>
 * 	<tr>
 * 		<th>radio</th>
 * 		<td>ラジオボタン</td>
 * 	</tr>
 * 	<tr>
 * 		<th>checkbox</th>
 * 		<td>チェックボックス</td>
 * 	</tr>
 * 	<tr>
 * 		<th>hidden</th>
 * 		<td>隠しフィールド</td>
 * 	</tr>
 * 	<tr>
 * 		<th>button</th>
 * 		<td>ボタン</td>
 * 	</tr>
 * </table>
 * <br/>
 * &lt;INPUT&gt; タグの type 属性および各種設定可能な属性とその動作に関しては、ＨＴＭＬリファレンスをご参照下さい。<br/>
 */
public class ImartTag4Input implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "input";
	}

	/**************************************************************************
	 * &lt;IMART type="input"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】&lt;INPUT&gt; タグ
	 *                  style : &lt;INPUT&gt; の type 属性名
	 *                  その他: タグ属性と属性値
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner){
		TagObject input_tag = new TagObject("INPUT");

		// コントロールタイプの設定
		input_tag.setAttribute("type", ScriptRuntime.toString(oAttr.get("style", null)));

		// その他任意の属性指定
		oAttr.delete("type");
		oAttr.delete("style");
		Object[] params = oAttr.getIds();
		for(int idx = 0; idx < params.length; idx++){
			
			Object value = oAttr.get((String) params[idx], null);
			
			if( (value instanceof String)
				||
				(value instanceof Number)
				||
				(value instanceof Boolean) ){
				
				input_tag.setAttribute((String) params[idx], value);
				
			}
		}

		return input_tag.getBegin();
	}

}
