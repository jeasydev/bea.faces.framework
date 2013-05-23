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
package org.primefaces.component.behavior.ajax;

import java.io.Serializable;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;

public class AjaxBehaviorListenerImpl implements AjaxBehaviorListener, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MethodExpression listener;
    private MethodExpression listenerWithArg;

    public AjaxBehaviorListenerImpl() {
    }

    public AjaxBehaviorListenerImpl(final MethodExpression listener, final MethodExpression listenerWithArg) {
        this.listener = listener;
        this.listenerWithArg = listenerWithArg;
    }

    @Override
    public void processAjaxBehavior(final AjaxBehaviorEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ELContext elContext = context.getELContext();

        try {
            listener.invoke(elContext, new Object[] {});
        } catch (final MethodNotFoundException mnfe) {
            processArgListener(context, elContext, event);
        } catch (final IllegalArgumentException iae) {
            processArgListener(context, elContext, event);
        }
    }

    private void processArgListener(final FacesContext context, final ELContext elContext, final AjaxBehaviorEvent event)
        throws AbortProcessingException {
        try {
            listenerWithArg.invoke(elContext, new Object[] { event });
        } catch (final MethodNotFoundException mnfe) {
            processCustomListener(context, elContext, event);
        } catch (final IllegalArgumentException e) {
            processCustomListener(context, elContext, event);
        }
    }

    private void processCustomListener(final FacesContext context,
                                       final ELContext elContext,
                                       final AjaxBehaviorEvent event) throws AbortProcessingException {
        final MethodExpression argListener = context.getApplication().getExpressionFactory()
            .createMethodExpression(elContext, listener.getExpressionString(), null, new Class[] { event.getClass() });

        argListener.invoke(elContext, new Object[] { event });
    }
}