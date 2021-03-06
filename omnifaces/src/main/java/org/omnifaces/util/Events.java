/*
 * Copyright 2012 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.util;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import org.omnifaces.eventlistener.CallbackPhaseListener;
import org.omnifaces.eventlistener.DefaultPhaseListener;
import org.omnifaces.eventlistener.DefaultViewEventListener;

/**
 * Collection of utility methods for the JSF API with respect to working with
 * Faces events.
 * 
 * @author Arjan Tijms
 */
public final class Events {

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Adds a phase listener to the current view that invokes the given callback
     * every time after given phase.
     * 
     * @param phaseId The phase ID to invoke the given callback every time
     *            after.
     * @param callback The callback to be invoked every time after the given
     *            phase ID of the current view.
     */
    public static void addAfterPhaseListener(final PhaseId phaseId, final Callback.Void callback) {
        Events.addPhaseListener(Events.createAfterPhaseListener(phaseId, callback));
    }

    // PhaseListener
    // --------------------------------------------------------------------------------------------------

    /**
     * Adds a phase listener to the current view that invokes the given callback
     * every time before given phase ID.
     * 
     * @param phaseId The phase ID to invoke the given callback every time
     *            before.
     * @param callback The callback to be invoked every time before the given
     *            phase ID of the current view.
     */
    public static void addBeforePhaseListener(final PhaseId phaseId, final Callback.Void callback) {
        Events.addPhaseListener(Events.createBeforePhaseListener(phaseId, callback));
    }

    /**
     * Adds a phase listener to the current view root of the current faces
     * context.
     * 
     * @param phaseListener The phase listener to be added to the current view
     *            root of the current faces context.
     */
    public static void addPhaseListener(final PhaseListener phaseListener) {
        Faces.getViewRoot().addPhaseListener(phaseListener);
    }

    /**
     * Creates a phase listener that invokes the given callback every time after
     * the given phase.
     * 
     * @param phaseId The phase ID to invoke the given callback every time
     *            after.
     * @param callback The callback to be invoked every time after the given
     *            phase ID of the current view.
     * @return A phase listener that invokes the given callback every time after
     *         the given phase.
     * @since 1.2
     */
    public static PhaseListener createAfterPhaseListener(final PhaseId phaseId, final Callback.Void callback) {
        return new DefaultPhaseListener(phaseId) {

            private static final long serialVersionUID = -7760218897262285339L;

            @Override
            public void afterPhase(final PhaseEvent event) {
                callback.invoke();
            }
        };
    }

    /**
     * Creates a phase listener that invokes the given callback every time
     * before the given phase.
     * 
     * @param phaseId The phase ID to invoke the given callback every time
     *            before.
     * @param callback The callback to be invoked every time before the given
     *            phase ID of the current view.
     * @return A phase listener that invokes the given callback every time
     *         before the given phase.
     * @since 1.2
     */
    public static PhaseListener createBeforePhaseListener(final PhaseId phaseId, final Callback.Void callback) {
        return new DefaultPhaseListener(phaseId) {

            private static final long serialVersionUID = -5078199683615308073L;

            @Override
            public void beforePhase(final PhaseEvent event) {
                callback.invoke();
            }
        };
    }

    /**
     * Removes the one and only phase listener from callbacks by the global
     * phase listener for the current request.
     * 
     * @since 1.2
     */
    public static void removeCallbackPhaseListener() {
        Faces.removeRequestAttribute(CallbackPhaseListener.CALLBACK_PHASE_LISTENER);
    }

    /**
     * Removes a phase listener from the current view root of the current faces
     * context.
     * 
     * @param phaseListener The phase listener to be removed from the current
     *            view root of the current faces context.
     */
    public static void removePhaseListener(final PhaseListener phaseListener) {
        Faces.getViewRoot().removePhaseListener(phaseListener);
    }

    /**
     * Sets a phase listener for callback by a global phase listener during the
     * current request that invokes the given callback every time after given
     * phase ID.
     * 
     * @param phaseId The phase ID to invoke the given callback every time
     *            after.
     * @param callback The callback to be invoked every time after the given
     *            phase ID of the current request.
     * @since 1.2
     */
    public static void setCallbackAfterPhaseListener(final PhaseId phaseId, final Callback.Void callback) {
        Events.setCallbackPhaseListener(Events.createAfterPhaseListener(phaseId, callback));
    }

    /**
     * Sets a phase listener for callback by a global phase listener during the
     * current request that invokes the given callback every time before given
     * phase ID.
     * 
     * @param phaseId The phase ID to invoke the given callback every time
     *            before.
     * @param callback The callback to be invoked every time before the given
     *            phase ID of the current request.
     * @since 1.2
     */
    public static void setCallBackBeforePhaseListener(final PhaseId phaseId, final Callback.Void callback) {
        Events.setCallbackPhaseListener(Events.createBeforePhaseListener(phaseId, callback));
    }

    /**
     * Sets phase listener for callback by a global phase listener during the
     * current request.
     * <p>
     * This differs in a few subtle ways from
     * {@link Events#addPhaseListener(PhaseListener)}. Namely, the phase
     * listener registered here will be called via the global phase listener,
     * which executes slightly earlier for its before phase and slightly later
     * for its after phase as compared to phase listeners attached to the view
     * root.
     * <p>
     * Additionally, a phase listener registered via this method will not become
     * part of the view state, but will execute only once. Phase listeners
     * attached to the view root will come back after each postback and have to
     * be remove manually (in Mojarra this can be difficult due to the fact
     * iterators over listeners are kept 'open' during each phase).
     * <p>
     * Note: at the moment only 1 callback phase listener per request can be
     * set.
     * 
     * @param phaseListener The phase listener to be set for callback during the
     *            current request.
     * @since 1.2
     */
    public static void setCallbackPhaseListener(final PhaseListener phaseListener) {
        Faces.setRequestAttribute(CallbackPhaseListener.CALLBACK_PHASE_LISTENER, phaseListener);
    }

    /**
     * Adds the given system event listener to the application that get invoked
     * every time when the given system event type is published.
     * 
     * @param type The system event type to listen on.
     * @param listener The system event listener to be invoked.
     * @since 1.1
     */
    public static void subscribeToEvent(final Class<? extends SystemEvent> type, final SystemEventListener listener) {
        Faces.getApplication().subscribeToEvent(type, listener);
    }

    /**
     * Install the callback instance referenced by argument listener into the
     * UIViewRoot as a listener for events of type systemEventClass.
     * 
     * @param type The system event type to listen on.
     * @param callback The callback to be invoked.
     * @since 1.2
     */
    public static void subscribeToViewEvent(final Class<? extends SystemEvent> type, final Callback.Void callback) {
        Faces.getViewRoot().subscribeToViewEvent(type, new DefaultViewEventListener() {
            @Override
            public void processEvent(final SystemEvent event) throws AbortProcessingException {
                callback.invoke();
            }
        });
    }

    /**
     * Install the listener instance referenced by argument listener into the
     * UIViewRoot as a listener for events of type systemEventClass.
     * 
     * @param type The system event type to listen on.
     * @param listener The system event listener to be invoked.
     * @since 1.2
     */
    public static void subscribeToViewEvent(final Class<? extends SystemEvent> type, final SystemEventListener listener) {
        Faces.getViewRoot().subscribeToViewEvent(type, listener);
    }

    private Events() {
        // Hide constructor.
    }

}