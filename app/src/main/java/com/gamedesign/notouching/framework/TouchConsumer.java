package com.gamedesign.notouching.framework;

public abstract class TouchConsumer {

    private EventModifier eventModifier;

    public TouchConsumer() {
    }

    public TouchConsumer(EventModifier eventModifier) {
        this.eventModifier = eventModifier;
    }

    public void handleTouchEvent(Input.TouchEvent event){
        if(eventModifier != null) eventModifier.apply(event);
        switch (event.type){
            case Input.TouchEvent.TOUCH_DOWN:
                handleTouchDown(event);
                break;
            case Input.TouchEvent.TOUCH_UP:
                handleTouchUp(event);
                break;
            case Input.TouchEvent.TOUCH_DRAGGED:
                handleTouchDragged(event);
                break;
        }
    }

    protected abstract void handleTouchDown(Input.TouchEvent event);
    protected  abstract void handleTouchUp(Input.TouchEvent event);
    protected  abstract void handleTouchDragged(Input.TouchEvent event);

    @FunctionalInterface
    public static interface EventModifier {

        void apply(Input.TouchEvent event);

    }

}
