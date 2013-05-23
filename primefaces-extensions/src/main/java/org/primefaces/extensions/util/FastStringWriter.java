/*
 * Copyright 2011 PrimeFaces Extensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */

package org.primefaces.extensions.util;

import java.io.IOException;
import java.io.Writer;

/**
 * Writer based on {@link StringBuilder}.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class FastStringWriter extends Writer {

    protected StringBuilder builder;

    public FastStringWriter() {
        builder = new StringBuilder();
    }

    public FastStringWriter(final int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }

        builder = new StringBuilder(initialCapacity);
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    public StringBuilder getBuffer() {
        return builder;
    }

    public void reset() {
        builder.setLength(0);
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }

        builder.append(cbuf, off, len);
    }

    @Override
    public void write(final String str) {
        builder.append(str);
    }

    @Override
    public void write(final String str, final int off, final int len) {
        builder.append(str.substring(off, off + len));
    }
}
