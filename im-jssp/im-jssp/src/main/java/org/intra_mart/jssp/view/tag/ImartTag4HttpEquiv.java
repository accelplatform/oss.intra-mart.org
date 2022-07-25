package org.intra_mart.jssp.view.tag;

import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="HTTP-EQUIV"&gt; タグ。<br/>
 * <br/>
 * 指定された名前と値を持つ応答ヘッダを設定します。<br/>
 * 属性 name にはヘッダの名前、属性 value にはヘッダの値を設定します。<br/>
 * ヘッダがすでに設定されていた場合は、新しい値が以前の値を上書きします。<br/>
 */
public class ImartTag4HttpEquiv implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "HTTP-EQUIV";
	}
	

	/**************************************************************************
	 * &lt;IMART type="HTTP-EQUIV"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】空文字列
	 *        【 概要 】指定された名前と値を持つ応答ヘッダを設定するタグ
	 *                  （HttpServletResponse の #setHeader() を利用します）
	 *                  name : ヘッダの名前
	 *                  value: ヘッダの値
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		Object headerName = oAttr.get("name", null);
		Object headerValue = oAttr.get("value", null);

		HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
		HttpServletResponse response = httpContext.getResponse();
		response.setHeader(String.valueOf(headerName), String.valueOf(headerValue));

		return "";
	}

}
