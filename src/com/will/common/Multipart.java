package com.will.common;

import java.util.ArrayList;

public class Multipart {
	
	private ArrayList<byte[]> parts = new ArrayList<byte[]>();

    public Multipart a(String... strings) {
        for (String s : strings) {
            parts.add(s == null ? "".getBytes() : s.getBytes());
        }
        return this;
    }

    public Multipart a(byte[]... bytes) {
        for (byte[] b : bytes) {
            parts.add(b == null ? "".getBytes() : b);
        }
        return this;
    }

    public byte[] get(int i) {
        return i >= parts.size() ? null : parts.get(i);
    }

    public String getString(int i) {
        return i >= parts.size() ? null : new String(parts.get(i));
    }

    public byte[][] toArray(int fromIndex, int toIndex) {
        return parts.subList(fromIndex, toIndex).toArray(new byte[0][]);
    }

    public int size() {
        return parts.size();
    }

}
