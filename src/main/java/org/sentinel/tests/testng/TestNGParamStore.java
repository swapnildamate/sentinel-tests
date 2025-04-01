package org.sentinel.tests.testng;

import org.testng.ITestContext;

import java.util.HashMap;
import java.util.Map;

public class TestNGParamStore {

    private static final Map<String, String> paramMap = new HashMap<>();

    public static void loadParameters(ITestContext context) {
        context.getCurrentXmlTest().getAllParameters()
                .forEach(paramMap::put);
    }

    public static String getParameter(String key) {
        return paramMap.get(key);
    }

    public static Map<String, String> getAllParameters() {
        return paramMap;
    }
}
