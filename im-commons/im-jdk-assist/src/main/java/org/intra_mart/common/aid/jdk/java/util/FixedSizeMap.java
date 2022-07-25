package org.intra_mart.common.aid.jdk.java.util;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * <B>LRU</B>(Least Recently Used) アルゴリズムによる
 * Map インターフェースの実装を提供します。<p>
 * このクラスのマップは、決められたサイズの領域持ちます。
 * キーはコンストラクタで指定された
 * マップのサイズに等しい個数しか、このマップに関連付けることはできません。
 * すでにマップのサイズに等しい個数のキーが関連付けられている状態で、
 * 別の新しいキーを関連付けようとした場合、LRU アルゴリズムに従い
 * 最も古くに使われたキーが削除され、全体のキー数がマップのサイズに
 * 等しくなるように調整されます。<br>
 * このオブジェクトは、Thread safe となっています。<br>
 * このマップは LRU アルゴリズムにより動作するため、
 * マッピングの追加や更新をしたり、キーにマッピングされている値の取得を
 * した時に、キーのアクセス順をチェックします。
 * したがって、マップのサイズが大きさに比例して put() や get() メソッドの
 * レスポンス時間が長くなります。
 * このハッシュテーブルは、適度な大きさで利用することを推奨します。
 * 
 * @see java.util.Hashtable
 */
public class FixedSizeMap extends AbstractMap{
	// インスタンス変数
	private int MAX_POOL;
	private Map DATA_STORE = new HashMap();						// 情報格納庫
	private LinkedList ORDER_LIST = new LinkedList();			// 順序列

	/**
	 * 指定されたサイズの領域を持つマップを構築します。
	 * @param size ハッシュテーブルの容量
	 * @throws IllegalArgumentException 容量が 1 より小さい場合
	 */
	public FixedSizeMap(int size) throws IllegalArgumentException{
		super();
		if(size > 0){ MAX_POOL = size; }
		else{ throw new IllegalArgumentException("Illegal Initial Capacity: " + String.valueOf(size)); }
	}

	/**
	 * 指定のキーの使用頻度リスト内での順位付け修正。
	 * すでにキーがリスト内に登録済みの場合は、使用頻度順の変更をします。
	 * @param key チェックをするキー
	 * @return 使用頻度リスト内に key が存在していた場合 true、そうでない場合 false。
	 */
	private boolean refleshOrder(Object key){

		if(ORDER_LIST.remove(key)){
			ORDER_LIST.addFirst(key);	// キーの移動
			return true;
		}
		else{
			return false;				// キーが存在せず
		}
	}

	/**
	 * このマップに対してマップできるキーの最大個数を返します。
	 * このメソッドの返す値は、コンストラクタで指定されたハッシュテーブルの
	 * 容量値をそのまま返すものです。
	 * @return このマップにエントリできるキーの最大個数
	 */
	public int capacity(){
		return MAX_POOL;
	}

	/**
	 * マップからマッピングをすべて削除します。
	 */
	public synchronized void clear(){
		this.DATA_STORE.clear();
		this.ORDER_LIST.clear();
	}

	/**
	 * 指定されたキーへのマッピングをこのマップが格納する場合に
	 * true を返します。
	 * @param key このマップにあるかどうかを判定するキー
	 * @return 指定されたキーへのマッピングをこのマップが含む場合は true
	 */
	public synchronized boolean containsKey(Object key){
		return this.DATA_STORE.containsKey(key);
	}

	/**
	 * このマップに格納されているマッピングのセットビューを返します。
	 * このセットの各要素は Map.Entry です。セットはこのマップを
	 * 基にしているので、マップへの変更、およびセットへの変更は、
	 * 互いに反映されます。
	 * セットでの繰り返し処理が進行中にマップが変更された場合、
	 * 反復の結果は保証されません。セットは要素の削除をサポートしており、
	 * 対応するエントリをマップから削除することができます。
	 * 削除には、Iterator.remove、Set.remove、removeAll、retainAll、
	 * および clear オペレーションが使えます。
	 * add または addAll オペレーションはサポートされません。
	 * @return このマップに格納されているマッピングのセットビュー
	 */
	public Set entrySet(){
		return this.DATA_STORE.entrySet();
	}

	/**
	 * マップが、指定されたキーにマッピングしている値を返します。
	 * マップに、このキーに対するマッピングがない場合は null を返します。
	 * @param key 関連付けられている値が返されるキー
	 * @return マップが、指定されたキーにマッピングしている値。このキーに対するマッピングがマップにない場合は null
	 * @see #containsKey(Object)
	 */
	public synchronized Object get(Object key){
		Object value = this.DATA_STORE.get(key);
		if(value != null){ this.refleshOrder(key); }
		return value;
	}

	/**
	 * 指定された値を、マップ内の指定されたキーに関連付けます 。
	 * マップにすでにこのキーに対するマッピングがある場合、
	 * 古い値は置き換えられます。
	 * @param key 指定された値が関連付けられるキー
	 * @param value 指定されたキーに関連付けられる値
	 * @return 指定されたキーに関連付けられていた以前の値。key にマッピングがなかった場合は null。
	 * @throws NullPointerException キーまたは値が null の場合
	 */
	public synchronized Object put(Object key, Object value){
		if(! this.refleshOrder(key)){
			// キーは存在せず＝キーは全く新しいモノだ
			while(MAX_POOL <= this.size()){
				// 最も使われていなかった情報の削除
				this.DATA_STORE.remove(this.ORDER_LIST.removeLast());
			}
			ORDER_LIST.addFirst(key);	// キーの追加
		}

		return this.DATA_STORE.put(key, value);
	}

	/**
	 * このキーにマッピングがある場合に、そのマッピングをマップから削除します。
	 * @param key マッピングがマップから削除されるキー
	 * @return 指定されたキーに関連付けられていた以前の値。key にマッピングがなかった場合は null。
	 */
	public synchronized Object remove(Object key){
		ORDER_LIST.remove(key);
		return this.DATA_STORE.remove(key);
	}
}

/* End of File */