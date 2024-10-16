public class CustomExceptionHandler {

    // Handling Multiple Exceptions (a)
    public static void handleMultipleExceptions(String fileName) throws IOException, FileNotFoundException {
        try (FileReader reader = new FileReader(fileName)) {
            // Code that might throw IOException or FileNotFoundException
            int ch;
            while ((ch = reader.read()) != -1) {
                System.out.print((char) ch);
            }
        } catch (IOException | FileNotFoundException e) { // Combined catch block for both exceptions
            if (e instanceof FileNotFoundException) {
                System.err.println("File not found: " + fileName);
            } else {
                System.err.println("Error reading file: " + fileName + " (" + e.getMessage() + ")");
            }
        }
    }

    // Re-throwing Exceptions (b)
    public static void rethrowException(int value) throws MyCustomException {
        if (value < 0) {
            throw new MyCustomException("Value cannot be negative"); // Re-throw a custom exception
        }
        // Code that might throw other exceptions (not re-thrown here)
    }

    // Resource Management (c)
    public static void manageResources(String url) throws IOException {
        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
            // Code that uses the connection (e.g., downloading data)
        } catch (Exception e) {
            System.err.println("Error accessing resource: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect(); // Ensure resource is closed even if exceptions occur
            }
        }
    }

    // Chaining Exceptions (d)
    public static void chainExceptions(int input) throws IOException, MyCustomCheckedException {
        try {
            validateInput(input); // Might throw MyCustomCheckedException
        } catch (MyCustomCheckedException e) {
            throw new IOException("Input validation failed: " + e.getMessage(), e); // Chain exceptions
        }
    }

    private static void validateInput(int input) throws MyCustomCheckedException {
        if (input <= 0) {
            throw new MyCustomCheckedException("Input must be positive");
        }
    }
}

// Custom exception for clarity (b)
class MyCustomException extends Exception {
    public MyCustomException(String message) {
        super(message);
    }
}

// Custom checked exception for chaining (d)
class MyCustomCheckedException extends Exception {
    public MyCustomCheckedException(String message) {
        super(message);
    }
}