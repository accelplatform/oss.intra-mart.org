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

/**
 * @fileoverview
 * im-JSPackman (intra-mart JavaScript Package Management).
 */

/*
 * Define the Required Gloval Variables.
 * - im      {Object}   Required top package object for management.
 * - Class   {Function} Define class. see {@link im.lang.ClassLoader#defineClass}.
 * - Import  {Function} Import class. see {@link im.lang.ClassLoader#loadClass}.
 * - Main    {Function} Invoker the main method. see {@link im.lang.ClassLoader#main}.
 * - Package {Function} Define package. see {@link im.lang.ClassLoader#definePackage}.
 */
var im;
var Class;
var Import;
var Main;
var Package;

/* The following code is effective only to Internet Explorer */
/*@cc_on im=document;eval('var document=im')@*/

/* initialize for defining the im.lang.ClassLoader class. */
im = {};
im.lang = {};

/**
 * Constructor.
 * @constructor
 * @class
 * Class Object is the root of the class hierarchy in Packman's classes.<br/>
 * Every class has Object as a superclass.
 * @param {im.lang.Class} _clazz class definition.
 * @author emooru
 * @version 0.2
 */
im.lang.PackmanObject = function (_clazz) {
	var simpleHashcode;
	/* constructor */
	{
		/* instance identifier, but the same identifier is offen generated.*/
		simpleHashcode = Math.random();
		if (!_clazz) {
			_clazz = im.lang.Class.forName(im.lang.Class.SUPER_CLASS_NAME);
		}
	}
	/**
	 * Returns the runtime class of an object. 
	 * @return the object of type Class that represents the runtime class of the object.
	 * @type im.lang.Class
	 */
	this.getClass = function () {
		return _clazz;
	}
	/** @private */
	this.setClass = function (clazz) {
		_clazz = clazz;
	}
	/**
	 * Returns a string representation of the object.
	 * @return a string representation of the object.
	 * @type String 
	 */
	this.toString = function () {
		return _clazz.getName() + "@" + simpleHashcode;
	}
	/**
	 * Returns a hash code value for the object.
	 * @return a hash code value for this object.
	 * @type Number
	 */
	this.hashCode = function () {
		return simpleHashcode;
	}
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @param {Object} obj the object to check
	 * @return true if this object is the same as the obj argument; false otherwise.
	 * @tyep Boolean
	 */
	this.equals = function (obj) {
		return (this === obj);
	}
}
/**
 * [Note] Please use {@link #forName} for getting Instance.
 * @constructor
 * @class
 * Javascript Package Class.
 * @author emooru
 * @version 0.2
 */
