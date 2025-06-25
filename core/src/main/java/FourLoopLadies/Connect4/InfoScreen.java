package FourLoopLadies.Connect4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class InfoScreen implements Screen {

    private final Game game;
    private Stage stage;
    private Texture backgroundTexture;

    public InfoScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(850, 1080));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("InfoScreen/background.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        Texture exitUp = new Texture(Gdx.files.internal("InfoScreen/exit_button_0.png"));
        Texture exitDown = new Texture(Gdx.files.internal("InfoScreen/exit_button_1.png"));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(exitUp));
        style.imageDown = new TextureRegionDrawable(new TextureRegion(exitDown));

        ButtonConfig exitConfig = new ButtonConfig("exit", "InfoScreen/exit_button_1.png", "InfoScreen/exit_button_0.png", 605.9f, 480.5f, 34.2f, 34.2f);
        ImageButton exitButton = exitConfig.createButton();
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameSettings.isSoundOn) GameSettings.clickSound.play();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(exitButton);
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
