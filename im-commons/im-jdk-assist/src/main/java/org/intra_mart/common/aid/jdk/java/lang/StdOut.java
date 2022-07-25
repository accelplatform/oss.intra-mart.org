package org.intra_mart.common.aid.jdk.java.lang;

import java.io.OutputStream;
import java.io.PrintStream;

import org.intra_mart.common.aid.jdk.java.io.MultipleOutputStream;


/**
 * このクラスは、システムメッセージ出力用です。
 *
 */
public class StdOut{
	/**
	 * クラスロード時点の標準出力への出力ストリーム
	 */
	private static PrintStream systemOut = System.out;
	/**
	 * 出力先を保持する出力ストリーム
	 */
	private static MultipleOutputStream streams = new MultipleOutputStream();
	/**
	 * メッセージの出力ストリーム
	 */
	private static PrintStream printStream = null;

	/**
	 * クラス初期化子。
	 */
	static{
		// デフォルトの出力先として標準出力を設定
		StdOut.addOutputStream(StdOut.systemOut);

		// 出力ストリームの作成
		PrintStream out = new PrintStream(StdOut.streams, true);

		// ＶＭへの再割り当て
		StdOut.setPrintStream(out);
	}

	/**
	 * このクラスがロードされた時点の、{@link java.lang.System#out} を
	 * 返します。<p>
	 * このメソッドのリターン値を引数にして、
	 * {@link #removeOutputStream(OutputStream)} を実行すると、
	 * 標準出力（コンソール）にメッセージが表示されなくなります。
	 * @return 標準出力にメッセージを書き出す出力ストリーム
	 */
	public static PrintStream getDefaultPrintStream(){
		return StdOut.systemOut;
	}

	/**
	 * メッセージを出力する出力ストリームを返します。<p>
	 * 返された出力ストリームの {@link java.io.PrintStream#close()} メソッドを
	 * 実行してストリームを閉じてしまうと、以後メッセージを出力できなく
	 * なります。
	 * @return 出力ストリーム
	 */
	public static PrintStream getPrintStream(){
		return StdOut.printStream;
	}

	/**
	 * メッセージを出力する出力ストリームを割り当て直します。<p>
	 * @return 出力ストリーム
	 */
	private static void setPrintStream(PrintStream out){
		StdOut.printStream = out;
		System.setOut(StdOut.printStream);
	}

	/**
	 * メッセージの出力先を追加します。
	 * @param out 出力ストリーム
	 */
	public static synchronized void addOutputStream(OutputStream out){
		StdOut.streams.addOutputStream(out);
	}

	/**
	 * メッセージの出力先を削除します。
	 * @param out 出力ストリーム
	 * @return 削除に成功した場合は true。
	 */
	public static synchronized boolean removeOutputStream(OutputStream out){
		return StdOut.streams.removeOutputStream(out);
	}

	/**
	 * 指定のメッセージを標準出力および指定の出力先に書き出します。
	 *
	 * @param msg メッセージ
	 */
	public static synchronized void write(String msg){
		try{
			StdOut.printStream.println(msg);
		}
		catch(Throwable t){
			StdErr.write("Standard-output write error: " + msg, t);
		}
	}


	/**
	 * 唯一のコンストラクタ。
	 * 誤ってインスタンス化されないように隠蔽化されています。
	 */
	private StdOut(){
		super();
	}
}

