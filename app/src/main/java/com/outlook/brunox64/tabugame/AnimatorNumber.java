package com.outlook.brunox64.tabugame;

/**
 * Created by bruno on 18/09/2016.
 */
public class AnimatorNumber extends EventDispacher {
    public interface Property {
        float get();
        void set(float value);
    }

    private GameContext ctx = GameContext.get();

    private Property property;
    private float add,alt,vel,aceler;
    private boolean animando;
    public AnimatorNumber(Property property) {
        super();
        this.property = property;
        reset();
    }
    private void reset() {
        add = 0f;
        alt = 0f;
        vel = ctx.toPixel(6f);
        aceler = ctx.toPixel(3f);
        animando = false;
    }
    private float getValue() {
        return property.get();
    }
    private void setValue(float value) {
        property.set(value);
    }
    public void sum(float add) {
        this.add += Math.abs(add);
        this.alt += add;
    }
    public void update() {
        if (add > 0) {
            if (!animando) {
                animando = true;
                dispatchEvent("beforeAnimation");
            }
            if (this.vel > this.add) {
                setValue(getValue() + this.alt);
                this.alt -= this.alt;
                this.add -= this.add;
            } else {
                setValue(getValue() + (this.alt < 0f ? -this.vel : this.vel));
                this.alt -= this.alt < 0f ? -this.vel : this.vel;
                this.add -= this.vel;
            }
            this.vel += (this.aceler+1f)*2f-1f;
            if (this.add <= 0f) {
                this.reset();
                this.dispatchEvent("afterAnimation");
            }
        }
    }
    public void stop() {
        this.reset();
        this.dispatchEvent("afterAnimation");
    }
}
