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

Import("im.util.Queue");
Import("im.jsunit.TestCase");

/**
 * @fileoverview <br/>
 * サイズ変更可能な配列の実装です。
 */

Class("im.util.QueueTest").extend("im.jsunit.TestCase").define(
	im.util.QueueTest = function (name) {
		var queue;
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
			queue = new im.util.Queue();
			queue.enqueue(value0);
			queue.enqueue(value1);
			queue.enqueue(value2);
			queue.enqueue(value3);
			queue.enqueue(value4);
			queue.enqueue(value5);
			queue.enqueue(value6);
		}
		this.testEmpty = function () {
			assertFalse(queue.empty());
		}
		this.testSize = function () {
			assertEquals(7, queue.size());
		}
		this.testDequeue = function () {
			assertEquals(value0, queue.dequeue());
			assertEquals(value1, queue.dequeue());
			assertEquals(value2, queue.dequeue());
			assertEquals(value3, queue.dequeue());
			assertEquals(value4, queue.dequeue());
			assertEquals(value5, queue.dequeue());
			assertEquals(value6, queue.dequeue());
			assertEquals(null,   queue.dequeue());
		}
		this.testEnqueue = function (item) {
			var value7 = "aa";
			queue.enqueue(value7);
			assertEquals(value0, queue.dequeue());
			assertEquals(value1, queue.dequeue());
			assertEquals(value2, queue.dequeue());
			assertEquals(value3, queue.dequeue());
			assertEquals(value4, queue.dequeue());
			assertEquals(value5, queue.dequeue());
			assertEquals(value6, queue.dequeue());
			assertEquals(value7, queue.dequeue());
			assertEquals(null,   queue.dequeue());
		}
	}
);
