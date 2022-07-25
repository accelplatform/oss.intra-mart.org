package org.intra_mart.common.aid.jdk.java.lang;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.intra_mart.common.platform.log.Logger;


/**
 * このクラスは、ローカルファイルシステムからリソースファイルを検索するクラスローダーの拡張実装です。<P>
 * <BR>
 * このクラスローダーは、リソースを検索するパスを任意に追加することが
 * できます。
 *
 */
public class ResourceLoader extends ClassLoader{
	
	private static Logger _logger = Logger.getLogger();
	
	/**
	 * クラスパスの追加に関するログレベル
	 */
	private Logger.Level logLevelForClassPath = Logger.Level.INFO;

	
	/**
	 * Jar 解凍オブジェクトをキャッシュしておくためのマップ
	 */
	private Map zipMap = new HashMap();
	/**
	 * class ファイルと、その格納アーカイブフィルの関連付け
	 */
	private Map resourceMap = new HashMap();

	/**
	 * クラスファイルを探す基準となる親ディレクトリ
	 */
	private List classDirs = new ArrayList();
	/**
	 * クラスファイルを探すアーカイブファイル
	 */
	private List classArchives = new ArrayList();
	
	/**
	 * このクラスをロードした ClassLoader を親クラスローダとして使用し、
	 * 新しいクラスローダを作成します。
	 */
	public ResourceLoader(){
		this(ResourceLoader.class.getClassLoader());
	}

	/**
	 * 指定された親クラスローダを使って、
	 * 委譲のために新しいクラスローダを作成します。<br>
	 * @param parent 親クラスローダー
	 */
	public ResourceLoader(ClassLoader parent){
		super(parent);
	}

	/**
	 * クラスパスを追加します。<p>
	 * ここで追加されるクラスパスは、このクラスローダー内でのみ有効です。<br>
	 * クラスローダーは、最も過去に追加されたパスから順に
	 * クラスを検索していきます。<br>
	 * path が既に追加済みである場合、このメソッドは何も行いません。
	 * @param path 追加するパス
	 */
	public synchronized void addClassPath(File path){
		if(path == null){ throw new NullPointerException("path is null"); }

		if(! this.classDirs.contains(path)){
			// 新規パスなのでリストに追加
			this.classDirs.add(path);
			_logger.log(this.getLogLevel(), "Add class path: " + path.getAbsolutePath());
		}
	}

	/**
	 * クラスパスに指定のアーカイブファイルを追加します。
	 * ここで追加されるクラスパスは、このクラスローダー内でのみ有効です。<br>
	 * クラスローダーは、最も過去に追加されたパスから順に
	 * クラスを検索していきます。
	 * path が既に追加済みである場合、このメソッドは何も行いません。
	 * @param path 追加するパス
	 */
	public synchronized void addClassArchive(File path){
		if(path == null){ throw new NullPointerException("path is null"); }

		if(! this.classArchives.contains(path)){
			// 新規パスなのでリストに追加
			this.classArchives.add(path);
			_logger.log(this.getLogLevel(), "Add class archive: " + path.getAbsolutePath());
		}
	}

	/**
	 * クラスパスに指定のアーカイブファイル格納ディレクトリを追加します。
	 * ここで追加されるクラスパスは、このクラスローダー内でのみ有効です。<br>
	 * このメソッドは、path に含まれる jar または zip ファイルを検索し、
	 * {@link #addClassArchive(File)} を呼び出して
	 * このクラスローダの検索パスに追加します。
	 * @param path 追加するパス
	 */
	public void addClassArchiveLibrary(File path){
		if(path == null){ throw new NullPointerException("path is null"); }

		// アーカイブファイルの検索→検索対象として登録
		File[] files = path.listFiles(archiveFileFilter);
		if(files != null){
			for(int idx = 0; idx < files.length; idx++){
				this.addClassArchive(files[idx]);
			}
		}
	}

	/**
	 * このクラスローダーに設定されているクラスパスを返します。<br>
	 * このクラスローダーでは、このメソッドにより返されるパスと
	 * このクラスローダーの動作している環境からクラスを検索しています。
	 * このメソッドで返されるパスは、このクラスローダー固有に設定されている
	 * パスに限られます。<br>
	 * @return クラスパス
	 * @see #addClassPath(File)
	 * @see java.io.File
	 */
	public synchronized File[] getClassPaths(){
		return (File[]) this.classDirs.toArray(new File[this.classDirs.size()]);
	}

	/**
	 * このクラスローダーに設定されているクラスアーカイブパスを返します。<br>
	 * このクラスローダーでは、このメソッドにより返されるパスと
	 * このクラスローダーの動作している環境からクラスを検索しています。
	 * このメソッドで返されるパスは、このクラスローダー固有に設定されている
	 * パスに限られます。<br>
	 * @return クラスパス
	 * @see #addClassArchive(File)
	 * @see java.io.File
	 */
	public synchronized File[] getClassArchives(){
		return (File[]) this.classArchives.toArray(new File[this.classArchives.size()]);
	}

