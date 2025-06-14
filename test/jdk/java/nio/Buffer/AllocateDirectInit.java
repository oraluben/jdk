/*
 * Copyright (c) 2001, 2025, Oracle and/or its affiliates. All rights reserved.
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

/**
 * @test
 * @bug 4490253 6535542 8357959
 * @key randomness
 * @library /test/lib
 * @build jdk.test.lib.RandomFactory
 * @summary Verify that newly allocated direct buffers are initialized.
 * @run main/othervm AllocateDirectInit
 */

import java.nio.ByteBuffer;
import java.util.Random;

import jdk.test.lib.RandomFactory;

public class AllocateDirectInit {
    private static final int MAX_BIN_LIMIT = 16 * 1024 * 1024;
    private static final int MAX_DEC_LIMIT = 10 * 1000 * 1000;
    private static final int TRIES_PER_LIMIT = 1024;

    private static final Random RND = RandomFactory.getRandom();

    public static void main(String [] args){
        // Try power of two limits
        for (int limit = 1; limit < MAX_BIN_LIMIT; limit *= 2) {
            check(ByteBuffer.allocateDirect(limit - 1));
            check(ByteBuffer.allocateDirect(limit));
            check(ByteBuffer.allocateDirect(limit + 1));
        }

        // Try power of ten limits
        for (int limit = 1; limit < MAX_DEC_LIMIT; limit *= 10) {
            check(ByteBuffer.allocateDirect(limit - 1));
            check(ByteBuffer.allocateDirect(limit));
            check(ByteBuffer.allocateDirect(limit + 1));
        }

        // Try random sizes within power of two limits
        for (int limit = 1; limit < MAX_BIN_LIMIT; limit *= 2) {
            for (int t = 0; t < TRIES_PER_LIMIT; t++) {
                check(ByteBuffer.allocateDirect(RND.nextInt(limit)));
            }
        }
    }

    private static void check(ByteBuffer bb) {
        while (bb.hasRemaining()) {
            if (bb.get() != 0) {
                int mismatchPos = bb.position();
                System.out.print("byte [");
                for (bb.position(0); bb.position() < bb.limit(); ) {
                    System.out.print(" " + Integer.toHexString(bb.get() & 0xff));
                }
                System.out.println(" ]");
                throw new RuntimeException("uninitialized buffer, position = " + mismatchPos);
            }
        }
    }
}
