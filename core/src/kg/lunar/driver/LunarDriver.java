package kg.lunar.driver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.I18NBundle;
import com.boontaran.games.StageGame;

import java.util.Locale;

import kg.lunar.driver.levels.Level;
import kg.lunar.driver.screens.LevelList;
import kg.lunar.driver.media.Media;
import kg.lunar.driver.screens.Intro;
import kg.lunar.driver.utils.Data;

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
    public static BitmapFont font40;

    private I18NBundle bundle;
    private String path_to_atlas;

    private GameCallBack gameCallback;

    public static Media media;

    private Intro intro;

    public static Data data;

    private LevelList levelList;
    private Level level;
    private int lastLevelId;


    public LunarDriver(GameCallBack gameCallback) {
        this.gameCallback = gameCallback;
    }

    @Override
    public void create () {
        StageGame.setAppSize(800, 480);

        Gdx.input.setCatchBackKey(true);

        Locale locale = Locale.getDefault();
        bundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
        path_to_atlas = bundle.get("path");


        loadingAssets = true;
        assetManager = new AssetManager();
        assetManager.load(path_to_atlas, TextureAtlas.class);
        assetManager.load("musics/music1.ogg", Music.class);
        assetManager.load("musics/level_failed.ogg", Music.class);
        assetManager.load("musics/level_win.ogg", Music.class);
        assetManager.load("sounds/level_completed.ogg", Sound.class);
        assetManager.load("sounds/fail.ogg", Sound.class);
        assetManager.load("sounds/click.ogg", Sound.class);
        assetManager.load("sounds/crash.ogg", Sound.class);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter sizeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        sizeParams.fontFileName = "fonts/GROBOLD.ttf";
        sizeParams.fontParameters.size = 40;

        assetManager.load("font40.ttf", BitmapFont.class, sizeParams);

        media = new Media(assetManager);
        data = new Data();
    }

    @Override
    public void render () {
        if (loadingAssets) {
            if (assetManager.update()) {
                loadingAssets = false;
                onAssetsLoaded();
            }
        }
        super.render();
    }

    @Override
    public void dispose () {
        assetManager.dispose();
        super.dispose();
    }

    private void onAssetsLoaded() {
        atlas = assetManager.get(path_to_atlas, TextureAtlas.class);
        font40 = assetManager.get("font40.ttf", BitmapFont.class);

        showIntro();
    }


    private void exitApp() {
        Gdx.app.exit();
    }


    private void showIntro() {
        intro = new Intro();
        setScreen(intro);

        intro.setCallback(new StageGame.Callback() {
            @Override
            public void call(int code) {
                if (code == Intro.ON_PLAY) {
                    showLevelList();
                    hideIntro();
                } else if (code == Intro.ON_BACK) {
                    exitApp();
                }
            }
        });

        media.playMusic("music1.ogg", true);
    }
    private void hideIntro() {
        intro = null;
    }

    private void showLevelList() {
        levelList = new LevelList();
        setScreen(levelList);

        levelList.setCallback(new StageGame.Callback() {
            @Override
            public void call(int code) {

                if (code == LevelList.ON_BACK) {
                    showIntro();
                    hideLevelList();
                } else if (code == LevelList.ON_LEVEL_SELECTED) {
                    showLevel(levelList.getSelectedLevelId());
                    hideLevelList();
                } else if(code == LevelList.ON_OPEN_MARKET) {
                    gameCallback.sendMassage(OPEN_MARKET);
                } else if (code == LevelList.ON_SHARE) {
                    gameCallback.sendMassage(SHARE);
                }

            }
        });

        gameCallback.sendMassage(SHOW_BANNER);
        media.playMusic("music1.ogg", true);
    }

    private void hideLevelList() {
        levelList = null;
        gameCallback.sendMassage(HIDE_BANNER);
    }

    private void showLevel(int id) {
        media.stopMusic("music1.ogg");

        lastLevelId = id;
        switch (id) {
            case 1:
                level = new Level("level1");
                break;
            case 2:
                level = new Level("level2");
                break;
            default:
                level = new Level("level" + id);
                break;
        }

        if (level.getMusicName() == null) {
            level.setMusic("music2.ogg");
        }

        setScreen(level);

        level.setCallback(new StageGame.Callback() {
            @Override
            public void call(int code) {
                if (code == Level.ON_RESTART) {
                    gameCallback.sendMassage(HIDE_BANNER);
                    gameCallback.sendMassage(SHOW_INTERSTITIAL);
                    media.stopMusic("level_failed.ogg");
                    media.stopMusic("level_win.ogg");
                    hideLevel();
                    showLevel(lastLevelId);
                } else if (code == Level.ON_QUIT) {
                    gameCallback.sendMassage(SHOW_INTERSTITIAL);
                    media.stopMusic("level_failed.ogg");
                    media.stopMusic("level_win.ogg");
                    hideLevel();
                    showLevelList();
                } else if (code == Level.ON_COMPLETED) {
                    updateProgress();
                    gameCallback.sendMassage(SHOW_INTERSTITIAL);
                    gameCallback.sendMassage(SHOW_BANNER);
                    media.stopMusic("level_win.ogg");
                    hideLevel();
                    showLevelList();
                } else if (code == Level.ON_PAUSED) {
                    gameCallback.sendMassage(SHOW_BANNER);

                } else if (code == Level.ON_RESUME) {
                    gameCallback.sendMassage(HIDE_BANNER);

                } else if (code == Level.ON_FAILED) {
                    gameCallback.sendMassage(SHOW_BANNER);
                    media.playMusic("level_failed.ogg", false);
                }
            }
        });

        gameCallback.sendMassage(LOAD_INTERSTITIAL);
    }

    private void hideLevel() {
        level.dispose();
        level = null;
    }

    protected void updateProgress() {
        int newProgress = lastLevelId + 1;
        if (newProgress > data.getProgress()) {
            data.setProgress(newProgress);
        }
    }


}
