// バインド変数宣言
var backGif   = Web.base() + "/sample/script/file/icons/back.gif";
var textGif   = Web.base() + "/sample/script/file/icons/text.gif";
var folderGif = Web.base() + "/sample/script/file/icons/folder.gif";

var currentDirPath;
var parentDirPath;
var existsParentDir = true;

var childDirectories = new Array();
var childFiles = new Array();

// 削除機能を有効にするには、trueを設定してください。
var DELETE_ENABLE_FLAG = false;

/**
 * 対象ディレクトリ配下のファイル情報を取得
 */
function init(request){

	// 対象ディレクトリ
	if(!isBlank(request.targetDirPath)){
		currentDirPath = request.targetDirPath;
	}
	else {
		currentDirPath = new File("").path();
	}
	var regExp = new RegExp("\\\\", "g");
	currentDirPath = currentDirPath.replace(regExp, "/");

	// 親ディレクトリ
	if(currentDirPath.lastIndexOf("/") != -1){
		parentDirPath = currentDirPath.substring(0, currentDirPath.lastIndexOf("/"));
	}
	else {
		parentDirPath = currentDirPath;
	}

	if(parentDirPath.lastIndexOf("/") == -1){
		existsParentDir = false;
	}

	// 対象ディレクトリのFileオブジェクト生成
	var targetDir = new File(currentDirPath);
	if(targetDir.exist() == false){
		displayError("対象ディレクトリは存在しません：『" + currentDirPath + "』");
	}


	// 対象ディレクトリ配下のディレクトリ一覧
	var directories = targetDir.directories();
	for(var idx = 0; idx < directories.length; idx++) {
		var dirObject = new File(currentDirPath + "/" + directories[idx]);

		var dir = new Object();
		dir.name         = directories[idx];
		dir.path         = currentDirPath + "/" + directories[idx];
		dir.size         = "";
		dir.lastModified = dirObject.lastModified();
		dir.canDelete    = canDeleteDir(dirObject) && DELETE_ENABLE_FLAG;

		childDirectories[idx] = dir;
	}


	// 対象ディレクトリ配下のファイル一覧
	var files = targetDir.files();
	for(var idx = 0; idx < files.length; idx++) {
		var fileObject = new File(currentDirPath + "/" + files[idx]);

		var file = new Object();
		file.name         = files[idx];
		file.path         = currentDirPath + "/" + files[idx];
		file.size         = fileObject.size();
		file.lastModified = fileObject.lastModified();
		file.canDelete    = fileObject.canWrite() && DELETE_ENABLE_FLAG;

		childFiles[idx] = file;
	}

}

// ディレクトリ削除 可・不可 チェック
function canDeleteDir(dirObject){
	if(dirObject.canWrite()
	   &&
	   ! isBlank(dirObject.files())
	   &&
	   dirObject.files().length == 0
	   &&
	   !isBlank(dirObject.directories())
	   &&
	   dirObject.directories().length == 0 ) {
		return true;
	}
	else {
		return false;
	}
}


/**
 * ファイルをダウンロード
 */
function downloadAction(request){
	var targetFile = new File(request.targetFilePath);

	if(! targetFile.exist()){
		displayError("対象ファイルは存在しません：『" + request.targetFilePath + "』");
	}
	else if(! targetFile.canRead()){
		displayError("ファイルを読み込めません：『" + request.targetFilePath + "』");
	}

	var response = Web.getHTTPResponse();

	// コンテントタイプを設定
	response.setContentType(getMimeType(request.targetFileName));

	// ファイル名を設定
	if(Web.getenv("HTTP_USER_AGENT").indexOf("MSIE") != -1){
		response.setHeader("Content-Disposition", "attachment; filename=" +
							URL.encode(request.targetFileName, "UTF-8") );
	}
	else{
		response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" +
							URL.encode(request.targetFileName, "UTF-8") );
	}

	// データ送信
	var stream = targetFile.load();
	if(isString(stream)){
		response.setContentLength(stream.length);
		response.sendMessageBody(stream);
	}
	else{
		response.setContentLength(0);
		response.sendMessageBody("");
	}
}

/**
 * ファイルをアップロード
 */
function uploadAction(request){

	// パラメータ情報(=RequestParameter オブジェクト)を取得
	var parameter = request.getParameter("localFile");

	// ファイルの中身を取得（バイナリ）
	var fileData = parameter.getValueAsStream();

	// ファイル名の取得
	var fileName = parameter.getFileName()

	// ファイルの書き出し
	var file = new File(request.targetDirPath + "/" + fileName);
	var result = file.save(fileData);

	if(!result){
		displayError("アップロードに失敗しました：『" + request.targetDirPath + "/" + fileName + "』");
	}
}

/**
 * ディレクトリを新規作成
 */
function createDirAction(request){
	// ディレクトリの作成
	var dir = new File(request.targetDirPath + "/" + request.dirName);
	var result = dir.makeDirectories();

	if(!result){
		displayError("ディレクトリの作成に失敗しました：『" + request.targetDirPath + "/" + request.dirName + "』");
	}
}

/**
 * ファイルを削除
 */
function deleteAction(request){
	var targetFile = new File(request.targetFilePath);

	if(! targetFile.exist()){
		displayError("削除対象は存在しません：『" + request.targetFilePath + "』");
	}
	else if(! targetFile.canWrite()){
		displayError("削除できません：『" + request.targetFilePath + "』");
	}

	// 削除！
	var result = targetFile.remove();

	if(!result){
		displayError("削除に失敗しました：『" + request.targetFilePath + "』");
	}
}

/**
 * MIMEタイプの取得
 */
function getMimeType(fileName){

	var MIME = new Object();
	MIME.PDF  = "application/pdf";
	MIME.GIF  = "image/gif";
	MIME.HTML = "text/html";
	MIME.HTM  = "text/html";
	MIME.JAR  = "application/java-archive";
	MIME.JS   = "application/x-javascript";
	MIME.JPEG = "image/jpeg";
	MIME.JPG  = "image/jpeg";
	MIME.XLS  = "application/vnd.ms-excel";
	MIME.PPT  = "application/vnd.ms-powerpoint";
	MIME.MP2  = "audio/x-mpeg";
	MIME.MPEG = "video/mpeg";
	MIME.MPG  = "video/mpeg";
	MIME.TXT  = "text/plain";
	MIME.TEXT = "text/plain";
	MIME.DOC  = "application/msword";

	var mimeType = null;

	if(fileName.lastIndexOf(".") != -1){
		var type = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
		mimeType = MIME[type];
	}

	if(isBlank(mimeType)) {
		return "application/octet-stream";
	}
	else {
		return mimeType
	}
}

/**
 * エラー表示
 */
function displayError(message){
	Debug.browse(message);
}
