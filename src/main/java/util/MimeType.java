package util;

import java.util.*;


public class MimeType {
    protected static final String WILDCARD_TYPE = "*";
    private static final BitSet TOKEN;

    static {
        // variable names refer to RFC 2616, section 2.2
        BitSet ctl = new BitSet(128);
        for (int i = 0; i <= 31; i++) {
            ctl.set(i);
        }
        ctl.set(127);

        BitSet separators = new BitSet(128);
        separators.set('(');
        separators.set(')');
        separators.set('<');
        separators.set('>');
        separators.set('@');
        separators.set(',');
        separators.set(';');
        separators.set(':');
        separators.set('\\');
        separators.set('\"');
        separators.set('/');
        separators.set('[');
        separators.set(']');
        separators.set('?');
        separators.set('=');
        separators.set('{');
        separators.set('}');
        separators.set(' ');
        separators.set('\t');

        TOKEN = new BitSet(128);
        TOKEN.set(0, 128);
        TOKEN.andNot(ctl);
        TOKEN.andNot(separators);
    }


    private final String type;
    private final String subtype;
    private volatile String toStringValue;

    public MimeType(String type) {
        this(type, WILDCARD_TYPE);
    }

    public MimeType(String type, String subtype) {
        Assert.hasLength(type, "'type' must not be empty");
        Assert.hasLength(subtype, "'subtype' must not be empty");
        checkToken(type);
        checkToken(subtype);
        this.type = type.toLowerCase(Locale.ENGLISH);
        this.subtype = subtype.toLowerCase(Locale.ENGLISH);
    }

    public MimeType(MimeType other) {
        this.type = other.type;
        this.subtype = other.subtype;
        this.toStringValue = other.toStringValue;
    }

    private void checkToken(String token) {
        for (int i = 0; i < token.length(); i++) {
            char ch = token.charAt(i);
            if (!TOKEN.get(ch)) {
                throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + token + "\"");
            }
        }
    }

    private boolean isQuotedString(String s) {
        if (s.length() < 2) {
            return false;
        }
        else {
            return ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")));
        }
    }

    protected String unquote(String s) {
        return (isQuotedString(s) ? s.substring(1, s.length() - 1) : s);
    }

    public boolean isWildcardType() {
        return WILDCARD_TYPE.equals(getType());
    }

    public boolean isWildcardSubtype() {
        String subtype = getSubtype();
        return (WILDCARD_TYPE.equals(subtype) || subtype.startsWith("*+"));
    }

    public String getType() {
        return this.type;
    }
    public String getSubtype() {
        return this.subtype;
    }

    @Override
    public String toString() {
        String value = this.toStringValue;
        if (value == null) {
            StringBuilder builder = new StringBuilder();
            appendTo(builder);
            value = builder.toString();
            this.toStringValue = value;
        }
        return value;
    }

    protected void appendTo(StringBuilder builder) {
        builder.append(this.type);
        builder.append('/');
        builder.append(this.subtype);
    }
}

