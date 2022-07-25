package org.intra_mart.jssp.view.tag;

import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="condition"&gt; タグ。<br/>
 * <br/>
 * タグに挟まれた範囲内の部分の実行を制御します。<br/>
 * 属性 validity に対して true 値を指定した場合は内部の処理を実行し、false 値を指定した場合は内部の処理を無視します。<br/>
 * 属性 negative に対して true 値を指定した場合、 属性 validity に対して指定した値を逆解釈して処理します。<br/>
 */
public class ImartTag4Condition implements ImartTagType {
	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "condition";
	}

	
	/**************************************************************************
	 * &lt;IMART type="condition"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】条件判定タグ 1
	 *                  validity: 有効・無効フラグ
	 *                            true が指定された場合タグ内を処理
	 *                            false が指定された場合無処理
	 *                  negative: 条件反転判定
	 *                            true が指定された場合 valid の判定を逆転
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		Object valid = oAttr.get("validity", null);
		Object negative = oAttr.get("negative", null);

		// 判定
		if(valid instanceof Boolean){
			if(negative instanceof Boolean){
				// 判定基準反転モード
				if(((Boolean) negative).booleanValue()){
					if(((Boolean) valid).booleanValue() == false){
						return ((InnerTextObject) oInner).execute();
					}
					else{
						return "";		// 該当せず
					}
				}
			}

			// 通常モード(reverse=false 時含む)
			if(((Boolean) valid).booleanValue()){
				return ((InnerTextObject) oInner).execute();
			}
		}

		// 該当項目無し（非表示）
		return "";
	}

}
