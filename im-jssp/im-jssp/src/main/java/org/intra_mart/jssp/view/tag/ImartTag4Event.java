package org.intra_mart.jssp.view.tag;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * &lt;IMART type="event"&gt; タグ。<br/>
 * <br/>
 * ページに対して、ブラウザ上で動作するイベント処理型ロジックを追加します。<br/>
 * このタグは、Client Side Java Script を利用して実現されています。<br/>
 * また、値のブリッジには、ブラウザのクッキーを利用しています。<br/>
 * (クッキーを利用しているので別ウィンドウ同士の通信にも利用できます)<br/>
 * そのため、クッキーの容量制限を越える量の値をやり取り(監視や受取)はできません。<br/>
 * また、このタグではクッキーを保存する際、クッキーの path 属性を 「/」として値を保存しています。<br/>
 * セキュリティ的に問題と なる場合には、この機能を利用しないで下さい。<br/>
 * <br/>
 * 属性 name には、動作する際のイベント名称(キーコード)を指定します。 
 * この name 属性に指定したコードが等しい &lt;IMART type="event"&gt; 同士が連携して動作します。<br/>
 * <br/>
 * 属性 focus には、監視対象となる Client Side Java Script の変数名称を文字列として指定して下さい。 
 * focus 属性で指定された変数の内容が送信される事になります。<br/>
 * <br/>
 * 属性 receive には、監視対象値を受け取るための Client Side Java Script の変数名称を
 * 文字列として指定して下さい。 
 * ここで指定した変数によって focus で監視している変数の値を受信する事ができます。<br/>
 * <br/>
 * 属性 init には、動作前にクッキーを初期化する必要がある場合、 
 * その初期値を文字列として指定する事ができます。<br/>
 * <br/>
 * 属性 onChange には、focus 属性で指定された変数内の値が変化した際に
 * 実行される Client Side Java Script のソースコードを文字列として指定します。<br/>
 * <br/>
 * 属性 time には、監視のタイミングをミリ秒で指定します。 
 * 属性 focus で指定された変数やクッキー内の情報は、
 * タイマー機能を利用して一定時間毎の監視を行っています。
 * デフォルトでは 250 ミリ秒毎に監視していますが、
 * 連動のタイミングをよりリアルタイムにしたい場合は、この値を小さく設定し、
 * ブラウザ処理の負荷が 高く動作が重い場合には、この値を大きく設定する事で任意に調整して下さい。<br/>
 */
public class ImartTag4Event implements ImartTagType {

	// シーケンス用クラス変数
	private volatile int counter = 0;

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.tag.ImartTagType#getTagName()
	 */
	public String getTagName() {
		return "event";
	}
	
