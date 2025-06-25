package FourLoopLadies.Connect4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
    private final Game game;

    public MainMenuScreen(Game game) {
        this.game = game;
    }

    private Stage stage;
    private ImageButton pvpButton;
    private ImageButton pvbButton;

    private boolean isAnimating = false;
    private boolean isPvpActive = GameSettings.isPvP;

    private Texture backgroundTexture;

    private final float CENTER_X = 850 / 2f ;
    private final float CENTER_Y = 821.7f;
    private final float RADIUS = 60f;

    private float pvpAngle;
    private float pvbAngle;

    @Override
    public void show() {
        stage = new Stage(new FitViewport(850, 1080));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("MainMenuScreen/background.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Play, settings ve info butonları
        ButtonConfig playButtonConfig = new ButtonConfig("play", "MainMenuScreen/play_button_1.png", "MainMenuScreen/play_button_0.png", 337.7f, 438.5f, 159.8f, 190.8f);
        ImageButton playButton = playButtonConfig.createButton();
        stage.addActor(playButton);

        ButtonConfig settingsButtonConfig = new ButtonConfig("settings", "MainMenuScreen/setting_button_1.png", "MainMenuScreen/setting_button_0.png", 683.7f, 53f, 66.9f, 66.2f);
        ImageButton settingsButton = settingsButtonConfig.createButton();
        stage.addActor(settingsButton);

        ButtonConfig infoButtonConfig = new ButtonConfig("info", "MainMenuScreen/info_button_1.png", "MainMenuScreen/info_button_0.png", 599.3f, 53f, 66.9f, 66.2f);
        ImageButton infoButton = infoButtonConfig.createButton();
        stage.addActor(infoButton);

        // PvP / PvB butonları
        final Texture pvpUpActive = new Texture(Gdx.files.internal("MainMenuScreen/PvP_button_1.png"));
        final Texture pvpUpInactive = new Texture(Gdx.files.internal("MainMenuScreen/PvP_button_0.png"));

        final Texture pvbUpActive = new Texture(Gdx.files.internal("MainMenuScreen/PvB_button_1.png"));
        final Texture pvbUpInactive = new Texture(Gdx.files.internal("MainMenuScreen/PvB_button_0.png"));

        pvpButton = createButton(pvpUpActive, pvpUpInactive, isPvpActive);
        pvbButton = createButton(pvbUpActive, pvbUpInactive, !isPvpActive);

        if (isPvpActive) {
            pvpAngle = 270f;
            pvbAngle = 90f;
            positionButtonOnCircle(pvpButton, pvpAngle, 1.2f);
            positionButtonOnCircle(pvbButton, pvbAngle, 0.75f);
            pvpButton.toFront();
        } else {
            pvpAngle = 90f;
            pvbAngle = 270f;
            positionButtonOnCircle(pvpButton, pvpAngle, 0.75f);
            positionButtonOnCircle(pvbButton, pvbAngle, 1.2f);
            pvbButton.toFront();
        }

        // Butonlara tıklama olayları
        pvpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                if (isAnimating || isPvpActive) return;
                rotateToActive(true, pvpUpActive, pvpUpInactive, pvbUpActive, pvbUpInactive);
                GameSettings.isPvP = true; // PvP modunu aktif et
            }
        });

        pvbButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                if (isAnimating || !isPvpActive) return;
                rotateToActive(false, pvpUpActive, pvpUpInactive, pvbUpActive, pvbUpInactive);
 		GameSettings.isPvP = false; // PvB modunu aktif et
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                GameSettings.isPvP = isPvpActive;
                game.setScreen(new SettingsScreen(game));
            }
        });

        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new InfoScreen(game));
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new GameScreen(game));
            }
        });

        stage.addActor(pvbButton);
        stage.addActor(pvpButton);
    }

    private ImageButton createButton(Texture activeTexture, Texture inactiveTexture, boolean active) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(active ? activeTexture : inactiveTexture));
        style.imageDown = new TextureRegionDrawable(new TextureRegion(active ? activeTexture : inactiveTexture));
        ImageButton button = new ImageButton(style);
        button.setSize(activeTexture.getWidth(), activeTexture.getHeight());
        button.setOrigin(button.getWidth() / 2, button.getHeight() / 2);
        return button;
    }

    private void updateButtonTexture(ImageButton button, Texture activeTexture, Texture inactiveTexture, boolean active) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(active ? activeTexture : inactiveTexture));
        style.imageDown = new TextureRegionDrawable(new TextureRegion(active ? activeTexture : inactiveTexture));
        button.setStyle(style);
    }

    private void positionButtonOnCircle(ImageButton button, float angleDegrees, float scale) {
        float rad = (float) Math.toRadians(angleDegrees);
        float x = CENTER_X + RADIUS * (float)Math.cos(rad) - button.getWidth() / 2;
        float y = 1080 - CENTER_Y + RADIUS * (float)Math.sin(rad) - button.getHeight();
        button.setPosition(x, y);
        button.setScale(scale);
        button.setOrigin(button.getWidth() / 2, button.getHeight() / 2);
    }

    private void rotateToActive(final boolean activatePvp,
                                final Texture pvpUpActive, final Texture pvpUpInactive,
                                final Texture pvbUpActive, final Texture pvbUpInactive) {
        isAnimating = true;

        float targetPvpAngle = activatePvp ? 270f : 90f;
        float targetPvbAngle = activatePvp ? 90f : 270f;
        float duration = 0.4f;

        pvpButton.addAction(Actions.sequence(
            Actions.moveTo(
                CENTER_X + RADIUS * (float)Math.cos(Math.toRadians(targetPvpAngle)) - pvpButton.getWidth() / 2,
                1080 - CENTER_Y + RADIUS * (float)Math.sin(Math.toRadians(targetPvpAngle)) - pvpButton.getHeight(),
                duration
            ),
            Actions.run(() -> pvpAngle = targetPvpAngle)
        ));

        pvbButton.addAction(Actions.sequence(
            Actions.moveTo(
                CENTER_X + RADIUS * (float)Math.cos(Math.toRadians(targetPvbAngle)) - pvbButton.getWidth() / 2,
                1080 - CENTER_Y + RADIUS * (float)Math.sin(Math.toRadians(targetPvbAngle)) - pvbButton.getHeight(),
                duration
            ),
            Actions.run(() -> pvbAngle = targetPvbAngle)
        ));

        if (activatePvp) {
            pvpButton.addAction(Actions.scaleTo(1.2f, 1.2f, duration));
            pvbButton.addAction(Actions.scaleTo(0.75f, 0.75f, duration));
            pvpButton.toFront();
        } else {
            pvbButton.addAction(Actions.scaleTo(1.2f, 1.2f, duration));
            pvpButton.addAction(Actions.scaleTo(0.75f, 0.75f, duration));
            pvbButton.toFront();
        }

        updateButtonTexture(pvpButton, pvpUpActive, pvpUpInactive, activatePvp);
        updateButtonTexture(pvbButton, pvbUpActive, pvbUpInactive, !activatePvp);

        isPvpActive = activatePvp;

        stage.addAction(Actions.sequence(
            Actions.delay(duration),
            Actions.run(() -> isAnimating = false)
        ));
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
    }
}
