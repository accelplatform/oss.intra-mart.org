package org.intra_mart.jssp.util.compile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;

import org.intra_mart.jssp.util.ClassNameHelper;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.optimizer.ClassCompiler;


/**
 * JavaScriptコンパイラ
 */
public final class JavaScriptCompiler {
	
	/**
	 * インタープリタモード時の最適化オプションレベルを設定する<br/>
	 * システムプロパティのキーです。<br/>
	 * <b>このシステムプロパティは、通常、設定の必要はありません。</b><br/>
	 * <br/>
	 * このシステムプロパティ値(=インタープリタモード時の最適化オプションレベル)は、
	 * Javaで実装されたJavaScriptAPIのデバッグ用として存在します。<br/>
	 * この設定値を 0 以上にすることで、Javaで実装されたJavaScriptAPIで例外が発生した際のスタックトレース内に、
	 * 実行中のJavaScripソースのファイルパス、および、実行行数が含まれるようになります。<br/>
	 * <br/>
	 * ただし、このシステムプロパティ値が 0 以上に設定されている場合、
	 * org.mozilla.javascript.debug.Debuggerが動作しなくなります。<br/>
	 * （ソース解釈(=コンパイル or インタープリット)時にエラーが発生します）<br/>
	 * したがって、JavaScriptソースのステップ実行等を行う場合は、
	 * このシステムプロパティ値を設定しないでください。
	 */
	private static final String PROP_KEY_4_INTERPRET_OPT_LEVEL 
										= "org.intra_mart.jssp.util.compile.interpret.optimizationLevel";
	
	/**
	 * コンパイル処理を直列化するためのモニタ
	 */
	private static final Object SYNCHRONIZED_MONITOR = new Object();

	/**
	 * 最適化レベル
	 */
	private int optimizationLevel = 0;
	
	/**
	 * デバッグ情報の付加
	 */
	private boolean generatingDebug = true;

	/**
	 * 新しいコンパイラを作成します。
	 */
	public JavaScriptCompiler(){
		super();
	}

	/**
	 * 最適化レベルの設定<br>
	 * 
	 * この設定は、インタプリタモード（{@link #interpret(Reader in, String name)}）では有効になりません。<br>
	 * （インタプリタモードの場合、最適化レベルは常に「-1」となります)
	 * @param level レベル（-1 <= level <= 9）
	 */
	public void setOptimizationLevel(int level){
		this.optimizationLevel = Math.min(Math.max(level, -1), 9);
	}

	/**
	 * 最適化レベルに関する現在の設定を返します。
	 * @return 最適化レベルの値（-1 <= level <= 9）
	 */
	public int getOptimizationLevel(){
		return this.optimizationLevel;
	}

	/**
	 * コンパイル時にデバッグ情報を含めるかどうかの設定。
	 * デバッグ情報を含んだ状態で実行すると、エラー発生時にエラー行を
	 * 知ることができます。
	 * デフォルトは、true です。
	 * @param validity デバッグ情報を含める場合 true。
	 */
	public void setGeneratingDebug(boolean validity){
		this.generatingDebug = validity;
	}

	/**
	 * デバッグ情報付加に関する現在の設定を返します。
	 * @return デバッグ情報を含める設定になっている場合 true。
	 */
	public boolean isGeneratingDebug(){
		return this.generatingDebug;
	}

	/**
	 * 指定のソースを解釈し実行します。
	 * @param in ソースを読み込むための入力ストリーム
	 * @param name ソースの名称
	 * @throws IOException
	 */
	public Script interpret(Reader in, String name) throws IOException{
		Context cx = Context.enter();

		synchronized(SYNCHRONIZED_MONITOR){
			try{
				// コンパイルオプションの設定（デバッグ, 最適化レベル）
				cx.setGeneratingDebug(true);
				cx.setOptimizationLevel(-1);
				
				// Javaで実装されたJavaScriptAPIのデバッグ用（通常は設定されていない）
				String interpretOptLevel = System.getProperty(PROP_KEY_4_INTERPRET_OPT_LEVEL);
				if(interpretOptLevel != null){
					cx.setOptimizationLevel(Integer.parseInt(interpretOptLevel));
				}

				// インタープリット！
				return cx.compileReader(in, name, 1, null);
			}
			finally{
				Context.exit();
			}
		}
	}

	/**
	 * 指定のソースをコンパイルします。
	 * @param in ソースを読み込むための入力ストリーム
	 * @param scriptName スクリプト名<BR>
	 * 			（例：sample\example\string\main.js）
	 * @param mainClassName コンパイル後のクラス名（パッケージ名含む）<BR>
	 * 			（例：_sample._example._string._main_js）
	 * @param targetFile 出力ファイル<BR>
	 * 			（例：C:\imart\doc\imart\WEB-INF\classes\_sample\_example\_string\_main_js.class）
	 * @throws IOException
	 */
	public void compile(Reader in, String scriptName, String mainClassName, File targetFile) throws IOException{

		Context cx = Context.enter();
		
		synchronized(SYNCHRONIZED_MONITOR){
			try{
				// JSソース読み込み
				String source = readReader(in);
		
				// コンパイルオプションの設定
				CompilerEnvirons compilerEnv = new CompilerEnvirons();
				compilerEnv.initFromContext(cx);
				compilerEnv.setGenerateDebugInfo(this.isGeneratingDebug());
				compilerEnv.setOptimizationLevel(this.getOptimizationLevel());	
				
				// コンパイル!
				ClassCompiler compiler = new ClassCompiler(compilerEnv);
				Object[] compiled = compiler.compileToClassFiles(source, scriptName, 1, mainClassName);
				if (compiled == null || compiled.length == 0) {
					return; // コンパイル対象がありませんでした。。。
				}
		
				// 出力先ルートディレクトリ作成
				File targetTopDir = null;
				String classFilePath = ClassNameHelper.toFilePath(mainClassName);
				int idx4TopDir = targetFile.getAbsolutePath().indexOf(classFilePath);
				if(idx4TopDir != -1){
					targetTopDir =  new File(targetFile.getAbsolutePath().substring(0, idx4TopDir));
				}
				else{
					throw new IllegalStateException("output file is wrong name : " + targetFile);
					// or WorkDirectory.instance()
				}

				// Classファイル書き込み
	            for (int i = 0; i != compiled.length; i += 2) {
	            	
	                String className = (String)compiled[i];
	                byte[] bytes = (byte[])compiled[i + 1];
	                
	                File outfile = getOutputFile(targetTopDir, className);
                    FileOutputStream os = new FileOutputStream(outfile);
                    try {
                        os.write(bytes);
                    } finally {
                        os.close();
                    }
	            }	            
			}
			finally{
				// コンテキストの開放
				Context.exit();
			}
		}
	}
	
    private static String readReader(Reader r) throws IOException {
		char[] buffer = new char[512];
		int cursor = 0;
		for (;;) {
			int n = r.read(buffer, cursor, buffer.length - cursor);
			if (n < 0) {
				break;
			}
			cursor += n;
			if (cursor == buffer.length) {
				char[] tmp = new char[buffer.length * 2];
				System.arraycopy(buffer, 0, tmp, 0, cursor);
				buffer = tmp;
			}
		}
		return new String(buffer, 0, cursor);
	}

    private File getOutputFile(File parentDir, String className)
    {
        String path = className.replace('.', File.separatorChar);
        path = path.concat(".class");
        File f = new File(parentDir, path);
        String dirPath = f.getParent();
        if (dirPath != null) {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return f;
    }

}