im.lang.Class = function (clazzName, _clazz, _initialized) {
	var _superClazzName;
	var _denyGlobalize;
	var _extendNative;
	var _staticInitialized;
	/* constructor */
	{
		im.lang.Class.SUPER_CLASS.call(this, this);
		_superClazzName    = im.lang.Class.SUPER_CLASS_NAME;
		_denyGlobalize     = false;
		_extendNative      = false;
		_initialized       = (_initialized ? true : false);
		// if you set the _initialized to true, you need to invoke the static initilize method.
		_staticInitialized = _initialized;
	}
	/**
	 * inherit superClazz.
	 * <br/><br/>
	 * [NOTE] the following prototype properties are added.<br/>
	 * <ul>
	 * <li>superclass {Function}: function of super class.</li>
	 * <li>clazz {Function}: function of self class definition.</li>
	 * </ul>
	 * @param {String} superClazzName super class name
	 * @return this.
	 * @type im.lang.Class
	 */
	this.extend = function (superClazzName) {
		_superClazzName = superClazzName;
		return this;
	}
	/**
	 * extend Native Object(Class).
	 * <br/><br/>
	 * [NOTE] the following prototype properties are added.<br/>
	 * <ul>
	 * <li>clazz {Function}: function of self class definition.</li>
	 * </ul>
	 * @param {String} nativeClazzName native class name
	 * @return this.
	 * @type im.lang.Class
	 */
	this.extendNative = function (nativeClazzName) {
		_superClazzName = nativeClazzName;
		_extendNative   = true;
		return this;
	}
	/**
	 * Indicates whether this class extend native class.
	 * @return true if this class extend native class; false otherwise.
	 * @type Boolean
	 */
	this.isExtendNative = function() {
		return _extendNative;
	}
	/**
	 * deny globalize.
	 * @return this class object.
	 * @type im.lang.Class
	 */
	this.denyGlobalize = function () {
		_denyGlobalize = true;
		return this;
	}
	/** @private */
	this.wasDeniedGlobalize = function () {
		return _denyGlobalize;
	}
	/**
	 * define the Class.
	 * <br/><br/>
	 * [NOTE] the following prototype properties are added.<br/>
	 * <ul>
	 * <li>clazz {Function}: function of self class.</li>
	 * </ul>
	 * @throws Error if the class is not assigned the Function Object.
	 * @param {Function} clazz class definition.
	 * @return this.
	 * @type im.lang.Class
	 */
	this.define = function (clazz) {
		if (clazz.prototype.constructor == Function) {
			throw new im.lang.JSPackmanError("'" + clazzName + "' is not Function Object.");
		}
		_clazz = clazz;
		// add the this to the ClassLoader.
		im.lang.ClassLoader.getSystemClassLoader().addClass(clazzName, this);
		// add the clazz property.
		_clazz.clazz = this;
		// replace to the new clazz.
		eval(clazzName + " = _clazz;");
		// notify the class is loaded.
		setTimeout("im.lang.ClassLoader.getSystemClassLoader().notifyClassLoaded('" + clazzName + "')", 1);
		// return this instacne for initialization on defining the class.
		return this;
	}
	/** @private */
	this.defineLocal = function (clazz) {
		_clazz = clazz;
		// add the this to the ClassLoader.
		im.lang.ClassLoader.getSystemClassLoader().addClass(clazzName, this);
		// add the clazz property.
		_clazz.clazz = this;
		// return this instacne for initialization on defining the class.
		return this;
	}
	/**
	 * Creates a new instance of the class represented by this Class object.
	 * @return a newly allocated instance of the class represented by this object.
	 * @type Object
	 */
	this.newInstance = function () {
		return new _clazz();
	}
	/**
	 * Returns a specified method.
	 * @param {String} name a method name.
	 * @return a specified method.
	 * @type im.lang.reflect.Method
	 * @throws im.lang.NoSuchMethodException();
	 */
	this.getMethod = function (name) {
		// 1. search a class method of specified name.
		if (_clazz[name] instanceof Function) {
			return new im.lang.reflect.Method(name, _clazz[name], this);
		}
		// 2. search a class member method of specified name.
		var instance = this.newInstance();
		if (instance[name] instanceof Function) {
			return new im.lang.reflect.Method(name, instance[name], this);
		}
		// not found...
		throw new im.lang.NoSuchMethodException("A method of '" + name + "' is not Found..");
	}
	/** @private  */
	this.getConstructor = function () {
		return _clazz;
	}
	/** private */
	this.initialize = function () {
		if (!_initialized) {
			_initialized = true;
			// inherit.
			var superClazz = im.lang.Class.forName(_superClazzName).initialize();
			if (_extendNative) {
				// extends Native Object. ex: Object, String.
				var superClazzPrototype = superClazz.getConstructor().prototype;
				var TempClass = new _clazz;
				for (var prop in TempClass) {
					superClazzPrototype[prop] = TempClass[prop];
				}
				_clazz = superClazz.getConstructor();
			} else {
				this.inherit(superClazz.getConstructor());
			}
		}
		return this;
	}
	/** @private */
	this.inherit = function (superClazz) {
		var superClazzPrototype = superClazz.prototype;
		var TempClass = function() {};
		TempClass.prototype = superClazzPrototype;
		_clazz.prototype = new TempClass;
		_clazz.prototype.constructor = _clazz;
		_clazz.prototype.superclass = function () {
			var originalSuperClazz = this.superclass;
			this.superclass = superClazz.prototype.superclass || null;
			superClazz.apply(this, arguments);
			this.setClass(im.lang.Class.forName(clazzName));
			if (this.constructor == _clazz) {
				delete this.superclass;
			} else {
				this.superclass = originalSuperClazz;
			}
		};
	}
	/**
	 * Returns the superclass of the class represented by this object.<br/>
	 * <br/>
	 * If this Class represents the Object class, then null is returned.
	 * @return the superclass of the class represented by this object.
	 * @type im.lang.Class
	 */
	this.getSuperclass = function () {
		if (_superClazzName == im.lang.Class.SUPER_CLASS_NAME) {
			return null;
		} else {
			return im.lang.Class.forName(_superClazzName);
		}
	}
	/**
	 * Returns the name of the class.
	 * @return the name of the class.
	 * @type String
	 */
	this.getName = function () {
		return clazzName;
	}
	/** @private */
	this.wasStaticInitialized = function () {
		return _staticInitialized;
	}
	/** @private */
	this.setStaticInitialized = function (staticInitialized) {
		_staticInitialized = staticInitialized;
	}
	/** @private */
	this.invokeStaticInitialize = function () {
		if (!_staticInitialized) {
			_staticInitialized = true;
			if (_clazz.initialize) {
				_clazz.initialize.invoke(null, null);
			}
		}
	}
	/**
	 * Returns the url of specified resource.<br/>
	 * <br/>
	 * return null if it is not found.
	 * @param {String} name
	 * @return the url of specified resource. return null if it is not found.
	 * @type String
	 */
	this.getResource = function (name) {
		// TODO use the classloader which is loaded this class.
		var cl = im.lang.ClassLoader.getSystemClassLoader();
		return cl.getTargetClassPath(clazzName).path + this.resolveName(name);
	}
	/**
	 * Add a package name prefix if the name is not absolute Remove leading "/"
	 * if name is absolute
	 * @private
	 */
	this.resolveName = function (name) {
		if (name.startsWith("/")) {
			return name.substring(1);
		} else {
			return clazzName.substring(0, clazzName.lastIndexOf(".")).replace(/\./g, "/") + "/" + name;
		}
	}
}
/* static field and static method of im.lang.Class class */
{
	/**
	 * The SUPER_CLASS is the root of the class hierarchy.
	 * @final
	 * @type Function
	 */
	im.lang.Class.SUPER_CLASS = im.lang.PackmanObject;
	/**
	 * The SUPER_CLASS_NAME is the root of the class hierarchy.
	 * @final
	 * @type String
	 */
	im.lang.Class.SUPER_CLASS_NAME = "im.lang.PackmanObject";
	/** @private */
	im.lang.Class.clazz = new im.lang.Class("im.lang.Class", im.lang.Class, true);
	/**
	 * Returns the Class object associated with the class.
	 * @param {String} clazzName inheritor class.
	 * @param {im.lang.ClassLoader} classloader? a class loader.
	 * @return the Class object for the class with the specified name.
	 * @type im.lang.Class
	 * @throws im.lang.ClassNotFoundException if the class is not found or have not loaded yet.
	 */
	im.lang.Class.forName = function (clazzName, classloader) {
		if (classloader) {
			return classLoader.findClass(clazzName);
		} else {
			return im.lang.ClassLoader.getSystemClassLoader().findClass(clazzName);
		}
	}
}
im.lang.InvocationHandler = function () {
	/* constructor */
	{
		this.superclass();
	}
	this.invoke = function (proxy, method, args) {
		return method.invoke(proxy, args);
	}
}
/**
 * [Note] Please use {@link #getInstance} for getting Instance.
 * @constructor
 * @class im.lang.MainInvocationHandler class.<br/>
 * Main invocation handler.
 * @author emooru
 * @version 0.2
 */
