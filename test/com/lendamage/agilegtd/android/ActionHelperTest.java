package com.lendamage.agilegtd.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

public class ActionHelperTest {
    
    @Test
    public void testRegexpHang() {
        Pattern sentence = Pattern.compile(
                "([\\w,][\\s]?)+[\\.\\?\\!]");
        Matcher m = sentence.matcher("Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk");
        assertFalse(m.find());
    }
    
    @Test
    public void testSentence() {
        assertEquals("Abc def", ActionHelper.getHeadFromBody("Abc def. Igk lmn."));
    }
    
    @Test
    public void testLongSentence() {
        assertEquals(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def", 
ActionHelper.getHeadFromBody(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def. Igk lmn."));
    }
    
    @Test
    public void testShortText() {
        assertEquals("Abc def igk", ActionHelper.getHeadFromBody("Abc def igk"));
    }
    
    @Test
    public void testLongText() {
        assertEquals(
"Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def", 
ActionHelper.getHeadFromBody(
"Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk"));
    }
    
    @Test
    public void testNewLine() {
        assertEquals("Abc def", ActionHelper.getHeadFromBody("Abc def\nIgk lmn."));
    }
    
    @Test
    public void testLongNewLine() {
        assertEquals(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def", 
ActionHelper.getHeadFromBody(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def\nIgk lmn."));
    }
    
    @Test
    public void testDotStart() {
        assertEquals("...Abc def", ActionHelper.getHeadFromBody("...Abc def"));
    }
    
    @Test
    @Ignore("Too extremal situation")
    public void testDotStart2() {
        assertEquals(". . .Abc def", ActionHelper.getHeadFromBody(". . .Abc def"));
    }
    
    @Test
    public void testNewLineStart() {
        assertEquals("Abc def", ActionHelper.getHeadFromBody("\n\nAbc def"));
    }
    
    @Test
    public void testLongNoSpaces() {
        assertEquals(
"AbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghij", 
ActionHelper.getHeadFromBody(
"AbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghij"));
    }
    
    @Test
    public void testSentenceWithExclamation() {
        assertEquals("Abc def!", ActionHelper.getHeadFromBody("Abc def! Igk lmn?"));
    }
    
    @Test
    public void testLongSentenceWithExclamation() {
        assertEquals(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def", 
ActionHelper.getHeadFromBody(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def! Igk lmn!"));
    }
    
    @Test
    public void testSentenceWithQuery() {
        assertEquals("Abc def?", ActionHelper.getHeadFromBody("Abc def? Igk lmn!"));
    }
    
    @Test
    public void testLongSentenceWithQuery() {
        assertEquals(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def", 
ActionHelper.getHeadFromBody(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def? Igk lmn!?"));
    }
    
    @Test
    public void testSentenceWithDots() {
        assertEquals(
                "Version 1.2.3", 
                ActionHelper.getHeadFromBody(
                "Version 1.2.3. Cool!"));
    }
    
    @Test
    public void testSentenceWithColons() {
        assertEquals(
                "Blabla: bla", 
                ActionHelper.getHeadFromBody(
                "Blabla: bla. Cool!"));
        assertEquals(
                "Blabla; bla", 
                ActionHelper.getHeadFromBody(
                "Blabla; bla. Cool!"));
    }

}