	/**
	 * 指定された名前を持つすべてのリソースを表す
	 * URL の Enumeration を返します。
	 * @param name リソース名
	 * @return リソースの URL の Enumeration
	 */
	protected synchronized Enumeration findResources(String name) throws IOException{
		URL url = this.findResource(name);		// URL 取得

		Vector list = new Vector();
		if(url != null){ list.add(url); }

		return list.elements();					// 結果の返却
	}

	/**
	 * 指定された名前を持つリソースを探します。
	 * @param name リソース名
	 * @return リソースを読み込むための URL。リソースが見つからなかった場合は null
	 */
	protected synchronized URL findResource(String name){
		// 過去の検索履歴からのアーカイブファイルの特定
		File aichiveFile = (File) this.resourceMap.get(name);
		if(aichiveFile != null){
			try{
				if(aichiveFile.isFile()){
					URL url = this.searchResourceURL(aichiveFile, name);
					if(url != null){ return url; }			// 結果の返却
				}
				else{
					File f = new File(aichiveFile, name);
					if(f.isFile()){ return f.toURL(); }		// 結果の返却
				}
			}
			catch(MalformedURLException murle){
				this.resourceMap.remove(name);
			}
			catch(IOException ioe){
				this.resourceMap.remove(name);
			}
		}

		return this.searchResourceURL(name);				// 検索だ！
	}

	/**
	 * リソースを検索します。
	 */
	private URL searchResourceURL(String filename){
		// ファイル検索
		Iterator cursor = this.classDirs.iterator();
		while(cursor.hasNext()){
			File parentDirectory = (File) cursor.next();
			File f = new File(parentDirectory, filename);
			if(f.isFile()){
				try{
					URL url = f.toURL();
					this.resourceMap.put(filename, parentDirectory);
					return url;
				}
				catch(MalformedURLException murle){
					return null;							// 未発見
				}
			}
		}

		// アーカイブファイルのチェック
		cursor = this.classArchives.iterator();
		while(cursor.hasNext()){
			try{
				URL url = this.searchResourceURL((File) cursor.next(), filename);
				if(url != null){ return url; }				// 返却
			}
			catch(IOException ioe){
				// 該当するファイルが見つからない
				continue;
			}
		}

		return null;		// 結局見つからなかった
	}

	/**
	 * 指定ディレクトリ以下の指定リソースファイルのデータを取得します。<br>
	 * ディレクトリ parent 以下に filename に該当するリソースファイルを
	 * 検索します。
	 * リソースファイルとは、クラスファイルやプロパティズファイルなどです。
	 * ファイルとして存在していない場合、parent 内の jar および zip ファイルの
	 * 検索はしません。
	 * サブディレクトリ以下の jar および zip ファイルの検索はしません。
	 * @param archiveFile アーカイブファイル
	 * @param filename ファイルパス
	 * @return リソースのＵＲＬ
	 * @throws IOException 入出力エラー
	 */
	private URL searchResourceURL(File archiveFile, String filename) throws IOException{
		if(archiveFile != null){
			if(archiveFile.isFile()){
				// 予備検索
				String archivePath = archiveFile.getAbsolutePath();

				// jar だっ・・・いや、zip かな？・・・どっちでもいいや！
				ZipFile zipFile = this.getZipFile(archivePath);

				// URL の context を作成
				URL context = new URL(("jar:").concat(archiveFile.toURL().toExternalForm()).concat("!/"));

				// エントリを順番に取り出してマッチング
				ZipEntry entry = zipFile.getEntry(filename);
				if(entry != null){
					// 該当者発見！！→キャッシュして返却
					resourceMap.put(filename, archiveFile);
					return new URL(context, filename);
				}
			}
		}

		// 収穫無し ＝ 該当のデータを発見できなかった
		throw new FileNotFoundException(filename);
	}

