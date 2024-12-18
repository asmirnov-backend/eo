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
package org.eolang.parser;

import java.util.Collections;

final class UnderlinedMessage {

    private final String origin;
    private final int from;
    private final int length;

    UnderlinedMessage(final String origin, final int from, final int length) {
        this.origin = origin;
        this.from = from;
        this.length = length;
    }

    String formatted() {
        return String.format(
            "%s\n%s",
            this.origin,
            this.underline()
        );
    }

    private String underline() {
        final String result;
        if (this.origin.isEmpty() || this.length <= 0 || this.from >= this.origin.length()) {
            result = "";
        } else if (this.from < 0) {
            result = this.repeat("^", this.origin.length());
        } else {
            result = String.format(
                "%s%s",
                this.repeat(" ", this.from),
                this.repeat("^", Math.min(this.length, this.origin.length()))
            );
        }
        return result;
    }

    private String repeat(final String symbol, final int n) {
        return String.join("", Collections.nCopies(n, symbol));
    }
}
