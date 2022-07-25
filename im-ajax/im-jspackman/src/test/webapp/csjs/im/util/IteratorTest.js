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

Import("im.util.Iterator");
Import("im.jsunit.TestCase");

/**
 * @fileoverview <br/>
 * サイズ変更可能な配列の実装です。
 */

Class("im.util.IteratorTest").extend("im.jsunit.TestCase").define(
	im.util.IteratorTest = function (name) {
		var it;
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
			var list = [];
			list.push(value0);
			list.push(value1);
			list.push(value2);
			list.push(value3);
			list.push(value4);
			list.push(value5);
			list.push(value6);
			it = new im.util.Iterator(list)
		}
		this.testHasNext = function () {
			for (var i = 0; i < 7; i++) {
				assertTrue(it.hasNext());
				it.next();
			}
			assertFalse(it.hasNext());
		}
		this.testNext = function() {
			assertEquals(value0, it.next());
			assertEquals(value1, it.next());
			assertEquals(value2, it.next());
			assertEquals(value3, it.next());
			assertEquals(value4, it.next());
			assertEquals(value5, it.next());
			assertEquals(value6, it.next());
			assertFalse(it.hasNext());
		}
		this.testRemove = function () {
			for (var i = 0; i < 7; i++) {
				it.next();
				it.remove();
			}
			assertFalse(it.hasNext());
		}
	}
);
