package org.intra_mart.jssp.script.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import org.intra_mart.common.aid.jdk.java.io.file.Directory;
import org.intra_mart.common.aid.jdk.java.lang.ExtendedClassLoader;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.source.SourceFile;
import org.intra_mart.jssp.source.property.SourceProperties;
import org.intra_mart.jssp.source.provider.SourceFileProvider;
import org.intra_mart.jssp.source.provider.SourceFileProviderManager;
import org.intra_mart.jssp.source.provider.SourceLoader;
import org.intra_mart.jssp.util.ClassNameHelper;
import org.intra_mart.jssp.util.compile.JavaScriptCompiler;
import org.intra_mart.jssp.util.config.HomeDirectory;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Script;

public class ScriptScopeBuilderImpl implements ScriptScopeBuilder{
	/**
	 * このビルダが扱うロケール
	 */
	private Locale locale = null;

	/**
	 * コンパイルしたクラスの出力先ディレクトリパス
	 */
	private Directory outputDir = null;

	/**
	 * 自動コンパイルしたソースを読み込むためのクラスローダ
	 */
	private ClassLoader dynamicClassLoader = null;

	/**
	 * コンストラクタ
	 * @param locale ロケール
	 * @throws IOException 入出力エラー
	 */
	public ScriptScopeBuilderImpl(Locale locale) throws IOException{
		super();
		this.locale = locale;

		// クラスファイル保存ディレクトリの決定
		this.outputDir = new Directory(this.getWorkDirectory(), locale.toString());
		if(this.outputDir.exists()){
			this.outputDir.deleteChilds();
		}
		else{
			if(! this.outputDir.mkdirs()){
				throw new IOException("Directory create error: " + this.outputDir.getAbsolutePath());
			}
		}

		// 自動コンパイルしたクラスを読み込むためのクラスローダ
		ExtendedClassLoader classLoader = new ExtendedClassLoader(this.getClass().getClassLoader());
		classLoader.addClassPath(this.outputDir);
		this.dynamicClassLoader = classLoader;

	}

