package com.aukocharlie.recorder4j.source;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 */
public class MethodMetadata {

    private final String className;
    private final String methodName;
    private List<String> parameterTypeList;

    public MethodMetadata(String className, String methodName, List<String> parameterList) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypeList = parameterList;
        if (this.parameterTypeList == null) {
            this.parameterTypeList = new ArrayList<>();
        }
    }

    public boolean equals(String className, String methodName, List<String> parameterTypeList) {
        return className != null && className.equals(this.className)
                && methodName != null && methodName.equals(this.methodName)
                && parameterTypeList != null && parameterTypeList.equals(this.parameterTypeList);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodMetadata) {
            MethodMetadata method = (MethodMetadata) obj;
            return this.equals(method.className, method.methodName, method.parameterTypeList);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (this.className == null ? 0 : this.className.hashCode());
        hashCode = 31 * hashCode + (this.methodName == null ? 0 : this.methodName.hashCode());
        hashCode = 31 * hashCode + (this.parameterTypeList == null ? 0 : this.parameterTypeList.hashCode());
        return hashCode;
    }

    @Override
    public String toString() {
        return String.format("%s.%s(%s)", className, methodName, String.join(", ", parameterTypeList));
    }

}
