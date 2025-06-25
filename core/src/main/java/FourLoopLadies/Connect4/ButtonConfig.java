package FourLoopLadies.Connect4;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonConfig {
    public String name;
    public String upImagePath;
    public String downImagePath;
    public float x, y;
    public float width, height;

    public ButtonConfig(String name, String upImagePath, String downImagePath,
                        float x, float y, float width, float height) {
        this.name = name;
        this.upImagePath = upImagePath;
        this.downImagePath = downImagePath;
        this.x = x;
        this.y = 1080 - (y + height);
        this.width = width;
        this.height = height;
    }

    public ImageButton createButton() {
        Texture upTex = new Texture(Gdx.files.internal(upImagePath));
        Texture downTex = new Texture(Gdx.files.internal(downImagePath));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(upTex));
        style.imageDown = new TextureRegionDrawable(new TextureRegion(downTex));

        ImageButton button = new ImageButton(style);
        button.setSize(width, height);
        button.setPosition(x, y);
        button.setOrigin(button.getWidth() / 2, button.getHeight() / 2);
        return button;
    }
}

