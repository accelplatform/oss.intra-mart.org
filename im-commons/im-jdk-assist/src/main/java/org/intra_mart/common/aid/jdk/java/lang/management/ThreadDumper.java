package org.intra_mart.common.aid.jdk.java.lang.management;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.intra_mart.common.platform.log.Logger;

// TODO デバッグログを仕込め！
/**
 *
 */
public class ThreadDumper {
	
	private static final Logger logger = Logger.getLogger();
	private static final String INDENT = "    ";
	private static final String findDeadlocksMethodName = "findDeadlockedThreads";
	private static final String pattern = "yyyy-MM-dd HH:mm:ss.SSSS";
	
	private boolean canDumpLocks = false;
	
	private PrintStream ps;
	private ThreadMXBean threadMXBean;

	/**
	 * @param hostname
	 * @param port
	 * @throws IOException
	 * @throws JMException
	 */
	public ThreadDumper(String hostname, int port) throws IOException, JMException {
		this(hostname, port, null);
	}
	
	/**
	 * @param ps
	 * @param hostname
	 * @param port
	 * @throws IOException
	 * @throws JMException
	 */
	public ThreadDumper(PrintStream ps, String hostname, int port) throws IOException, JMException {
		this(ps, hostname, port, null);
	}
	
	/**
	 * @param hostname
	 * @param port
	 * @param jmxServiceEnvironment
	 * @throws IOException
	 * @throws JMException
	 */
	public ThreadDumper(String hostname, 
						int port, 
						Map<String, ?> jmxServiceEnvironment) throws IOException, JMException {
		this(System.out, hostname, port, jmxServiceEnvironment);
	}

	/**
	 * @param ps
	 * @param hostname
	 * @param port
	 * @param jmxServiceEnvironment
	 * @throws IOException
	 * @throws JMException
	 */
	public ThreadDumper(PrintStream ps,
						String hostname,
						int port,
						Map<String, ?> jmxServiceEnvironment) throws IOException, JMException {
		this(ps, createJMXServiceURL(hostname, port), jmxServiceEnvironment);
	}

	/**
	 * @param ps
	 * @param url
	 * @param jmxServiceEnvironment
	 * @throws IOException
	 * @throws JMException
	 */
	public ThreadDumper(PrintStream ps,
						JMXServiceURL url,
						Map<String, ?> jmxServiceEnvironment) throws IOException, JMException {
		this.ps = ps;
		
		logger.debug("Connecting to {}", url);
		JMXConnector jmxc = JMXConnectorFactory.connect(url, jmxServiceEnvironment);
		MBeanServerConnection server = jmxc.getMBeanServerConnection();
		
		this.threadMXBean 
				= ManagementFactory.newPlatformMXBeanProxy(server,
														   ManagementFactory.THREAD_MXBEAN_NAME, 
														   ThreadMXBean.class);

		ObjectName objname = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
		MBeanOperationInfo[] infos = server.getMBeanInfo(objname).getOperations();
		for (MBeanOperationInfo info : infos) {
			if (info.getName().equals(findDeadlocksMethodName)) {
				canDumpLocks = true;
				break;
			}
		}
	}		

	/**
	 * @param hostname
	 * @param port
	 * @return
	 * @throws MalformedURLException
	 */
	private static JMXServiceURL createJMXServiceURL(String hostname, int port)
										throws MalformedURLException {
		
		String urlPath = "/jndi/rmi://" + hostname + ":" + port + "/jmxrmi";
		JMXServiceURL url = new JMXServiceURL("rmi", "", 0, urlPath);
		return url;
	}

	/**
	 * スレッドダンプを出力します。
	 * @throws IOException
	 */
	public void dump() throws IOException {
		if (canDumpLocks) {
			if (threadMXBean.isObjectMonitorUsageSupported() && threadMXBean.isSynchronizerUsageSupported()) {
				dumpThreadInfoWithLocks();
			}
			else{
				dumpThreadInfo();
			}
		}
		else {
			dumpThreadInfo();
		}
		
		this.printDeadlockInfo();
	}

	/**
	 * 
	 */
	private void dumpThreadInfo() {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		ps.println(sdf.format(new Date()));
		ps.println("Full Java thread dump");
		
		long[] tids = this.threadMXBean.getAllThreadIds();
		ThreadInfo[] tinfos = threadMXBean.getThreadInfo(tids, Integer.MAX_VALUE);
		for (ThreadInfo ti : tinfos) {
			printThreadInfo(ti);
			printStackTrace(ti);
		}
		ps.println();
	}

