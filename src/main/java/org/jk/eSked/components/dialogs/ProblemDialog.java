package org.jk.eSked.components.dialogs;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import org.jk.eSked.components.SuccessNotification;
import org.jk.eSked.model.User;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.users.UserService;

import javax.mail.MessagingException;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Random;

public class ProblemDialog extends Dialog {
    private final UserService userService;
    private final TextField passField;
    private Button passButton;
    private Registration registration;

    public ProblemDialog(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.passField = new TextField();
        this.passButton = new Button("Wyślij");

        add(createMainLayout());

        registration = passButton.addClickListener(event -> {
            try {
                validateNameInput(passField.getValue());
                passField.setInvalid(false);
                Random random = new Random();
                int code = random.nextInt(89999) + 10000;
                String emailBody = "Witaj " + passField.getValue() + "," + "<br><br>Twój kod zmiany hasła to: " + "<br><br>" + code +
                        "<br><br>" + "Teraz możesz wpisać go na stronie!" + "<br><br> Z poważaniem, <br>Zespół eSked";
                emailService.sendEmail(userService.getEmailFromUsername(passField.getValue()), "Potwierdzenie zmiany hasła w eSked!", emailBody);

                passField.setWidth("60%");
                passField.setPlaceholder("Kod z wiad. email");
                passField.clear();

                passButton.setText("Potwierdź");
                passButton.setWidth("40%");
                registration = passButton.addClickListener(newEvent -> {
                    try {
                        validateCode(passField.getValue(), code);
                        passField.setInvalid(false);

                        PasswordField pass1 = new PasswordField("Nowe Hasło");
                        PasswordField pass2 = new PasswordField("Powtórz Nowe Hasło");
                        Button confirm = new Button("Zmień hasło!", event3 -> {
                            try {
                                validatePasswords(pass1.getValue(), pass2.getValue());
                                pass1.setInvalid(false);
                                pass2.setInvalid(false);
                                userService.changePassword(userService.getIdFromUsername(passField.getValue()), User.encodePassword(pass1.getValue()));

                                SuccessNotification notification = new SuccessNotification("Zmieniono hasło!");
                                notification.open();

                                close();
                                removeAll();
                                passButton.setText("Zmień");
                                passField.clear();
                                add(createMainLayout());

                            } catch (ValidationException ex) {
                                pass2.setErrorMessage(ex.getMessage());
                                pass1.setInvalid(true);
                                pass2.setInvalid(true);
                            }
                        });
                        confirm.addClickShortcut(Key.ENTER);
                        confirm.setWidth("100%");
                        VerticalLayout layout = new VerticalLayout(pass1, pass2, confirm);
                        removeAll();
                        add(layout);
                    } catch (ValidationException ex) {
                        passField.setErrorMessage(ex.getMessage());
                        passField.setInvalid(true);
                    }
                });
            } catch (ValidationException ex) {
                passField.setErrorMessage(ex.getMessage());
                passField.setInvalid(true);
            } catch (MessagingException mex) {
                passField.setErrorMessage("Email error contact admin");
                passField.setInvalid(true);
            }
        });
    }

    private VerticalLayout createMainLayout() {
        Label passLabel = new Label("Odzyskaj Hasło");
        passLabel.getStyle().set("font-weight", "bold");

        passField.setPlaceholder("Nazwa Użytkownika");
        passField.setWidth("70%");

        passButton.setText("Wyślij");
        passButton.addClickShortcut(Key.ENTER);
        passButton.setWidth("30%");

        HorizontalLayout passFieldLayout = new HorizontalLayout(passField, passButton);
        passFieldLayout.setWidth("100%");

        Label emailLabel = new Label("Kontakt:");
        emailLabel.getStyle().set("font-weight", "bold");
        Label email = new Label("eskedinfo@gmail.com");
        email.getStyle().set("font-weight", "bold");

        VerticalLayout mainLayout = new VerticalLayout(passLabel, passFieldLayout, emailLabel, email);
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        return mainLayout;
    }

    private void validateNameInput(String intput) {
        if (intput.isEmpty()) throw new ValidationException("Pole nie może być puste");

        Collection<String> users = userService.getUsernames();
        if (!users.contains(intput)) throw new ValidationException("Użytkownik z taką nazwą nie istnieje");
    }

    private void validatePasswords(String input1, String input2) {
        if (input1.isEmpty()) throw new ValidationException("Pola nie mogą być puste");

        if (input2.isEmpty()) throw new ValidationException("Pola nie mogą być puste");

        if (!input1.equals(input2)) throw new ValidationException("Hasła muszą być identyczne");

    }

    private void validateCode(String input, int code) {
        if (input.isEmpty()) throw new ValidationException("Pole nie może być puste");

        if (!input.equals(Integer.toString(code))) throw new ValidationException("Podany kod jest nie prawidłowy");
    }
}