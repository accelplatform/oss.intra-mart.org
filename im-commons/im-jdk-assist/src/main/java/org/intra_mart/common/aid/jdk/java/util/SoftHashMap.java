package org.intra_mart.common.aid.jdk.java.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * ハッシュテーブルに基づいた「ソフト参照」によるMap実装です。<p>
 * キーにマッピングされた値をソフト参照によって保持します。
 * キーは、ハッシュ値とオブジェクトの同値関係によって検証されるので、
 * 異なるインスタンスであっても同一キーとみなされることがあります。<br>
 * このオブジェクトは、メモリに敏感なキャッシュ機能を実装するときに
 * 利用できます。<p>
 * このマップは、キーに関連付けられる値に null を許容しません。<p>
 * <b>この実装は同期化されません。</b>
 */
public class SoftHashMap extends AbstractMap{
	// データの保管用
	private SoftEntryMap entryMap = new SoftEntryMap();

	/**
	 * 空の SoftHashMap を作成します。
	 */
	public SoftHashMap(){
		super();
	}

	/**
	 * マップ内のキーと値のマッピングの数を返します。
	 * マップに Integer.MAX_VALUE より多くの要素がある場合は、
	 * Integer.MAX_VALUE を返します。
	 * @return マップ内のキー値マッピングの数
	 */
	public int size() {
		this.entryMap.expungeStaleEntries();
		return this.entryMap.size();
	}

	/**
	 * このマップがこの値に 1 つ以上のキーをマッピングする場合に、
	 * true を返します。
	 * より厳密に言えば、
	 * このマップに、(value==null ? v==null : value.equals(v)) となるような
	 * 値 v へのマッピングが 1 つ以上ある場合に限って true を返します。
	 * @param value マップにあるかどうかを判定される値
	 * @return このマップが 1 つ以上のキーにこの値をマッピングする場合は true
	 */
	public boolean containsValue(Object value){
		this.entryMap.expungeStaleEntries();
		if(value != null){
			Collection collection = this.values();
			Iterator iterator = collection.iterator();
			while(iterator.hasNext()){
				Object o = iterator.next();
				if(value.equals(o)){
					return true;			// 一致するものを発見
				}
			}
		}
		else{
			return false;
		}

		return false;
	}

	/**
	 * マップが指定のキーのマッピングを保持する場合に true を返します。
	 * @param key マップにあるかどうかが判定されるキー
	 * @return マップが指定のキーのマッピングを保持する場合は true
	 */
	public boolean containsKey(Object key){
		this.entryMap.expungeStaleEntries();
		return this.entryMap.containsKey(key);
	}

	/**
	 * このマップによる指定されたキーのマッピング先となる値を返します。<p>
	 * このキーへのマッピングをマップが含まない場合は null を返します。
	 * また、キーにマッピングされている値が、
	 * 既にガーベージコレクションの対象となり取り出せい場合は、nullを返します。
	 *
	 * @param key 関連付けられた値が返されるキー
	 * @return マップが指定されたキーをマップする値
	 */
	public Object get(Object key){
		this.entryMap.expungeStaleEntries();
		SoftEntry entry = this.entryMap.getSoftEntry(key);
		if(entry != null){
			return entry.getValue();
		}
		return null;
	}

	/**
	 * 指定された値と指定されたキーをこのマップに関連付けます。
	 * @param key 指定される値が関連付けられるキー
	 * @param value 指定されるキーに関連付けられる値
	 * @return 指定のキーに関連した以前の値。キーにマッピングがなかった場合は null。
	 * @throws NullPointerException 指定された値が null の場合
	 */
	public Object put(Object key, Object value){
		this.entryMap.expungeStaleEntries();
		if(value != null){
			return this.entryMap.put(key, value);
		}
		else{
			throw new NullPointerException("key is mapped to null. key=" + key);
		}
	}

	/**
	 * このキーにマッピングがある場合に、そのマッピングをマップから削除します。
	 * @param key マッピングがマップから削除されるキー
	 * @return 指定のキーに関連した以前の値。キーにエントリがなかった場合は null。
	 */
	public Object remove(Object key){
		this.entryMap.expungeStaleEntries();
		return this.entryMap.remove(key);
	}

	/**
	 * マップからマッピングをすべて削除します
	 */
	public void clear(){
		this.entryMap.clear();
	}

	/**
	 * このマップに格納されているキーの Set ビューを返します。
	 * @return マップに含まれているキーのセットビュー
	 */
	public Set keySet(){
		return new KeySet(this.entryMap);
	}

	private class KeySet extends AbstractSet{
		private SoftEntryMap entryMap;

