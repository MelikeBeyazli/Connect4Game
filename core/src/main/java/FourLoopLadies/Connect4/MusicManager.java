package FourLoopLadies.Connect4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private static Music music;

    public static void init() {
        if (music == null) {
            music = Gdx.audio.newMusic(Gdx.files.internal("voices/forestismagic.mp3"));
            music.setLooping(true);
            if (GameSettings.isMusicOn) music.play();
        }
    }

    public static void updateMusicState() {
        if (music == null) return;

        if (GameSettings.isMusicOn) {
            if (!music.isPlaying()) music.play();
        } else {
            if (music.isPlaying()) music.pause(); // veya stop()
        }
    }

    public static void dispose() {
        if (music != null) music.dispose();
    }
}
