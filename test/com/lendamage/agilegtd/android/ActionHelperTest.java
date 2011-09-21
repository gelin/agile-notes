package com.lendamage.agilegtd.android;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ActionHelperTest {
    
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
    public void testDotStart2() {
        assertEquals(". . .Abc def", ActionHelper.getHeadFromBody(". . .Abc def"));
    }
    
    @Test
    public void testNewLineStart() {
        assertEquals("Abc def", ActionHelper.getHeadFromBody("\n\nAbc def"));
    }
    
    @Test
    public void longNoSpaces() {
        assertEquals(
"AbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghij", 
ActionHelper.getHeadFromBody(
"AbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghij"));
    }

}
