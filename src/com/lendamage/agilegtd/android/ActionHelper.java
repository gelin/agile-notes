package com.lendamage.agilegtd.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionHelper {

    /** Maximum length of the head */
    static final int MAX_HEAD_SIZE = 80;
    /** Regular expression to select the head */
    static final Pattern SENTENCE_PATTERN = Pattern.compile(
            "\\s*(([\\w+],?[\\s&&[^\\n]]*)+?[\\.\\?\\!\\n])\\s*", 
            Pattern.CASE_INSENSITIVE|Pattern.UNIX_LINES);
    /** Head group number in the regexp */
    static final int SENTENCE_GROUP = 1;
    
    public static String getHeadFromBody(String body) {
        assert(body != null);
        Matcher m = SENTENCE_PATTERN.matcher(body);
        String sentence;
        if (m.find()) {
            sentence = m.group(SENTENCE_GROUP).trim();
        } else {
            sentence = body.trim();
        }
        if (sentence.endsWith(".")) {
            sentence = sentence.substring(0, sentence.length() - 1);
        }
        if (sentence.length() > MAX_HEAD_SIZE) {
            int spacePos = sentence.lastIndexOf(' ', MAX_HEAD_SIZE);
            if (spacePos > 0) {
                return sentence.substring(0, spacePos);
            } else {
                return sentence.substring(0, MAX_HEAD_SIZE);
            }
        }
        return sentence;
    }
    
}