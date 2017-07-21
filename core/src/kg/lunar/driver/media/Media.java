package kg.lunar.driver.media;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class Media {
    private AssetManager assetManager;

    public Media(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void playSound(String name){
        Sound sound = assetManager.get("sounds/"+name,Sound.class);
        sound.play();
    }
}
