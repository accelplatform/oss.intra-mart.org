package org.intra_mart.common.aid.jdk.java.lang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.intra_mart.common.platform.log.Logger;


/**
 * このクラスは、拡張されたクラスローダーです。<P>
 * <BR>
 * このクラスローダーは、システムのホームディレクトリおよび
 * 現在のカレントディレクトリを各リソースの検索パスとします。
 * また検索パスがディレクトリであった場合、そのディレクトリ内にある
 * jar または zip ファイル内もリソース検索の対象とします。<br>
 * jar または zip 内を検索する場合、そのアーカイブ内の jar または zip 内も
 * リソースファイルの検索対象としますので、複数の jar アーカイブを
 * 一つの zip ファイルにまとめておくこともできます。<p>
 *
 */
public class LuxuryClassLoader extends ExtendedClassLoader{
	
	private static Logger _logger = Logger.getLogger();
/*
	public static void main(String[] args){
		try{
			File dir = new File("E:/work/resin-3.0.9/work");
			LuxuryClassLoader lc = new LuxuryClassLoader(dir);

			String path ="E:/work/resin-3.0.9";
			File file = new File(path);
			lc.addClassArchiveLibrary(file);

			long start = System.currentTimeMillis();
			java.net.URL url = lc.getResource("com/caucho/server/resin/Resin.class");
			long end = System.currentTimeMillis();
			System.out.println("URL=" + url);
			System.out.println("Time=" + (end - start));
			Thread.sleep(5000);
			System.gc();
			Thread.sleep(1000);
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}
*/


	/**
	 * アーカイブ(jar or zip)を展開するディレクトリ
	 */
	private File workDir = null;

	/**
	 * このクラスをロードした ClassLoader を親クラスローダとして使用し、
	 * 新しいクラスローダを作成します。<br>
	 * 指定されたパス名をクラスパスとして追加されたアーカイブファイルを
	 * 操作するための一時領域として使用します。
	 * @param path テンポラリとして利用するディレクトリパス
	 */
	public LuxuryClassLoader(File path){
		this(LuxuryClassLoader.class.getClassLoader(), path);
	}

	/**
	 * 指定された親クラスローダを使って、
	 * 委譲のために新しいクラスローダを作成します。<br>
	 * 指定されたパス名をクラスパスとして追加されたアーカイブファイルを
	 * 操作するための一時領域として使用します。
	 * @param parent 親クラスローダー
	 * @param path アーカイブ(jar or zip)を展開するためのディレクトリ
	 */
	public LuxuryClassLoader(ClassLoader parent, File path){
		super(parent);

		if(path == null){
			throw new NullPointerException("path is null");
		}

		if(path.exists()){
			if(path.isFile()){
				// パスはファイルとして存在しています。
				throw new IllegalArgumentException("path is a File:" + path);
			}
		}
		else{
			if(! path.mkdirs()){
				// ディレクトリの作成に失敗しました。
				throw new IllegalStateException("Failed to create directory :" + path);
			}
		}

		this.workDir = path;
	}

	/**
	 * クラスパスに指定のアーカイブファイルを追加します。
	 * ここで追加されるクラスパスは、このクラスローダー内でのみ有効です。<br>
	 * クラスローダーは、最も最近に追加されたパスから順に
	 * クラスを検索していきます。
	 * @param path 追加するパス
	 */
	public synchronized void addClassArchive(File path){
		String archivePath = path.getAbsolutePath();
		try {
			ZipFile zipFile = this.getZipFile(archivePath);

			Enumeration cursor = zipFile.entries();
			while(cursor.hasMoreElements()){
				ZipEntry entry = (ZipEntry) cursor.nextElement();
				if(! entry.isDirectory()){
					String entryName = entry.getName();

					String filePath = entryName.toLowerCase();
					if(filePath.endsWith(".jar") || filePath.endsWith(".zip")){
						try{
							InputStream in = zipFile.getInputStream(entry);
							try{
								File entryPath = new File(entryName);
								File file = new File(this.workDir, entryPath.getName());
//								try{
									if( _logger.isEnabled(this.getLogLevel()) ){
										_logger.log(this.getLogLevel(), archivePath + "!/" + entryName + "->" + file.getAbsolutePath());
									}
									this.createArchiveFile(in, file);
									this.addClassArchive(file);	// 再帰登録
//								}
//								finally{
//									// 仮想マシン終了時->ファイル自動削除
//									file.deleteOnExit();
//								}
							}
							finally {
								try{
									in.close();
								}
								catch(IOException e){
									// 実行に影響がないため無視。
								}
							}
						}
						catch(IOException ioe){
							IOException e = new IOException("File I/O error: " + entryName);
							e.initCause(ioe);
							throw e;
						}
					}
				}
			}
		}
		catch(ZipException ioe){
			IllegalArgumentException iae = new IllegalArgumentException("Failed to decompress file:" + path);
			iae.initCause(ioe);
			throw iae;
		}
		catch(IOException ioe){
			IllegalStateException ise = new IllegalStateException("File I/O error: " + path);
			ise.initCause(ioe);
			throw ise;
		}

		super.addClassArchive(path);
	}

	/**
	 * 指定のパスで表されるファイルを Zip として解凍するための
	 * ZipFile オブジェクトを返します。
	 * @param path パス
	 * @return ZipFile オブジェクト
	 * @throws ZipException ZIP 形式エラーが発生した場合
	 * @throws IOException 入出力エラーが発生した場合
	 */
	private ZipFile getZipFile(String path) throws ZipException, IOException{
		if(path.toLowerCase().endsWith(".jar")){
			return new JarFile(path);
		}
		else{
			return new ZipFile(path);
		}
	}

	/**
	 * 指定の入力ストリームからデータを取得してアーカイブファイルを生成します。<br>
	 *
	 * @param in アーカイブ(jar or zip)中の入力ストリーム
	 * @param path 出力先のパス
	 * @see java.io.File
	 * @see java.io.OutpurStream
	 */
	private void createArchiveFile(InputStream in, File path) throws IOException {
		// 入力ストリームから取得するデータを出力するファイルを生成
		OutputStream out = new FileOutputStream(path);
		try{
			byte[] bs = new byte[8192];		// バッファは 8[KByte] にしよう
			int len;
			while(true){
				len = in.read(bs);
				if(len != -1){
					out.write(bs, 0, len);
				} else {
					break;//読み込み終了
				}
			}
		}
		finally{
			out.close();
		}
	}
}

