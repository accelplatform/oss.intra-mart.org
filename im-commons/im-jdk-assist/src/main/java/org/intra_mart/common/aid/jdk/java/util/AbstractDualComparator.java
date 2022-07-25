package org.intra_mart.common.aid.jdk.java.util;

import java.util.Comparator;


/**
 * ２つのコンパレータを使用してソートを行うクラスです。<BR>
 * 比較の順番は、指定された第１コンパレータ、第２コンパレータの順に行われます。まず、第１コンパレータで比較を行います。
 * もし、その比較の結果が０の時は第２コンパレータで比較を行いその結果を返します。
 * 
 */
public abstract class AbstractDualComparator implements DualComparator {

	/**
	 * 新しいオブジェクトを生成します。
	 *
	 */
	public AbstractDualComparator() {
	}
	
	/**
	 *
	 * 順序付けのために ２ つの引数を比較するメソッドです。<br>
	 * 最初の引数が ２ 番目の引数より小さ い場合は負の整数、両方が等しい場合は ０、最初の引数が ２ 番目の引 数より大きい場合は正の整数を返します。<br>
	 * このメソッドは ２ つのコンパレータを使用して比較を行いその結果を返します。
	 * 比較の順番は、指定された第１コンパレータ、第２コンパレータの順に行われます。まず、第１コンパレータで比較を行います。
	 * 比較の結果が０でないときは、その比較した結果を返します。もし比較の結果が０の時は第２コンパレータで比較を行いその結果を返します。
	 * @param arg0 引数１
	 * @param arg1 引数２
	 */
	public int compare(Object arg0,Object arg1) {
		Comparator primaryComparator = getPrimaryComparator();
		Comparator secondaryComparator = getSecondaryComparator();
		int result;
		//プリマリコンパレータで比較する。
		result = primaryComparator.compare(arg0,arg1);
		//プリマリコンパレータの結果が0の時はセカンダリコンパレータで比較をする。
		if(result == 0 && secondaryComparator != null) {
			result = secondaryComparator.compare(arg0,arg1);
		}
		return result;
	}
}
