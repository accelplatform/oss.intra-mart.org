package org.intra_mart.jst.server.generic.resin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour;

/**
 * ResinServerBehaviour.
 * 
 */
@SuppressWarnings("restriction")
public class ResinServerBehaviour extends GenericServerBehaviour {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour#stop
	 * (boolean)
	 */
	public void stop(final boolean force) {
		terminate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.server.core.model.ServerBehaviourDelegate#restart(java
	 * .lang.String)
	 */
	public void restart(final String launchMode) throws CoreException {
		terminate();
		getServer().start(launchMode, new NullProgressMonitor());
	}

	/**
	 * setupLaunchClasspath.<br />
	 * Resin起動前にtools.jarをクラスパスに追加します。<br />
	 * Eclipse上で設定されているInstalled JREは、JDKが指定されている必要があります。
	 * 
	 * @param workingCopy
	 *            workingCopy.
	 * @param vmInstall
	 *            vmInstall.
	 * @param classPathList
	 *            classPathList.
	 * @see org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour#setupLaunchClasspath(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy,
	 *      org.eclipse.jdt.launching.IVMInstall, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	protected void setupLaunchClasspath(final ILaunchConfigurationWorkingCopy workingCopy, final IVMInstall vmInstall, final List classPathList) {

		Path compilerPath = null;

		if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
			// mac.
			// vm intall location   : /System/Library/Frameworks/JavaVM.framework/Versions/1.x.x/Home
			// classes.jar location : /System/Library/Frameworks/JavaVM.framework/Versions/1.x.x/Classes/classes.jar
			compilerPath = new Path(new File(vmInstall.getInstallLocation().getParent()).getAbsolutePath() + File.separator + "Classes" + File.separator + "classes.jar");
		} else {
			// others.
			// tools.jar location : %vmInstallLoaction%/lib/tools.jar
			compilerPath = new Path(vmInstall.getInstallLocation().getAbsolutePath() + File.separator + "lib" + File.separator + "tools.jar");
		}

		if (compilerPath != null && compilerPath.toFile().exists()) {
			// 存在しなければ追加しない
			// -> 手動でresin.xmlを<javac compiler="javac" args="-source 1.5"/>に変更してもらう?
			classPathList.add(JavaRuntime.newArchiveRuntimeClasspathEntry(compilerPath));
		}

		super.setupLaunchClasspath(workingCopy, vmInstall, classPathList);
	}

	/**
	 * 起動時にresin.xmlファイルの生成を行います.
	 * 
	 * @param launch
	 *            launch.
	 * @param launchMode
	 *            launchMode.
	 * @param monitor
	 *            monitor.
	 * @see org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour#setupLaunch(org.eclipse.debug.core.ILaunch,
	 *      java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void setupLaunch(final ILaunch launch, final String launchMode, final IProgressMonitor monitor) throws CoreException {
		createConfigFile();
		super.setupLaunch(launch, launchMode, monitor);
	}

	/**
	 * プログラム引数にresin設定ファイル(-conf resin.xml)を追加します.
	 * 
	 * @return program arguments.
	 * @see org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour#
	 *      getProgramArguments ()
	 */
	@Override
	protected String getProgramArguments() {
		return super.getProgramArguments() + "-conf conf/" + getConfigFileName();
	}

	/**
	 * 設定ファイルテンプレートを取得します.
	 * 
	 * @return config file template stream.
	 */
	protected InputStream getConfigFileTemplate() {
		final String type = getServer().getServerType().getId();
		final String configFileLocation = "/servers/conf/" + type.substring(type.lastIndexOf(".") + 1) + ".xml";

		return ResinServerBehaviour.class.getResourceAsStream(configFileLocation);
	}

	/**
	 * 設定ファイル名を取得します.
	 * 
	 * @return config file name.
	 */
	protected String getConfigFileName() {
		return getServer().getName().replaceAll(" ", "_") + ".xml";
	}

	/**
	 * 設定ファイルを作成します.<br />
	 * 既に設定ファイルが存在する場合は作成しません。
	 */
	protected void createConfigFile() {
		final File file = new File(getRuntimeDelegate().getRuntime().getLocation().toFile(), "/conf/" + getConfigFileName());

		if (file.exists()) {
			return;
		}

		InputStream in = null;
		OutputStream out = null;

		try {
			in = getConfigFileTemplate();
			out = new BufferedOutputStream(new FileOutputStream(file));

			int size = 0;
			final byte[] buff = new byte[1024];

			while ((size = in.read(buff)) != -1) {
				out.write(buff, 0, size);
			}
			out.flush();
		} catch (final FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					// ignore.
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					// ignore.
				}
			}
		}
	}
}
