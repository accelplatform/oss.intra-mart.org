package org.intra_mart.common.platform.util;

import org.intra_mart.common.platform.util.NameUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 *
 */
public class NameUtilTest extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(NameUtilTest.class);
        suite.setName("NameUtil test");

        return suite;
    }

    /**
     * @param name
     */
    public NameUtilTest(String name) {
        super(name);
    }

    public void testIsValidApplicationName() throws Exception {
        assertTrue(NameUtil.isValidApplicationName("a"));
        assertTrue(NameUtil.isValidApplicationName("b"));
        assertTrue(NameUtil.isValidApplicationName("c"));
        assertTrue(NameUtil.isValidApplicationName("d"));
        assertTrue(NameUtil.isValidApplicationName("e"));
        assertTrue(NameUtil.isValidApplicationName("f"));
        assertTrue(NameUtil.isValidApplicationName("g"));
        assertTrue(NameUtil.isValidApplicationName("h"));
        assertTrue(NameUtil.isValidApplicationName("i"));
        assertTrue(NameUtil.isValidApplicationName("j"));
        assertTrue(NameUtil.isValidApplicationName("k"));
        assertTrue(NameUtil.isValidApplicationName("l"));
        assertTrue(NameUtil.isValidApplicationName("m"));
        assertTrue(NameUtil.isValidApplicationName("n"));
        assertTrue(NameUtil.isValidApplicationName("o"));
        assertTrue(NameUtil.isValidApplicationName("p"));
        assertTrue(NameUtil.isValidApplicationName("q"));
        assertTrue(NameUtil.isValidApplicationName("r"));
        assertTrue(NameUtil.isValidApplicationName("s"));
        assertTrue(NameUtil.isValidApplicationName("t"));
        assertTrue(NameUtil.isValidApplicationName("u"));
        assertTrue(NameUtil.isValidApplicationName("v"));
        assertTrue(NameUtil.isValidApplicationName("w"));
        assertTrue(NameUtil.isValidApplicationName("x"));
        assertTrue(NameUtil.isValidApplicationName("y"));
        assertTrue(NameUtil.isValidApplicationName("z"));
        assertTrue(NameUtil.isValidApplicationName("A"));
        assertTrue(NameUtil.isValidApplicationName("B"));
        assertTrue(NameUtil.isValidApplicationName("C"));
        assertTrue(NameUtil.isValidApplicationName("D"));
        assertTrue(NameUtil.isValidApplicationName("E"));
        assertTrue(NameUtil.isValidApplicationName("F"));
        assertTrue(NameUtil.isValidApplicationName("G"));
        assertTrue(NameUtil.isValidApplicationName("H"));
        assertTrue(NameUtil.isValidApplicationName("I"));
        assertTrue(NameUtil.isValidApplicationName("J"));
        assertTrue(NameUtil.isValidApplicationName("K"));
        assertTrue(NameUtil.isValidApplicationName("L"));
        assertTrue(NameUtil.isValidApplicationName("M"));
        assertTrue(NameUtil.isValidApplicationName("N"));
        assertTrue(NameUtil.isValidApplicationName("O"));
        assertTrue(NameUtil.isValidApplicationName("P"));
        assertTrue(NameUtil.isValidApplicationName("Q"));
        assertTrue(NameUtil.isValidApplicationName("R"));
        assertTrue(NameUtil.isValidApplicationName("S"));
        assertTrue(NameUtil.isValidApplicationName("T"));
        assertTrue(NameUtil.isValidApplicationName("U"));
        assertTrue(NameUtil.isValidApplicationName("V"));
        assertTrue(NameUtil.isValidApplicationName("W"));
        assertTrue(NameUtil.isValidApplicationName("X"));
        assertTrue(NameUtil.isValidApplicationName("Y"));
        assertTrue(NameUtil.isValidApplicationName("Z"));
        assertTrue(NameUtil.isValidApplicationName("0"));
        assertTrue(NameUtil.isValidApplicationName("1"));
        assertTrue(NameUtil.isValidApplicationName("2"));
        assertTrue(NameUtil.isValidApplicationName("3"));
        assertTrue(NameUtil.isValidApplicationName("4"));
        assertTrue(NameUtil.isValidApplicationName("5"));
        assertTrue(NameUtil.isValidApplicationName("6"));
        assertTrue(NameUtil.isValidApplicationName("7"));
        assertTrue(NameUtil.isValidApplicationName("8"));
        assertTrue(NameUtil.isValidApplicationName("9"));
        assertTrue(NameUtil.isValidApplicationName("_"));
        assertTrue(
            NameUtil.isValidApplicationName(
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"));
        assertTrue(NameUtil.isValidApplicationName("aaa.bbb"));
        assertFalse(NameUtil.isValidApplicationName(""));
        assertFalse(NameUtil.isValidApplicationName(" "));
        assertTrue(NameUtil.isValidApplicationName(".")); //← 正しいアプリケーション名とみなします。
        assertFalse(NameUtil.isValidApplicationName("-"));
        assertFalse(NameUtil.isValidApplicationName("/"));
        assertTrue(NameUtil.isValidApplicationName(".aaa")); //← 正しいアプリケーション名とみなします。
        assertTrue(NameUtil.isValidApplicationName(".aaa.bbb")); //← 正しいアプリケーション名とみなします。
        assertFalse(NameUtil.isValidApplicationName("aaa.bbb."));
        assertFalse(NameUtil.isValidApplicationName("" + (char) ('a' - 1)));
        assertFalse(NameUtil.isValidApplicationName("" + (char) ('z' + 1)));
        assertFalse(NameUtil.isValidApplicationName("" + (char) ('A' - 1)));
        assertFalse(NameUtil.isValidApplicationName("" + (char) ('Z' + 1)));
        assertFalse(NameUtil.isValidApplicationName("" + (char) ('0' - 1)));
        assertFalse(NameUtil.isValidApplicationName("" + (char) ('9' + 1)));
        assertFalse(NameUtil.isValidApplicationName("" + (char) ('_' - 1)));
        assertFalse(NameUtil.isValidApplicationName("" + (char) ('_' + 1)));
        assertFalse(NameUtil.isValidApplicationName(null));
        assertFalse(NameUtil.isValidApplicationName(""));
    }

    public void testIsNameChar() throws Exception {
        assertTrue(NameUtil.isNameChar('a'));
        assertTrue(NameUtil.isNameChar('b'));
        assertTrue(NameUtil.isNameChar('c'));
        assertTrue(NameUtil.isNameChar('d'));
        assertTrue(NameUtil.isNameChar('e'));
        assertTrue(NameUtil.isNameChar('f'));
        assertTrue(NameUtil.isNameChar('g'));
        assertTrue(NameUtil.isNameChar('h'));
        assertTrue(NameUtil.isNameChar('i'));
        assertTrue(NameUtil.isNameChar('j'));
        assertTrue(NameUtil.isNameChar('k'));
        assertTrue(NameUtil.isNameChar('l'));
        assertTrue(NameUtil.isNameChar('m'));
        assertTrue(NameUtil.isNameChar('n'));
        assertTrue(NameUtil.isNameChar('o'));
        assertTrue(NameUtil.isNameChar('p'));
        assertTrue(NameUtil.isNameChar('q'));
        assertTrue(NameUtil.isNameChar('r'));
        assertTrue(NameUtil.isNameChar('s'));
        assertTrue(NameUtil.isNameChar('t'));
        assertTrue(NameUtil.isNameChar('u'));
        assertTrue(NameUtil.isNameChar('v'));
        assertTrue(NameUtil.isNameChar('w'));
        assertTrue(NameUtil.isNameChar('x'));
        assertTrue(NameUtil.isNameChar('y'));
        assertTrue(NameUtil.isNameChar('z'));
        assertTrue(NameUtil.isNameChar('A'));
        assertTrue(NameUtil.isNameChar('B'));
        assertTrue(NameUtil.isNameChar('C'));
        assertTrue(NameUtil.isNameChar('D'));
        assertTrue(NameUtil.isNameChar('E'));
        assertTrue(NameUtil.isNameChar('F'));
        assertTrue(NameUtil.isNameChar('G'));
        assertTrue(NameUtil.isNameChar('H'));
        assertTrue(NameUtil.isNameChar('I'));
        assertTrue(NameUtil.isNameChar('J'));
        assertTrue(NameUtil.isNameChar('K'));
        assertTrue(NameUtil.isNameChar('L'));
        assertTrue(NameUtil.isNameChar('M'));
        assertTrue(NameUtil.isNameChar('N'));
        assertTrue(NameUtil.isNameChar('O'));
        assertTrue(NameUtil.isNameChar('P'));
        assertTrue(NameUtil.isNameChar('Q'));
        assertTrue(NameUtil.isNameChar('R'));
        assertTrue(NameUtil.isNameChar('S'));
        assertTrue(NameUtil.isNameChar('T'));
        assertTrue(NameUtil.isNameChar('U'));
        assertTrue(NameUtil.isNameChar('V'));
        assertTrue(NameUtil.isNameChar('W'));
        assertTrue(NameUtil.isNameChar('X'));
        assertTrue(NameUtil.isNameChar('Y'));
        assertTrue(NameUtil.isNameChar('Z'));
        assertTrue(NameUtil.isNameChar('0'));
        assertTrue(NameUtil.isNameChar('1'));
        assertTrue(NameUtil.isNameChar('2'));
        assertTrue(NameUtil.isNameChar('3'));
        assertTrue(NameUtil.isNameChar('4'));
        assertTrue(NameUtil.isNameChar('5'));
        assertTrue(NameUtil.isNameChar('6'));
        assertTrue(NameUtil.isNameChar('7'));
        assertTrue(NameUtil.isNameChar('8'));
        assertTrue(NameUtil.isNameChar('9'));
        assertTrue(NameUtil.isNameChar('_'));
        assertFalse(NameUtil.isNameChar(' '));
        assertTrue(NameUtil.isNameChar('.')); //← 正しいアプリケーション名とみなします。
        assertFalse(NameUtil.isNameChar('-'));
        assertFalse(NameUtil.isNameChar('/'));
        assertFalse(NameUtil.isNameChar('~'));
        assertFalse(NameUtil.isNameChar((char) ('a' - 1)));
        assertFalse(NameUtil.isNameChar((char) ('z' + 1)));
        assertFalse(NameUtil.isNameChar((char) ('A' - 1)));
        assertFalse(NameUtil.isNameChar((char) ('Z' + 1)));
        assertFalse(NameUtil.isNameChar((char) ('0' - 1)));
        assertFalse(NameUtil.isNameChar((char) ('9' + 1)));
        assertFalse(NameUtil.isNameChar((char) ('_' - 1)));
        assertFalse(NameUtil.isNameChar((char) ('_' + 1)));
    }

    public void testIsValidName() throws Exception {
        assertTrue(NameUtil.isValidName("a"));
        assertTrue(NameUtil.isValidName("b"));
        assertTrue(NameUtil.isValidName("c"));
        assertTrue(NameUtil.isValidName("d"));
        assertTrue(NameUtil.isValidName("e"));
        assertTrue(NameUtil.isValidName("f"));
        assertTrue(NameUtil.isValidName("g"));
        assertTrue(NameUtil.isValidName("h"));
        assertTrue(NameUtil.isValidName("i"));
        assertTrue(NameUtil.isValidName("j"));
        assertTrue(NameUtil.isValidName("k"));
        assertTrue(NameUtil.isValidName("l"));
        assertTrue(NameUtil.isValidName("m"));
        assertTrue(NameUtil.isValidName("n"));
        assertTrue(NameUtil.isValidName("o"));
        assertTrue(NameUtil.isValidName("p"));
        assertTrue(NameUtil.isValidName("q"));
        assertTrue(NameUtil.isValidName("r"));
        assertTrue(NameUtil.isValidName("s"));
        assertTrue(NameUtil.isValidName("t"));
        assertTrue(NameUtil.isValidName("u"));
        assertTrue(NameUtil.isValidName("v"));
        assertTrue(NameUtil.isValidName("w"));
        assertTrue(NameUtil.isValidName("x"));
        assertTrue(NameUtil.isValidName("y"));
        assertTrue(NameUtil.isValidName("z"));
        assertTrue(NameUtil.isValidName("A"));
        assertTrue(NameUtil.isValidName("B"));
        assertTrue(NameUtil.isValidName("C"));
        assertTrue(NameUtil.isValidName("D"));
        assertTrue(NameUtil.isValidName("E"));
        assertTrue(NameUtil.isValidName("F"));
        assertTrue(NameUtil.isValidName("G"));
        assertTrue(NameUtil.isValidName("H"));
        assertTrue(NameUtil.isValidName("I"));
        assertTrue(NameUtil.isValidName("J"));
        assertTrue(NameUtil.isValidName("K"));
        assertTrue(NameUtil.isValidName("L"));
        assertTrue(NameUtil.isValidName("M"));
        assertTrue(NameUtil.isValidName("N"));
        assertTrue(NameUtil.isValidName("O"));
        assertTrue(NameUtil.isValidName("P"));
        assertTrue(NameUtil.isValidName("Q"));
        assertTrue(NameUtil.isValidName("R"));
        assertTrue(NameUtil.isValidName("S"));
        assertTrue(NameUtil.isValidName("T"));
        assertTrue(NameUtil.isValidName("U"));
        assertTrue(NameUtil.isValidName("V"));
        assertTrue(NameUtil.isValidName("W"));
        assertTrue(NameUtil.isValidName("X"));
        assertTrue(NameUtil.isValidName("Y"));
        assertTrue(NameUtil.isValidName("Z"));
        assertTrue(NameUtil.isValidName("0"));
        assertTrue(NameUtil.isValidName("1"));
        assertTrue(NameUtil.isValidName("2"));
        assertTrue(NameUtil.isValidName("3"));
        assertTrue(NameUtil.isValidName("4"));
        assertTrue(NameUtil.isValidName("5"));
        assertTrue(NameUtil.isValidName("6"));
        assertTrue(NameUtil.isValidName("7"));
        assertTrue(NameUtil.isValidName("8"));
        assertTrue(NameUtil.isValidName("9"));
        assertTrue(NameUtil.isValidName("_"));
        assertTrue(
            NameUtil.isValidName(
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"));
        assertFalse(NameUtil.isValidName(" "));
        assertTrue(NameUtil.isValidName("."));  //← 正しい名前とみなします。
        assertFalse(NameUtil.isValidName("-"));
        assertFalse(NameUtil.isValidName("/"));
        assertFalse(NameUtil.isValidName("~"));
        assertFalse(NameUtil.isValidName("" + (char) ('a' - 1)));
        assertFalse(NameUtil.isValidName("" + (char) ('z' + 1)));
        assertFalse(NameUtil.isValidName("" + (char) ('A' - 1)));
        assertFalse(NameUtil.isValidName("" + (char) ('Z' + 1)));
        assertFalse(NameUtil.isValidName("" + (char) ('0' - 1)));
        assertFalse(NameUtil.isValidName("" + (char) ('9' + 1)));
        assertFalse(NameUtil.isValidName("" + (char) ('_' - 1)));
        assertFalse(NameUtil.isValidName("" + (char) ('_' + 1)));
        assertTrue(NameUtil.isValidName("aaa.bbb"));  //← 正しい名前とみなします。
        assertFalse(NameUtil.isValidName(null));
        assertFalse(NameUtil.isValidName(""));
    }

    public void testIsValidPath() throws Exception {
        assertTrue(NameUtil.isValidPath("/"));
        assertTrue(NameUtil.isValidPath("/a"));
        assertTrue(NameUtil.isValidPath("/b"));
        assertTrue(NameUtil.isValidPath("/c"));
        assertTrue(NameUtil.isValidPath("/d"));
        assertTrue(NameUtil.isValidPath("/e"));
        assertTrue(NameUtil.isValidPath("/f"));
        assertTrue(NameUtil.isValidPath("/g"));
        assertTrue(NameUtil.isValidPath("/h"));
        assertTrue(NameUtil.isValidPath("/i"));
        assertTrue(NameUtil.isValidPath("/j"));
        assertTrue(NameUtil.isValidPath("/k"));
        assertTrue(NameUtil.isValidPath("/l"));
        assertTrue(NameUtil.isValidPath("/m"));
        assertTrue(NameUtil.isValidPath("/n"));
        assertTrue(NameUtil.isValidPath("/o"));
        assertTrue(NameUtil.isValidPath("/p"));
        assertTrue(NameUtil.isValidPath("/q"));
        assertTrue(NameUtil.isValidPath("/r"));
        assertTrue(NameUtil.isValidPath("/s"));
        assertTrue(NameUtil.isValidPath("/t"));
        assertTrue(NameUtil.isValidPath("/u"));
        assertTrue(NameUtil.isValidPath("/v"));
        assertTrue(NameUtil.isValidPath("/w"));
        assertTrue(NameUtil.isValidPath("/x"));
        assertTrue(NameUtil.isValidPath("/y"));
        assertTrue(NameUtil.isValidPath("/z"));
        assertTrue(NameUtil.isValidPath("/A"));
        assertTrue(NameUtil.isValidPath("/B"));
        assertTrue(NameUtil.isValidPath("/C"));
        assertTrue(NameUtil.isValidPath("/D"));
        assertTrue(NameUtil.isValidPath("/E"));
        assertTrue(NameUtil.isValidPath("/F"));
        assertTrue(NameUtil.isValidPath("/G"));
        assertTrue(NameUtil.isValidPath("/H"));
        assertTrue(NameUtil.isValidPath("/I"));
        assertTrue(NameUtil.isValidPath("/J"));
        assertTrue(NameUtil.isValidPath("/K"));
        assertTrue(NameUtil.isValidPath("/L"));
        assertTrue(NameUtil.isValidPath("/M"));
        assertTrue(NameUtil.isValidPath("/N"));
        assertTrue(NameUtil.isValidPath("/O"));
        assertTrue(NameUtil.isValidPath("/P"));
        assertTrue(NameUtil.isValidPath("/Q"));
        assertTrue(NameUtil.isValidPath("/R"));
        assertTrue(NameUtil.isValidPath("/S"));
        assertTrue(NameUtil.isValidPath("/T"));
        assertTrue(NameUtil.isValidPath("/U"));
        assertTrue(NameUtil.isValidPath("/V"));
        assertTrue(NameUtil.isValidPath("/W"));
        assertTrue(NameUtil.isValidPath("/X"));
        assertTrue(NameUtil.isValidPath("/Y"));
        assertTrue(NameUtil.isValidPath("/Z"));
        assertTrue(NameUtil.isValidPath("/0"));
        assertTrue(NameUtil.isValidPath("/1"));
        assertTrue(NameUtil.isValidPath("/2"));
        assertTrue(NameUtil.isValidPath("/3"));
        assertTrue(NameUtil.isValidPath("/4"));
        assertTrue(NameUtil.isValidPath("/5"));
        assertTrue(NameUtil.isValidPath("/6"));
        assertTrue(NameUtil.isValidPath("/7"));
        assertTrue(NameUtil.isValidPath("/8"));
        assertTrue(NameUtil.isValidPath("/9"));
        assertTrue(NameUtil.isValidPath("/_"));
        assertTrue(
            NameUtil.isValidPath(
                "/abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"));
        assertTrue(NameUtil.isValidPath("/abc"));
        assertTrue(NameUtil.isValidPath("/abc/def"));
        assertTrue(NameUtil.isValidPath("/abc/def/ghi"));
        assertFalse(NameUtil.isValidPath(" "));
        assertFalse(NameUtil.isValidPath("/ "));
        assertFalse(NameUtil.isValidPath("."));
        assertTrue(NameUtil.isValidPath("/.")); //←正しいパスとみなします。
        assertFalse(NameUtil.isValidPath("-"));
        assertFalse(NameUtil.isValidPath("/-"));
        assertFalse(NameUtil.isValidPath("~"));
        assertFalse(NameUtil.isValidPath("/~"));
        assertFalse(NameUtil.isValidPath("/abc/"));
        assertFalse(NameUtil.isValidPath("/abc/def/"));
        assertFalse(NameUtil.isValidPath("/abc/def/ghi/"));
        assertFalse(NameUtil.isValidPath("/abc//def"));
        assertFalse(NameUtil.isValidPath("/" + (char) ('a' - 1)));
        assertFalse(NameUtil.isValidPath("/" + (char) ('z' + 1)));
        assertFalse(NameUtil.isValidPath("/" + (char) ('A' - 1)));
        assertFalse(NameUtil.isValidPath("/" + (char) ('Z' + 1)));
        assertFalse(NameUtil.isValidPath("/" + (char) ('0' - 1)));
        assertFalse(NameUtil.isValidPath("/" + (char) ('9' + 1)));
        assertFalse(NameUtil.isValidPath("/" + (char) ('_' - 1)));
        assertFalse(NameUtil.isValidPath("/" + (char) ('_' + 1)));
        assertFalse(NameUtil.isValidPath(null));
        assertFalse(NameUtil.isValidPath(""));
    }
}
