package org.intra_mart.jssp.source.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.jssp.source.SourceFile;
import org.intra_mart.jssp.source.property.SourceProperties;
import org.intra_mart.jssp.source.property.SourcePropertyHandler;
import org.xml.sax.SAXException;


/**
 * ソースファイルを検索しロードするクラスです。
 */
public class SourceLoader{
	/**
	 * ソース検索の基準ディレクトリ
	 */
	private File parentDirectory = null;
	
	/**
	 * ソースの設定情報を読み込むハンドラ
	 */
	private SourcePropertyHandler propertyHandler = null;

	/**
	 * 新しいローダーを作成します。
	 */
	public SourceLoader(File dir){
		super();
		if(dir != null){
			this.parentDirectory = dir;
			this.propertyHandler = new SourcePropertyHandler(dir);
		}
		else{
			throw new NullPointerException("dir is null.");
		}
	}

	/**
	 * このローダがソースを検索する基準としているディレクトリを返します。
	 * @return ディレクトリを表す {@link java.io.File} オブジェクト
	 */
	public File getDirectory(){
		return this.parentDirectory;
	}

	/**
	 * 指定のソースを読み込んで返します。
	 * @param path ソースパス
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public final SourceFile loadSource(String path) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		// ファイルパスの決定
		File file = new File(this.getDirectory(), path);

		// ソースに関する設定情報の取得
		SourceProperties sourceProperties = propertyHandler.getProperties(file);

		return new SourceFile(file, sourceProperties);
	}
}


