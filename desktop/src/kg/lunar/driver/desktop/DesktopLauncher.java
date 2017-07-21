package kg.lunar.driver.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import kg.lunar.driver.GameCallBack;
import kg.lunar.driver.LunarDriver;

public class DesktopLauncher {
    public DesktopLauncher() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 400;
        new LwjglApplication(new LunarDriver(callBack), config);
    }

    private GameCallBack callBack = new GameCallBack() {
        @Override
        public void sendMassage(int massage) {
            System.out.printf("DesktopLauncher sendMassage: "+massage);
        }
    };

    public static void main(String[] args) {
        new DesktopLauncher();
    }
}
