package FourLoopLadies.Connect4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class SettingsScreen implements Screen {
    private final Game game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private TextField numberInput;

    private final String[] difficulties = {"easy", "normal", "hard"};
    private final float[] positionsY = {545.5f, 699.1f, 852.7f};

    public SettingsScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(850, 1080));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("SettingScreen/setting_background.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        BitmapFont font = new BitmapFont();

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.font.getData().setScale(2f);
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.cursor = skin.getDrawable("cursor");
        textFieldStyle.messageFont = font;
        textFieldStyle.messageFontColor = Color.WHITE;

        numberInput = new TextField(Integer.toString(GameSettings.numberOfTurns), textFieldStyle);
        numberInput.setMessageText("Turns (1-20)");
        numberInput.setPosition(331.8f, 1080 - 344.5f - 35.9f);
        numberInput.setSize(369.6f, 35.9f);
        numberInput.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        numberInput.setAlignment(Align.center);

        numberInput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String text = numberInput.getText();
                if (text.isEmpty()) return;

                try {
                    int val = Integer.parseInt(text);
                    if (val < 1) val = 1;
                    else if (val > 20) val = 20;
                    numberInput.setText(Integer.toString(val));
                    GameSettings.numberOfTurns = val;
                } catch (NumberFormatException e) {
                    numberInput.setText("3");
                    GameSettings.numberOfTurns = 3;
                }
            }
        });

        ButtonConfig exitConfig = new ButtonConfig("exit", "SettingScreen/exit_button_1.png", "SettingScreen/exit_button_0.png", 416.8f, 69.6f, 30f, 30f);
        ImageButton exitButton = exitConfig.createButton();
        stage.addActor(exitButton);
        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(numberInput);


        ButtonConfig settingConfig = new ButtonConfig("setting", "SettingScreen/setting_button_1.png", "SettingScreen/setting_button_0.png", 145.4f, 131.7f, 186.8f, 43.2f);
        ImageButton settingButton = settingConfig.createButton();
        stage.addActor(settingButton);
        settingButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new SettingsScreen(game));
            }
        });

        ButtonConfig player1Config = new ButtonConfig("player1", "SettingScreen/player1st_button_0.png", "SettingScreen/player1st_button_1.png", 330f, 131.7f, 186.8f, 43.2f);
        ImageButton player1Button = player1Config.createButton();
        stage.addActor(player1Button);
        player1Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new Player1Screen(game));
            }
        });

        ButtonConfig player2Config = new ButtonConfig("player2", "SettingScreen/player2nd_button_0.png", "SettingScreen/player2nd_button_1.png", 515.6f, 131.7f, 186.8f, 43.2f);
        ImageButton player2Button = player2Config.createButton();
        stage.addActor(player2Button);
        player2Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                if (!GameSettings.isPvP) return;
                game.setScreen(new Player2Screen(game));
            }
        });

        ButtonConfig openVoiceConfig = new ButtonConfig("openVoice", "SettingScreen/voice_on_button_1.png", "SettingScreen/voice_on_button_0.png", 291.5f, 224f, 76.9f, 76.2f);
        ImageButton openVoiceButton = openVoiceConfig.createButton();
        stage.addActor(openVoiceButton);
        openVoiceButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.isSoundOn = GameSettings.isSoundOn? false: true;
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                SubMenuScreen.updateVoiceButtons(openVoiceButton,"voice");
            }
        });

        ButtonConfig musicOnConfig = new ButtonConfig("openMusic", "SettingScreen/music_on_button_1.png", "SettingScreen/music_on_button_0.png", 449.9f, 224f, 76.9f, 76.2f);
        ImageButton musicOnButton = musicOnConfig.createButton();
        stage.addActor(musicOnButton);
        musicOnButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.isMusicOn = GameSettings.isMusicOn? false: true;
                MusicManager.updateMusicState();
                SubMenuScreen.updateVoiceButtons(musicOnButton, "music");
            }
        });


        ImageButton[] difficultyButtons = new ImageButton[difficulties.length];

        for (int i = 0; i < difficulties.length; i++) {
            final String level = difficulties[i];
            final float posY = positionsY[i];

            ButtonConfig config = new ButtonConfig(
                level,
                "SettingScreen/" + level + (level.equals(GameSettings.difficulty) ? "_button_1.png" : "_button_0.png"),
                "SettingScreen/" + level + (level.equals(GameSettings.difficulty) ? "_button_0.png" : "_button_1.png"),
                294.3f, posY, 262.7f, 95.7f
            );

            final ImageButton button = config.createButton();
            difficultyButtons[i] = button;
            stage.addActor(button);

            button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                    GameSettings.difficulty = level;
                    updateModeButtons(difficultyButtons, level);
                }
            });
        }
    }

    private void updateModeButtons(ImageButton[] buttons, String selectedLevel) {
        for (int i = 0; i < buttons.length; i++) {
            String level = difficulties[i];
            String suffix = level.equals(selectedLevel) ? "_button_1.png" : "_button_0.png";
            buttons[i].getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(
                new Texture(Gdx.files.internal("SettingScreen/" + level + suffix))
            ));
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

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        skin.dispose();
    }
}
