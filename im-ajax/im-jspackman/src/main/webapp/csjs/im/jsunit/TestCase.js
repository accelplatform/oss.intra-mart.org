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

Import("im.jsunit.Assert");
Import("im.jsunit.Test");
Import("im.lang.reflect.InvocationTargetException");

/**
 * @fileoverview
 * このファイルまたはクラスの概要を記述♪
 */

Class("im.jsunit.TestCase").extend("im.jsunit.Test").define(
	/**
	 * Constructs a test case with the given name.
	 * @constructor
	 * @param {String} name? the name of the test case
	 * @class A test case defines the fixture to run multiple tests.
	 * @extends im.jsunit.Test
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.TestCase = function (name) {
		var fName;
		/* コンストラクタ */
		{
			this.superclass();
			if (name) {
				fName = name;
			} else {
				fName = null;
			}
		}
		/**
		 * Counts the number of test cases executed by {@link #run}.
		 */
		this.countTestCases = function () {
			return 1;
		}
		/**
		 * Creates a default TestResult object
		 * @private
		 */
		this.createResult = function () {
			return new im.jsunit.TestResult();
		}
		/**
		 * Runs a test and collects its result in a TestResult instance.<br/>
		 * if you do not specify the testResult, default TestResult object is created.
		 * @param {im.jsunit.TestResult} testResult? 
		 */
		this.run = function (testResult) {
			if (!(testResult instanceof im.jsunit.TestResult)) {
				testResult = this.createResult();
			}
			testResult.run(this);
			return testResult;
		}
		/**
		 * Runs the bare test sequence.
		 * @throws Throwable if any exception is thrown
		 */
		this.runBare = function () {
			var exception = null;
			this.setUp();
			try {
				this.runTest();
			} catch (running) {
				exception = running;
			} finally {
				try {
					this.tearDown();
				} catch (tearingDown) {
					if (exception == null) {
						exception = tearingDown;
					}
				}
			}
			if (exception != null) {
				throw exception;
			}
		}
		/**
		 * Override to run the test and assert its state.
		 * @throws Throwable if any exception is thrown
		 */
		this.runTest = function () {
			im.jsunit.Assert.assertNotNull(fName);
			var runMethod = null;
			try {
				runMethod = this.getClass().getMethod(fName);
			} catch (e) {
				if (e instanceof im.lang.NoSuchMethodException) {
					im.jsunit.Assert.fail("Method \"" + fName + "\" not found");
				}
			}
			try {
				runMethod.invoke(this);
			} catch (e) {
				if (e instanceof im.lang.reflect.InvocationTargetException) {
					throw e.getTargetException();
				}
			}
		}
		/**
		 * Sets up the fixture, for example, open a network connection.
		 * This method is called before a test is executed.
		 */
		this.setUp = function () {}
		/**
		 * Tears down the fixture, for example, close a network connection.
		 * This method is called after a test is executed.
		 */
		this.tearDown = function () {}
		/**
		 * Returns a string representation of the test case.
		 * @return a string representation of the test case.
		 * @type String
		 */
		this.toString = function () {
			return this.getName() + "(" + this.getClass().getName() + ")";
		}
		/**
		 * Gets the name of a TestCase
		 * @return the name of the TestCase
		 */
		this.getName = function () {
			return fName;
		}
		/**
		 * Sets the name of a TestCase
		 * @param {String} name the name to set
		 */
		this.setName = function (name) {
			fName = name;
		}
	}
);
