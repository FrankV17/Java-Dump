// Re-throwing Exceptions (b)
public static void rethrowException(int value) throws MyCustomException {
    if (value < 0) {
        throw new MyCustomException("Value cannot be negative"); // Re-throw a custom exception
    }
    // Code that might throw other exceptions (not re-thrown here)
}

// Custom exception for clarity (b)
class MyCustomException extends Exception {
    public MyCustomException(String message) {
        super(message);
    }
}
