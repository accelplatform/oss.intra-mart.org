package org.intra_mart.common.aid.jdk.java.io.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.intra_mart.common.aid.jdk.java.util.FixedSizeMap;


/**
 * File インスタンスを作成するためのファクトリクラスです。<P>
 * このクラスは、指定のディレクトリを親とすして、相対パスにより解決される
 * 抽象パスを表す File インスタンスを生成する機能を提供します。
 * 生成された File インスタンスは、オブジェクト内にプールされるため、
 * 同じパスを繰り返し利用ようなプログラムで利用することにより、
 * メモリリソースを節約することができます。
 * <P>
 *
 */
public class ExtendedDirectory extends Directory{
	/**
	 * ZIP 形式の圧縮ファイル作成時に利用するファイル名のセパレータ
	 */
	private static final String FILE_SEPARATOR = String.valueOf(File.separatorChar);
	/**
	 * このディレクトリを親とするファイルオブジェクトのキャッシュ領域
	 */
	FixedSizeMap POOL;

	/**
	 * File オブジェクトを作成するためのファクトリオブジェクトを作成します。<p>
	 * このオブジェクトは、File インスタンスのプール機能を実装しています。
	 * 引数 size はプールのサイズを指定して下さい。プールは、最大で
	 * size 個のインスタンスをキャッシュするように動作します。
	 * @param pathname ディレクトリパス
	 * @param size キャッシュサイズ
	 * @throws NullPointerException 引数 parent が null の場合
	 * @throws IllegalArgumentException 引数 size が 1 よりも小さい場合
	 */
	public ExtendedDirectory(String pathname, int size){
		super(pathname);
		POOL = new FixedSizeMap(size);
	}

	/**
	 * File オブジェクトを作成するためのファクトリオブジェクトを作成します。<p>
	 * このオブジェクトは、File インスタンスのプール機能を実装しています。
	 * このオブジェクトのパスは、parent を親ディレクトリとした
	 * 子抽象パス path により解決されます。<br>
	 * 引数 size はプールのサイズを指定して下さい。プールは、最大で
	 * size 個のインスタンスをキャッシュするように動作します。
	 * @param parent 親ディレクトリ
	 * @param path パス
	 * @param size キャッシュサイズ
	 * @throws NullPointerException 引数 parent が null の場合
	 * @throws IllegalArgumentException 引数 size が 1 よりも小さい場合
	 */
	public ExtendedDirectory(File parent, String path, int size){
		super(parent, path);
		POOL = new FixedSizeMap(size);
	}

	/**
	 * このディレクトリを親ディレクトリとして
	 * path で表される ExtendedFile オブジェクトを返します。
	 *
	 * @param path 子パス名文字列
	 * @return path から作成される File インスタンス
	 * @throws NullPointerException path が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがディレクトリとして存在している場合
	 */
	public synchronized ExtendedFile childFile(String path){
		File f = (File) POOL.get(path);
		if(f != null){
			if(f instanceof ExtendedFile){
				return (ExtendedFile) f;
			}
		}

		f = findFile(path);
		POOL.put(path, f);
		return (ExtendedFile) f;
	}

	/**
	 * このディレクトリを親ディレクトリとして
	 * path で表される ExtendedFile オブジェクトを返します。<p>
	 * このメソッドは、childFile メソッドが要求されたファイルオブジェクトを
	 * プールしていなかった場合に、新しいファイルオブジェクトを作成するために
	 * 呼び出します。
	 * サブクラスは、このメソッドをオーバーライドすることにより、
	 * ファイルインスタンスを任意に作成することができます。<p>
	 * このメソッドは、単純にこのオブジェクトを親ディレクトリとして
	 * 引数 path を解決したファイルオブジェクトを作成して返します。
	 *
	 * @param path 子パス名文字列
	 * @return path から作成される File インスタンス
	 * @throws NullPointerException path が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがディレクトリとして存在している場合
	 */
	protected ExtendedFile findFile(String path){
		return new ExtendedFile(this, path);
	}

