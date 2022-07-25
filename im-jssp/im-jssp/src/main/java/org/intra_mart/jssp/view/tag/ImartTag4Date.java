package org.intra_mart.jssp.view.tag;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.intra_mart.jssp.util.JsUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="date"&gt; タグ。<br/>
 * <br/>
 * タグの指定された場所に、指定された日付データを文字列として挿入します。<br/>
 */
public class ImartTag4Date implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "date";
	}

	/**************************************************************************
	 * &lt;IMART type="date"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】置換
	 *                  value : 置換文字列
	 *                  format: 文字列置換時のフォーマット文字列
	 *                          java.text.SimpleDateFormat の仕様に準じる
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		
		Object value = oAttr.get("value", null);
		Date dData;

		// 対象日付データの取得
		if(JsUtil.isDate(value)){
			dData = JsUtil.toDate(value);
		}
		else if(value instanceof String){
			try{
				dData = DateFormat.getDateTimeInstance().parse((String) value);
			}
			catch(ParseException pe){
				dData = new Date();
			}
		}
		else if(value instanceof Number){
			dData = new Date(((Number) value).longValue());
		}
		else{
			dData = new Date();
		}

		// フォーマット変換
		if(oAttr.has("format", null)){
			// フォーマット取得
			SimpleDateFormat sdf = new SimpleDateFormat(ScriptRuntime.toString(oAttr.get("format", null)));
			try{
				return sdf.format(dData);
			}
			catch(IllegalArgumentException iae){
				// nothing
			}
		}

		// フォーマットなしのデフォルト動作
		return Context.toString(value);
	}

}
