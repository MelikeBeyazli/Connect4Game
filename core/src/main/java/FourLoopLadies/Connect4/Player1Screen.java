package FourLoopLadies.Connect4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Player1Screen implements Screen {

    private final Game game;
    private Stage stage;
    private Skin skin;

    private Texture backgroundTexture;
    private TextField nameInput;

    private String playerName;
    private String selectedStone;
    private int selectedIconIndex;

    private final String[] stoneColors = {"red", "green", "blue", "yellow", "orange", "purple", "pink"};
    private final int iconCount = 8;

    private Array<ImageButton> stoneButtons;
    private Array<ImageButton> iconButtons;

    public Player1Screen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(850, 1080));
        Gdx.input.setInputProcessor(stage);

        // Load settings from GameSettings
        playerName = GameSettings.player1Name;
        selectedStone = GameSettings.player1StoneColor;
        selectedIconIndex = GameSettings.player1IconIndex;

        backgroundTexture = new Texture(Gdx.files.internal("SettingScreen/player_background.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        BitmapFont font = new BitmapFont();
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.font.getData().setScale(2f);
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.cursor = skin.getDrawable("cursor");
        textFieldStyle.messageFont = font;
        textFieldStyle.messageFontColor = Color.LIGHT_GRAY;

        nameInput = new TextField(playerName, textFieldStyle);
        nameInput.setMessageText("Player 1 Name");
        nameInput.setPosition(323.7f, 1080f - 39.3f-257.1f);
        nameInput.setSize(365.8f, 39.3f);
        nameInput.setAlignment(Align.center);
        stage.addActor(nameInput);

        nameInput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                playerName = nameInput.getText().isEmpty() ? "Player 1" : nameInput.getText();
                GameSettings.player1Name = playerName;
            }
        });

        stoneButtons = new Array<>();
        iconButtons = new Array<>();

        for (int i = 0; i < stoneColors.length; i++) {
            String color = stoneColors[i];
            final String buttonPath = "SettingScreen/" + color + "_stone_button";

            ButtonConfig buttonConfig = new ButtonConfig(color + "_button", buttonPath + "_1.png", buttonPath + "_0.png", 338 + i * 50, 346.5f, 42.9f, 45f);
            ImageButton button = buttonConfig.createButton();
            final String selectedColor = color;

            button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                    selectedStone = selectedColor;
                    if(selectedColor!= GameSettings.player2StoneColor) {
                        GameSettings.player1StoneColor = selectedColor;
                        updateStoneButtonVisuals();
                    }
                }
            });

            stoneButtons.add(button);
            stage.addActor(button);
        }

        for (int i = 0; i < iconCount; i++) {
            String iconPath = "SettingScreen/profil_icon_" + (i + 1) + ".png";
            ImageButton iconButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(iconPath)))));
            iconButton.setPosition(178.7f + (i % 4) * 130, 1080f -140f - 588.2f - (i / 4) * 150);

            final int index = i;
            iconButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                    selectedIconIndex = index;
                    if(selectedIconIndex!= GameSettings.player2IconIndex) {
                        GameSettings.player1IconIndex = index;
                        updateSelectedIconVisuals();
                    }
                }
            });

            iconButtons.add(iconButton);
            stage.addActor(iconButton);
        }

        updateStoneButtonVisuals();
        updateSelectedIconVisuals();

        ButtonConfig exitConfig = new ButtonConfig("exit", "SettingScreen/exit_button_1.png", "SettingScreen/exit_button_0.png", 416.8f, 69.6f, 30f, 30f);

        ButtonConfig settingConfig = new ButtonConfig("setting", "SettingScreen/setting_button_0.png", "SettingScreen/setting_button_1.png", 145.4f, 131.7f, 186.8f, 43.2f);
        ButtonConfig player1Config = new ButtonConfig("player1", "SettingScreen/player1st_button_1.png", "SettingScreen/player1st_button_0.png", 330f, 131.7f, 186.8f, 43.2f);
        ButtonConfig player2Config = new ButtonConfig("player2", "SettingScreen/player2nd_button_0.png", "SettingScreen/player2nd_button_1.png", 515.6f, 131.7f, 186.8f, 43.2f);

        ImageButton exitButton = exitConfig.createButton();
        ImageButton settingButton = settingConfig.createButton();
        ImageButton player1Button = player1Config.createButton();
        ImageButton player2Button = player2Config.createButton();

        stage.addActor(exitButton);
        stage.addActor(settingButton);
        stage.addActor(player1Button);
        stage.addActor(player2Button);

        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        settingButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new SettingsScreen(game));
            }
        });

        player1Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new Player1Screen(game));
            }
        });

        player2Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                if (!GameSettings.isPvP) return;
                game.setScreen(new Player2Screen(game));
            }
        });
    }

    private void updateStoneButtonVisuals() {
        for (int i = 0; i < stoneButtons.size; i++) {
            ImageButton button = stoneButtons.get(i);
            String color = stoneColors[i];
            String suffix = color.equals(selectedStone) ? "_stone_button_1.png" : "_stone_button_0.png";
            button.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("SettingScreen/" + color + suffix))));
        }
    }

    private void updateSelectedIconVisuals() {
        for (int i = 0; i < iconButtons.size; i++) {
            ImageButton button = iconButtons.get(i);
            button.setColor(1, 1, 1, i == selectedIconIndex ? 0.5f : 1f);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
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
