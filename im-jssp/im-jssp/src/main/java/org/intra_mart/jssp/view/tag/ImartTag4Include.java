package org.intra_mart.jssp.view.tag;

import java.io.FileNotFoundException;

import org.intra_mart.jssp.exception.JavaScriptRedirectException;
import org.intra_mart.jssp.exception.TagRuntimeException;
import org.intra_mart.jssp.page.Page;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="include"&gt; タグ。<br/>
 * <br/>
 * 任意のJSSPコンテンツをコールして部分ページソースを挿入します。<br/>
 * 属性 page に対して次に表示させたいページパス（拡張子なし）を指定する ことで、部分ページを表示させることができます。<br/>
 * ここで指定するパスは、JSSPコンテンツのソースディレクトリからの相対パス形式になります。（デフォルトはコンテキストパスに対応する実際のパス）<br/>
 * <br/>
 * その他、表示ページ(属性 page に指定しているプログラム)に対して引き渡す引数を、任意の個数だけ指定可能です。<br/>
 * page に指定されたページのファンクション・コンテナ内 init() 関数では、引数 argV で受け取る事ができます。<br/>
 * その際、属性名称が引数 argV オブジェクトのプロパティ名となり、属性値が引数 argV オブジェクトの該当プロパティに格納されます。<br/>
 */
public class ImartTag4Include implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "include";
	}

	/**********************************************************************************
	 * &lt;IMART type="include"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】ページインポート
	 *                  ページプログラム(HTML&JS)を読み込んで実行結果を置換
	 *                  page  : 実行対象ページソースパス
	 *                  その他: 実行 JS ファイル内 init() への引数
	 *                          実行される JS ファイル内の init(attr, client) の
	 *                          第１引数 attr としてタグの属性および属性値が
	 *                          オブジェクト形式で渡される。
	 * </pre>
	 * @throws JavaScriptRedirectException
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 **********************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		// 実行環境の取得
		Context cx = Context.getCurrentContext();

		// コンテンツオブジェクトのインスタンス生成
		Page page = new Page(ScriptRuntime.toString(oAttr.get("page", null)));

		// 引数 attr, client オブジェクトの生成
		Object args[] = { oAttr };
		
		try {
			return page.execute(cx, args);
		}
		catch (JavaScriptRedirectException e) {
			throw e;
		}
		catch (JavaScriptException e) {
			throw e;
		}
		catch (FileNotFoundException e) {
			throw new TagRuntimeException(this.getTagName(), e);
		}
	}

}
