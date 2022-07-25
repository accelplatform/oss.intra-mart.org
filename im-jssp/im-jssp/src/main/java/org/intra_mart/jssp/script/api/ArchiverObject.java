package org.intra_mart.jssp.script.api;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

import org.intra_mart.common.aid.jdk.java.util.Archiver;
import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.jssp.util.RuntimeObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * ファイルを圧縮／解凍するためのオブジェクトです。<br/>
 * 
 * @scope public
 * @name Archiver
 * @since 7.1
 */
public class ArchiverObject extends ScriptableObject {
	
	public static final String CLASS_NAME = "Archiver";
	
	private static final String NON_CONVERT_CHARSET = "ISO-8859-1";
	private static String BASE_LOGGER_NAME = ArchiverObject.class.getName();
	private static Logger logger = Logger.getLogger(BASE_LOGGER_NAME);
	private static boolean _tempFileDeleteFlag = true;

	static{
		String flag = System.getProperty(ArchiverObject.class.getName() + "._tempFileDeleteFlag", "true");
		_tempFileDeleteFlag = Boolean.parseBoolean(flag);
		
		if(_tempFileDeleteFlag == false){
			// 明示的に設定した場合（＝falseに設定）のみINFOで出力
			logger.info("_tempFileDelete: {}", _tempFileDeleteFlag);
		}
		else{
			logger.trace("_tempFileDelete: {}", _tempFileDeleteFlag);
		}
	}

	/* (non-Javadoc)
	 * @see jp.co.intra_mart.system.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName() {
		return ArchiverObject.CLASS_NAME;
	}

	/**
	 * 指定されたファイルやディレクトリをZIP形式で圧縮します。<br/>
	 * <br/>
	 * 引数オブジェクトは以下の形式です。<br/>
	 * <br/>
	 * <b>【注意】</b><br/>
	 * 圧縮ファイル内のファイル名は「UTF-8」で扱われます。<br/>
	 * （そのため、ファイル名が「UTF-8」に対応していないOSやアーカイバを利用した場合、文字化けが発生します）<br/>
	 * 
	 * <table border="1">
	 * 	<tr>
	 * 		<th>プロパティ</th>
	 * 		<th>型</th>
	 * 		<th>説明</th>
	 * 		<th>必須</th>
	 * 	</tr>
	 * 	<tr>
	 * 		<th rowspan="3">src</th>
	 * 		<td>File</td>
	 * 		<td>
	 * 			圧縮するファイル、または、ディレクトリのパスを示すFileオブジェクトを指定します。
	 * 		</td>
	 * 		<td rowspan="3">
	 * 			Yes。３つのうちどちらか１つ。
	 * 		</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>String(バイナリ)</td>
	 * 		<td>
	 * 			圧縮するファイルの内容をバイナリ形式の文字列で指定します。<br/>
	 * 			バイナリ形式の文字列とは、File#load()やRequestParameter#getValueAsStream()の返却値と同じ形式です。
	 * 		</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>Array<br/>（配列要素の型 が File）</td>
	 * 		<td>
	 * 			圧縮するファイル、または、ディレクトリのパスを示すFileオブジェクトを要素とする配列を指定します。<br/>
	 * 			（配列要素が、Fileで統一されていない場合の動作は未定義です）
	 * 		</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<th>srcFileName</th>
	 * 		<td>String</td>
	 * 		<td>
	 * 			srcプロパティが String(バイナリ) 形式で指定された際の、圧縮後ファイル名を指定します。<br/>
	 * 			なお、srcプロパティが String(バイナリ) 形式で指定されなかった場合、このプロパティは無視されます。
	 * 		</td>
	 * 		<td>srcプロパティが String(バイナリ) 形式で指定された場合は必須。</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<th rowspan="1">dest</th>
	 * 		<td>File</td>
	 * 		<td>
	 * 			ZIPファイルの出力先パスを示すFileオブジェクトを指定します。<br/>
	 * 			出力先のパスにファイルやディレクトリが既に存在する場合、それらを削除してから圧縮を行います。<br/>
	 * 		</td>
	 * 		<td rowspan="1">
	 * 			No。<br/>
	 * 			このプロパティが未指定の場合、作成されたZIPファイルの内容はファイルに出力されません。<br/>
	 * 			作成されたZIPファイルの内容は、String(バイナリ)として返却されます。
	 * 		</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<th>filter</th>
	 * 		<td>Function</td>
	 * 		<td>
	 * 			圧縮するファイルのフィルタリングを行うための関数を指定します。<br/>
	 * 			<br/>
	 * 			Archiverオブジェクトは、ディレクトリの圧縮処理を行う際、<br/>
	 * 			そのディレクトリ配下に含まれるファイルやディレクトリを圧縮するか否かを判定するために、この関数を実行します。<br/>
	 * 			この関数の引数には、圧縮処理を行おうとしているファイルやディレクトリを示すFileオブジェクトが渡されます。<br/>
	 * 			<br/>
	 * 			この関数が<b>falseを返却した場合</b>、該当ファイルは<b>圧縮対象から外れます</b>。<br/>
	 * 			上記以外の場合、該当ファイルは圧縮対象となります。<br/>
	 * 			<br/>
	 * 			このプロパティが未指定の場合、または、null、undefinedが指定された場合は、すべてのファイルが圧縮対象となります。<br/>
	 * 			このプロパティに指定された関数は、<b>src</b>プロパティに「Fileオブジェクト」、または、「Array（配列要素の型 が File）」が指定された時だけ有効となります。<br/>
	 * 		</td>
	 * 		<td>No。</td>
	 * 	</tr>
	 *  </table>
	 *  
	 * <h3>サンプルコード</h3>
	 * 以下のサンプルコードでは、<br/>
	 * 「<b>src/foo/</b>」ディレクトリ配下の、ファイル名に「<b>.txt</b>」が含まれるファイルを、<br/>
	 * 「<b>dest/bar.zip</b>」ファイルとしてZIP圧縮します。<br/>
	 * 変数 <b>result</b> には「<b>dest/bar.zip</b>」を示す新しいFileオブジェクトが格納されます。<br/>
	 * 
	 * <pre>
	 * var result = Archiver.zip(
	 *                  {
	 *                      src    : new File("src/foo/"),
	 *                      dest   : new File("dest/bar.zip"),
	 *                      filter : function(target){
	 *                          if(target.isDirectory() || target.path().indexOf(".txt") != -1){
	 *                              return true;
	 *                          }
	 *                          else{
	 *                              return false;
	 *                          }
	 *                      }
	 *                  }
	 *              );
	 * </pre>
	 * 
	 * @scope public
	 * @param arg Object 引数オブジェクト
	 * @return Object 引数オブジェクトの<b>dest</b>プロパティにFileオブジェクトを指定した場合は、ZIPファイルの出力先パスを示すFileオブジェクトが返却されます。<br/>
	 * 				  上記以外の場合は、作成されたZIPファイルの内容が、String(バイナリ)として返却されます。
	 */
	public static Object zip(Object arg){
		// APIリスト用ダミー
		return null;
	}
	
