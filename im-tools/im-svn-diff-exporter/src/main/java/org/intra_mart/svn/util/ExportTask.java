package org.intra_mart.svn.util;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

public class ExportTask extends Task {
	
	private String user     = "guest";
	private String password = "";

	private String url      = null; // 例：http://oss.intra-mart.org/projects/im-tools/svn/trunk/im-svn-diff-exporter
	private String revision = null; // 例：9, HEAD
	
	private String destDir  = System.getProperty("java.io.tmpdir") + "/ExportTaskTemp/";

	private boolean ignoreEmptyDir = false;
	
	/* (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {

		DiffExporter expoter = new DiffExporter();
		
		try {
			// 初期化
			expoter.initialize(this.user, this.password);
			
			// Export 開始
			expoter.doExport(SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url)),
							 "",
							 DiffExporter.convertRevision(revision),
							 destDir,
							 ignoreEmptyDir);
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


	public void setUser(String user) {
		this.user = user;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public void setRevision(String revision) {
		this.revision = revision;
	}

	public void setDestDir(String destDir) {
		this.destDir = destDir;
	}

	public void setIgnoreEmptyDir(boolean ignoreEmptyDir) {
		this.ignoreEmptyDir = ignoreEmptyDir;
	}

}
