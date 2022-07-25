package org.intra_mart.common.aid.jdk.util.report;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * メッセージのファイル出力機能を提供します。<p>
 * このクラスは、report メソッドで渡されたメッセージを指定のファイルに
 * 出力します。
 *
 */
public class FileReporter extends AbstractMessageReporter{
	/**
	 * 基礎出力ストリームです。
	 */
	protected ReportFileWriter out;
	/**
	 * ローテイト条件リスト
	 */
	private ReportFileConditioner[] conditioners = new ReportFileConditioner[0];
	/**
	 * 指定された基礎出力ストリームにメッセージを書き込む
	 * Reporter オブジェクトを作成します。
	 * @param out 基礎出力ストリーム
	 */
	public FileReporter(ReportFileWriter out){
		super();
		this.out = out;
	}

	/**
	 * ログファイルのローテイト機能などを実装するためのチェッカーオブジェクトの
	 * 追加登録。<br>
	 * 引数が null の場合は、何の効果もありません。
	 * また、conditioner が既に登録済みのインスタンスである場合、
	 * 追加登録はされません。
	 *
	 * @param conditioner 追加する ReportFileConditioner のインスタンス
	 */
	public synchronized void addConditioner(ReportFileConditioner conditioner){
		// 引数の null チェック
		if(conditioner != null){
			// null ではないからコンディショナの追加
			Set list = new HashSet();
			ReportFileConditioner[] now = this.conditioners;

			// 現在のリスト情報を反映
			for(int idx = 0; idx < now.length; idx++){ list.add(now[idx]); }
			list.add(conditioner);		// 新規追加

			// 設定として反映
			this.conditioners = (ReportFileConditioner[]) list.toArray(new ReportFileConditioner[list.size()]);
		}
	}

	/**
	 * ログファイルのローテイト機能などを実装するためのチェッカーオブジェクトの
	 * 削除。<br>
	 * 引数が null の場合は、何の効果もありません。
	 *
	 * @param conditioner 削除する ReportFileConditioner のインスタンス
	 * @return 指定のオブジェクトを削除できた場合 true。
	 */
	public synchronized boolean removeConditioner(ReportFileConditioner conditioner){
		// 引数の null チェック
		if(conditioner != null){
			// null ではないからコンディショナの追加
			Set list = new HashSet();
			ReportFileConditioner[] now = this.conditioners;

			// 現在のリスト情報を反映
			for(int idx = 0; idx < now.length; idx++){ list.add(now[idx]); }
			if(list.remove(conditioner)){
				// 削除成功なので設定として反映
				this.conditioners = (ReportFileConditioner[]) list.toArray(new ReportFileConditioner[list.size()]);
				return true;
			}
		}

		return false;		// 削除はされず
	}

	/**
	 * メッセージを出力します。
	 * このオブジェクトが既に閉じられている場合、このメソッドは
	 * 何も行いません。
	 * @param message メッセージ
	 */
	public synchronized void report(String message){
		if(out != null){
			// ローテイト条件のチェック
			ReportFileConditioner[] list = this.conditioners;
			boolean rotative = false;
			for(int idx = list.length - 1; idx >= 0; --idx){
				rotative |= list[idx].expiration(this.out);
			}
			if(rotative){
				try{
					// ローテイトするゾ(出力ストリームの再構築)
					this.out.reconstruction();
				}
				catch(IOException ioe){
					// 再構築に失敗
					System.err.println("Reconstruction error report file.");
					ioe.printStackTrace();
					
				}
			}

			// メッセージの出力
			if(message != null){
				try{
					this.out.write(message);
				}
				catch(IOException ioe){
					// 出力エラー
					System.err.println("Report error.");
					ioe.printStackTrace();

				}
			}
		}
	}

	/**
	 * このオブジェクトを閉じます。
	 * このオブジェクトに関わる全てのリソースを開放します。
	 * オブジェクトが閉じられると、report メソッドは何もしなくなります。
	 * @throws IOException 入出力エラー
	 */
	public synchronized void close() throws IOException{
		if(out != null){
			try{
				this.out.close();
			}
			finally{
				this.out = null;
			}
		}
	}

	/**
	 * このオブジェクトのバッファの情報をすべて出力します。
	 * また、このメソッドは基礎ストリームも同時にフラッシュします。
	 * @throws IOException 入出力エラー
	 */
	public synchronized void flush() throws IOException{
		if(out != null){
			this.out.flush();
		}
		else{
			throw new IOException(this.getClass().getName().concat(" is closed"));
		}
	}

	/**
	 * このオブジェクトがガーベージコレクションによって破棄される時に、
	 * ガーベージコレクタによって呼び出されます。<br>
	 * このオブジェクトの全てのリソースを破棄します。
	 */
	protected void finalize() throws Throwable{
		this.close();
	}
}
