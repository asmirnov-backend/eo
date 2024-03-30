/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2024 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.eolang;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Data}.
 *
 * @since 0.1
 */
final class DataTest {
    @Test
    void printsByteArray() {
        MatcherAssert.assertThat(
            new Data.ToPhi(new byte[] {(byte) 0x01, (byte) 0xf2}).toString(),
            Matchers.containsString("01-F2")
        );
    }

    @Test
    void printsEmptyByteArray() {
        MatcherAssert.assertThat(
            new Data.ToPhi(new byte[0]).toString(),
            Matchers.containsString(":-")
        );
    }

    @Test
    void printsString() {
        final String input = "Hello,\nдруг!";
        final byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        final StringBuilder expected = new StringBuilder(0);
        for (final byte elem : input.getBytes(StandardCharsets.UTF_8)) {
            if (expected.length() > 0) {
                expected.append('-');
            }
            expected.append(String.format("%02X", elem));
        }
        MatcherAssert.assertThat(
            String.format(
                "Expected input string was \"%s\" with byte representation \"%s\".",
                input,
                expected
            ),
            new Data.ToPhi(bytes).toString(),
            Matchers.containsString(expected.toString())
        );
    }

    @Test
    void getsVertex() {
        MatcherAssert.assertThat(
            new Dataized(new Data.ToPhi(1L).take(Attr.VERTEX)).take(Long.class),
            Matchers.greaterThan(0L)
        );
    }

    @Test
    void comparesVertex() {
        MatcherAssert.assertThat(
            new Dataized(new Data.ToPhi(42L).take(Attr.VERTEX)).take(Long.class),
            Matchers.not(
                Matchers.equalTo(
                    new Dataized(new Data.ToPhi(42L).take(Attr.VERTEX)).take(Long.class)
                )
            )
        );
    }

    @Test
    void comparesTwoDatas() {
        MatcherAssert.assertThat(
            new Data.ToPhi(1L),
            Matchers.not(Matchers.equalTo(new Data.ToPhi(1L)))
        );
        MatcherAssert.assertThat(
            new Data.ToPhi("Welcome"),
            Matchers.not(Matchers.equalTo(new Data.ToPhi("Welcome")))
        );
        MatcherAssert.assertThat(
            new Data.ToPhi(2.18d),
            Matchers.not(Matchers.equalTo(new Data.ToPhi(2.18d)))
        );
        MatcherAssert.assertThat(
            new Data.ToPhi(new byte[] {(byte) 0x00, (byte) 0x1f}),
            Matchers.not(Matchers.equalTo(new Data.ToPhi(new byte[] {(byte) 0x00, (byte) 0x1f})))
        );
    }
}
