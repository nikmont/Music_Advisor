package advisor;

import java.util.*;

public class Main {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Map<String, String> cliArg = new HashMap<>();
        boolean isExit = false;
        boolean isAuthorized = false;
        String action;

        if (args.length >= 2) {
            for (int i = 0; i < args.length; i += 2) {
                cliArg.put(args[i], args[i + 1]);
            }
        }

        Controller.SERVER_PATH = cliArg.getOrDefault("-access", "https://accounts.spotify.com");
        Controller.API_PATH = cliArg.getOrDefault("-resource", "https://api.spotify.com");
        View.elemPerPage = Integer.parseInt(cliArg.getOrDefault("-page", "5"));

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
                        View.getFeatured();
                        break;
                    case "new":
                        View.newReleases();
                        break;
                    case "categories":
                        View.getCategories();
                        break;
                    case "playlists":
                        View.getPlaylists(scan.nextLine().trim());
                        break;
                    case "exit":
                        isExit = true;
                }
            }
        }
    }
}
