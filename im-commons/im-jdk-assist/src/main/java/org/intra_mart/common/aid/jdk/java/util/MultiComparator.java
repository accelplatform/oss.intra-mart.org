package org.intra_mart.common.aid.jdk.java.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import org.intra_mart.common.aid.jdk.java.util.*;


/**
 * 複数のコンパレータを使用してソートを行うためにクラスです。<br>
 * 実行したコンパレータの <CODE>compare()</CODE> メソッドの戻り値が０でなくなるか、全てのコンパレータの実行が完了するまで下記の順番でコンパレータの実行が行われます。<BR><P>
 * ・コンストラクタで指定された第１コンパレータ<BR>
 * ・コンストラクタで指定された第２コンパレータ<BR>
 * ・addComparator()で追加されたコンパレータ<BR>
 * <BR></P>
 * このクラスは同じインスタンスのコンパレータを２つ以上持つことを想定されていません。
 * そのような操作を行うと、正しい結果が得られない可能性があります。
 * 
 */
public class MultiComparator extends AbstractDualComparator {
	/**
	 * 第１コンパレータ
	 */
	private Comparator primaryComparator;
	/**
	 * 第２コンパレータ
	 */
	private MultiComparator secondaryComparator;

	/**
	 * 第１コンパレータ 、第２コンパレータを指定して、新しいオブジェクトを生成します。<br>
	 * もし <CODE>primaryComparator</CODE> がnullで、 <CODE>secondaryComparator</CODE> がnullでない時は、<CODE>secondaryComparator</CODE> が第１コンパレータとして扱われます。<br>
	 * <CODE>primaryComparator</CODE> 、<CODE>secondaryComparator</CODE> がともにnullの時は例外を発生させます。
	 * @param primaryComparator　第１コンパレータ
	 * @param secondaryComparator　第２コンパレータ
	 * @throws NullPointerException  <CODE>primaryComparator</CODE> 、 <CODE>secondaryComparator</CODE> がともにnullの時発生します。
	 */
	public MultiComparator(
		Comparator primaryComparator,
		Comparator secondaryComparator) throws NullPointerException{

		if (primaryComparator == null && secondaryComparator == null) {
			//プリマリ、セカンダリどちらもNULLのときは例外を発生させる
			throw new NullPointerException("PrimaryComparator and SecondaryComparator is NULL!");
		} else if (primaryComparator == null) {
			//プリマリがNULLの時は、セカンダリをプリマリとして扱う
			this.primaryComparator = secondaryComparator;
		} else {
			this.primaryComparator = primaryComparator;
			//リンクドリストみたいなアルゴリズム
			if (secondaryComparator != null) {
				this.secondaryComparator =
					new MultiComparator(secondaryComparator, null);
			}
		}
	}

	/**
	 * 第１コンパレータを返します。
	 * @return 第１コンパレータ
	 */
	public Comparator getPrimaryComparator() {
		return this.primaryComparator;
	}

	/**
	 * 第２コンパレータを返します。
	 * このメソッドの戻り値は、設定された第２コンパレータをラップした<CODE>MultiComparator</CODE>のオブジェクトになります。
	 * もし、第２コンパレータが設定されていないときはnullを返します。
	 * @return 第２コンパレータ
	 */
	public Comparator getSecondaryComparator() {
		return this.secondaryComparator;
	}

	/**
	 * 指定されたコンパレータ <CODE>comparator</CODE> を追加します。
	 * 指定されたコンパレータ <CODE>comparator</CODE>がnullの時は例外を発生させます。
	 * @param comparator　コンパレータ
	 * @throws NullPointerException 指定された<CODE>comparator</CODE>がnullの時に発生します。
	 */
	public void addComparator(Comparator comparator) throws NullPointerException{
		if(comparator == null) {
			throw new NullPointerException("The specified comparator is Null!");
		}

		if (secondaryComparator == null) {
			secondaryComparator = new MultiComparator(comparator, null);
		} else {
			secondaryComparator.addComparator(comparator);
		}
	}

	/**
	 * 指定されたコンパレータ <CODE>comparator</CODE>を削除します。
	 * 指定されたコンパレータの削除に成功したときはtrue、失敗したときはfalseを返します。
	 * このクラスのオブジェクトが持つ全てのコンパレータを削除しようとすると例外が発生します。
	 * @param comparator 削除対象のコンパレータ
	 * @return 削除に成功したときはtrue、失敗したときはfalseを返す。
	 * @throws IllegalArgumentException 全てのコンパレータを削除しようとした時に発生します。
	 */
	public boolean removeComparator(Comparator comparator)
		throws IllegalArgumentException {
		boolean result = false;
		if (comparator == this.primaryComparator) {
			//このブロックはトップレベルでのみ実行されます。

			if (this.secondaryComparator == null) {
				//全てのコンパレータを削除しようとしたときは例外を発生させる。
				throw new IllegalArgumentException("If the specified comparator is deleted, the comparator which MultiComparator has will be lost.");
			}
			//自分のprimaryと一致したとき
			this.primaryComparator = secondaryComparator.getPrimaryComparator();
			this.secondaryComparator =
				(MultiComparator) secondaryComparator.getSecondaryComparator();
			result = true;

		} else if (this.secondaryComparator == null) {
			//自分のprimaryと一致せず、secondaryComparatorがnullの時はfalseを返す
			result = false;
		} else if (
			comparator == this.secondaryComparator.getPrimaryComparator()) {
			//自分のsecondary(子）のprimaryと一致
			this.secondaryComparator =
				(MultiComparator) this
					.secondaryComparator
					.getSecondaryComparator();
			result = true;
		} else {
			//自分のsecondary（子)のremoveComparatorを呼び出す
			result = this.secondaryComparator.removeComparator(comparator);
		}
		return result;
	}

	/**
	 * 設定されている全てのコンパレータを取得します。
	 * @return　設定されている全てのコンパレータ
	 */
	public Collection getComparators() {
		ArrayList list = new ArrayList();
		MultiComparator multi = this;

		do {
			list.add(multi.getPrimaryComparator());
			multi = (MultiComparator) multi.getSecondaryComparator();
		} while (multi != null);
		return list;
	}
}