	/**
	 * このディレクトリを親ディレクトリとして
	 * path で表される ExtendedDirectory オブジェクトを返します。
	 * このメソッドは、このオブジェクトのプール許容値と等しい
	 * プール許容値を持つ ExtendedDirectory のインスタンスを返します。
	 *
	 * @param path 子パス名文字列
	 * @return path から作成される File インスタンス
	 * @throws NullPointerException path が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがファイルとして存在している場合
	 */
	public synchronized ExtendedDirectory childDirectory(String path){
		File f = (File) POOL.get(path);
		if(f != null){
			if(f instanceof ExtendedDirectory){
				return (ExtendedDirectory) f;
			}
		}

		f = findDirectory(path);
		POOL.put(path, f);
		return (ExtendedDirectory) f;
	}

	/**
	 * このディレクトリを親ディレクトリとして
	 * path で表される ExtendedDirectory オブジェクトを返します。<p>
	 * このメソッドは、childDirectory メソッドが要求されたファイルオブジェクトを
	 * プールしていなかった場合に、新しいファイルオブジェクトを作成するために
	 * 呼び出します。
	 * サブクラスは、このメソッドをオーバーライドすることにより、
	 * ファイルインスタンスを任意に作成することができます。<p>
	 * このメソッドは、単純にこのオブジェクトを親ディレクトリとして
	 * 引数 path を解決したファイルオブジェクトを作成して返します。
	 *
	 * @param path 子パス名文字列
	 * @return path から作成される File インスタンス
	 * @throws NullPointerException path が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがファイルとして存在している場合
	 */
	protected ExtendedDirectory findDirectory(String path){
		return new ExtendedDirectory(this, path, POOL.capacity());
	}

	/**
	 * このディレクトリを親ディレクトリとして
	 * path で表される File オブジェクトを返します。
	 *
	 * @param path 子パス名文字列
	 * @return path から作成される File インスタンス
	 * @throws NullPointerException path が null の場合
	 */
	public synchronized File childPath(String path){
		File f = (File) POOL.get(path);
		if(f == null){
			f = findPath(path);
			POOL.put(path, f);
		}
		return f;
	}

	/**
	 * このディレクトリを親ディレクトリとして
	 * path で表される File オブジェクトを返します。<p>
	 * このメソッドは、childPath メソッドが要求されたファイルオブジェクトを
	 * プールしていなかった場合に、新しいファイルオブジェクトを作成するために
	 * 呼び出します。
	 * サブクラスは、このメソッドをオーバーライドすることにより、
	 * ファイルインスタンスを任意に作成することができます。<p>
	 * このメソッドは、単純にこのオブジェクトを親ディレクトリとして
	 * 引数 path を解決したファイルオブジェクトを作成して返します。
	 *
	 * @param path 子パス名文字列
	 * @return path から作成される File インスタンス
	 * @throws NullPointerException path が null の場合
	 */
	protected File findPath(String path){
		return new File(this, path);
	}

	/**
	 * キャッシュ領域をクリアします。
	 */
	public synchronized void reset(){
		POOL.clear();
	}

	/**
	 * このディレクトリ内にあるすべてのファイルを
	 * 指定のファイルにバックアップします。
	 * 作成されるファイルは、ZIP ファイル形式となります。
	 * @param f 出力ファイルパス
	 * @throws IOException 入出力エラー
	 * @throws NullPointerException 引数が null の場合
	 */
	public void archive(File f) throws IOException{
		this.archive(f, new FileFilter4allow());
	}

	/**
	 * このディレクトリ内にあるすべてのファイルを
	 * 指定のファイルにバックアップします。
	 * 作成されるファイルは、ZIP ファイル形式となります。
	 * ファイルに出力されるファイルは、フィルタの基準を満たす必要があります。
	 * 圧縮されるファイルは、
	 * {@link java.io.FileFilter#accept(java.io.File)}
	 * メソッドが呼び出されたときに true が返される場合だけです。
	 * @param f 出力ファイルパス
	 * @throws IOException 入出力エラー
	 * @throws NullPointerException 引数が null の場合
	 */
	public void archive(File f, FileFilter filter) throws IOException{
		OutputStream out = new BufferedOutputStream(new FileOutputStream(f), 2048);
		try{
			this.archive(out, filter);
		}
		finally{
			out.close();
		}
	}

	/**
	 * このディレクトリ内にあるすべてのファイルを
	 * 指定の出力ストリームに ZIP ファイル形式で出力します。
	 * @param out 出力ストリーム
	 * @throws IOException 入出力エラー
	 * @throws NullPointerException 引数が null の場合
	 */
	public void archive(OutputStream out) throws IOException{
		this.archive(out, new FileFilter4allow());
	}

