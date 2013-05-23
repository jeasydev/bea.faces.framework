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
package org.primefaces.model;

import java.io.Serializable;

public class CroppedImage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String originalFilename;

    private byte[] bytes;

    private int left;

    private int top;

    private int width;

    private int height;

    public CroppedImage() {
        // No op
    }

    public CroppedImage(final String originalFilename,
                        final byte[] bytes,
                        final int left,
                        final int top,
                        final int width,
                        final int height) {
        this.originalFilename = originalFilename;
        this.bytes = bytes;
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getHeight() {
        return height;
    }

    public int getLeft() {
        return left;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public int getTop() {
        return top;
    }

    public int getWidth() {
        return width;
    }

    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public void setLeft(final int left) {
        this.left = left;
    }

    public void setOriginalFilename(final String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public void setTop(final int top) {
        this.top = top;
    }

    public void setWidth(final int width) {
        this.width = width;
    }
}
