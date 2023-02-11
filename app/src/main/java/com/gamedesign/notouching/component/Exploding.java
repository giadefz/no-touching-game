package com.gamedesign.notouching.component;

import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Joint;

public class Exploding extends Component {

    public float timeUntilIgnition;
    public Joint target;
    private boolean exploded;

    @Override
    public ComponentType type() {
        return ComponentType.Exploding;
    }


    public void shortenFuse(float deltaTime) {
        timeUntilIgnition -= deltaTime;
        if(timeUntilIgnition <= 0){
            explode();
            exploded = true;
        }
    }

    private synchronized void explode(){
        Assets.explosion.play(2);
        WorldHandler.destroyJoint(target);
        WorldHandler.destroyBody(this.owner.getBody());
        target.delete();
    }

    public void setTarget(Joint target) {
        this.target = target;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setExplodedToTrue() {
        this.timeUntilIgnition = 0;
    }
}
