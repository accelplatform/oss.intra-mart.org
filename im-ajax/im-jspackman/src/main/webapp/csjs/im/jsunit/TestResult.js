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

Import("im.util.List");
Import("im.jsunit.AssertionFailedError");
Import("im.jsunit.TestFailure");

/**
 * @fileoverview
 * このファイルまたはクラスの概要を記述♪
 */

Class("im.jsunit.TestResult").define(
	/**
	 * コンストラクタの説明。
	 * @constructor
	 * @param 引数名 {型} 引数の説明
	 * @class クラスの説明
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.TestResult = function () {
		var fFailures;
		var fErrors;
		var fListeners;
		var fRunTests;
		var fStop;
		/* コンストラクタ */
		{
			this.superclass();
			fFailures  = new im.util.List();
			fErrors    = new im.util.List();
			fListeners = new im.util.List();
			fRunTests  = 0;
			fStop      = false;
		}
		/**
		 * Adds an error to the list of errors. The passed in exception
		 * caused the error.
		 * @param {im.jsunit.Test} test 
		 * @param {im.lang.Throwable} t 
		 */
		this.addError = function (test, t) {
			fErrors.add(new im.jsunit.TestFailure(test, t));
			var it = fListeners.iterator();
			while (it.hasNext()) {
				var aListener = it.next();
				aListener.addError(test, t);
			}
		}
		/**
		 * Adds a failure to the list of failures. The passed in exception
		 * caused the failure.
		 * @param {im.jsunit.Test} test 
		 * @param {im.lang.AssertionFailedError} t 
		 */
		this.addFailure = function (test,  t) {
			fFailures.add(new im.jsunit.TestFailure(test, t));
			var it = fListeners.iterator();
			while (it.hasNext()) {
				var aListener = it.next();
				aListener.addFailure(test, t);
			}
		}
		/**
		 * Registers a TestListener
		 * @param {im.jsunit.TestListener} listener 
		 */
		this.addListener = function (listener) {
			fListeners.add(listener);
		}
		/**
		 * Unregisters a TestListener
		 * @param {im.jsunit.TestListener} listener 
		 */
		this.removeListener = function (listener) {
			fListeners.remove(fListeners.indexOf(listener));
		}
		/**
		 * Informs the result that a test was completed.
		 * @param {im.jsunit.Test} test 
		 */
		this.endTest = function (test) {
			var it = fListeners.iterator();
			while (it.hasNext()) {
				var aListener = it.next();
				aListener.endTest(test);
			}
		}
		/**
		 * Gets the number of detected errors.
		 * @return the number of detected errors.
		 * @type Number
		 */
		this.errorCount = function () {
			return fErrors.size();
		}
		/**
		 * Returns an Iterator for the errors.
		 * @return an Iterator for the errors.
		 * @type im.util.Iterator
		 */
		this.errors = function () {
			return fErrors.iterator();
		}
		/**
		 * Gets the number of detected failures.
		 * @return the number of detected failures.
		 * @type Number
		 */
		this.failureCount = function () {
			return fFailures.size();
		}
		/**
		 * Returns an Iterator for the failures.
		 * @return an Iterator for the failures.
		 * @type im.util.Iterator
		 */
		this.failures = function () {
			return fFailures.iterator();
		}
		
		/**
		 * Runs a TestCase.
		 * @private
		 * @param {im.jsunit.TestCase} test 
		 */
		this.run = function (test) {
			this.startTest(test);
			var Protectable = function () {
				this.protect = function () {
					test.runBare();
				}
			};
			this.runProtected(test, new Protectable());
			this.endTest(test);
		}
		/**
		 * Gets the number of run tests.
		 * @return the number of run tests.
		 * @type Number
		 */
		this.runCount = function () {
			return fRunTests;
		}
		/**
		 * Runs a TestCase.
		 * @param {im.jsunit.Test} test 
		 * @param {im.jsunit.Protectable} p 
		 */
		this.runProtected = function (test, p) {
			try {
				p.protect();
			} catch (e) {
				if (e instanceof im.jsunit.AssertionFailedError) {
					this.addFailure(test, e);
				} else {
					this.addError(test, e);
				}
			}
		}
		/**
		 * Checks whether the test run should stop.
		 * @return whether the test run should stop.
		 * @type Boolean
		 */
		this.shouldStop = function () {
			return fStop;
		}
		/**
		 * Informs the result that a test will be started.
		 * @param {im.jsunit.Test} test 
		 */
		this.startTest = function (test) {
			var count = test.countTestCases();
			fRunTests += count;
			var it = fListeners.iterator();
			while (it.hasNext()) {
				var aListener = it.next();
				aListener.startTest(test);
			}
		}
		/**
		 * Marks that the test run should stop.
		 */
		this.stop = function () {
			fStop = true;
		}
		/**
		 * Returns whether the entire test was successful or not.
		 * @return whether the entire test was successful or not.
		 * @type Boolean
		 */
		this.wasSuccessful = function () {
			return (this.failureCount() == 0 && this.errorCount() == 0);
		}
	}
);
