package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;

public class View {

    static int elemPerPage;

    public static void printAuthLink(String URI) {
        System.out.println("use this link to request the access code:\n" + URI);
        System.out.println("waiting for code...");
    }

//    public static void paginationOutput(JsonArray arr) {
//
//        int pageCount = arr.size() % elemPerPage == 0 ? arr.size() / elemPerPage : (arr.size() / elemPerPage) + 1;
//        int elemCount = 0;
//
//        for (int i = 0, n = 1; i < arr.size(); i += elemPerPage, n++) { //настроить кол-во и мб вывести все это в отедльную функцию
//            System.out.printf("---PAGE %d OF %d---%n", n, pageCount);
//            for (int k = 0; k < elemPerPage && elemCount < arr.size(); k++) {
//
//
//                System.out.println(arr.get(elemCount).getAsJsonObject().get("name").getAsString());
//
//
//
//                elemCount++;
//            }
//
//
//            boolean isCorrectAction = false;
//            while (!isCorrectAction) {
//
//                //String pageAction = scan.nextLine();
//
//                switch (scan.nextLine()) {
//                    case "next":
//                        if (n == pageCount) {
//                            System.out.println("No more pages.");
//                        } else {
//                            isCorrectAction = true;
//                        }
//                        break;
//                    case "prev":
//                        if (n == 1) {
//                            System.out.println("No more pages.");
//                        } else {
//                            n -= 2;
//                            i -= (elemPerPage * 2);
//                            elemCount -= (elemPerPage * 2);
//                            isCorrectAction = true;
//                        }
//                        break;
//                }
//
//
//            }
//        }
//    }


    public static void getPlaylists(String playlistName) {

        for (JsonElement jsonElement : Controller.getPlaylist(playlistName)) {
            System.out.println("\n" + jsonElement.getAsJsonObject().get("name").getAsString() + "\n" +
                    jsonElement.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString());
        }
    }

    public static void getCategories() {

//        JsonArray categories = Controller.getCategories(); //всегда 20 категорий
//
//        int pageCount = categories.size() % elemPerPage == 0 ? categories.size() / elemPerPage : (categories.size() / elemPerPage) + 1;
//        int elemCount = 0;
//
//        for (int i = 0, n = 1; i < categories.size(); i += elemPerPage, n++) { //настроить кол-во и мб вывести все это в отедльную функцию
//            System.out.printf("---PAGE %d OF %d---%n", n, pageCount);
//            for (int k = 0; k < elemPerPage && elemCount < categories.size(); k++) {
//                System.out.println(categories.get(elemCount).getAsJsonObject().get("name").getAsString());
//                elemCount++;
//            }
//
//
//            boolean isCorrectAction = false;
//            while (!isCorrectAction) {
//
//                //String pageAction = scan.nextLine();
//
//                switch (scan.nextLine()) {
//                    case "next":
//                        if (n == pageCount) {
//                            System.out.println("No more pages.");
//                        } else {
//                            isCorrectAction = true;
//                        }
//                        break;
//                    case "prev":
//                        if (n == 1) {
//                            System.out.println("No more pages.");
//                        } else {
//                            n -= 2;
//                            i -= (elemPerPage * 2);
//                            elemCount -= (elemPerPage * 2);
//                            isCorrectAction = true;
//                        }
//                        break;
//                }
//
//
//            }
//
//
//
//        }

    }

    public static void newReleases() {

        JsonArray itemsArr = Controller.getNewReleases();
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

        JsonArray itemsArr = Controller.getFeatured();

        for (JsonElement jsonElement : itemsArr) {
            System.out.println(jsonElement.getAsJsonObject().get("name").getAsString() + "\n" +
                    jsonElement.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
        }
    }
}
