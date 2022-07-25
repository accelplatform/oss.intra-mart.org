package org.intra_mart.common.aid.jdk.util.report;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.intra_mart.common.aid.jdk.util.EventListener;
import org.intra_mart.common.aid.jdk.util.EventQueueController;


/**
 * メッセージ出力をイベント処理するためのフィルタークラスです。<p>
 * このクラスを利用することにより、メッセージの MessageReporter における
 * 処理を現在のスレッドと並列実行させることができます。
 * これにより、このクラスの report メソッドと、出力先となる MessageReporter の
 * report メソッドの実行は非同期に処理されます。<p>
 * このクラスを利用することのメリットは、メッセージの出力処理を
 * 待たなくても、現在のスレッドの実行を継続できることにあります。
 * 逆に、このクラスを利用することにより、メッセージ出力処理と現在のスレッド処理
 * がスレッドセーフであることが保証されなければなりません。<br>
 * report メソッドで指定されたメッセージはキューに蓄積され、
 * 現在のスレッドと異なるメッセージ処理専用のスレッドで
 * 基礎出力ストリームに渡されます。
 * メッセージの出力処理は、現在のスレッドと非同期ですが、
 * report メソッドの実行順とメッセージの出力順は保証されます。
 *
 */
public class RepresentativeReporter extends FilterMessageReporter{
	/**
	 * イベントキュー。
	 * すべてのログ出力は、このキューを利用します。
	 * このキューを一元的に利用することにより、キューの持つ性質である
	 * イベントの直列化という恩恵を受けることができます。
	 * つまり、すべてのログは登録順を保証されながら直列処理されます。
	 * ログ出力ロジックが並列動作することはないことから、互いに干渉を
	 * 受けることがないため、リスナーではスレッドセーフなプログラム技法を
	 * 意識することはありません。
	 * このキューが static であることが前提となって
	 * 他のクラスが構成されています。
	 */
	private EventQueueController queue;

	/**
	 * 指定された出力ストリームにメッセージを書き込む
	 * イベントストリームを作成します。
	 * @param out 基礎出力リポーター
	 */
	public RepresentativeReporter(MessageReporter out, EventQueueController queue){
		super(out);
		this.queue = queue;
	}

	/**
	 * このストリームが書き込み可能かどうかをチェックします。
	 * @return ストリームの書き込みが可能な場合 true、そうでない場合 false。
	 */
	public boolean canReport(){
		return this.queue != null;
	}

	/**
	 * メッセージを出力します。
	 * @param message メッセージ
	 */
	public synchronized void report(String message){
		if(this.canReport()){ this.entry(message); }
	}

	/**
	 * data をメッセージとして出力します。<br>
	 * @param data データ
	 */
	public synchronized void report(Object data){
		if(this.canReport()){ this.entry(data); }
	}

	/**
	 * ストリームを閉じます。<br>
	 * このメソッドは、ストリームを閉じる前に flush メソッドを実行します。
	 * オブジェクトが閉じられると、report メソッドは何もしなくなります。
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized void close() throws IOException{
		try{
			this.flush();							// キューの出力
			this.out.close();						// ストリームの閉鎖
		}
		finally{
			this.queue = null;
		}
	}

	/**
	 * ストリームをフラッシュします。<p>
	 * ストリームに蓄積されているすべてのメッセージイベントを処理します。
	 * このメソッドを実行した場合、現在のスレッドで
	 * メッセージが処理されます。また、このメソッドはすべてのメッセージ出力
	 * 処理が完了するまでブロックします。
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized void flush() throws IOException{
		if(this.canReport()){
			this.queue.flush();
			this.out.flush();
		}
		else{
			throw new IOException(this.getClass().getName().concat(" is closed"));
		}
	}

	/**
	 * イベントリスナーの登録。<br>
	 * イベントリスナーは、一旦キューに溜められます。
	 * キューに保管されたリスナーは、
	 * このコントローラにより現在実行中のスレッドとは
	 * 別のスレッドで登録順に呼び出されます。<p>
	 * 実行されたリスナーは、処理が終了すると破棄されます。<p>
	 * 通常のスレッドクラスを利用した並列処理との違いは、
	 * イベントリスナーが登録順に実行される点と、
	 * イベントリスナーの実行が直列化される点にあります。
	 *
	 * @param message メッセージデータ
	 * @throws NullPointerException listener が null の場合
	 */
	private void entry(Object message){
		if(this.canReport()){
			EventListener listener = new ReportListener(this.out, message);
			while(true){
				try{
					this.queue.entry(listener);
					break;						// 登録完了
				}
				catch(IndexOutOfBoundsException ioobe){
					// キューが飽和している
					try{
						this.queue.eventGenerating();
					}
					catch(NoSuchElementException nsee){
						// キューが空？・・・それはないハズ
					}
				}
				catch(IllegalStateException ise){
					// コントローラが破棄されている？
					this.queue = null;
					break;						// 登録せずに終了
				}
			}
		}
	}

	/**
	 * イベント起動のためのラッピングクラス
	 */
	private static class ReportListener implements EventListener{
		private MessageReporter out;
		private Object message;

		/**
		 * リポート処理をイベント実行するためのオブジェクトを作成します。
		 * @param reporter 出力先リポーターオブジェクト
		 * @param message メッセージデータ
		 */
		public ReportListener(MessageReporter reporter, Object message){
			this.out = reporter;
			this.message = message;
		}

		/**
		 * イベント実行ロジックです。
		 * 指定されたリポーターに対して指定されたメッセージを書き込みます。
		 */
		public void handleEvent(){
			this.out.report(message);
		}
	}
}
