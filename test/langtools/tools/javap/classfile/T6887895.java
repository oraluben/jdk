/*
 * Copyright (c) 2009, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 6887895
 * @summary test getting constantpool elements' basename through asInternalName() API
 */

import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.*;
import java.lang.classfile.*;
import java.lang.classfile.constantpool.*;

public class T6887895 {
    public static void main(String[] args) throws Exception {
        new T6887895().run();
    }

    void run() throws Exception {
        Set<String> found = new TreeSet<String>();

        ClassModel cm = getClassFile("T6887895$Test.class");
        ConstantPool cp = cm.constantPool();
        for (PoolEntry pe : cp) {
            if (pe instanceof ClassEntry ce) {
                String name = ce.asInternalName();
                System.out.println("found: " + name);
                if (ce.asSymbol().isClassOrInterface())
                    found.add(name);
            }
        }

        String[] expectNames = {
                "java/lang/Object",
                "java/lang/String",
                "T6887895",
                "T6887895$Test",
                "java/util/Objects",
        };

        Set<String> expect = new TreeSet<String>(Arrays.asList(expectNames));
        if (!found.equals(expect)) {
            System.err.println("found: " + found);
            System.err.println("expect: " + expect);
            throw new Exception("unexpected values found");
        }
    }

    ClassModel getClassFile(String name) throws IOException, URISyntaxException {
        URL rsc = getClass().getResource(name);
        return ClassFile.of().parse(Paths.get(rsc.toURI()));
    }

    class Test {
        void m() {
            boolean[] az = new boolean[0];
            boolean[][] aaz = new boolean[0][];
            boolean[][][] aaaz = new boolean[0][][];

            byte[] ab = new byte[0];
            byte[][] aab = new byte[0][];
            byte[][][] aaab = new byte[0][][];

            char[] ac = new char[0];
            char[][] aac = new char[0][];
            char[][][] aaac = new char[0][][];

            double[] ad = new double[0];
            double[][] aad = new double[0][];
            double[][][] aaad = new double[0][][];

            float[] af = new float[0];
            float[][] aaf = new float[0][];
            float[][][] aaaf = new float[0][][];

            int[] ai = new int[0];
            int[][] aai = new int[0][];
            int[][][] aaai = new int[0][][];

            long[] al = new long[0];
            long[][] aal = new long[0][];
            long[][][] aaal = new long[0][][];

            short[] as = new short[0];
            short[][] aas = new short[0][];
            short[][][] aaas = new short[0][][];

            String[] aS = new String[0];
            String[][] aaS = new String[0][];
            String[][][] aaaS = new String[0][][];
        }
    }
}

