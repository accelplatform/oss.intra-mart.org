package org.intra_mart.common.aid.jsdk.javax.servlet.http.session;


/**
 * 特定のＨＴＴＰセッションが見つからない場合にスローされます。
 * 
 */
public class NoSuchSessionException extends Exception{
	/**
	 * 詳細メッセージを持たない NoSuchSessionException を構築します。
	 */
	public NoSuchSessionException(){
		super();
	}

	/**
	 * 詳細メッセージを持つ NoSuchSessionException を構築します。
	 * @param message 詳細メッセージ
	 */
	public NoSuchSessionException(String message){
		super(message);
	}

	/**
	 * 指定された詳細メッセージおよび原因を使用して新しい NoSuchSessionException を構築します。
	 * @param message 詳細メッセージ
	 * @param cause 原因 (Throwable.getCause() メソッドによる取得用に保存される)。null 値が許可される。null 値は原因が存在しないか未知であることを示す
	 */
	public NoSuchSessionException(String message, Throwable cause){
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して新しい NoSuchSessionException を構築します。
	 * @param cause 原因 (Throwable.getCause() メソッドによる取得用に保存される)。null 値が許可される。null 値は原因が存在しないか未知であることを示す
	 */
	public NoSuchSessionException(Throwable cause){
		super(cause);
	}
}
