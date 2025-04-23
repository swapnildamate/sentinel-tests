package org.sentinel.tests.constants;

public class Config {

    private Config(){

    }

    //
    public static final String RUN_ON="run_on";
    public static final String RUN_ON_LOCAL="local";
    public static final String RUN_ON_REMOTE="remote";
    public static final String MAX_RETRY_COUNT="maxRetryCount";
    public static final String TAKE_SNAP_ON_FAILURE="takeSnapOnFailure";
    public static final String TEST_CASES_RESULT_MAP="testCasesResultMap";

    //Desired Capabilities constants.
    public static final String PLATFORM = "platform";
    public static final String BROWSER_NAME = "browserName";
    public static final String BROWSER_VERSION = "version";
    public static final String BUILD_NAME="build";
    public static final String RUN_NAME = "name";
    public static final String LT_BUILD="LT_BUILD";

    //Test case Severity
    public static final String CRITICAL="Critical";
    public static final String MAJOR="Major";
    public static final String MEDIUM="Medium";
    public static final String LOW="Low";



}
