package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;

public class AdminReturnButton extends Button {
    public AdminReturnButton() {
        setText("Powrót");
        setWidth("100%");
        addClickListener(event -> UI.getCurrent().navigate("admin"));
    }
}
