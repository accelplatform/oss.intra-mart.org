/*
 * 作成日: 2004/01/06
 */
package org.intra_mart.framework.system.log;

/**
 * ログモデルクラスです。
 * @author INTRAMART
 * @version 1.0
 */
class LogModel {
	
	private String logAgentName;
	private LogAgentParam[] logAgentParams;

	public static String ID = "log-config";
	public static String P_ID_AGENT_NAME = "agent-class";
	public static String P_ID_INIT_PARAM = "init-param";
	public static String P_ID_PARAM_NAME = "param-name";
	public static String P_ID_PARAM_VALUE = "param-value";

	/**
	 * LogAgentNameを取得します。
	 * @return String LogAgentName
	 */
	String getLogAgentName() {
		return logAgentName;
	}

	/**
	 * LogAgentParamsを取得します。
	 * @return LogAgentParam[] LogAgentParams
	 */
	LogAgentParam[] getLogAgetnParams() {
		return logAgentParams;
	}

	/**
	 * LogAgentNameを設定します。
	 * @param String LogAgentName
	 */
	void setLogAgentName(String name) {
		this.logAgentName = name;		
	}

	/**
	 * LogAgentParamを設定します。
	 * @param LogAgentParam[] params
	 */
	void setLogAgentParams(LogAgentParam[] params) {
		this.logAgentParams = params;	
	}

}
