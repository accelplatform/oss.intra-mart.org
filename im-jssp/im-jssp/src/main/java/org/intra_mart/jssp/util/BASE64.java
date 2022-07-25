package org.intra_mart.jssp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

// TODO [OSS-JSSP] im-commonsに移動予定
/**
 * 文字列をBASE64エンコード・デコードするクラス
 */
public class BASE64{
	// クラス変数
	static final String B_ELEMENTS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

//	public static void main(String[] args){
//		try{
//			String b_str = encode((new FileObject("debug.txt")).getByteArray());
//			System.out.println(b_str);
//			System.out.println(new String(decode(b_str), "SJIS"));
//		}
//		catch(Exception e){
//			System.out.println(e);
//		}
//
//		System.exit(0);
//	}


	/**
	 * ＢＡＳＥ６４エンコードをします。
	 * 
	 * @param target エンコード対象バイト列
	 * @return エンコード結果文字列
	 */
	public static String encode(byte[] target){
		StringBuffer buf = new StringBuffer();
		ByteArrayInputStream in = new ByteArrayInputStream(target);
		int chr = 0, block;

		try{
			while((chr = in.read()) != -1){
				block = chr << 16;
				if((chr = in.read()) != -1){
					block |= chr << 8;
					if((chr = in.read()) != -1){
						block |= chr;
						buf.append(B_ELEMENTS.charAt(block >> 18));
						buf.append(B_ELEMENTS.charAt((block >> 12) & 0x3f));
						buf.append(B_ELEMENTS.charAt((block >> 6) & 0x3f));
						buf.append(B_ELEMENTS.charAt(block & 0x3f));
					}
					else{
						buf.append(B_ELEMENTS.charAt(block >> 18));
						buf.append(B_ELEMENTS.charAt((block >> 12) & 0x3f));
						buf.append(B_ELEMENTS.charAt((block >> 6) & 0x3f));
						buf.append('=');
					}
				}
				else{
					buf.append(B_ELEMENTS.charAt(block >> 18));
					buf.append(B_ELEMENTS.charAt((block >> 12) & 0x3f));
					buf.append("==");
				}
			}

//			// 文字列長の調節
//			buf.setLength((int) Math.ceil(target.length / 3) * 4);

//			int mod = target.length % 3;
//			if(mod > 0){
//				buf.setCharAt(buf.length() - 1, '=');
//				if(mod == 1){
//					buf.setCharAt(buf.length() - 2, '=');
//				}
//			}

			in.close();
		}
		catch(IOException ioe){
			System.out.println(ioe);
		}

		return buf.toString();
	}


	/**
	 * ＢＡＳＥ６４デコードをします。
	 * @param target デコード対象文字列
	 * @return デコード結果文字列
	 */
	public static byte[] decode(String target){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StringReader in = new StringReader(target);
		int chr, idx, block;

		try{
			while((chr = in.read()) != -1){
				idx = B_ELEMENTS.indexOf((char) chr);
				block = idx << 18;
				if((idx = B_ELEMENTS.indexOf((char) in.read())) != -1){
					block |= idx << 12;
					if((idx = B_ELEMENTS.indexOf((char) in.read())) != -1){
						block |= idx << 6;
						if((idx = B_ELEMENTS.indexOf((char) in.read())) != -1){
							block |= idx;
							out.write(block >> 16);				// １バイト目
							out.write((block >> 8) & 0xff);		// ２バイト目
							out.write(block & 0xff);			// ３バイト目
						}
						else{
							out.write(block >> 16);				// １バイト目
							out.write((block >> 8) & 0xff);		// ２バイト目
							break;
						}
					}
					else{
						out.write(block >> 16);					// １バイト目
						break;
					}
				}
				else{
					out.write(block >> 16);						// １バイト目
					break;
				}
			}

//			// 有功バイト数の確認
//			if(target.endsWith("=")){
//				if(target.endsWith("==")){
//					byte[] result = out.toByteArray();			// 仮取得
//					out.reset();								// 初期化
//					out.write(result, 0, result.length - 2);	// 再登録
//				}
//				else{
//					byte[] result = out.toByteArray();			// 仮取得
//					out.reset();								// 初期化
//					out.write(result, 0, result.length - 1);	// 再登録
//				}
//			}
//			else{
//				byte[] result = out.toByteArray();				// 仮取得
//				out.reset();									// 初期化
//				out.write(result, 0, result.length - 3);		// 再登録
//			}
		}
		catch(IOException ioe){
			System.out.println(ioe);
		}
		finally{
			in.close();
			try{
				out.close();
			}
			catch(IOException ioe){
				System.out.println(ioe);
			}
		}

		return out.toByteArray();						// 返却
	}
}


/* End of File */