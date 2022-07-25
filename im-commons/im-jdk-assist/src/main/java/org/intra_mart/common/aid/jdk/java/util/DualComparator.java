package org.intra_mart.common.aid.jdk.java.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * ２つのコンパレータを使用してソートを行うためのインターフェースです。
 * 
 */
public interface DualComparator extends Serializable,Comparator{
	/**
	 *１番目コンパレータを取得します。
	 * @return　１番目のコンパレータ
	 */
	public Comparator getPrimaryComparator();
	/**
	 * ２番目コンパレータを取得します。
	 * @return　２番目のコンパレータ
	 */
	public Comparator getSecondaryComparator();
}
