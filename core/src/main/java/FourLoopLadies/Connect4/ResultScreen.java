package FourLoopLadies.Connect4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ResultScreen implements Screen {

    private final Game game;
    private final boolean isPlayer1Winner;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture exitTexture;
    private Texture winnerIconTexture;
    private String winnerName;
    private Texture player1IconTexture;
    private Texture player2IconTexture;

    public ResultScreen(Game game, boolean isPlayer1Winner) {
        this.game = game;
        this.isPlayer1Winner = isPlayer1Winner;

        backgroundTexture = new Texture( "ResultScreen/" + (GameSettings.isTie? "draw" : "win") + "_background.png");

        player1IconTexture = new Texture("GameScreen/profil_icon_" + (GameSettings.player1IconIndex + 1) + ".png");

        if (GameSettings.isPvP) {
            player2IconTexture = new Texture("GameScreen/profil_icon_" + (GameSettings.player2IconIndex + 1) + ".png");
        } else {
            player2IconTexture = new Texture("SettingScreen/computer_icon.png");
        }
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(850, 1080));
        Gdx.input.setInputProcessor(stage);

        // Yazı stili
        BitmapFont font = new BitmapFont();
        LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);


        ButtonConfig exitConfig = new ButtonConfig("exit", "SettingScreen/exit_button_1.png", "SettingScreen/exit_button_0.png", 416.8f, 69.6f, 30f, 30f);
        ImageButton exitButton = exitConfig.createButton();
        stage.addActor(exitButton);
        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        if(GameSettings.isTie) {
            // 1. oyuncu ikonu
            Image player1Icon = new Image(player1IconTexture);
            player1Icon.setSize(206.8f, 310.7f);
            player1Icon.setPosition(154.6f, 1080f - 352.6f - 310.7f);
            stage.addActor(player1Icon);

            // 2. oyuncu ikonu
            Image player2Icon = new Image(player2IconTexture);
            player2Icon.setSize(206.8f, 310.7f);
            player2Icon.setPosition(483.8f, 1080f - 352.6f - 310.7f);
            stage.addActor(player2Icon);

            // 1. oyuncu ismi
            Label player1Label = new Label(GameSettings.player1Name, labelStyle);
            player1Label.setSize(239.5f, 61.2f);
            player1Label.setFontScale(3f);
            player1Label.setPosition(132.7f, 1080f - 732.8f - 61.2f);
            stage.addActor(player1Label);

            // 2. oyuncu ismi
            String player2Name = GameSettings.isPvP ? GameSettings.player2Name : "Computer";
            Label player2Label = new Label(player2Name, labelStyle);
            player2Label.setSize(239.5f, 61.2f);
            player2Label.setFontScale(3f);
            player2Label.setPosition(459.2f, 1080f - 730.8f - 61.2f);
            stage.addActor(player2Label);
        }
        else {
            // Kazanan ikon
            Image winnerIcon = new Image(isPlayer1Winner? player1IconTexture : player2IconTexture );
            winnerIcon.setPosition(335.7f, 1080f-310.7f-363f);
            winnerIcon.setSize(206.8f, 310.7f);
            stage.addActor(winnerIcon);
            if( GameSettings.isPvP )
            winnerName = isPlayer1Winner? GameSettings.player1Name : GameSettings.player2Name;
            else winnerName = isPlayer1Winner? GameSettings.player1Name : "Computer";

            Label nameLabel = new Label(winnerName, labelStyle);
            nameLabel.setSize(239.5f, 61.2f);
            nameLabel.setFontScale(3f); // Daha büyük yazı
            nameLabel.setPosition(311f, 1080f - 732.8f - 61.2f); // Daha sağda
            stage.addActor(nameLabel);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1,1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        exitTexture.dispose();
        winnerIconTexture.dispose();
    }
}
