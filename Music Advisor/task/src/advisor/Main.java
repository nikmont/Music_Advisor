package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.*;

public class Main {

    static Scanner scan = new Scanner(System.in);
    static Server server = new Server();

    public static void main(String[] args) {
        Map<String, String> cliArg = new HashMap<>();

        if (args.length >= 2) {
            for (int i = 0; i < args.length; i += 2) {
                cliArg.put(args[i], args[i + 1]);
            }
        }

        Server.SERVER_PATH = cliArg.getOrDefault("-access", "https://accounts.spotify.com");
        Server.API_PATH = cliArg.getOrDefault("-resource", "https://api.spotify.com");

        setAction();
    }

    public static void setAction() {

    boolean isAuthorized = false;
    String action;

    while (true) {
        action = scan.next();
        if (!"auth".equals(action) && !isAuthorized && !"exit".equals(action)) {
            System.out.println("Please, provide access for application.");
        } else {
            switch (action) {
                case "auth":
                    authorization();
                    isAuthorized = true;
                    break;
                case "featured":
                    getFeatured();
                    break;
                case "new":
                    newReleases();
                    break;
                case "categories":
                    getCategories();
                    break;
                case "playlists":
                    getPlaylists();
                    break;
                case "exit":
                    return;
            }
        }
    }
}

    public static void authorization() {
        server.getCode();
        server.getToken();
    }

    public static void getPlaylists() {
        Map<String, String> categories = new HashMap<>();
        String playlistName = scan.nextLine().trim();
        String playlistJson;
        String categoryID;

        JsonObject categ = JsonParser.parseString(server.getCategories()).getAsJsonObject().get("categories").getAsJsonObject();
        for (JsonElement jsonElement : categ.get("items").getAsJsonArray()) {
            categories.put(jsonElement.getAsJsonObject().get("name").getAsString(), jsonElement.getAsJsonObject().get("id").getAsString());
        }

        if (categories.containsKey(playlistName)) {
            categoryID = categories.get(playlistName);
        } else {
            System.out.println("Unknown category name.");
            return;
        }

        playlistJson = server.getPlaylist(categoryID);

        if (playlistJson != null) {
        JsonObject jo = JsonParser.parseString(playlistJson).getAsJsonObject().get("playlists").getAsJsonObject();

            for (JsonElement jsonElement : jo.get("items").getAsJsonArray()) {
            System.out.println("\n" + jsonElement.getAsJsonObject().get("name").getAsString() + "\n" +
                    jsonElement.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString());
            }

        } else {
            System.out.println("Error");
        }
    }

    public static void getCategories() {
        String jsonCateg = server.getCategories();
        JsonObject jo = JsonParser.parseString(jsonCateg).getAsJsonObject();
        JsonObject categ = jo.get("categories").getAsJsonObject();

        for (JsonElement jsonElement : categ.get("items").getAsJsonArray()) {
            System.out.println("\n" + jsonElement.getAsJsonObject().get("name").getAsString());
        }
    }

    public static void newReleases() {
        String body = server.getNewReleases();

            JsonArray itemsArr = JsonParser.parseString(body).getAsJsonObject()
                    .get("albums").getAsJsonObject()
                    .get("items").getAsJsonArray();

            List<String> artists = new ArrayList<>();

            for (JsonElement jsonElement : itemsArr) {

                for (JsonElement artist : jsonElement.getAsJsonObject().get("artists").getAsJsonArray()) {
                    artists.add(artist.getAsJsonObject().get("name").getAsString());
                }

                System.out.println("\n" + jsonElement.getAsJsonObject().get("name").getAsString());
                System.out.println(artists.toString());
                System.out.println(jsonElement.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString());
                artists.clear();
            }
    }

    public static void getFeatured() {
        String featured = server.getFeatured();

        JsonArray itemsArr = JsonParser.parseString(featured).getAsJsonObject()
                .get("playlists").getAsJsonObject()
                .get("items").getAsJsonArray();

        for (JsonElement jsonElement : itemsArr) {
            System.out.println(jsonElement.getAsJsonObject().get("name").getAsString() + "\n" +
                    jsonElement.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
        }
    }
}
