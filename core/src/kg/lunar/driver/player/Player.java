package kg.lunar.driver.player;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.boontaran.games.ActorClip;

import kg.lunar.driver.LunarDriver;
import kg.lunar.driver.Setting;
import kg.lunar.driver.levels.Level;

public class Player extends ActorClip implements IBody {

    private Image roverImg, astronautImg, astronautFallImg, frontWheelImage;

    private Group frontWheelCont, rearWheelCont, astronautFallCount;
    public Body rover, frontWheel, rearWheel, astronaut;

    private Joint frontWheelJoint, rearWheelJoint, astronautJoint;

    private World world;

    private boolean hasDestroyed = false;
    private boolean destroyedOnNextUpdate = false;

    private boolean isTouchGround = true;

    private float jumpimpulse = Setting.JUMP_IMPULSE;
    private float jumpWait = 0;

    private Level level;

    public Player(Level level) {
        this.level = level;

        roverImg = new Image(LunarDriver.atlas.findRegion("rover"));
        childs.addActor(roverImg);
        roverImg.setX(-roverImg.getWidth() / 2);
        roverImg.setY(-15);


        astronautImg = new Image(LunarDriver.atlas.findRegion("astronaut"));

        childs.addActor(astronautImg);
        roverImg.setX(-35);
        roverImg.setY(20);

        astronautFallImg = new Image(LunarDriver.atlas.findRegion("astronaut_fall"));
        astronautFallCount.addActor(astronautImg);

        astronautFallImg.setX(-astronautFallImg.getWidth() / 2);
        astronautFallImg.setY(-astronautFallImg.getHeight() / 2);

    }

    public void touchGround() {
        isTouchGround = true;
    }

    public boolean isTouchedGround() {
        if (jumpWait > 0) return false;
        return isTouchGround;
    }


    @Override
    public Body createBody(World world) {
        this.world = world;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.linearDamping = 0;


        frontWheelCont = new Group();
        frontWheelImage = new Image(LunarDriver.atlas.findRegion("front_wheel"));

        frontWheelCont.addActor(frontWheelImage);
        frontWheelImage.setX(-frontWheelImage.getWidth()/2);
        frontWheelImage.setY(-frontWheelImage.getHeight()/2);

        getParent().addActor(frontWheelCont);

        return rover;
    }
}
