package org.intra_mart.common.aid.jdk.java.io;

import java.io.Writer;
import java.io.IOException;

/**
 * このクラスは、部分文字列を別な部分文字列へ置換しながら
 * ストリームに文字列を書き込むためのストリームフィルタを実装します。<br>
 * 置換対象となる部分文字列と置換する文字列の長さは同じ必要はありません。
 * 置換対象となる文字データと異なる長さの文字データを置換後のデータとして
 * 指定することも可能です。この場合、
 * このクラスによって基礎ストリームに書き込まれるデータ量は、
 * 元のデータ量と変化します。<p>
 * <pre>
 *   java.io.StringWriter sw = new java.io.StringWriter();<br>
 *   ReplaceWriter rw = new ReplaceWriter(sw, "abc", "xyz");<br>
 *   rw.write("abc");
 *   String result = sw.toString();
 * </pre>
 * 文字データ "abc" は、"xyz" に変化します。この場合、以下のように
 * 記述しても同様の結果が得られます。
 * <pre>
 *   java.io.StringWriter sw = new java.io.StringWriter();<br>
 *   ReplaceWriter rw = new ReplaceWriter(sw, "abc", "xyz");<br>
 *   rw.write("a");
 *   rw.write("b");
 *   rw.write("c");
 *   String result = sw.toString();
 * </pre>
 *
 */
public class ReplaceWriter extends Writer{
	private Writer out;						// 基礎ストリーム
	private char[] newChars = null;			// 置換後
	private char[] oldChars;				// 置換前
	private char startCharactor;			// 置換対象文字データの先頭文字
	private int charsLength;				// 置換対象文字データの長さ
	private int activeIndex = 0;			// 置換対象文字データの検索現在位置

/*
	public static void main(String[] arg){
		try{
			String oldWord = "bc";
			String newWord = "xyz";
			String str1 = "aaabbcbbbdfbccbb";
			String str2 = "caaabbcbbbdfbccbb";
			java.io.StringWriter sw = new java.io.StringWriter();
			ReplaceWriter rw = new ReplaceWriter(sw, oldWord, newWord);
			rw.write(str1);
			rw.write(str2);
			rw.close();
			sw.close();

			System.out.println("old word: " + oldWord);
			System.out.println("new word: " + newWord);
			System.out.println("  before: " + str1 + " " + str2);
			System.out.println("   after: " + sw.toString());
		}
		catch(Throwable t){
			t.printStackTrace();
		}

		System.exit(0);
	}
*/

	/**
	 * 文字列削除のためのストリームフィルタを作成します。<br>
	 * このインスタンスに書き込まれた文字データは部分文字列の削除を行いながら
	 * 基礎ストリームに書き込まれます。
	 *
	 * @param out 基礎ストリーム
	 * @param oldChars 削除対象となる文字の配列
	 */
	public ReplaceWriter(Writer out, char[] oldChars){
		this(out, oldChars, null);
	}

	/**
	 * 文字列削除のためのストリームフィルタを作成します。<br>
	 * このインスタンスに書き込まれた文字データは部分文字列の削除を行いながら
	 * 基礎ストリームに書き込まれます。
	 *
	 * @param out 基礎ストリーム
	 * @param oldString 削除対象となる文字列
	 */
	public ReplaceWriter(Writer out, String oldString){
		this(out, oldString.toCharArray());
	}

	/**
	 * 文字列置換のためのストリームフィルタを作成します。<br>
	 * このインスタンスに書き込まれた文字データは適切な置換を行いながら
	 * 基礎ストリームに書き込まれます。
	 *
	 * @param out 基礎ストリーム
	 * @param oldChars 置換対象となる文字の配列
	 * @param newChars 置換する文字の配列
	 */
	public ReplaceWriter(Writer out, char[] oldChars, char[] newChars){
		super();
		this.out = out;
		this.oldChars = oldChars;
		if(newChars != null){
			if(newChars.length > 0){
				this.newChars = newChars;		// 置換する文字データ
			}
		}
		startCharactor = oldChars[0];
		charsLength = oldChars.length;
	}

