# pmd usage exampele
PMD 7.7.0

Try [check code](src/test/resources/BadClass.java) (for example [PmdSmokeTest](src/test/java/com/github/deripas/pmd/PmdSmokeTest.java)) with [pmd configuration](src/test/resources/pmd-example.xml)

```java
package checkstyle;

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
```
Founded issues:
1. **UnnecessaryImport** Unused import 'java.io.InputStream' - OK
1. **MethodNamingConventions** The instance method name 'Calc' doesn't match '[a-z][a-zA-Z0-9]*' - OK
1. **UnusedLocalVariable** Avoid unused local variables such as 'c'. - OK
1. **UnusedPrivateMethod** Avoid unused private methods such as 'somePrivateFunction(String)' - FALSE POSITIVE
```
        assertThat(violations, has("UnnecessaryImport", 3));
        assertThat(violations, has("MethodNamingConventions", 7));
        assertThat(violations, has("UnusedLocalVariable", 8));
        assertThat(violations, has("UnusedPrivateMethod", 15));
```
UnusedPrivateMethod - unexpected issue because the method is used.
