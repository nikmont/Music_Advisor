package advisor;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Controller {

    public static String SERVER_PATH;
    public static String API_PATH;
    public static final String CLIENT_ID = "5f40b38a1feb433bb7445b77a13393de";
    public static final String CLIENT_SECRET = "6746238235184c2f9d431e6bedc5c046";
    public static final String REDIRECT_IP = "http://localhost:";
    public static final int REDIRECT_PORT = 8080;
    public static String ACCESS_TOKEN = "";
    public static String ACCESS_CODE = "";
    public static String featuredURI = "/v1/browse/featured-playlists";
    public static String newReleasesURI = "/v1/browse/new-releases";
    public static String categoriesURI = "/v1/browse/categories";

    private static String getUri() {
        return SERVER_PATH + "/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_IP + REDIRECT_PORT
                + "&response_type=code";
    }

    public static void getCode() {

        View.printAuthLink(getUri());

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(REDIRECT_PORT), 0);

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

            while ("".equals(ACCESS_CODE)) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            server.stop(10);

        } catch (IOException e) {
            System.out.println("Error at response");
        }
    }

    public static void getToken() {
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
            ACCESS_TOKEN = JsonParser.parseString(response.body()).getAsJsonObject().get("access_token").getAsString();
            System.out.println("token is " + ACCESS_TOKEN);
            System.out.println("Success!");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error at request");
        }
    }

    private static String getRequestData(String uriString) {
        HttpClient client = HttpClient.newBuilder().build();


        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(uriString))
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Request error");
        }
        
        return response != null ? response.body() : null;
    }


    public static JsonArray getFeatured() {

        return JsonParser.parseString(getRequestData(API_PATH + featuredURI)).getAsJsonObject()
        .get("playlists").getAsJsonObject()
                .get("items").getAsJsonArray();
    }

    public static JsonArray getNewReleases() {

        return JsonParser.parseString(getRequestData(API_PATH + newReleasesURI)).getAsJsonObject()
                .get("albums").getAsJsonObject()
                .get("items").getAsJsonArray();

    }

    private static String getCategoryID(String name) {

        for (JsonElement jsonElement : getCategories()) {
            var elementName = jsonElement.getAsJsonObject().get("name").getAsString();
            if (name.equals(elementName)) {
                return jsonElement.getAsJsonObject().get("id").getAsString();
            }
        }

        return null;
    }

    public static JsonArray getPlaylist(String categoryName) {

        String categoryID = getCategoryID(categoryName);
        String playlistURI = API_PATH + "/v1/browse/categories/" + categoryID + "/playlists";

        return JsonParser.parseString(getRequestData(API_PATH + playlistURI)).getAsJsonObject()
                .get("playlists").getAsJsonObject()
                .get("items").getAsJsonArray();

    }

    public static JsonArray getCategories() {

        return JsonParser.parseString(getRequestData(API_PATH + categoriesURI)).getAsJsonObject()
                .get("categories").getAsJsonObject()
                .get("items").getAsJsonArray();
    }
}
