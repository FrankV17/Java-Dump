// Chaining Exceptions (d)
public static void chainExceptions(int input) throws IOException, MyCustomCheckedException {
    try {
        validateInput(input); // Might throw MyCustomCheckedException
    } catch (MyCustomCheckedException e) {
        throw new IOException("Input validation failed: " + e.getMessage(), e); // Chain exceptions
    }
}

// Custom checked exception for chaining (d)
class MyCustomCheckedException extends Exception {
    public MyCustomCheckedException(String message) {
        super(message);
    }
}