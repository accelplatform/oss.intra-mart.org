package org.intra_mart.jssp.view.tag;

import java.io.Serializable;

import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART&gt; タグのインターフェースです。<br>
 * &lt;IMART&gt; タグを実装する場合は、このインターフェースを実装する必要があります。<br>
 * 本インターフェースの実装クラスは、ゼロ・パラメータコンストラクタが必要です。
 */
public interface ImartTagType extends Serializable {

	/**
	 * タグ名称を返却します。
	 * @return タグ名称
	 */
	public String getTagName();

	/**
	 * タグを実行します。
	 * @param oAttr タグに対する引数情報
	 * @param oInner タグに挟まれた部分の情報
	 * @return HTML ソース（文字列）
	 */
	public String doTag(Scriptable oAttr, Scriptable oInner);
	
}
