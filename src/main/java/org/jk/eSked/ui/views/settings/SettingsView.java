package org.jk.eSked.ui.views.settings;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.ui.components.menu.Menu;
import org.jk.eSked.ui.components.settings.tabs.AccountTab;
import org.jk.eSked.ui.components.settings.tabs.DeleteTab;
import org.jk.eSked.ui.components.settings.tabs.GroupTab;
import org.jk.eSked.ui.components.settings.tabs.OtherTab;

@Route(value = "settings", layout = Menu.class)
@PageTitle("Ustawienia")
public class SettingsView extends VerticalLayout {
    public SettingsView() {
        SessionService.setAutoTheme();

        add(new AccountTab(), new GroupTab(), new OtherTab(), new DeleteTab());
        setSizeFull();
    }
}
