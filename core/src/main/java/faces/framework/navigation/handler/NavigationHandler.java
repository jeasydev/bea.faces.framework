package faces.framework.navigation.handler;

import java.util.Map;
import java.util.Set;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;

public class NavigationHandler extends ConfigurableNavigationHandler {

    @Override
    public NavigationCase getNavigationCase(FacesContext context, String arg1, String arg2) {
        return null;
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
        return null;
    }

    @Override
    public void handleNavigation(FacesContext context, String arg1, String arg2) {

    }

}
