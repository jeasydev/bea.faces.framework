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
package org.primefaces.component.filedownload;

import java.io.IOException;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

public class FileDownloadTagHandler extends TagHandler {

    private final TagAttribute value;
    private final TagAttribute contentDisposition;

    public FileDownloadTagHandler(final TagConfig tagConfig) {
        super(tagConfig);
        value = getRequiredAttribute("value");
        contentDisposition = getAttribute("contentDisposition");
    }

    @Override
    public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException,
        FacesException, FaceletException, ELException {
        if (ComponentHandler.isNew(parent)) {
            final ValueExpression valueVE = value.getValueExpression(faceletContext, Object.class);
            ValueExpression contentDispositionVE = null;

            if (contentDisposition != null)
                contentDispositionVE = contentDisposition.getValueExpression(faceletContext, String.class);

            final ActionSource actionSource = (ActionSource) parent;
            actionSource.addActionListener(new FileDownloadActionListener(valueVE, contentDispositionVE));
        }
    }
}