	/**
	 * 関数「zip()」の実体
	 */
	public static Object jsStaticFunction_zip(final Context cx, final Scriptable thisObj, final Object[] args, final Function funObj) throws IOException {

		try{
			//=======================================
			// 引数チェック
			//=======================================
			if(!(args[0] instanceof Scriptable)){
				throw new IllegalArgumentException("The argument have to be JavaScript Object.");
			}
			
			String prop4src         = "src";
			String prop4srcFileName = "srcFileName";
			String prop4dest        = "dest";
			String prop4filter      = "filter";
			
			Scriptable arg = (ScriptableObject)args[0];
			Object arg4src         = arg.get(prop4src, arg);
			Object arg4srcFileName = arg.get(prop4srcFileName, arg);
			Object arg4dest        = arg.get(prop4dest, arg);
			Object arg4filter      = arg.get(prop4filter, arg);
			
			//------------------------
			// 入力
			//------------------------
			String srcString = null;
			String fileName4srcString = null;		
			File srcFile = null;
			List<File> srcFileList = new ArrayList<File>();
			
			if(arg4src == ScriptableObject.NOT_FOUND || arg4src == Undefined.instance ){
				throw new IllegalArgumentException("Argument '" + prop4src + "' have to be specified.");
			}
			else{
				if(arg4src instanceof String){
					srcString = (String)arg4src;
					
					if(arg4srcFileName != ScriptableObject.NOT_FOUND){
						fileName4srcString = (String)arg4srcFileName;
					}
					else{
						throw new IllegalArgumentException("Argument '" + prop4srcFileName + "' have to be specified.");
					}
				}
				else if(arg4src instanceof FileAccessObject){
					srcFile = new File( ((FileAccessObject)arg4src).jsFunction_path() );
				}
				else if(arg4src instanceof NativeArray){
					NativeArray jsArray = (NativeArray) arg4src;
					for(int idx = 0, max = (int)jsArray.getLength(); idx < max; idx++){
						Object obj = jsArray.get(idx, jsArray);
						if(obj instanceof FileAccessObject){
							File f = new File( ((FileAccessObject)obj).jsFunction_path() );
							srcFileList.add(f);
						}
					}
				}
				else{
					throw new IllegalArgumentException("Argument '" + prop4src + "' is unexpected type.");
				}
			}
			
			//------------------------
			// 出力
			//------------------------
			File zipFile = null;
			
			if(arg4dest != ScriptableObject.NOT_FOUND){
				if( arg4dest instanceof FileAccessObject ){
					zipFile = new File(((FileAccessObject)arg4dest).jsFunction_path());
				}
				else{
					throw new IllegalArgumentException("Argument '" + prop4dest + "' is unexpected type.");
				}
				
			}
	
			//------------------------
			// FileFilter
			//------------------------
			FileFilter filter = null;
			
			if(arg4filter != ScriptableObject.NOT_FOUND){
				if( arg4filter instanceof Function ){
					filter = new FileFilter4JavaScriptFunction((Function)arg4filter);
				}
				else if( arg4filter instanceof NativeJavaObject
						 &&
						 ((NativeJavaObject)arg4filter).unwrap() instanceof FileFilter ){
					NativeJavaObject javaObj = (NativeJavaObject)arg4filter;
					filter = (FileFilter) javaObj.unwrap();
				}
				else{
					throw new IllegalArgumentException("Argument '" + prop4filter + "' is unexpected type: " + arg4filter.getClass());
				}
			}
			
			
			//=======================================
			// Caseチェック → 圧縮実行！
			//=======================================
			if(srcFile != null){
				File src = srcFile;
				
				// 空のディレクトリを圧縮しようとしている場合
				if(src.isDirectory()
				   && 
				   src.list().length == 0) {
					
					logger.warn("skipping zip archive because no files were included: {}", src.getAbsolutePath());
					
					if(zipFile != null){
						return createFileAccessObject(zipFile);
					}
					else{
						return Undefined.instance;
					}
				}

				//------------------------
				// Zip-Case-1
				//------------------------
				if(zipFile != null){
					if(logger.isTraceEnabled()){
						String caseName = "Zip-Case-1";
						Logger innerLogger = Logger.getLogger(BASE_LOGGER_NAME + "." + caseName);
						innerLogger.trace("--------------------------------------");
						innerLogger.trace("src:  {}", src.getCanonicalPath());
						innerLogger.trace("dest: {}", zipFile.getCanonicalPath());
						innerLogger.trace("--------------------------------------");
					}
					
					// 圧縮
					Archiver.zip(src, zipFile, filter);
					
					// 結果返却
					return createFileAccessObject(zipFile);
				}
				//------------------------
				// Zip-Case-3
				//------------------------
				else{
					if(logger.isTraceEnabled()){
						String caseName = "Zip-Case-3";
						Logger innerLogger = Logger.getLogger(BASE_LOGGER_NAME + "." + caseName);
						innerLogger.trace("--------------------------------------");
						innerLogger.trace("src:  {}", src.getCanonicalPath());
						innerLogger.trace("dest: {}", "String(Binary)...");
						innerLogger.trace("--------------------------------------");
					}

					// 一時ファイルを作成
					File tempZipFileOnAppRuntime = createTempFileOnAppRuntime(".zip");
					
					// 圧縮
					Archiver.zip(src, tempZipFileOnAppRuntime, filter);
					byte[] bytes = load(tempZipFileOnAppRuntime);
					
					// 一時ファイルを削除
					deleteTempFileOnAppRuntime(tempZipFileOnAppRuntime);

					// 結果返却
					return new String(bytes, NON_CONVERT_CHARSET);
				}
			}
			
			else if(srcString != null){
				byte[] binary = srcString.getBytes(NON_CONVERT_CHARSET);
				
				//------------------------
				// Zip-Case-7
				//------------------------
				if(zipFile != null){
					if(logger.isTraceEnabled()){
						String caseName = "Zip-Case-7";
						Logger innerLogger = Logger.getLogger(BASE_LOGGER_NAME + "." + caseName);
						innerLogger.trace("--------------------------------------");
						innerLogger.trace("src:  {}", "String(Binary)...");
						innerLogger.trace("dest: {}", zipFile.getCanonicalPath());
						innerLogger.trace("--------------------------------------");
					}

					// 圧縮
					Archiver.zip(binary, fileName4srcString, zipFile);
					
					// 結果返却
					return createFileAccessObject(zipFile);
				}
				//------------------------
				// Zip-Case-9
				//------------------------
				else{
					if(logger.isTraceEnabled()){
						String caseName = "Zip-Case-9";
						Logger innerLogger = Logger.getLogger(BASE_LOGGER_NAME + "." + caseName);
						innerLogger.trace("--------------------------------------");
						innerLogger.trace("src:  {}", "String(Binary)...");
						innerLogger.trace("dest: {}", "String(Binary)...");
						innerLogger.trace("--------------------------------------");
					}

					// 一時ファイルを作成
					File tempZipFileOnAppRuntime = createTempFileOnAppRuntime(".zip");
					
					// 圧縮
					Archiver.zip(binary, fileName4srcString, tempZipFileOnAppRuntime);
					byte[] bytes = load(tempZipFileOnAppRuntime);
					
					// 一時ファイルを削除
					deleteTempFileOnAppRuntime(tempZipFileOnAppRuntime);

					// 結果返却
					return new String(bytes, NON_CONVERT_CHARSET);
				}
			}
			
			else if(srcFileList.size() != 0){
				File[] sources = srcFileList.toArray(new File[srcFileList.size()]);
				
				//------------------------
				// Zip-Case-10
				//------------------------
				if(zipFile != null){
					if(logger.isTraceEnabled()){
						String caseName = "Zip-Case-10";
						Logger innerLogger = Logger.getLogger(BASE_LOGGER_NAME + "." + caseName);
						innerLogger.trace("--------------------------------------");
						for(int idx = 0; idx < sources.length; idx++){
							innerLogger.trace("src[{}]: {}", idx, sources[idx].getCanonicalFile());
						}
						innerLogger.trace("dest:   {}", zipFile.getCanonicalPath());
						innerLogger.trace("--------------------------------------");
					}

					// 圧縮
					Archiver.zip(sources, zipFile, filter);
					
					// 結果返却
					return createFileAccessObject(zipFile);
				}
				//------------------------
				// Zip-Case-12
				//------------------------
				else{
					if(logger.isTraceEnabled()){
						String caseName = "Zip-Case-12";
						Logger innerLogger = Logger.getLogger(BASE_LOGGER_NAME + "." + caseName);
						innerLogger.trace("--------------------------------------");
						for(int idx = 0; idx < sources.length; idx++){
							innerLogger.trace("src[{}]: {}", idx, sources[idx].getCanonicalFile());
						}
						innerLogger.trace("dest:   {}", "String(Binary)...");
						innerLogger.trace("--------------------------------------");
					}

					// 一時ファイルを作成
					File tempZipFileOnAppRuntime = createTempFileOnAppRuntime(".zip");
					
					// 圧縮
					Archiver.zip(sources, tempZipFileOnAppRuntime, filter);
					byte[] bytes = load(tempZipFileOnAppRuntime);
					
					// 一時ファイルを削除
					deleteTempFileOnAppRuntime(tempZipFileOnAppRuntime);

					// 結果返却
					return new String(bytes, NON_CONVERT_CHARSET);
				}
			}
			
			
			else{
				throw new IllegalStateException("Argument '" + prop4src + "'(=" + arg4src + ") is illegal.");
			}

		}
		catch(Exception e){
			throw Context.throwAsScriptRuntimeEx(e);
		}
	}

	
	/**
	 * @param file
	 * @return
	 */
	private static FileAccessObject createFileAccessObject(File file) {
		FileAccessObject dest = 
			(FileAccessObject) RuntimeObject.newObject("File", new Object[] { file.getAbsolutePath() });
		return dest;
	}

	
	/**
	 * @param src
	 * @return
	 * @throws IOException
	 */
	private static byte[] load(File src) throws IOException {
		FileInputStream fis = new FileInputStream(src);
		int iFSize = (int) src.length();
		byte[] aBuf = new byte[iFSize];
		fis.read(aBuf);
		fis.close();
		return aBuf;
	}

	
	/**
	 * ZIPファイルを解凍します。
	 * <br/>
	 * 引数オブジェクトは以下の形式です。<br/>
	 * <br/>
	 * <b>【注意】</b><br/>
	 * 圧縮ファイル内のファイル名は「UTF-8」で扱われます。<br/>
	 * （そのため、ファイル名が「UTF-8」に対応していないOSやアーカイバを利用した場合、文字化けが発生します）<br/>
	 * 
	 * <table border="1">
	 * 	<tr>
	 * 		<th>プロパティ</th>
	 * 		<th>型</th>
	 * 		<th>説明</th>
	 * 		<th>必須</th>
	 * 	</tr>
	 * 	<tr>
	 * 		<th rowspan="2">src</th>
	 * 		<td>File</td>
	 * 		<td>
	 * 			解凍するZIPファイルのパスを示すFileオブジェクトを指定します。
	 * 		</td>
	 * 		<td rowspan="2">Yes。２つのうちどちらか１つ。</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>String(バイナリ)</td>
	 * 		<td>
	 * 			解凍するZIPファイルの内容をバイナリ形式の文字列で指定します。<br/>
	 * 			バイナリ形式の文字列とは、File#load()やRequestParameter#getValueAsStream()の返却値と同じ形式です。
	 * 		</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<th rowspan="1">dest</th>
	 * 		<td>File</td>
	 * 		<td>
	 * 			ZIPファイルの解凍先のパスを示すFileオブジェクトを指定します。<br/>
	 * 			解凍先のパスにファイルやディレクトリが既に存在する場合、それらを削除してから解凍を行います。
	 * 		</td>
	 * 		<td rowspan="1">Yes。</td>
	 * 	</tr>
	 *  </table>
	 *  
	 * <h3>サンプルコード</h3>
	 * 以下のサンプルコードでは、<br/>
	 * 「<b>src/foo.zip</b>」ファイルを、「<b>dest/bar/</b>」ディレクトリに解凍します。<br/>
	 * 変数 <b>result</b> には「<b>dest/bar/</b>」ディレクトリを示す新しいFileオブジェクトが格納されます。<br/>
	 * 
	 * <pre>
	 * var result = Archiver.unzip(
	 *                  {
	 *                      src  : new File("src/foo.zip"),
	 *                      dest : new File("dest/bar/")
	 *                  }
	 *              );
	 * </pre>
	 * 
	 * @scope public
	 * @param arg Object 引数オブジェクト
	 * @return File 解凍先のパスを示すFileオブジェクト。<br/>
	 * 				ZIP形式ではないファイルを解凍しようとした場合は undefined が返却されます。
	 */
	public static Object unzip(Object arg){
		// APIリスト用ダミー
		return null;
	}

