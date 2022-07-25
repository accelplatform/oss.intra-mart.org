package org.intra_mart.jssp.script.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import org.intra_mart.common.aid.jdk.util.charset.AdvancedOutputStreamWriter;
import org.intra_mart.jssp.util.RuntimeObject;
import org.intra_mart.jssp.util.SimpleLog;
import org.intra_mart.jssp.util.config.HomeDirectory;
import org.intra_mart.jssp.util.config.JSSPConfigHandler;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * ファイル操作オブジェクト。<br/>
 * <br/>
 * ファイルを操作するＡＰＩを持つオブジェクトです。<br/>
 * このＡＰＩは、Web Application Serverが 動作しているマシンを対象としてファイル操作を行います。<br/>
 * 
 * @scope public
 * @name File
 */
public class FileAccessObject extends ScriptableObject implements Cloneable, java.io.Serializable{
	
	private static Object _lock = new Object();	// 同期化用のロックオブジェクト

	/**
	 * 相対パスを解決するときに使用する親ディレクトリ
	 */
	private static File _parentDir;
	private static Object _parentDirMonitor = new Object();
	
	/**
	 * 相対パスを解決するときに使用する親ディレクトリを取得します。<br/>
	 * ユニットテスト時にHomeDirectory.instance()を実行しないようにするためのメソッドとして用意しました。
	 * @return
	 */
	private static File getParentDir(){
		if(_parentDir == null){
			synchronized (_parentDirMonitor) {
				if(_parentDir == null){
					_parentDir  = HomeDirectory.instance();
				}
			}
		}
		
		return _parentDir;
	}
	
	private File fileInstance;

    /**
     * JSSP実行環境への登録用デフォルトコンストラクタ
     */
	public FileAccessObject(){
		super();
	}

    /**
     * JSSP実行環境下でのオブジェクト名称を取得します
     *
     * @return String JSSP実行環境下でのオブジェクト名称
     */
	public String getClassName() {
		return "File";
	}

