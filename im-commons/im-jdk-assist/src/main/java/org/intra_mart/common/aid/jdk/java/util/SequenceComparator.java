package org.intra_mart.common.aid.jdk.java.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

/**
 * 挿入順にソートするためのコンパレータです。<BR>
 * このコンパレータは挿入が早い物から遅い物の順に並べます。<BR>
 * このコンパレータは、挿入されたオブジェクトの順番をインスタンス変数として持っています。
 * そのため、複数のオブジェクトでこのコンパレータを共有使用すると正しく動作しない可能性があります。<BR>
 * またこのクラスは、java.lang.TreeSet、java.lang.TreeMapで使用されることを想定して作られています。
 * そのため、他のクラスのコンパレータとして使用すると正しく動作しない可能性があります。
 * クラスで管理できるオブジェクトの数は、２の３１乗-１までのみ管理できます。
 * 
 */
public class SequenceComparator implements Comparator, Serializable {
	/**
	 * map
	 */
	private WeakHashMap map;
	/**
	 * 挿入順番
	 */
	private long count;

	/**
	 * 新しいオブジェクトを生成します。
	 */
	public SequenceComparator() {
		map = new WeakHashMap();
		count = 0;

	}

	/**
	 * 順序付けのために ２ つの引数を比較するメソッドです。<br>
	 * 最初の引数が ２ 番目の引数より小さ い場合は負の整数、両方が等しい場合は 0、最初の引数が 2 番目の引 数より大きい場合は正の整数を返します。<br>
	 * このメソッドの目的は、挿入順にソートすることです。
	 * 引数１と引数２を比較したとき、引数１の方が挿入が遅いときは正の数、早い時は負の数を返します。<BR>
	 * 引数１と引数２が同じインスタンスの時は０を返します
	 * @param arg0 引き数１
	 * @param arg1 引き数２
	 * @return 比較した結果
	 */
	public int compare(Object arg0, Object arg1) {
		//このメソッドはTreeMapの実装に依存しています。

		Long num0 = (Long) map.get(arg0);
		Long num1 = (Long) map.get(arg1);

		if (num1 == null) {
			num1 = new Long(count);
			map.put(arg1, num1);
			count++;
		}
		if (num0 == null) {
			num0 = new Long(count);
			map.put(arg0, num0);
			count++;
		}
		check();
		return num0.compareTo(num1);
	}

	/**
	 * 再配置が必要かチェックする。
	 *
	 */
	private void check() {
		if (count == Long.MAX_VALUE || count == (Long.MAX_VALUE - 1)) {
			replace();
		}
	}

	/**
	 * mapの再配置を行う。
	 *
	 */
	private void replace() {
		count = 0;
		Set keySet = map.keySet();
		Iterator ite = keySet.iterator();
		Iterator ite2;

		TreeMap sourtMap = new TreeMap();
		Collection col;

		while (ite.hasNext()) {
			//並び替え
			Object obj = ite.next();
			Long num = (Long) map.get(obj);
			sourtMap.put(num, obj);
		}

		//並び換えたものをmapに入れなおす。
		map.clear();
		col = sourtMap.values();
		ite2 = col.iterator();
		while (ite2.hasNext()) {
			Object obj = ite2.next();
			map.put(obj, new Long(count));
			count++;
		}
	}

}
