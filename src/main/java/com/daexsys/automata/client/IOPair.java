package com.daexsys.automata.client;

import java.io.InputStream;
import java.io.OutputStream;

public class IOPair {

    private InputStream inputStream;
    private OutputStream outputStream;

    public IOPair(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
