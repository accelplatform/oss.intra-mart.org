/*
 * Copyright 2008 the OPEN INTRA-MART.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

Package("im.log");

/**
 * @fileoverview <br/>
 * ログ出力機能インターフェース。
 */

Import("im.log.Logger");
Import("im.util.Queue");

Class("im.log.SimpleLogger").extend("im.log.Logger").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @class
	 * 別ウィンドウ上にログ出力するクラスです。<br/>
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.5
	 */
	im.log.SimpleLogger = function (name) {
		/* constructor */
		{
			this.superclass();
		}
		/**
		 * traceレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.trace = function (message) {
			this.println("[TRACE][" + name + "] "+ message);
		}
		/**
		 * debugレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.debug = function (message) {
			this.println("[DEBUG][" + name + "] " + message);
		}
		/**
		 * infoレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.info = function (message) {
			this.println("[INFO][" + name + "] " + message);
		}
		/**
		 * warnレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.warn = function (message) {
			this.println("[WARN][" + name + "] " + message);
		}
		/**
		 * errorレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.error = function (message) {
			this.println("[ERROR][" + name + "] " + message);
		}
		/**
		 * Logger のインスタンス名を取得します。
		 * @return Logger のインスタンス名
		 * @type String
		 */
		this.getName = function () {
			return name;
		}
		/**
		 * traceレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return traceレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isTraceEnabled = function () {
			return false;
		}
		/**
		 * debugレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return debugレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isDebugEnabled = function () {
			return false;
		}
		/**
		 * infoレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return infoレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isInfoEnabled = function () {
			return false;
		}
		/**
		 * warnレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return warnレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isWarnEnabled = function () {
			return false;
		}
		/**
		 * errorレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return errorレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isErrorEnabled = function () {
			return false;
		}
		
		/** @pirvate */
		this.println = function (msg) {
			this.print(msg + "<br/>");
		}
		/** @pirvate */
		this.print = function (msg) {
			if (!im.log.SimpleLogger.hasWindowOpened()) {
				im.log.SimpleLogger.openWindow();
			}
			im.log.SimpleLogger.logQueue.enqueue(msg);
			if (im.log.SimpleLogger.intervalId == null) {
				im.log.SimpleLogger.intervalId = setInterval("im.log.SimpleLogger.printToLogWindow()", 200);
			}
		}
	}
);
/* static field and static method of im.log.SimpleLogger class */
{
	/** @private */
	im.log.SimpleLogger.LOG_WINDOW_NAME = "im.log.SimpleLogger";
	/** @private */
	im.log.SimpleLogger.OPEN_HTML_FILE = "assets/html/simple_logger.html";
	/** @private */
	im.log.SimpleLogger.LOG_CONSOLE_ELEMENT_ID = "LOG_CONSOLE";
	/** @private */
	im.log.SimpleLogger.logWindow = null;
	/** @private */
	im.log.SimpleLogger.logQueue;
	/** @private */
	im.log.SimpleLogger.intervalId = null;
	/** @pirvate */
	im.log.SimpleLogger.hasWindowOpened = function () {
		return !(im.log.SimpleLogger.logWindow == null || im.log.SimpleLogger.logWindow.closed); 
	}
	/** @pirvate */
	im.log.SimpleLogger.openWindow = function () {
		if (!im.log.SimpleLogger.hasWindowOpened()) {
			var openFileName = im.log.SimpleLogger.clazz.getResource(im.log.SimpleLogger.OPEN_HTML_FILE);
			im.log.SimpleLogger.logWindow = window.open(openFileName, im.log.SimpleLogger.LOG_WINDOW_NAME, "width=200,height=200,scrollbars=yes");
		}
	}
	/** @private */
	im.log.SimpleLogger.printToLogWindow = function () {
		if (!im.log.SimpleLogger.hasWindowOpened()) {
			im.log.SimpleLogger.openWindow();
			return;
		}
		var logWindowDocument = im.log.SimpleLogger.logWindow.document;
		if (logWindowDocument == null) {
			return;
		} 
		var logConsoleNode = logWindowDocument.getElementById(im.log.SimpleLogger.LOG_CONSOLE_ELEMENT_ID);
		if (logConsoleNode == null) {
			return;
		}
		logConsoleNode.innerHTML += im.log.SimpleLogger.logQueue.dequeue(); 
		if (im.log.SimpleLogger.logQueue.size() == 0) {
			clearInterval(im.log.SimpleLogger.intervalId);
			im.log.SimpleLogger.intervalId = null;
		}
	}
	/** @private */
	im.log.SimpleLogger.initialize = function(){
		im.log.SimpleLogger.logQueue = new im.util.Queue();
	}
}