package com.gamedesign.notouching.component;

public class Exploding extends Component {

    public float timeUntilIgnition;

    @Override
    public ComponentType type() {
        return ComponentType.Exploding;
    }

}