	/**********************************************************************************
	 * &lt;IMART type="event"&gt; タグの実行メソッド。
	 * <pre>
	 *        【 入力 】oAttr : タグに対する引数情報
	 *                  oInner: タグに挟まれた部分の情報
	 *        【返却値】HTML ソース（文字列）
	 *        【 概要 】ＣＳＪＳ event 機能
	 *                  クライアントサイドクッキーを利用して値をブリッジする機能
	 *                  一定間隔で対象の変数内のデータを監視して変更が確認された
	 *                  場合、そのデータをクッキーに書き込み、受取側はクッキー
	 *                  からデータを取得。
	 *                  ブラウザ上での実行時に扱われるデータは文字列のみ。
	 *                  ＣＳＪＳにて実現されている機能であるためブラウザの制約を
	 *                  顕著に受ける。
	 *                  name    : カテゴリ名称
	 *                            このカテゴリ名称が同一の event タグ同志が連動
	 *                            （event タグ同志はこの属性のみで関連付け）
	 *                  focus   : 監視対象ＣＳＪＳ変数名称
	 *                            ＣＳＪＳ変数名を文字列として指定
	 *                  receive : 監視変数内データの受け入れＣＳＪＳ変数名称
	 *                            ＣＳＪＳ変数名を文字列として指定
	 *                  init    : クッキー内初期値
	 *                            初期値を指定
	 *                  onChange: データ変更時の実行スクリプト
	 *                            監視対象変数内データが変更された場合に実行
	 *                            されるＣＳＪＳスクリプト
	 *                            実行されるＣＳＪＳスクリプトを文字列として指定
	 *                  time    : クッキー監視間隔時間（ミリ秒）
	 *                            無指定の場合のデフォルト値は 250 ミリ秒
	 * </pre>
	 ***********************************************************************************/
	public String doTag(Scriptable oAttr, Scriptable oInner){
		
		String id = Integer.toString((counter = ++counter & 0xff), 16);	// 識別
		String name = ScriptRuntime.toString(oAttr.get("name", null));	// 名称
		
		// 関連付けするクライアントサイド変数(クッキー → バインド変数)
		boolean bOutput = oAttr.has("receive", null);
		String sOutput = ScriptRuntime.toString(oAttr.get("receive", null));
		
		// 関連付けするクライアントサイド変数(バインド変数 → クッキー)
		boolean bInput = oAttr.has("focus", null);
		String sInput = ScriptRuntime.toString(oAttr.get("focus", null));
		
		// 初期化
		boolean bInit = oAttr.has("init", null);
		String sInit = ScriptRuntime.toString(oAttr.get("init", null));
		
		// 監視間隔（ミリ秒）
		String sleep_time = "250";		// デフォルト値
		
		// 変更確認時の実行スクリプト
		boolean bScript = oAttr.has("onChange", null);
		String sScript = ScriptRuntime.toString(oAttr.get("onChange", null));

		// 関数および変数名称
		String nameTimerFnc = "imChkCookie" + id + "()";
		String nameID = "imTimerID" + id;
		String nameDef = "imDefault" + id;
		String nameFocus = "imFocus" + id;

		StringBuffer buf = new StringBuffer();		// ソース作成領域

		// 監視間隔の調整
		if(oAttr.has("time", null)){
			sleep_time = Integer.toString(ScriptRuntime.toInt32(oAttr.get("time", null)));
		}

		// ソース作成
		buf.append("<SCRIPT language=\"JavaScript\">\n");

			// タイマー処理関数
			buf.append("function " + nameTimerFnc + "{");
			if(bScript || bOutput){
				buf.append("var evtValue = imGetCookie(\"" + name + "\");");
			}
			// バインド変数からの値の受けとり
			if(bInput){
				buf.append("if(" + nameFocus + "!=" + sInput + "){");
				buf.append("imSetCookie(\"" + name + "\", " + sInput + ");");
				buf.append(nameFocus + "=" + sInput + ";}");
			}
			// バインド変数への値の受け渡し
			if(bOutput){
				buf.append(sOutput + "= evtValue;");
			}
			// イベント・スクリプトの起動
			if(bScript){
				buf.append("if(" + nameDef + " != evtValue){");
				buf.append(nameDef + " = evtValue;\n");
				buf.append(sScript);
				buf.append("\n}");
			}
			// タイマーのクリア
			buf.append("clearTimeout(" + nameID + ");");
			// タイマー設定
			buf.append(nameID);
			buf.append(" = setTimeout(\"" + nameTimerFnc + "\", " + sleep_time + ");");
			// タイマー処理関数の終了
			buf.append("}\n");

			// クッキー設定関数
			if(bInput || bInit){
				buf.append("function imSetCookie(sName, sValue){");
				buf.append("document.cookie = ");
				buf.append("sName + \"=\" + escape(sValue) + \"; path=/\";");
				buf.append("}\n");
			}

			// クッキー取得関数
			if(bScript || bOutput){
				buf.append("function imGetCookie(sName){");
				buf.append("var sCookie = new String(document.cookie);");
				buf.append("var nStart = sCookie.indexOf(sName + \"=\");");
				// 指定参照名で検索失敗
				buf.append("if(nStart == -1){ return \"\"; }");
				buf.append("nStart = sCookie.indexOf(\"=\", nStart) + 1;");
				// 指定参照名で検索失敗
				buf.append("if(nStart == -1){ return \"\"; }");
				buf.append("var nEnd = ");
				buf.append("sCookie.indexOf(\";\", nStart);");
				// 値の終了地点の修正
				buf.append("if(nEnd == -1){ nEnd = sCookie.length; }");
				buf.append("return unescape(sCookie.substring(nStart, nEnd));");
				buf.append("}\n");
			}

			// グローバル領域変数の設定
			if(bInit){
				// クッキーへの初期値の設定
				buf.append("imSetCookie(\"" + name + "\", \"" + sInit + "\");\n");
			}
			if(bInput){
				if(! bInit){
					// バインド変数からの値の受けとり
					buf.append("imSetCookie(\"" + name + "\", " + sInput + ");\n");
				}
				// 参照初期値の取得
				buf.append(nameFocus + "=" + sInput + ";");
			}
			if(bScript){
				// クッキー内初期値取得
				buf.append("var " + nameDef + " = imGetCookie(\"" + name + "\");\n");
			}

			// 割り込みタイマー用ハンドル変数
			buf.append("var " + nameID + " = setTimeout(\"" + nameTimerFnc + "\", 100);\n");

		// ソース作成終了
		buf.append("</SCRIPT>");

		return buf.toString();
	}

}
