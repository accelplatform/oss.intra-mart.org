package org.intra_mart.common.aid.jsdk.javax.servlet.exception;

import javax.servlet.ServletException;

/**
 * Servlet 例外の拡張モデルです。
 * JRE 1.4 から、Throwable に #getCause() が実装されました。
 * これと、ServletException が従来から持つ #getRootCause() が
 * 同義となるようにしてあります。
 * 
 * @see javax.servlet.ServletException
 */
public class ExtendedServletException extends ServletException{
	/**
	 * 例外オブジェクトを作成します。
	 */
	public ExtendedServletException(){
		super();
	}

	/**
	 * 指定されたメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public ExtendedServletException(String message){
		super(message);
	}

	/**
	 * 指定されたメッセージと根本原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public ExtendedServletException(String message, Throwable rootCause){
		super(message, rootCause);
		if(this.getCause() != rootCause){ this.initCause(rootCause); }
	}

	/**
	 * 根本原因となった例外を持つ例外を作成します。
	 * @param rootCause 原因となった例外
	 */
	public ExtendedServletException(Throwable rootCause){
		super(rootCause);
		if(this.getCause() != rootCause){ this.initCause(rootCause); }
	}
}

