/**
 * Rhinoで定義されているグローバル関数の実行サンプル
 */
function init(request){
	
	execute_ImportClassAndPackage();
	execute_Environment();

	execute_Seal();

	execute_Gc();
	execute_ToInt32();
	execute_Version();

	execute_ReadFile();
	execute_ReadUrl();

	execute_LoadClass();
	execute_DefineClass();
	// execute_SerializeAndDeserialize(); // ← うまく動作しません。。。

	execute_SyncAndSpawn();
		
	Debug.browse("Rhinoで定義されているグローバル関数を実行しました。");
}


/**
 * importClass(), importPackage()
 */
function execute_ImportClassAndPackage(){
	Debug.print("\n************* importClass(), importPackage() *************");
	
	importClass(java.lang.System);
	importPackage(java.util);
	
	var list = new ArrayList();
	list.add("value1");
	list.add("value2");
	list.add("value3");
	
	var it = list.iterator();
	while (it.hasNext()) {
		System.out.println(it.next());
	}
}


/**
 * グローバル変数「environment」
 */
function execute_Environment(){
	Debug.print("\n************* グローバル変数「environment」 *************");
	
	for (var key in environment) {
		var value     = environment[key];
		var keyType   = typeof(key);
		var valueType = typeof(value);
		
		Debug.print("[" + keyType + "] " + key + "  -->  [" + valueType + "] " + value);
	}
	
	Debug.print("java.vm.version .... " + environment["java.vm.version"].toString()); // ←　toString()が必要
}


/**
 * seal()
 */
function execute_Seal(){
	Debug.print("\n************* seal() *************");
	
	var obj = { 
		prop1 : "文字列", 
		prop2 : 123
	};
	
	Debug.print("===== 1回目 =====");
	Debug.console(obj);
	
	obj.prop1 = obj.prop1 + "（編集しました）";
	obj.prop2 = obj.prop2 + 456;

	Debug.print("===== 2回目 =====");
	Debug.console(obj);

	// ###### EXECUTE : seal() #####
	seal(obj);
	
	try{
		obj.prop1 = obj.prop1 + "（再編集）";
		obj.prop2 = obj.prop2 + 789;
	
		Debug.print("===== 3回目 =====");
		Debug.console(obj);
	}
	catch(e){
		Debug.print("===== エラー発生 =====");
		Debug.print(e.message);
	}
}


/**
 * gc()
 */
function execute_Gc(){
	Debug.print("\n************* gc() *************");

	var runtime = Packages.java.lang.Runtime.getRuntime();
	var beforeFreeSize = runtime.freeMemory();
		
	// ###### EXECUTE : gc() #####
	gc();

	Debug.print("[Free-Memory]: " + beforeFreeSize + " -> " + runtime.freeMemory() + " / " + runtime.totalMemory());
}


/**
 * toint32()
 */
function execute_ToInt32(){
	Debug.print("\n************* toint32() *************");

	// ###### EXECUTE : toint32() #####
	Debug.console(toint32(),
				 toint32(12345), 
				 toint32("67890"), toint32("mojiretsu"), 
				 toint32(new Date()),
				 toint32(true), toint32(false),
				 toint32(undefined),
				 toint32(null));
}


/**
 * version()
 */
function execute_Version(){
	Debug.print("\n************* version() *************");

	// ###### EXECUTE : version() #####
	Debug.console(version());
}


/**
 * readFile()
 */
function execute_ReadFile(){
	Debug.print("\n************* readFile() *************");

	var jsFile = new File("sample/tag/hello/hello_main.js");
	var path = jsFile.path();
	
	// ###### EXECUTE : readFile() #####
	var contents = readFile(path, "UTF-8");
	
	Debug.print(path);
	Debug.print("-------------------------");
	Debug.print(contents);
}


/**
 * readUrl()
 */
function execute_ReadUrl(){
	Debug.print("\n************* readUrl() *************");

	var url = Web.base() + "/sample/tag/hello/hello_main.jssp";
	
	// ###### EXECUTE : readUrl() #####
	// 第2引数は文字コード名 （省略時は java.net.URLConnection#getContentType() 内の charset）
	var contents = readUrl(url);
	
	Debug.print(url);
	Debug.print("-------------------------");
	Debug.print(contents);
}


/**
 * loadClass()
 */
function execute_LoadClass(){
	Debug.print("\n************* loadClass() *************");

	// ※※※※ 注意 ※※※※
	// このサンプルを実行するには、JSコンパイル後のクラスをクラスパスにコピーする必要があります。	
	// （便宜的に、im-jssp-sample/src/main/resources/ 配下にクラスファイルを用意してあります）

	// また、以下を実行することで、クラスファイルを生成することも可能です。
	//     load("sample/script/rhino_global/load_class_target");

	try{
		// ###### EXECUTE : loadClass() #####
		loadClass("_sample._script._rhino_global._load_class_target_js");
	
		// load_class_target.js内で定義されている関数を実行
		functionOfLoadClassTarget();
	}
	catch(e){
		Debug.print("===== エラー発生 =====");
		Debug.print(e.message);
	}
}


/**
 * defineClass()
 */
function execute_DefineClass(){
	Debug.print("\n************* defineClass() *************");

	// ※※※※ 注意 ※※※※
	// このサンプル関数を実行するには、conf/jssp-config.xmlで設定されているSessionオブジェクトをコメントアウトする必要があります。

	try{
		Debug.print("===== 1回目 =====");
		Debug.print(Session.getId());
	}
	catch(e){
		Debug.print("===== エラー発生 =====");
		Debug.print(e.message);
	}	

	// ###### EXECUTE : defineClass() #####
	defineClass("org.intra_mart.jssp.script.api.SessionObject");

	Debug.print("===== 2回目 =====");
	Debug.print(Session.getId());
}


/**
 * serialize(), deserialize()
 */
function execute_SerializeAndDeserialize(){
	Debug.print("\n************* serialize(), deserialize() *************");
	
	// 以下の形式では、NotSerializableExceptionが発生します。。。なぜ？
	var obj = { 
		prop1 : "値1", 
		prop2 : new Date(), 
		prop3 : false, 
		prop4 : 98765 
	};
	
	var obj = "文字列";
	
	Debug.print("==== シリアライズ前 ====");
	Debug.console(obj);
	
	var destDir = environment["java.io.tmpdir"].toString();
	var destFile = destDir + "selialized_object";
	
	// ###### EXECUTE : serialize() #####
	serialize(obj, destFile);

	// ###### EXECUTE : deserialize() #####
	var deObj = deserialize(destFile);

	Debug.print("==== シリアライズ後 ====");
	Debug.print("[" + typeof(deObj) + "] " + deObj); // Object型として扱われます。
}


/**
 * sync(), spawn()
 */
function execute_SyncAndSpawn(){
	Debug.print("\n************* sync(), spawn() *************");

	var o = {
		// ###### EXECUTE : sync() #####
		f: sync(
			function(name, time){
				Debug.print("[" + name + "] +++ entry +++");
				sleep(time * 1000);
				Debug.print("[" + name + "] --- exit  ---");
			}
		)
	};
	
	var f4spawn = function(){
		var threadName = Packages.java.lang.Thread.currentThread().getName();
		Debug.print("[" + threadName + "] ****** START ******");
		o.f(threadName, 5);
		Debug.print("[" + threadName + "] ******  END  ******");
	};

	// ###### EXECUTE : spawn() #####
	spawn(f4spawn);
	spawn(f4spawn);
}
