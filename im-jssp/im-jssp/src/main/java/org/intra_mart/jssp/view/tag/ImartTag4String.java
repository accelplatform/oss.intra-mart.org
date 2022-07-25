package org.intra_mart.jssp.view.tag;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.intra_mart.jssp.util.JsUtil;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="string"&gt; タグ。<br/>
 * <br/>
 * タグの指定されている個所に指定のデータを文字列として挿入します。<br/>
 * （実際には、タグ部分が指定文字列で置換されるように動作します）
 */
public class ImartTag4String implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "string";
	}

	/**************************************************************************
	 * &lt;IMART type="string"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】置換
	 *                  value : 置換文字列
	 *                  format: 文字列置換時のフォーマット文字列
	 *                          Date および Number 型変数の時のみ適応
	 *                          Date  : SimpleDateFormat
	 *                          Number: DecimalFormat
     * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		
		Object value = oAttr.get("value", null);

		// 指定データが文字列の場合
		if(value instanceof String){ 
			return (String) value; 
		}

		// 指定データが数値の場合
		if(value instanceof Number){
			if(oAttr.has("format", null)){
				// フォーマット取得
				DecimalFormat df = new DecimalFormat(ScriptRuntime.toString(oAttr.get("format", null)));
				try{
					return df.format(value);
				}
				catch(IllegalArgumentException iae){
					// nothing
				}
			}
			
			// フォーマットなしのデフォルト動作
			return ScriptRuntime.toString(value);
		}

		// 指定データがオブジェクトの場合
		if(value instanceof Scriptable){
			// 指定データが Date 型の場合
			if(JsUtil.isDate(value)){
				if(oAttr.has("format", null)){
					// フォーマット取得
					SimpleDateFormat sdf = new SimpleDateFormat(ScriptRuntime.toString(oAttr.get("format", null)));
					try{
						return sdf.format(JsUtil.toDate(value));
					}
					catch(IllegalArgumentException iae){
						// nothing
					}
				}
				
				// フォーマットなしのデフォルト動作
				return ScriptRuntime.toString(value);
			}
			// それ以外はデフォルト動作↓
		}

		// 該当項目無し
		return "";
	}

}
