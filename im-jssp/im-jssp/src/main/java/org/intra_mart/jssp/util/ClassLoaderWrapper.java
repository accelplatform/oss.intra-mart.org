package org.intra_mart.jssp.util;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;


/**
 * クラスローダラッパ
 */
public class ClassLoaderWrapper extends ClassLoader{

	/**
	 * ラップするクラスローダ
	 */
	private ClassLoader classLoader = null;

	/**
	 * 新しいクラスローダを作成します。
	 * このクラスローダは、引数 loader をラップしただけの機能を提供します。
	 * @param loader ラップするローダ
	 */
	public ClassLoaderWrapper(ClassLoader loader){
		this(loader, ClassLoaderWrapper.class.getClassLoader());
	}

	/**
	 * 指定の親を持つクラスローダを作成します。
	 * このクラスローダは、引数 loader をラップしただけの機能を提供します。
	 * @param parent 親ローダ
	 * @param loader ラップするローダ
	 */
	public ClassLoaderWrapper(ClassLoader parent, ClassLoader loader){
		super(parent);
		this.classLoader = loader;
	}

	/**
	 * 指定されたクラスを探します。
	 * @param name クラスの名前
	 * @return 結果の Class オブジェクト
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException{
		return this.classLoader.loadClass(name);
	}

	/**
	 * 指定された名前を持つリソースを見つけます。
	 * @param name リソース名
	 * @return リソースを読み込むための URL。ただし、リソースが見つからなかった場合や、呼び出し側がリソースを取得する適切な特権を持っていない場合は null
	 */
	protected  URL findResource(String name){
		return this.classLoader.getResource(name);
	}

	/**
	 * 指定された名前を持つすべてのリソースを表す URL の Enumeration を返します。
	 * @param name リソース名
	 * @return リソースの URL の Enumeration
	 */
	protected Enumeration<URL> findResources(String name) throws IOException{
		return this.classLoader.getResources(name);
	}
}

