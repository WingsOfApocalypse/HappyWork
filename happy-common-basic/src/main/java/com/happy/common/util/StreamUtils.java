package com.happy.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * IO流工具类
 *
 * Created by Derek on 16/9/12.
 */
public class StreamUtils {

    public static void closeStream(Closeable stream){
        if(stream!=null){
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStream(OutputStream stream){
        if(stream!=null){
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
