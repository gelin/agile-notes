package com.lendamage.agilegtd.android;

import android.test.AndroidTestCase;

public class ActionHelperTest extends AndroidTestCase {
    
    public void testSentence() {
        assertEquals("Abc def", ActionHelper.getHeadFromBody("Abc def. Igk lmn."));
    }
    
    public void testLongSentence() {
        assertEquals(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def", 
ActionHelper.getHeadFromBody(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def. Igk lmn."));
    }
    
    public void testShortText() {
        assertEquals("Abc def igk", ActionHelper.getHeadFromBody("Abc def igk"));
    }
    
    public void testLongText() {
        assertEquals(
"Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def", 
ActionHelper.getHeadFromBody(
"Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk Abc def igk"));
    }
    
    public void testNewLine() {
        assertEquals("Abc def", ActionHelper.getHeadFromBody("Abc def\nIgk lmn."));
    }
    
    public void testLongNewLine() {
        assertEquals(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def", 
ActionHelper.getHeadFromBody(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def\nIgk lmn."));
    }
    
    public void testDotStart() {
        assertEquals("...Abc def", ActionHelper.getHeadFromBody("...Abc def"));
    }
    
    public void testNewLineStart() {
        assertEquals("Abc def", ActionHelper.getHeadFromBody("\n\nAbc def"));
    }
    
    public void testLongNoSpaces() {
        assertEquals(
"AbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghij", 
ActionHelper.getHeadFromBody(
"AbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghij"));
    }
    
    public void testSentenceWithExclamation() {
        assertEquals("Abc def!", ActionHelper.getHeadFromBody("Abc def! Igk lmn?"));
    }
    
    public void testLongSentenceWithExclamation() {
        assertEquals(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def", 
ActionHelper.getHeadFromBody(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def! Igk lmn!"));
    }
    
    public void testSentenceWithQuery() {
        assertEquals("Abc def?", ActionHelper.getHeadFromBody("Abc def? Igk lmn!"));
    }
    
    public void testLongSentenceWithQuery() {
        assertEquals(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def", 
ActionHelper.getHeadFromBody(
"Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def Abc def? Igk lmn!?"));
    }
    
    public void testSentenceWithDots() {
        assertEquals(
                "Version 1.2.3", 
                ActionHelper.getHeadFromBody(
                "Version 1.2.3. Cool!"));
    }

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
