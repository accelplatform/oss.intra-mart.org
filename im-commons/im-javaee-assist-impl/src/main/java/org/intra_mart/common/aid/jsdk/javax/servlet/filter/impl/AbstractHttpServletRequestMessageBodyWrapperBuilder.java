package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import javax.servlet.FilterConfig;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletRequestMessageBodyWrapperBuilder;


/**
 * HttpServletRequest を拡張したインタフェースです。
 */
public abstract class AbstractHttpServletRequestMessageBodyWrapperBuilder implements HttpServletRequestMessageBodyWrapperBuilder{
	/**
	 * このビルダを使うフィルタの設定オブジェクト
	 */
	private FilterConfig filterConfig = null;

	/**
	 * 唯一のコンストラクタ
	 */
	protected AbstractHttpServletRequestMessageBodyWrapperBuilder(){
		super();
	}

	/**
	 * フィルタ設定オブジェクトを返します。
	 * 返されるフィルタ設定オブジェクトは、
	 * このビルダが初期化された（{@link #init(FilterConfig)}が呼び出された）
	 * 時に渡されたものと同じです。
	 * @return フィルタ設定オブジェクト
	 */
	public FilterConfig getFilterConfig(){
		return this.filterConfig;
	}

	/**
	 * このオブジェクトを初期化します。
	 */
	public final void init(FilterConfig config){
		this.filterConfig = config;
		this.handleInit();
	}

	/**
	 * このオブジェクトを初期化します。<p>
	 * このメソッドは、{@link #init(FilterConfig)} から呼び出されます。
	 * サブクラスが初期化処理を必要とする場合は、
	 * このメソッドをオーバーライドしてください。<p>
	 * このメソッドは、何もしません。
	 */
	public void handleInit(){
		return;
	}

	/**
	 * このオブジェクトの破棄処理をします。<p>
	 */
	public final void destroy(){
		try{
			this.handleDestroy();
		}
		finally{
			this.filterConfig = null;
		}
	}

	/**
	 * このオブジェクトの破棄処理をします。<p>
	 * このメソッドは、{@link #destroy()} から呼び出されます。
	 * サブクラスが終了処理を必要とする場合は、
	 * このメソッドをオーバーライドしてください。<p>
	 * このメソッドは何もしません。
	 */
	public void handleDestroy(){
		return;
	}
}

