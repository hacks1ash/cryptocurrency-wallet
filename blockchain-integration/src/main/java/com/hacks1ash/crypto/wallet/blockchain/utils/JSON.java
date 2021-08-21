package com.hacks1ash.crypto.wallet.blockchain.utils;

import java.util.Date;
import java.util.Map;

public class JSON {

    @SuppressWarnings("rawtypes")
    public static String stringify(Object o) {
        if (o == null)
            return "null";
        if ((o instanceof Number) || (o instanceof Boolean))
            return String.valueOf(o);
        if (o instanceof Date)
            return "new Date("+((Date)o).getTime()+")";
        if (o instanceof Map)
            return stringify((Map)o);
        if (o instanceof Iterable)
            return stringify((Iterable)o);
        if (o instanceof Object[])
            return stringify((Object[])o);
        return stringify(String.valueOf(o));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String stringify(Map m) {
        StringBuilder b = new StringBuilder();
        b.append('{');
        boolean first = true;
        for (Map.Entry e : ((Map<Object, Object>)m).entrySet()) {
            if (first)
                first = false;
            else
                b.append(",");
            b.append(stringify(e.getKey().toString()));
            b.append(':');
            b.append(stringify(e.getValue()));
            
        }
        b.append('}');
        return b.toString();
    }

    @SuppressWarnings("rawtypes")
    public static String stringify(Iterable c) {
        StringBuilder b = new StringBuilder();
        b.append('[');
        boolean first = true;
        for (Object o : c) {
            if (first)
                first = false;
            else
                b.append(",");
            b.append(stringify(o));
        }
        b.append(']');
        return b.toString();
    }
    
    public static String stringify(Object[] c) {
        StringBuilder b = new StringBuilder();
        b.append('[');
        boolean first = true;
        for (Object o : c) {
            if (first)
                first = false;
            else
                b.append(",");
            b.append(stringify(o));
        }
        b.append(']');
        return b.toString();
    }
    
    public static String stringify(String s) {
        StringBuilder b = new StringBuilder(s.length() + 2);
        b.append('"');
        for(; !s.isEmpty(); s = s.substring(1)) {
            char c = s.charAt(0);
            switch (c) {
                case '\t':
                    b.append("\\t");
                    break;
                case '\r':
                    b.append("\\r");
                    break;
                case '\n':
                    b.append("\\n");
                    break;
                case '\f':
                    b.append("\\f");
                    break;
                case '\b':
                    b.append("\\b");
                    break;
                case '"':
                case '\\':
                    b.append("\\");
                    b.append(c);
                    break;
                default:
                    b.append(c);
            }
        }
        b.append('"');
        return b.toString();
    }
    
    public static Object parse(String s) {
        return CrippledJavaScriptParser.parseJSExpr(s);
    }

}