	/**
	 * 指定されたリソースを読み込む入力ストリームを返します。
	 * 検索順については、getResource(String) のドキュメントを参照してください。
	 * このメソッドは、まずスーパークラスの getResourceAsStream(String name) を
	 * 呼び出します。この結果が null だった場合、このオブジェクトに
	 * 設定されているリソースパス内を検索します。
	 * @param name リソース名
	 * @return リソースを読み込むための入力ストリーム。リソースが見つからなかった場合は null
	 */
/*
	public synchronized InputStream getResourceAsStream(String name){
		ClassLoader cl = this.getParent();
		InputStream in = cl != null ? cl.getResourceAsStream(name) : ClassLoader.getSystemResourceAsStream(name);

		if(in == null){
			if(name != null){
				File archiveFile = (File) this.resourceMap.get(name);
				if(archiveFile != null){
					try{
						if(archiveFile.isFile()){
							return this.searchResourceInputStream(archiveFile, name);
						}
						else{
							File f = new File(archiveFile, name);
							if(f.isFile()){ return new FileInputStream(f); }
						}
					}
					catch(IOException ioe){
						this.resourceMap.remove(name);
					}
				}

				// ファイル検索
				Iterator cursor = this.classDirs.iterator();
				while(cursor.hasNext()){
					File parentDirectory = (File) cursor.next();
					File f = new File(parentDirectory, name);
					if(f.isFile()){
						try{
							this.resourceMap.put(name, parentDirectory);
							return new FileInputStream(f);
						}
						catch(FileNotFoundException fnfe){
							continue;
						}
					}
				}

				// アーカイブファイルのチェック
				cursor = this.classArchives.iterator();
				while(cursor.hasNext()){
					try{
						return this.searchResourceInputStream((File) cursor.next(), name);
					}
					catch(IOException ioe){
						// 該当するファイルが見つからない
						continue;
					}
				}

				return null;
			}
		}

		return in;		// 結局、見つからなかった
	}
*/

	/**
	 * 指定ディレクトリ以下の指定リソースファイルのデータを取得します。<br>
	 * ディレクトリ parent 以下に filename に該当するリソースファイルを
	 * 検索します。
	 * リソースファイルとは、クラスファイルやプロパティズファイルなどです。
	 * ファイルとして存在していない場合、parent 内の jar および zip ファイルを
	 * 検索します。
	 * サブディレクトリ以下の jar および zip ファイルの検索はしません。
	 * @param archiveFile アーカイブファイル
	 * @param filename ファイルパス
	 * @return リソースの入力ストリーム
	 * @throws IOException 入出力エラー
	 */
/*
	private InputStream searchResourceInputStream(File archiveFile, String filename) throws IOException{
		if(archiveFile != null){
			if(archiveFile.isFile()){

				// jar だっ・・・いや、zip かな？・・・どっちでもいいや！
				String archivePath = archiveFile.getAbsolutePath();
				ZipFile zipFile = this.getZipFile(archivePath);

				// エントリを順番に取り出してマッチング
				ZipEntry entry = zipFile.getEntry(filename);
				if(entry != null){
					// 該当者発見！！→キャッシュして返却
					this.resourceMap.put(filename, archiveFile);
					return zipFile.getInputStream(entry);
				}
			}
		}

		// 収穫無し ＝ 該当のデータを発見できなかった
		throw new FileNotFoundException(filename);
	}
*/
	/**
	 * 指定のパスで表されるファイルを Zip として解凍するための
	 * ZipFile オブジェクトを返します。
	 * @param path パス
	 * @return ZipFile オブジェクト
	 * @throws ZipException ZIP 形式エラーが発生した場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	private ZipFile getZipFile(String path) throws ZipException, IOException{
//		Reference reference = (Reference) this.zipMap.get(path);
//		if(reference != null){
//			ZipFile zipFile = (ZipFile) reference.get();
//			if(zipFile != null){ return zipFile; }
//		}

		if(path.toLowerCase().endsWith(".jar")){
			return new JarFile(path);
//			JarFile jarFile = new JarFile(path);
//			this.zipMap.put(path, new SoftReference(jarFile));
//			return jarFile;
		}
		else{
			return new ZipFile(path);
//			ZipFile zipFile = new ZipFile(path);
//			this.zipMap.put(path, new SoftReference(zipFile));
//			return zipFile;
		}
	}


	/**
	 * アーカイブファイルを検索する場合に使われるフィルターインスタンス。
	 */
	private static FileFilter archiveFileFilter = new ArchiveSearchFilter();
	/**
	 * アーカイブファイルを検索する場合に使われるフィルタークラス。
	 */
	private static class ArchiveSearchFilter implements FileFilter{
		protected ArchiveSearchFilter(){
			super();
		}

		/**
		 * 指定された抽象パス名がパス名リストに含まれる必要があるかどうかを判定します。
		 * @param pathname テスト対象の抽象パス名
		 * @return pathname が含まれる必要がある場合は true
		 */
		public boolean accept(File pathname){
			if(pathname.isFile()){
				String name = pathname.getName().toLowerCase();
				return (name.endsWith(".jar") || name.endsWith(".zip"));
			}

			return false;		// 条件に該当しない
		}
	}
	/**
	 * 現在のログレベルを取得します。<br/>
	 * このログレベルは、クラスパスの追加に関するログレベルです。
	 * @return ログレベル
	 */
	public Logger.Level getLogLevel() {
		return logLevelForClassPath;
	}

	/**
	 * ログレベルを設定します。<br/>
	 * このログレベルは、クラスパスの追加に関するログレベルです。
	 * 
	 * @param logLevel ログレベル
	 */
	public void setLogLevel(Logger.Level logLevel) {
		this.logLevelForClassPath = logLevel;
	}
}

