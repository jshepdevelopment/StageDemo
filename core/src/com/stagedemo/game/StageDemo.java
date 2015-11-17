package com.stagedemo.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;

import java.util.Random;

public class StageDemo implements ApplicationListener {

    // create Actor "Bubble that displays TextureRegion passed
    class Bubble extends Actor {
        private TextureRegion _texture;

        public Bubble(TextureRegion texture){
            _texture = texture;
            setBounds(getX(),getY(),_texture.getRegionWidth(), _texture.getRegionHeight());

            this.addListener(new InputListener(){
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons){
                    System.out.println("Touched" + getName());
                    setVisible(false);
                    return true;
                }
            });
        }

        // implements draw() completely to handle rotation and scaling
        public void draw(Batch batch, float alpha){
            batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }

        // hit() instead of bounding box, checks bounding circle
        public Actor hit(float x, float y, boolean touchable){
            // If this Actor is hidden or untouchable, it cant be hit
            if(!this.isVisible() || this.getTouchable() == Touchable.disabled)
                return null;

            // get the centerpoint of bounding circle, center of the rect
            float centerX = getWidth()/2;
            float centerY = getHeight()/2;

            // Square roots are bad m'kay. In "real" code, simply square both sides for much speedy fastness
            // This however is the proper, unoptimized and easiest to grok equation for a hit within a circle
            // You could of course use LibGDX's Circle class instead.

            // radius of circle
            float radius = (float) Math.sqrt(centerX * centerX +
                    centerY * centerY);

            // distance of point from the center of the circle
            float distance = (float) Math.sqrt(((centerX - x) * (centerX - x))
                    + ((centerY - y) * (centerY - y)));

            // if distance is less than circle radius, a hit occurs
            if(distance <= radius) return this;

            // no hit
            return null;
        }
    }

    private Bubble[] bubbles;
    private MoveToAction[] moveActions;

    private int bubbleCount = 15;
    private Stage stage;

    @Override
    public void create() {
        // stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        stage = new Stage();
        final TextureRegion bubbleTexture = new TextureRegion(new Texture("bubble.png"));

        // initialize arrays
        bubbles = new Bubble[bubbleCount];
        moveActions = new MoveToAction[bubbleCount];

        // random number seed
        Random random = new Random();

        // make 10 bubble objects at random on screen locations
        for(int i = 0; i < bubbleCount; i++){
            bubbles[i] = new Bubble(bubbleTexture);
            moveActions[i] = new MoveToAction();
            Vector2 coords = new Vector2(bubbles[i].getX(), bubbles[i].getY());

            //Assign the position of the bubble to a random value within the screen boundaries
            int randomX = random.nextInt(Gdx.graphics.getWidth() - (int)bubbles[i].getWidth());

            bubbles[i].setScale(random.nextFloat());
            bubbles[i].setPosition(randomX, 0 - (int) bubbles[i].getHeight());
            // Set the name of the bubble to it's index within the loop
            bubbles[i].setName(Integer.toString(i));

            moveActions[i].setPosition(randomX, 2600f);
            moveActions[i].setDuration(random.nextFloat());
            bubbles[i].addAction(moveActions[i]);

//            bubbles[i].localToStageCoordinates(coords);
//            bubbles[i].getStage().stageToScreenCoordinates(coords);
            Gdx.app.log("JSLOG", " bubbles[i].getY() + coords.y)"+ bubbles[i].getY() + " " + coords.y);

            //if(moveActions[i].getY() > 2500f) {
            //    moveActions[i].setPosition(randomX, 0 - (int) bubbles[i].getHeight());
            //}

            // Add them to the stage
            stage.addActor(bubbles[i]);
        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
