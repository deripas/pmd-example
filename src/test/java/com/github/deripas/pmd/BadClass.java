package com.github.deripas.pmd;

import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Collectors;

public class BadClass {

    public int Calc(int a, int b) {
        int c = 0;
        Optional.ofNullable("someVariable")
            .map(this::somePrivateFunction)  // this private function
            .stream()
            .collect(Collectors.toList());
        return a + b * 31;
    }

    private String somePrivateFunction(String s) {
        return s;
    }
}
