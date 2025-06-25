package FourLoopLadies.Connect4;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;

import java.util.Random;
import java.util.ArrayList;

public class GameScreen implements Screen {

    private final Game game;
    private Stage stage;
    private boolean gameEnded = false; // aynı ekranda birden fazla oyun oynamak için

    private Texture backgroundTexture;
    private Texture player1IconTexture;
    private Texture player2IconTexture;
    private Texture computerIconTexture;

    private Label player1ScoreLabel;
    private Label player2ScoreLabel;
    private Label turnLabel;
    private Skin skin;
    public int currentTurn = 1;


    private Texture player1disc, player2disc, emptySlotTexture;

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private Image[][] boardSlots = new Image[ROWS][COLS];
    private int[][] boardState = new int[ROWS][COLS];

    private BitmapFont font;
    private SpriteBatch batch;

    private boolean player1Turn;  // Kim oynuyor (true=player1, false=player2 veya bot)

    public GameScreen(Game game) {
        this.game = game;
        resetScores();
        // Initialize empty boardState
        for (int row = 0; row < ROWS; row++) {
            for (int col=0; col < COLS; col++) {
                this.boardState[row][col] = 0;
            }
        }
    }

    public GameScreen(Game game, int[][] boardState) {
        this.game = game;

        if (boardState != null) {
            for (int row = 0; row < ROWS; row++) {
                System.arraycopy(boardState[row], 0, this.boardState[row], 0, COLS);
            }
        } else {
            // Yeni oyun başlat
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    this.boardState[row][col] = 0;
                }
            }
        }
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(800, 1080));
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();
        font = new BitmapFont();

        player1Turn = GameSettings.isFirstPlayerStarting;

        backgroundTexture = new Texture(Gdx.files.internal("GameScreen/background.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        player1IconTexture = new Texture("GameScreen/profil_icon_" + (GameSettings.player1IconIndex + 1) + ".png");
        Image player1Image = new Image(player1IconTexture);
        player1Image.setPosition(250f, 1080f - 180.8f - 175.1f);
        stage.addActor(player1Image);
        addLabelBelow(player1Image, GameSettings.player1Name);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        // Player 1 skoru için label, sol üst
        player1ScoreLabel = new Label("", skin);
        player1ScoreLabel.setPosition(375f, 1080f-384.9f-20f);
        player1ScoreLabel.setFontScale(2f);
        stage.addActor(player1ScoreLabel);

        // Player 2 skoru için label, sağ üst
        player2ScoreLabel = new Label("", skin);
        player2ScoreLabel.setPosition(430f, 1080f-384.9f-20f);
        player2ScoreLabel.setFontScale(2f);
        stage.addActor(player2ScoreLabel);

        // Tur bilgisi için label, orta üst
        turnLabel = new Label("", skin);
        turnLabel.setPosition(150f, 1080f-110.2f-30f);
        turnLabel.setFontScale(2f);
        stage.addActor(turnLabel);

        updateLabels();

        if (GameSettings.isPvP) {
            player2IconTexture = new Texture("GameScreen/profil_icon_" + (GameSettings.player2IconIndex + 1) + ".png");
            Image player2Image = new Image(player2IconTexture);
            player2Image.setPosition(480f, 1080f - 180.8f - 175.1f);
            stage.addActor(player2Image);
            addLabelBelow(player2Image, GameSettings.player2Name);
        } else {
            computerIconTexture = new Texture("SettingScreen/computer_icon.png");
            Image computerImage = new Image(computerIconTexture);
            computerImage.setPosition(480f, 1080f - 180.8f - 175.1f);
            stage.addActor(computerImage);
            addLabelBelow(computerImage, "Computer");
        }

        // Menü butonu
        ButtonConfig subMenuConfig = new ButtonConfig("subMenu", "GameScreen/submenu_button_1.png", "GameScreen/submenu_button_1.png", 587f, 42.5f, 78.9f, 78.2f);
        ImageButton subMenuButton = subMenuConfig.createButton();
        stage.addActor(subMenuButton);

        subMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                int[][] savedState = saveBoardState(); // Tahtanın durumunu kaydet
                game.setScreen(new SubMenuScreen(game, savedState)); // SubMenuScreen'e geç
            }
        });

        // Taş görselleri
        player1disc = new Texture("GameScreen/" + GameSettings.player1StoneColor + "_stone.png");
        player2disc = new Texture("GameScreen/" + GameSettings.player2StoneColor + "_stone.png");
        emptySlotTexture = new Texture("GameScreen/empty_stone.png");

        float slotWidth = 60.6f;
        float slotHeight = 63.5f;
        float startX = 129.6f + 70f;
        float startY = 1080f - 530.6f - 63.5f;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Image slot = new Image(emptySlotTexture);
                float x = startX + col * slotWidth;
                float y = startY - row * slotHeight;
                slot.setBounds(x, y, slotWidth, slotHeight);
                boardSlots[row][col] = slot;
                // Remove boardState reset here to preserve saved state
                // boardState[row][col] = 0;
                stage.addActor(slot);
            }
        }


        // Restore board visuals according to saved boardState
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int player = boardState[row][col];
                if (player == 1) {
                    boardSlots[row][col].setDrawable(new TextureRegionDrawable(new TextureRegion(player1disc)));
                } else if (player == 2) {
                    boardSlots[row][col].setDrawable(new TextureRegionDrawable(new TextureRegion(player2disc)));
                } else {
                    boardSlots[row][col].setDrawable(new TextureRegionDrawable(new TextureRegion(emptySlotTexture)));
                }
            }
        }
        // Clickable columns
        for (int col = 0; col < COLS; col++) {
            Image clickArea = new Image();
            clickArea.setBounds(startX + col * slotWidth, startY - 5 * slotHeight, slotWidth, slotHeight * ROWS);
            final int clickedCol = col;
            clickArea.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (GameSettings.isSoundOn) GameSettings.dropSound.play();
                    if (player1Turn || GameSettings.isPvP) {
                        handleColumnClick(clickedCol);
                    }
                }
            });
            stage.addActor(clickArea);
        }
    }

    private void handleColumnClick(int col) {
        if (gameEnded) return; // Oyun sona ermişse tıklamayı işlememeliyiz

        for (int row = ROWS - 1; row >= 0; row--) {
            if (boardState[row][col] == 0) {
                int currentPlayer = player1Turn ? 1 : 2;
                boardState[row][col] = currentPlayer;

                TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(
                    currentPlayer == 1 ? player1disc : player2disc));
                boardSlots[row][col].setDrawable(drawable);

                if (checkWin(boardState, row, col, currentPlayer)) {
                    showWinMessage(currentPlayer);
                    disableBoard();
                    return;
                }

                if (isBoardFull()) {
                    showDrawMessage();
                    disableBoard();
                    return;
                }

                player1Turn = !player1Turn; // sıra değiştir

                // Eğer PvB modundaysak ve botun sırasıysa
                if (!GameSettings.isPvP && !player1Turn) {
                    makeBotMove();
                }

                updateLabels();
                break;
            }
        }
    }


    private void makeBotMove() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                int botMove;
                switch (GameSettings.difficulty) {
                    case "easy":
                        botMove = EasyBot.getMove(boardState);
                        if (GameSettings.isSoundOn) GameSettings.dropSound.play();
                        break;
                    case "normal":
                        botMove = MediumBot.getMove(boardState);
                        if (GameSettings.isSoundOn) GameSettings.dropSound.play();
                        break;
                    case "hard":
                        botMove = HardBot.getMove(boardState);
                        if (GameSettings.isSoundOn) GameSettings.dropSound.play();
                        break;
                    default:
                        botMove = MediumBot.getMove(boardState);
                        if (GameSettings.isSoundOn) GameSettings.dropSound.play();
                }

                handleColumnClick(botMove); // Gecikmeli hamle
            }
        }, 1.0f); // 1 saniye gecikme
    }

    private void addLabelBelow(Image image, String text) {
        LabelStyle style = new LabelStyle(font, Color.WHITE);
        Label label = new Label(text, style);
        label.setFontScale(1.5f);
        label.setPosition(image.getX() + 10, image.getY() - 60f);
        stage.addActor(label);
    }

    private static boolean checkWin(int[][] board, int row, int col, int player) {
        // 4 taş hizalama kontrolü - yatay, dikey, çapraz
        return (countContinuous(board, row, col, 0, 1, player) + countContinuous(board, row, col, 0, -1, player) - 1 >= 4) ||
            (countContinuous(board, row, col, 1, 0, player) + countContinuous(board, row, col, -1, 0, player) - 1 >= 4) ||
            (countContinuous(board, row, col, 1, 1, player) + countContinuous(board, row, col, -1, -1, player) - 1 >= 4) ||
            (countContinuous(board, row, col, 1, -1, player) + countContinuous(board, row, col, -1, 1, player) - 1 >= 4);
    }

    private boolean isBoardFull() {
        for (int col = 0; col < COLS; col++) {
            if (boardState[0][col] == 0) return false;
        }
        return true;
    }

    private void showWinMessage(int player) {
        String winner;
        if (player == 1) {
            GameSettings.player1Score++;
            winner = GameSettings.player1Name;
        } else {
            if (GameSettings.isPvP) {
                GameSettings.player2Score++;
                winner = GameSettings.player2Name;
            } else {
                GameSettings.player2Score++;
                winner = "Computer";
            }
        }

        System.out.println("Winner is: " + winner);
        gameEnded = true;
        nextTurnOrFinish();
    }

    private void showDrawMessage() {
        System.out.println("Game ended in a draw.");
        gameEnded = true;
        nextTurnOrFinish();
    }

    private void nextTurnOrFinish() {
        currentTurn++;
        updateLabels(); // Tur sayısını güncelle

        if (currentTurn > GameSettings.numberOfTurns) {
            // Tüm turlar bitti
            boolean isPlayer1Winner = false;
            GameSettings.isTie = false;

            if (GameSettings.player1Score > GameSettings.player2Score) {
                isPlayer1Winner = true;
            } else if (GameSettings.player2Score > GameSettings.player1Score) {
                isPlayer1Winner = false;
            } else {
                GameSettings.isTie = true;
            }
            game.setScreen(new ResultScreen(game, isPlayer1Winner));
        } else {
            resetBoard();  //Yeni tura geç
        }
    }
    public static void resetScores() {
        GameSettings.player1Score = 0;
        GameSettings.player2Score = 0;
    }
    private void updateLabels() {
        player1ScoreLabel.setText(GameSettings.player1Score);
        player2ScoreLabel.setText(GameSettings.player2Score);
        turnLabel.setText(currentTurn + " / " + GameSettings.numberOfTurns);
    }
    private void resetBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                boardState[row][col] = 0;
                boardSlots[row][col].setDrawable(new TextureRegionDrawable(new TextureRegion(emptySlotTexture)));
                boardSlots[row][col].setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);
            }
        }

        player1Turn = GameSettings.isFirstPlayerStarting;
        gameEnded = false;

        // Sırayı bir sonraki turda değiştirmek istersen:
        GameSettings.isFirstPlayerStarting = !GameSettings.isFirstPlayerStarting;
        player1Turn = GameSettings.isFirstPlayerStarting;

        // PvB modundaysa ve ilk hamle bot'a geçtiyse
        if (!GameSettings.isPvP && !player1Turn) {
            makeBotMove();
        }
        updateLabels();
    }

    private void disableBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                boardSlots[row][col].setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        font.dispose();
        backgroundTexture.dispose();
        player1IconTexture.dispose();
        if (player2IconTexture != null) player2IconTexture.dispose();
        if (computerIconTexture != null) computerIconTexture.dispose();
        player1disc.dispose();
        player2disc.dispose();
        emptySlotTexture.dispose();
    }

    private int[][] saveBoardState() {
        int[][] savedState = new int[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            System.arraycopy(boardState[row], 0, savedState[row], 0, COLS);
        }
        return savedState;
    }

    private static int evaluateBoard(int[][] board) {
        int score = 0;
        Random rand = new Random();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == 2) { // Bot
                    score += evaluatePosition(board, row, col, 2);
                } else if (board[row][col] == 1) { // Rakip
                    score -= evaluatePosition(board, row, col, 1);
                }
            }
        }
        score += rand.nextInt(31) - 15;

        return score;
    }

    private static int evaluatePosition(int[][] board, int row, int col, int player) {
        int score = 0;

        int[][] directions = {
            {0, 1},   // Yatay →
            {1, 0},   // Dikey ↓
            {1, 1},   // Çapraz ↘
            {1, -1}   // Çapraz ↙
        };

        for (int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];

            for (int offset = -3; offset <= 0; offset++) {
                int countPlayer = 0;
                int countEmpty = 0;
                boolean valid = true;

                for (int i = 0; i < 4; i++) {
                    int r = row + (offset + i) * dr;
                    int c = col + (offset + i) * dc;

                    if (r < 0 || r >= ROWS || c < 0 || c >= COLS) {
                        valid = false;
                        break;
                    }

                    if (board[r][c] == player) {
                        countPlayer++;
                    } else if (board[r][c] == 0) {
                        countEmpty++;
                    }
                }

                if (valid) {
                    if (countPlayer == 4) {
                        score += 1000;
                    } else if (countPlayer == 3 && countEmpty == 1) {
                        score += 50;
                    } else if (countPlayer == 2 && countEmpty == 2) {
                        score += 10;
                    }
                }
            }
        }

        return score;
    }

    static class EasyBot {
        private static final Random rand = new Random();

        static int getMove(int[][] board) {

            int winningMove = findWinningMove(board, 2);
            if (winningMove != -1) {
                return winningMove;
            }

            int blockingMove = findWinningMove(board, 1);
            if (blockingMove != -1) {
                return blockingMove;
            }

            int col;
            do {
                col = rand.nextInt(COLS);
            } while (board[0][col] != 0);
            return col;
        }

        private static int findWinningMove(int[][] board, int player) {
            for (int col = 0; col < COLS; col++) {
                int row = getAvailableRow(board, col);
                if (row != -1) {
                    board[row][col] = player;
                    if (checkWin(board, row, col, player)) {
                        board[row][col] = 0;
                        return col;
                    }
                    board[row][col] = 0;
                }
            }
            return -1;
        }

        private static int getAvailableRow(int[][] board, int col) {
            for (int row = ROWS - 1; row >= 0; row--) {
                if (board[row][col] == 0) {
                    return row;
                }
            }
            return -1;
        }


    }
    static class MediumBot {
        static int getMove(int[][] board) {
            int winningMove = findWinningMove(board);
            if (winningMove != -1) return winningMove;

            int defensiveMove = findDefensiveMove(board);
            if (defensiveMove != -1) return defensiveMove;

            return getBestScoringMove(board);
        }

        private static int findWinningMove(int[][] board) {
            for (int col = 0; col < COLS; col++) {
                int row = getAvailableRow(board, col);
                if (row != -1) {
                    board[row][col] = 2;
                    boolean win = checkWin(board, row, col, 2);
                    board[row][col] = 0;
                    if (win) return col;
                }
            }
            return -1;
        }

        private static int findDefensiveMove(int[][] board) {
            for (int col = 0; col < COLS; col++) {
                int row = getAvailableRow(board, col);
                if (row != -1) {
                    board[row][col] = 1;
                    boolean oppWin = checkWin(board, row, col, 1);
                    board[row][col] = 0;
                    if (oppWin) return col;
                }
            }
            return -1;
        }

        private static int getBestScoringMove(int[][] board) {
            int bestScore = Integer.MIN_VALUE;
            ArrayList<Integer> bestCols = new ArrayList<>();
            for (int col = 0; col < COLS; col++) {
                int row = getAvailableRow(board, col);
                if (row != -1) {
                    board[row][col] = 2;
                    int score = evaluateBoard(board);
                    board[row][col] = 0;
                    if (score > bestScore) {
                        bestScore = score;
                        bestCols.clear();
                        bestCols.add(col);
                    } else if (score == bestScore) {
                        bestCols.add(col);
                    }
                }
            }
            if (!bestCols.isEmpty()) {
                Random rand = new Random();
                return bestCols.get(rand.nextInt(bestCols.size()));
            }
            return -1;
        }

        private static int getAvailableRow(int[][] board, int col) {
            for (int row = ROWS - 1; row >= 0; row--) {
                if (board[row][col] == 0) return row;
            }
            return -1;
        }
    }

    static class HardBot {
        private static final int MAX_DEPTH = 6;

        static int getMove(int[][] board) {
            int bestScore = Integer.MIN_VALUE;
            ArrayList<Integer> bestMoves = new ArrayList<>();

            for (int col = 0; col < COLS; col++) {
                int row = getAvailableRow(board, col);
                if (row != -1) {
                    board[row][col] = 2;
                    int score = minimax(board, MAX_DEPTH - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    board[row][col] = 0;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMoves.clear();
                        bestMoves.add(col);
                    } else if (score == bestScore) {
                        bestMoves.add(col);
                    }
                }
            }

            if (!bestMoves.isEmpty()) {
                int index = new Random().nextInt(bestMoves.size());
                return bestMoves.get(index);
            }
            return -1;
        }

        private static int minimax(int[][] board, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
            if (depth == 0 || isTerminalNode(board)) {
                return evaluateBoard(board);
            }

            if (isMaximizingPlayer) {
                int maxEval = Integer.MIN_VALUE;
                for (int col = 0; col < COLS; col++) {
                    int row = getAvailableRow(board, col);
                    if (row != -1) {
                        board[row][col] = 2;
                        int eval = minimax(board, depth - 1, false, alpha, beta);
                        board[row][col] = 0;
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) break;
                    }
                }
                return maxEval;
            } else {
                int minEval = Integer.MAX_VALUE;
                for (int col = 0; col < COLS; col++) {
                    int row = getAvailableRow(board, col);
                    if (row != -1) {
                        board[row][col] = 1;
                        int eval = minimax(board, depth - 1, true, alpha, beta);
                        board[row][col] = 0;
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) break;
                    }
                }
                return minEval;
            }
        }

        private static boolean isTerminalNode(int[][] board) {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (board[row][col] != 0) {
                        if (checkWin(board, row, col, board[row][col])) {
                            return true;
                        }
                    }
                }
            }
            return isBoardFull(board);
        }

        private static boolean isBoardFull(int[][] board) {
            for (int col = 0; col < COLS; col++) {
                if (board[0][col] == 0) return false;
            }
            return true;
        }

        private static int getAvailableRow(int[][] board, int col) {
            for (int row = ROWS - 1; row >= 0; row--) {
                if (board[row][col] == 0) {
                    return row;
                }
            }
            return -1;
        }
    }

    private static int countContinuous(int[][] board, int row, int col, int deltaRow, int deltaCol, int player) {
        int count = 0;
        int r = row;
        int c = col;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == player) {
            count++;
            r += deltaRow;
            c += deltaCol;
        }
        return count;
    }
}
