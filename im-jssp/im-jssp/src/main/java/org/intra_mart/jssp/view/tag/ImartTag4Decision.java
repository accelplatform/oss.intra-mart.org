package org.intra_mart.jssp.view.tag;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

/**
 * &lt;IMART type="decision"&gt; タグ。<br/>
 * <br/>
 * タグに挟まれた部分の実行の条件分岐を行います。<br/>
 * 属性 value に与えられた値が、属性 case に与えられた値に等しい場合のみタグに挟まれた範囲内の実行を行います。<br/>
 * value または case 属性に対しては null および undefined 値以外の値を指定するようにして下さい。<br/>
 * 上記の各属性に対して null および undefined 値を指定した場合の動作は保証外です。<br/>
 */
public class ImartTag4Decision implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "decision";
	}

	/**************************************************************************
	 * &lt;IMART type="decision"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】条件判定タグ 2
	 *                  value: 判定する値
	 *                  case : 判定時のキー
	 *                         value で受け取った値が、この値に等しければ
	 *                         内容を処理
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {

		Object key = oAttr.get("case", null);		// チェック値
		Object value = oAttr.get("value", null);	// チェック対象値

		// 無効データ指定チェック
		if(key == null && (key instanceof Undefined) && key == Scriptable.NOT_FOUND){
			return "";
		}
		if(value == null && (value instanceof Undefined) && value == Scriptable.NOT_FOUND){
			return "";
		}

		// 判定
		if(key.equals(value)){
			return ((InnerTextObject) oInner).execute(); 
		}

		// 該当せず（非表示）
		return "";
	}	
}
