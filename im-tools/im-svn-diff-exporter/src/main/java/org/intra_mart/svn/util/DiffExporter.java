package org.intra_mart.svn.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaProcessor;
import org.tmatesoft.svn.core.io.diff.SVNDiffWindow;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class DiffExporter {

	private static String user     = "guest";
	private static String password = "";

	private static String urlFrom      = "http://oss.intra-mart.org/projects/im-tools/svn/trunk/im-svn-diff-exporter";
	private static String revisionFrom = "9";
	
	private static String urlTo      = urlFrom;
	private static String revisionTo = "HEAD";
	
	private static boolean recursive = true;
	
	private static String destDir    = System.getProperty("java.io.tmpdir") + "/im-svn-diff-exporter-temp/";
	private static String diffFile   = destDir + "diff.txt";
	private static String resultFile = destDir + "result.txt";
	
	private static boolean ignoreEmptyDir = false;

	/**
	 * サンプルです。
	 * 
	 * @param args
	 * @throws SVNException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, SVNException {

		DiffExporter expoter = new DiffExporter();

		// 初期化
		expoter.initialize(user, password);

//		// Export 開始
//		expoter.doExport(SVNRepositoryFactory.create(SVNURL.parseURIEncoded(urlTo)),
//						 "",
//						 DiffExporter.convertRevision(revisionTo),
//						 destDir,
//						 ignoreEmptyDir);
//
//		// Diff 開始
//		expoter.doDiff(SVNURL.parseURIEncoded(urlFrom),
//					   DiffExporter.convertRevision(revisionFrom),
//					   SVNURL.parseURIEncoded(urlTo),
//					   DiffExporter.convertRevision(revisionTo),
//					   recursive,
//					   diffFile);
//
//		// Export開始（Diffファイルに則って）
//		expoter.doExportBasedOnDiff(SVNURL.parseURIEncoded(urlTo),
//									DiffExporter.convertRevision(revisionTo),
//									destDir,
//									diffFile);
//		
//		// 指定されたDiffファイルを元に、ファイルパス、および、作成者をファイルに出力
//		expoter.extractFilePathAndAuthorBasedOnDiff(
//							SVNURL.parseURIEncoded(urlTo),
//							DiffExporter.convertRevision(revisionTo),
//							diffFile,
//							resultFile);

	}

	private static final String DIFF_FILE_LABEL = "Index: ";
	private SVNClientManager ourClientManager;
	private ISVNAuthenticationManager authManager;

	/**
	 * 初期化を行います。
	 * 
	 * @param userName ユーザアカウント名
	 * @param password ユーザパスワード
	 * @throws SVNException
	 */
	public void initialize(String userName, String password) throws SVNException{
		
    	// for DAV (over http and https)
    	DAVRepositoryFactory.setup();
        
        // for svn (over svn and svn+ssh)
    	SVNRepositoryFactoryImpl.setup();

    	// For using over file:///
    	FSRepositoryFactory.setup();
    	
		// クライアントマネージャ生成
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		ourClientManager = SVNClientManager.newInstance(options, userName, password);
		
		authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, password);

	}
	
	/**
	 * @param url
	 * @param filePath
	 * @param revision
	 * @param destDir
	 * @param ignoreEmptyDir
	 * @throws SVNException
	 * @throws IOException
	 */
	public void doExport(SVNRepository repository,
						 String filePath,
						 SVNRevision revision,
						 String destDir,
						 boolean ignoreEmptyDir)
					throws SVNException, IOException {

		if(this.authManager == null){
			throw new IllegalStateException("Not yet initialized");
		}
		
		repository.setAuthenticationManager(authManager);
		
		long revisionNumber = revision.getNumber();

		// ノードタイプ取得
		SVNNodeKind nodeKind = repository.checkPath(filePath, revisionNumber);

		// ファイルの場合
		if (nodeKind == SVNNodeKind.FILE) {
			
			File targetFile = createTargetFile(repository, filePath, destDir, revisionNumber);				
			targetFile.getParentFile().mkdirs();
			
			FileOutputStream fos = new FileOutputStream(targetFile);
			BufferedOutputStream exportStream = new BufferedOutputStream(fos);
			
			repository.getFile(filePath, revisionNumber, null, exportStream);
			
			exportStream.close();
		}
		// ディレクトリの場合
		else if (nodeKind == SVNNodeKind.DIR){

		    Collection entries = repository.getDir(filePath, revisionNumber, null, (Collection) null);

		    if(entries.size() == 0 && ignoreEmptyDir == false){
				File targetFile = createTargetFile(repository, filePath, destDir, revisionNumber);
				targetFile.mkdirs();
		    }
		    else{
			    Iterator iterator = entries.iterator();
		        while (iterator.hasNext()) {
		            SVNDirEntry entry = (SVNDirEntry) iterator.next();
		            
					String childPath = (filePath != null && filePath.length() != 0) ?
											filePath + "/" + entry.getRelativePath() :
												entry.getRelativePath();
	
					// 再帰
			        this.doExport(repository,
			        		 childPath,
							 revision,
							 destDir,
							 ignoreEmptyDir);
		        }
		    }
		}
		// そのほかは、エラー扱い
		else{
			String url = repository.getLocation().getURIEncodedPath() + filePath;
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "No entry at URL ''{0}''", url);
			throw new SVNException(err);
		}
		
	}

	private File createTargetFile(SVNRepository repository, String filePath,
			String destDir, long revisionNumber) throws SVNException {
		SVNDirEntry entry = repository.info(filePath, revisionNumber);
		
		File targetFile = null;
		if(filePath != null && filePath.length() != 0){
			targetFile = new File(destDir, filePath);
			System.out.println(destDir + "/" + filePath);
		}
		else{
			targetFile = new File(destDir, entry.getRelativePath());
			System.out.println(destDir + "/" + entry.getRelativePath());
		}
		return targetFile;
	}
	
	/**
	 * Diffファイルを生成します。
	 * 
	 * @param svnUrlFrom URL(From)
	 * @param svnRevisionFrom URL(From)のリビジョン
	 * @param svnUrlTo URL(To)
	 * @param svnRevisionTo URL(To)のリビジョン
	 * @param recursive 再帰的にDiffを取得する場合はtrue, それ以外はfalse
	 * @param diffFile Diffファイルのパス
	 * 
	 * @return 生成されたDiffファイル
	 * 
	 * @throws IOException Diffファイルの生成に失敗した場合
	 * @throws SVNException リビジョン指定が不正な場合、または、指定されたリビジョンには、URLが存在しない場合
	 */
	public File doDiff(SVNURL svnUrlFrom,
						SVNRevision svnRevisionFrom,
						SVNURL svnUrlTo,
						SVNRevision svnRevisionTo,
						boolean recursive,
						String diffFile)
					throws IOException, SVNException {

		if(this.ourClientManager == null){
			throw new IllegalStateException("Not yet initialized");
		}

		SVNDiffClient diffClient = this.ourClientManager.getDiffClient();

		File targetFile = new File(diffFile);
		File parent = targetFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		targetFile.createNewFile();		
		
		System.out.println("I'm creating the diff file -> " + targetFile.getAbsolutePath());
		System.out.println("Please Wait....");
		DotWriter thread = new DotWriter();
		thread.start();
		
		BufferedOutputStream diffStream = new BufferedOutputStream(new FileOutputStream(targetFile));
		diffClient.doDiff(svnUrlFrom,
						  svnRevisionFrom,
						  svnUrlTo,
						  svnRevisionTo,
						  recursive,
						  true,
						  diffStream);
		diffStream.close();
		
		thread.setRun(false);
		
		if(targetFile.length() == 0){
			FileWriter writer = new FileWriter(targetFile);
			writer.write("There is no differences. \n");
			writer.write("------------------------------------" + "\n");
			writer.write("svnUrlFrom      : " + svnUrlFrom      + "\n");
			writer.write("svnRevisionFrom : " + svnRevisionFrom + "\n");
			writer.write("svnUrlTo        : " + svnUrlTo        + "\n");
			writer.write("svnRevisionTo   : " + svnRevisionTo   + "\n");
			writer.write("recursive       : " + recursive       + "\n");
			writer.write("------------------------------------" + "\n");
			writer.close();
		}
		
		return targetFile;
	}

	
	/**
	 * 指定されたDiffファイルを元に、エクスポートを行います。<BR/>
	 * Diffファイルの行頭が"Index: "で指定されるファイルをエクスポートします。<BR/>
	 * <B>【注意】URLはディレクトリを指定してください。</B><BR/>
	 * 
	 * @param url URL（ディレクトリを指定してください）
	 * @param revision リビジョン
	 * @param destDir エクスポート先ディレクトリ
	 * @param diffFile Diffファイルのパス
	 * 
	 * @throws IOException ファイル入出力
	 * @throws SVNException
	 */
	public void doExportBasedOnDiff(SVNURL url,
								     SVNRevision revision,
								     String destDir,
								     String diffFile)
					throws IOException, SVNException {

		if(this.ourClientManager == null){
			throw new IllegalStateException("Not yet initialized");
		}

		SVNWCClient wcClient = ourClientManager.getWCClient();

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(diffFile)));
		
		try{
			String line;
			while ((line = br.readLine()) != null) {
				
				int index = line.indexOf(DIFF_FILE_LABEL);
				if (index == 0) {
					String path = line.substring(index + DIFF_FILE_LABEL.length());
	
					File targetFile = new File(destDir, path);
					File parentFile = targetFile.getParentFile();
					if (!parentFile.exists()) {
						parentFile.mkdirs();
					}
					
					BufferedOutputStream exportStream = new BufferedOutputStream(new FileOutputStream(targetFile));
					try{
						wcClient.doGetFileContents(url.appendPath(path, true),
													revision,
													revision,
													true,
													exportStream);
	
						System.out.println("Exported : " + targetFile.getAbsolutePath());
					}
					catch(SVNException e){
						
						SVNErrorMessage errorMessage = e.getErrorMessage(); 
						
						// ファイルが見つからない場合(=404 Not Found)は、削除ファイルとみなし処理続行
						if( errorMessage.getErrorCode().equals(SVNErrorCode.RA_DAV_PATH_NOT_FOUND)
						    ||
						    errorMessage.getErrorCode().equals(SVNErrorCode.FS_NOT_FOUND) ){
							System.err.println(errorMessage.getFullMessage());
							
							// ファイル削除
							exportStream.close();
							targetFile.delete();
						}
						// 上記以外は例外をそのままスロー
						else {
							throw e;
						}
					}
					finally{
						// ファイル削除されていなかったらクローズ
						if(targetFile.exists()){
							exportStream.close();
						}
					}
					
				}
			}
		}
		finally{
			if(br != null){
				br.close();					
			}
		}
	}

	
	/**
	 * 指定されたDiffファイルを元に、ファイルパス、および、作成者をファイルに出力します。<BR/>
	 *  Diffファイルの行頭が"Index: "で指定されるファイルをエクスポートします。<BR/>
	 *  <B>【注意】URLはディレクトリを指定してください。</B><BR/>
	 *  
	 * @param url
	 * @param svnRevisionTo
	 * @param diffFile
	 * @param resultFile
	 */
	public void extractFilePathAndAuthorBasedOnDiff(
													SVNURL url,
													SVNRevision svnRevisionTo,
													String diffFile,
													String resultFile) throws IOException, SVNException {

		if(this.authManager == null){
			throw new IllegalStateException("Not yet initialized");
		}

		SVNRepository repository = SVNRepositoryFactory.create(url);
		repository.setAuthenticationManager(authManager);

		// 入力ファイル
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(diffFile)));

		// 出力ファイル
		File outFile = new File(resultFile);
		File parent = outFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		outFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		System.out.println("I'm creating the result file -> " + outFile.getAbsolutePath());
		
		// ヘッダーを出力
		writer.write(
				"Type"     + "\t" + // ('A':added, 'D':deleted, 'M':modified, 'R':replaced, 'Err':Error、’?’:) 
				"Path"     + "\t" + 
				"Revision" + "\t" +
				"Author"   + "\t" +
				"Date"     + "\t" +
				"Message"  + "\n");
		
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_kk:mm:ss");

        try{
			String line;
			while ((line = br.readLine()) != null) {
				
				int index = line.indexOf(DIFF_FILE_LABEL);
				if (index == 0) {
					String path = line.substring(index + DIFF_FILE_LABEL.length());

					String path4Log = url.toString() + "/" + path;

					// 対象パス＆リビジョンの情報を取得
					SVNDirEntry dirEntry = repository.info(path, svnRevisionTo.getNumber());
					
					if(dirEntry == null){
						//===============================
						// ログ書き込み (ファイルが削除されている or 不正なリビジョン指定)
						//===============================
						writer.write("Err"         + "\t" + 
									 path4Log      + "\t" + 
									 svnRevisionTo + "\t" +
									 "-"           + "\t" +
									 "-"           + "\t" +
									 "SVNDirEntry is null. (File was deleted or specified invalid revision.)" + "\n");
					}
					else{
						long targetRevision = dirEntry.getRevision(); 

						// リビジョン「targetRevision」 から リビジョン「HEAD」 のコミット・ログを取得
						Collection logEntries = repository.log(
															new String[]{ path },
															null, 
															targetRevision,
															SVNRevision.HEAD.getNumber(),
															true,
															false); // ← コピー・ヒストリーも取得するので「false」指定

						SVNLogEntry targetLogEntry = null;
						for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
							SVNLogEntry logEntry = (SVNLogEntry) entries.next();
							if(logEntry.getRevision() == targetRevision){
								targetLogEntry = logEntry;
								break;
							}
						}

						if(targetLogEntry == null){
							//===============================
							// ログ書き込み (更新タイプが不明)
							//===============================
							// (「コミットメッセージ(=dirEntry.getCommitMessage())」は取得できません → なぜ？)
							writer.write("?"                            + "\t" + 
									 	 path4Log                       + "\t" + 
									 	 dirEntry.getRevision()         + "\t" +
									 	 dirEntry.getAuthor()           + "\t" +
									 	 sdf.format(dirEntry.getDate()) + "\t" +
									 	 dirEntry.getCommitMessage()    + "\n");
						}
						else{
							SVNLogEntryPath svnLogEntryPath = null;
							
							Map paths = targetLogEntry.getChangedPaths();
							Iterator it = paths.entrySet().iterator();
							while(it.hasNext()){
								Map.Entry me = (Map.Entry) it.next();
								String targetPath = (String) me.getKey();
								
								if(targetPath.indexOf(path) != -1){
									svnLogEntryPath = (SVNLogEntryPath) me.getValue();
									break;
								}
							}
							
							char type4Log = '?';
							if(svnLogEntryPath != null){
								type4Log = svnLogEntryPath.getType();
								// path4Log = svnLogEntryPath.getPath();
							}
						
							//===============================
							// ログ書き込み
							//===============================
							// （SVNLogEntry、および、SVNLogEntryPathを利用してログ出力する理由
							//  　　∵SVNDirEntryでは、「更新タイプ」と「コミットメッセージ」が取得できないから）
							writer.write(type4Log                                       + "\t" + 
										 path4Log                                       + "\t" + 
										 targetLogEntry.getRevision()                   + "\t" +
										 targetLogEntry.getAuthor()                     + "\t" +
										 sdf.format(targetLogEntry.getDate())           + "\t" +
										 targetLogEntry.getMessage().replace('\n', ' ') + "\n");
						}
					}
				}
			}
		}
		finally{
			if(br != null){
				br.close();
			}
			
			if(writer != null){
				writer.close();
			}
		}
	}

	
	/**
	 * @param revision
	 * @return
	 */
	public static SVNRevision convertRevision(String revision){
		if("HEAD".equals(revision)){
			return SVNRevision.HEAD;
		}
		else if("WORKING".equals(revision)){
			return SVNRevision.WORKING;
		}
		else if("PREVIOUS".equals(revision)){
			return SVNRevision.PREVIOUS;
		}
		else if("BASE".equals(revision)){
			return SVNRevision.BASE;
		}
		else if("COMMITTED".equals(revision)){
			return SVNRevision.COMMITTED;
		}
		else if("UNDEFINED".equals(revision)){
			return SVNRevision.UNDEFINED;
		}
		else {
			return SVNRevision.create(Long.parseLong(revision));
		}
	}

	
	
	private static class DotWriter extends Thread{
		
		private boolean isRun = true;
		private long sleepTime = 1000;
		
		public void setRun(boolean isRun){
			this.isRun = isRun;
		}
		
		public void run(){
			while(isRun){
				
				try {
					System.out.print(".");
					sleep(sleepTime);
				} 
				catch (InterruptedException e) {
					/* Do Nothing */ 
				}

			}
		}
	}

}
