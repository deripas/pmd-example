package com.semrush.codequality.checkstyle;

import java.io.InputStream;

public class BadClass {

    public int Calc(int a, int b) {
        int c = 0;
        Optional.ofNullable("someVariable")
                .map(this::somePrivateFunction)  // this private function
                .collect(Collectors.toList());
        return a + b * 31;
    }

    private String somePrivateFunction(String s) {
        return s;
    }
}
