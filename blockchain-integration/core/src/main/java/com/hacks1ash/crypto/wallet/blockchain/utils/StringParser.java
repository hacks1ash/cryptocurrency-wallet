package com.hacks1ash.crypto.wallet.blockchain.utils;

public class StringParser {

    private String string;
    int index;
    
    public int length(){
    	return string.length()-index;
    }

    public StringParser(String string) {
        this.string = string;
        index = 0;
    }

    public void forward(int chars) {
    	index += chars;
    }

    public char poll() {
        char c = string.charAt(index);
        forward(1);
        return c;
    }

    public String poll(int length) {
        String str = string.substring(index, length+index);
        forward(length);
        return str;
    }
    
    private void commit(){
    	string = string.substring(index);
    	index = 0;
    }

    public String pollBeforeSkipDelim(String s) {
    	commit();
        int i = string.indexOf(s);
        if (i == -1)
            throw new RuntimeException("\"" + s + "\" not found in \"" + string + "\"");
        String rv = string.substring(0, i);
        forward(i + s.length());
        return rv;
    }

    public char peek() {
        return string.charAt(index);
    }

    public String peek(int length) {
        return string.substring(index, length+index);
    }

    public String trim() {
    	commit();
        return string = string.trim();
    }
    
    public char charAt(int pos) {
		return string.charAt(pos+index);
	}

    public boolean isEmpty() {
    	return (string.length()<=index);
    }

    @Override
    public String toString() {
    	commit();
        return string;
    }

}