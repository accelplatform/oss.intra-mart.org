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

Package("im.util");

Import("im.util.Stack");
Import("im.util.EmptyStackException");
Import("im.jsunit.TestCase");

/**
 * @fileoverview <br/>
 * サイズ変更可能な配列の実装です。
 */

Class("im.util.StackTest").extend("im.jsunit.TestCase").define(
	im.util.StackTest = function (name) {
		var stack;
		var value0 = "aa";
		var value1 = ["bb"];
		var value2 = value0;
		var value3 = null;
		var value4 = undefined;
		var value5 = 100;
		var value6 = {key: "cc"};
		/* constructor */
		{
			this.superclass(name);
		}
		this.setUp = function () {
			stack = new im.util.Stack();
			stack.push(value0);
			stack.push(value1);
			stack.push(value2);
			stack.push(value3);
			stack.push(value4);
			stack.push(value5);
			stack.push(value6);
		}
		this.testEmpty = function () {
			assertFalse(stack.empty());
		}
		this.testSize = function () {
			assertEquals(7, stack.size());
		}
		this.testPop = function () {
			assertEquals(value6, stack.pop());
			assertEquals(value5, stack.pop());
			assertEquals(value4, stack.pop());
			assertEquals(value3, stack.pop());
			assertEquals(value2, stack.pop());
			assertEquals(value1, stack.pop());
			assertEquals(value0, stack.pop());
			try {
				stack.pop();
				fail();
			} catch(e) {
				assertTrue(e instanceof im.util.EmptyStackException);
			}
		}
		this.testPush = function (item) {
			var value7 = "aa";
			stack.push(value7);
			assertEquals(value7, stack.pop());
		}
	}
);
