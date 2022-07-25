# oss.intra-mart.org (Archived)

## IMPORTANT

***This repository has been archived***
このリポジトリは oss.intra-mart.org にて公開されていたソースコードをアーカイブしています。

## im-Jssp Framework Project

サーバ側のビジネスロジックを JavaScript で実装する事の出来るWeb アプリケーション開発モデルの実行エンジンです。<br>
プレゼンテーションページと呼ばれる画面レイアウトを定義したファイルと、ファンクションコンテナと呼ばれる Javascript で実装したプログラムファイルの2つで動作します。<br>
プレゼンテーションページは HTML で記述し、このモデル専用の特殊なタグ（```<IMART>```タグ）を用いて、ファンクションコンテナで生成した各種データを展開し、ブラウザに表示することができます。<br>
スクリプト・・・それも一般的な JavaScript での実装になるため、比較的簡単にアプリケーション開発をできることが特徴です。<br>
また、HTML と JavaScript という、Web ページ作成のための言語で、サーバ側ロジックを実装できるため、技術習得も簡単です。<br>
プログラムがインタプリタで動作する事も特徴の一つで、JSP などと異なりWebアプリケーションをデプロイする手間もありません。<br>

## im-JavaEE Framework Project

MVCモデルをベースとした Web アプリケーション開発の為のフレームワークです。<br>
実装言語は java です。<br>
Seasar2 との連動により、AOP 機能の利用も可能になります。<br>

## im-UISupplements Project

im-UISupplements は、クライアントサイドのJavaScriptクラスで構築されたWebユーザインターフェース部品郡の総称です。Ajaxを利用してユーザインターフェースの向上を目指します。<br>

## im-Common Libraries Project

Java アプリケーション（Webアプリケーションを含む）開発において、皆が利用できるように設計された汎用API群です。<br>
便利なクラスを多数用意しています。<br>
このライブラリは、OPEN INTRA-MART の各プロジェクトでも利用しています。<br>
用途により、いくつかの種類があります。<br>
必要なものだけをチョイスして利用することも可能です。<br>

## im-Tools Project

im-Tools プロジェクトでは、データベースエクスポートツールや、Subversionで管理されているファイルの差分を取得するツールなど、開発に有用なツール群を公開しています。<br>