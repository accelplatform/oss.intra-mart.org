package org.intra_mart.jssp.view.tag;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="tag"&gt; タグ。<br/>
 * <br/>
 * 任意のタグを生成します。<br/>
 * 属性 name には、目的のタグ名称を文字列として指定します。<br/>
 * (属性 name に対して、文字列型以外の値が指定された場合の動作は保証外)<br/>
 * <br/>
 * その他の属性は、そのまま、生成される任意タグの属性値となります。<br/>
 */
public class ImartTag4Tag implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "tag";
	}

	/**************************************************************************
	 * &lt;IMART type="tag"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】任意タグ生成
	 *                  name  : タグ名称
	 *                  その他: タグ属性と属性値
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		TagObject optionalTag = new TagObject(ScriptRuntime.toString(oAttr.get("name", null)));

		// その他任意の属性指定
		oAttr.delete("type");
		oAttr.delete("name");
		Object[] params = oAttr.getIds();
		for(int idx = 0; idx < params.length; idx++){

			Object value = oAttr.get((String) params[idx], null);
			
			if( (value instanceof String)
				||
				(value instanceof Number)
				||
				(value instanceof Boolean) ){
				
				optionalTag.setAttribute((String) params[idx], value);
				
			}
		}

		return optionalTag.getBegin() + ((InnerTextObject) oInner).execute() + optionalTag.getTerminate();
	}

}