	/* (non-Javadoc)
	 * @see org.mozilla.jssp.provider.application.ScriptScopeBuilder#getLocale()
	 */
	public Locale getLocale(){
		return this.locale;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.jssp.provider.application.ScriptScopeBuilder#getOutputDirectory()
	 */
	public File getOutputDirectory(){
		return this.outputDir;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.jssp.provider.application.ScriptScopeBuilder#getScriptScope(java.lang.String)
	 */
	public ScriptScope getScriptScope(String path) 
				throws JavaScriptException, 
						FileNotFoundException, 
						InstantiationException, 
						IllegalAccessException, 
						IOException, 
						ClassNotFoundException{

		return getScriptScope(path, null, null);
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.script.provider.ScriptScopeBuilder#getScriptScope(java.lang.String, java.io.File)
	 */
	public ScriptScope getScriptScope(String path, File sourceDir) 
				throws JavaScriptException, 
						FileNotFoundException, 
						InstantiationException, 
						IllegalAccessException, 
						IOException, 
						ClassNotFoundException{

		return getScriptScope(path, null, sourceDir);
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.script.provider.ScriptScopeBuilder#getScriptScope(java.lang.String, org.intra_mart.jssp.script.ScriptScope)
	 */
	public ScriptScope getScriptScope(String path, ScriptScope scope) 
				throws JavaScriptException, 
						FileNotFoundException, 
						InstantiationException, 
						IllegalAccessException, 
						IOException, 
						ClassNotFoundException{
		return getScriptScope(path, scope, null);
	}


	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.script.provider.ScriptScopeBuilder#getScriptScope(java.lang.String, org.intra_mart.jssp.script.ScriptScope, java.io.File)
	 */
	public ScriptScope getScriptScope(String path, ScriptScope scope, File sourceDir) 
				throws JavaScriptException, 
						FileNotFoundException, 
						InstantiationException, 
						IllegalAccessException, 
						IOException, 
						ClassNotFoundException{

		// 実行環境の取得
		Context cx = Context.enter();

		if(scope == null){
			// 変数スコープの生成
			scope = new ScriptScope(path);
		}
		
		// 登録前のスコープをバックアップ
		ScriptScope previousScriptScope = ScriptScope.entry(scope);

		try{
			// 実行
			Script script = this.getScript(path + ".js", sourceDir);
			script.exec(cx, scope);
			return scope;
		}
		finally{
			// スコープの復元
			ScriptScope.entry(previousScriptScope);

			// 実行環境の開放
			Context.exit();
		}

	}
	
	/**
	 * 指定のソースをコンパイルします。
	 * 
	 * @param path ソースのパス名
	 * @param sourceDir ソースディレクトリ
	 * 			（ソース検索時、まずこのディレクトリ元に検索します。その後に、コンフィグファイルに設定されたソースディレクトリからの検索を行います）
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Script getScript(String path, File sourceDir) throws InstantiationException, IllegalAccessException, FileNotFoundException, IOException, ClassNotFoundException{

		// クラス名の取得
		String className = ClassNameHelper.toClassName(path);

		try{
			// クラス化されたソースの検索
			try{
				try{
					ClassLoader classLoader = JSSPClassLoaderManager.getClassLoader(this.getLocale());
					Class clazz = classLoader.loadClass(className);
					return (Script) clazz.newInstance();
				}
				catch(ClassNotFoundException cnfe){
					ClassLoader classLoader = JSSPClassLoaderManager.getClassLoader();
					Class clazz = classLoader.loadClass(className);
					return (Script) clazz.newInstance();
				}
			}
			catch(ClassNotFoundException cnfe){
				Class clazz = this.dynamicClassLoader.loadClass(className);
				return (Script) clazz.newInstance();
			}
		}
		// クラスが存在しない→ソースを検索
		catch(ClassNotFoundException cnfe){

			SourceFile sourceFile = null;
			
			// 指定されたソースディレクトリで検索
			if(sourceDir != null){
				try {
					SourceLoader sourceLoader = new SourceLoader(sourceDir);
					sourceFile = sourceLoader.loadSource(path);
				}
				catch (Exception e) {
					/* 無視 => 通常のソースディレクトリで検索 */
				}
			}
			
			// 指定されたソースディレクトリで見つからなかった場合
			if(sourceFile == null){
				// 通常のソースディレクトリで検索
				SourceFileProvider provider = SourceFileProviderManager.getSourceFileProvider(this.getLocale());
				sourceFile = provider.getSourceFile(path);
			}
			
			if(sourceFile != null){
				SourceProperties properties = sourceFile.getSourceProperties();
				String srcPath = sourceFile.getAbsolutePath();

				JavaScriptCompiler compiler = new JavaScriptCompiler();
				compiler.setGeneratingDebug(true);

				try{
					if(properties.enableJavaScriptCompile()){
						String fileName = ClassNameHelper.toFilePath(className);
						File classFile = new File(this.getOutputDirectory(), fileName);
						
						// コンパイル！
						compiler.setOptimizationLevel(properties.getJavaScriptOptimizationLevel());
						compiler.compile(sourceFile.getReader(), srcPath, className, classFile);
						
						Class clazz = this.dynamicClassLoader.loadClass(className);
						return (Script) clazz.newInstance();
					}
					else{
						// インタプリタモード（インタプリタモードの場合、最適化レベルは常に「-1」となります)
						return compiler.interpret(sourceFile.getReader(), srcPath);
					}
				}
				catch(UnsupportedOperationException uoe){
					// インタプリタモード（インタプリタモードの場合、最適化レベルは常に「-1」となります)
					return compiler.interpret(sourceFile.getReader(), srcPath);
				}
			}
			else{
				throw new FileNotFoundException("Function-Container not found: " + className + " <" + path + ">");
			}
		}
	}

	/**
	 * コンパイル結果のクラスファイルを出力するディレクトリを返します。
	 * 
	 * @return ディレクトリパスをあらわす File オブジェクト
	 */
	private File getWorkDirectory(){
		String outputDir = JSSPConfigHandlerManager.getConfigHandler().getOutputDirectory4ComiledScript();
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
			throw new IllegalStateException("Output directory is not defined : [Comiled Script]");
		}
	}

}

