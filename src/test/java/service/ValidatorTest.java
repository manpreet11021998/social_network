package service;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static service.Validator.*;
//import static service.Validator.ValidationCode;

public class ValidatorTest {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String USER_NOT_FOUND_RU = "������������ �� ������";
    private static final String VALID_EMAIL = "test@mail.ru";
    private static final String VALID_PASSWORD = "qwerty";
    private static final String ANOTHER_VALID_PASSWORD = "qwerty123";
    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final String INVALID_EMAIL = "test";
    private static final String INVALID_PASSWORD = "";
    private static final String INVALID_NAME = "John Doe";

    @Before
    public void beforeValidatorTest() {

        //To test abstract class itself
        @SuppressWarnings("SameReturnValue")
        class ValidatorImpl extends Validator {
            private boolean abstractMethod() {
                return true;
            }
        }

        ValidatorImpl validatorImpl = new ValidatorImpl();
        assertThat(validatorImpl.abstractMethod(), is(true));

    }

    @Test
    public void getMessageTest() throws Exception {

        assertThat(getMessage(ValidationCode.USER_NOT_FOUND, new Locale("ru")),
                is(USER_NOT_FOUND_RU));
        assertThat(getMessage(ValidationCode.USER_NOT_FOUND, Locale.ENGLISH),
                is(USER_NOT_FOUND));
        assertThat(getMessage(ValidationCode.USER_NOT_FOUND, Locale.GERMAN),
                is(getMessage(ValidationCode.USER_NOT_FOUND, Locale.getDefault())));
        assertThat(getMessage(ValidationCode.REGISTRATION, Locale.GERMAN),
                is(getMessage(ValidationCode.REGISTRATION, Locale.getDefault())));

    }

    @Test
    public void validateLoginTest() throws Exception {

        assertThat(validateLogin(INVALID_EMAIL, VALID_PASSWORD),
                is(ValidationCode.EMAIL_NOT_VALID));
        assertThat(validateLogin(VALID_EMAIL, INVALID_PASSWORD),
                is(ValidationCode.PASS_NOT_VALID));
        assertThat(validateLogin(VALID_EMAIL, VALID_PASSWORD),
                is(ValidationCode.LOGIN_SUCCESS));

    }

    @Test
    public void validateRegistrationTest() throws Exception {

        assertThat(validateRegistration(INVALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_FIRST_NAME, VALID_LAST_NAME),
                is(ValidationCode.EMAIL_NOT_VALID));
        assertThat(validateRegistration(VALID_EMAIL, INVALID_PASSWORD, VALID_PASSWORD, VALID_FIRST_NAME, VALID_LAST_NAME),
                is(ValidationCode.PASS_NOT_VALID));
        assertThat(validateRegistration(VALID_EMAIL, VALID_PASSWORD, ANOTHER_VALID_PASSWORD, VALID_FIRST_NAME, VALID_LAST_NAME),
                is(ValidationCode.PASS_DIFFERS));
        assertThat(validateRegistration(VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, INVALID_NAME, VALID_LAST_NAME),
                is(ValidationCode.NAME_NOT_VALID));
        assertThat(validateRegistration(VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_FIRST_NAME, INVALID_NAME),
                is(ValidationCode.NAME_NOT_VALID));
        assertThat(validateRegistration(VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_FIRST_NAME, VALID_LAST_NAME),
                is(ValidationCode.REGISTRATION_SUCCESS));

    }

    @Test
    public void validatePasswordChangeTest() throws Exception {
        assertThat(validatePasswordChange(VALID_PASSWORD, VALID_PASSWORD, VALID_PASSWORD),
                is(ValidationCode.PASS_NOT_CHANGED));
        assertThat(validatePasswordChange(VALID_PASSWORD, INVALID_PASSWORD, VALID_PASSWORD),
                is(ValidationCode.PASS_NOT_VALID));
        assertThat(validatePasswordChange(ANOTHER_VALID_PASSWORD, VALID_PASSWORD, ANOTHER_VALID_PASSWORD),
                is(ValidationCode.PASS_DIFFERS));
        assertThat(validatePasswordChange(VALID_PASSWORD, ANOTHER_VALID_PASSWORD, ANOTHER_VALID_PASSWORD),
                is(ValidationCode.PASS_CHANGED_SUCCESS));

    }

    @Test
    public void validateContactTest() throws Exception {
        assertThat(validateContact(VALID_EMAIL, VALID_FIRST_NAME, VALID_LAST_NAME),
                is(ValidationCode.CONTACT_CHANGED_SUCCESS));
        assertThat(validateContact(INVALID_EMAIL, VALID_FIRST_NAME, VALID_LAST_NAME),
                is(ValidationCode.EMAIL_NOT_VALID));
        assertThat(validateContact(VALID_EMAIL, INVALID_NAME, VALID_LAST_NAME),
                is(ValidationCode.NAME_NOT_VALID));
        assertThat(validateContact(VALID_EMAIL, VALID_LAST_NAME, INVALID_NAME),
                is(ValidationCode.NAME_NOT_VALID));
    }

    @Test
    public void isValidCodeTest() throws Exception {
        assertThat(isValidCode(ValidationCode.LOGIN_SUCCESS), is(true));
        assertThat(isValidCode(ValidationCode.REGISTRATION_SUCCESS), is(true));
        assertThat(isValidCode(ValidationCode.CONTACT_CHANGED_SUCCESS), is(true));
        assertThat(isValidCode(ValidationCode.PASS_CHANGED_SUCCESS), is(true));
        assertThat(isValidCode(ValidationCode.PASS_DIFFERS), is(false));
    }

}