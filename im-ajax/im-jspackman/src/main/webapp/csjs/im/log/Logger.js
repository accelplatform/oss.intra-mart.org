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

Class("im.log.Logger").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @class
	 * ログ出力機能を提供するクラスのインターフェースです。<br/>
	 * Logger で使用される5つのログレベルは(順番に)以下のようになります。
	 * <ol>
	 *  <li>trace (最も軽微)</li>
	 *  <li>debug</li>
	 *  <li>info</li>
	 *  <li>warn</li>
	 *  <li>error (最も重大)</li>
	 * </ol>
	 * インスタンスは {@link im.log.LoggerFactory#getLogger}で取得してください。
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.5
	 */
	im.log.Logger = function (name) {
		var logger;
		/* constructor */
		{
			this.superclass();
		}
		/**
		 * traceレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.trace = function (message) {}
		/**
		 * debugレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.debug = function (message) {}
		/**
		 * infoレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.info = function (message) {}
		/**
		 * warnレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.warn = function (message) {}
		/**
		 * errorレベルのログを出力します。<br/>
		 * @param {String} message　メッセージ文字列
		 */
		this.error = function (message) {}
		/**
		 * Logger のインスタンス名を取得します。
		 * @return Logger のインスタンス名
		 * @type String
		 */
		this.getName = function () {}
		/**
		 * traceレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return traceレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isTraceEnabled = function () {}
		/**
		 * debugレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return debugレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isDebugEnabled = function () {}
		/**
		 * infoレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return infoレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isInfoEnabled = function () {}
		/**
		 * warnレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return warnレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isWarnEnabled = function () {}
		/**
		 * errorレベルのログ処理が現在有効かどうかチェックします。<br/>
		 * @return errorレベルのログ処理が有効の場合は true, それ以外は false
		 * @type Boolean
		 */
		this.isErrorEnabled = function () {}
	}
);