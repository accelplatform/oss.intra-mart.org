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

/**
 * @fileoverview
 * このファイルまたはクラスの概要を記述♪
 */

Class("im.jsunit.Test").extend("im.jsunit.Assert").define(
	/**
	 * Constructs a test.
	 * @constructor
	 * @class A Test can be run and collect its results.
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.Test = function () {
		/* コンストラクタ */
		{
			this.superclass();
		}
		/**
		 * Counts the number of test cases executed by {@link #run}.
		 */
		this.countTestCases = function () {}
		/**
		 * Runs a test and collects its result in a TestResult instance.
		 * @param {im.jsunit.TestResult} testResult 
		 */
		this.run = function (testResult) {}
	}
);
