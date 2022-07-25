/*
 * SecurityUtil.java
 *
 * Created on 2004/05/25, 18:55:21
 */

package org.intra_mart.framework.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

/**
 * セキュリティに関連するユーティリティです。
 *
 * @author INTRAMART
 * @since 4.3
 */
public class SecurityUtil {

    /**
     * 暗号を作成します。
     *
     * @param applicationID アプリケーションID
     * @param serviceID サービスID
     * @param httpSession セッション情報
     * @return 暗号化クエリー
     * @throws ServletException フィルタ内で例外が発生
     */
    public static String createCheckSum(
        String applicationID,
        String serviceID,
        HttpSession httpSession)
        throws ServletException {

        int sequre;
        int proof;
        String strCode = null;

        sequre =
            httpSession.getId().hashCode()
                ^ new Long(httpSession.getCreationTime()).hashCode();
        proof = applicationID.hashCode() ^ serviceID.hashCode() ^ sequre;

        strCode =
            Integer.toString(sequre, 36) + "_" + Integer.toString(proof, 36);

        return strCode;
    }
}
