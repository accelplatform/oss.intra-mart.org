/*
 * Copyright 2007 the OPEN INTRA-MART.
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

Package("im.jsunit");

/**
 * @fileoverview
 * A Listener for test progress.
 */

Class("im.jsunit.TestListener").define(
	/**
	 * Constructs a test listener.
	 * @constructor
	 * @class A Listener for test progress.
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.TestListener = function () {
		/* コンストラクタ */
		{
			this.superclass();
		}
		/**
		 * An error occurred.
		 * @param {im.jsunit.Test} test 
		 * @param {im.lang.Throwable} t 
		 */
		this.addError = function (test, t) {}
		/**
		 * A failure occurred.
		 * @param {im.jsunit.Test} test 
		 * @param {im.jsunit.AssertionFailedError} t 
		 */
		this.addFailure = function (test, t) {}
		/**
		 * A test ended.
		 * @param {im.jsunit.Test} test 
		 */
		this.endTest = function (test) {}
		/**
		 * A test started.
		 * @param {im.jsunit.Test} test 
		 */
		this.startTest = function (test) {}
	}
);
/* static field and static method of im.jsunit.TestListener class */
{
	/**
	 * @final
	 * @type Number
	 */
	im.jsunit.TestListener.STATUS_ERROR = 1;
	/**
	 * @final
	 * @type Number
	 */
	im.jsunit.TestListener.STATUS_FAILURE = 2;
}
