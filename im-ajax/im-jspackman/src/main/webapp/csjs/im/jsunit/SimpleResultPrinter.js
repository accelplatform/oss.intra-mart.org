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
//Import("im.text.NumberFormat");

/**
 * @fileoverview
 * このファイルまたはクラスの概要を記述♪
 */

Class("im.jsunit.SimpleResultPrinter").extend("im.jsunit.TestListener").define(
	/**
	 * コンストラクタの説明。
	 * @constructor
	 * @class クラスの説明
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.SimpleResultPrinter = function () {
		/* コンストラクタ */
		{
			this.superclass();
		}
		/**
		 * @param {im.jsunit.TestResult} result 
		 * @param {Number} runTime 
		 */
		this.print = function(result, runTime){
			this.printHeader(runTime);
			this.printErrors(result);
			this.printFailures(result);
			this.printFooter(result);
		}
		/** @private */
		this.printHeader = function (runTime) {
			alert("Time: "+ this.elapsedTimeAsString(runTime));
		}
		/** @private */
		this.printErrors = function (result) {
			this.printDefects(result.errors(), result.errorCount(), "error");
		}
		/** @private */
		this.printFailures = function(result) {
			this.printDefects(result.failures(), result.failureCount(), "failure");
		}
		/** @private */
		this.printDefects = function (booBoos, count, type) {
			if (count == 0) {
				return;
			} else if (count == 1) {
				alert("There was " + count + " " + type + ":");
			} else {
				alert("There were " + count + " " + type + "s:");
				for (var i = 1; booBoos.length; i++) {
					this.printDefectHeader(booBoo, count);
					this.printDefectTrace(booBoo);
				}
			}
		}
		/** @private */
		this.printDefectHeader = function (booBoo, count) {
			alert(count + ") " + booBoo.failedTest());
		}
		/** @private */
		this.printDefectTrace = function (booBoo) {
			alert(booBoo.trace());
		}
		/** @private */
		this.printFooter = function (result) {
			if (result.wasSuccessful()) {
				alert("OK");
				alert(" (" + result.runCount() + " test" + (result.runCount() == 1 ? "": "s") + ")");
			} else {
				alert("FAILURES!!!");
				alert("Tests run: " + result.runCount() +
						",  Failures: " + result.failureCount() +
						",  Errors: " + result.errorCount());
			}
		}
		/**
		 * Returns the formatted string of the elapsed time.
		 * Duplicated from BaseTestRunner. Fix it.
		 * @param {Number} runTime 
		 * @return the loaded Class for a suite name
		 * @type String
		 */
		this.elapsedTimeAsString = function (runTime) {
//			return im.text.NumberFormat.getInstance().format(runTime/1000.0);
			return runTime/1000.0;
		}
		/**
		 * An error occurred.
		 * @param {im.jsunit.Test} test 
		 * @param {im.lang.Throwable} t 
		 */
		this.addError = function (test, t) {
			alert("ERROR: " + test.getName());
		}
		/**
		 * A failure occurred.
		 * @param {im.jsunit.Test} test 
		 * @param {im.jsunit.AssertionFailedError} t 
		 */
		this.addFailure = function (test, t) {
			alert("FAILURE: " + test.getName());
		}
		/**
		 * A test ended.
		 * @param {im.jsunit.Test} test 
		 */
		this.endTest = function (test) {
			//alert("END: " + test.getName());
		}
		/**
		 * A test started.
		 * @param {im.jsunit.Test} test 
		 */
		this.startTest = function (test) {
			//alert("START: " + test.getName());
		}
	}
);
