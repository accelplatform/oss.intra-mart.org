package org.intra_mart.common.platform.util;

import java.util.StringTokenizer;

/**
 * 名前に関連するユーティリティです。
 *
 */
public class NameUtil {

    /**
     * 指定された文字列がアプリケーション名として正しいかどうかを判定します。
     *
     * @param name 判定対象の文字列
     * @return 判定結果
     */
    public static boolean isValidApplicationName(String name) {
        if (name == null) {
            return false;
        }

        int nameLength = name.length();
        int count = 0;
        char chr;

        if (nameLength == 0) {
            return false;
        }

        // 最初は英数字のみ
        if (!isNameChar(name.charAt(count++))) {
            return false;
        }

        while (count < nameLength) {
            chr = name.charAt(count++);
            if (chr == '.') {
                // 最後は英数字のみ
                if (count >= nameLength) {
                    return false;
                }

                // ピリオドの次は英数字のみ
                if (!isNameChar(name.charAt(count++))) {
                    return false;
                }
            } else {
                // パッケージ名は英数字のみ
                if (!isNameChar(chr)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 指定された文字が英数字またはアンダーバーまたはピリオドであるかどうかを判断します。
     *
     * @param chr 判定対象の文字
     * @return 英数字またはアンダーバーの場合<code>true</code>、その他の場合は<code>false</code>
     */
    public static boolean isNameChar(char chr) {
        return chr >= 'a'
            && chr <= 'z'
            || chr >= 'A'
            && chr <= 'Z'
            || chr >= '0'
            && chr <= '9'
            || chr == '_'
        	|| chr == '.';
    }

    /**
     * 指定された文字列が名前として妥当かどうか判定します。
     * 以下の文字列のみを含むもの名前として妥当であると判断されます。
     * <ul>
     * <li>英数字
     * <li>アンダーバー（&quot;<code>_</code>&quot;）
     * </ul>
     *
     * @param name 文字列
     * @return 文字列が名前として妥当な場合<code>true</code>、それ以外の場合は<code>false</code>
     */
    public static boolean isValidName(String name) {
        int len = 0;
        char chr;

        if (name == null) {
            return false;
        }

        len = name.length();
        if (len == 0) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            if (isNameChar(name.charAt(i))) {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * 指定された文字列がパスとして妥当かどうか判定します。
     * 以下の条件をすべて満たす場合、パスとして妥当であると判断されます。
     * <ul>
     * <li>スラッシュ（&quot;<code>/</code>&quot;）から始まる
     * <li>{@link #isValidName(String)}を満たす文字列で終了する、またはスラッシュ一文字のみである
     * <li>スラッシュとスラッシュの間の文字列は{@link #isValidName(String)}を満たす
     * </ul>
     *
     * @param path 文字列
     * @return 文字列がパスとして妥当な場合<code>true</code>、それ以外の場合は<code>false</code>
     */
    public static boolean isValidPath(String path) {
        int len = 0;
        char chr;
        StringTokenizer tokens = null;
        String token = null;
        boolean flag = false;

        // 最低一文字必要
        if (path == null) {
            return false;
        }
        len = path.length();
        if (len == 0) {
            return false;
        }

        // スラッシュのみならば妥当
        if (path.equals("/")) {
            return true;
        }

        // スラッシュの間が名前であり、スラッシュで終わらなければ妥当
        tokens = new StringTokenizer(path, "/", true);
        while (tokens.hasMoreTokens()) {
            token = tokens.nextToken();
            if (token.charAt(0) != '/') {
                return false;
            }
            if (!tokens.hasMoreTokens()) {
                return false;
            }
            token = tokens.nextToken();
            if (!isValidName(token)) {
                return false;
            }
        }

        return true;
    }

}