im.lang.MainInvocationHandler = function () {
	var _clazzName;
	var args;
	var _domContentLoaded = false;
	var _classLoaded      = false;
	{
		this.superclass();
		args = [];
	}
	/**
	 * invoke the main method of the specified class when all loaded classes are available.<br/>
	 * if you want to specify the arguments, you can add the arguments after clazzName argument.
	 * @param {String} clazzName class name.
	 */
	this.invoke = function (clazzName) {
		_clazzName = clazzName;
		for (var i = 1; i < arguments.length; i++) {
			args.push(arguments[i]);
		}
		var classLoader = im.lang.ClassLoader.getSystemClassLoader();
		classLoader.addClassLoadedEventListener(this);
		if (clazzName != "this") {
			classLoader.loadClass(clazzName);
		}
		if (document.addEventListener) {
			if (document.readyState) {
				// for Safari, Webkit and Opera. ref: But Webkit and Opera supports the DOMContentLoaded event.
				// TODO: When the Safari supports the DOMContentLoaded event, it is necessary to use it.
				_domContentLoaded = true;
			} else {
				// for Firefox
				document.addEventListener("DOMContentLoaded", this.domContentLoaded, false);
			}
		} else {
			// for IE
			_domContentLoaded = true;
		}
	}
	/**
	 * Invoked when the necessary classes are loaded.
	 * @private
	 */
	this.classLoaded = function () {
		_classLoaded = true;
		if (_domContentLoaded) {
			im.lang.MainInvocationHandler.getInstance()._invoke();
		}
	}
	/** @private  */
	this.domContentLoaded = function () {
		_domContentLoaded = true;
		if (_classLoaded) {
			im.lang.MainInvocationHandler.getInstance()._invoke();
		}
	}
	/** @private  */
	this._invoke = function () {
		setTimeout(this.__invoke, 1);
	}
	/** @private  */
	this.__invoke = function () {
		try {
			if (_clazzName == "this") {
				if (main instanceof Function) {
					main(args);
				}
			} else {
				eval("var mainMethod = " + _clazzName + ".main;");
				if (mainMethod instanceof Function) {
					mainMethod(args);
				}
			}
		} catch (e) {
			e.message = "JSPackman Error: main method in '" + _clazzName + "' class for detail: " + e.message;
			throw e;
		}
	}
}
/* static field and static method of im.lang.MainInvocationHandler class */
{
	/** @private */
	im.lang.MainInvocationHandler.handler;
	/** @private */
	im.lang.MainInvocationHandler.initialize = function () {
		im.lang.MainInvocationHandler.handler = new im.lang.MainInvocationHandler();
	}
	/**
	 * Returns the main invocation handler.
	 * @return instance of im.lang.MainInvocationHandler
	 * @type im.lang.MainInvocationHandler
	 */
	im.lang.MainInvocationHandler.getInstance = function () {
		return im.lang.MainInvocationHandler.handler;
	}
}

