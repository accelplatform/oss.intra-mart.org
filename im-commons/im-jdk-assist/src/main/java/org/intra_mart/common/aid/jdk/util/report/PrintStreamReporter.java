package org.intra_mart.common.aid.jdk.util.report;

import java.io.IOException;
import java.io.PrintStream;

/**
 * メッセージ出力用クラスです。<p>
 * このクラスは、指定の PrintStream に対してメッセージを出力するための
 * 機能を実装しています。
 * 例えば、このクラスのコンストラクタの引数に java.lang.System.out を指定した
 * 場合、標準出力に対してメッセージを出力することができます。<br>
 * このクラスの特徴は、report メソッドに渡されたメッセージを
 * PrintStream の println メソッドに渡します。
 * これにより、report メソッドの呼び出し毎にメッセージを改行付きで
 * 指定の PrintStream に出力することができます（１行毎に出力）。
 *
 */
public class PrintStreamReporter extends AbstractMessageReporter{
	/**
	 * 基礎出力ストリームです。
	 */
	private PrintStream out;

	/**
	 * 指定された基礎出力ストリームにメッセージを書き込む
	 * Reporter オブジェクトを作成します。
	 * @param out 基礎出力ストリーム
	 */
	public PrintStreamReporter(PrintStream out){
		super();
		this.out = out;
	}

	/**
	 * メッセージを出力します。
	 * このオブジェクトが既に閉じられている場合、このメソッドは
	 * 何も行いません。
	 * @param message メッセージ
	 */
	public synchronized void report(String message){
		if(out != null){ this.out.println(message); }
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
				this.flush();
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
}
