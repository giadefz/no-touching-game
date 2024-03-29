package com.gamedesign.notouching.util;

import com.gamedesign.notouching.component.GameObject;

/** An unordered pair of game objects.
 *
 */
public class Collision {
    public GameObject a, b;

    public Collision() {
    }

    @Override
    public int hashCode() {
        return a.hashCode() ^ b.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Collision))
            return false;
        Collision otherCollision = (Collision) other;
        return (a.equals(otherCollision.a) && b.equals(otherCollision.b)) ||
               (a.equals(otherCollision.b) && b.equals(otherCollision.a));
    }
}
