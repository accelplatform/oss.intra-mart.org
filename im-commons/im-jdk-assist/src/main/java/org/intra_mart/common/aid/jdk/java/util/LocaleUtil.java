package org.intra_mart.common.aid.jdk.java.util;

import java.util.Locale;

/**
 * ロケールユーティリティクラス。 <BR>
 * <BR>
 * ロケールに関する実用的な関数および定数値を提供するクラス。 <BR>
 *
 */
public class LocaleUtil {
/**
 * テスト用
 */
/*
public static void main(String[] args){
	try{
		String[]locales = new String[]{
				null,
				"",
				"     ",
				"ja",
				"ja_JP",
				"ja_JP_WIN",
				"ja_JP_Traditional_WIN",
				"ja__Traditional_WIN",
				"_JP",
				"_JP_Traditional_WIN",
				"__Traditional_WIN"
		};
		for(int i = 0; i < locales.length; i++){
			System.out.println("--------------------------------------------------");
			Locale locale = LocaleUtil.toLocale(locales[i]);
			System.out.println("data: " + locales[i]);
			if(locale != null){
				System.out.println("locale: " + locale.toString());
				System.out.println("language: " + locale.getLanguage());
				System.out.println("country: " + locale.getCountry());
				System.out.println("variant: " + locale.getVariant());
				System.out.println("DisplayLanguage: " + locale.getDisplayLanguage());
				System.out.println("DisplayCountry: " + locale.getDisplayCountry());
				System.out.println("DisplayVariant: " + locale.getDisplayVariant());
				System.out.println("DisplayName: " + locale.getDisplayName());
				System.out.println("ISO3Language: " + locale.getISO3Language());
				System.out.println("ISO3Country: " + locale.getISO3Country());
			}
			System.out.println("--------------------------------------------------");
		}
//		System.out.println("null: " + LocaleUtil.toLocale(null));
//		System.out.println("zero-length string: " + LocaleUtil.toLocale(""));
//		System.out.println("Only space: " + LocaleUtil.toLocale("    "));
//		System.out.println("ja: " + LocaleUtil.toLocale("ja"));
//		System.out.println("ja_JP: " + LocaleUtil.toLocale("ja_JP"));
//		System.out.println("ja_JP_WIN: " + LocaleUtil.toLocale("ja_JP_WIN"));
//		System.out.println("ja_JP_Traditional_WIN: " + LocaleUtil.toLocale("ja_JP_Traditional_WIN"));
//		System.out.println("ja__Traditional_WIN: " + LocaleUtil.toLocale("ja__Traditional_WIN"));
//		System.out.println("_JP: " + LocaleUtil.toLocale("_JP"));
//		System.out.println("__Traditional_WIN: " + LocaleUtil.toLocale("__Traditional_WIN"));
	}
	catch(Throwable t){
		t.printStackTrace();
	}
}
*/

    /**
     * コンストラクタ。<BR>
     * <BR>
     * 隠蔽します。
     */
    private LocaleUtil() {
        super();
    }

    /**
     * ロケール用のセパレータ。<BR>
     * <BR>
     * 同じパッケージ内のみ使用可能。
     */
    static final String LOCALE_SEPARATOR = "_";

    /**
     * ロケール文字列をロケールクラスに変換する。 <BR>
     * <BR>
     * ロケール文字列を解析し、ロケールクラスを生成して返却する。 <BR>
     *
     * @param locale
     *            ロケール文字列 <BR>
     *            例 ja_JP,en_US などロケールの文字列表現
     * @return ロケールクラス <BR>
     *         ロケール文字列が空文字列、空白文字のみの文字列およびnullの場合はnullを返却する。 <BR>
     */
    public static Locale toLocale(String locale) {
		if(locale == null){
			return null;
		}
		else{
			String localeString = locale.trim();
			if(localeString.length() == 0){ return null; }

			int index = localeString.indexOf(LocaleUtil.LOCALE_SEPARATOR);
			if(index == -1){
				// セパレータ文字がない＝言語指定のみ
				return new Locale(localeString);
			}
			// 言語コードの特定
			String language = localeString.substring(0, index);

			// 国コードの検索
			index = localeString.indexOf(LocaleUtil.LOCALE_SEPARATOR, index + 1);
			if(index == -1){
				// セパレータ文字が見つからない＝バリアントがない
				String country = localeString.substring(language.length() + 1);
				return new Locale(language, country);
			}
			else{
				// 国コードの特定
				String country = localeString.substring(language.length() + 1, index);
				// バリアントの特定
				String variant = localeString.substring(index + 1);

				return new Locale(language, country, variant);
			}
		}


/*
        String language = null;
        String country = null;
        String variant = null;

        // ロケール文字列チェック
        if (locale == null || locale.trim().length() == 0) {
            return null;
        }

        //ロケール文字列をロケール用のセパレータ(LOCALE_SEPARATOR)で分割する。
        StringTokenizer st = new StringTokenizer(locale, LOCALE_SEPARATOR);

        // 言語の取得
        if (st.hasMoreTokens()) {
            language = st.nextToken();
        }
        // 国の取得
        if (st.hasMoreTokens()) {
            country = st.nextToken();
        }
        // 国情報がないので、言語情報でロケールを作成。
        else {
            return new Locale(language);
        }
        // バリアントの取得
        if (st.hasMoreTokens()) {
            variant = st.nextToken();
        }
        // バリアント情報がないので、言語および国情報でロケールを作成。
        else {
            return new Locale(language, country);
        }

        // 言語、国およびバリアント情報でロケールを作成。
        return new Locale(language, country, variant);

*/
    }

}