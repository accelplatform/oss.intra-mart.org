package org.intra_mart.jssp.view;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.intra_mart.jssp.view.tag.ImartTagAnalyzer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

/**
 * HTMLソースを解析します.
 *
 */
public class ViewScope implements Serializable {

	/** 空ViewScope */
	public static final ViewScope EMPTY = new ViewScope.NullViewScope();

	// TAG_NAMEを修正する事により、テンプレート上に記述するタグ名を変更する事が可能です.
	// タグ名は大文字, 小文字に対応しています、TAG_NAMEに定義するタグ名は小文字で指定してください.
	// <IMART type="... /> -> <EXTRA type="... />
	/** タグ名 */
	public static final String TAG_NAME = "imart";

	/** 構成要素群 */
	private final Collection<Object> element = new ArrayList<Object>();

	/**
	 * コンストラクタ.<br />
	 * 
	 * @param in HTMLソース(Unicode)
	 */
	public ViewScope(final Reader in){
		super();	// スーパークラスのコンストラクタの呼び出し
		analyze(in);	// ソース解析
	}

	/**
	 * HTMLソースの解析を行います.<br />
	 * 解析結果イメージ(順序はHTMLソースに依存します.)<br/ >
	 * <pre>
	 * ViewScope
	 *  ├ char[]..: IMARTタグ以外のソース部分
	 *  └ ImartTagAnalyzer..: IMARTタグ部分
	 *      └ inner@ViewScope..: IMARTタグ内部のソース部分
	 *          ├ char[]..: IMARTタグ以外のソース部分
	 *          └ ImartTagAnalyzer..: IMARTタグ部分...
	 * </pre>
	 * 
	 * @param in HTMLソース.
	 */
	protected void analyze(final Reader in){
		final CharArrayWriter token = new CharArrayWriter();		

		int chr;

		try{
			// トークン解析
			while((chr = in.read()) != -1){
				if(chr == (int)'<'){
					if(isStartTag(in)){
						// <IMART> タグ発見！！
						element.add(token.toCharArray());
						token.reset();
						element.add(new ImartTagAnalyzer(in));
					}
					else if(isEndTag(in)){
						// </IMART> タグ発見！！
						while((chr = in.read()) != -1){
							// タグの終端までスキップ
							if((char)chr == '>'){
								// ループの終了
								break;
							}
							if((char)chr == '\"'){
								// 文字列定数部分のスキップ
								while((chr = in.read()) != -1){
									if((char)chr == '\"'){ break; }
									if((char)chr == '\\'){ chr = in.read(); }
								}
							}
						}
						// 検索ループの終了: </IMART>以降のトークンは親のImartTagAnalyzer/ViewScopeが保持
						break;
					}
					else{
						// 通常処理
						token.write((char)chr);
					}
				}
				else{
					// 通常処理
					token.write((char)chr);
				}
			}	// End of While
		}
		catch(final IOException ioe){
		}
		if(token.size() > 0){
			// トークンが存在すれば、それを格納
			element.add(token.toCharArray());
		}
	}

	/**
	 * IMART開始タグであるか判定を行います.<br />
	 * このメソッドは引数inのpositionを変更しません.
	 * 
	 * @param in HTMLソース
	 * @return 判定結果 true: 開始タグ/false: 開始タグではない
	 */
	private boolean isStartTag(final Reader in){
		final StringBuilder tag = new StringBuilder();
		char chr;
		boolean bRes = false;

		final int LENGTH = TAG_NAME.length();
		
		try{
			in.mark(LENGTH);	// 現在位置にマーキング
			// 文字列の抽出 I M A R T
			for(int i = 0; i < LENGTH; i++) {
				tag.append((char)in.read());
			}

			chr = (char)in.read();

			// チェック
			bRes = tag.toString().toLowerCase().equals(TAG_NAME) && Character.isWhitespace(chr);
		}
		catch(final IOException ioe){
			// 何もしない
		}
		finally{
			try{
				in.reset();
			}
			catch(final IOException ioe){
				// 何もしない
			}
		}

		// 結果に返却
		return bRes;
	}

