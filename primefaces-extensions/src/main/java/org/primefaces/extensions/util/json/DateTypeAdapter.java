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

package org.primefaces.extensions.util.json;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.DateUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Gson Serializer / Deserializer for {@link Date}.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.6.2
 */
public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private Object timeZone;

    public DateTypeAdapter() {
    }

    public DateTypeAdapter(final Object timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
        throws JsonParseException {
        if (timeZone == null) {
            return new Date(json.getAsJsonPrimitive().getAsLong());
        } else {
            final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            final TimeZone tz = ComponentUtils.resolveTimeZone(timeZone);

            return DateUtils.toUtcDate(calendar, tz, json.getAsJsonPrimitive().getAsLong());
        }
    }

    public Object getTimeZone() {
        return timeZone;
    }

    @Override
    public JsonElement serialize(final Date src, final Type typeOfSrc, final JsonSerializationContext context) {
        if (timeZone == null) {
            return new JsonPrimitive(src.getTime());
        } else {
            final TimeZone tz = ComponentUtils.resolveTimeZone(timeZone);
            final Calendar calendar = Calendar.getInstance(tz);

            return new JsonPrimitive(DateUtils.toLocalDate(calendar, tz, src));
        }
    }

    public void setTimeZone(final Object timeZone) {
        this.timeZone = timeZone;
    }
}
