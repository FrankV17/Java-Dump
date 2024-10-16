// Chaining Exceptions (d)
public static void chainExceptions(int input) throws IOException, MyCustomCheckedException {
    try {
        validateInput(input); // Might throw MyCustomCheckedException
    } catch (MyCustomCheckedException e) {
        throw new IOException("Input validation failed: " + e.getMessage(), e); // Chain exceptions
    }
    
    private static void validateInput(int input) throws MyCustomCheckedException {
        if (input <= 0) {
            throw new MyCustomCheckedException("Input must be positive");
        }
    }
}

// Custom checked exception for chaining (d)
class MyCustomCheckedException extends Exception {
    public MyCustomCheckedException(String message) {
        super(message);
    }
}