	/**
	 * このディレクトリ内にあるすべてのファイルを
	 * 指定の出力ストリームに ZIP ファイル形式で出力します。
	 * ストリームに出力されるファイルは、フィルタの基準を満たす必要があります。
	 * ストリームにファイルの内容が出力されるのは、
	 * {@link java.io.FileFilter#accept(java.io.File)}
	 * メソッドが呼び出されたときに true が返される場合だけです。
	 * @param out 出力ストリーム
	 * @throws IOException 入出力エラー
	 * @throws NullPointerException 引数が null の場合
	 */
	public void archive(OutputStream out, FileFilter filter) throws IOException{
		ZipOutputStream zos = new ZipOutputStream(out);
		try{
			this.archive(zos, filter);
		}
		finally{
			zos.finish();
		}
	}

	/**
	 * このディレクトリ内にあるすべてのファイルを
	 * 指定の圧縮用出力ストリームに ZIP ファイル形式で出力します。
	 * @param out 出力ストリーム
	 * @throws IOException 入出力エラー
	 * @throws NullPointerException 引数が null の場合
	 */
	public void archive(ZipOutputStream out) throws IOException{
		this.archive(out, new FileFilter4allow());
	}

	/**
	 * このディレクトリ内にあるすべてのファイルを
	 * 指定の圧縮用出力ストリームに ZIP ファイル形式で出力します。
	 * ストリームに出力されるファイルは、フィルタの基準を満たす必要があります。
	 * ストリームにファイルの内容が出力されるのは、
	 * {@link java.io.FileFilter#accept(java.io.File)}
	 * メソッドが呼び出されたときに true が返される場合だけです。
	 * @param out 出力ストリーム
	 * @param filter ファイル名フィルタ
	 * @throws IOException 入出力エラー
	 * @throws NullPointerException 引数が null の場合
	 */
	public void archive(ZipOutputStream out, FileFilter filter) throws IOException{
		Map view = getChildNodes(new HashMap(), this, "");
		Iterator cursor = view.entrySet().iterator();
		while(cursor.hasNext()){
			Map.Entry entry = (Map.Entry) cursor.next();
			if(filter.accept((File) entry.getValue())){
				file2zip(out, (String) entry.getKey(), (File) entry.getValue());
			}
		}
	}

	// zip に圧縮
	private static void file2zip(ZipOutputStream zos, String name, File path) throws IOException{
		// 入力ストリームの作成
		FileInputStream fis = new FileInputStream(path);
		BufferedInputStream in = new BufferedInputStream(fis, 2048);

		// エントリーの登録
		ZipEntry zipEntry = new ZipEntry(name.replace('\\', '/'));
		zos.putNextEntry(zipEntry);

		try{
			// 読込＆書込
			for(int b = in.read(); b != -1; b = in.read()){ zos.write(b); }
		}
		finally{
			try{
				in.close();				// 入力ストリームの破棄
			}
			finally{
				zos.closeEntry();		// zip エントリの終端指定
			}
		}
	}

	// 指定ディレクトリ以下のすべてのファイルを検索
	private Map getChildNodes(Map list, File dir, String prefix){
		File[] nodes = dir.listFiles();
		for(int idx = 0; idx < nodes.length; idx++){
			if(nodes[idx].isFile()){
				// ファイルだ！
				list.put(prefix.concat(nodes[idx].getName()), nodes[idx]);
			}
			else{
				// ディレクトリだ！
				getChildNodes(list, nodes[idx], prefix.concat(nodes[idx].getName()).concat(FILE_SEPARATOR));
			}
		}
		return list;
	}

	/**
	 * すべてのファイルをリスト化することを許容する
	 * デフォルトのフィルター。
	 */
	private static class FileFilter4allow implements FileFilter{
		/**
		 * 唯一のコンストラクタ
		 */
		protected FileFilter4allow(){
			super();
		}

		/**
		 *指定された抽象パス名がパス名リストに含まれる必要があるかどうかを判定します。
		 * <p>このメソッドは、必ず true を返します。
		 * @param pathname テスト対象の抽象パス名
		 * @return pathname が含まれる必要がある場合は true
		 */
		public boolean accept(File pathname){
			return true;
		}
	}
}


