/*
 * Copyright 2009-2012 Prime Teknoloji.
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
 */
package org.primefaces.util;

public class ArrayUtils {

    public static String[] concat(final String[]... arrays) {
        int destSize = 0;
        for (final String[] array : arrays) {
            destSize += array.length;
        }
        final String[] dest = new String[destSize];
        int lastIndex = 0;
        for (final String[] array : arrays) {
            System.arraycopy(array, 0, dest, lastIndex, array.length);
            lastIndex += array.length;
        }

        return dest;
    }

    public static String[] concat(final String[] array1, final String[] array2) {
        final int length1 = array1.length;
        final int length2 = array2.length;
        final int length = length1 + length2;

        final String[] dest = new String[length];

        System.arraycopy(array1, 0, dest, 0, length1);
        System.arraycopy(array2, 0, dest, length1, length2);

        return dest;
    }

    public static boolean contains(final String[] array, final String searchedText) {

        if (array == null || array.length == 0) return false;

        for (final String element : array) {
            if (element.equalsIgnoreCase(searchedText)) return true;
        }

        return false;
    }

    private ArrayUtils() {
    }
}
