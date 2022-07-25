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
            API �w���v (<xsl:value-of select="/j2ee:tlds/j2ee:config/j2ee:window-title"/>)
          </TITLE>
          <LINK REL ="stylesheet" TYPE="text/css" HREF="stylesheet.css" TITLE="Style"/>
        </HEAD>
        <SCRIPT>
          function asd() {
            parent.document.title="API �w���v (<xsl:value-of select="normalize-space(/j2ee:tlds/j2ee:config/j2ee:window-title)"/>)";
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
                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1"> &#160;<a href="overview-summary.html"><font CLASS="NavBarFont1"><b>�T�v</b></font></a>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;���C�u����&#160;</font></td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;�^�O&#160;</font></td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1Rev">    &#160;<font CLASS="NavBarFont1Rev"><b>�w���v</b></font>&#160;</td>
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
                &#160;<a HREF="index.html" TARGET="_top"><b>�t���[������</b></a>&#160;
                &#160;<a HREF="help-doc.html" TARGET="_top"><b>�t���[���Ȃ�</b></a>&#160;
                <script>
                  <!--
                  if(window==top) {
                    document.writeln('<A HREF="alltags-noframe.html" TARGET=""><B>���ׂẴ^�O</B></A>');
                  }
                  //-->
                </script>
                <noscript>
                  <a HREF="alltags-noframe.html" TARGET=""><b>���ׂẴ^�O</b></a>
                </noscript>
              </font></td>
            </tr>
          </table>
          <HR/>
          <CENTER>
            <H1>���̃^�O�E���C�u�����E�h�L�������g�͂ǂ��\������Ă��邩�B</H1>
          </CENTER>
		  ����TLD(Tag Library Descriptor)�h�L�������g�̃i�r�Q�[�V�����E�o�[�̐����͈ȉ��̒ʂ�B
          <H3>�T�v</H3>
          <BLOCKQUOTE>
            <P/>
            <A HREF="overview-summary.html">�T�v</A> �y�[�W�́A����TLD�h�L�������g�̑��ʂł���A���ꂼ��̂��߂ɊT�v�����ׂẴ^�O�E���C�u�����̃��X�g��\�����܂��B
          </BLOCKQUOTE>
          <H3>���C�u����</H3>
          <BLOCKQUOTE>
            <P/>
			���ꂼ��̃^�O�E���C�u�����ɂ́A���̌��؁A���X�i�[�A�^�O�A�֐��̃��X�g���܂�1�y�[�W������A���ꂼ��̊T�v�ƂȂ�܂��B
			���̃y�[�W��4�̃J�e�S�����܂݂܂�:
			<UL>
              <li>����</li>
              <li>���X�i�[</li>
              <li>�^�O</li>
              <li>�֐�</li>
            </UL>
          </BLOCKQUOTE>
          <H3>����</H3>
          <BLOCKQUOTE>
            <P/>
            �^�O�E���C�u�����͂O�ȏ�̌��؂��`���邱�Ƃ��ł��܂��B
            �^�O�E���C�u�����Ɍ��؂�����Ȃ�A����͂��ꎩ�g�̃y�[�W�Ɍ��؁A���؂����s����N���X�A����ї��p�\�ȏ������p�����[�^�ɂ��Đ������܂��B
          </BLOCKQUOTE>
          <h3>���X�i�[</h3>
          <blockquote>
            <p/>
            �^�O�E���C�u�����͂O�ȏ�̃��X�i���`���邱�Ƃ��ł��܂��B
            �^�O�E���C�u�����ɏ��Ȃ��Ƃ�1�̃��X�i����`����Ă���Ȃ�A���ׂẴ��X�i�E�N���X���������y�[�W������܂��B
          </blockquote>
          <h3>�^�O</h3>
          <blockquote>
            <p/>
			�^�O�E���C�u�����͂O�ȏ�̃^�O���`���邱�Ƃ��ł��܂��B�܂��A�e�^�O�ɂ̓^�O�ɂ��Đ������邻�ꎩ�g�̃y�[�W������܂��B
			���̃f�B�X�v���C���A���̃��j�[�N�ȃA�N�V�������A�^�O�����s����N���X�ATagExtraInfo�N���X�A�{�f�B�[���e�A�^�C�v�A�ϐ����A�����A�^�O�����I�����A����у^�O�̃I�v�V�����̎g�p���������܂��B
          </blockquote>
          <h3>�֐�</h3>
          <blockquote>
            <p/>
			�^�O�E���C�u�����͂O�ȏ�̊֐����`���邱�Ƃ��ł��܂��B�e�֐��ɂ́A�֐��ɂ��Đ������邻�ꎩ�g�̃y�[�W������܂��B
			���̃��j�[�N�ȃA�N�V�������A���̃f�B�X�v���C���A�^�O�����s����N���X�ɂ��Đ������܂��B
			�^�O�E���C�u�����ɏ��Ȃ��Ƃ�1�̊֐�����`����Ă���Ȃ�A�@�\�̂��ׂĂ̋@�\�A�@�\�����s����N���X�A�@�\�����A����уI�v�V�����̎g�p��������y�[�W������܂��B
          </blockquote>
          <!--
          <H3>�C���f�b�N�X</H3>
          <BLOCKQUOTE>
			<A HREF="index-files/index-1.html">�C���f�b�N�X</A>�͂��ׂĂ̌��؁A���X�i�A�^�O�A�@�\�A�ϐ��A�����ϐ��A����ё������A���t�@�x�b�g���Ƀ��X�g���Ă��܂��B
          </BLOCKQUOTE>
          -->
          <H3>Prev/Next</H3>
          <blockquote>
			�����̃����N�͎����O�̌��؁A���X�i�[�A�^�O�A�֐��A�܂��͊֘A����y�[�W�Ɉړ����܂��B
		  </blockquote>
          <H3>�t���[������/�t���[���Ȃ�</H3>
          <blockquote>
			�����̃����N�́AHTML�t���[���̎g�p�A���g�p��I���ł��܂��B
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
                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1"> &#160;<a href="overview-summary.html"><font CLASS="NavBarFont1"><b>�T�v</b></font></a>&#160;</td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;���C�u����&#160;</font></td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <font CLASS="NavBarFont1">&#160;�^�O&#160;</font></td>
                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1Rev">    &#160;<font CLASS="NavBarFont1Rev"><b>�w���v</b></font>&#160;</td>
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
                &#160;<a HREF="index.html" TARGET="_top"><b>�t���[������</b></a>&#160;
                &#160;<a HREF="help-doc.html" TARGET="_top"><b>�t���[���Ȃ�</b></a>&#160;
                <script>
                  <!--
                  if(window==top) {
                    document.writeln('<A HREF="alltags-noframe.html" TARGET=""><B>���ׂẴ^�O</B></A>');
                  }
                  //-->
                </script>
                <noscript>
                  <a HREF="alltags-noframe.html" TARGET=""><b>���ׂẴ^�O</b></a>
                </noscript>
              </font></td>
            </tr>
          </table>
          <HR/>
        </BODY>
      </HTML>
    </xsl:template>
</xsl:stylesheet> 