		/**
		 * Create a KeySet.
		 */
		protected KeySet(SoftEntryMap entryMap){
			super();
			this.entryMap = entryMap;
		}

		public Iterator iterator(){
			this.entryMap.expungeStaleEntries();
			return new KeyIterator(this.entryMap);
		}

		public int size(){
			this.entryMap.expungeStaleEntries();
			return this.entryMap.size();
		}

		public boolean contains(Object o){
			this.entryMap.expungeStaleEntries();
			return this.entryMap.containsKey(o);
		}

		public boolean remove(Object o){
			this.entryMap.expungeStaleEntries();
			return this.entryMap.remove(o) != null;
		}

		public void clear(){
			this.entryMap.clear();
		}

		public Object[] toArray(){
			return this.getKeyCollection().toArray();
		}

		public Object[] toArray(Object a[]){
			return this.getKeyCollection().toArray(a);
		}

		private Collection getKeyCollection(){
			this.entryMap.expungeStaleEntries();
			Collection collection = new ArrayList(this.size());
			Iterator iterator = this.iterator();
			while(iterator.hasNext()){
				collection.add(iterator.next());
			}
			return collection;
		}

		private class KeyIterator extends SoftHashMapIterator{
			/**
			 * Create a KeyIterator.
			 */
			protected KeyIterator(SoftEntryMap entryMap){
				super(entryMap);
			}

			/**
			 * 次の要素を返す。
			 */
			public Object next(){
				return this.nextSoftEntry().getKey();
			}
		}
	}


	/**
	 * このマップに格納されている値のコレクションビューを返します。
	 * @return マップ内に保持されている値のコレクションビュー
	 */
	public Collection values(){
		return new ValuesCollection(this.entryMap);
	}

	private class ValuesCollection extends AbstractCollection{
		private SoftEntryMap entryMap;

		protected ValuesCollection(SoftEntryMap entryMap){
			super();
			this.entryMap = entryMap;
		}

		public Iterator iterator() {
			this.entryMap.expungeStaleEntries();
			return new ValueIterator(this.entryMap);
		}

		public int size() {
			this.entryMap.expungeStaleEntries();
			return this.entryMap.size();
		}

		public boolean contains(Object o) {
			this.entryMap.expungeStaleEntries();
			return this.entryMap.containsValue(o);
		}

		public void clear() {
			this.entryMap.clear();
		}

		public Object[] toArray(){
			return this.getValueCollection().toArray();
		}

		public Object[] toArray(Object a[]){
			return this.getValueCollection().toArray(a);
		}

		private Collection getValueCollection(){
			this.entryMap.expungeStaleEntries();
			Collection collection = new ArrayList(this.size());
			Iterator iterator = this.iterator();
			while(iterator.hasNext()){
				collection.add(iterator.next());
			}
			return collection;
		}

		private class ValueIterator extends SoftHashMapIterator{
			protected ValueIterator(SoftEntryMap entryMap){
				super(entryMap);
			}

			public Object next(){
				return this.nextSoftEntry().getValue();
			}
		}
	}


	/**
	 * このマップに格納されているマッピングのセットビューを返します。<p>
	 * このセットの各要素は Map.Entry です。
	 * セットはこのマップを基にしているので、
	 * マップへの変更、およびセットへの変更は、互いに反映されます。
	 * セットでの繰り返し処理が進行中にマップが変更された場合、
	 * 反復の結果は保証されません。
	 * セットは要素の削除をサポートしており、
	 * 対応するエントリをマップから削除することができます。
	 * 削除には、Iterator.remove、Set.remove、removeAll、retainAll、
	 * および clear オペレーションが使えます。
	 * add または addAll オペレーションはサポートされません。
	 * @return マップ内に保持されているマッピングのセットビュー
	 */
	public Set entrySet(){
		return new EntrySet(this.entryMap);
	}

	private class EntrySet extends AbstractSet{
		private SoftEntryMap entryMap;

		/**
		 * Create a EntrySet.
		 */
		protected EntrySet(SoftEntryMap entryMap){
			super();
			this.entryMap = entryMap;
		}

		public Iterator iterator(){
			this.entryMap.expungeStaleEntries();
			return new EntryIterator(this.entryMap);
		}

		public boolean contains(Object o){
			this.entryMap.expungeStaleEntries();
			if(o == null){
				if(o instanceof Map.Entry){
					Map.Entry entry = (Map.Entry) o;
					Object key = entry.getKey();
					Object candidate = this.entryMap.get(key);
					return entry.equals(candidate);
				}
			}
			return false;
		}

