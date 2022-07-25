Package("im.util");

Import("im.util.StepFrameProvider");

/**
 * @fileoverview <br/>
 * フレーム単位で定期的に処理を実行するための抽象クラス
 */
 
Class("im.util.StepFrame").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @class フレーム単位で定期的に処理を実行するための抽象クラス。<br/>
	 * フレーム単位で定期的に処理を実行したい場合、拡張クラスを作成し、{@link #stepFrame}を実装してください。
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.5
	 */
	im.util.StepFrame = function () {
		/* constructor */
		{
			this.superclass();
		}
		/**
		 * ステップフレーム処理を開始します。
		 */
		this.start = function () {
			im.util.StepFrameProvider.getInstance().addStepFrame(this);
		}
		/**
		 * ステップフレーム処理を停止します。
		 */
		this.stop = function () {
			im.util.StepFrameProvider.getInstance().removeStepFrame(this);
		}
		/**
		 * １ステップフレーム処理。<br/>
		 * <br/>
		 * 拡張クラスで本メソッドを実装してください。<br/>
		 */
		this.stepFrame = function () {
			alert("Please implements the method 'im.util.StepFrame#stepFrame()");
		}
	}
);
