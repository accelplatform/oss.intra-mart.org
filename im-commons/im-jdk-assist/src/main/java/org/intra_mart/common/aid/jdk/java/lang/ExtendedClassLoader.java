package org.intra_mart.common.aid.jdk.java.lang;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.intra_mart.common.platform.log.Logger;


/**
 * このクラスは、拡張されたクラスローダーです。<P>
 * <BR>
 * このクラスローダーは、リソースを検索するパスを任意に追加することが
 * できます。
 *
 */
public class ExtendedClassLoader extends ResourceLoader{
	
	private static Logger _logger = Logger.getLogger();

/*
public static void main(String[] args){

	try{
		File file = new File("E:/work/bm43/app/bin/intramart.zip");
		ExtendedClassLoader classLoader = new ExtendedClassLoader();
		classLoader.addClassArchive(file);
		classLoader.addClassPath(file.getParentFile());
		String path = "org/intra_mart/resources/drawer/color_list.properties";

//		URL[] urls = { file.toURL(), file.getParentFile().toURL() };
//		URLClassLoader urlClassLoader = new URLClassLoader(urls);

		InputStream in = classLoader.getResourceAsStream(path);
		while(true){
			int chr = in.read();
			if(chr == -1){ break; }
			System.out.write(chr);
		}
		URL url = classLoader.getResource("foundation.properties");
		System.out.println("URL=" + file.getParentFile().toURL().toExternalForm());
		System.out.println("URL=" + url.toExternalForm());
		Class clazz = classLoader.loadClass("org.intra_mart.system.javascript.imapi.EnvironObject");
		System.out.println("Class=" + clazz.toString());
	}
	catch(Throwable t){
		t.printStackTrace();
	}
}
*/

	/**
	 * ネイティブライブラリファイルを探す基準となる親ディレクトリ
	 */
	private List nativeDirs = new ArrayList();

	/**
	 * このクラスをロードした ClassLoader を親クラスローダとして使用し、
	 * 新しいクラスローダを作成します。
	 */
	public ExtendedClassLoader(){
		this(ExtendedClassLoader.class.getClassLoader());
	}

	/**
	 * 指定された親クラスローダを使って、
	 * 委譲のために新しいクラスローダを作成します。<br>
	 * @param parent 親クラスローダー
	 */
	public ExtendedClassLoader(ClassLoader parent){
		super(parent);
	}

	/**
	 * クラスパスを追加します。
	 * ここで追加されるクラスパスは、このクラスローダー内でのみ有効です。<br>
	 * クラスローダーは、最も過去に追加されたパスから順に
	 * ネイティブリソースを検索していきます。
	 * path が既に追加済みである場合、このメソッドは何も行いません。
	 * @param path 追加するパス
	 */
	public synchronized void addNativePath(File path){
		if(path == null){ throw new NullPointerException("path is null"); }

		if(! nativeDirs.contains(path)){
			// 新規パスなのでリストに追加
			nativeDirs.add(path);
			_logger.log(this.getLogLevel(), "Add native libraries path: " + path.getAbsolutePath());
		}
	}

	/**
	 * このクラスローダーに設定されているネイティブライブラリパスを返します。<br>
	 * このクラスローダーでは、このメソッドにより返されるパスと
	 * このクラスローダーの動作している環境からネイティブライブラリを検索しています。
	 * このメソッドで返されるパスは、このクラスローダー固有に設定されている
	 * パスに限られます。<br>
	 * @return クラスパス
	 * @see #addNativePath(File)
	 * @see java.io.File
	 */
	public synchronized File[] getNativePaths(){
		return (File[]) this.nativeDirs.toArray(new File[this.nativeDirs.size()]);
	}

	/**
	 * ネイティブライブラリの絶対パス名を返します。<br>
	 * VM はこのメソッドを呼び出して、このクラスローダでロードされたクラスに
	 * 属するネイティブライブラリを見つけます。
	 * このメソッドが null を返す場合、VM は java.library.path プロパティで
	 * 指定されたパスにしたがってライブラリを検索します。
	 * @param libname ライブラリ名
	 * @return ネイティブライブラリの絶対パス
	 */
	protected synchronized String findLibrary(String libname){
		String fileName = System.mapLibraryName(libname);

		Iterator cursor = nativeDirs.iterator();
		while(cursor.hasNext()){
			File f = new File((File) cursor.next(), fileName);
			if(f.isFile()){ return f.getAbsolutePath(); }	// 発見！
		}

		return null;											// 発見できず
	}

	/**
	 * 指定されたクラスを探します。<br>
	 * このメソッドは、クラスをロードするための新しい委譲モデルに準拠する
	 * クラスローダ実装によってオーバーライドされ、必要なクラスの
	 * 親クラスローダのチェック後に loadClass メソッドによって呼び出されます。
	 * デフォルトの実装は ClassNotFoundException をスローします。
	 * @param name クラス名
	 * @return 結果の Class オブジェクト
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 */
	protected synchronized Class findClass(String name) throws ClassNotFoundException{
		// クラスファイル名
		String filename = name.replace('.', '/').concat(".class");

		// 生のまま存在するクラスファイルを検索
		try{
			InputStream in = this.getResourceAsStream(filename);
			if(in != null){
				try{
					// パッケージの定義
					int dot = name.lastIndexOf('.');
					if(dot > 0){
						String packageName = name.substring(0, dot);
						if(getPackage(packageName) == null){
							this.definePackage(packageName, null, null, null, null, null, null, null);
						}
					}

					// クラスデータの取得 → クラス定義およびクラスの返却
					byte[] b = this.getStreamData(in);
					return this.defineClass(name, b, 0, b.length);
				}
				finally{
					in.close();
				}
			}
		}
		catch(IOException ioe){
			// 該当するファイルが見つからない
		}

		// 最終手段(例外を発生させるため)
		return ClassLoader.getSystemClassLoader().loadClass(name);
	}

	/**
	 * 指定の入力ストリームからデータを取得してバイト配列として返します。<br>
	 * この関数は、入力ストリームが -1 を返すまでブロックします。
	 * @param in 入力ストリーム
	 * @return ファイルの中身
	 * @throws IOException 入出力エラー
	 */
	private byte[] getStreamData(InputStream in) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		byte[] buf = new byte[2048];

		while(true){
			int len = in.read(buf);
			if(len > -1){ baos.write(buf, 0, len); } else{ break; }
		}

		return baos.toByteArray();
	}
}

