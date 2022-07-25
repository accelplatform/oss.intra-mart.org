package org.intra_mart.jssp.view.tag;

import org.intra_mart.jssp.exception.TagRuntimeException;
import org.intra_mart.jssp.page.JSSPQuery;
import org.intra_mart.jssp.page.JSSPQueryManager;
import org.intra_mart.jssp.util.BASE64;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="submit"&gt; タグ。<br/>
 * <br/>
 * サブミットボタンを表示します。<br/>
 * 属性 page に対して次に表示させたいページパス（拡張子なし）を指定することで、
 * ページを表示させることができます。<br/>
 * <br/>
 * ここで指定するパスは、JSSPコンテンツのソースディレクトリからの相対パス形式になります。（デフォルトはコンテキストパスに対応する実際のパス）<br/>
 * <br/>
 * また、&lt;IMART type="form"&gt; にて page 属性が指定されていて遷移ページ先の指定要求が重複した場合、
 * &lt;IMART type="submit"&gt; で指定した page 指定が 優先され、
 * &lt;IMART type="form"&gt; で指定した page 指定は無視されます。<br/>
 * <br/>
 * 属性 action は、この&lt;IMART type="submit"&gt; タグを表示したページの
 * ファンクション・コンテナ内に定義されている関数をリクエスト時に起動するための指定で、
 * 起動させたい関数名称を指定する事で可能になります。<br/>
 * action 関数の実行は、
 * 遷移先ページの作成処理(該当ページのファンクション・コンテナ内 init() 関数)よりも先に動作します。<br/>
 * <br/>
 * また、&lt;IMART type="form"&gt; にて action 属性が指定されていて関数実行要求が重複した場合、
 * &lt;IMART type="submit"&gt; で指定した action 関数が優先的に 実行され、
 * &lt;IMART type="form"&gt; で指定した action 関数の実行要求は無視されます。<br/>
 * <br/>
 * この &lt;IMART type="submit"&gt; は、１つのフォーム内に複数の配置が可能であり、 
 * その際に指定した page および action 指定は、クリックされたサブミットボタンに対して個々に指定可能で、
 * 独立した動作をします。<br/>
 * <br/>
 * なお、このタグは HTML の &lt;INPUT type="submit"&gt; の name 属性を利用して制御を行っています。
 * したがって、任意属性として name を記述した場合、 画面遷移や関数実行指定等が正常に動作しなくなります。<br/>
 * <br/> 
 * <!--
 * *****************************************************************************
 * -->
 * <table border="1">
 * 	<tr>
 * 		<th colspan="2">&lt;IMART type="<font color="blue">form</font>"&gt; タグ</th>
 * 		<th colspan="2">&lt;IMART type="<font color="red">submit</font>"&gt; タグ</th>
 * 		<th rowspan="2">結果</th>
 * 	</tr>
 * 	<tr>
 * 		<th>属性 action</th>
 * 		<th>属性 page</th>
 * 		<th>属性 action</th>
 * 		<th>属性 page</th>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			&lt;IMART type="<font color="blue">form</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="blue">form</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="blue">form</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			&lt;IMART type="<font color="blue">form</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			この&lt;IMART type="submit"&gt; タグを表示したページを再表示します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="blue">form</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="blue">form</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 		この&lt;IMART type="submit"&gt; タグを表示したページを再表示します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			&lt;IMART type="<font color="blue">form</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="blue">form</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td><i>指定なし</i></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 action に指定された関数を実行後、<br/>
 * 			この&lt;IMART type="submit"&gt; タグを表示したページを再表示します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><b><u>指定あり</u></b></td>
 * 		<td align="left">
 * 			&lt;IMART type="<font color="red">submit</font>"&gt;の属性 page に指定されたページに遷移します。
 * 		</td>
 * 	</tr>
 * 	<tr align="center">
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td><i>指定なし</i></td>
 * 		<td align="left">
 * 			この&lt;IMART type="submit"&gt; タグを表示したページを再表示します。
 * 		</td>
 * 	</tr>
 * </table>
 * <!--
 * *****************************************************************************
 * -->
 */
public class ImartTag4Submit implements ImartTagType {

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "submit";
	}

	/**************************************************************************
	 * &lt;IMART type="submit"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】&lt;INPUT type="submit"&gt; タグ
	 *                  page  : リクエスト後の遷移先ページパス
	 *                  action: リクエスト時の起動関数名
	 *                  その他: タグ属性と属性値
	 * </pre>
	 **************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner) {
		
		String currentPath = JSSPQueryManager.currentJSSPQuery().getNextEventPagePath();		
		if(currentPath == null){
			NullPointerException npe = new NullPointerException("current page path is null.");
			throw new TagRuntimeException(this.getTagName(), npe);			
		}		

		boolean hasQuery = false;
		JSSPQuery jsspQuery = JSSPQueryManager.createJSSPQuery();
		TagObject submitTag = new TagObject("INPUT");

		// コントロールタイプの設定
		submitTag.setAttribute("type", "submit");

		// ページ遷移指定の確認
		// URL オブジェクトの生成
		if(oAttr.has("page", null)){
			// 画面遷移用
			hasQuery = true;
			jsspQuery.setNextEventPagePath(ScriptRuntime.toString(oAttr.get("page", null)));
			oAttr.delete("page");
		}
		
		// リクエスト時コール関数指定の確認
		if(oAttr.has("action", null)){
			hasQuery = true;
			try{
				jsspQuery.setActionEventName(ScriptRuntime.toString(oAttr.get("action", null)));
			}
			catch(IllegalArgumentException iae){
				throw new TagRuntimeException(this.getTagName(), iae);
			}
			oAttr.delete("action");
		}

		// コントロール名称の決定
		if(hasQuery){
			jsspQuery.setFromPagePath(currentPath);
			String param = jsspQuery.createJSSPQueryString();
			submitTag.setAttribute("name", "imsubmit_" + BASE64.encode(param.getBytes()));
		}

		// その他任意の属性指定
		oAttr.delete("type");
		Object[] params = oAttr.getIds();
		for(int idx = 0; idx < params.length; idx++){
			
			Object value = oAttr.get((String) params[idx], null);
			
			if( (value instanceof String) 
			    ||
			    (value instanceof Number)
			    ||
			    (value instanceof Boolean) ) {
				
				submitTag.setAttribute((String) params[idx], value);
				
			}
		}

		return submitTag.getBegin();
	}

}
