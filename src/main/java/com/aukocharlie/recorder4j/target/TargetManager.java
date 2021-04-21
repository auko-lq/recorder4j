package com.aukocharlie.recorder4j.target;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.*;
import java.util.stream.Collectors;

/**
 * @author auko
 */
public class TargetManager {

    // package/class/method 均只是 record 方法调用，field 才会 record 字段赋值
    private List<String> recordPackages = new ArrayList<>();
    private List<Class<?>> recordClasses = new ArrayList<>();
    private List<Method> recordMethods = new ArrayList<>();
    private List<Field> recordFields = new ArrayList<>();

    @Getter
    private String[] excludeClasses = {"java.*", "javax.*", "sun.*", "com.sun.*", "jdk.internal.*"};


    public void scanPackage(String[] packages) {
        // TODO: Scanning package to recognize annotation
    }

    public void checkTarget() {

    }

    public boolean shouldRecordFields() {
        return recordFields.size() > 0;
    }

    public void addRecordPackage(String pack) {
        this.recordPackages.add(pack);
    }

    public void setRecordPackages(String[] packages) {
        this.recordPackages = Arrays.stream(packages).collect(Collectors.toList());
    }

    public void addRecordClass(Class<?> clazz) {
        this.recordClasses.add(clazz);
    }

    public void setRecordClasses(Class<?>[] classes) {
        this.recordClasses = Arrays.stream(classes).collect(Collectors.toList());
    }

    public void addRecordMethod(Method method) {
        this.recordMethods.add(method);
    }

    public void setRecordMethods(Method[] methods) {
        this.recordMethods = Arrays.stream(methods).collect(Collectors.toList());
    }

    public void addRecordField(Field field) {
        this.recordFields.add(field);
    }

    public void setRecordFields(Field[] fields) {
        this.recordFields = Arrays.stream(fields).collect(Collectors.toList());
    }
}