		public boolean remove(Object o){
			this.entryMap.expungeStaleEntries();
			if(o == null){
				if(o instanceof Map.Entry){
					Map.Entry entry = (Map.Entry) o;
					Object key = entry.getKey();
					Object candidate = this.entryMap.get(key);
					if(entry.equals(candidate)){
						return this.entryMap.remove(key) != null;
					}
				}
			}
			return false;
		}

		public int size(){
			this.entryMap.expungeStaleEntries();
			return this.entryMap.size();
		}

		public void clear(){
			this.entryMap.clear();
		}

		public Object[] toArray(){
			return this.getEntryCollection().toArray();
		}

		public Object[] toArray(Object a[]){
			return this.getEntryCollection().toArray(a);
		}

		private Collection getEntryCollection(){
			this.entryMap.expungeStaleEntries();
			Collection collection = new ArrayList(this.size());
			Iterator iterator = this.iterator();
			while(iterator.hasNext()){
				collection.add(iterator.next());
			}
			return collection;
		}

		private class EntryIterator extends SoftHashMapIterator{
			protected EntryIterator(SoftEntryMap entryMap){
				super(entryMap);
			}

			public Object next(){
				return this.nextSoftEntry();
			}
		}
	}



	/**
	 * Iterator の抽象実装。
	 * entrySet, keySet, values のそれぞれの具体実装に対する共通部分。
	 */
	private static abstract class SoftHashMapIterator implements Iterator{
		private int currentIndex = 0;
		private SoftEntry[] softEntries;
		private SoftEntryMap entryMap;
		private int expectedModCount;
		private SoftEntry lastReturned = null;

		/**
		 * Create a SoftHashMapIterator.
		 */
		protected SoftHashMapIterator(SoftEntryMap entryMap){
			super();
			this.entryMap = entryMap;
			this.expectedModCount = this.entryMap.currentModifyNumber();

			Collection collection = this.entryMap.values();
			this.softEntries = (SoftEntry[]) collection.toArray(new SoftEntry[collection.size()]);
		}

		/**
		 *
		 */
		public boolean hasNext(){
			return this.softEntries.length > this.currentIndex;
		}

		/**
		 * 次のエントリを返す。
		 */
		protected SoftEntry nextSoftEntry(){
			if(this.hasNext()){
				if(this.expectedModCount == this.entryMap.currentModifyNumber()){
					this.lastReturned = this.softEntries[this.currentIndex];
					this.currentIndex++;
					return this.lastReturned;
				}
				else{
					throw new ConcurrentModificationException();
				}
			}
			else{
				throw new NoSuchElementException();
			}
		}

		/**
		 * 最後に返された要素を削除します。
		 */
		public void remove(){
			if(this.lastReturned == null){
				throw new IllegalStateException();
			}

			this.entryMap.remove(this.lastReturned.getKey());			// 削除

			// 削除処理終了・・・後処理
			this.expectedModCount = this.entryMap.currentModifyNumber();
			this.lastReturned = null;
		}
	}



	/**
	 * キーと値を保持する箱
	 */
	private static class ValueReference extends SoftReference{
		private Object key;							// 関連付けられているキー
		private ReferenceQueue queue;				// 参照キュー
		private volatile boolean validity = true;	// 有効フラグ

		/**
		 * Create new reference.
		 */
		protected ValueReference(Object key, Object value, ReferenceQueue queue){
			super(value, queue);
			this.key = key;
			this.queue = queue;
		}

		/**
		 * マッピングされているキーを返します。
		 * @return キー
		 */
		public Object getKey(){
			return this.key;
		}

		/**
		 * データの有効(true)・無効(false)を返します。
		 * 有効の場合は、SoftReference の効果によって値が削除された場合、
		 * マップの関連付けを削除します。
		 */
		public boolean isValid(){
			return this.validity;
		}

		/**
		 * このデータを無効にします。
		 */
		public void invalidate(){
			this.validity = false;
			this.enqueue();
		}

		/**
		 * 参照キューを返します。
		 */
		public ReferenceQueue getReferenceQueue(){
			return this.queue;
		}
	}

	/**
	 * Map.Entry の実装
	 */
	private static class SoftEntry implements Map.Entry {
		private ValueReference valueReference;

		/**
		 * Create new entry.
		 */
		private SoftEntry(ValueReference valueReference){
			super();
			this.valueReference = valueReference;
		}

		/**
		 * Create new entry.
		 */
		protected SoftEntry(Object key, Object value, ReferenceQueue queue){
			this(new ValueReference(key, value, queue));
		}


		public Object getKey(){
			return this.getValueReference().getKey();
		}

		public Object getValue(){
			return this.getValueReference().get();
		}

