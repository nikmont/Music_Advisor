package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length > 1 && "-access".equals(args[0])) {
            Server.SERVER_PATH = args[1];
        }
        setAction();
    }

    public static void setAction() {

    Server server = new Server();
    boolean isAuthorized = false;
    String action;

    while (true) {
        action = scan.next();
        if (!"auth".equals(action) && !isAuthorized && !"exit".equals(action)) {
            System.out.println("Please, provide access for application.");
        } else {
            switch (action) {
                case "auth":
                    authorization(server);
                    isAuthorized = true;
                    break;
                case "featured":
                    getFeatured(server);
                    break;
                case "new":
                    newReleases(server);
                    break;
                case "categories":
                    getCategories(server);
                    break;
                case "playlists":
                    getPlaylists(server);
                    break;
                case "exit":
                    return;
            }
        }
    }
}

    public static void authorization(Server server) {
        server.getCode();
        server.getToken();
    }

    public static void getPlaylists(Server server) {

        String playlistName = scan.nextLine();
        String playlist = server.getPlaylist(playlistName);

    }

    public static void getCategories(Server server) {
        String jsonCateg = server.getCategories();
        JsonObject jo = JsonParser.parseString(jsonCateg).getAsJsonObject();
        JsonObject categ = jo.get("categories").getAsJsonObject();

        for (JsonElement jsonElement : categ.get("items").getAsJsonArray()) {
            System.out.println(jsonElement.getAsJsonObject().get("name").getAsString());
        }
    }

    public static void newReleases(Server server) {
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

    public static void getFeatured(Server server) {
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
