package kr.io.wolverine.webflux.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatUtils {
    /*
    {

    }
     */
    public static String extractJsonString(String content) {
        int startIndex = content.indexOf('{');
        int entIndex = content.lastIndexOf('}');
        if (startIndex != -1 && entIndex != -1 && startIndex < entIndex) {
            return content.substring(startIndex, entIndex + 1);
        }
        log.error("extractJsonString error. content: " + content);
        return "";
    }
}