	/**
	 * IMART終了タグであるか判定を行います.<br />
	 * このメソッドは引数inのpositionを変更しません.
	 * 
	 * @param in HTMLソース
	 * @return 判定結果 true: 終了タグ/false: 終了タグではない
	 */
	private boolean isEndTag(final Reader in){

		final StringBuilder tag = new StringBuilder();
		char chr;
		boolean bRes = false;

		final String END_TAG_NAME = "/" + TAG_NAME;
		final int LENGTH = END_TAG_NAME.length(); 
		
		try{
			in.mark(LENGTH);	// 現在位置にマーキング
			// 文字列の抽出
			for(int i = 0; i < LENGTH; i++) {
				// / I M A R T
				tag.append((char)in.read());
			}

			chr = (char)in.read();

			// チェック "/IMART " or "/IMART>" 
			bRes = tag.toString().toLowerCase().equals(END_TAG_NAME) && (Character.isWhitespace(chr) || chr == '>');
		}
		catch(final IOException ioe){
			// 何もしない
		}
		finally{
			try{
				in.reset();
			}
			catch(final IOException ioe){
				// 何もしない
			}
		}

		// 結果に返却
		return bRes;
	}

	/**
	 * ＨＴＭＬソースの生成。
	 * 
	 * @param cx ＪＳ実行環境
	 * @param scope ＪＳ実行時変数スコープ
	 * @return 行結果（クライアントへの返却ソース）
	 * @throws JavaScriptException
	 * @throws IOException
	 */
	public String execute(final Context cx, final Scriptable scope) throws JavaScriptException, IOException{
		final Iterator iterator = element.iterator();	// 要素の抽出(データ)
		final CharArrayWriter out = new CharArrayWriter();

		while(iterator.hasNext()){
			final Object data = iterator.next();

			if(data instanceof ImartTagAnalyzer){
				// <IMART> タグ実行
				out.write(((ImartTagAnalyzer) data).execute(cx, scope));
			}
			else{
				// 通常ソース部分
				out.write((char[]) data);
			}
		}

		// ソースの返却
		return out.toString();
	}

	/**
	 * デバッグ用に解析結果をSystem.outへ出力します.
	 * 
	 */
	public void print(){
		final Iterator iterator = element.iterator();
		Object data;
		java.lang.System.out.print("[element:" + element.size() + "]");

		while(iterator.hasNext()){
			try{
				data = iterator.next();
				// セパレータ出力
				System.out.println("[" + hashCode() + "][" + data.hashCode() + "]-------------------------------------------------");
				if(data instanceof char[]) {
					System.out.print(new String((char[]) data));
				} else if(data instanceof ImartTagAnalyzer) {
					((ImartTagAnalyzer) data).print();
				}
			}
			catch(final NoSuchElementException nsee){
				// nothing
			}
		}
	}

	/**
	 * 何も行わないViewScopeです.
	 *
	 */
	private static class NullViewScope extends ViewScope {

		/**
		 * コンストラクタ.
		 */
		private NullViewScope() {
			super(null);
		}

		/**
		 * このメソッドは何も行いません.
		 *
		 * @param in ソース
		 * @see org.intra_mart.jssp.view.ViewScope#analyze(java.io.Reader)
		 */
		@Override
		protected void analyze(final Reader in) {
			// do not nothing.
		}

		/**
		 * このメソッドは必ず空文字を返却します.
		 *
		 * @param cx Context.
		 * @param scope Scope.
		 * @see org.intra_mart.jssp.view.ViewScope#execute(org.mozilla.javascript.Context, org.mozilla.javascript.Scriptable)
		 */
		@Override
		public String execute(final Context cx, final Scriptable scope) throws JavaScriptException, IOException {
			return "";
		}

		/*
		 * @see org.intra_mart.jssp.view.ViewScope#print()
		 */
		@Override
		public void print() {
			System.out.println("-- NullViewScope --");
		}
	}
}
