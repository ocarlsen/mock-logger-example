package com.ocarlsen.test.example.log4j.junit3;

import com.ocarlsen.test.example.log4j.LoggingClass;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.mockito.InOrder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public abstract class MyLoggingClassTestBase<T extends LoggingClass> extends TestCase {

    public void testLoggingMethod() throws Exception {

        // Prepare mocks
        final Logger logger = getMockLogger();
        prepareBeforeInstance(logger, getLoggingClassName(), getLoggerFieldName());

        // Given
        final T testInstance = newLoggingInstance();
        prepareAfterInstance(logger, testInstance, getLoggerFieldName());

        // When
        testInstance.loggingMethod();

        // Then
        // (Asserts on other logic.)

        // Verify mocks
        verifyLogger(logger);
        verifyNoMoreInteractions(logger);
    }

    public void testLoggingMethodWithException() throws Exception {

        // Prepare mocks
        final Logger logger = getMockLogger();
        prepareBeforeInstance(logger, getLoggingClassName(), getLoggerFieldName());

        // Given
        final T testInstance = newLoggingInstance();
        final Exception ex = new Exception("fake it");
        prepareAfterInstance(logger, testInstance, getLoggerFieldName());

        // When
        testInstance.loggingMethodWithException(ex);

        // Then
        // (Asserts on other logic.)

        // Verify mocks
        verifyLogger(logger, ex);
        verifyNoMoreInteractions(logger);
    }

    protected abstract T newLoggingInstance();

    protected abstract String getLoggingClassName();

    protected abstract String getLoggerFieldName();

    protected abstract Logger getMockLogger();

    protected void verifyLogger(final Logger logger) {
        final InOrder inOrder = inOrder(logger);
        inOrder.verify(logger).trace("this is a trace message");
        inOrder.verify(logger).debug("this is a debug message");
        inOrder.verify(logger).info("this is an info message");
        inOrder.verify(logger).warn("this is a warn message");
        inOrder.verify(logger).error("this is an error message");
        inOrder.verifyNoMoreInteractions();
    }

    protected void verifyLogger(final Logger logger, final Exception ex) {
        final InOrder inOrder = inOrder(logger);
        inOrder.verify(logger).trace("this is a trace message", ex);
        inOrder.verify(logger).debug("this is a debug message", ex);
        inOrder.verify(logger).info("this is an info message", ex);
        inOrder.verify(logger).warn("this is a warn message", ex);
        inOrder.verify(logger).error("this is an error message", ex);
        inOrder.verifyNoMoreInteractions();
    }

    @SuppressWarnings("SameParameterValue")
    protected abstract void prepareAfterInstance(final Logger logger,
                                                 final Object testInstance,   // Need to provide as Object so it does not get loaded before we can mock it.
                                                 final String loggerFieldName) throws Exception;

    @SuppressWarnings("SameParameterValue")
    protected abstract void prepareBeforeInstance(final Logger mockLogger,
                                                  final String loggingClassName,  // Need to provide as String so it does not get loaded before we can mock it.
                                                  final String loggerFieldName) throws Exception;
}
