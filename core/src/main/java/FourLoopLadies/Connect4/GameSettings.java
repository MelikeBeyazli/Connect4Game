package FourLoopLadies.Connect4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class GameSettings {
    public static Boolean isPvP = true;
    public static Boolean isTie = false;
    public static int numberOfTurns = 3; // toplam tur sayısı
    public static String difficulty = "normal"; // overall difficulty

    public static String player1Name = "Player 1";
    public static String player1StoneColor = "red";
    public static int player1IconIndex = 0;

    public static String player2Name = "Player 2";
    public static String player2StoneColor = "blue";
    public static int player2IconIndex = 1;
    public static int currentTurn = 1;

    // Yeni ekleme: skorlar
    public static int player1Score = 0;
    public static int player2Score = 0;

    // isFirstPlayerStarting ekle (istersen)
    public static boolean isFirstPlayerStarting = true;

    public static Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("voices/stone_dropping_voice.mp3"));;
    public static Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("voices/click.wav"));

    public static boolean isSoundOn = true;
    public static boolean isMusicOn = true;

    public static void reset() {
        isSoundOn = true;
        isPvP = true;
        numberOfTurns = 3;
        difficulty = "normal";

        player1Name = "Player 1";
        player1StoneColor = "red";
        player1IconIndex = 0;

        player2Name = "Player 2";
        player2StoneColor = "blue";
        player2IconIndex = 1;

        player1Score = 0;
        player2Score = 0;

        isFirstPlayerStarting = true;
    }
}
