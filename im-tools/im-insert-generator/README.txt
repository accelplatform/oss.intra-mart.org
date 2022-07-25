= IM インサート・ジェネレータ =

== IM インサート・ジェネレータとは？ ==
IM インサート・ジェネレータは汎用的なデータベースエクスポートツールです。[[BR]]
IM インサート・ジェネレータを利用して複数のテーブルから一括して
SQL INSERT文を出力し別DBなどに容易にデータ移行することが可能です。


== 利用方法 ==

=== 1. 圧縮ファイル「im-insert-generator-X.X.X-bin.zip」を任意のディレクトリに展開します。 ===
 * 展開した結果は以下のようになっています。
   ---------------------------------
   im-insert-generator-X.X.X/
       ├─conf/
       ├─input/
       ├─lib/
       ├─log/
       ├─output/
       ├─build.xml
       ├─LICENSE.txt
       └─README.txt
   ---------------------------------
   * 以下、IM インサート・ジェネレータのルートディレクトリを <% IG_HOME %> と表記します。


=== 2. <% IG_HOME %>/input/jdbc.propertiesファイルに、データベースの設定を行います。 ===
利用するデータベースの設定が、コメント（#）になっている場合は外してください。

=== 3. <% IG_HOME %>/lib フォルダに、JDBCドライバを配置します。 ===
使用しているRDBのJDK1.4に対応したJDBCドライバを使用してください。

=== 4. <% IG_HOME %>/build.xmlを編集します。 ===
以下の箇所をエクスポート対象のテーブル名に書き換えてください。
   ---------------------------------
   <antcall target="generate">
       <param name="type" value="insertGenerator"/>
       <param name="table_name" value="foo_table"/>
       <param name="input_path" value="input/jdbc.properties"/>
       <param name="output_dir" value="output"/>
   </antcall>
   ---------------------------------

複数のテーブルをエクスポートする場合はantcallタグを以下のように複数指定します。
   ---------------------------------
   <antcall target="generate">
       <param name="type" value="insertGenerator"/>
       <param name="table_name" value="foo_table"/>
       <param name="input_path" value="input/jdbc.properties"/>
       <param name="output_dir" value="output"/>
   </antcall>
   
   <antcall target="generate">
       <param name="type" value="insertGenerator"/>
       <param name="table_name" value="bar_table"/>
       <param name="input_path" value="input/jdbc.properties"/>
       <param name="output_dir" value="output"/>
   </antcall>
   ---------------------------------

=== 5. コマンドプロンプトを起動し <% IG_HOME %>に移動します。 ===

=== 6. 環境設定を行います。 ===
==== Windowsの場合 ====
以下のコマンドを、ご使用の環境に合わせて実行してください。
   ---------------------------------
   set ANT_HOME=<% Antの展開ディレクトリ%>
   set JAVA_HOME=<% JDKのホームディレクトリ %>
   set PATH=%PATH%;%ANT_HOME%\bin
   ---------------------------------

===== （例） =====
   ---------------------------------
   C:\im-insert-generator-X.X.X> set ANT_HOME=C:\apache-ant-1.6.5
   C:\im-insert-generator-X.X.X> set JAVA_HOME=C:\j2sdk1.4.2_08
   C:\im-insert-generator-X.X.X> set PATH=%PATH%;%ANT_HOME%\bin
   ---------------------------------

==== Unixの場合 ====
以下のコマンドを、ご使用の環境に合わせて実行してください。 
以下は bash の例です。 
   ---------------------------------
   export ANT_HOME=<% Antの展開ディレクトリ%>
   export JAVA_HOME=<% JDKのホームディレクトリ %>
   export PATH=$PATH:$ANT_HOME/bin
   ---------------------------------

===== （例） =====
   ---------------------------------
   >export ANT_HOME=/usr/local/apache-ant-1.6.5
   >export JAVA_HOME=/usr/local/j2sdk1.4.2_08
   >export PATH=$PATH:$ANT_HOME/bin
   ---------------------------------

=== 7. 「ant」 コマンドで、IM インサート・ジェネレータが実行されます。 ===
===== （例） =====
   ---------------------------------
   C:\im-insert-generator-X.X.X> ant
   ---------------------------------

<% IG_HOME %>/output ディレクトリに、エクスポートされたSQLファイルが出力されます。
 * データ件数が0件の場合はファイルを出力しません。
 

