package org.intra_mart.common.aid.jsdk.javax.servlet.http.impl;

import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;



/**
 * HTTP コンテキスト・マネージャの実装
 * 
 */
public class HTTPContextManagerImpl extends HTTPContextManager {

	/**
	 * スレッドに関連付けて HTTP コンテキストを一時保存するマップ
	 */
	private Map contextMap = new WeakHashMap();

	/**
	 * 現在のスレッドに関連付けられた HTTP コンテキストを返します。
	 * @return 現在のスレッドに関連付けられた HTTP コンテキスト
	 */
	public synchronized HTTPContext getCurrentContext(){
		return (HTTPContext) contextMap.get(Thread.currentThread());
	}

	/**
	 * マネージャーが保持するすべての HTTP コンテキストを返します。
	 * @return マネージャーが保持するすべての HTTP コンテキストの配列
	 */
	public synchronized HTTPContext[] getAllContext(){
		return (HTTPContext[]) this.contextMap.values().toArray(new HTTPContext[this.contextMap.size()]);
	}
	
	/**
	 * マネージャーが保持する HTTP コンテキストの数を返します。
	 * @return マネージャーが保持する HTTP コンテキストの数
	 */
	public synchronized int getLength(){
		return this.contextMap.size();
	}
	
	/**
	 * 現在のスレッドに指定のコンテキストを関連付けます。
	 * @param httpContext 関連付ける HTTP コンテキスト
	 * @return 以前に関連付けれられていた HTTP コンテキスト。以前に関連付けられていた HTTP コンテキストがない場合は null。
	 */
	public synchronized HTTPContext entry(HTTPContext httpContext){
		if(httpContext != null){
			Thread thread = Thread.currentThread();
			return (HTTPContext) this.contextMap.put(thread, httpContext);
		}
		else{
			return this.release();
		}
	}

	/**
	 * 現在のスレッドに指定の HTTP コンテキストを関連付けます。
	 * @param servletContext サーブレット・コンテキスト
	 * @param request リクエスト
	 * @param response レスポンス
	 * @return 以前に関連付けれられていた HTTP コンテキスト。以前に関連付けられていた HTTP コンテキストがない場合は null。
	 */
	public HTTPContext entry(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response){
		return this.entry(new HTTPContextImpl(servletContext, request, response));
	}

		
	/**
	 * 現在のスレッドに対する HTTP コンテキストの関連付けを開放します。
	 * @return 以前に関連付けれられていた HTTP コンテキスト。以前に関連付けられていた HTTP コンテキストがない場合は null。
	 */
	public synchronized HTTPContext release(){
		return (HTTPContext) this.contextMap.remove(Thread.currentThread());
	}

}

