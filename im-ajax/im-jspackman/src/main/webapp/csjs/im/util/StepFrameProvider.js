Package("im.util");

Import("im.util.List");


/**
 * @fileoverview <br/>
 * フレーム単位で定期的に実行する仕組みを提供
 */
 
Class("im.util.StepFrameProvider").define(
	/**
	 * コンストラクタ。{@link #getInstance}を使用してインスタンスを取得してください。
	 * @constructor
	 * @class フレーム単位で定期的に実行する仕組みを提供するクラスです。
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.5
	 */
	im.util.StepFrameProvider = function () {
		var stepFrameList;
		var timerId;
		/* constructor */
		{
			this.superclass();
			stepFrameList = new im.util.List();
		}
		/**
		 * StepFrame インスタンスを追加します。<br/>
		 * @param {im.util.StepFrame} stepFrame 追加する StepFrame インスタンス
		 */
		this.addStepFrame = function (stepFrame) {
			if (stepFrameList.isEmpty()) {
				this.startFrame();
			}
			// 同じインスタンスが既に登録されている場合、リストに追加しない。
			var idx = stepFrameList.indexOf(stepFrame);
			if (idx < 0) {
				stepFrameList.add(stepFrame);
			} 
		}
		/**
		 * StepFrame インスタンスを削除します。<br/>
		 * @param {im.util.StepFrame} stepFrame 削除する StepFrame インスタンス
		 */
		this.removeStepFrame = function (stepFrame) {
			var idx = stepFrameList.indexOf(stepFrame);
			if (idx >= 0) {
				stepFrameList.remove(idx);
			}
			if (stepFrameList.isEmpty()) {
				this.stopFrame();
			}
		}
		/** @private */
		this.invokeStepFrame = function () {
			var it = stepFrameList.iterator();
			while (it.hasNext()) {
				try {
					it.next().stepFrame();
				} catch (ignore) {
					// TODO ログ出力
				}
			}
			// リストが空でなかったら次のステップフレーム処理を予約。
			if (!stepFrameList.isEmpty()) {
				this.startFrame();
			}
		}
		/** @private */
		this.startFrame = function () {
			var thisClazz = im.util.StepFrameProvider;
			timerId = setTimeout(thisClazz.stepFrame, thisClazz.FRAME_RATE);
		}
		/** @private */
		this.stopFrame = function () {
			clearTimeout(timerId);
		}
	}
);
/* definition of static fields and/or static methods */
{
	/**
	 * フレームレート
	 * @type Number
	 * @final
	 */
	im.util.StepFrameProvider.FRAME_RATE = 24;
	/**
	 * フレームインターバル時間(秒)
	 * @type Number
	 * @final
	 * @private
	 */
	im.util.StepFrameProvider.FRAME_INTERVAL_SECOND = parseInt(1000 / im.util.StepFrameProvider.FRAME_RATE);
	/**
	 * シングルトン。
	 * @type im.util.StepFrameProvider
	 * @private
	 */
	im.util.StepFrameProvider.singleton;
	/**
	 * {@link im.util.StepFrameProvider}の唯一のインスタンスを取得します。
	 * @type im.util.StepFrameProvider
	 * @public
	 */
	im.util.StepFrameProvider.getInstance = function () {
		return im.util.StepFrameProvider.singleton;
	}
	/** @private */
	im.util.StepFrameProvider.stepFrame = function () {
		im.util.StepFrameProvider.singleton.invokeStepFrame();
	}
	/** @private */
	im.util.StepFrameProvider.initialize = function () {
		im.util.StepFrameProvider.singleton = new im.util.StepFrameProvider();
	}
}
