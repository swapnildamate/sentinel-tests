/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.testng;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Custom TestNG annotation transformer that implements IAnnotationTransformer.
 * This transformer automatically adds retry functionality to test methods by
 * setting
 * a retry analyzer. When tests fail, the RetryAnalyzer will attempt to re-run
 * the
 * failed tests according to configured retry policies.
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see org.testng.IAnnotationTransformer
 * @see org.testng.annotations.ITestAnnotation
 */
public class AnnotationTransformer implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
