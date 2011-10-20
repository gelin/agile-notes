/*
    Agile GTD. Flexible Android implementation of GTD.
    Copyright (C) 2011  Denis Nelubin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lendamage.agilegtd.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionHelper {

    /** Maximum length of the head */
    static final int MAX_HEAD_SIZE = 80;
    /** Regular expression to select the head */
    static final Pattern SENTENCE_PATTERN = Pattern.compile(
            "(([\\S][\\s&&[^\\n]]*)+?[\\.\\?\\!][\\s$])|(([\\S][\\s&&[^\\n]]*)+?\\n)",
            //"([\\S&&[^\\.\\?\\!]]+[\\s&&[^\\n]]*)+[\\.\\?\\!]",
            Pattern.CASE_INSENSITIVE|Pattern.UNIX_LINES);
    /** Head group number in the regexp */
    static final int SENTENCE_GROUP = 0;
    
    public static String getHeadFromBody(String body) {
        assert(body != null);
        Matcher m = SENTENCE_PATTERN.matcher(body);
        String sentence;
        if (m.find()) {
            sentence = m.group(SENTENCE_GROUP).trim();
        } else {
            sentence = body.trim();
        }
        //System.out.println(sentence);
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
    
    private ActionHelper() {
        //avoid instantiation
    }
    
}