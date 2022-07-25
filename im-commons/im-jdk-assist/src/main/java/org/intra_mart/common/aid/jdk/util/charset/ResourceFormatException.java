package org.intra_mart.common.aid.jdk.util.charset;

/**
 * リソースの内容が不適切であることを通知する例外です。<p>
 * 設定の内容に誤りがあるか、または定義の書式が正しくない場合にスローされます。
 *
 */
public class ResourceFormatException extends Exception{
	/**
	 * 指定された詳細メッセージを使用して、新規スロー可能オブジェクトを構築します。
	 * @param msg エラーメッセージ
	 */
	public ResourceFormatException(String msg){
		super(msg);
	}

	/**
	 * 指定された詳細メッセージおよび原因を使用して新規スロー可能オブジェクトを構築します。
	 * @param msg エラーメッセージ
	 * @param cause 原因。null 値が許可される。null 値は原因が存在しないか未知であることを示す
	 */
	public ResourceFormatException(String msg, Throwable cause){
		super(msg, cause);
	}
}

/* End of File */