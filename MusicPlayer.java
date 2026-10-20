// Nicole Shmerkin
// Java
// VS Code

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MusicPlayer {
    private final LinkedList<Song> playlist = new LinkedList<>(); // keeps the songs in order
    private final Map<String, Song> library = new HashMap<>(); // finds songs by unique ID
    private final Deque<Song> upNext = new ArrayDeque<>(); // stores queued songs to play next
    private final Deque<Song> history = new ArrayDeque<>(); // remembers recently played songs
    private final Map<String, Integer> playCounts = new HashMap<>(); // tracks how many times each song was played
    private Song current; // song currently playing

    public boolean addSong(String id, String title, String artist) { // adds a song if the ID is new
        if (library.containsKey(id)) {
            return false;
        }

        Song song = new Song(id, title, artist);
        playlist.addLast(song);
        library.put(id, song);
        return true;
    }

    public Song findSong(String id) { // returns a song by ID or null
        return library.get(id);
    }

    public List<String> showPlaylist() { // shows all playlist song titles
        List<String> titles = new ArrayList<>();
        for (Song song : playlist) {
            titles.add(song.getTitle());
        }
        return titles;
    }

    private void startSong(Song song) { // sets the current song and counts a play
        if (song == null) {
            return;
        }
        current = song;
        playCounts.put(song.getId(), playCounts.getOrDefault(song.getId(), 0) + 1);
    }

    public Song play(String id) { // plays a song by ID and records it in history
        Song song = library.get(id);
        if (song == null) {
            return null;
        }

        if (current != null) {
            history.addFirst(current);
        }

        startSong(song);
        return song;
    }

    public Song nowPlaying() { // returns the currently playing song
        return current;
    }

    public int playCount(String id) { // returns how many times a song has been played
        return playCounts.getOrDefault(id, 0);
    }

    public boolean queueLater(String id) { // adds a song to the end of the queue
        Song song = library.get(id);
        if (song == null) {
            return false;
        }
        upNext.addLast(song);
        return true;
    }

    public boolean queueNext(String id) { // adds a song to the front of the queue
        Song song = library.get(id);
        if (song == null) {
            return false;
        }
        upNext.addFirst(song);
        return true;
    }

    public List<String> showUpNext() { // shows songs waiting to play
        List<String> titles = new ArrayList<>();
        for (Song song : upNext) {
            if (library.containsKey(song.getId())) {
                titles.add(song.getTitle());
            }
        }
        return titles;
    }

    public Song next() { // plays the next queued or playlist song
        Song nextSong = null;

        if (!upNext.isEmpty()) {
            while (!upNext.isEmpty()) {
                Song candidate = upNext.removeFirst();
                if (library.containsKey(candidate.getId())) {
                    nextSong = candidate;
                    break;
                }
            }
        } else if (current != null) {
            int index = playlist.indexOf(current);
            if (index >= 0 && index + 1 < playlist.size()) {
                nextSong = playlist.get(index + 1);
            }
        }

        if (nextSong == null) {
            return null;
        }

        if (current != null) {
            history.addFirst(current);
        }

        startSong(nextSong);
        return nextSong;
    }

    public Song back() { // goes back to the previous song in history
        if (current == null) {
            return null;
        }

        Song previousSong = null;
        while (!history.isEmpty()) {
            Song candidate = history.removeFirst();
            if (library.containsKey(candidate.getId())) {
                previousSong = candidate;
                break;
            }
        }

        if (previousSong == null) {
            return null;
        }

        Song oldCurrent = current;
        upNext.addFirst(oldCurrent);
        startSong(previousSong);
        return previousSong;
    }

    public List<String> showHistory() { // displays previously played songs
        List<String> titles = new ArrayList<>();
        for (Song song : history) {
            if (library.containsKey(song.getId())) {
                titles.add(song.getTitle());
            }
        }
        return titles;
    }

    public boolean removeSong(String id) { // removes a song from the library and playlist
        Song song = library.get(id);
        if (song == null) {
            return false;
        }

        if (current != null && current.getId().equals(id)) {
            return false;
        }

        playlist.remove(song);
        library.remove(id);
        playCounts.remove(id);
        return true;
    }
}
