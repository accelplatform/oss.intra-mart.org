package org.intra_mart.jssp.util.config;

import java.io.File;
import java.util.logging.Level;

import org.intra_mart.common.aid.jdk.java.io.file.ExtendedDirectory;
import org.intra_mart.jssp.util.SimpleLog;


/**
 * このクラスは、システムのホームディレクトリに関する機能を
 * 提供します。
 */
public class HomeDirectory extends ExtendedDirectory{
	
	private static String _homeDirectoryString;
	private static HomeDirectory _instance;

	/**
	 * ホームディレクトリを表すオブジェクトを取得します。
	 * @return ホームディレクトリを表す File クラスのインスタンス
	 */
	public static synchronized HomeDirectory instance(){
		if(_instance == null){
			_instance = new HomeDirectory();
		}
		return _instance;
	}

	/**
	 * システムのホームディレクトリを設定します。
	 * 
	 * @param path ホームディレクトリを表すパス名
	 * @throws NullPointerException 引数が null の場合
	 * @deprecated このメソッドは、システム全体を不安定にしてしまう危険性があるため、通常は使用してはいけません。
	 */
	public static void definePath(String path){
		
		if(path == null){
			throw new NullPointerException("path is null"); 
		}

		// ファイルインスタンスの生成
		_instance = new HomeDirectory(path.trim());
	}

	/**
	 * システムのホームディレクトリを返します。<br>
	 * 通常は、インストールされた環境と設定値から自動的にホームディレクトリが決定されます。
	 * <br>例外として、definePath() メソッドを使用して
	 * 意図的にホームディレクトリの情報を書き換えた場合は、
	 * 書き換えられたパス情報が返されます。
	 * 
	 * @return ホームディレクトリを表すパス名
	 */
	public static String findPath(){
		return _homeDirectoryString;
	}

	/**
	 * 唯一のコンストラクタ。
	 * 他のクラスからインスタンス化されないように隠蔽されています。
	 * このコンストラクタは、このクラス内でしか利用できません。
	 * これにより、このクラスのインスタンス化はこのクラス内でしか行えません。
	 */
	private HomeDirectory(){
		this(_homeDirectoryString);
	}

	/**
	 * 唯一のコンストラクタ。
	 * 他のクラスからインスタンス化されないように隠蔽されています。
	 * このコンストラクタは、このクラス内でしか利用できません。
	 * これにより、このクラスのインスタンス化はこのクラス内でしか行えません。
	 * @param path ホームディレクトリを表すパス名
	 */
	private HomeDirectory(String path){
		
		// 親コンストラクタ呼出
		super(path, 64);
		
		// パス文字列の保存
		_homeDirectoryString = this.getAbsolutePath().replace(File.separatorChar, '/');
		
		// 外部に情報を通知
		// TODO [OSS-JSSP] ログ機能実装
		SimpleLog.logp(Level.INFO, "System Home Directory: " + this.getAbsolutePath());	
	}
}