/**
 * [Note] Please use {@link #getSystemClassLoader} for getting Instance.
 * @constructor
 * @param {im.lang.ClassLoader} parent? a parent class loader
 * @class im.lang.ClassLoader class.<br/>
 * Javascript Package ClassLoader.
 * @throws Error if a charset attribue of script tag is not specified.
 * @author emooru
 * @version 0.2
 */
im.lang.ClassLoader = function (parent) {
	var isTopFrame;
	var doc;
	var headNodes;
	var globalizes;
	var classes;
	var listener;
	var loading;
	var hasParent;
	var classpaths;
	var defaultClassPath;
	var defaultCharset;
	/* constructor */
	{
		hasParent = (parent instanceof Function);
		if (!hasParent) {
			isTopFrame = (top.document != document && top.im && top.im.lang);
			//TODO cache the class definition object for perfomance.
			//doc        = isTopFrame ? top.document : document;
			doc         = document;
			headNodes   = doc.getElementsByTagName('head')[0];
			globalizes  = {};
			classes     = {};
			listeners   = [];
			loading     = 0;
			classpaths  = [];
		}
	}
	/**
	 * define the specified package.
	 * @private
	 * @param {String} packageName package name.
	 */
	this.definePackage = function (packageName) {
		checkPackageName(packageName);
		var splitedPackageName = packageName.split(".");
		eval(splitedPackageName[0] + " = (typeof(" + splitedPackageName[0] + ") == 'undefined') ? {} : " + splitedPackageName[0] + ";");
		eval("var pkg = " + splitedPackageName[0] + ";");
		for (var i = 1; i < splitedPackageName.length; i++) {
			pkg[splitedPackageName[i]] = typeof(pkg[splitedPackageName[i]]) == 'undefined' ? new Object() : pkg[splitedPackageName[i]];
			pkg = pkg[splitedPackageName[i]];
		}
		/** @private */
		function checkPackageName() {
			if (!(typeof(packageName) == "string" && packageName.match(/(\w)+((\.(\w)+))*/))) {
				try {
					throw new im.lang.JSPackmanError("Package name '" + packageName + "' is incorrect.");
				} catch (e) {
					throw new Error("JSPackman Error: Package name '" + packageName + "' is incorrect.");
				}
			}
		}
	}
	/**
	 * define the specified class.
	 * @private
	 * @param {String} clazzName class name
	 * @param {Function} clazz implimented function
	 * @return class.
	 * @type im.lang.Class
	 */
	this.defineClass = function (clazzName) {
		return new im.lang.Class(clazzName);
	}
	/**
	 * Loads the class with the specified name.
	 * @param {String} clazzName class name with package name 
	 * @param {Boolean} globalize if you want to globalize class without package name, specify the true. 
	 * @return The resulting Class object if the class is loaded, or null.
	 * @type im.lang.Class
	 */
	this.loadClass = function (clazzName, globalize) {
		try {
			if (hasParent) {
				return this.findClass(clazzName);
			} else {
				globalize = (typeof(globalize) == 'boolean' && globalize);
				globalizes[clazzName] = (globalizes[clazzName] || globalize);
				return im.lang.ClassLoader.getSystemClassLoader().findClass(clazzName);
			}
		} catch (e) {
			return null;
		}
	}
	/**
	 * find the specified class.
	 * <br/><br/>
	 * Finds the specified class. This method should be overridden by class loader implementations
	 * that follow the delegation model for loading classes, and will be invoked by the {@link #loadClass} 
	 * method after checking the parent class loader for the requested class. 
	 * The default implementation adds the script tag and throws a {@link im.lang.ClassNotFoundException}
	 * if the class have not been loaded yet.
	 * @private
	 * @param {String} clazzName class name with package name
	 * @return The resulting Class object.
	 * @type im.lang.Class
	 * @throws im.lang.ClassNotFoundException if the class is not found or have not loaded yet.
	 */
	this.findClass = function (clazzName) {
		if (hasParent) {
			return parent.findClass(clazzName);
		} else if (classes[clazzName]) {
			return classes[clazzName];
		} else {
			if (typeof(classes[clazzName]) == "undefined") {
				classes[clazzName] = false;
				loading++;
				var cl = im.lang.ClassLoader.getSystemClassLoader();
				// determine the packageRoot and charset;
				var targetClassPath = cl.getTargetClassPath(clazzName);
				cl.addScriptTag(
					targetClassPath.path + clazzName.replace(/\./g, "/") + ".js",
					targetClassPath.charset);
			}
			try {
				throw new im.lang.ClassNotFoundException("'" + clazzName + "' is not found or have not loaded yet.");
			} catch (e) {
				throw new Error("JSPackman Error: '" + clazzName + "' is not found or have not loaded yet.");
			}
		}
	}
	/**
	 * add the event listener which is implemented the classLoaded method.<br/>
	 * <br/>
	 * The classLoaded method invoked when the necessary classes are loaded.
	 * @private
	 * @param {Object} classLoadededEventListener a instance of the class that is implemented the classLoaded method.
	 */
	this.addClassLoadedEventListener = function (classLoadededEventListener) {
		listeners.push(classLoadededEventListener);
	}
	/**
	 * Indicates whether some classes are loading.
	 * @private
	 * @return true if some classes are loading; false otherwise.
	 * @type Boolean
	 */
	this.nowLoading = function () {
		return (loading > 0);
	}
	/**
	 * Returns a parent class loader.<br/>
	 * @return a parent class loader. if a parent is null(undefined), returns the SystemClassLoader.
	 * @type im.lang.ClassLoader
	 */
	this.getParent = function(){
		if (hasParent) {
			return parent;
		} else {
			return im.lang.ClassLoader.getSystemClassLoader();
		}
	}
	/** @private */
	this.addClasspath = function (path, pattern, ignoreCase, charset){
		var regexp;
		if (ignoreCase) {
			regexp = new RegExp(pattern, "i");
		}
		else {
			regexp = new RegExp(pattern);
		}
		if (!charset) {
			charset = defaultCharset;
		}
		classpaths.push({
			"path": path,
			"pattern": regexp,
			"charset": charset
		});
	}
	/** @private */
	this.setDefaultClasspath = function (classpath) {
		defaultClasspath = classpath;
	};
	/** @private */
	this.setDefaultCharset = function (charset) {
		defaultCharset = charset;
	};
	/**
	 * Returns the TargetClassPath object that has 'path' and 'charset' properties.
	 * @private
	 * @param {Object} content
	 * @return a object that has 'path' and 'charset' properties.
	 * @type Object
	 */
	this.getTargetClassPath = function (content) {
		// define the shortcut for perfomance.
		var path    = defaultClasspath;
		var charset = defaultCharset;
		// TODO add the content exist check option!! 
		for (var i = 0; i < classpaths.length; i++) {
			if (classpaths[i].pattern.test(content)) {
				path    = classpaths[i].path;
				charset = classpaths[i].charset;
				break;
			}
		}
		return {"path": path, "charset": charset};
	}
	/** @private  */
	this.notifyClassLoaded = function (clazzName) {
		loading--;
		if (loading <= 0) {
			// initialize the all loaded classes.
			for (var aClassName in classes) {
				if (classes[aClassName].initialize) {
					classes[aClassName].initialize();
				}
			}
			for (var aClassName in classes) {
				var aClass = classes[aClassName];
				// call the static initialize method of each classes, like static initializer of java.
				if (!aClass.wasStaticInitialized()) {
					aClass.invokeStaticInitialize();
				}
				// add the specified global class to the global varialbe.
				if (globalizes[aClassName] && !aClass.wasDeniedGlobalize()) {
					var splitedClassName = aClassName.split('.');
					eval(splitedClassName[splitedClassName.length-1] + " = " + aClassName + ";");
				}
			}
			// invoke the class loaded listener.
			while (listeners.length > 0) {
				listeners.shift().classLoaded();
			}
		}
	}
	/** @private */
	this.addScriptTag = function (src, charset) {
		 // add script node.
		var scriptNode = doc.createElement("script");
		scriptNode.type = "text/javascript";
		// For IE, it is necessary to set charset attribute before setting src attribute.
		scriptNode.charset = charset;
		scriptNode.defer   = "defer";
		scriptNode.src     = src;
		headNodes.appendChild(scriptNode);
	}
	/** @private */
	this.addClass = function (clazzName, jspackClassInstance) {
		classes[clazzName] = jspackClassInstance;
	}
}
/* static field and static method of im.lang.ClassLoader class */
{
	/** @private */
	im.lang.ClassLoader.loader;
	/**
	 * Returns the JavaScript Package ClassLoader.
	 * @return instance of im.lang.ClassLoader
	 * @type im.lang.ClassLoader
	 */
	im.lang.ClassLoader.getSystemClassLoader = function () {
		return im.lang.ClassLoader.loader;
	}
	/** @private  */
	im.lang.ClassLoader.initialize = function () {
		var loader = new im.lang.ClassLoader();
		im.lang.ClassLoader.loader = loader;
		im.lang.ClassLoader.clazz  = new im.lang.Class("im.lang.ClassLoader", im.lang.ClassLoader, true);
		im.lang.Class.SUPER_CLASS.call(im.lang.ClassLoader.loader, im.lang.ClassLoader.clazz);
		// add the Super Class and ClassLoader.
		loader.addClass(im.lang.Class.SUPER_CLASS_NAME, new im.lang.Class(im.lang.Class.SUPER_CLASS_NAME, im.lang.Class.SUPER_CLASS, true));
		// add the local defined classes.
		loader.addClass("im.lang.Class",       im.lang.Class.clazz);
		loader.addClass("im.lang.ClassLoader", im.lang.ClassLoader.clazz);
		// define the local defined function as class and initialize them.
		loader.defineClass("im.lang.PackmanBoss").defineLocal(im.lang.PackmanBoss).initialize().setStaticInitialized(true);
		loader.defineClass("im.lang.InvocationHandler").defineLocal(im.lang.InvocationHandler);
		loader.defineClass("im.lang.MainInvocationHandler").extend("im.lang.InvocationHandler").defineLocal(im.lang.MainInvocationHandler).initialize().setStaticInitialized(true);
		im.lang.MainInvocationHandler.initialize();
		// add Global Native Class(Object).
		loader.addClass("Array",    new im.lang.Class("Array", Array, true));
		loader.addClass("Boolean",  new im.lang.Class("Boolean", Boolean, true));
		loader.addClass("Date",     new im.lang.Class("Date", Date, true));
		loader.addClass("Function", new im.lang.Class("Function", Function, true));
		loader.addClass("Number",   new im.lang.Class("Number", Number, true));
		loader.addClass("Object",   new im.lang.Class("Object", Object, true));
		loader.addClass("RegExp",   new im.lang.Class("RegExp", RegExp, true));
		loader.addClass("String",   new im.lang.Class("String", String, true));
		// add Global Native Error Class(Object).
		loader.addClass("Error",          new im.lang.Class("Error", Error, true));
		loader.addClass("EvalError",      new im.lang.Class("EvalError", EvalError, true));
		loader.addClass("RangeError",     new im.lang.Class("RangeError", RangeError, true));
		loader.addClass("ReferenceError", new im.lang.Class("ReferenceError", ReferenceError, true));
		loader.addClass("SyntaxError",    new im.lang.Class("SyntaxError", SyntaxError, true));
		loader.addClass("TypeError",      new im.lang.Class("TypeError", TypeError, true));
		loader.addClass("URIError",       new im.lang.Class("URIError", URIError, true));
	}
}
/**
 * Constructor.
 * @constructor
 * @class im.lang.PackmanBoss class.<br/>
 * @author emooru
 * @version 0.2
 */
