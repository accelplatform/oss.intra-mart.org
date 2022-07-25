/*
 * EventsAttributes.java
 *
 * Created on 2002/01/28, 16:03
 */

package org.intra_mart.framework.base.web.tag.attribute;

import java.io.IOException;

/**
 * ブラウザのイベントに関連するタグの属性情報です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EventsAttributes extends GenericAttributes {

    /**
     * onClick
     */
    private String onClick;

    /**
     * onDblClick
     */
    private String onDblClick;

    /**
     * onMouseDown
     */
    private String onMouseDown;

    /**
     * onMouseUp
     */
    private String onMouseUp;

    /**
     * onMouseOver
     */
    private String onMouseOver;

    /**
     * onMouseMove
     */
    private String onMouseMove;

    /**
     * onMouseOut
     */
    private String onMouseOut;

    /**
     * onKeyPress
     */
    private String onKeyPress;

    /**
     * onKeyDown
     */
    private String onKeyDown;

    /**
     * onKeyUp
     */
    private String onKeyUp;

    /**
     * EventsAttributesを新規に生成します。
     */
    public EventsAttributes() {
        super();
        setOnClick(null);
        setOnDblClick(null);
        setOnMouseDown(null);
        setOnMouseUp(null);
        setOnMouseOver(null);
        setOnMouseMove(null);
        setOnMouseOut(null);
        setOnKeyPress(null);
        setOnKeyDown(null);
        setOnKeyUp(null);
    }

    /**
     * 属性<CODE>onClick</CODE>の値を取得します。
     *
     * @return onClick
     */
    public String getOnClick() {
        return this.onClick;
    }

    /**
     * 属性<CODE>onClick</CODE>の値を設定します。
     *
     * @param onClick onClick
     */
    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    /**
     * 属性<CODE>onDblClick</CODE>の値を取得します。
     *
     * @return onDblClick
     */
    public String getOnDblClick() {
        return this.onDblClick;
    }

    /**
     * 属性<CODE>obDblClick</CODE>の値を設定します。
     *
     * @param onDblClick onDblClick
     */
    public void setOnDblClick(String onDblClick) {
        this.onDblClick = onDblClick;
    }

    /**
     * 属性<CODE>onMouseDown</CODE>の値を取得します。
     *
     * @return onMouseDown
     */
    public String getOnMouseDown() {
        return this.onMouseDown;
    }

    /**
     * 属性<CODE>onMouseDown</CODE>の値を設定します。
     *
     * @param onMouseDown onMouseDown
     */
    public void setOnMouseDown(String onMouseDown) {
        this.onMouseDown = onMouseDown;
    }

    /**
     * 属性<CODE>onMouseUp</CODE>の値を取得します。
     *
     * @return onMouseUp
     */
    public String getOnMouseUp() {
        return this.onMouseUp;
    }

    /**
     * 属性<CODE>onMouseUp</CODE>の値を設定します。
     *
     * @param onMouseUp onMouseUp
     */
    public void setOnMouseUp(String onMouseUp) {
        this.onMouseUp = onMouseUp;
    }

    /**
     * 属性<CODE>onMouseOver</CODE>の値を取得します。
     *
     * @return onMouseOver
     */
    public String getOnMouseOver() {
        return this.onMouseOver;
    }

    /**
     * 属性<CODE>onMouseOver</CODE>の値を設定します。
     *
     * @param onMouseOver onMouseOver
     */
    public void setOnMouseOver(String onMouseOver) {
        this.onMouseOver = onMouseOver;
    }

    /**
     * 属性<CODE>onMouseMove</CODE>の値を取得します。
     *
     * @return onMouseMove
     */
    public String getOnMouseMove() {
        return this.onMouseMove;
    }

    /**
     * 属性<CODE>onMouseMove</CODE>の値を設定します。
     *
     * @param onMouseMove onMouseMove
     */
    public void setOnMouseMove(String onMouseMove) {
        this.onMouseMove = onMouseMove;
    }

    /**
     * 属性<CODE>onMouseOut</CODE>の値を取得します。
     *
     * @return onMouseOut
     */
    public String getOnMouseOut() {
        return this.onMouseOut;
    }

    /**
     * 属性<CODE>onMouseOut</CODE>の値を設定します。
     *
     * @param onMouseOut onMouseOut
     */
    public void setOnMouseOut(String onMouseOut) {
        this.onMouseOut = onMouseOut;
    }

    /**
     * 属性<CODE>onKeyPress</CODE>の値を取得します。
     *
     * @return onKeyPress
     */
    public String getOnKeyPress() {
        return this.onKeyPress;
    }

    /**
     * 属性<CODE>onKeyPress</CODE>の値を設定します。
     *
     * @param onKeyPress onKeyPress
     */
    public void setOnKeyPress(String onKeyPress) {
        this.onKeyPress = onKeyPress;
    }

    /**
     * 属性<CODE>onKeyDown</CODE>の値を取得します。
     *
     * @return onKeyDown
     */
    public String getOnKeyDown() {
        return this.onKeyDown;
    }

    /**
     * 属性<CODE>onKeyDown</CODE>の値を設定します。
     *
     * @param onKeyDown onKeyDown
     */
    public void setOnKeyDown(String onKeyDown) {
        this.onKeyDown = onKeyDown;
    }

    /**
     * 属性<CODE>onKeyUp</CODE>の値を取得します。
     *
     * @return onKeyUp
     */
    public String getOnKeyUp() {
        return this.onKeyUp;
    }

    /**
     * 属性<CODE>onKeyUp</CODE>の値を設定します。
     *
     * @param onKeyUp onKeyUp
     */
    public void setOnKeyUp(String onKeyUp) {
        this.onKeyUp = onKeyUp;
    }

    /**
     * 属性<CODE>onClick</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnClick() throws IOException {
        getTagWriter().printAttribute("onClick", getOnClick());
    }

    /**
     * 属性<CODE>onDblClick</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnDblClick() throws IOException {
        getTagWriter().printAttribute("onDblClick", getOnDblClick());
    }

    /**
     * 属性<CODE>onMouseDown</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnMouseDown() throws IOException {
        getTagWriter().printAttribute("onMouseDown", getOnMouseDown());
    }

    /**
     * 属性<CODE>onMouseUp</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnMouseUp() throws IOException {
        getTagWriter().printAttribute("onMouseUp", getOnMouseUp());
    }

    /**
     * 属性<CODE>onMouseOver</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnMouseOver() throws IOException {
        getTagWriter().printAttribute("onMouseOver", getOnMouseOver());
    }

    /**
     * 属性<CODE>onMouseMove</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnMouseMove() throws IOException {
        getTagWriter().printAttribute("onMouseMove", getOnMouseMove());
    }

    /**
     * 属性<CODE>onMouseOut</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnMouseOut() throws IOException {
        getTagWriter().printAttribute("onMouseOut", getOnMouseOut());
    }

    /**
     * 属性<CODE>onKeyPress</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnKeyPress() throws IOException {
        getTagWriter().printAttribute("onKeyPress", getOnKeyPress());
    }

    /**
     * 属性<CODE>onKeyDown</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnKeyDown() throws IOException {
        getTagWriter().printAttribute("onKeyDown", getOnKeyDown());
    }

    /**
     * 属性<CODE>onKeyUp</CODE>を出力します。
     *
     * @throws IOException 出力中に例外が発生
     */
    protected void printOnKeyUp() throws IOException {
        getTagWriter().printAttribute("onKeyUp", getOnKeyUp());
    }

    /**
     * 属性の内容を表示します。
     *
     * @throws IOException 出力中に例外が発生
     */
    public void printAttributes() throws IOException {
        // onClick
        printOnClick();

        // onDblClick
        printOnDblClick();

        // onMouseDown
        printOnMouseDown();

        // onMouseUp
        printOnMouseUp();

        // onMouseOver
        printOnMouseOver();

        // onMouseMove
        printOnMouseMove();

        // onMouseOut
        printOnMouseOut();

        // onKeyPress
        printOnKeyPress();

        // onKeyDown
        printOnKeyDown();

        // onKeyUp
        printOnKeyUp();
    }
}
