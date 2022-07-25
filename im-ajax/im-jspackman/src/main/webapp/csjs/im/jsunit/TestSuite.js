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

Import("im.jsunit.Test");

/**
 * @fileoverview
 * このファイルまたはクラスの概要を記述♪
 */

Class("im.jsunit.TestSuite").extend("im.jsunit.Test").define(
	/**
	 * コンストラクタ。<br/>
	 * 引数 testCaseClass および name は指定しなくてもよい。
	 * @constructor
	 * @param {im.lang.Class} testCaseClass? {@link im.jsunit.TestCase} のクラス
	 * @class 
	 * <p>A <code>TestSuite</code> is a <code>Composite</code> of Tests.
	 * It runs a collection of test cases. Here is an example using
	 * the dynamic test definition.
	 * <pre><code>
	 * var suite = new im.jsunit.TestSuite();
	 * suite.addTest(new FooTest("foo"));
	 * suite.addTest(new BarTest("bar"));
	 * </code></pre>
	 * </p>
	 * 
	 * <p>Alternatively, a TestSuite can extract the tests to be run automatically.
	 * To do so you pass the class of your TestCase class to the
	 * TestSuite constructor.
	 * <pre>
	 * var suite = new TestSuite(FooTest.class);
	 * </pre>
	 * </p>
	 * <p>This constructor creates a suite with all the methods
	 * starting with "test" that take no arguments.</p>
	 * @extends im.jsunit.Test
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.TestSuite = function (testCaseClass) {
		var fName;
		var fTests;
		/* コンストラクタ */
		{
			this.superclass();
			fTests = [];
			if (testCaseClass) {
				fName = testCaseClass.getName();
				var testCase = testCaseClass.newInstance();
				for (var prop in testCase) {
					if (testCase[prop] instanceof Function) {
						if (prop.startsWith("test")) {
							fTests.push(im.jsunit.TestSuite.createTest(testCaseClass, prop));
						}
					}
				}
				if (fTests.length == 0) {
					fTests.push(im.jsunit.TestSuite.warning("No tests found in " + fName));
				}
			}
		}
		/**
		 * Runs the tests and collects their result in a TestResult.
		 * @param {im.jsunit.TestResult} result 
		 */
		this.run = function (result) {
			for (var i = 0; i < fTests.length; i++) {
				if (result.shouldStop()) {
					break;
				}
				this.runTest(fTests[i], result);
			}
		}
		/**
		 * @param {im.jsunit.Test} test 
		 * @param {im.jsunit.TestResult} result 
		 */
		this.runTest = function (test, result) {
			test.run(result);
		}
		/**
		 * Adds a test to the suite.
		 * @param {im.jsunit.Test} test 
		 */
		this.addTest = function (test) {
			fTests.push(test);
		}
		/**
		 * Adds the tests from the given class to the suite
		 * @param {im.lang.Class} testClass a class that extends {@link im.jsunit.TestCase}
		 */
		this.addTestSuite = function (testCase) {
			this.addTest(new im.jsunit.TestSuite(testCase));
		}
		/**
		 * Counts the number of test cases that will be run by this test.
		 * @return the number of test cases
		 * @type Number
		 */
		this.countTestCases = function () {
			var count= 0;
			for (var i = 0; i < fTests.length; i++) {
				count += fTests[i].countTestCases();
			}
			return count;
		}
		/**
		 * Returns the name of the suite. Not all
		 * test suites have a name and this method
		 * can return null.
		 * @return the name of the suite
		 * @type String
		 */
		this.getName = function () {
			return fName;
		}
		/**
		 * Sets the name of the suite.
		 * @param {String} name the name to set
		 */
		this.setName = function (name) {
			fName = name;
		}
		/**
		 * Returns the test at the given index
		 * @param {Number} index 
		 * @return the test at the given index;
		 * @type im.jsunit.Test
		 */
		this.testAt = function (index) {
			return fTests[index];
		}
		/**
		 * Returns the number of tests in this suite.
		 * @return the number of tests in this suite
		 * @type Number
		 */
		this.testCount = function () {
			return fTests.length;
		}
		/**
		 * Returns the tests as an Array.
		 * @return the tests as an Array
		 * @type Array
		 */
		this.tests = function () {
			return fTests;
		}
		/**
		 * Returns the name of test.
		 * @return the name of test
		 * @type String
		 */
		this.toString = function () {
			return this.getName();
		}
	}
);
/* static field and static method of im.jsunit.TestSuite class */
{
	/**
	 * ...as the moon sets over the early morning Merlin, Oregon
	 * mountains, our intrepid adventurers type...
	 * @param {im.lang.Class} theClass a class of im.jsunit.TestCase
	 * @param {String} name a test name
	 * @return a instance fo {@link im.jsunit.Test}
	 * @type im.jsunit.Test
	 */
	im.jsunit.TestSuite.createTest = function (theClass, name) {
		var test;
		if (theClass instanceof im.lang.Class) {
			test = theClass.newInstance();
			if (test instanceof im.jsunit.TestCase) {
				test.setName(name);
			}
			else {
				throw new im.lang.IllegalArgumentException("theClass is not class of im.jsunit.TestCase.");
			}
		} else {
			throw new im.lang.IllegalArgumentException("theClass argument is not instance of im.lang.Class.");
		}
		return test;
	}
	/**
	 * Returns a test which will fail and log a warning message.
	 * @param {String} message a warning message
	 * @return a test which will fail and log a warning message
	 * @type im.jsunit.Test
	 */
	im.jsunit.TestSuite.warning = function (message) {
		var test = new TestCase("warning");
		test.runTest = function () {
			test.fail(message);
		}
		return test;
	}
}
