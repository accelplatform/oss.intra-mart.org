package org.intra_mart.common.aid.jdk.java.lang;

import java.io.OutputStream;
import java.io.PrintStream;

import org.intra_mart.common.aid.jdk.java.io.MultipleOutputStream;


/**
 * このクラスは、エラーメッセージ出力用です。
 *
 */
public class StdErr{
	/**
	 * クラスロード時点での標準エラー出力への出力ストリーム
	 */
	private static PrintStream systemErr = System.err;
	/**
	 * 出力先を保持する出力ストリーム
	 */
	private static MultipleOutputStream streams = null;
	/**
	 * メッセージの出力ストリーム
	 */
	private static PrintStream printStream = null;
	/**
	 * 例外の出力リスナ
	 */
	private static ThrowablePrintListener throwablePrinter = null;

	/**
	 * クラス初期化子。
	 */
	static{
		// 出力ストリームの作成
		StdErr.streams = new MultipleOutputStream();

		// デフォルトの出力先として標準エラー出力を設定
		StdErr.addOutputStream(StdErr.systemErr);

		// 標準の例外出力リスナを設定
		ThrowablePrintListener listener = new ThrowablePrintListenerImpl(StdErr.systemErr);
		StdErr.setThrowablePrintListener(listener);

		// ＶＭへの再割り当て
		PrintStream out = new PrintStream(StdErr.streams, true);
		StdErr.setPrintStream(out);
	}

	/**
	 * このクラスがロードされた時点の、{@link java.lang.System#err} を
	 * 返します。<p>
	 * このメソッドのリターン値を引数にして、
	 * {@link #removeOutputStream(OutputStream)} を実行すると、
	 * 標準エラー出力（コンソール）にメッセージが表示されなくなります。
	 * @return 標準エラー出力にメッセージを書き出す出力ストリーム
	 */
	public static PrintStream getDefaultPrintStream(){
		return StdErr.systemErr;
	}

	/**
	 * メッセージを出力する出力ストリームを返します。<p>
	 * 返された出力ストリームの {@link java.io.PrintStream#close()} メソッドを
	 * 実行してストリームを閉じてしまうと、以後メッセージを出力できなく
	 * なります。
	 * @return 出力ストリーム
	 */
	public static PrintStream getPrintStream(){
		return StdErr.printStream;
	}

	/**
	 * メッセージを出力する出力ストリームを割り当て直します。<p>
	 * @return 出力ストリーム
	 */
	private static void setPrintStream(PrintStream out){
		StdErr.printStream = out;
		System.setErr(StdErr.printStream);
	}

	/**
	 * メッセージの出力先を追加します。
	 * @param out 出力ストリーム
	 */
	public static synchronized void addOutputStream(OutputStream out){
		StdErr.streams.addOutputStream(out);
	}

	/**
	 * メッセージの出力先を削除します。
	 * @param out 出力ストリーム
	 * @return 削除に成功した場合は true。
	 */
	public static synchronized boolean removeOutputStream(OutputStream out){
		return StdErr.streams.removeOutputStream(out);
	}

	/**
	 * 指定の例外出力リスナを割り当て直します。
	 * @param listener 新しい例外出力リスナ
	 */
	public static synchronized void setThrowablePrintListener(ThrowablePrintListener listener){
		if(listener == null){
			throw new NullPointerException("listener is null.");
		}
		else{
			StdErr.throwablePrinter = listener;
		}
	}

	/**
	 * 現在の例外出力リスナを返します。
	 * @return 現在有効な例外出力リスナ
	 */
	public static synchronized ThrowablePrintListener getThrowablePrintListener(){
		return StdErr.throwablePrinter;
	}

	/**
	 * 指定のメッセージを標準エラー出力および指定の出力先に書き出します。
	 *
	 * @param msg メッセージ
	 */
	public static synchronized void write(String msg){
		try{
			StdErr.printStream.println(msg);
		}
		catch(Throwable t){
			StdErr.systemErr.println("Standard-error-output write error: " + msg);
			t.printStackTrace(StdErr.systemErr);
		}
	}

	/**
	 * 指定のメッセージを標準エラー出力および指定の出力先に書き出します。
	 *
	 * @param msg メッセージ
	 * @param exception 例外
	 */
	public static synchronized void write(String msg, Throwable exception){
		try{
			StdErr.write(msg);
		}
		finally{
			if(exception != null){
				try{
					StdErr.throwablePrinter.printMessage(exception);
					StdErr.throwablePrinter.printStackTrace(exception);
				}
				catch(Throwable t){
					try{
						exception.printStackTrace(StdErr.systemErr);
					}
					finally{
						t.printStackTrace(StdErr.systemErr);
					}
				}
			}
		}
	}


	/**
	 * 唯一のコンストラクタ。
	 * 誤ってインスタンス化されないように隠蔽化されています。
	 */
	private StdErr(){
		super();
	}


	/**
	 * 例外を出力するための標準実装です。
	 */
	private static class ThrowablePrintListenerImpl implements ThrowablePrintListener{
		private PrintStream out = null;

		/**
		 * コンストラクタ
		 * @param out メッセージを書き出す出力ストリーム
		 */
		protected ThrowablePrintListenerImpl(PrintStream out){
			super();
			this.out = out;
		}

		/**
		 * 例外のメッセージを出力します。
		 * @param t 例外
		 */
		public void printMessage(Throwable t){
//			this.out.println(t.getMessage());
		}

		/**
		 * 例外のスタックトレースを出力します。
		 * @param t 例外
		 */
		public void printStackTrace(Throwable t){
			t.printStackTrace(this.out);
		}
	}
}


