package advisor;

import java.util.*;

public class Main {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Map<String, String> cliArg = new HashMap<>();
        boolean isExit = false;
        boolean isAuthorized = false;
        String action;
        String genre = "";
        int elemPerPage;

        if (args.length >= 2) {
            for (int i = 0; i < args.length; i += 2) {
                cliArg.put(args[i], args[i + 1]);
            }
        }

        Controller.SERVER_PATH = cliArg.getOrDefault("-access", "https://accounts.spotify.com");
        Controller.API_PATH = cliArg.getOrDefault("-resource", "https://api.spotify.com");
        elemPerPage = Integer.parseInt(cliArg.getOrDefault("-page", "5"));

        while (!isExit) {
            action = scan.next();
            if (!isAuthorized && !"auth".equals(action) && !"exit".equals(action)) {
                System.out.println("Please, provide access for application.");
            } else {
                switch (action) {
                    case "auth":
                        Controller.getCode();
                        Controller.getToken();
                        isAuthorized = true;
                        break;
                    case "featured":
                        View.currentAction = View.Actions.FEATURED;
                        View.printFeatured(true, true, elemPerPage);
                        break;
                    case "new":
                        View.currentAction = View.Actions.NEWRLS;
                        View.printReleases(true, true, elemPerPage);
                        break;
                    case "categories":
                        View.currentAction = View.Actions.CATEGORIES;
                        View.printCategories(true, true, elemPerPage);
                        break;
                    case "playlists":
                        View.currentAction = View.Actions.PLAYLISTS;
                        genre = scan.nextLine().trim();
                        View.printPlaylists(genre, true, true, elemPerPage);
                        break;
                    case "next":
                        if (!(View.currentAction == View.Actions.DEFAULT)) {
                            switch (View.currentAction) {
                                case NEWRLS:
                                    View.printReleases(false, true, elemPerPage);
                                    break;
                                case FEATURED:
                                    View.printFeatured(false, true, elemPerPage);
                                    break;
                                case PLAYLISTS:
                                    View.printPlaylists(genre, false, true, elemPerPage);
                                    break;
                                case CATEGORIES:
                                    View.printCategories(false, true, elemPerPage);
                                    break;
                                default:
                                    System.out.println("Error at next page");
                            }
                        }
                        break;
                    case "prev":
                        if (!(View.currentAction == View.Actions.DEFAULT)) {
                            switch (View.currentAction) {
                                case NEWRLS:
                                    View.printReleases(false, false, elemPerPage);
                                    break;
                                case FEATURED:
                                    View.printFeatured(false, false, elemPerPage);
                                    break;
                                case PLAYLISTS:
                                    View.printPlaylists(genre, false, false, elemPerPage);
                                    break;
                                case CATEGORIES:
                                    View.printCategories(false, false, elemPerPage);
                                    break;
                                default:
                                    System.out.println("Error at prev page");
                            }
                        }
                        break;
                    case "exit":
                        isExit = true;
                }
            }
        }
    }
}
