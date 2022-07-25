package org.intra_mart.jssp.view.tag;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="textarea"&gt; タグ。<br/>
 * <br/>
 * フォーム内にテキストエリアを表示します。 <br/>
 * <br/>
 * 属性 value に対して指定した文字列を初期表示文字列とします。<br/>
 * (属性 value に対して文字列以外の値を指定した場合の動作は保証外)<br/>
 * <br/>
 * その他のタグ属性は、そのまま &lt;TEXTAREA&gt; タグの属性として指定されます。<br/>
 * (&lt;TEXTAREA&gt; タグの仕様に関しては、ＨＴＭＬリファレンスを参照下さい)<br/>
 * <br/>
 * タグに挟まれた範囲内に文字列を指定すると、テキストエリアの初期表示値とすることができます。<br/>
 */
public class ImartTag4Textarea implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "textarea";
	}

	/**************************************************************************
	 * &lt;IMART type="textarea"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】&lt;TEXTAREA&gt; タグ
	 *                  value  : テキストエリアに対する初期表示値(文字列)
	 *                  その他 : タグ属性と属性値
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) 
	{
		TagObject textareaTag = new TagObject("TEXTAREA");
		String defaultValue;

		// 初期表示値の取得
		if(oAttr.has("value", null)){
			Object attrValue = oAttr.get("value", null);
			if(attrValue instanceof String){
				defaultValue = (String) attrValue;					// 文字列
			}
			else if(attrValue instanceof Number){
				defaultValue = ScriptRuntime.toString(attrValue);	// 数値型
			}
			else{
				defaultValue = "";									// 基本値
			}
		}
		else{
			defaultValue = "";	// デフォルト値
		}

		// その他任意の属性指定
		oAttr.delete("type");
		oAttr.delete("value");
		Object[] params = oAttr.getIds();
		for(int idx = 0; idx < params.length; idx++){
			
			Object value = oAttr.get((String) params[idx], null);
			
			if( (value instanceof String)
				||
				(value instanceof Number)
				||
				(value instanceof Boolean) ){
				
				textareaTag.setAttribute((String) params[idx], value);
				
			}
		}

		return textareaTag.getBegin() + defaultValue + ((InnerTextObject) oInner).execute() + textareaTag.getTerminate();
	}

}
