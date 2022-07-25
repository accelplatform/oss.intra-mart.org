package org.intra_mart.common.aid.jdk.java.util;

import java.util.Comparator;

/**
 * コンパレータのフィルタとして機能します。<br>
 * フィルタ対象のコンパレータの <CODE>compare()</CODE> メソッドの結果を反転させて返します。
 *
 */
public class ReverseComparator extends FilterComparator {

	/**
	 * 指定されたコンパレータ <CODE>comparator</CODE> をフィルター対象として、新しいオブジェクトを生成します。
	 * @param comparator フィルター対象のコンパレータ
	 */
	public ReverseComparator(Comparator comparator) {
		super(comparator);
	}

	/**
	 * フィルタ対象のコンパレータの <CODE>compare() </CODE>メソッドの結果を反転させて返します。
	 * @param arg0 引数１
	 * @param arg1 引数２
	 * @return フィルタ対象のコンパレータの <CODE>compare() </CODE>メソッドの結果を反転させた値
	 */
	public int compare(Object arg0,Object arg1) {
		return -super.compare(arg0,arg1);
	}
}
