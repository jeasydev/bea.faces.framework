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

package org.primefaces.extensions.application;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.util.VersionProvider;

/**
 * {@link SystemEventListener} which displays the PrimeFaces Extensions version
 * on startup.
 * 
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class PostConstructApplicationEventListener implements SystemEventListener {

    private static final Logger LOGGER = Logger.getLogger(PostConstructApplicationEventListener.class.getName());

    @Override
    public boolean isListenerForSource(final Object source) {
        return true;
    }

    @Override
    public void processEvent(final SystemEvent event) {
        if (StringUtils.isNotBlank(VersionProvider.getVersion())) {
            PostConstructApplicationEventListener.LOGGER.log(Level.INFO, "Running on PrimeFaces Extensions {0}",
                                                             VersionProvider.getVersion());
        }
    }
}
