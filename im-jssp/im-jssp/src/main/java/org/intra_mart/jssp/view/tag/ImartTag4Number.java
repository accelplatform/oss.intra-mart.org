package org.intra_mart.jssp.view.tag;

import java.text.DecimalFormat;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="number"&gt; タグ。<br/>
 * <br/>
 * タグの指定されている個所に指定の数値データを文字列として挿入します。<br/>
 * （実際には、タグ部分が指定文字列で置換されるように動作します。）<br/>
 * フォーマット文字列は ###,### のように指定して下さい。<br/>
 * format 属性指定時の仕様に関しては、
 * <a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/java/text/DecimalFormat.html">java.text.DecimalFormat</a> に準拠します。<br/>
 * 
 * @see java.text.DecimalFormat
 */
public class ImartTag4Number implements ImartTagType {

	public String getTagName() {
		return "number";
	}

	/**************************************************************************
	 * &lt;IMART type="number"&gt; タグの実行メソッド。
     * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】置換
	 *                  value : 置換文字列
	 *                  format: 文字列置換時のフォーマット文字列
	 *                          java.text.DecimalFormat の仕様に準じる
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		Double value = new Double(ScriptRuntime.toNumber(oAttr.get("value", null)));

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

}
