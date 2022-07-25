package org.intra_mart.common.aid.jdk.java.io.file;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * このクラスは、ファイルを扱う時に便利な機能を提供します。<P>
 *
 * このクラスは java.io.File クラスをベースに、オブジェクトが表す抽象パスが
 * ファイルである場合に利用することができます。<br>
 * <P>
 *
 * @see java.io.File
 * @see org.intra_mart.common.aid.jdk.java.io.file.ExtendedDirectory
 */
public class ExtendedFile extends File{
	/**
	 * 指定のパスがファイルを表すかどうかをチェックします。
	 * @param path ファイルの抽象パス
	 * @throws IllegalArgumentException 指定の抽象パスがディレクトリとして存在している場合
	 */
	private static void isValid(File path) throws IllegalArgumentException{
		if(path.isDirectory()){
			throw new IllegalArgumentException("A path is a directory: " + path.getAbsolutePath());
		}
	}

	/**
	 * 指定されたパス名文字列を抽象パス名に変換して、
	 * 新しい File のインスタンスを生成します。
	 * 指定された文字列が空の文字列の場合、結果は空の抽象パス名になります。
	 * @param pathname パス名文字列
	 * @throws NullPointerException pathname 引数が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがディレクトリとして存在している場合
	 * @see java.io.File#File(File, String)
	 */
	public ExtendedFile(String pathname){
		super(pathname);
		isValid(this);
	}

	/**
	 * 親抽象パス名および子パス名文字列から新しい File のインスタンスを
	 * 生成します。<p>
	 * parent が null の場合、新しい File のインスタンスは、
	 * 指定された child パス名文字列で単一引数の File コンストラクタを
	 * 呼び出したように生成されます。<p>
	 * そうでない場合、parent 抽象パス名はディレクトリを示し、
	 * child パス名文字列はディレクトリまたはファイルを示します。
	 * child パス名文字列が絶対の場合、
	 * それはシステムに依存する方法で相対パス名に変換されます。
	 * parent が空の抽象パス名の場合、新しい File のインスタンスは、
	 * child を抽象パス名に変換し、その結果をシステムに依存する
	 * デフォルトディレクトリを基準に解決することで生成されます。
	 * そうでない場合、各パス名文字列は抽象パス名に変換され、
	 * 子抽象パス名は親を基準に解決されます。
	 * @param parent 親抽象パス名
	 * @param child 子パス名文字列
	 * @throws NullPointerException child が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがディレクトリとして存在している場合
	 * @see java.io.File#File(File, String)
	 */
	public ExtendedFile(File parent, String child){
		super(parent, child);
		isValid(this);
	}

	/**
	 * 親パス名文字列および子パス名文字列から
	 * 新しい File のインスタンスを生成します。<p>
	 * parent が null の場合、新しい File のインスタンスは、
	 * 指定された child パス名文字列で単一引数の File コンストラクタを
	 * 呼び出したように生成されます。<pr>
	 * そうでない場合、parent パス名文字列はディレクトリを示し、
	 * child パス名文字列はディレクトリまたはファイルを示します。
	 * child パス名文字列が絶対の場合、
	 * それはシステムに依存する方法で相対パス名に変換されます。
	 * parent が空の文字列の場合、新しい File のインスタンスは、
	 * child を抽象パス名に変換し、
	 * その結果をシステムに依存するデフォルトディレクトリを基準に
	 * 解決することで生成されます。
	 * そうでない場合、各パス名文字列は抽象パス名に変換され、
	 * 子抽象パス名は親を基準に解決されます。
	 * @param parent 親パス名文字列
	 * @param child 子パス名文字列
	 * @throws NullPointerException child が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがディレクトリとして存在している場合
	 * @see java.io.File#File(File, String)
	 */
	public ExtendedFile(String parent, String child){
		super(parent, child);
		isValid(this);
	}

