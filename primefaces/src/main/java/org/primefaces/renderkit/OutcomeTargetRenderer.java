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
package org.primefaces.renderkit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.UIOutcomeTarget;

public class OutcomeTargetRenderer extends CoreRenderer {

    protected NavigationCase findNavigationCase(final FacesContext context, final UIOutcomeTarget outcomeTarget) {
        final ConfigurableNavigationHandler navHandler = (ConfigurableNavigationHandler) context.getApplication()
            .getNavigationHandler();
        String outcome = outcomeTarget.getOutcome();

        if (outcome == null) {
            outcome = context.getViewRoot().getViewId();
        }

        return navHandler.getNavigationCase(context, null, outcome);
    }

    /**
     * Find all parameters to include by looking at nested uiparams and params
     * of navigation case
     */
    protected Map<String, List<String>> getParams(final NavigationCase navCase, final UIOutcomeTarget outcomeTarget) {
        final Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();

        // UIParams
        for (final UIComponent child : outcomeTarget.getChildren()) {
            if (child.isRendered() && (child instanceof UIParameter)) {
                final UIParameter uiParam = (UIParameter) child;

                if (!uiParam.isDisable()) {
                    List<String> paramValues = params.get(uiParam.getName());
                    if (paramValues == null) {
                        paramValues = new ArrayList<String>();
                        params.put(uiParam.getName(), paramValues);
                    }

                    paramValues.add(String.valueOf(uiParam.getValue()));
                }
            }
        }

        // NavCase Params
        final Map<String, List<String>> navCaseParams = navCase.getParameters();
        if (navCaseParams != null && !navCaseParams.isEmpty()) {
            for (final Map.Entry<String, List<String>> entry : navCaseParams.entrySet()) {
                final String key = entry.getKey();

                // UIParams take precedence
                if (!params.containsKey(key)) {
                    params.put(key, entry.getValue());
                }
            }
        }

        return params;
    }

    protected String getTargetURL(final FacesContext context, final UIOutcomeTarget outcomeTarget) {
        String url;
        final String href = outcomeTarget.getHref();

        if (href != null) {
            url = getResourceURL(context, href);
        } else {
            final NavigationCase navCase = findNavigationCase(context, outcomeTarget);
            final String toViewId = navCase.getToViewId(context);
            final boolean isIncludeViewParams = isIncludeViewParams(outcomeTarget, navCase);
            final Map<String, List<String>> params = getParams(navCase, outcomeTarget);

            url = context.getApplication().getViewHandler().getBookmarkableURL(context, toViewId, params,
                                                                               isIncludeViewParams);

            // fragment
            if (outcomeTarget.getFragment() != null) {
                url += "#" + outcomeTarget.getFragment();
            }
        }

        return url;
    }

    protected boolean isIncludeViewParams(final UIOutcomeTarget outcomeTarget, final NavigationCase navCase) {
        return outcomeTarget.isIncludeViewParams() || navCase.isIncludeViewParams();
    }
}
