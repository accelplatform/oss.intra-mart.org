package org.intra_mart.jssp.util.compile;

import java.io.IOException;
import java.io.Reader;

import org.intra_mart.jssp.view.ViewScope;


public final class PresentationPageCompiler{
	/**
	 * コンストラクタ
	 */
	public PresentationPageCompiler(){
		super();
	}

	/**
	 * 指定のソースをコンパイルします。
	 * @param in ソースを読み込むための入力ストリーム
	 * @throws IOException
	 */
	public ViewScope compile(Reader in) throws IOException{
		return new ViewScope(in);
	}
}

