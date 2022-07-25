package org.intra_mart.common.aid.jdk.util.report;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * メッセージを複数のストリームに出力するためのフィルタークラスです。<p>
 * このクラスは、MessageReporter インターフェースを実装したクラスに対する
 * フィルタークラスとして働きます。
 * report メソッドに渡されたメッセージデータは、指定された複数の
 * MessageReporter インターフェースすべてに渡されます。<br>
 * このフィルタークラスを利用することにより、複数の MessageReporter に
 * 対して同じメッセージを渡すことができるため、メッセージ出力プログラムを
 * 効率的に記述することができます。<p>
 * メッセージの出力先となる MessageReporter は、addReporter メソッドで
 * 定義します。addReporter メソッドでの定義順と、実際の実行順は
 * 保証されません。
 *
 */
public class MultiCastReporter extends AbstractMessageReporter{
	/**
	 *
	 */
	private Set REPORTERS = new HashSet();

	/**
	 * 唯一のコンストラクタ。
	 */
	public MultiCastReporter(){
		super();
	}

	/**
	 * 指定のリポーターオブジェクトをこのオブジェクトの出力対象に追加します。
	 * すでに指定のオブジェクトを要素としてもっていた場合、
	 * このメソッドは何も行いません。
	 * @param reporter セットに追加される要素
	 */
	public synchronized void addReporter(MessageReporter reporter){
		if(this.REPORTERS != null){
			this.REPORTERS.add(reporter);
		}
	}

	/**
	 * 指定のオブジェクトをこのオブジェクトの要素として持っているかどうか
	 * 判定します。
	 * @param reporter セットに含まれているかチェックする要素
	 * @return セット内に指定された要素がある場合は true
	 */
	public boolean hasReporter(MessageReporter reporter){
		if(this.REPORTERS != null){
			return this.REPORTERS.contains(reporter);
		}
		else{
			return false;
		}
	}

	/**
	 * 指定のリポーターオブジェクトをこのオブジェクトの出力対象から
	 * 削除します。
	 * @param reporter セットにあった場合に削除されるオブジェクト
	 * @return 指定された要素がセット内にあった場合は true
	 */
	public synchronized boolean removeReporter(MessageReporter reporter){
		if(this.REPORTERS != null){
			return this.REPORTERS.remove(reporter);
		}
		else{
			return false;
		}
	}

	/**
	 * セット内の要素についての反復子を返します。
	 * 反復子から取得される各要素は、addReporter メソッドにより追加された
	 * リポーターオブジェクトになります。
	 * 各要素の取得順は保証されません。
	 * このオブジェクトが閉じられている場合 null を返します。
	 * @return セット内の要素についての反復子
	 */
	public Iterator getReporters(){
		if(this.REPORTERS != null){
			return this.REPORTERS.iterator();
		}
		else{
			return null;
		}
	}

	/**
	 * メッセージを出力します。
	 * @param message メッセージ
	 */
	public synchronized void report(String message){
		if(this.REPORTERS != null){
			Iterator cursor = this.REPORTERS.iterator();
			while(cursor.hasNext()){
				((MessageReporter) cursor.next()).report(message);
			}
		}
	}

	/**
	 * data をメッセージとして出力します。<br>
	 * @param data データ
	 */
	public synchronized void report(Object data){
		if(this.REPORTERS != null){
			Iterator cursor = this.REPORTERS.iterator();
			while(cursor.hasNext()){
				((MessageReporter) cursor.next()).report(data);
			}
		}
	}

	/**
	 * このオブジェクトを閉じます。
	 * オブジェクトが閉じられると、report メソッドは何もしなくなります。
	 * このオブジェクトの持つすべての Reporter オブジェクトを閉じて、
	 * リソースを開放します。
	 * @throws IOException 入出力エラー
	 */
	public synchronized void close() throws IOException{
		if(this.REPORTERS != null){
			try{
				this.flush();
			}
			finally{
				this.REPORTERS.clear();
				this.REPORTERS = null;
			}
		}
	}

	/**
	 * このオブジェクトのバッファの情報をすべて出力します。
	 * このメソッドは、何も行いません。
	 * このオブジェクトの持つすべての Reporter オブジェクトをフラッシュします。
	 * @throws IOException 入出力エラー
	 */
	public synchronized void flush() throws IOException{
		if(this.REPORTERS != null){
			Iterator cursor = this.REPORTERS.iterator();
			while(cursor.hasNext()){
				((MessageReporter) cursor.next()).flush();
			}
		}
		else{
			throw new IOException(this.getClass().getName().concat(" is closed"));
		}
	}
}