	/**
	 * 関数「unzip()」の実体
	 * @throws IOException 
	 */
	public static Object jsStaticFunction_unzip(final Context cx, final Scriptable thisObj, final Object[] args, final Function funObj) throws IOException {
		
		try{
			//=======================================
			// 引数チェック
			//=======================================
			if(!(args[0] instanceof Scriptable)){
				throw new IllegalArgumentException("The argument have to be JavaScript Object.");
			}
			
			String prop4src  = "src";
			String prop4dest = "dest";

			Scriptable arg = (ScriptableObject)args[0];
			Object arg4src  = arg.get(prop4src, arg);
			Object arg4dest = arg.get(prop4dest, arg);
			
			
			//------------------------
			// 入力
			//------------------------
			String zipString = null;
			File zipFile = null;
			
			if(arg4src == ScriptableObject.NOT_FOUND || arg4src == Undefined.instance ){
				throw new IllegalArgumentException("Argument '" + prop4src + "' have to be specified.");
			}
			else{
				if(arg4src instanceof String){
					zipString = (String)arg4src;
					
					// 空文字の場合
					if(zipString.length() == 0){
						throw new IllegalArgumentException("Argument '" + prop4src + "' cannot specify empty string.");
					}
				}
				else if(arg4src instanceof FileAccessObject){
					zipFile = new File( ((FileAccessObject)arg4src).jsFunction_path() );
				}
				else{
					throw new IllegalArgumentException("Argument '" + prop4src + "' is unexpected type.");
				}
			}
			
			//------------------------
			// 出力
			//------------------------
			File destDir = null;
			
			if(arg4dest == ScriptableObject.NOT_FOUND || arg4dest == Undefined.instance ){
				throw new IllegalArgumentException("Argument '" + prop4dest + "' have to be specified.");
			}
			else{
				if( arg4dest instanceof FileAccessObject ){
					destDir        = new File       (       ((FileAccessObject) arg4dest).jsFunction_path());
				}
				else{
					throw new IllegalArgumentException("Argument '" + prop4dest + "' is unexpected type.");
				}
			}
	
			
			//=======================================
			// Caseチェック → 圧縮実行！
			//=======================================
			if(zipFile != null){
				File src = zipFile;
				
				//------------------------
				// UnZip-Case-1
				//------------------------
				if(destDir != null){
					if(logger.isTraceEnabled()){
						String caseName = "UnZip-Case-1";
						Logger innerLogger = Logger.getLogger(BASE_LOGGER_NAME + "." + caseName);
						innerLogger.trace("--------------------------------------");
						innerLogger.trace("src:  {}", src.getCanonicalPath());
						innerLogger.trace("dest: {}", destDir.getCanonicalPath());
						innerLogger.trace("--------------------------------------");
					}

					// 解凍
					Archiver.unzip(src, destDir);
					
					// 結果返却
					return createFileAccessObject(destDir);
				}
				else{
					throw new IllegalStateException();
				}
			}
			
			else if(zipString != null){
				byte[] binary = zipString.getBytes(NON_CONVERT_CHARSET);

				//------------------------
				// UnZip-Case-5
				//------------------------
				if(destDir != null){
					if(logger.isTraceEnabled()){
						String caseName = "UnZip-Case-5";
						Logger innerLogger = Logger.getLogger(BASE_LOGGER_NAME + "." + caseName);
						innerLogger.trace("--------------------------------------");
						innerLogger.trace("src:  {}", "String(Binary)...");
						innerLogger.trace("dest: {}", destDir.getCanonicalPath());
						innerLogger.trace("--------------------------------------");
					}

					// 解凍
					Archiver.unzip(binary, destDir);
					
					// 結果返却
					return createFileAccessObject(destDir);
				}
				else{
					throw new IllegalStateException();
				}
			}
			else{
				throw new IllegalStateException("Argument '" + prop4src + "'(=" + arg4src + ") is illegal.");
			}
		}
		catch(ZipException ze){
			logger.error(ze.getMessage(), ze);
			return Undefined.instance;
		}
		catch(Exception e){
			throw Context.throwAsScriptRuntimeEx(e);
		}
	}

