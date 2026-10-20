import java.util.ArrayDeque;
import java.util.Deque;

/*
 * Extra credit file.
 * This file is separate from the main assignment and does not change the required output.
 *
 * Features included:
 * 1) Undo: uses a second stack of previous song ids
 * 2) Repeat mode: uses a boolean flag, not a circular linked list
 */
public class ExtraCredit {
    private static class CombinedMusicPlayer extends MusicPlayer {
        private boolean repeatOn = false;
        private final Deque<String> undoHistory = new ArrayDeque<>();

        @Override
        public Song play(String id) {
            Song before = nowPlaying();
            Song after = super.play(id);
            if (after != null && before != null) {
                undoHistory.push(before.getId());
            }
            return after;
        }

        @Override
        public Song next() {
            Song nextSong = super.next();
            if (nextSong == null && repeatOn && nowPlaying() != null) {
                return super.play(nowPlaying().getId());
            }
            return nextSong;
        }

        @Override
        public Song back() {
            Song before = nowPlaying();
            Song after = super.back();
            if (after != null && before != null) {
                undoHistory.push(before.getId());
            }
            return after;
        }

        public void setRepeat(boolean repeatOn) {
            this.repeatOn = repeatOn;
        }

        public boolean isRepeatOn() {
            return repeatOn;
        }

        public Song undo() {
            if (undoHistory.isEmpty()) {
                return null;
            }
            String previousId = undoHistory.pop();
            return super.play(previousId);
        }
    }

    private static String formatSong(Song song) {
        return song == null ? "null" : song.getTitle();
    }

    public static void main(String[] args) {
        CombinedMusicPlayer player = new CombinedMusicPlayer();

        System.out.println("--- 1. Build the playlist ---");
        System.out.println(player.addSong("s1", "Night Drive", "artist"));
        System.out.println(player.addSong("s2", "Slow Tide", "artist"));
        System.out.println(player.addSong("s3", "Copper Sky", "artist"));
        System.out.println(player.addSong("s4", "Static Bloom", "artist"));
        System.out.println(player.addSong("s5", "Low Orbit", "artist"));
        System.out.println(player.addSong("s6", "Glass Rivers", "artist"));
        System.out.println("duplicate s3: " + player.addSong("s3", "Copper Sky", "artist"));
        System.out.println("playlist: " + player.showPlaylist());

        System.out.println("--- 2. Look up songs ---");
        System.out.println("find s4: " + formatSong(player.findSong("s4")));
        System.out.println("find s9: " + formatSong(player.findSong("s9")));

        System.out.println("--- 3. Play and move forward ---");
        System.out.println("play s1: " + formatSong(player.play("s1")));
        System.out.println("play s9: " + formatSong(player.play("s9")));
        System.out.println("next: " + formatSong(player.next()));
        System.out.println("next: " + formatSong(player.next()));
        System.out.println("history: " + player.showHistory());

        System.out.println("--- 4. Up next ---");
        System.out.println(player.queueNext("s1"));
        System.out.println(player.queueLater("s5"));
        System.out.println(player.queueLater("s6"));
        System.out.println("queue s9: " + player.queueLater("s9"));
        System.out.println("up next: " + player.showUpNext());
        System.out.println("next: " + formatSong(player.next()));
        System.out.println("now playing: " + formatSong(player.nowPlaying()));
        System.out.println("history: " + player.showHistory());

        System.out.println("--- 5. Back button ---");
        System.out.println("back: " + formatSong(player.back()));
        System.out.println("up next: " + player.showUpNext());
        System.out.println("history: " + player.showHistory());

        System.out.println("--- 6. Remove songs ---");
        System.out.println("remove s5: " + player.removeSong("s5"));
        System.out.println("remove current (s3): " + player.removeSong("s3"));
        System.out.println("remove s9: " + player.removeSong("s9"));
        System.out.println("playlist: " + player.showPlaylist());
        System.out.println("up next: " + player.showUpNext());

        System.out.println("--- 7. Removed songs are skipped ---");
        System.out.println("next: " + formatSong(player.next()));
        System.out.println("next: " + formatSong(player.next()));
        System.out.println("next at end: " + formatSong(player.next()));
        System.out.println("now playing: " + formatSong(player.nowPlaying()));
        System.out.println("back: " + formatSong(player.back()));
        System.out.println("up next: " + player.showUpNext());

        System.out.println("--- 8. Play counts ---");
        System.out.println("s1: " + player.playCount("s1"));
        System.out.println("s3: " + player.playCount("s3"));
        System.out.println("s6: " + player.playCount("s6"));
        System.out.println("s5 (removed): " + player.playCount("s5"));

        System.out.println("--- 9. Empty player ---");
        CombinedMusicPlayer empty = new CombinedMusicPlayer();
        System.out.println("next: " + formatSong(empty.next()));
        System.out.println("back: " + formatSong(empty.back()));
        System.out.println("now playing: " + formatSong(empty.nowPlaying()));
        System.out.println("playlist: " + empty.showPlaylist());

        System.out.println("--- Extra credit: repeat mode ---");
        CombinedMusicPlayer repeatPlayer = new CombinedMusicPlayer();
        repeatPlayer.addSong("s1", "Night Drive", "artist");
        repeatPlayer.addSong("s2", "Slow Tide", "artist");
        repeatPlayer.addSong("s3", "Copper Sky", "artist");
        repeatPlayer.play("s1");
        repeatPlayer.setRepeat(true);
        System.out.println("repeat on: " + repeatPlayer.isRepeatOn());
        System.out.println("repeat next: " + formatSong(repeatPlayer.next()));

        System.out.println("--- Extra credit: undo ---");
        CombinedMusicPlayer undoPlayer = new CombinedMusicPlayer();
        undoPlayer.addSong("s1", "Night Drive", "artist");
        undoPlayer.addSong("s2", "Slow Tide", "artist");
        undoPlayer.addSong("s3", "Copper Sky", "artist");
        undoPlayer.play("s1");
        undoPlayer.play("s2");
        System.out.println("after second play: " + formatSong(undoPlayer.nowPlaying()));
        System.out.println("after undo: " + formatSong(undoPlayer.undo()));
    }
}
