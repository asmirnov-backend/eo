/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2025 Objectionary.com
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

/**
 * Abstract exception.
 *
 * <p>The exception raised when something is not right inside
 * attributes.</p>
 *
 * @since 0.21
 */
public abstract class ExAbstract extends RuntimeException {

    /**
     * Serialization identifier.
     */
    private static final long serialVersionUID = 597749420437007615L;

    /**
     * Ctor.
     * @param cause Exception cause
     */
    public ExAbstract(final String cause) {
        super(cause);
    }

    /**
     * Ctor.
     * @param cause Exception cause
     * @param root Root cause exception
     */
    public ExAbstract(final String cause, final Throwable root) {
        super(cause, root);
    }

    /**
     * Ctor.
     * @param root Root cause exception
     */
    public ExAbstract(final Throwable root) {
        super(root);
    }
}
