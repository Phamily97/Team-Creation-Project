package com.teamb13.teamcreater.module;

import java.util.HashSet;
import java.util.Objects;

public class Header {

    public HashSet<String> header;

    public Header() {
        header = new HashSet<>();
    }

    public void addHeader(String headerStr) {
        header.add(headerStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header1 = (Header) o;
        return Objects.equals(header, header1.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }

    @Override
    public String toString() {
        return "Header{" +
                "header=" + header +
                '}';
    }
}
