package org.intra_mart.jssp.view.tag;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.intra_mart.jssp.exception.NoSuchTagException;
import org.mozilla.javascript.Scriptable;


/**
 * &lt;IMART&gt; タグの登録、実行を管理するマネージャです。
 */
public class ImartTagTypeManger implements Serializable {

	/**
	 * タグ名をキーとしてImartTagTypeのインスタンスを保管するマップです。
	 */
	private Map<String, ImartTagType> imartTagTypeMap = new HashMap<String, ImartTagType>();

	private static ImartTagTypeManger _instance;

	/**
	 * インスタンスを返却します。（シングルトン）
	 * @return
	 */
	public static synchronized ImartTagTypeManger getInstance(){

		if(_instance == null){
			_instance = new ImartTagTypeManger();
		}
		return _instance;

	}

	/**
	 * プライベート・コンストラクタ
	 */
	private ImartTagTypeManger(){
	}
	
	
	/**
	 * &lt;IMART&gt; タグを定義します。
	 *
	 * @deprecated JSSP実行環境の起動時にのみ利用します。
	 * @param clazz {@link ImartTagType}を実装したクラス
	 *
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public void defineImartTag(Class clazz)
					throws InstantiationException,
							IllegalAccessException,
							SecurityException,
							NoSuchMethodException {

		Object object = clazz.newInstance();

		if(! (object instanceof ImartTagType) ){
			throw new IllegalStateException(
					"Imart-Tag must implement '" + ImartTagType.class.getName() + "': " + clazz.getName());
		}

		ImartTagType imartTagType = (ImartTagType) object;
		this.imartTagTypeMap.put(imartTagType.getTagName(), imartTagType);
	}

	/**
	 * &lt;IMART&gt; タグを実行します。
	 *
	 * @param name 実行するタグ名
	 * @param attr タグに対する属性情報
	 * @param inner タグに挟まれている部分の情報
	 * @return ＨＴＭＬソース（文字列）
	 *
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String invoke(String name, Scriptable attr, Scriptable inner)
					throws IllegalArgumentException, 
							IllegalAccessException, 
							InvocationTargetException {

		ImartTagType imartTagType = this.imartTagTypeMap.get(name);
		
		if(imartTagType != null){
			return imartTagType.doTag(attr, inner);
		}
		else{
			throw new NoSuchTagException(name);
		}
		
	}
}
