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

import java.util.function.Function;

/**
 * Attribute that constructs object lazily.
 *
 * <p>The attribute depends on context (argument of lambda expression).</p>
 *
 * @since 0.1
 */
public final class AtComposite implements Attr {
    /**
     * The argument of the expression.
     */
    private final Phi argument;

    /**
     * The expression itself.
     */
    private final Function<Phi, Phi> expr;

    /**
     * Ctor.
     * @param obj The \rho
     * @param exp The expression
     */
    public AtComposite(final Phi obj, final Function<Phi, Phi> exp) {
        this.argument = obj;
        this.expr = exp;
    }

    @Override
    public Attr copy(final Phi self) {
        return new AtComposite(self, this.expr);
    }

    @Override
    public Phi get() {
        return this.expr.apply(this.argument);
    }

    @Override
    public void put(final Phi phi) {
        throw new ExReadOnly(
            "Can't overwrite lambda expression"
        );
    }
}