	/**
	 * 
	 */
	private void dumpThreadInfoWithLocks() {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		ps.println(sdf.format(new Date()));
		ps.println("Full Java thread dump (With locks info)");

		ThreadInfo[] tinfos = threadMXBean.dumpAllThreads(true, true);
		for (ThreadInfo ti : tinfos) {
			printThreadInfo(ti);
			printStackTraceWithLocks(ti);
			
			LockInfo[] syncs = ti.getLockedSynchronizers();
			printLockInfo(syncs);
		}
		ps.println();
	}

	/**
	 * @param ti
	 */
	private void printStackTrace(ThreadInfo ti) {
		StackTraceElement[] stacktraces = ti.getStackTrace();
		for (int idx = 0; idx < stacktraces.length; idx++) {
			StackTraceElement ste = stacktraces[idx];
			ps.println(INDENT + "at " + ste.toString());
		}
		ps.println();
	}
	
	/**
	 * @param ti
	 */
	private void printStackTraceWithLocks(ThreadInfo ti) {
		MonitorInfo[] monitors = ti.getLockedMonitors();
		
		StackTraceElement[] stacktraces = ti.getStackTrace();
		for (int idx = 0; idx < stacktraces.length; idx++) {
			StackTraceElement ste = stacktraces[idx];
			ps.println(INDENT + "at " + ste.toString());
			
			for (MonitorInfo mi : monitors) {
				if (mi.getLockedStackDepth() == idx) {
					ps.println(INDENT + "  - locked " + mi);
				}
			}
		}
		ps.println();
	}

	/**
	 * @param ti
	 */
	private void printThreadInfo(ThreadInfo ti) {
		StringBuilder sb = new StringBuilder("\"" + ti.getThreadName() + "\""
				+ " Id=" + ti.getThreadId() + " in " + ti.getThreadState());
		if (ti.getLockName() != null) {
			sb.append(" on lock=" + ti.getLockName());
		}
		if (ti.isSuspended()) {
			sb.append(" (suspended)");
		}
		if (ti.isInNative()) {
			sb.append(" (running in native)");
		}
		ps.println(sb.toString());
		if (ti.getLockOwnerName() != null) {
			ps.println(INDENT + " owned by " + ti.getLockOwnerName() + " Id="
					+ ti.getLockOwnerId());
		}
	}

	/**
	 * @param locks
	 */
	private void printLockInfo(LockInfo[] locks) {
		ps.println(INDENT + "Locked synchronizers: count = " + locks.length);
		for (LockInfo li : locks) {
			ps.println(INDENT + "  - " + li);
		}
		ps.println();
	}

	private void printDeadlockInfo() {
		if (canDumpLocks && threadMXBean.isSynchronizerUsageSupported()) {
			long[] tids = this.threadMXBean.findDeadlockedThreads();
			if (tids == null) {
				ps.println("No deadlock found.");
				return;
			}
			else{
				ps.println("Deadlock found :-");
				ThreadInfo[] infos = this.threadMXBean.getThreadInfo(tids, true, true);
				for (ThreadInfo ti : infos) {
					printStackTraceWithLocks(ti);
					printLockInfo(ti.getLockedSynchronizers());
				}
				return;
			}
		}

		long[] tids = this.threadMXBean.findMonitorDeadlockedThreads();
		if (tids == null) {
			ps.println("No deadlock found.");
			return;
		}
		else{
			ps.println("Deadlock found :-");
			ThreadInfo[] infos = this.threadMXBean.getThreadInfo(tids, Integer.MAX_VALUE);
			for (ThreadInfo ti : infos) {
				printThreadInfo(ti);
			}
		}
	}
	

	public static void main(String[] args) throws IOException, MalformedObjectNameException, JMException {
		if (args.length < 1) {
			usage();
			return;
		}
		
		if("-help".equals(args[0])){
			usage();
			return;
		}
		
		String[] splitted = args[0].split(":");
		if (splitted.length != 2) {
			usage();
			return;
		}
		
		String hostname = splitted[0];
		int port;
		Map<String, Object> environment = new HashMap<String, Object>();
		
		try {
			port = Integer.parseInt(splitted[1]);
		}
		catch (NumberFormatException e) {
			usage();
			return;
		}

		if (args.length == 2) {
			usage();
			return;
		}

		if (args.length > 2) {
			String username = args[1];
			String password = args[2];
			
			environment.put(javax.management.remote.JMXConnector.CREDENTIALS, new String[]{username, password});
		}

		if (args.length > 3) {
			String protocolProviderPackages = args[3];
			environment.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, protocolProviderPackages);
		}

		ThreadDumper threadDumper = new ThreadDumper(hostname, port, environment);
		threadDumper.dump();
	}

	private static void usage() {
		System.out.println("Usage: java " + ThreadDumper.class.getName() + " <hostname>:<port> [username password]");
	}
}
