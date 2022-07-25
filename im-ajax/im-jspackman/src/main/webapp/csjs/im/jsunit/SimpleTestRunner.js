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

Import("im.jsunit.BaseTestRunner");
Import("im.jsunit.SimpleResultPrinter");
Import("im.jsunit.TestSuite");
Import("im.jsunit.TestResult");

/**
 * @fileoverview
 * このファイルまたはクラスの概要を記述♪
 */

Class("im.jsunit.SimpleTestRunner").extend("im.jsunit.BaseTestRunner").define(
	/**
	 * コンストラクタの説明。
	 * @constructor
	 * @param {im.jsunit.SimpleResultPrinter} resultPrinter 
	 * @class クラスの説明
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.SimpleTestRunner = function (resultPrinter) {
		/* コンストラクタ */
		{
			this.superclass();
		}
		/**
		 * Creates the TestResult to be used for the test run.
		 * @private
		 * @return the TestResult to be used for the test run.
		 * @type im.jsunit.TestResult
		 */
		this.createTestResult = function () {
			return new im.jsunit.TestResult();
		}
		/**
		 * @param {im.jsunit.Test} suite 
		 * @return the TestResult to be used for the test run.
		 * @type im.jsunit.TestResult
		 */
		this.doRun = function (suite) {
			var result = this.createTestResult();
			result.addListener(resultPrinter);
			var startTime = (new Date()).getTime();
			suite.run(result);
			var endTime = (new Date()).getTime();
			var runTime = endTime - startTime;
			resultPrinter.print(result, runTime);
			return result;
		}
		/**
		 * Starts a test run. Analyzes the command line arguments and runs the given
		 * test suite.
		 * @param {String} args 
		 * @return the TestResult to be used for the test run.
		 * @type im.jsunit.TestResult
		 * @throws im.lang.IllegalArgumentException
		 * @throws im.lang.Exception
		 */
		this.start = function (args) {
			var testCase;
			if (args.length != 1 || typeof(args[0]) != "string") {
				throw new im.lang.IllegalArgumentException("arguments is not correct..");
			} else {
				testCase = args[0];
			}
			try {
				var suite = this.getTest(testCase);
				return this.doRun(suite);
			} catch (e) {
				throw new im.lang.Exception("Could not create and run test suite: " + e);
			}
		}
	}
);
/* static field and static method of im.jsunit.SimpleTestRunner class */
{
	im.jsunit.SimpleTestRunner.SUCCESS_EXIT= 0;
	im.jsunit.SimpleTestRunner.FAILURE_EXIT= 1;
	im.jsunit.SimpleTestRunner.EXCEPTION_EXIT= 2;
	/**
	 * Runs a suite extracted from a TestCase subclass.
	 * @param {im.lang.Class} test a test class that extends im.jsunit.Test.
	 * @return test result.
	 * @type im.jsunit.TestResult
	 */
	im.jsunit.SimpleTestRunner.run = function (test) {
		var testSuite;
		var testInstance = test.clazz.newInstance();
		if (testInstance instanceof im.jsunit.TestCase) {
			testSuite = new im.jsunit.TestSuite(test);
		} else if (testInstance instanceof im.jsunit.TestSuite) {
			testSuite = test;
		} else {
			alert("error: argument is not im.jsunit.Test instance...");
		}
		var runner = new im.jsunit.SimpleTestRunner(new im.jsunit.SimpleResultPrinter());
		return runner.doRun(testSuite);
	}
	/**
	 */
	im.jsunit.SimpleTestRunner.main = function (args) {
		var aTestRunner = new im.jsunit.SimpleTestRunner(new im.jsunit.SimpleResultPrinter());
		try {
			var testResult = aTestRunner.start(args);
			if (testResult.wasSuccessful()) {
				alert("SUCCESS_EXIT");
			} else {
				alert("FAILURE_EXIT");
			}
		} catch (e) {
			alert("EXCEPTION_EXIT: " + e.message);
		}
	}
}