		/**
		 * エントリに対応する値を、指定された値に置き換えます。
		 * @param value エントリに格納されている新しい値
		 * @return エントリに対応する以前の値
		 */
		public Object setValue(Object value){
			ValueReference reference = this.getValueReference();	// 参照
			reference.invalidate();									// 無効
			Object returnValue = this.getValue();					// 値を保存
			this.valueReference = new ValueReference(reference.getKey(), value, reference.getReferenceQueue());
			return returnValue;
		}

		/**
		 * 値への参照を返す。
		 */
		public ValueReference getValueReference(){
			return this.valueReference;
		}

		public boolean equals(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry e = (Map.Entry)o;
			Object k1 = getKey();
			Object k2 = e.getKey();
			if (k1 == k2 || (k1 != null && k1.equals(k2))) {
				Object v1 = getValue();
				Object v2 = e.getValue();
				if (v1 == v2 || (v1 != null && v1.equals(v2)))
					return true;
			}
			return false;
		}

		public int hashCode() {
			Object k = getKey();
			Object v = getValue();
			return	((k==null ? 0 : k.hashCode()) ^
					 (v==null ? 0 : v.hashCode()));
		}

		public String toString() {
			return getKey() + "=" + getValue();
		}
	}



	private static class SoftEntryMap extends HashMap{
		private volatile int modifyNumber = 0;
		private ReferenceQueue referenceQueue = new ReferenceQueue();

		protected SoftEntryMap(){
			super();
		}

		/**
		 * 値がガーベージコレクションの対象となり、
		 * 既に無効となっているマッピングをクリアする。
		 */
	    protected void expungeStaleEntries(){
			while(true){
				Reference reference = this.referenceQueue.poll();
				if(reference != null){
					ValueReference valueReference = (ValueReference) reference;
					if(valueReference.isValid()){
						Object key = valueReference.getKey();
						super.remove(key);			// マッピングを削除
					}
				}
				else{
					break;							// 対象ナシ→チェック終了
				}
			}
		}

		/**
		 * 現在の更新カウンタ値を返す
		 */
		public int currentModifyNumber(){
			return this.modifyNumber;
		}

		/**
		 * 指定のキーにマッピングされているエントリを返す
		 */
		public SoftEntry getSoftEntry(Object key){
			return (SoftEntry) super.get(key);
		}

		/**
		 * エントリのコレクションを返す
		 */
		public Collection getSoftEntryCollection(){
			return this.values();
		}

		/**
		 * このマップによる指定されたキーのマッピング先となる値を返します。<p>
		 * このキーへのマッピングをマップが含まない場合は null を返します。
		 * また、キーにマッピングされている値が、
		 * 既にガーベージコレクションの対象となり取り出せい場合は、nullを返します。
		 *
		 * @param key 関連付けられた値が返されるキー
		 * @return マップが指定されたキーをマップする値
		 */
		public Object get(Object key){
			Map.Entry entry = (Map.Entry) this.getSoftEntry(key);
			if(entry != null){
				return entry.getValue();
			}
			return null;
		}

		/**
		 * 指定された値と指定されたキーをこのマップに関連付けます。
		 * @param key 指定される値が関連付けられるキー
		 * @param value 指定されるキーに関連付けられる値
		 * @return 指定のキーに関連した以前の値。キーにマッピングがなかった場合は null。
		 * @throws NullPointerException 指定された値が null の場合
		 */
		public Object put(Object key, Object value){
			Object returnValue = null;
			Map.Entry entry = this.getSoftEntry(key);
			if(entry != null){
				returnValue = entry.getValue();
				entry.setValue(value);
			}
			else{
				SoftEntry softEntry = new SoftEntry(key, value, this.referenceQueue);
				super.put(key, softEntry);
			}
			this.modifyNumber++;
			return returnValue;
		}

		/**
		 * このキーにマッピングがある場合に、そのマッピングをマップから削除します。
		 * @param key マッピングがマップから削除されるキー
		 * @return 指定のキーに関連した以前の値。キーにエントリがなかった場合は null。
		 */
		public Object remove(Object key){
			SoftEntry softEntry = (SoftEntry) super.remove(key);
			if(softEntry != null){
				try{
					ValueReference valueReference = softEntry.getValueReference();
					valueReference.invalidate();		// 無効設定
					return valueReference.get();
				}
				finally{
					this.modifyNumber++;
				}
			}
			return null;
		}

		/**
		 * マップからマッピングをすべて削除します
		 */
		public void clear(){
			super.clear();
			this.referenceQueue = new ReferenceQueue();
			this.modifyNumber++;
		}
	}
}

