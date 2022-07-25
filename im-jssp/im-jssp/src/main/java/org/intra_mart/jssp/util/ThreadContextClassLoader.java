package org.intra_mart.jssp.util;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;


/**
 * 現在実行中のスレッドが持つコンテキストクラスローダを親とする
 * クラスローダです。
 */
public class ThreadContextClassLoader extends ClassLoader{
	/**
	 * 新しいクラスローダを作成します。
	 */
	public ThreadContextClassLoader(){
		super(ThreadContextClassLoader.class.getClassLoader());
	}

	/**
	 * 指定されたクラスを探します。
	 * @param name クラスの名前
	 * @return 結果の Class オブジェクト
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException{
		return this.currentClassLoader().loadClass(name);
	}

	/**
	 * 指定された名前を持つリソースを見つけます。
	 * @param name リソース名
	 * @return リソースを読み込むための URL。ただし、リソースが見つからなかった場合や、呼び出し側がリソースを取得する適切な特権を持っていない場合は null
	 */
	protected  URL findResource(String name){
		return this.currentClassLoader().getResource(name);
	}

	/**
	 * 指定された名前を持つすべてのリソースを表す URL の Enumeration を返します。
	 * @param name リソース名
	 * @return リソースの URL の Enumeration
	 */
	protected Enumeration<URL> findResources(String name) throws IOException{
		return this.currentClassLoader().getResources(name);
	}

	/**
	 * 現在実行中のスレッドが持つコンテキストクラスローダを返します。
	 */
	private ClassLoader currentClassLoader(){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader != null){
			return classLoader;
		}
		else{
			return ClassLoader.getSystemClassLoader();
		}
	}
}

