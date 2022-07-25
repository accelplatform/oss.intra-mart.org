package org.intra_mart.jssp.view.tag;

import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="Content-Type"&gt; タグ。<br/>
 * <br/>
 * クライアントへ送信される応答のコンテンツ形式を設定します。<br/>
 * 属性 value に指定したコンテンツ形式が、応答ヘッダ Content-type の値になります。<br/>
 * コンテンツ形式には、たとえば text/html; charset=Shift_JIS のように、<br/>
 * 使用される文字エンコーディングのタイプを含めることができます。<br/>
 *
 */
public class ImartTag4ContentType implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "Content-Type";
	}
	
	
	/**************************************************************************
	 * &lt;IMART type="Content-Type"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】空文字列
	 *        【 概要 】Content-Type を設定するタグ
	 *                  value: コンテンツの MIME タイプを指定する String
	 *                         （ServletResponse の #setContentType()にセットする値）
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		Object contentType = oAttr.get("value", null);

		HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
		HttpServletResponse response = httpContext.getResponse();
		response.setContentType(String.valueOf(contentType));

		return "";
	}

}