	/**
	 * コンストラクタ。<br/>
	 * <br/>
	 * 指定のパスに対するファイル(ディレクトリ)操作を行うためのインスタンスを生成します。<br/>
	 * このコンストラクタで生成されたインスタンスが、
	 * 実際に操作する対象は、Web Application Server が動作しているマシン上のファイルになります。<br/>
	 * <br/>
	 * パスが相対パス形式で指定された場合、
	 * このコンストラクタは、JSSP実行環境のホームディレクトリを親として絶対パスを解決します。<br/>
	 * （JSSP実行環境のホームディレクトリのデフォルトはコンテキストパスに対応する実際のパスです）
	 * 
	 * @scope public
	 * @param path String ファイルパス
	 */
	public static Object jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr){
		if(inNewExpr){
			// new 演算子によりコール
			try{
				if(args.length == 1){
					return new FileAccessObject(ScriptRuntime.toString(args[0]));
				}
				if(args.length > 1){
					if(args[1] instanceof String){
						return new FileAccessObject(ScriptRuntime.toString(args[0]), (String) args[1]);
					}
					else{
						return new FileAccessObject(ScriptRuntime.toString(args[0]));
					}
				}
				return new FileAccessObject("");
			}
			catch(NullPointerException npe){
				return new FileAccessObject(ScriptRuntime.toString(Undefined.instance));
			}
		}
		else{
			// 関数指定
			return null;
		}
	}

	/**
	 * コンストラクタ。<br/>
	 * <br/>
	 * 指定のパスに対するファイル(ディレクトリ)操作を行うためのインスタンスを生成します。<br/>
	 * 実際にアクセスするファイルは、parent + child が表すファイルまたはディレクトリになります。<br/>
	 * このコンストラクタで生成されたインスタンスが、
	 * 実際に操作する対象は、Web Application Server が動作しているマシン上のファイルになります。<br/>
	 * 
	 * @scope public
	 * @param parent String 親ディレクトリパス
	 * @param child String ファイルパス
	 */
	public static Object jsConst_File(Context cx, Object[] args, Function ctorObj, boolean inNewExpr){
	    // 複数コンストラクタ用ダミー
	    return null;
	}
	
	/**
	 * プライベート・コンストラクタ
	 * @param pathname
	 * @throws NullPointerException
	 */
	private FileAccessObject(String pathname) {
		fileInstance = new File(pathname);
		if(! fileInstance.isAbsolute()){
			fileInstance = new File(getParentDir(), pathname);
		}
	}

	/**
	 * プライベート・コンストラクタ
	 * @param parent
	 * @param child
	 * @throws NullPointerException
	 */
	private FileAccessObject(String parent, String child) {
		fileInstance = new File(parent, child);
	}


	/**
	 * ディレクトリ一覧を取得します。<br/>
	 * <br/>
	 * コンストラクタで指定されたディレクトリ・パスに含まれる全てのディレクトリ名称を返却します。<br/>
	 * コンストラクタで指定されたパスが存在するディレクトリではなかった場合の動作は保証されません。<br/>
	 * 
	 * @scope public
	 * @return Array ディレクトリ一覧（配列型）
	 */
	public Object jsFunction_directories(){
		File[] members = fileInstance.listFiles();
		if(members != null){
			Set<String> directories = new TreeSet<String>();
			for(int idx = 0; idx < members.length; idx++){
				// ディレクトリのみ
				if(members[idx].isDirectory()){
					directories.add(members[idx].getName());	
				}
			}
			return RuntimeObject.newArrayInstance(directories.toArray());
		}
		else{
			return null;	// ディレクトリではないらしい
		}
	}

	/**
	 * ファイル一覧を取得します。<br/>
	 * <br/>
	 * コンストラクタで指定されたディレクトリ・パスに含まれる全てのファイル名称を返却します。<br/>
	 * コンストラクタで指定されたパスが存在するディレクトリではなかった場合の動作は保証されません。<br/>
	 * 
	 * @scope public
	 * @return Array ファイル一覧（配列型）
	 */
	public Object jsFunction_files(){
		File[] members = fileInstance.listFiles();
		if(members != null){
			Set<String> files = new TreeSet<String>();
			for(int idx = 0; idx < members.length; idx++){
				// ファイルのみ
				if(members[idx].isFile()){
					files.add(members[idx].getName());
				}
			}
			return RuntimeObject.newArrayInstance(files.toArray());
		}
		else{
			return null;	// ディレクトリではないらしい
		}
	}

	/**
	 * ファイル(ディレクトリ)の存在確認を行います。<br/>
	 * <br/>
	 * コンストラクタで指定されたパスが存在しているかどうかをチェックした結果を返却します。<br/>
	 * @scope public
	 * @return Boolean true : 存在する / false : 存在しない
	 */
	public boolean jsFunction_exist(){
		return fileInstance.exists();
	}

	/**
	 * ディレクトリの存在確認を行います。<br/>
	 * <br/>
	 * コンストラクタで指定されたパスがディレクトリとして存在しているかどうかをチェックした結果を返却します。<br/>
	 * 
	 * @scope public
	 * @return Boolean true : 存在する / false : 存在しない
	 */
	public boolean jsFunction_isDirectory(){
		return fileInstance.isDirectory();
	}

	/**
	 * ファイルの存在確認を行います。<br/>
	 * <br/>
	 * コンストラクタで指定されたパスがファイルとして存在しているかどうかをチェックした結果を返却します。<br/>
	 * 
	 * @scope public
	 * @return Boolean true : 存在する / false : 存在しない
	 */
	public boolean jsFunction_isFile(){
		return fileInstance.isFile();
	}

	
	/**
	 * ファイルの読み込み可否を判定します。
	 * 
	 * @scope public
	 * @return Boolean true : 読み込み可能 / false : 読み込み不可
	 */
	public boolean jsFunction_canRead(){
		return fileInstance.canRead();
	}

	
	/**
	 * ファイルの書き込み可否を判定します。
	 * 
	 * @scope public
	 * @return Boolean true : 書き込み可能 / false : 書き込み不可
	 */
	public boolean jsFunction_canWrite(){
		return fileInstance.canWrite();
	}

	
	/**
	 * ファイルサイズを取得します。<br/>
	 * <br/>
	 * このインスタンスの示すファイル・パスのファイルの大きさを返却します。<br/>
	 * 返却値の単位は、バイトです。<br/>
	 * このインスタンスの示すファイルパスが存在しない場合の動作は未定義です。<br/>
	 * 
	 * @scope public
	 * @return Number ファイルサイズ(バイト数)
	 */
	public double jsFunction_size(){
		return fileInstance.length();
	}

	
	/**
	 * ファイルの最終更新日付を取得します。<br/>
	 * <br/>
	 * コンストラクタで指定されたパスの最終更新時刻を返却します。<br/>
	 * 返却値は、GMT 1970 年 1 月 1 日からのミリ秒で返す。 <br/>
	 * 
	 * @scope public
	 * @return Number 最終更新日付（ミリ秒）
	 */
	public double jsFunction_lastModified(){
		return fileInstance.lastModified();
	}

	
	/**
	 * パス名を取得します。<br/>
	 * <br/>
	 * このインスタンスの示すパス名を返却します。<br/>
	 * 
	 * @scope public
	 * @return String パス名
	 */
	public String jsFunction_path(){
		return fileInstance.getAbsolutePath();
	}


	/**
	 * ファイルの内容を取得します。<br/>
	 * <br/>
	 * コンストラクタで指定されたファイル内のデータをストリームとして返します。<br/>
	 * ファイルの内容はバイナリとして扱われます。<br/>
	 * 
	 * @scope public
	 * @return String ファイルの内容
	 */
	public String jsFunction_load(){
		if(fileInstance.canRead()){
			try{
				// ファイルを読み込むためのインスタンスを生成
				FileInputStream fis = new FileInputStream(fileInstance);
				// ファイルのサイズをチェック
				int iFSize = (int) fileInstance.length();
				// 読込バッファを定義
				byte[] aBuf = new byte[iFSize];
				// データ読込
				fis.read(aBuf);
				fis.close();
				// データの整形
				try{
					return new String(aBuf, "8859_1");
				}
				catch(UnsupportedEncodingException uee){
					return new String(aBuf);
				}
			}
			catch(FileNotFoundException fnfe){
				return null;
			}
			catch(IOException ioe){
				return null;
			}
		}
		else{
			return null;
		}
	}


	/**
	 * テキストファイル内容を取得します。<br/>
	 * <br/>
	 * このインスタンスの示すファイルの内容を返却します。<br/>
	 * ファイルデータはテキストとして扱われます。<br/>
	 * ファイルデータは、読込後、自動的に Unicode に変換されます。<br/>
	 * 
	 * @scope public
	 * @return String ファイルの内容
	 */
	public String jsFunction_read(){
		if(fileInstance.canRead()){
			try{
				// ファイルを読み込むためのインスタンスを生成
				FileInputStream fis = new FileInputStream(fileInstance);
				// ファイルのサイズをチェック
				int iFSize = (int) fileInstance.length();
				// 読込バッファを定義
				byte[] aBuf = new byte[iFSize];
				// データ読込
				fis.read(aBuf);
				fis.close();
				// データの整形
				try{
					JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
					String enc = config.getServerCharacterEncoding();
					return new String(aBuf, enc);
				}
				catch(UnsupportedEncodingException uee){
					return new String(aBuf);
				}
			}
			catch(FileNotFoundException fnfe){
				return null;
			}
			catch(IOException ioe){
				return null;
			}
		}

		// 返却値の設定
		return null;
	}

	
	/**
	 * ファイルへのデータ出力を行います。<br/>
	 * <br/>
	 * このインスタンスの示すファイル・パスに対して引数に指定されたデータを書き出します。<br/>
	 * データはバイナリとして扱われます。<br/>
	 * 
	 * @scope public
	 * @param strm String データ
	 * @return Boolean true : 成功 / false : 失敗
	 */
	public boolean jsFunction_save(String strm){
		synchronized (_lock) {
			try{
				// ファイルを読み込むためのインスタンスを生成
				FileOutputStream fos = new FileOutputStream(fileInstance);
				// データ書出
				fos.write(strm.getBytes("8859_1"));
				fos.flush();
				fos.close();
				return true;
			}
			catch(UnsupportedEncodingException uee){
				return false;
			}
			catch(FileNotFoundException fnfe){
				return false;
			}
			catch(IOException ioe){
				return false;
			}
		}
	}

	
	/**
	 * テキストファイルの出力を行います。<br/>
	 * <br/>
	 * このインスタンスの示すファイル・パスに対して引数に指定されたデータを書き出します。<br/>
	 * データはテキストとして扱われます。<br/>
	 * 引数 strm には Unicode のデータを渡して下さい。<br/>
	 * ファイルに対して出力する際には、自動的にサーバ文字エンコーディング設定の文字コードに変換されます。<br/>
	 * 
	 * @scope public
	 * @param strm String テキストデータ
	 * @return Boolean true : 成功 / false : 失敗
	 */
	public boolean jsFunction_write(String strm){
		
		synchronized (_lock) {

			try{
				// ファイルを読み込むためのインスタンスを生成
				FileOutputStream fos = new FileOutputStream(fileInstance);
				try{
					// 出力の作成
					JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
					String enc = config.getServerCharacterEncoding();
					Writer out = new AdvancedOutputStreamWriter(fos, enc);
	
					// データ書出
					out.write(strm);
					out.flush();
				}
				finally{
					fos.close();
				}
				return true;
			}
			catch(Exception e){
				// TODO [OSS-JSSP] ログ機能実装
				SimpleLog.logp(Level.WARNING, "File write error: path=" + fileInstance.getAbsolutePath(), e);
				return false;
			}
			
		}
	}


	/**
	 * テキストのファイルへ追記を行います。<br/>
	 * <br/>
	 * ファイルに対して指定のテキストデータを追記します。<br/>
	 * 引数 strm へは Unicode 体系のテキストデータを渡して下さい。<br/>
	 * ファイルへの出力時に、サーバ文字エンコーディング設定で解決される文字コードへ変換をしてファイルに出力します。<br/>
	 * ファイルが存在しない場合には、新規にファイルを作成してデータ出力を行います。<br/>
	 * 
	 * @scope public
	 * @param strm String テキストデータ
	 * @return Boolean true : 成功 / false : 失敗
	 */
	public boolean jsFunction_append(String strm){
		
		synchronized (_lock) {

			try{
				// ファイルを読み込むためのインスタンスを生成(追記モード)
				FileOutputStream fos = new FileOutputStream(fileInstance.getAbsolutePath(), true);
				try{
					// 出力の作成
					JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
					String enc = config.getServerCharacterEncoding();
					Writer out = new AdvancedOutputStreamWriter(fos, enc);
	
					// データ書出
					out.write(strm);
					out.flush();
				}
				finally{
					fos.close();
				}
				return true;
			}
			catch(Exception e){
				// TODO [OSS-JSSP] ログ機能実装
				SimpleLog.logp(Level.WARNING, "File write error: path=" + fileInstance.getAbsolutePath(), e);

				// 登録失敗			
				return false;
			}

		}
		
	}

	
	/**
	 * ディレクトリを作成します。<br/>
	 * <br/>
	 * コンストラクタで指定されたパスをディレクトリとして新規作成します。<br/>
	 * 存在していないが必要な親ディレクトリも一緒に作成されます。<br/>
	 * このオペレーションが失敗した場合でも、いくつかの必要な親ディレクトリの作成には成功した場合があります。<br/>
	 * 
	 * @scope public
	 * @return Boolean true : 成功 / false : 失敗
	 */
	public boolean jsFunction_makeDirectories(){
		synchronized (_lock) {
			return fileInstance.mkdirs();
		}
	}

	
	/**
	 * ディレクトリを作成します。<br/>
	 * <br/>
	 * コンストラクタで指定されたパスをディレクトリとして新規作成します。<br/>
	 * 
	 * @scope private
	 * @return Boolean true : 成功 / false : 失敗
	 */
	public boolean jsFunction_makeDirectory(){
		synchronized (_lock) {
			return fileInstance.mkdir();
		}
	}

	
	/**
	 * ファイル名を変更します。<br/>
	 * <br/>
	 * このFile オブジェクトを、引数 newFile が示すファイル名に変更します。<br/>
	 * 引数 newFile には、File オブジェクトのインスタンスを渡して下さい。<br/>
	 * 
	 * @scope public
	 * @param newFile File 変更後のファイル名を表す File オブジェクト
	 * @return Boolean true : 成功 / false : 失敗
	 */
	public boolean jsFunction_move(Object newFile){
		
		synchronized (_lock) {
			
			try{
				if(newFile instanceof FileAccessObject){
					return this.fileInstance.renameTo( ((FileAccessObject) newFile).getFileInstance() );
				}
				else if(newFile instanceof String){
					FileAccessObject destFile = new FileAccessObject((String) newFile);
					return this.fileInstance.renameTo(destFile.getFileInstance());					
				}
				else{
					return false;
				}
			}
			catch(NullPointerException npe){
				return false;
			}

		}
	}
	
	/**
	 * ファイル名を変更します。<br/>
	 * <br/>
	 * このFileオブジェクトが示すファイルの名前を、引数 newPath に変更します。<br/>
	 * 
	 * 引数 newPath は、絶対パス形式、
	 * または、JSSP実行環境のホームディレクトリからの相対パス形式で指定します。
	 * 
	 * @scope public
	 * @param newPath String ファイルパス
	 * @return Boolean true : 成功 / false : 失敗
	 */
	public boolean move(String newPath){
        // APIリスト用 
		return false;
	}

	private File getFileInstance(){
		return this.fileInstance;
	}

	
	/**
	 * ファイルまたはディレクトリを削除します。<br/>
	 * <br/>
	 * このインスタンスの示すパスを削除します。<br/>
	 * 
	 * @scope public
	 * @return Boolean true : 成功 / false : 失敗
	 */
	public boolean jsFunction_remove(){
		synchronized (_lock) {		
			return fileInstance.delete();
		}
	}

}