package org.intra_mart.jssp.script.provider;

import java.io.File;
import java.util.Locale;


import org.intra_mart.common.aid.jdk.java.lang.ExtendedClassLoader;
import org.intra_mart.jssp.util.ThreadContextClassLoader;
import org.intra_mart.jssp.util.config.HomeDirectory;
import org.intra_mart.jssp.util.config.JSSPConfigHandler;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;

/**
 * コンフィグファイル（ex. conf/jssp-config.xml）に設定されている値を元に、
 * クラスを検索するクラスローダです。
 */
public class JSSPClassLoaderImpl extends ExtendedClassLoader{

	/**
	 * コンストラクタ
	 * @param locale ロケール
	 */
	public JSSPClassLoaderImpl(Locale locale){
		super(new ThreadContextClassLoader());

		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		
		String[] classes = config.getClassPathDirectories(locale);		
		for(String value : classes){
			String path = value.trim();
			if(path.length() > 0){
				this.addClassPath(this.createFile(path));
			}			
		}		

		String[] archives = config.getClassArchives(locale);
		for(String value : archives){
			String path = value.trim();
			if(path.length() > 0){
				this.addClassArchive(this.createFile(path));
			}			
		}
		
		String[] libraries = config.getClassArchiveDirectories(locale);
		for(String value : libraries){
			String path = value.trim();
			if(path.length() > 0){
				this.addClassArchiveLibrary(this.createFile(path));
			}			
		}	
	}

	/**
	 * コンストラクタ
	 */
	public JSSPClassLoaderImpl(){
		super(new ThreadContextClassLoader());
		
		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		
		String[] classes = config.getGeneralClassPathDirectories();		
		for(String value : classes){
			String path = value.trim();
			if(path.length() > 0){
				this.addClassPath(this.createFile(path));
			}			
		}		

		String[] archives = config.getGeneralClassArchives();
		for(String value : archives){
			String path = value.trim();
			if(path.length() > 0){
				this.addClassArchive(this.createFile(path));
			}			
		}
		

		String[] libraries = config.getGeneralClassArchiveDirectories();
		for(String value : libraries){
			String path = value.trim();
			if(path.length() > 0){
				this.addClassArchiveLibrary(this.createFile(path));
			}			
		}
		
	}

	/**
	 * 指定のパス名を表す File オブジェクトを返します。
	 * path が絶対パスではない場合、
	 * {@link org.intra_mart.jssp.util.config.HomeDirectory}を親ディレクトリとしてパスを解決します。
	 * 
	 * @param path パス名
	 * @return 絶対パスを表す File オブジェクト
	 */
	private File createFile(String path){
		File file = new File(path);
		if(! file.isAbsolute()){
			file = new File(HomeDirectory.instance(), path);
		}
		return file;
	}
}

