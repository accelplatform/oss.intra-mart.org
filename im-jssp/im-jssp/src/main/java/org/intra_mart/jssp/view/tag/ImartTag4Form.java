package org.intra_mart.jssp.view.tag;

import org.intra_mart.jssp.exception.TagRuntimeException;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="form"&gt; タグ。<br/>
 * <br/>
 * フォームタグ(&lt;FORM"&gt;)を生成します。<br/>
 * このタグを利用する事でJSSP実行環境のセッションを維持しながらプログラムを動作させる事ができます。<br/>
 * <br/>
 * 属性 page に対して次に表示させたいページパス（拡張子なし）を指定することで、ページ遷移をさせることができます。<br/>
 * ここで指定するパスは、JSSPコンテンツのソースディレクトリからの相対パス形式になります。（デフォルトはコンテキストパスに対応する実際のパス）<br/>
 * <br/>
 * 属性 label は、遷移先ページ内の任意のアンカータグ(&lt;A name="*"&gt;)の位置を初期表示位置として指定する事ができます。<br/>
 * （この指定はブラウザの機能を利用しているため、ブラウザ製品により期待通りに動作しない場合があります）<br/>
 * <br/>
 * 属性 action は、このフォームを表示したページのファンクション・コンテナ内に定義されている関数をリクエスト時に起動します。<br/>
 * action 関数の実行は、遷移先ページの作成処理(該当ページのファンクション・コンテナ内 init() 関数)よりも先に動作します。<br/>
 * <br/>
 * 遷移先ページ(属性 page に指定しているプログラム)に対して引き渡すＵＲＬ引数は、&lt;IMART type="hidden"&gt; または &lt;INPUT type="hidden"&gt; を利用して指定可能です。<br/>
 * 属性 page に指定されたページのファンクション・コンテナ内 init() 関数または、属性 action で指定された関数では、関数引数として request を受け取る事ができます。<br/>
 * その際、属性名称が引数 request オブジェクトのプロパティ名となり、属性値が引数 request オブジェクトの該当プロパティに格納されます。<br/>
 */
public class ImartTag4Form implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "form";
	}
	
	/**************************************************************************
	 * &lt;IMART type="form"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】フォームタグ（&lt;FORM&gt;）
	 *                  page  : 表示ページパス
	 *                  label : リンクラベル
	 *                  action: コール時起動関数(同ページ js 内)
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		TagObject tag = new TagObject("FORM");
		UniformResourceLocator url;
		Object page = oAttr.get("page", null);
		Object label = oAttr.get("label", null);
		Object action = oAttr.get("action", null);

		// 遷移先の設定
		if(page instanceof String){
			url = new UniformResourceLocator((String) page);	// 画面遷移用
		}
		else{
			url = new UniformResourceLocator();					// リロード用
		}

		// action 属性のチェック
		try{
			if(action instanceof String){
				url.setAction((String) action); 
			}
		}
		catch(IllegalArgumentException iae){
			throw new TagRuntimeException("form", iae);
		}

		// ラベルの設定
		if(label instanceof String){
			url.setLabel((String) label); 
		}

		// タグ基本属性の設定(Ｗｅｂスクリプトパス名の設定)
		String jsspQueryString = url.scriptNameWithSession();
		tag.setAttribute("action", jsspQueryString);

		// 不要な属性値の削除
		oAttr.delete("type");
		oAttr.delete("page");
		oAttr.delete("label");
		oAttr.delete("action");

		// タグ属性値の抽出
		Object[] params = oAttr.getIds();
		for(int idx = 0; idx < params.length; idx++){
			Object value = oAttr.get((String) params[idx], null);
			if((value instanceof String) || (value instanceof Number) || (value instanceof Boolean)){
				tag.setAttribute((String) params[idx], value);
			}
		}

		// スクリプト開発モデルでのページ遷移におけるURL引数用INPUTタグ
		String inputTag4JSSPQuery = url.createInputTag4JSSPQuery();
		return tag.getBegin() + inputTag4JSSPQuery + ((InnerTextObject) oInner).execute() + tag.getTerminate();
	}
}
