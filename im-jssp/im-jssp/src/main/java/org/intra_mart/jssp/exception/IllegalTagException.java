package org.intra_mart.jssp.exception;

/**
 * タグのシンタックスエラー等を通知するための例外
 */
public class IllegalTagException extends JSSPSystemException{
	public IllegalTagException(String msg){
		super(msg);
	}
}