im.lang.PackmanBoss = function() {
	/* constructor */
	{
		this.superclass();
	}
}
/* static field and static method of im.lang.PakamanBoss class */
{
	/**
	 * Version Number of JSPackman.
	 * @type String
	 */
	im.lang.PackmanBoss.VERSION = "";
	/** @private */
	im.lang.PackmanBoss.SELF_FILE_NAME = "im/lang/jspackman";
	/**
	 * Home Path of HTML Contents
	 * @type String
	 */
	im.lang.PackmanBoss.HTML_HOME = "";
	/**
	 * Home Path of CSS Contents
	 * @type String
	 */
	im.lang.PackmanBoss.CSS_HOME = "";
	/**
	 * Home Path of Image Contents
	 * @type String
	 */
	im.lang.PackmanBoss.IMAGE_HOME = "";
	/** @private  */
	im.lang.PackmanBoss.getXMLHttpRequest = function(){
		if (window.ActiveXObject) {
			try {
				return new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					return new ActiveXObject("Microsoft.XMLHTTP");
				} catch (ignore) {
				}
			}
		} else if (window.XMLHttpRequest) {
			return new XMLHttpRequest();
		}
		throw new Error("Your browser is not supported the XMLHttpRequest..");
	}
	im.lang.PackmanBoss.toCurrentPathFromDocumentLocation = function(absolutePath) {
		var documentLocation = document.location.toString();
		var count = 0;
		var currentPath;
		while(true) {
			var lastIndex = documentLocation.lastIndexOf("/");
			if (lastIndex < 0) {
				return absolutePath;
			}
			var parentPath = documentLocation.substring(0, lastIndex);
			if (absolutePath.indexOf(parentPath) === 0) {
				currentPath = absolutePath.substring(parentPath.length + 1);
				break;
			} else {
				count++;
				documentLocation = parentPath;
			}
		}
		for (var i = 0; i < count; i++) {
			currentPath = "../" + currentPath;
		}
		return currentPath;
	}
	/**
	 * Initialize the 'jspackman' as the PackmanBoss.
	 * @private
	 */
	im.lang.PackmanBoss.initialize = function(){
		// initialize the ClassLoader
		im.lang.ClassLoader.initialize();
		var classLoader = im.lang.ClassLoader.getSystemClassLoader();
		
		// set the Global Functions.
		Package = classLoader.definePackage;
		Class   = classLoader.defineClass;
		Import  = classLoader.loadClass;
		Main    = im.lang.MainInvocationHandler.getInstance();
		
		// define the shortcut for perfomance.
		var boss = im.lang.PackmanBoss;
		// get and set the default-value of classpath and charset.
		var scriptNodes = document.getElementsByTagName("script");
		var classpath;
		var charset;
		for (var i = 0; i < scriptNodes.length; i++) {
			var lastIndex = scriptNodes[i].src.lastIndexOf(boss.SELF_FILE_NAME);
			if (lastIndex != -1) {
				classpath = im.lang.PackmanBoss.toCurrentPathFromDocumentLocation(scriptNodes[i].src.substring(0, lastIndex - 1)) + "/";
				charset   = scriptNodes[i].charset;
				break;
			}
		}
		classLoader.setDefaultClasspath(classpath);
		if (!charset) {
			// check if the charset is specified.
			throw new Error("JSPackman Error: Please specify the charset attribute of script tag for '" + boss.SELF_FILE_NAME + "'.");
		}
		classLoader.setDefaultCharset(charset);
		
		// add the classpath from global variable of 'JSPACKMAN_CLASSPATH'.
		if (typeof(JSPACKMAN_CLASSPATH) === "object" && JSPACKMAN_CLASSPATH.constructor === Array.prototype.constructor) {
			for (var i = 0; i < JSPACKMAN_CLASSPATH.length; i++) {
				var aClasspath  = JSPACKMAN_CLASSPATH[i];
				var aPath       = aClasspath.path.replace("$JSPACKMAN_HOME/", classpath);
				var aPattern    = aClasspath.classNamePattern;
				var aIgnoreCase = (aClasspath.ignoreCase === true);
				var aCharset    = aClasspath.charset;
				classLoader.addClasspath(aPath, aPattern, aIgnoreCase, aCharset);
			}
		}
		
		// get the JSPackman Setting XML Object.
		var jspackmanNode;
		var xmlHttpRequest = boss.getXMLHttpRequest();
		xmlHttpRequest.open("GET", classpath + boss.SELF_FILE_NAME + ".xml", false);
		xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		xmlHttpRequest.send("");
		jspackmanNode = xmlHttpRequest.responseXML.documentElement;
		// get and set the version number.
		boss.VERSION = jspackmanNode.getAttributeNode("version").nodeValue;
		// get the ClassLoader Settings
		var classLoaderNode = jspackmanNode.getElementsByTagName("class-loader")[0];
		for (var i = 0; i < classLoaderNode.childNodes.length; i++) {
			var aChildNode = classLoaderNode.childNodes[i];
			if (aChildNode.tagName == "classpaths") {
				for (var j = 0; j < aChildNode.childNodes.length; j++) {
					var aClasspathNode = aChildNode.childNodes[j];
					if (aClasspathNode.tagName == "classpath") {
						var path = aClasspathNode.firstChild.nodeValue.replace("$JSPACKMAN_HOME/", classpath);
						var pattern = aClasspathNode.getAttributeNode("class-name-pattern").nodeValue;
						var ignoreCaseAttr = aClasspathNode.getAttributeNode("ignore-case");
						var ignoreCase = (ignoreCaseAttr != null) && (ignoreCaseAttr.nodeValue == "true");
						var charset = aClasspathNode.getAttributeNode("charset").nodeValue;
						classLoader.addClasspath(path, pattern, ignoreCase, charset)
					}
				}
			} else if (aChildNode.tagName == "available-classes") {
				for (var j = 0; j < aChildNode.childNodes.length; j++) {
					var aClassNode = aChildNode.childNodes[j];
					if (aClassNode.tagName == "class") {
						var className = aClassNode.getAttributeNode("name").nodeValue;
						classLoader.loadClass(className);
					}
				}
			}
		}
		// get the Contents Home Path Setting
		/*
		var contentHomePathNode = jspackmanNode.getElementsByTagName("contents_home_paths")[0];
		for (var i = 0; i < contentHomePathNode.childNodes.length; i++) {
			var aChildNode = contentHomePathNode.childNodes[i];
			var path = aChildNode.firstChild.nodeValue.replace("$JSPACKMAN_HOME/", classpath);
			if (aChildNode.tagName == "html_home_path") {
				im.lang.PackmanBoss.HTML_HOME = path;
			} else if (aChildNode.tagName == "css_home_path") {
				im.lang.PackmanBoss.CSS_HOME = path;
			} else if (aChildNode.tagName == "image_home_path") {
				im.lang.PackmanBoss.IMAGE_HOME = path;
			} else {
				// ignore
			}
		}
		*/
	}
}
// initialize the PackmanBoss.
im.lang.PackmanBoss.initialize();
