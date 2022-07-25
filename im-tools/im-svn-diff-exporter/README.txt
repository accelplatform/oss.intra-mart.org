======================================================================
SVN差分抽出ツール(im-svn-diff-exporter)

2007-10-22
http://oss.intra-mart.org/projects/im-tools/wiki/im-svn-diff-exporter
======================================================================

== SVN差分抽出ツールとは？ ==
Subversionで管理されているファイルの差分を取得するツールです。
ある2点のリビジョン間の差分を抽出し、差分ファイルの一覧 または 差分ファイルのエクスポートを行います。 

== ダウンロード ==
以下のURLよりダウンロード可能です。
 * http://oss.intra-mart.org/downloads/im-tools/developing/archives_for_src_0.1.0/im-svn-diff-exporter-0.1.0-bin.zip

== 利用方法 ==
Antタスクとして利用することが可能です。
以下のタスクが提供されています。
 * export
   * SVNリポジトリからファイルをエクスポートします。

 * diffExport
   * 差分ファイルのエクスポートを行います。

 * diffFilePathExtract
   * 差分ファイルの一覧を出力します。
     * 更新タイプ、ファイル名、リビジョン、作成者、コミット日時、コメントの6項目がタブ区切りで出力されます。

  ※ アーカイブ内の 「im-svn-diff-exporter-X.X.X/build.xml」 にビルドファイルのサンプルがあります。

  ※ 各Antタスクの詳細は、以下のURLをご参照ください。
   http://oss.intra-mart.org/projects/im-tools/wiki/im-svn-diff-exporter

----------------------------------------------------------------------
All Rights Reserved by OPEN INTRA-MART
http://oss.intra-mart.org/
