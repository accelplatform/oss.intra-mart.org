/**
 * ファイルを読み込みます。
 */
function readFile(path, readASCII, delayTime){

	Debug.print("######## EXECUTE! : sample/jssp_rpc/display_file/server_side_logic/file_reader#readFile() ########");

	// コールバック関数を確認するために遅延処理を入れています。
	Debug.print("遅延時間：" + delayTime + "[s]");
	sleep(delayTime * 1000);

	// チェック
	var file = new File(path);
	if( !file.exist() ){
		return createResultObject(true, "対象ファイルは存在しません。", file.path(), null);
	}
	else if( !file.isFile() ){
		return createResultObject(true, "ファイルを指定してください。", file.path(), null);
	}
	else if( !file.canRead() ){
		return createResultObject(true, "対象ファイルを読み込むことは出来ません。", file.path(), null);
	}

	// 読み込み実行
	var readData;
	if(readASCII == true){
		// File#read()の返却値は、以下の設定値を元に自動的に Unicode に変換されます。
		//  ==> 「/conf/jssp-config.xml」の「intra-mart/server-character-encoding」
		readData = file.read();
	}
	else {
		readData = file.load();
	}

	// ファイルの内容を返却
	if(readData == null){
		return createResultObject(true, "読み込みに失敗しました。", file.path(), null);
	}
	else {
		return createResultObject(false, "完了", file.path(), readData);
	}
}

/**
 * 結果オブジェクトを取得します。
 */
function createResultObject(isError, message, path, data){

	var result = new Object();
	
	result.error   = isError;
	result.massage = message;
	result.path    = path;
	result.data    = data;

	return result;
}
