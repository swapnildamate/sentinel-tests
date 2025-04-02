package org.sentinel.tests.utils.insights;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.model.Status;

import java.lang.reflect.Method;

public class UpdateAllure {

    public static void step(boolean result, String passMsg, String failMsg) {
        if (result == true) {
            pass(passMsg);
        } else {
            fail(failMsg);
        }
    }


    public static void pass(final String parameter) {
        Allure.step(parameter, Status.PASSED);
    }


    public static void fail(final String parameter) {
        Allure.step(parameter, Status.FAILED);
    }

    // Helper method to get @Description annotation value
    public String getTestDescription(String methodName) {
        try {
            Method method = this.getClass().getMethod(methodName);
            Description description = method.getAnnotation(Description.class);
            return (description != null) ? description.value() : "No description found";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "Error fetching description";
        }
    }
}
