<?xml version="1.0" encoding="Shift_JIS" ?>

<xsl:stylesheet version="1.0"
    xmlns:j2ee="http://java.sun.com/xml/ns/j2ee" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <xsl:output method="html" indent="yes"/>

    <!-- template rule matching source root element -->
    <xsl:template match="/">
      <HTML>
        <HEAD>
          <TITLE>
            API ヘルプ (<xsl:value-of select="/j2ee:tlds/j2ee:config/j2ee:window-title"/>)
          </TITLE>
          <LINK REL ="stylesheet" TYPE="text/css" HREF="stylesheet.css" TITLE="Style"/>
        </HEAD>
        <SCRIPT>
          function asd() {
            parent.document.title="API ヘルプ (<xsl:value-of select="normalize-space(/j2ee:tlds/j2ee:config/j2ee:window-title)"/>)";
          }
        </SCRIPT>
        <BODY BGCOLOR="white" onload="asd();">
          <a name="navbar_top"><!-- --></a>
          <table border="0" width="100%" cellpadding="1" cellspacing="0">
            <tr>
              <td COLSPAN="3" BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
                <a NAME="navbar_top_firstrow"><!-- --></a>
                <table BORDER="0" CELLPADDING="0" CELLSPACING="3">
                  <tr ALIGN="center" VALIGN="top">
                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1"> &#160;<a href="overview-summary.html"><font CLASS="NavBarFont1"><b>概要</b></font></a>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;ライブラリ&#160;</font></td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;タグ&#160;</font></td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1Rev">    &#160;<font CLASS="NavBarFont1Rev"><b>ヘルプ</b></font>&#160;</td>
                  </tr>
                </table>
              </td>
              <td ALIGN="right" VALIGN="top" ROWSPAN="3"><em>
                </em>
              </td>
            </tr>
            <tr>
              <td BGCOLOR="white" CLASS="NavBarCell2"><font SIZE="-2">
                &#160;PREV&#160;
                &#160;NEXT&#160;
              </font></td>
              <td BGCOLOR="white" CLASS="NavBarCell2"><font SIZE="-2">
                &#160;<a HREF="index.html" TARGET="_top"><b>フレームあり</b></a>&#160;
                &#160;<a HREF="help-doc.html" TARGET="_top"><b>フレームなし</b></a>&#160;
                <script>
                  <!--
                  if(window==top) {
                    document.writeln('<A HREF="alltags-noframe.html" TARGET=""><B>すべてのタグ</B></A>');
                  }
                  //-->
                </script>
                <noscript>
                  <a HREF="alltags-noframe.html" TARGET=""><b>すべてのタグ</b></a>
                </noscript>
              </font></td>
            </tr>
          </table>
          <HR/>
          <CENTER>
            <H1>このタグ・ライブラリ・ドキュメントはどう構成されているか。</H1>
          </CENTER>
		  このTLD(Tag Library Descriptor)ドキュメントのナビゲーション・バーの説明は以下の通り。
          <H3>概要</H3>
          <BLOCKQUOTE>
            <P/>
            <A HREF="overview-summary.html">概要</A> ページは、このTLDドキュメントの第一面であり、それぞれのために概要をすべてのタグ・ライブラリのリストを表示します。
          </BLOCKQUOTE>
          <H3>ライブラリ</H3>
          <BLOCKQUOTE>
            <P/>
			それぞれのタグ・ライブラリには、その検証、リスナー、タグ、関数のリストを含む1ページがあり、それぞれの概要となります。
			このページは4つのカテゴリを含みます:
			<UL>
              <li>検証</li>
              <li>リスナー</li>
              <li>タグ</li>
              <li>関数</li>
            </UL>
          </BLOCKQUOTE>
          <H3>検証</H3>
          <BLOCKQUOTE>
            <P/>
            タグ・ライブラリは０以上の検証を定義することができます。
            タグ・ライブラリに検証があるなら、それはそれ自身のページに検証、検証を実行するクラス、および利用可能な初期化パラメータについて説明します。
          </BLOCKQUOTE>
          <h3>リスナー</h3>
          <blockquote>
            <p/>
            タグ・ライブラリは０以上のリスナを定義することができます。
            タグ・ライブラリに少なくとも1つのリスナが定義されているなら、すべてのリスナ・クラスを説明するページがあります。
          </blockquote>
          <h3>タグ</h3>
          <blockquote>
            <p/>
			タグ・ライブラリは０以上のタグを定義することができます。また、各タグにはタグについて説明するそれ自身のページがあります。
			そのディスプレイ名、そのユニークなアクション名、タグを実行するクラス、TagExtraInfoクラス、ボディー内容、タイプ、変数情報、属性、タグが動的属性、およびタグのオプションの使用例を説明します。
          </blockquote>
          <h3>関数</h3>
          <blockquote>
            <p/>
			タグ・ライブラリは０以上の関数を定義することができます。各関数には、関数について説明するそれ自身のページがあります。
			そのユニークなアクション名、そのディスプレイ名、タグを実行するクラスについて説明します。
			タグ・ライブラリに少なくとも1つの関数が定義されているなら、機能のすべての機能、機能を実行するクラス、機能署名、およびオプションの使用例を示すページがあります。
          </blockquote>
          <!--
          <H3>インデックス</H3>
          <BLOCKQUOTE>
			<A HREF="index-files/index-1.html">インデックス</A>はすべての検証、リスナ、タグ、機能、変数、属性変数、および属性をアルファベット順にリストしています。
          </BLOCKQUOTE>
          -->
          <H3>Prev/Next</H3>
          <blockquote>
			これらのリンクは次か前の検証、リスナー、タグ、関数、または関連するページに移動します。
		  </blockquote>
          <H3>フレームあり/フレームなし</H3>
          <blockquote>
			これらのリンクは、HTMLフレームの使用、未使用を選択できます。
		  </blockquote>
          <BR/>
          <HR/>
          <a name="navbar_bottom"><!-- --></a>
          <table border="0" width="100%" cellpadding="1" cellspacing="0">
            <tr>
              <td COLSPAN="3" BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
                <a NAME="navbar_bottom_firstrow"><!-- --></a>
                <table BORDER="0" CELLPADDING="0" CELLSPACING="3">
                  <tr ALIGN="center" VALIGN="top">
                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1"> &#160;<a href="overview-summary.html"><font CLASS="NavBarFont1"><b>概要</b></font></a>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;ライブラリ&#160;</font></td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;タグ&#160;</font></td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1Rev">    &#160;<font CLASS="NavBarFont1Rev"><b>ヘルプ</b></font>&#160;</td>
                  </tr>
                </table>
              </td>
              <td ALIGN="right" VALIGN="top" ROWSPAN="3"><em>
                </em>
              </td>
            </tr>
            <tr>
              <td BGCOLOR="white" CLASS="NavBarCell2"><font SIZE="-2">
                &#160;PREV&#160;
                &#160;NEXT&#160;
              </font></td>
              <td BGCOLOR="white" CLASS="NavBarCell2"><font SIZE="-2">
                &#160;<a HREF="index.html" TARGET="_top"><b>フレームあり</b></a>&#160;
                &#160;<a HREF="help-doc.html" TARGET="_top"><b>フレームなし</b></a>&#160;
                <script>
                  <!--
                  if(window==top) {
                    document.writeln('<A HREF="alltags-noframe.html" TARGET=""><B>すべてのタグ</B></A>');
                  }
                  //-->
                </script>
                <noscript>
                  <a HREF="alltags-noframe.html" TARGET=""><b>すべてのタグ</b></a>
                </noscript>
              </font></td>
            </tr>
          </table>
          <HR/>
        </BODY>
      </HTML>
    </xsl:template>
</xsl:stylesheet> 
