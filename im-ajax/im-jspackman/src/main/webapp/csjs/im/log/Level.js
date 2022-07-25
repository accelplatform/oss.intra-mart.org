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
 * サイズ変更可能な配列の実装です。
 */

Class("im.log.Level").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @class
	 * @param {Number} levelInt
	 * @param {String} levelStr
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.5
	 */
	im.log.Level = function (levelInt, levelStr) {
		/* constructor */
		{
			this.superclass();
		}
		/**
		 * int value of this Level
		 * @final
		 * @type Number
		 */
		this.levelInt = levelInt;
		/**
		 * String value of this Level
		 * @final
		 * @type Number
		 */
		this.levelStr = levelStr;
		/**
		 * Convert the string or int specified as argument to a Level.
		 * If the conversion fails, then this method returns {@link #DEBUG}.
		 * @param {Object} level string or int
		 * @return a Level instance
		 * @type im.log.Level
		 */
		this.toLevel = function (level) {
			if (level instanceof Number) {
				switch (level) {
					case im.log.Level.ALL_INT:
						return im.log.Level.ALL;
					case im.log.Level.TRACE_INT:
						return im.log.Level.TRACE;
					case im.log.Level.DEBUG_INT:
						return im.log.Level.DEBUG;
					case im.log.Level.INFO_INT:
						return im.log.Level.INFO;
					case im.log.Level.WARN_INT:
						return im.log.Level.WARN;
					case im.log.Level.ERROR_INT:
						return im.log.Level.ERROR;
					case im.log.Level.OFF_INT:
						return im.log.Level.OFF;
					default:
						return im.log.Level.DEBUG;
				}
			} else if (level instanceof String) {
				if (level.equalsIgnoreCase("ALL")) {
					return im.log.Level.ALL;
				}
				if (level.equalsIgnoreCase("TRACE")) {
					return im.log.Level.TRACE;
				}
				if (level.equalsIgnoreCase("DEBUG")) {
					return im.log.Level.DEBUG;
				}
				if (level.equalsIgnoreCase("INFO")) {
					return im.log.Level.INFO;
				}
				if (level.equalsIgnoreCase("WARN")) {
					return im.log.Level.WARN;
				}
				if (level.equalsIgnoreCase("ERROR")) {
					return im.log.Level.ERROR;
				}
				if (level.equalsIgnoreCase("OFF")) {
					return im.log.Level.OFF;
				}
				return im.log.Level.DEBUG;
			} else {
				throw new im.lang.IllegalArgumentException("argument of level is not instance of Number nor String");
			}
		}
		/**
		 * Returns <code>true</code> if this Level has a higher or equal Level than
		 * the Level specified as argument, <code>false</code> otherwise.
		 * @param {im.log.Level} level
		 * @return Returns true if this Level has a higher or equal Level, false otherwise.
		 * @type Boolean
		 */
		this.isGreaterOrEqual = function (level) {
			return levelInt >= level.levelInt;
		}
		/**
		 * Returns the string representation of this Level.
		 * @return the string representation of this Level
		 * @type String
		 */
		this.toString = function () {
			return levelStr;
		}
	}
);
/* static field and static method of im.log.Level class */
{
	/**
	 * @final
	 * @Number
	 */
	im.log.Level.OFF_INT = Number.MAX_VALUE;
	/**
	 * @final
	 * @Number
	 */
	im.log.Level.ERROR_INT = 40000;
	/**
	 * @final
	 * @Number
	 */
	im.log.Level.WARN_INT = 30000;
	/**
	 * @final
	 * @Number
	 */
	im.log.Level.INFO_INT = 20000;
	/**
	 * @final
	 * @Number
	 */
	im.log.Level.DEBUG_INT = 10000;
	/**
	 * @final
	 * @Number
	 */
	im.log.Level.TRACE_INT = 5000;
	/**
	 * @final
	 * @Number
	 */
	im.log.Level.ALL_INT = Number.MIN_VALUE;
	/**
	 * The <code>OFF</code> is used to turn off logging.
	 * @final
	 * @type im.log.Level
	 */
	im.log.Level.OFF;
	/**
	 * The <code>ERROR</code> level designates error events which may or not
	 * be fatal to the application.
	 * @final
	 * @type im.log.Level
	 */
	im.log.Level.ERROR;
	/**
	 * The <code>WARN</code> level designates potentially harmful situations.
	 * @final
	 * @type im.log.Level
	 */
	im.log.Level.WARN;
	/**
	 * The <code>INFO</code> level designates informational messages
	 * highlighting overall progress of the application.
	 * @final
	 * @type im.log.Level
	 */
	im.log.Level.INFO;
	/**
	 * The <code>DEBUG</code> level designates informational events of lower importance.
	 * @final
	 * @type im.log.Level
	 */
	im.log.Level.DEBUG;
	/**
	 * The <code>TRACE</code> level designates informational events of very low importance.
	 * @final
	 * @type im.log.Level
	 */
	im.log.Level.TRACE;
	/**
	 * The <code>ALL</code> is used to turn on all logging.
	 * @final
	 * @type im.log.Level
	 */
	im.log.Level.OFF;
	/**
	 * @private
	 */
	im.log.Level.initialize = function () {
		im.log.Level.OFF   = new Level(im.log.Level.OFF_INT, "OFF");
		im.log.Level.ERROR = new Level(im.log.Level.ERROR_INT, "ERROR");
		im.log.Level.WARN  = new Level(im.log.Level.WARN_INT, "WARN");
		im.log.Level.INFO  = new Level(im.log.Level.INFO_INT, "INFO");
		im.log.Level.DEBUG = new Level(im.log.Level.DEBUG_INT, "DEBUG");
		im.log.Level.TRACE = new Level(im.log.Level.TRACE_INT, "TRACE");
		im.log.Level.OFF   = new Level(im.log.Level.OFF_INT, "OFF");
	}
}