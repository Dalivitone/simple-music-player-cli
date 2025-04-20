import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private ArrayList<Song> songs;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
        this.songs = new ArrayList<>();
    }

    public void start() {
        printWelcome();
        while(true) {
            options();
            int number = Integer.valueOf(scanner.nextLine());
            if(number == 4) {
                break;
            }
            handleInput(number);
        }

    }

    private void printWelcome() {
        System.out.println(Colours.boldColorize("+----------------------------------------+", Colours.PURPLE));
        System.out.println(Colours.boldColorize("|             MUSIC PLAYER CLI            ", Colours.PURPLE));
        System.out.println(Colours.boldColorize("+----------------------------------------+", Colours.PURPLE));
        System.out.println();    
    }

    private void options() {
        System.out.println(Colours.bold("MAIN MENU:"));
        System.out.println(Colours.colorize("1", Colours.GREEN) + " - Load songs from folder");
        System.out.println(Colours.colorize("2", Colours.GREEN) + " - List all songs");
        System.out.println(Colours.colorize("3", Colours.GREEN) + " - ️ Play a song");
        System.out.println(Colours.colorize("4", Colours.GREEN) + " - Quit");
        System.out.println();
    }

    private void handleInput(int number) {
        if(number == 3) {
            playSong();
        } else if(number == 1) {
            loadFolder();
        } else if(number == 2) {
            System.out.println(Colours.bold("\n SONG LIBRARY"));
            if(songs.isEmpty()) {
                System.out.println(Colours.colorize("No songs in library. Use option 1 to load songs.", Colours.YELLOW));
                return;
            }
            listSongs();
        }
    }

    private void playSong() {
        if(!songs.isEmpty()) {
            listSongs();
            System.out.println(Colours.bold("Choose what song to play:"));
            int choice = Integer.valueOf(scanner.nextLine());
            Song song = songs.get(choice - 1);
            song.start();
            System.out.println(Colours.colorize("Now playing: ", Colours.CYAN) + song);
            songsOptions(song);
        } else {
            System.out.println(Colours.YELLOW + " Error: No songs." + Colours.RESET);
        }
    }

    private void loadFolder() {
        try {
            System.out.println("Write a folder: ");
            String path = scanner.nextLine();
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.getName().endsWith(".wav")) {
                    String title = file.getName();
                    Song song = new Song(title, file);
                    songs.add(song);
                }
            }
        } catch(Exception e) {
            System.out.println(Colours.YELLOW + " Error: Invalid directory path." + Colours.RESET);
        }
    }

    private void listSongs() {
        for(int i = 0; i < songs.size(); i++) {
            System.out.println(Colours.colorize((i + 1) + ": ", Colours.CYAN) + songs.get(i));
        }
    }

    private Song nextTrack(Song song) {
        int currentIndex = songs.indexOf(song);
        song.stop();
        currentIndex++;
        if(currentIndex < songs.size()) {
            return getSong(currentIndex);
        } else {
            System.out.println("Reached the end");
            return null;
        }
    }

    private Song getSong(int currentIndex) {
        Song next = songs.get(currentIndex);
        next.start();
        System.out.println(Colours.colorize("Now playing: ", Colours.CYAN) + next);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        next.status();
        return next;
    }

    private Song previousTrack(Song song) {
        int currentIndex = songs.indexOf(song);
        song.stop();
        currentIndex--;
        if(currentIndex >= 0) {
            return getSong(currentIndex);
        } else {
            System.out.println("Reached the end");
            return null;
        }
    }

    private void songsOptions(Song song) {
        System.out.println(Colours.bold("\nPLAYER CONTROLS"));
        song.status();
        while(true) {
            System.out.println("\n" +
                    Colours.colorize("[s]", Colours.GREEN) + " Stop | " +
                    Colours.colorize("[r]", Colours.GREEN) + " Resume | " +
                    Colours.colorize("[f]", Colours.GREEN) + " Skip forward 5s | " +
                    Colours.colorize("[b]", Colours.GREEN) + " Skip back 5s | " +
                    Colours.colorize("[n]", Colours.GREEN) + " Next track | " +
                    Colours.colorize("[p]", Colours.GREEN) + " Previous track | " +
                    Colours.colorize("[x]", Colours.GREEN) + " Exit to menu");
            String command = scanner.nextLine();
            if(command.equals("s")) {
                song.pause();
                System.out.println(Colours.colorize(" Stopped", Colours.BLUE));
            } else if(command.equals("r")) {
                song.resume();
                System.out.println(Colours.colorize("  Resumed", Colours.GREEN));
                System.out.println(song);
            } else if(command.equals("x")) {
                song.stop();
                System.out.println(Colours.colorize("  Stopped playback", Colours.PURPLE));
                break;
            } else if(command.equals("f")) {
                song.skipForward();
                System.out.println(Colours.colorize(" Skipped forward 5 seconds", Colours.BLUE));
                System.out.println(song);
            } else if(command.equals("b")) {
                song.skipBack();
                System.out.println(Colours.colorize(" Skipped back 5 seconds", Colours.BLUE));
                System.out.println(song);
            } else if(command.equals("n")) {
                Song next = nextTrack(song);
                if(next != null) {
                    song = next;
                }
            } else if(command.equals("p")) {
                Song previous = previousTrack(song);
                if(previous != null) {
                    song = previous;
                }
            }
            else {
                System.out.println(Colours.colorize("Wrong command!", Colours.YELLOW));
            }
            System.out.println();
        }
    }
}
