package com.app.global.config.xss;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

public class HtmlCharacterEscapes extends CharacterEscapes {

    private final int[] asciiEscapes;

    public HtmlCharacterEscapes() {
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        return new SerializedString(escapeHtml4(Character.toString((char) ch)));
    }

    private static String escapeHtml4(String input) {
        final StringBuilder escaped = new StringBuilder();
        final int length = input.length();
        for (int offset = 0; offset < length; ) {
            final int codePoint = input.codePointAt(offset);
            offset += Character.charCount(codePoint);
            switch (codePoint) {
                case '<':
                    escaped.append("&lt;");
                    break;
                case '>':
                    escaped.append("&gt;");
                    break;
                case '&':
                    escaped.append("&amp;");
                    break;
                case '"':
                    escaped.append("&quot;");
                    break;
                case '\'':
                    escaped.append("&#39;");
                    break;
                case '/':
                    escaped.append("&#47;");
                    break;
                default:
                    if (codePoint > 127) {
                        escaped.append("&#");
                        escaped.append(Integer.toString(codePoint, 10));
                        escaped.append(";");
                    } else {
                        escaped.append(Character.toChars(codePoint));
                    }
                    break;
            }
        }
        return escaped.toString();
    }
}

