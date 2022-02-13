package com.aukocharlie.recorder4j.util;

import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class TypeSolverUtils {

    public static CombinedTypeSolver generateTypeSolver(String sourcePath) throws IOException {
        CombinedTypeSolver solver = new CombinedTypeSolver();
        // 1. JavaParserTypeSolver
        solver.add(new JavaParserTypeSolver(sourcePath));

        // 2. ReflectionTypeSolver
        solver.add(new ReflectionTypeSolver());

        return solver;
    }

    static class FindFileVisitor extends SimpleFileVisitor<Path> {
        private List<String> fileNameList = new ArrayList<>();

        private String fileSuffix;

        public FindFileVisitor(String fileSuffix) {
            this.fileSuffix = fileSuffix;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (file.toString().endsWith(fileSuffix)) {
                fileNameList.add(file.toString());
            }
            return FileVisitResult.CONTINUE;
        }

        public List<String> getFileNameList() {
            return fileNameList;
        }
    }

}
