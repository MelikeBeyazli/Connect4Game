package FourLoopLadies.Connect4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SubMenuScreen implements Screen {
    private final Game game;
    private Stage stage;
    private Texture backgroundTexture;
    private int[][] boardState;

    public SubMenuScreen(Game game, int[][] boardState) {
        this.game = game;
        this.boardState = boardState;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(850, 1080));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("SubMenuScreen/background.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        background.setTouchable(null);
        stage.addActor(background);

        // Buton konfigürasyonları
        ButtonConfig replayBtnCfg = new ButtonConfig("replay", "SubMenuScreen/replay_button_1.png", "SubMenuScreen/replay_button_0.png", 225.5f, 718.1f, 97.8f, 96.5f);
        ImageButton replayBtn = replayBtnCfg.createButton();
        stage.addActor(replayBtn);
        replayBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new GameScreen(game)); // Örnek olarak GameScreen'e geçiş
            }
        });

        ButtonConfig continueBtnCfg = new ButtonConfig("continue", "SubMenuScreen/contiune_button_1.png", "SubMenuScreen/contiune_button_0.png", 357.7f, 689.3f, 129.1f, 154.2f);
        ImageButton continueBtn = continueBtnCfg.createButton();
        stage.addActor(continueBtn);
        continueBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new GameScreen(game, boardState));
            }
        });

        ButtonConfig exitBtnCfg = new ButtonConfig("exit", "SubMenuScreen/exit_button_1.png", "SubMenuScreen/exit_button_0.png", 521.2f, 718.1f, 97.7f, 96.5f);
        ImageButton exitBtn = exitBtnCfg.createButton();
        stage.addActor(exitBtn);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new MainMenuScreen(game)); // Ana menüye dön
            }
        });

        ButtonConfig openVoiceConfig = new ButtonConfig("openVoice", "SettingScreen/voice_on_button_1.png", "SettingScreen/voice_on_button_0.png", 352.9f, 966.4f, 57.7f, 57.2f);
        ImageButton openVoiceButton = openVoiceConfig.createButton();
        stage.addActor(openVoiceButton);
        openVoiceButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.isSoundOn = GameSettings.isSoundOn? false: true;
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                updateVoiceButtons(openVoiceButton,"voice");
            }
        });

        ButtonConfig musicOnConfig = new ButtonConfig("openMusic", "SettingScreen/music_on_button_1.png", "SettingScreen/music_on_button_0.png", 439.4f, 966.4f, 57.7f, 57.2f);
        ImageButton musicOnButton = musicOnConfig.createButton();
        stage.addActor(musicOnButton);
        musicOnButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.isMusicOn = GameSettings.isMusicOn? false: true;
                MusicManager.updateMusicState();
                updateVoiceButtons(musicOnButton, "music");
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
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
    }
    public static void updateVoiceButtons(ImageButton voiceBtn, String btnName) {
        String base = "SettingScreen/"+btnName;
        if (btnName.equals("voice")) {
            voiceBtn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(base + (GameSettings.isSoundOn ? "_on" : "_off") + "_button_1.png"))));
            voiceBtn.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(base + (GameSettings.isSoundOn ? "_on" : "_off") + "_button_0.png"))));
        }
        else if (btnName.equals("music")) {
            voiceBtn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(base + (GameSettings.isMusicOn ? "_on" : "_off") + "_button_1.png"))));
            voiceBtn.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(base + (GameSettings.isMusicOn ? "_on" : "_off") + "_button_0.png"))));
        }
    }
}
