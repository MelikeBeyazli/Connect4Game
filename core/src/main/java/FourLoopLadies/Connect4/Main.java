package FourLoopLadies.Connect4;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    @Override
    public void create() {
        MusicManager.init();
        setScreen(new MainMenuScreen(this));
    }
}
