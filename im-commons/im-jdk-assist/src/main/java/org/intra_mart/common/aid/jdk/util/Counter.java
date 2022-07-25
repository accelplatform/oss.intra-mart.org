package org.intra_mart.common.aid.jdk.util;

import java.io.Serializable;

/**
 * このクラスは、カウンター機能を提供します。<P>
 * ある数値を基準として１ずつ増やしたり１ずつ減らしたりすることができます。
 *
 */
public class Counter extends Number implements Serializable{
	/**
	 * カウンターの値
	 */
	private long currentValue = 0;

	/**
	 * カウンターオブジェクトを作成します。<p>
	 * 初期値は０です。したがって、このコンストラクタの呼び出しは、
	 * 以下の呼び出しと同義です。<br>
	 * <code>new Counter(0)</code>
	 */
	public Counter(){
		this(0);
	}

	/**
	 * 指定の初期値でカウンターオブジェクトを作成します。
	 * @param initValue カウンターの初期値
	 */
	public Counter(long initValue){
		super();

		this.currentValue = initValue;
	}

	/**
	 * カウンターの値を１だけ増加させます。
	 * @return 変化後のカウンターの値
	 */
	public synchronized long increment(){
		return ++currentValue;
	}

	/**
	 * カウンターの値を１だけ減少させます。
	 * @return 変化後のカウンターの値
	 */
	public synchronized long decrement(){
		return --currentValue;
	}

	/**
	 * カウンターの現在の値を int 型として返します。
	 * 値を丸めたり切り捨てたりすることもあります。
	 * @return このオブジェクトが表す数値を int 型に変換した値;
	 */
	public synchronized int intValue(){
		return (int) this.currentValue;
	}

	/**
	 * カウンターの現在の値を long 型として返します。
	 * @return このオブジェクトが表す数値を long 型に変換した値
	 */
	public synchronized long longValue(){
		return this.currentValue;
	}

	/**
	 * カウンターの現在の値を float 型として返します。
	 * 値を丸めることもあります。
	 * @return このオブジェクトが表す数値を float 型に変換した値
	 */
	public synchronized float floatValue(){
		return (float) this.currentValue;
	}

	/**
	 * カウンターの現在の値を double 型として返します。
	 * 値を丸めることもあります。
	 * @return このオブジェクトが表す数値を double 型に変換した値
	 */
	public synchronized double doubleValue(){
		return (double) this.currentValue;
	}
}

