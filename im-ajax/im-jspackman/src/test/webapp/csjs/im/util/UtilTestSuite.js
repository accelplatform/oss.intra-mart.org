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

Import("im.util.ListTest");
Import("im.util.IteratorTest");
Import("im.util.MapTest");
Import("im.util.QueueTest");
Import("im.util.StackTest");
Import("im.jsunit.TestSuite");

Class("im.util.UtilTestSuite").define(
	im.util.UtilTestSuite = function () {
		{
			this.superclass();
		}
	}
);
{
	im.util.UtilTestSuite.suite = function(){
		var suite = new im.jsunit.TestSuite();
		suite.addTestSuite(im.util.ListTest.clazz);
		suite.addTestSuite(im.util.IteratorTest.clazz);
		suite.addTestSuite(im.util.MapTest.clazz);
		suite.addTestSuite(im.util.QueueTest.clazz);
		suite.addTestSuite(im.util.StackTest.clazz);
		return suite;
	}
}