	/**
	 * 文字列置換のためのストリームフィルタを作成します。
	 *
	 * @param out 基礎ストリーム
	 * @param oldString 置換対象となる文字列
	 * @param newString 置換する文字列
	 */
	public ReplaceWriter(Writer out, String oldString, String newString){
		this(out, oldString.toCharArray(), newString.toCharArray());
	}

	/**
	 * 単一文字を書き込みます。
	 * 書き込まれる文字は、指定された整数値の下位 16 ビットに格納されます。
	 * 上位 16 ビットは無視されます。
	 * @param c 文字
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized void write(int c) throws IOException{
		ensureOpen();		// 準備ＯＫ？
		if(c != oldChars[activeIndex]){
			if(activeIndex == 0){
				this.out.write(c);				// 基礎ストリームへの書き込み
			}
			else{
				// バッファ内データの再チェック
				int max = activeIndex;
				this.activeIndex = 0;			// チェックポインタの再初期化
				this.out.write(startCharactor);	// 基礎ストリームへの書き込み
				for(int idx = 1; idx < max; idx++){
					this.write(oldChars[idx]);	// 再帰処理による再チェック
				}
				this.write(c);					// 再帰処理
			}
		}
		else{
			// チェックポインタの更新
			if(++activeIndex == charsLength){
				this.activeIndex = 0;			// チェックポインタの再初期化
				// 置換データの書き込み
				if(newChars != null){ this.out.write(newChars); }
			}
		}
	}

	/**
	 * 文字列の一部を基礎ストリームに書き込みます。<br>
	 *
	 * @param cbuf 文字の配列
	 * @param off 文字の書き込み開始オフセット
	 * @param len 書き込む文字数
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized void write(char[] cbuf, int off, int len) throws IOException{
		ensureOpen();		// 準備ＯＫ？
		while(--len >= 0){ this.write(cbuf[off++]); }		// データの書き出し
	}

	/**
	 * バッファに保存してあるデータを書き出します。<p>
	 * ストリームがさまざまな write() メソッドからの文字をバッファに
	 * 保存してある場合、これらの文字を基礎ストリームへただちに
	 * 書き込みます。<br>
	 * このメソッドは、このストリームの持つ置換対象データと比較中の
	 * 文字データを含むすべてのバッファを基礎ストリームに書き込みます。
	 * このメソッドを呼び出し後は、write() メソッドによって与えられる
	 * データを改めて検索し直します。この機能のより、このストリームフィルタの
	 * 複数のストリームでの再利用が可能となります。<p>
	 * このときストリームは閉じられません。
	 * 複数のフィルタを同じ出力ストリームに連続して適用するときに
	 * このメソッドを使用します。
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized void finish() throws IOException{
		ensureOpen();			// 準備ＯＫ？
		if(activeIndex > 0){
			// 一致検索途中のデータを出力
			this.out.write(oldChars, 0, activeIndex);
			activeIndex = 0;
		}
	}

	/**
	 * ストリームをフラッシュします。<p>
	 * 現在バッファに保存されているデータが置換対象の文字データと
	 * 比較途中である場合、比較中のデータはフラッシュされません。
	 * このストリームの持つすべてのバッファをフラッシュするためには、
	 * finish() メソッドを呼び出したあとにこのメソッドを呼び出して下さい。<br>
	 * 宛先が別の文字またはバイトストリームの場合は、
	 * この宛先をフラッシュします。つまり 1 つの flush() の呼び出しで、
	 * 関連する Writer および OutputStream のすべてのバッファを
	 * フラッシュします。
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized void flush() throws IOException{
		ensureOpen();			// 準備ＯＫ？
		if(activeIndex > 0){
			// 一致検索途中のデータを出力
			this.out.write(oldChars, 0, activeIndex);
		}
		this.out.flush();		// 基礎ストリームのフラッシュ
	}

	/**
	 * ストリームを閉じてフラッシュします。<br>
	 * ストリームを一度閉じると、以降 write() または flush() を呼び出すと、
	 * IOException がスローされます。
	 * ただし、前に閉じたストリームを閉じても効果はありません。
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public synchronized void close() throws IOException{
		if(out != null){
			finish();
			flush();
			out.close();
			out = null;
		}
	}

	// 基礎ストリームのチェック
	private void ensureOpen() throws IOException{
		if(out == null){ throw new IOException("Stream closed"); }
	}
}

