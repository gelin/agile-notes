package com.lendamage.agilegtd.android;

public class ActionHelper {

    static int MAX_HEAD_SIZE = 80;
    
    public static String getHeadFromBody(String body) {
        assert(body != null);
        String trimBody = body.trim();
        int pointPos = trimBody.indexOf('.');
        
        //avoiding dots at the beginning
        while (pointPos >= 0) {
            if (trimBody.substring(0, pointPos).trim().length() > 0) {
                break;
            }
            pointPos = trimBody.indexOf('.', pointPos);
        }
        
        int newLinePos = trimBody.indexOf('\n');
        int pos = (pointPos >= 0 && newLinePos >= 0) ? Math.min(pointPos, newLinePos) : 
            Math.max(pointPos, newLinePos);
        
        if (pos < 0 && trimBody.length() <= MAX_HEAD_SIZE) {
            return trimBody;
        }
        if (pos > 0 && pos <= MAX_HEAD_SIZE) {
            return trimBody.substring(0, pos);
        }
        int spacePos = trimBody.lastIndexOf(" ", MAX_HEAD_SIZE);
        if (spacePos > 0) {
            return trimBody.substring(0, spacePos).trim();
        }
        
        return trimBody.substring(0, MAX_HEAD_SIZE);
    }
    
}
