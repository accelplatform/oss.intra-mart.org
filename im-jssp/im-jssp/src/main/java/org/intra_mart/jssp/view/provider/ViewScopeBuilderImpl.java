/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * @(#)SourceManager.java        1.00 10 Nov 2004
 *
 * This software is the confidential and proprietary information of
 * NTTDATA INTRAMART Co.,Ltd. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered
 * into with NTTDATA INTRAMART Co.,Ltd.
 *
 * Copyright (C) 2000-2004 NTTDATA INTRAMART Co.,Ltd.  All rights reserved.
 */
package org.intra_mart.jssp.view.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;


import org.intra_mart.common.aid.jdk.java.io.file.Directory;
import org.intra_mart.common.aid.jdk.java.lang.ExtendedClassLoader;
import org.intra_mart.jssp.source.SourceFile;
import org.intra_mart.jssp.source.property.SourceProperties;
import org.intra_mart.jssp.source.provider.SourceFileProvider;
import org.intra_mart.jssp.source.provider.SourceFileProviderManager;
import org.intra_mart.jssp.util.ClassNameHelper;
import org.intra_mart.jssp.util.compile.PresentationPageCompiler;
import org.intra_mart.jssp.util.config.HomeDirectory;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.intra_mart.jssp.view.ViewScope;



/**
 * ViewScopeビルダの実装です。
 */
public class ViewScopeBuilderImpl implements ViewScopeBuilder{

	/**
	 * このビルダが扱うロケール
	 */
	private Locale locale = null;
	
	/**
	 * コンパイルしたクラスの出力先ディレクトリパス
	 */
	private Directory classPath = null;
	
	/**
	 * 自動コンパイルしたソースを読み込むためのクラスローダ
	 */
	private ClassLoader dynamicClassLoader = null;

	/**
	 * コンストラクタ
	 * @param locale ロケール
	 * @throws IOException 入出力エラー
	 */
	public ViewScopeBuilderImpl(Locale locale) throws IOException{
		super();
		this.locale = locale;

		// クラスファイル保存ディレクトリの決定
		this.classPath = new Directory(this.getWorkDirectory(), locale.toString());
		if(this.classPath.exists()){
			this.classPath.deleteChilds();
		}
		else{
			if(! this.classPath.mkdirs()){
				throw new IOException("Directory create error: " + this.classPath.getAbsolutePath());
			}
		}

		// 自動コンパイルしたクラスを読み込むためのクラスローダ
		ExtendedClassLoader classLoader = new ExtendedClassLoader(this.getClass().getClassLoader());
		classLoader.addClassPath(this.classPath);
		this.dynamicClassLoader = classLoader;		
		
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.provider.ViewScopeBuilder#getLocale()
	 */
	public Locale getLocale(){
		return this.locale;
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.provider.ViewScopeBuilder#getOutputDirectory()
	 */
	public File getOutputDirectory(){
		return this.classPath;
	}	
	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.view.provider.ViewScopeBuilder#getViewScope(java.lang.String)
	 */
	public ViewScope getViewScope(String path) throws FileNotFoundException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {

		String pathWithExt  = path + ".html";
		String className = ClassNameHelper.toClassName(pathWithExt);
		String filePathConvertedClassName = ClassNameHelper.toFilePath(className);
		
		// パスに該当するコンパイル済みソースを検索
		InputStream inputStream = this.dynamicClassLoader.getResourceAsStream(filePathConvertedClassName);
		if(inputStream != null){
			// 復元
			ObjectInputStream in = new ObjectInputStream(inputStream);
			try{
				return (ViewScope) in.readObject();
			}
			finally{
				in.close();
			}
		}
		else{
			// ソースを検索
			SourceFileProvider sourceManager = SourceFileProviderManager.getSourceFileProvider(this.getLocale());
			SourceFile sourceFile = sourceManager.getSourceFile(pathWithExt);
			
			if(sourceFile != null){
				PresentationPageCompiler compiler = new PresentationPageCompiler();
				ViewScope viewScope = compiler.compile(sourceFile.getReader());

				// キャッシュするかどうかをチェック
				SourceProperties properties = sourceFile.getSourceProperties();
				if(properties.enableViewCompile()){
					this.writeObject(filePathConvertedClassName, viewScope);
				}
				
				return viewScope;
			}
			else{
				throw new FileNotFoundException(pathWithExt);
			}
		}
	}


	/**
	 * オブジェクト value を path が表すファイルに書き出します。
	 * @param path ファイルパス
	 * @param value 書き出す値
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeObject(String path, Object value) throws FileNotFoundException, IOException{

		File file = new File(this.getOutputDirectory(), path);
		File dir = file.getParentFile();
		
		if(! dir.isDirectory()){
			dir.mkdirs(); 
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

		try{
			out.writeObject(value);
		}
		finally{
			out.close();
		}
	}

	/**
	 * コンパイル結果のクラスファイルを出力するディレクトリを返します。
	 * @return ディレクトリパスをあらわす File オブジェクト
	 */
	private File getWorkDirectory(){
		String outputDir = JSSPConfigHandlerManager.getConfigHandler().getOutputDirectory4ComiledView();
		if(outputDir != null){
			File file = new File(outputDir);
			if(! file.isAbsolute()){
				return new File(HomeDirectory.instance(), outputDir);		
			}
			else{
				return file;
			}
		}
		else{
			throw new IllegalStateException("Output directory is not defined : [Comiled View]");
		}

	
	}
}

