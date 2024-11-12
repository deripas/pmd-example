# pmd usage exampele
PMD 7.7.0

Try [check code](src/test/resources/BadClass.java) (for example [PmdSmokeTest](src/test/java/com/github/deripas/pmd/PmdSmokeTest.java)) with [pmd configuration](src/test/resources/pmd-example.xml)

```java
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

```
Founded issues:
1. **UnnecessaryImport** Unused import 'java.io.InputStream' - OK
1. **MethodNamingConventions** The instance method name 'Calc' doesn't match '[a-z][a-zA-Z0-9]*' - OK
1. **UnusedLocalVariable** Avoid unused local variables such as 'c'. - OK
```