	/**
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	private static File createTempFileOnAppRuntime(String suffix) throws IOException {
		File tempFile = File.createTempFile(ArchiverObject.class.getSimpleName(), suffix);
		tempFile.deleteOnExit();
		
		logger.debug("Create Temp File @AppRuntime: {}", tempFile);
		return tempFile;
	}

	/**
	 * @param tempFile
	 */
	private static void deleteTempFileOnAppRuntime(File tempFile) {
		if(_tempFileDeleteFlag == true){
			tempFile.delete();
		}
	}

	/**
	 *
	 */
	private static class FileFilter4JavaScriptFunction implements FileFilter, Serializable {
		Function filter;
		
		public FileFilter4JavaScriptFunction(Function filter){
			this.filter = filter;
		}

		public boolean accept(File pathname) {
			return invokeFilter(this.filter, pathname);
		}
		
		/**
		 * @param filter
		 * @param target
		 * @return
		 */
		private boolean invokeFilter(final Function filter, final File target) {
			if(filter == null){
				return true;
			}
			else{
				FileAccessObject jsFile = createFileAccessObject(target);
				Object[] args4filter = { jsFile };
				
				Context cx = Context.enter();
				try{
					Scriptable scope = cx.initStandardObjects();
					Object result = filter.call(cx, scope, null, args4filter);
			
					if( (result instanceof Boolean) && ((Boolean)result == Boolean.FALSE) ){
						logger.trace("Filter result -> FALSE: {}", target);
						return false;
					}
					else{
						logger.trace("Filter result -> TRUE:  {}", target);
						return true;
					}
				}
				finally{
					Context.exit();
				}
			}
		}
	}

}
