/*
 * MigrationMain.java
 *
 * Created on 2005/05/16,  17:38:27
 */
package org.intra_mart.data_migration;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.intra_mart.data_migration.common.util.ExceptionUtil;
import org.intra_mart.data_migration.generator.Generator;
import org.intra_mart.data_migration.generator.GeneratorContext;
import org.intra_mart.data_migration.generator.GeneratorContextImpl;


/**
 * SQLデータの作成を行います。
 * 
 * @author intra-mart
 * 
 */
public final class GeneratorMain {
	/**
	 * コンフィグ
	 */
	private static final String CONFIG_PATH = "conf/main.properties";

	/**
	 * ログ出力先を指定するキー
	 */
	private static final String LOG_DIR_KEY = "log_dir";

	/**
	 * 異常ステータス
	 */
	private static int ERROR_STATUS_CODE = -1;

	/**
	 * 移行データの抽出処理を実行します。 <br>
	 * <br>
	 * 引数[0] 移行対象の種別 <br>
	 * 引数[1] 出力対象のテーブル名<br>
	 * 引数[2] 入力ファイル名 (接続設定ファイル名) <br>
	 * 引数[3] 出力ディレクトリ名 <br>
	 * 
	 * @param args 引数
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			usage();
			System.exit(ERROR_STATUS_CODE);
		}

		// プロパティの初期化
		Properties properties = new Properties();
		try {
			properties = new Properties();
			properties.load(new FileInputStream(CONFIG_PATH));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ERROR_STATUS_CODE);
		}

		// データ生成オブジェクトを取得
		Generator generator = null;
		try {
			properties.load(new FileInputStream(CONFIG_PATH));
			generator = (Generator) Class.forName(
					properties.getProperty(args[0])).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ERROR_STATUS_CODE);
		}

		// ロガーの生成
		Logger logger = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			logger = Logger.getLogger(generator.getClass().getName());
			File logDir = new File(properties.getProperty(LOG_DIR_KEY));
			File logFile = new File(logDir, args[0] + "_"
					+ format.format(new Date()) + ".log");
			FileHandler fileHandler = new FileHandler(
					logFile.getAbsolutePath(), false);
			logger.addHandler(fileHandler);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ERROR_STATUS_CODE);
		}

		// コンテキストの初期化
		GeneratorContext context = null;
		try {
			context = new GeneratorContextImpl(args[1], args[2], args[3], logger);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ERROR_STATUS_CODE);
		}

		//
		// データの抽出を実行します。
		//
		try {
			logger.info("[Start] " + generator.getClass().getName());
			generator.execute(context);
		} catch (Exception e) {
			// 移行時に発生した例外はログに出力する
			logger.severe(ExceptionUtil.getStackTrace(e));
		} finally {
			logger.info("[Finish] " + generator.getClass().getName());
		}
	}

	/**
	 * 使用方法を出力します。
	 */
	public static final void usage() {
		System.out.println("Usage: " + GeneratorMain.class.getName()
				+ " type tableNmae inputFileName outputFileName");
	}
}