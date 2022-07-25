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

Import("im.log.SimpleLogger");

/**
 * @fileoverview <br/>
 * ロガーファクトリー
 */

Class("im.log.LoggerFactory").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @class
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.5
	 */
	im.log.LoggerFactory = function (name) {
		var loggers;
		/* constructor */
		{
			this.superclass();
			loggers = [];
		}
		/**
		 * Return an appropriate {@link im.log.Logger} instance as specified by the
		 * <code>name</code> parameter.
		 * @private
		 * @param {String} name the name of the Logger
		 * @return 引数でしてされた Logger のインスタンス
		 * @type im.log.Logger
		 */
		this._getLogger = function (name) {
			// TODO this is prototype...
			return new im.log.SimpleLogger(name);
		}
	}
);
/* static field and static method of im.log.LoggerFactory class */
{
	/** @private */
	im.log.LoggerFactory.singleton;
	/**
	 * 引数で指定されたクラスの Logger を取得します。
	 * @param {im.lang.Class} clazz? クラス ※省略可能。
	 * @return 引数で指定された Logger
	 * @type im.log.Logger
	 */
	im.log.LoggerFactory.getLogger = function (clazz) {
		if ((clazz instanceof im.lang.Class)) {
			return im.log.LoggerFactory.singleton._getLogger(clazz.getName());
		} else {
			return im.log.LoggerFactory.singleton._getLogger("default");
		}
	}
	/** @private */
	im.log.LoggerFactory.initialize = function(){
		im.log.LoggerFactory.singleton = new im.log.LoggerFactory();
	}
}
