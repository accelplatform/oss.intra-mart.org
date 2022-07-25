var logger1 = Logger.getLogger();

function init(){
	var logger2 = Logger.getLogger();
	
	logger1.info("インクルード先の　logger1　で出力");
	logger2.info("インクルード先の　logger2　で出力");
}
