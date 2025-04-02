package org.sentinel.tests.reportUtils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

public class AllureLogUtil {

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
}
