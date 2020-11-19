package advisor;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Server {
    public static String SERVER_PATH = "https://accounts.spotify.com";
    public static String CLIENT_ID = "5f40b38a1feb433bb7445b77a13393de";
    public static String CLIENT_SECRET = "6746238235184c2f9d431e6bedc5c046";
    public static String REDIRECT_IP = "http://localhost:";
    public static int REDIRECT_PORT = 8080;
    public static String ACCESS_TOKEN = "";
    public static String ACCESS_CODE = "";

    HttpServer server;


    public String getUri() {
        return SERVER_PATH + "/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_IP + REDIRECT_PORT
                + "&response_type=code";
    }

    public void getCode() {

        System.out.println("use this link to request the access code:\n" +
                getUri());

        try {
            server = HttpServer.create(new InetSocketAddress(REDIRECT_PORT), 0);

            server.createContext("/",
                    exchange -> {
                        String query = exchange.getRequestURI().getQuery();
                        String response;
                        if(query != null && query.contains("code=")) {
                            ACCESS_CODE = query.substring(5);
                            System.out.println("code received");
                            response = "Got the code. Return back to your program.";
                        } else {
                            response = "Authorization code not found. Try again.";
                        }
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes());
                        exchange.getResponseBody().close();
            });
            server.start();

            System.out.println("waiting for code...");

            while ("".equals(ACCESS_CODE)) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            server.stop(1);

        } catch (IOException e) {
            System.out.println("Error at response");
        }
    }



    public void getToken() {
        HttpClient client = HttpClient.newBuilder().build();

        System.out.println("making http request for access_token...\nresponse:");

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(SERVER_PATH + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code" +
                        "&code=" + ACCESS_CODE +
                        "&redirect_uri=" + REDIRECT_IP + REDIRECT_PORT +
                        "&client_secret=" + CLIENT_SECRET +
                        "&client_id=" + CLIENT_ID))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            //ACCESS_TOKEN = ;
            System.out.println("---SUCCESS---");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error at request");
        }
    }

}