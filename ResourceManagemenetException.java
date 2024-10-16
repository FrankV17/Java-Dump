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