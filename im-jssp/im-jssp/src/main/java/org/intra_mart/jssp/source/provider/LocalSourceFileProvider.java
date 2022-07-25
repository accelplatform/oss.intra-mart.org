package org.intra_mart.jssp.source.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.jssp.source.SourceFile;
import org.intra_mart.jssp.util.config.HomeDirectory;
import org.xml.sax.SAXException;


/**
 * ソースファイルのデータを持つクラスです。
 */
public class LocalSourceFileProvider implements SourceFileProvider{
	
	/**
	 * ソースを読み込むオブジェクトを保管します。
	 */
	private static Map<String, LocalSourceFileProvider> sourceProviderMap = new HashMap<String, LocalSourceFileProvider>();

	/**
	 * 指定のディレクトリからソースを検索するためのオブジェクトを返します。
	 * 
	 * @param dir ソース検索の基準ディレクトリ名
	 * @return
	 */
	public static synchronized LocalSourceFileProvider getInstance(String dir){
		LocalSourceFileProvider manager = (LocalSourceFileProvider) sourceProviderMap.get(dir);
		if(manager == null){
			manager = new LocalSourceFileProvider(dir);
			sourceProviderMap.put(dir, manager);
		}

		return manager;
	}

	/**
	 * ソース検索の基準ディレクトリからソースファイルを検索するSourceLoader
	 */
	private SourceLoader sourceLoader = null;

	/**
	 * ソースを読み込むオブジェクトを新しく作成します。
	 * @param rootDirPath ソースを読み込む親ディレクトリ
	 */
	private LocalSourceFileProvider(String rootDirPath){
		super();

		File file = new File(rootDirPath);
		if(! file.isAbsolute()){
			file = new File(HomeDirectory.instance(), rootDirPath);
		}

		this.sourceLoader = new SourceLoader(file);
	}

	/**
	 * ソース検索の基準ディレクトリを返却します。
	 */
	public File getParentDirectory(){
		return this.sourceLoader.getDirectory();
	}

	/**
	 * ソースを読み込んで返します。
	 * 
	 * @param path ソースのパス
	 * @return ソースデータ
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public SourceFile getSourceFile(String path) throws FileNotFoundException, IOException{
		try {
			return this.sourceLoader.loadSource(path);
		}
		catch(ParserConfigurationException pce){
			IOException ioe = new IOException("Configuration file read error.");
			ioe.initCause(pce);
			throw ioe;
		}
		catch(SAXException saxe){
			IOException ioe = new IOException("Configuration file read error.");
			ioe.initCause(saxe);
			throw ioe;
		}
	}
}

