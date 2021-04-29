package com.aukocharlie.recorder4j.result;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author auko
 */
public class StreamRedirectThread extends Thread {

    private final Reader in;
    private final Writer out;

    private static final int BUFFER_SIZE = 2048;

    /**
     * Set up for copy.
     *
     * @param name Name of the thread
     * @param in   Stream to copy from
     * @param out  Stream to copy to
     */
    StreamRedirectThread(String name, InputStream in, OutputStream out) {
        super(name);
        this.in = new InputStreamReader(in, Charset.forName("gbk"));
        this.out = new OutputStreamWriter(out);
        setPriority(Thread.MAX_PRIORITY - 1);
    }

    @Override
    public void run() {
        try {
            char[] buf = new char[BUFFER_SIZE];
            int count;
            while ((count = in.read(buf, 0, BUFFER_SIZE)) >= 0) {
                out.write(buf, 0, count);
            }
            out.flush();
        } catch (IOException e) {
            throw new RecorderRuntimeException("Stream redirect exception: " + e);
        }
    }

}
