package advisor;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class View {

    enum Actions { DEFAULT, FEATURED, NEWRLS, CATEGORIES, PLAYLISTS }

    static Actions currentAction = Actions.DEFAULT;
    private static int currentNewReleasesPageNumber = 0;
    private static int currentCategoryNamesPageNumber = 0;
    private static int currentPlaylistsPageNumber = 0;
    private static int currentFeaturedPageNumber = 0;

    public static void printAuthLink(String URI) {
        System.out.println("use this link to request the access code:\n" + URI);
        System.out.println("waiting for code...");
    }

    public static void printCategories(boolean firstUse, boolean nextPage, int len) {
        var categories = Controller.getCategories();
        double pageCount = Math.ceil((double) categories.size() / len);

        if (firstUse) {
            currentCategoryNamesPageNumber = 0;
        }

        if (nextPage) {
            if (currentCategoryNamesPageNumber >= categories.size()) {
                System.out.println("No more pages.");
                return;
            }
            currentCategoryNamesPageNumber += len;
        } else {
            if (currentCategoryNamesPageNumber - (len * 2) < 0) {
                System.out.println("No more pages.");
                return;
            }
            currentCategoryNamesPageNumber -= len;
        }

        for (int i = currentCategoryNamesPageNumber - len; i < currentCategoryNamesPageNumber && i < categories.size(); i++) {
            System.out.println(categories.get(i).getAsJsonObject().get("name").getAsString());
        }

        System.out.printf("---PAGE %d OF %.0f---%n", currentCategoryNamesPageNumber / len, pageCount);
    }

    public static void printPlaylists(String genre, boolean firstUse, boolean nextPage, int len) {
        var playlists = Controller.getPlaylist(genre);
        double pageCount = Math.ceil((double) playlists.size() / len);

        if (firstUse) {
            currentPlaylistsPageNumber = 0;
        }

        if (nextPage) {
            if (currentPlaylistsPageNumber >= playlists.size()) {
                System.out.println("No more pages.");
                return;
            }
            currentPlaylistsPageNumber += len;
        } else {
            if (currentPlaylistsPageNumber - (len * 2) < 0) {
                System.out.println("No more pages.");
                return;
            }
            currentPlaylistsPageNumber -= len;
        }

        for (int i = currentPlaylistsPageNumber - len; i < currentPlaylistsPageNumber && i < playlists.size(); i++) {
            System.out.println("\n" + playlists.get(i).getAsJsonObject().get("name").getAsString() + "\n" +
                    playlists.get(i).getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString());
        }

        System.out.printf("---PAGE %d OF %.0f---%n", currentPlaylistsPageNumber / len, pageCount);
    }

    public static void printFeatured(boolean firstUse, boolean nextPage, int len) {
        var featured = Controller.getFeatured();
        double pageCount = Math.ceil((double) featured.size() / len);

        if (firstUse) {
            currentFeaturedPageNumber = 0;
        }

        if (nextPage) {
            if (currentFeaturedPageNumber >= featured.size()) {
                System.out.println("No more pages.");
                return;
            }
            currentFeaturedPageNumber += len;
        } else {
            if (currentFeaturedPageNumber - (len * 2) < 0) {
                System.out.println("No more pages.");
                return;
            }
            currentFeaturedPageNumber -= len;
        }

        for (int i = currentFeaturedPageNumber - len; i < currentFeaturedPageNumber && i < featured.size(); i++) {
            System.out.println(featured.get(i).getAsJsonObject().get("name").getAsString() + "\n" +
                    featured.get(i).getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
        }

        System.out.printf("---PAGE %d OF %.0f---%n", currentFeaturedPageNumber / len, pageCount);
    }

    public static void printReleases(boolean firstUse, boolean nextPage, int len) {
        var releases = Controller.getNewReleases();
        List<String> artists = new ArrayList<>();
        double pageCount = Math.ceil((double) releases.size() / len);

        if (firstUse) {
            currentNewReleasesPageNumber = 0;
        }

        if (nextPage) {
            if (currentNewReleasesPageNumber >= releases.size()) {
                System.out.println("No more pages.");
                return;
            }
            currentNewReleasesPageNumber += len;
        } else {
            if (currentNewReleasesPageNumber - (len * 2) < 0) {
                System.out.println("No more pages.");
                return;
            }
            currentNewReleasesPageNumber -= len;
        }

        for (int i = currentNewReleasesPageNumber - len; i < currentNewReleasesPageNumber && i < releases.size(); i++) {

            for (JsonElement artist : releases.get(i).getAsJsonObject().get("artists").getAsJsonArray()) {
                artists.add(artist.getAsJsonObject().get("name").getAsString());
            }

            System.out.println("\n" + releases.get(i).getAsJsonObject().get("name").getAsString());
            System.out.println(artists.toString());
            System.out.println(releases.get(i).getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString());
            artists.clear();
        }

        System.out.printf("---PAGE %d OF %.0f---%n", currentNewReleasesPageNumber / len, pageCount);
    }
}
