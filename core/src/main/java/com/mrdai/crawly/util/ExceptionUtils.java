package com.mrdai.crawly.util;

public final class ExceptionUtils {
    private static final StackTraceElement[] emptyTrace = new StackTraceElement[0];

    /**
     * Returns an exception of the given type with an empty stack trace.
     *
     * @param exceptionType the excepted type of the exception.
     * @param <T> the expected type of the the exception.
     * @return an exception of the given type with an empty stack trace.
     *
     * @throws NullPointerException if the given constructor is a {@code null} or returns a {@code null}.
     * @throws IllegalArgumentException if failed to create an instance for the given type.
     */
    public static <T extends Throwable> T emptyTraceException(Class<T> exceptionType) {
        if (exceptionType == null)
            throw new NullPointerException("The given type exception cannot be null.");

        T exception;
        try {
            exception = exceptionType.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Failed to instantiate the given type of exception.", e);
        }

        exception.setStackTrace(emptyTrace);

        return exception;
    }

    /**
     * Returns an with the given type and empty stack trace, and initializes its cause with the given
     * {@code Throwable}.
     * <p>
     * The method uses {@link Throwable#initCause(Throwable)} to initialize the cause, which can only be called once.
     * If this method is already called within the no-arg constructor of the given type, this will result to
     * an {@code IllegalStateException}.
     *
     * @param exceptionType the expected type of the exception.
     * @param cause the given {@code Throwable} that will be initialized as the cause of the produced {@code Throwable}.
     * @param <T> the expected type of the the exception.
     * @return an exception of the given type with empty stack trace and the given cause.
     *
     * @throws NullPointerException if the given constructor is a {@code null} or returns a {@code null}.
     * @throws IllegalStateException if the produced exception's {@link Throwable#initCause(Throwable)} has already
     *                               been called within the given supplier.
     * @throws IllegalArgumentException if failed to create an instance for the given type.
     *
     * @see Throwable#initCause(Throwable)
     */
    public static <T extends Throwable> T emptyTraceException(Class<T> exceptionType, Throwable cause) {
        T exception = emptyTraceException(exceptionType);
        exception.initCause(cause);

        return exception;
    }

    private ExceptionUtils() {
        throw new AssertionError("ExceptionUtils should not be instantiated!");
    }
}
