package org.intra_mart.jssp.source.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


/**
 * ソースファイルのデータを持つクラスです。
 */
public class SourcePropertyHandler{
	/**
	 * 設定ファイルを検索する基準となるパス
	 */
	private File parentDirectory;

	/**
	 * 基準となるディレクトリ用のコンストラクタ。
	 * このコンストラクタで作成されたハンドラは、すべてのハンドラの基礎と
	 * なります。
	 */
	public SourcePropertyHandler(File path){
		super();
		this.parentDirectory = path;
	}

	/**
	 * このディレクトリに関する設定情報を返します。
	 * このディレクトリに設定ファイルが存在しない場合、
	 * 親ディレクトリに遡って設定を検索します。
	 * @param srcPath ソースパス
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public SourceProperties getProperties(File srcPath) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		String fileLabelName = srcPath.getName();
		int dot = fileLabelName.lastIndexOf('.');
		if(dot > 0){
			fileLabelName = fileLabelName.substring(0, dot);
		}
		String fileName = fileLabelName.concat(".properties");

		File dir = srcPath.getParentFile();
		File file = new File(dir, fileName);

		// ディレクトリ共通設定を取得
		GenericSourceProperties parentProperties = this.getGenericProperties(dir);

		if(file.exists()){
			if(file.isFile()){
				if(file.canRead()){
					FileInputStream in = new FileInputStream(file);
					try{
						return new PrivateSourceProperties(in, parentProperties);
					}
					finally{
						in.close();
					}
				}
			}
		}

		return parentProperties;
	}

	/**
	 * このディレクトリに関する設定情報を返します。
	 * このディレクトリに設定ファイルが存在しない場合、
	 * 親ディレクトリに遡って設定を検索します。
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private GenericSourceProperties getGenericProperties(File dir) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		FoundationSourceProperties fsp = FoundationSourceProperties.instance();

		File file = new File(dir, fsp.getSourcePropertyName());
		if(file.isFile()){
			InputStream in = new FileInputStream(file);
			try{
				if(! dir.equals(this.parentDirectory)){
					File parent = dir.getParentFile();
					if(parent != null){
						return new GenericSourceProperties(in, this.getGenericProperties(parent));
					}
				}

				return new GenericSourceProperties(in, FoundationSourceProperties.instance());
			}
			finally{
				in.close();
			}
		}
		else{
			File parent = dir.getParentFile();
			if(parent != null){
				return this.getGenericProperties(parent);
			}
			else{
				return FoundationSourceProperties.instance();
			}
		}
	}
}

