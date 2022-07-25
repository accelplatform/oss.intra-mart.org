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

Import("im.jsunit.TestListener");
Import("im.jsunit.TestSuite");
//Import("im.text.NumberFormat");

/**
 * @fileoverview
 * このファイルまたはクラスの概要を記述♪
 */

Class("im.jsunit.BaseTestRunner").extend("im.jsunit.TestListener").define(
	/**
	 * コンストラクタの説明。
	 * @constructor
	 * @param 引数名 {型} 引数の説明
	 * @class クラスの説明
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.BaseTestRunner = function () {
		var fLoading;
		/* コンストラクタ */
		{
			this.superclass();
			fLoading = true;
		}
		/**
		 * An error occurred.
		 * @param {im.jsunit.Test} test 
		 * @param {im.lang.Throwable} t 
		 */
		this.addError = function (test, t) {
			this.testFailed(im.jsunit.TestListener.STATUS_ERROR, test, t);
		}
		/**
		 * A failure occurred.
		 * @param {im.jsunit.Test} test 
		 * @param {im.jsunit.AssertionFailedError} t 
		 */
		this.addFailure = function (test, t) {
			this.testFailed(im.jsunit.TestListener.STATUS_FAILURE, test, t);
		}
		/**
		 * A test ended.
		 * @param {im.jsunit.Test} test 
		 */
		this.endTest = function (test) {
			this.testEnded(test.toString());
		}
		/**
		 * A test started.
		 * @param {im.jsunit.Test} test 
		 */
		this.startTest = function (test) {
			this.testStarted(test.toString());
		}
		/**
		 * @param {String} testName 
		 */
		this.testStarted = function (testName) {}
		/**
		 * @param {String} testName 
		 */
		this.testEnded = function (testName) {}
		/**
		 * @param {Number} testName 
		 * @param {im.jsunit.Test} test 
		 * @param {im.lang.Throwable} t 
		 */
		this.testFailed = function (status, test, t) {}
		/**
		 * Returns the Test corresponding to the given suite. This is
		 * a template method, subclasses override runFailed(), clearStatus().
		 * @param {String} suiteClassName 
		 * @return the Test corresponding to the given suite
		 * @type im.jsunit.Test
		 */
		this.getTest = function (suiteClassName) {
			if (suiteClassName.length <= 0) {
				this.clearStatus();
				return null;
			}
			var testClass = null;
			try {
				testClass = this.loadSuiteClass(suiteClassName);
			} catch (e) {
				if (e instanceof im.lang.ClassNotFoundException) {
					this.runFailed("Class not found \"" + suiteClassName +"\"");
					return null;
				} else {
					this.runFailed("Error: " + e.toString());
					return null;
				}
			}
			var suiteMethod = null;
			try {
				suiteMethod = testClass.getMethod(im.jsunit.BaseTestRunner.SUITE_METHODNAME);
	 		} catch(e) {
	 			// try to extract a test suite automatically
				this.clearStatus();
				return new im.jsunit.TestSuite(testClass);
			}
			var test = null;
			try {
				test = suiteMethod.invoke(testClass.newInstance(), null);
				if (test == null) {
					return test;
				}
			} catch (e) {
				this.runFailed("Failed to invoke suite():" + e.toString());
				return null;
			}
			this.clearStatus();
			return test;
		}
		/**
		 * Returns the formatted string of the elapsed time.
		 * @param {Number} runTime 
		 * @return the formatted string of the elapsed time
		 * @type String
		 */
		this.elapsedTimeAsString = function (runTime) {
			//return im.text.NumberFormat.getInstance().format(runTime/1000.0);
			return runTime/1000.0;
		}
		/**
		 * Sets the loading behaviour of the test runner
		 * @param {Boolean} enable 
		 */
		this.setLoading = function (enable) {
			fLoading = enable;
		}
		/**
		 * Extract the class name from a String.
		 * @param {String} className a class name.
		 * @return a class name that is extracted package name
		 * @type String
		 */
		this.extractClassName = function (className) {
			return className.substring(className.lastIndexOf(".") + 1);
		}
		/**
		 * Override to define how to handle a failed loading of a test suite.
		 * @param {String} message 
		 */
		this.runFailed = function (message) {}
		/**
		 * Returns the loaded Class for a suite name.
		 * @param {String} suiteClassName
		 * @return the loaded Class for a suite name
		 * @type im.lang.Class
		 * @throws ClassNotFoundException
		 */
		this.loadSuiteClass = function (suiteClassName) {
			return im.lang.Class.forName(suiteClassName);
		}
		/**
		 * Clears the status message.
		 */
		this.clearStatus = function() {}
	}
);
/* static field and static method of im.jsunit.BaseTestRunner class */
{
	/**
	 * @final 
	 * @type String
	 */
	im.jsunit.BaseTestRunner.SUITE_METHODNAME = "suite";
}
