/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.renderkit.layout;

import org.primefaces.extensions.model.layout.LayoutOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Singleton instance of Gson to convert layout options.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class GsonLayoutOptions {

    private static final GsonLayoutOptions INSTANCE = new GsonLayoutOptions();

    public static Gson getGson() {
        return GsonLayoutOptions.INSTANCE.gson;
    }

    private final Gson gson;

    private GsonLayoutOptions() {
        final GsonBuilder gsonBilder = new GsonBuilder();
        gsonBilder.registerTypeAdapter(LayoutOptions.class, new LayoutOptionsSerializer());
        gson = gsonBilder.create();
    }
}