package kg.lunar.driver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.I18NBundle;
import com.boontaran.games.StageGame;

import java.util.Locale;

public class LunarDriver extends Game {

    public static final int SHOW_BANNER = 1;
    public static final int HIDE_BANNER = 2;
    public static final int LOAD_INTERSTITIAL = 3;
    public static final int SHOW_INTERSTITIAL = 4;
    public static final int OPEN_MARKET = 5;
    public static final int SHARE = 6;

    private boolean loadingAssets = false;
    private AssetManager assetManager;

    public static TextureAtlas atlas;
    public static BitmapFont font;

    private I18NBundle bundle;
    private String path_to_atlas;
    private GameCallBack gameCallBack;

    public LunarDriver(GameCallBack gameCallBack) {
        this.gameCallBack = gameCallBack;
    }

    @Override
    public void create() {
        StageGame.setAppSize(800, 400);

        Gdx.input.setCatchBackKey(true);

        Locale locale= Locale.getDefault();
        bundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),locale);
        path_to_atlas = bundle.get("path");


        loadingAssets = true;
        assetManager = new AssetManager();
        assetManager.load(path_to_atlas, TextureAtlas.class);
        assetManager.load("musics/music1.ogg", Music.class);
        assetManager.load("musics/level_win.ogg", Music.class);
        assetManager.load("musics/level_failed.ogg", Music.class);

        assetManager.load("sounds/level_completed.ogg", Sound.class);
        assetManager.load("sounds/click.ogg", Sound.class);
        assetManager.load("sounds/crash.ogg", Sound.class);
        assetManager.load("sounds/fail.ogg", Sound.class);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter sizeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        sizeParams.fontFileName = "fonts/GROBOLD.ttf";
        sizeParams.fontParameters.size = 40;

        assetManager.load("font40.ttf", BitmapFont.class, sizeParams);


    }

    @Override
    public void render() {
        if (loadingAssets) {
            if (assetManager.update()) {
                loadingAssets = false;
                onAssetsLoaded();
            }
        }
        super.render();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        super.dispose();
    }

    private void onAssetsLoaded() {
        atlas = assetManager.get(path_to_atlas, TextureAtlas.class);
        font = assetManager.get("font40.ttf", BitmapFont.class);
    }

    private void exitApp() {
        Gdx.app.exit();
    }

}
