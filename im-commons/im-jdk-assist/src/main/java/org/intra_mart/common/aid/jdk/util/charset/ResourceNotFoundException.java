package org.intra_mart.common.aid.jdk.util.charset;

/**
 * リソースがないことを通知する例外です。<p>
 *
 */
public class ResourceNotFoundException extends Exception{
	/**
	 * 指定された詳細メッセージを使用して、新規スロー可能オブジェクトを構築します。
	 * @param msg エラーメッセージ
	 */
	public ResourceNotFoundException(String msg){
		super(msg);
	}

	/**
	 * 指定された詳細メッセージおよび原因を使用して新規スロー可能オブジェクトを構築します。
	 * @param msg エラーメッセージ
	 * @param cause 原因。null 値が許可される。null 値は原因が存在しないか未知であることを示す
	 */
	public ResourceNotFoundException(String msg, Throwable cause){
		super(msg, cause);
	}
}

/* End of File */