	/**
	 * この抽象パスが表すファイルからデータを読み込みます。
	 * @return ファイルの内容
	 * @throws FileNotFoundException ファイルが存在しないか、普通のファイルではなくディレクトリであるか、またはなんらかの理由で読み込みのために開くことができない場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public byte[] getBytes() throws FileNotFoundException, IOException{
		// ファイルを読み込むためのインスタンスを生成
		FileInputStream fis = new FileInputStream(this);

		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			byte[] aBuf = new byte[2048];				// 読込バッファを定義
			while(true){
				// ファイルロード
				int readSize = fis.read(aBuf);
				if(readSize != -1){
					out.write(aBuf, 0, readSize);
				}
				else{
					break;
				}
			}
			fis.close();
			return out.toByteArray();
		}
		catch(IOException ioe){
			try{
				fis.close();
			}
			catch(Exception e){
				// 無視です
			}
			throw ioe;
		}
	}

	/**
	 * このオブジェクトの抽象パスが表すファイルからデータを読み込みます。<p>
	 * データはテキストとして扱い、デフォルトの文字エンコーディングから
	 * Unicode に変換して返します。
	 * @return ファイルのデータ
	 * @throws FileNotFoundException ファイルが存在しないか、普通のファイルではなくディレクトリであるか、またはなんらかの理由で読み込みのために開くことができない場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public String readString() throws FileNotFoundException, IOException{
		// 読み込んだデータの文字列への変換
//		return readString(org.intra_mart.system.Registry.instance().getProperty("intra-mart/server-charset", System.getProperty("file.encoding")));
		return new String(getBytes());
	}

	/**
	 * このオブジェクトの抽象パスが表すファイルからデータを読み込みます。<p>
	 * データはテキストとして扱い、指定の文字エンコーディングから
	 * Unicode に変換して返します。
	 * @param enc 文字エンコーディング名
	 * @return ファイルのデータ
	 * @throws FileNotFoundException ファイルが存在しないか、普通のファイルではなくディレクトリであるか、またはなんらかの理由で読み込みのために開くことができない場合
	 * @throws UnsupportedEncodingException 指定の文字のエンコーディングがサポートされていない場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public String readString(String enc) throws FileNotFoundException, UnsupportedEncodingException, IOException{
		// 読み込んだデータの文字列への変換
		return new String(getBytes(), enc);
	}

	/**
	 * ファイルの行を繰り返し処理する反復子を作成します。<p>
	 * データはテキストとして扱い、デフォルトの文字エンコーディングを使って
	 * Unicode に変換されます。
	 * 反復子が返す各要素は、ファイル内の 1 行のテキストです。
	 * 1 つの行は、改行 ('\n')、復帰 ('\r')、
	 * または復帰とその直後に続く改行のどれかにより終了したと見なされます。
	 * @return ファイルの行を繰り返し処理する反復子
	 * @throws FileNotFoundException ファイルが存在しないか、普通のファイルではなくディレクトリであるか、またはなんらかの理由で読み込みのために開くことができない場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public Iterator readLines() throws FileNotFoundException, IOException{
//		return readLines(org.intra_mart.system.Registry.instance().getProperty("intra-mart/server-charset", System.getProperty("file.encoding")));
		return readLines(new FileReader(this));
	}

	/**
	 * ファイルの行を繰り返し処理する反復子を作成します。<p>
	 * データはテキストとして扱い、指定の文字エンコーディングを使って
	 * Unicode に変換されます。
	 * 反復子が返す各要素は、ファイル内の 1 行のテキストです。
	 * 1 つの行は、改行 ('\n')、復帰 ('\r')、
	 * または復帰とその直後に続く改行のどれかにより終了したと見なされます。
	 * @param enc 文字エンコーディング名
	 * @return ファイルの行を繰り返し処理する反復子
	 * @throws FileNotFoundException ファイルが存在しないか、普通のファイルではなくディレクトリであるか、またはなんらかの理由で読み込みのために開くことができない場合
	 * @throws UnsupportedEncodingException 指定の文字のエンコーディングがサポートされていない場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public Iterator readLines(String enc) throws FileNotFoundException, UnsupportedEncodingException, IOException{
		FileInputStream fis = new FileInputStream(this);
		return readLines(new InputStreamReader(fis, enc));
	}

	/**
	 * ファイルの行を繰り返し処理する反復子を作成します。<p>
	 * データはテキストとして扱い、指定の文字エンコーディングを使って
	 * Unicode に変換されます。
	 * 反復子が返す各要素は、ファイル内の 1 行のテキストです。
	 * 1 つの行は、改行 ('\n')、復帰 ('\r')、
	 * または復帰とその直後に続く改行のどれかにより終了したと見なされます。
	 * @param reader 入力ストリーム
	 * @return ファイルの行を繰り返し処理する反復子
	 * @throws IOException 入出力エラーが発生した場合
	 */
	private Iterator readLines(Reader reader) throws IOException{
		BufferedReader in = new BufferedReader(reader);
		try{
			List lines = new ArrayList();
			while(in.ready()){
				String line = in.readLine();
				if(line != null){ lines.add(line); } else{ break; }
			}
			in.close();
			return lines.iterator();
		}
		catch(IOException ioe){
			try{
			}
			catch(Exception e){
				// 無視
			}
			throw ioe;
		}
	}

