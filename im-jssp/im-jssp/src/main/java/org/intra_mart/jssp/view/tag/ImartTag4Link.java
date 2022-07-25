package org.intra_mart.jssp.view.tag;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;

import org.intra_mart.jssp.exception.TagRuntimeException;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="link"&gt; タグ。<br/>
 * <br/>
 * アンカータグ(&lt;A&gt;)を生成します。<br/>
 * このタグを利用する事で JSSP実行環境のセッションを維持しながらプログラムを動作させる事が出来ます。<br/>
 * <br/>
 * 属性 page に対して次に表示させたいページパス（拡張子なし）を指定することで、ページ遷移をさせることができます。<br/>
 * ここで指定するパスは、JSSPコンテンツのソースディレクトリからの相対パス形式になります。（デフォルトはコンテキストパスに対応する実際のパス）<br/>
 * <br/>
 * 属性 label は、遷移先ページ内の任意のアンカータグ(&lt;A name="*"&gt;)の位置を初期表示位置として指定する事ができます。<br/>
 * （この指定はブラウザの機能を利用しているため、ブラウザ製品により期待通りに動作しない場合があります）<br/>
 * <br/>
 * 属性 action に関数名称を指定すると、このリンクを表示したページのファンクション・コンテナ内に定義されている関数をリクエスト時に起動します。<br/>
 * 属性 action で指定された関数は、遷移先ページの作成処理(該当ページのファンクション・コンテナ内 init() 関数)よりも先に動作します。<br/>
 * <br/>
 * リンクの画面表示名称は、このタグに挟まれた範囲内の文字列になります。<br/>
 * <br/>
 * その他、遷移先ページ(属性 page に指定しているプログラム)に対して引き渡すＵＲＬ引数を、任意の個数だけ指定可能です。<br/>
 * 属性 page に指定されたページのファンクション・コンテナ内 init() 関数または、属性 action で指定された関数では、関数引数として request を受け取る事ができます。<br/>
 * その際、属性名称が引数 request オブジェクトのプロパティ名となり、属性値が引数 request オブジェクトの該当プロパティに格納されます。<br/>
 */
public class ImartTag4Link implements ImartTagType {

	private static Collection<String> tagAttributeNames4link = new HashSet<String>();

	static{
		tagAttributeNames4link.add("target");
		tagAttributeNames4link.add("onclick");
	}
	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "link";
	}

	
	/**************************************************************************
	 * &lt;IMART type="link"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】リンクタグ（&lt;A&gt;）
	 *                  page  : 表示ページパス
	 *                  label : リンクラベル
	 *                  action: コール時起動関数(同ページ js 内)
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		
		TagObject tag = new TagObject("A");		// アンカータグ
		
		UniformResourceLocator url;
		Object page = oAttr.get("page", null);
		Object action = oAttr.get("action", null);
		Object label = oAttr.get("label", null);

		// URL オブジェクトの生成
		if(page instanceof String){
			url = new UniformResourceLocator((String) page);	// 画面遷移用
		}
		else{
			url = new UniformResourceLocator();					// リロード用
		}
		
		// ラベルの設定
		if(label instanceof String){
			url.setLabel((String) label);
		}

		// リクエスト時起動関数設定の確認
		try{
			if(action instanceof String){
				url.setAction((String) action); 
			}
		}
		catch(IllegalArgumentException iae){
			throw new TagRuntimeException(this.getTagName(), iae);
		}

		// タグ属性とＵＲＬ引数の設定
		oAttr.delete("type");
		oAttr.delete("page");
		oAttr.delete("action");
		oAttr.delete("label");

		Object[] params = oAttr.getIds();
		for(int idx = 0; idx < params.length; idx++){
			
			Object value = oAttr.get((String) params[idx], null);
			
			if(value != null){
				if(value instanceof String){
					tag.setAttribute((String) params[idx], value);
					if(! tagAttributeNames4link.contains(((String) value).toLowerCase())){
						url.setArgument((String) params[idx], (String) value);
					}
				}
				else if(value instanceof Number){
					url.setArgument((String) params[idx], ScriptRuntime.toString(value));
				}
				else if(value instanceof Boolean){
					tag.setAttribute((String) params[idx], value);
				}
			}
			
		}

		try{
			// タグ基本属性の設定
			tag.setAttribute("href", url.locationWithSession());
		}
		catch(UnsupportedEncodingException e){
			throw new TagRuntimeException(this.getTagName(), e);
		}

		return tag.getBegin() + ((InnerTextObject) oInner).execute() + tag.getTerminate();
	}

}
