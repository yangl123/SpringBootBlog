package com.yangle.util;

import sun.misc.BASE64Encoder;

public class Base64Test {
    public static void main(String[] args) {
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = new BASE64Encoder().encode("111111".getBytes());
        System.out.println(encode);
    }
}
