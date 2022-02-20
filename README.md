# Recorder4j

A recorder capable of recording Java programs. These records contain changes of field value in the method block,
the method call tracking, and recording isolation when multi-threaded.

# demo

## Test class

```java
package com.aukocharlie.recorder4j;


public class ChainCase {

    public String str;

    public ChainCase(String str) {
        this.str = str;
    }

    public static void main(String[] args) {
        ChainCase chainCase = new ChainCase("0")
                .chain("2")
                .chain(new ChainCase("3").test(), new ChainCase("4").test())
                .chain("5");
        System.out.println(chainCase.str);
    }

    public ChainCase chain(String str) {
        this.str += str;
        return this;
    }

    public ChainCase chain(String str1, String str2) {
        this.str += str1 + str2;
        return this;
    }

    public String test() {
        return str + "test";
    }
}

```

## Run Recorder4j

```java
Recorder recorder=Recorder.builder()
        .main(ChainCase.class)
        .srcRelativeRootPath("src/test/java")
        .outPutReplace("com.aukocharlie.recorder4j.","")
        .build();
recorder.run();
```

## Output
```java
 ==== VM started ==== 
Ready to parse source code: com.aukocharlie.recorder4j.ChainCase
METHOD_ENTRY: line: 13  "thread=main", ChainCase.main(java.lang.String[]), main, -1:-1 -> -1:-1
    FIELD_MODIFIED: line: 13   static:  | local: args = []
    METHOD_ENTRY: line: 13  "thread=main", ChainCase.<init>(java.lang.String), new ChainCase("0"), new ChainCase("0")(13:31 -> 13:49)
        FIELD_MODIFIED: line: 8   static:  | local: str = "0"
        FIELD_MODIFIED: line: 10   static:  | local: str = "0"
    METHOD_EXIT: "thread=main", return=<void value>
    METHOD_ENTRY: line: 14  "thread=main", ChainCase.chain(java.lang.String), chain("2"), chain("2")(14:18 -> 14:28)
        FIELD_MODIFIED: line: 21   static:  | local: str = "2" | this: {str = "0"}
        FIELD_MODIFIED: line: 22   static:  | local: str = "2" | this: {str = "02"}
    METHOD_EXIT: "thread=main", return=instance of ChainCase(id=197)
    METHOD_ENTRY: line: 14  "thread=main", ChainCase.<init>(java.lang.String), new ChainCase("3"), new ChainCase("3")(15:24 -> 15:42)
        FIELD_MODIFIED: line: 8   static:  | local: str = "3"
        FIELD_MODIFIED: line: 10   static:  | local: str = "3"
    METHOD_EXIT: "thread=main", return=<void value>
    METHOD_ENTRY: line: 15  "thread=main", ChainCase.test(), test(), test()(15:43 -> 15:49)
    METHOD_EXIT: "thread=main", return="3test"
    METHOD_ENTRY: line: 15  "thread=main", ChainCase.<init>(java.lang.String), new ChainCase("4"), new ChainCase("4")(15:51 -> 15:69)
        FIELD_MODIFIED: line: 8   static:  | local: str = "4"
        FIELD_MODIFIED: line: 10   static:  | local: str = "4"
    METHOD_EXIT: "thread=main", return=<void value>
    METHOD_ENTRY: line: 15  "thread=main", ChainCase.test(), test(), test()(15:70 -> 15:76)
    METHOD_EXIT: "thread=main", return="4test"
    METHOD_ENTRY: line: 15  "thread=main", ChainCase.chain(java.lang.String, java.lang.String), chain(new ChainCase("3").test(), new ChainCase("4").test()), chain(new ChainCase("3").test(), new ChainCase("4").test())(15:18 -> 15:77)
        FIELD_MODIFIED: line: 26   static:  | local: str1 = "3test", str2 = "4test" | this: {str = "02"}
        FIELD_MODIFIED: line: 27   static:  | local: str1 = "3test", str2 = "4test" | this: {str = "023test4test"}
    METHOD_EXIT: "thread=main", return=instance of ChainCase(id=197)
    METHOD_ENTRY: line: 16  "thread=main", ChainCase.chain(java.lang.String), chain("5"), chain("5")(16:18 -> 16:28)
        FIELD_MODIFIED: line: 21   static:  | local: str = "5" | this: {str = "023test4test"}
        FIELD_MODIFIED: line: 22   static:  | local: str = "5" | this: {str = "023test4test5"}
    METHOD_EXIT: "thread=main", return=instance of ChainCase(id=197)
    FIELD_MODIFIED: line: 17   static:  | local: args = [], chainCase = {str = "023test4test5"}
METHOD_EXIT: "thread=main", return=<void value>
 ==== VM terminated ==== 
023test4test5
 ==== VM disconnected ====
```

## TODO
[x] method chaining
[] Lambda function
[] condition expression
[] if/else statement
[] loop statement
[] try/catch statement
[] exception
[] multi thread
[] refactor, use javaParser