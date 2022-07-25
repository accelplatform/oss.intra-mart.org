package org.intra_mart.common.aid.jdk.util.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * メッセージをファイルに出力するストリームクラスです。<p>
 * このクラスは、メッセージを指定のファイルに対して出力するための
 * スケルトンです。
 * また、このクラスは reset メソッドをオーバーライドすることにより、
 * ファイルのローテイト機能などを実装することができます。
 *
 */
public class ReportFileWriter extends Writer{
	/**
	 * このクラスで扱うファイル
	 */
	private File logFile;
	/**
	 * このオブジェクトでの出力メッセージサイズ（文字数？）
	 */
	private long outSize = 0;
	/**
	 * このライターの出力先ライター
	 */
	private Writer out;

	/**
	 * ファイルへメッセージを書き出すためのライターオブジェクトを作成します。
	 *
	 * @param f このライターで書き出すファイル
	 * @throws IOException 入出力エラー
	 */
	public ReportFileWriter(File f) throws IOException{
		super();
		this.logFile = f;
		// 親ディレクトリの作成→出力ストリームの準備
		if(! f.getParentFile().isDirectory()){ f.getParentFile().mkdirs(); }
		this.out = this.createWriter();
	}

	/**
	 * このオブジェクトが文字を書き出すファイルを取得します。<br>
	 * このメソッドは、このオブジェクトの作成時にコンストラクタの引数として
	 * 与えられた File オブジェクトへの参照を返します。
	 * @return このライターで書き出すファイル
	 */
	public File getFile(){
		return this.logFile;
	}

	/**
	 * メッセージをファイルへ書き出すための出力ストリームを作成します。
	 * <p>
	 * このメソッドは、単に getFile メソッドが返すファイルに対して
	 * 文字を書き出すための出力ストリームを作成して返します。
	 * このメソッドの返す出力ストリームを使うことで、 getFile メソッドの返す
	 * ファイルに対してメッセージを追記していくことができます。<br>
	 * メッセージを任意の文字コードに変換したり、出力バッファを備えた
	 * Writer オブジェクトを作成する必要がある場合には、サブクラスで
	 * このメソッドをオーバーライドして実装して下さい。
	 * @return 出力ストリーム
	 */
	protected Writer createWriter() throws IOException{
		return new OutputStreamWriter(new FileOutputStream(this.logFile.getAbsolutePath(), true));
	}

	/**
	 * このライターの出力文字数を取得します。<br>
	 * このメソッドで返される文字数は、このファイルに対して
	 * 現在までに出力した文字数です。
	 * 文字数カウンタは、reset メソッドを実行することにより
	 * 0 (ゼロ)にリセットされます。
	 * @return 出力文字数
	 */
	public long length(){
		return this.outSize;
	}

	/**
	 * このオブジェクトが文字を書き出している基礎 Writer を返します。
	 * @return 基礎 Writer オブジェクト
	 */
	public Writer currentWriter(){
		return this.out;
	}

	/**
	 * 基礎ストリームに文字を出力します。<br>
	 * このメソッドをサブクラスがオーバーライドすることにより、
	 * バッファ機能などを実装することができます。
	 * ただし、このメソッドをオーバーライドしたサブクラスは、
	 * 同時に length メソッドもオーバーライドする必要があります。
	 * @param cbuf 文字の配列
	 * @param off 文字の書き込み開始オフセット
	 * @param len 書き込む文字数
	 */
	public synchronized void write(char[] cbuf, int off, int len) throws IOException{
		this.out.write(cbuf, off, len);
		this.outSize += len;
	}

	/**
	 * 現在使用中の Writer をリセットします。<br>
	 * このメソッドは、FileReporter クラスにより呼び出されます。
	 * FileReporter オブジェクトでは、ReportFileConditioner オブジェクトが
	 * ファイルの初期化を必要と判断した場合に、このメソッドを呼び出して、
	 * Writer の初期化を求めてきます。
	 * これにより、リポートファイルのローテイト機能などを
	 * 実装することができます。
	 */
	protected synchronized void reconstruction() throws IOException{
		this.outSize = 0;
		this.out = this.reset(this.out);				// ストリームの再構築
	}

	/**
	 * 文字の出力ストリームをリセットします。<p>
	 * このメソッドは reset メソッドに呼び出されます。<br>
	 * このメソッドは、単に引数のストリーム(<code>writer</code>)をそのまま
	 * リターンします。ファイルのローテイト機能などを実装する場合、
	 * サブクラスでこのメソッドをオーバーライドして下さい。
	 * @param writer 現在のストリーム
	 * @return 新しいストリーム
	 */
	protected Writer reset(Writer writer) throws IOException{
		return writer;
	}

	/**
	 * ストリームを閉じてフラッシュします。<br>
	 * ストリームを一度閉じると、以降 write() または flush() を呼び出すと、
	 * IOException がスローされます。
	 * ただし、前に閉じたストリームを閉じても効果はありません。
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void close() throws IOException{
		this.out.close();
	}

	/**
	 * ストリームをフラッシュします。<br>
	 * ストリームがさまざまな write() メソッドからの文字をバッファに
	 * 保存してある場合、これらの文字の目的の宛先にただちに文字を書き込みます。
	 * 宛先が別の文字またはバイトストリームの場合は、
	 * この宛先をフラッシュします。
	 * つまり 1 つの flush() の呼び出しで、
	 * 関連する Writer および OutputStream の
	 * すべてのバッファをフラッシュします。<p>
	 * サブクラスで、文字のバッファリング機能を実装している場合、
	 * このメソッドをオーバーライドしてバッファに保存している
	 * 文字をフラッシュするように実装しなければいけません。
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void flush() throws IOException{
		this.out.flush();
	}

	/**
	 * このオブジェクトがガーベージコレクションによって破棄される時に、
	 * ガーベージコレクタによって呼び出されます。<br>
	 * このオブジェクトの全てのリソースを破棄します。
	 */
	protected void finalize() throws Throwable{
		this.close();
	}

	/**
	 * シャットダウンイベントが発生したときに実行されるメソッドです。
	 * このメソッドは、シャットダウンイベント発生時に、
	 * バッファされているバイトをすべて出力するために
	 * flush() メソッドを呼び出します。
	 */
	public void handleShutdown(){
		try{
			this.flush();
		}
		catch(IOException ioe){
			System.err.println("Report file flush error: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}
}

