package com.gamedesign.notouching.component;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RevoluteJoint;
import com.google.fpl.liquidfun.Vec2;

public class ChassisEngine extends Component {

    public float[] targetCoordinates;
    public Joint[] bombTargets;
    public RevoluteJoint frontWheelJoint;
    public RevoluteJoint backWheelJoint;
    public int bombEjectedIndex = 0;
    public boolean isPlaying = false;
    private static final Vec2 vec2 = new Vec2();

    @Override
    public ComponentType type() {
        return ComponentType.AI;
    }

    public void move(float motorSpeed, Level level){
        Entity chassis = this.owner;
        if(!isPlaying){
            Assets.engine.setLooping(true);
            Assets.engine.setVolume(0.5f);
            Assets.engine.play();
            isPlaying = true;
        }
        float targetCoordinate;
        if (this.targetCoordinates.length == bombEjectedIndex) {
            targetCoordinate = 3000f;
        } else {
            targetCoordinate = this.targetCoordinates[bombEjectedIndex];
        }
        if ((chassis.getBody().getWorldCenter().getX() * SCALING_FACTOR - targetCoordinate) < -50) {
            frontWheelJoint.setMotorSpeed(motorSpeed);
            backWheelJoint.setMotorSpeed(motorSpeed);
        } else if ((chassis.getBody().getWorldCenter().getX() * SCALING_FACTOR - targetCoordinate) > 50) {
            frontWheelJoint.setMotorSpeed(-motorSpeed);
            backWheelJoint.setMotorSpeed(-motorSpeed);
        } else {
            frontWheelJoint.setMotorSpeed(motorSpeed);
            backWheelJoint.setMotorSpeed(0);
            vec2.setY(0);
            vec2.setX(0);
            ejectBomb(level);
        }
    }

    public void ejectBomb(Level level) {
        GameObject bomb = Assets.gameObjectsJSON.getGameObject(GameObjects.BOMB, level.game);
        PixmapDrawable bombDrawable = bomb.getComponent(ComponentType.Drawable);
        Exploding component = bomb.getComponent(ComponentType.Exploding);
        component.timeUntilIgnition = level.timeUntilBombIgnition;
        component.setTarget(bombTargets[bombEjectedIndex]);
        PixmapDrawable chassisDrawable = owner.getComponent(ComponentType.Drawable);
        vec2.setX(-(chassisDrawable.width / 2 + bombDrawable.width));
        vec2.setY(0);
        Vec2 chassisCoordinates = owner.getBody().getWorldPoint(vec2);
        bomb.setPosition(chassisCoordinates.getX(), chassisCoordinates.getY() + 4f);
        level.addGameObject(bomb);
        bombEjectedIndex++;
    }

}
