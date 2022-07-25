package org.intra_mart.svn.util;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

public class DiffExportTask extends Task {
	
	private String user     = "guest";
	private String password = "";

	private String urlFrom      = null; // 例：http://oss.intra-mart.org/projects/im-tools/svn/trunk/im-svn-diff-exporter
	private String revisionFrom = null; // 例：9
	
	private String urlTo      = urlFrom;
	private String revisionTo = "HEAD";

	private boolean recursive = true;
	
	private String destDir        = System.getProperty("java.io.tmpdir") + "/DiffExportTaskTemp/";
	private String diffResultFile = destDir + "diff.txt";

		
	/* (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {

		DiffExporter expoter = new DiffExporter();

		/*
			System.out.println("====Setting DiffExportTask =======================");
			System.out.println("user: " + user);
			System.out.println("password: " + password);
	
			System.out.println("urlFrom: " + urlFrom);
			System.out.println("revisionFrom: " + revisionFrom);
	
			System.out.println("urlTo: " + urlTo);
			System.out.println("revisionTo: " + revisionTo);
	
			System.out.println("recursive: " + recursive);
	
			System.out.println("destDir: " + destDir);
			System.out.println("diffResultFile: " + diffResultFile);
			System.out.println("==================================================");
		*/
		
		try {
			// 初期化
			expoter.initialize(this.user, this.password);

			// Diff 開始
			expoter.doDiff(SVNURL.parseURIEncoded(this.urlFrom),
							DiffExporter.convertRevision(revisionFrom),
							SVNURL.parseURIEncoded(urlTo),
							DiffExporter.convertRevision(revisionTo),
							recursive,
							diffResultFile);
			
			// Export開始（Diffファイルに則って）
			expoter.doExportBasedOnDiff(SVNURL.parseURIEncoded(urlTo),
										DiffExporter.convertRevision(revisionTo),
										destDir,
										diffResultFile);
		}
		catch (SVNException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	
	
	}


	public void setDestDir(String destDir) {
		this.destDir = destDir;
	}


	public void setDiffResultFile(String diffResultFile) {
		this.diffResultFile = diffResultFile;
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


	
}
