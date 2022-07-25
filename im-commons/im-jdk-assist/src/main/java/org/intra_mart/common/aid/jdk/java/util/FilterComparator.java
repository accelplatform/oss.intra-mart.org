package org.intra_mart.common.aid.jdk.java.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * コンパレータのフィルターとして機能する抽象クラスです。
 *
 */
public abstract class FilterComparator implements Comparator ,Serializable{

	private Comparator parent;

	/**
	 * 指定されれたコンパレータ <CODE>comparator</CODE> をフィルター対象として、新しいオブジェクトを生成します。
	 * @param comparator フィルター対象のコンパレータ
	 */
	public FilterComparator(Comparator comparator) {
		this.parent = comparator;
	}

	/**
	 * 順序付けのために 2 つの引数を比較するメソッドです。<br>
	 * 最初の引数が 2 番目の引数より小さ い場合は負の整数、両方が等しい場合は 0、最初の引数が 2 番目の引 数より大きい場合は正の整数を返します。<br>
	 * @param arg0 引数１
	 * @param arg1 引数２
	 * @return 比較した結果
	 */
	public int compare(Object arg0, Object arg1) {
		return parent.compare(arg0, arg1);
	}

	/**
	 * フィルター対象のコンパレータを返します。
	 * @return フィルター対象のコンパレータ
	 */
	public Comparator getParentComparator() {
		return parent;
	}

}
