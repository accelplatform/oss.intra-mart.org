package org.intra_mart.jssp.view.tag;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;

import org.intra_mart.jssp.exception.TagRuntimeException;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="frame"&gt; タグ。<br/>
 * <br/>
 * フレームタグ(&lt;FRAME&gt;)を生成します。<br/>
 * このタグを利用する事でJSSP実行環境のセッションを維持しながらプログラムを動作させる事が出来ます。<br/>
 * <br/>
 * 属性 page に対して次に表示させたいページパス（拡張子なし）を指定することで、ページを表示させることができます。<br/>
 * ここで指定するパスは、JSSPコンテンツのソースディレクトリからの相対パス形式になります。（デフォルトはコンテキストパスに対応する実際のパス）<br/>
 * <br/>
 * 属性 label は、表示ページ内の任意のアンカータグ(&lt;A name="*"&gt;)の位置を初期表示位置として指定する事ができます。<br/>
 * この指定はブラウザの機能を利用しているため、ブラウザ製品により期待通りに動作しない場合があります。<br/>
 * <br/>
 * 属性 action に関数名称を指定すると、この&lt;IMART type="frame"&gt; タグを記述したページのファンクション・コンテナ内に定義されている関数をリクエスト時に起動します。<br/>
 * 属性 action で指定された関数は、表示ページの作成処理(該当ページのファンクション・コンテナ内 init() 関数)よりも先に動作します。<br/>
 * <br/>
 * その他、表示ページ(属性 page に指定しているプログラム)に対して引き渡すＵＲＬ引数を、任意の個数だけ指定可能です。<br/>
 * 属性 page に指定されたページのファンクション・コンテナ内 init() 関数、または、属性 action で指定された関数では、<br/>
 * 指定されたＵＲＬ引数を関数引数 request で受け取る事ができます。<br/>
 * その際、属性名称が引数 request オブジェクトのプロパティ名となり、属性値が引数 request オブジェクトの該当プロパティに格納されます。<br/>
 * <br/>
 * また &lt;FRAME&gt; タグに指定可能なオプション属性を任意に指定する事が出来ます。<br/>
 * &lt;FRAME&gt; タグの動作仕様に関しては、市販の HTML リファレンスを参照してください。<br/>
 */
public class ImartTag4Frame implements ImartTagType {

	private static Collection<String> tagAttributeNames4frame = new HashSet<String>();

	static{
		tagAttributeNames4frame.add("name");
		tagAttributeNames4frame.add("scrolling");
		tagAttributeNames4frame.add("marginwidth");
		tagAttributeNames4frame.add("marginheigh");
		tagAttributeNames4frame.add("noresize");
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "frame";
	}
	
	
	/**************************************************************************
	 * &lt;IMART type="frame"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】フレームタグ(&lt;FRAME&gt;)
	 *                  page  : 表示ページパス
	 *                  label : リンクラベル
	 *                  action: コール時起動関数(同ページ js 内)
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {

		TagObject tag = new TagObject("FRAME");		// フレームタグ
		
		UniformResourceLocator url;
		Object page = oAttr.get("page", null);
		Object label = oAttr.get("label", null);
		Object action = oAttr.get("action", null);

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
		oAttr.delete("label");
		oAttr.delete("action");

		Object[] params = oAttr.getIds();
		for(int idx = 0; idx < params.length; idx++){
			
			Object value = oAttr.get((String) params[idx], null);
			
			if(value != null){
				if(value instanceof String){
					tag.setAttribute((String) params[idx], value);
					if(! tagAttributeNames4frame.contains(((String) value).toLowerCase())){
						url.setArgument((String) params[idx], (String) value);
					}
				}
				else if(value instanceof Number){
//					tag.setAttribute((String) params[idx], value);
					url.setArgument((String) params[idx], Double.toString(((Number) value).doubleValue()));
				}
				else if(value instanceof Boolean){
					tag.setAttribute((String) params[idx], value);
				}
			}
			
		}

		try{
			// タグ基本属性の設定
			if(page instanceof String){
				tag.setAttribute("src", url.locationWithSession());
			}
		}
		catch(UnsupportedEncodingException e){
			throw new TagRuntimeException(this.getTagName(), e);
		}

		return tag.getBegin();
	}
	
}
