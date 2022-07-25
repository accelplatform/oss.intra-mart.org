package org.intra_mart.jssp.view.tag;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * &lt;IMART type="select"&gt; タグ。<br/>
 * <br/>
 * フォーム内にコンボボックス（またはリストボックス）を表示します。<br/>
 * <br/>
 * 属性 list に対しては、オブジェクトを指定して下さい。<br/>
 * 属性 list に指定したオブジェクトに格納されているすべての値が、
 * コンボボックス内の表示要素になります。<br/>
 * 属性 list に指定されたオブジェクトのプロパティ名が
 * コンボボックス内の各要素(&lt;OPTION&gt; タグ)の value 値になり、
 * 該当するプロパティに格納されている値がコンボボックス内の要素の画面上での表示名になります。<br/>
 * (オブジェクトに格納されている値が文字列以外の場合の動作は保証外。)<br/>
 * <br/>
 * 属性 selected には、コンボボックスの初期選択(初期表示)値を指定します。<br/>
 * 初期選択状態にする値のキー(属性 list に指定したオブジェクトの該当するプロパティ名)を指定することで、
 * 初期選択状態にすることができます。<br/>
 * 属性 select に指定した値に該当するキーが属性 list に指定したオブジェクトのプロパティとして存在しない場合は、
 * どの値も選択状態にはなりません。<br/>
 * (ただし、コンボボックスの初期選択指定が存在しない場合は、
 * ブラウザの仕様で自動的にコンボボックス内の先頭の要素が初期選択状態として表示されます)<br/>
 * <br/>
 * また、初期選択状態とするキーを同時に複数個指定する場合は、
 * 属性 selected に対して配列を指定する事で可能になります。<br/>
 * その際、配列内の各要素の値として、
 * 初期選択状態にするキー(属性 list に指定したオブジェクトの該当するプロパティ名)を格納してください。<br/>
 * <br/>
 * 属性 blank は、コンボボックス内の任意の位置に空データを表示させるための属性指定になります。<br/>
 * 値としてキーワード "top" を指定するとボックス内の先頭位置に空データが表示され、<br/>
 * キーワード "bottom" を指定するとボックス内の最後尾位置に空データが表示されます。<br/>
 * <br/>
 * 上記以外の属性を指定した場合、それらは &lt;SELECT&gt; タグの各属性として出力されます。<br/>
 * (&lt;SELECT&gt; タグの仕様に関しては市販のＨＴＭＬリファレンスを参照して下さい)<br/>
 */
public class ImartTag4Select implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "select";
	}

	/************************************************************************************
	 * &lt;IMART type="select"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】&lt;SELECT&gt; タグ
	 *                  list    : 表示リスト（オブジェクト型）
	 *                            指定のオブジェクトのプロパティがキーに
	 *                            値が表示名称となる
	 *                  selected: 初期選択値の設定
	 *                            初期選択状態とするキーを文字列で指定
	 *                            初期選択状態とするキーを表す文字列を要素に
	 *                            持つ配列を指定する事で複数指定可能
	 *                  blank   : キーワードの示す位置へブランクデータを挿入
	 *                            top   : 値の最上位にブランクデータを設定
	 *                            bottom: 値の最下位にブランクデータを設定
	 * </pre>
	 *************************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		StringBuffer buf = new StringBuffer();
		TagObject selectTag = new TagObject("SELECT");
		Object[] params;

		// 必要属性の取得
		Object selected = oAttr.get("selected", null);
		String sBlank = ScriptRuntime.toString(oAttr.get("blank", null));
		Object list = oAttr.get("list", null);

		// selected 属性のチェック
		List<String> defaultSelected = new ArrayList<String>();
		if(selected instanceof ScriptableObject){
			ScriptableObject ary = (ScriptableObject) selected;
			params = ary.getIds();
			for(int idx = 0; idx < params.length; idx++){
				if(params[idx] instanceof Number){
					defaultSelected.add(ScriptRuntime.toString(ary.get(((Number) params[idx]).intValue(), null)));
				}
			}
		}
		else if(selected != Scriptable.NOT_FOUND){
			defaultSelected.add(ScriptRuntime.toString(selected));
		}

		// <SELECT> タグのための属性抽出
		oAttr.delete("selected");
		oAttr.delete("blank");
		oAttr.delete("list");
		oAttr.delete("type");
		params = oAttr.getIds();
		for(int idx = 0; idx < params.length; idx++){
			
			Object value = oAttr.get((String) params[idx], null);
			
			if( (value instanceof String)
				||
				(value instanceof Number)
				||
				(value instanceof Boolean) ){

				selectTag.setAttribute((String) params[idx], value);
				
			}
		}

		// <OPTION>ソースの作成
		if(sBlank.toLowerCase().equals("top")){
			buf.append("<OPTION value=\"\"");
			if(defaultSelected.contains("")){
				buf.append(" selected"); 
			}
			buf.append(">");
		}
		if(list instanceof ScriptableObject){
			ScriptableObject options = (ScriptableObject) list;
			params = options.getIds();
			for(int idx = 0; idx < params.length; idx++){
				String name = ScriptRuntime.toString(params[idx]);
				buf.append("<OPTION value=\"");
				buf.append(name);	// キー
				if(defaultSelected.contains(name)){
					buf.append("\" selected>");		// 初期選択状態
				}
				else{
					buf.append("\">");				// 通常アイテム
				}
				if(params[idx] instanceof String){
					// 表示値
					buf.append(ScriptRuntime.toString(options.get((String) params[idx], null)));
				}
				else{
					// 表示値
					buf.append(ScriptRuntime.toString(options.get(((Number) params[idx]).intValue(), null)));
				}
			}
		}
		if(sBlank.toLowerCase().equals("bottom")){
			buf.append("<OPTION value=\"\"");
			if(defaultSelected.contains("")){
				buf.append(" selected"); 
			}
			buf.append(">");
		}

		return selectTag.getBegin() + buf.toString() + ((InnerTextObject) oInner).execute() + selectTag.getTerminate();
	}

}