	/**
	 * デフォルトの文字列を指定の文字エンコーディングを使って
	 * このオブジェクトの表すファイルに書き込みます。<p>
	 * @param str 書き込む文字列
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(String str) throws IOException{
//		write(str, org.intra_mart.system.Registry.instance().getProperty("intra-mart/server-charset", System.getProperty("file.encoding")));
		write(str.getBytes());
	}

	/**
	 * 指定された文字列を指定の文字エンコーディングを使って
	 * このオブジェクトの表すファイルに書き込みます。<p>
	 * @param str 書き込む文字列
	 * @param enc 使用するエンコーディングの名前
	 * @throws UnsupportedEncodingException 指定の文字のエンコーディングがサポートされていない場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(String str, String enc) throws UnsupportedEncodingException, IOException{
		write(str.getBytes(enc));
	}

	/**
	 * デフォルトの文字列を指定の文字エンコーディングを使って
	 * このオブジェクトの表すファイルに書き込みます。<p>
	 * 文字列は、ファイルの先頭ではなくファイルの最後に書き込まれます。
	 * したがって、すでに存在するファイルに対して文字列を追記する場合に、
	 * このメソッドを利用します。
	 * このメソッドは、ファイルが存在しなかった場合、新しくファイルを
	 * 作成して文字列をファイルの先頭に書き込みます。
	 * @param str 書き込む文字列
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void append(String str) throws IOException{
//		append(str, org.intra_mart.system.Registry.instance().getProperty("intra-mart/server-charset", System.getProperty("file.encoding")));
		append(str.getBytes());
	}

	/**
	 * 指定された文字列を指定の文字エンコーディングを使って
	 * このオブジェクトの表すファイルに書き込みます。<p>
	 * 文字列は、ファイルの先頭ではなくファイルの最後に書き込まれます。
	 * したがって、すでに存在するファイルに対して文字列を追記する場合に、
	 * このメソッドを利用します。
	 * このメソッドは、ファイルが存在しなかった場合、新しくファイルを
	 * 作成して文字列をファイルの先頭に書き込みます。
	 * @param str 書き込む文字列
	 * @param enc 使用するエンコーディングの名前
	 * @throws UnsupportedEncodingException 指定の文字のエンコーディングがサポートされていない場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void append(String str, String enc) throws UnsupportedEncodingException, IOException{
		append(str.getBytes(enc));
	}

	/**
	 * 指定されたバイト配列の b.length バイトをこのオブジェクトの表すファイルに
	 * 書き込みます。<p>
	 * バイトは、ファイルの先頭ではなくファイルの最後に書き込まれます。
	 * したがって、すでに存在するファイルに対してバイトを追記する場合に、
	 * このメソッドを利用します。
	 * このメソッドは、ファイルが存在しなかった場合、新しくファイルを
	 * 作成してバイトをファイルの先頭に書き込みます。
	 * @param b データ
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void append(byte[] b) throws IOException{
		this.write(b, true);
	}

	/**
	 * 指定されたバイト配列の b.length バイトをこのオブジェクトの表すファイルに
	 * 書き込みます。
	 * @param b データ
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(byte[] b) throws IOException{
		this.write(b, false);
	}

	/**
	 * 指定されたバイト配列の b.length バイトをこのオブジェクトの表すファイルに
	 * 書き込みます。
	 * @param b データ
	 * @param append true の場合、バイトはファイルの先頭ではなく最後に書き込まれる
	 * @throws FileNotFoundException ファイルは存在するが、普通のファイルではなくディレクトリである場合、ファイルは存在せず作成もできない場合、またはなんらかの理由で開くことができない場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	private synchronized void write(byte[] b, boolean append) throws FileNotFoundException, IOException{
		FileOutputStream fos = new FileOutputStream(this.getAbsolutePath(), append);
		try{
			fos.write(b);
			fos.close();
		}
		catch(IOException ioe){
			try{
				fos.close();
			}
			catch(Exception e){
				// 無視です。
			}
			throw ioe;
		}
	}
}


