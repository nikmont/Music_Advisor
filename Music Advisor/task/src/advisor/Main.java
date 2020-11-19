package advisor;

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
                    exit();
                    return;
            }
        }
    }
}

    public static void authorization(Server server) {
        server.getCode();

        server.getToken();
    }

    public static void getPlaylists() {
        String playlistName = scan.next();
        System.out.println("---" + playlistName.toUpperCase() + " PLAYLISTS---\n" +
                "Walk Like A Badass  \n" +
                "Rage Beats  \n" +
                "Arab Mood Booster  \n" +
                "Sunday Stroll");
    }

    public static void getCategories() {
        System.out.println("---CATEGORIES---\n" +
                "Top Lists\n" +
                "Pop\n" +
                "Mood\n" +
                "Latin");
    }

    public static void newReleases() {
        System.out.println("---NEW RELEASES---\n" +
                "Mountains [Sia, Diplo, Labrinth]\n" +
                "Runaway [Lil Peep]\n" +
                "The Greatest Show [Panic! At The Disco]\n" +
                "All Out Life [Slipknot]");
    }

    public static void getFeatured() {
        System.out.println("---FEATURED---\n" +
                "Mellow Morning\n" +
                "Wake Up and Smell the Coffee\n" +
                "Monday Motivation\n" +
                "Songs to Sing in the Shower");
    }

    public static void exit() {
        System.out.println("---GOODBYE!---");
    }
}
