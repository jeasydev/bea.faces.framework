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

package org.primefaces.extensions.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Utils class for tag handlers.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5.1
 */
public class TagUtils {

    public static Collection<List<ClientBehavior>> getClientBehaviors(final FaceletContext context,
                                                                      final TagAttribute event,
                                                                      final ClientBehaviorHolder clientBehaviorHolder) {
        final Map<String, List<ClientBehavior>> mapBehaviors = clientBehaviorHolder.getClientBehaviors();
        if (mapBehaviors == null || mapBehaviors.isEmpty()) {
            return null;
        }

        final String events = (event != null ? event.getValue(context) : null);
        final String[] arrEvents = (events != null ? events.split("[\\s,]+") : null);
        if (arrEvents == null || arrEvents.length < 1) {
            return mapBehaviors.values();
        }

        final Collection<List<ClientBehavior>> behaviors = new ArrayList<List<ClientBehavior>>();

        final Set<String> keys = mapBehaviors.keySet();
        for (final String key : keys) {
            if (ArrayUtils.contains(arrEvents, key)) {
                behaviors.add(mapBehaviors.get(key));
            }
        }

        return behaviors;
    }
}
