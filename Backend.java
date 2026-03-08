import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Backend {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/calculate", new CalculateHandler());
        server.createContext("/", new StaticHandler());
        server.setExecutor(null); // default executor
        System.out.println("Server started on http://localhost:8000");
        server.start();
    }

    static class CalculateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            String numsParam = params.get("nums");
            String op = params.get("op");
            String response;
            try {
                int[] numbers = parseNumbers(numsParam);
                double result;
                switch (op) {
                    case "sum":
                        result = Calculator.sum(numbers);
                        break;
                    case "subtract":
                        result = Calculator.sequentialSubtract(numbers);
                        break;
                    case "product":
                        result = Calculator.product(numbers);
                        break;
                    case "average":
                        result = Calculator.average(numbers);
                        break;
                    default:
                        throw new IllegalArgumentException("unknown operation: " + op);
                }
                response = "{\"result\":" + result + "}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes().length);
            } catch (Exception e) {
                response = "{\"error\":\"" + e.getMessage() + "\"}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(400, response.getBytes().length);
            }
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            String path = uri.getPath();
            if (path.equals("/")) {
                path = "/frontend.html";
            }
            File file = new File("." + path).getCanonicalFile();
            if (!file.exists() || !file.getPath().startsWith(new File(".").getCanonicalPath())) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            byte[] bytes = Files.readAllBytes(file.toPath());
            String contentType = guessContentType(file.getName());
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
        private String guessContentType(String name) {
            if (name.endsWith(".html")) return "text/html";
            if (name.endsWith(".css")) return "text/css";
            if (name.endsWith(".js")) return "application/javascript";
            return "application/octet-stream";
        }
    }

    private static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            }
        }
        return result;
    }

    private static int[] parseNumbers(String commaSeparated) throws IllegalArgumentException {
        if (commaSeparated == null || commaSeparated.isEmpty()) {
            return new int[0];
        }
        String[] parts = commaSeparated.split(",");
        int[] nums = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            nums[i] = Integer.parseInt(parts[i].trim());
        }
        return nums;
    }

}
