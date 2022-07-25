package org.intra_mart.svn.util;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

public class DiffFilePathExtractTask  extends Task {

	private String user     = "guest";
	private String password = "";

	private String urlFrom      = null; // 例：http://oss.intra-mart.org/projects/im-tools/svn/trunk/im-svn-diff-exporter
	private String revisionFrom = null; // 例：9
	
	private String urlTo      = urlFrom;
	private String revisionTo = "HEAD";

	private boolean recursive = true;
	
	private String destDir    = System.getProperty("java.io.tmpdir") + "/DiffFilePathExtractTaskTemp/";
	private String diffFile   = destDir + "diff.txt";
	private String resultFile = destDir + "resultFile.txt";

	
	/* (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		
		DiffExporter expoter = new DiffExporter();

		try {
			// 初期化
			expoter.initialize(user, password);
			
			// Diff 開始
			expoter.doDiff(SVNURL.parseURIEncoded(urlFrom),
						   DiffExporter.convertRevision(revisionFrom),
						   SVNURL.parseURIEncoded(urlTo),
						   DiffExporter.convertRevision(revisionTo),
						   recursive,
						   diffFile);

			// 指定されたDiffファイルを元に、ファイルパス、および、作成者をファイルに出力
			expoter.extractFilePathAndAuthorBasedOnDiff(
								SVNURL.parseURIEncoded(urlTo),
								DiffExporter.convertRevision(revisionTo),
								diffFile,
								resultFile);
		}
		catch (SVNException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}


	public void setDiffFile(String diffFile) {
		this.diffFile = diffFile;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}


	public void setRevisionFrom(String revisionFrom) {
		this.revisionFrom = revisionFrom;
	}


	public void setRevisionTo(String revisionTo) {
		this.revisionTo = revisionTo;
	}


	public void setUrlFrom(String urlFrom) {
		this.urlFrom = urlFrom;
	}


	public void setUrlTo(String urlTo) {
		this.urlTo = urlTo;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}

}


