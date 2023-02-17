package com.gamedesign.notouching.util;

import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Pool;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

import java.util.Collection;
import java.util.HashSet;

public class MyContactListener extends ContactListener {

    private final Collection<Collision> cache = new HashSet<>();
    private final Pool<Collision> collisionPool = new Pool<>(Collision::new, 20);

    public Collection<Collision> getCollisions() {
        Collection<Collision> result = new HashSet<>(cache);
        cache.clear();
        return result;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();
        Object userdataA = ba.getUserData(), userdataB = bb.getUserData();
        GameObject a = (GameObject)userdataA,
                   b = (GameObject)userdataB;

        Collision collision = collisionPool.newObject();
        collision.a = a;
        collision.b = b;
        cache.add(collision);
    }
}
