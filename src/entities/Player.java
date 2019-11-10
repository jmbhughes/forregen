package entities;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity {

	private static final float RUN_SPEED = 50;   // units per second
	private static final float TURN_SPEED = 200; // degrees per second
	public static final float GRAVITY = -150;
	private static final float JUMP_POWER = 30;
	
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isAirborn = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public boolean move(Terrain terrain, List<Entity> entities){
		boolean canMove = true;
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		// Move player
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		if (canMoveWithoutCollision(new Vector3f(dx, 0, dz), entities)) {
			super.increasePosition(dx, 0, dz);
			canMove = true;
		} else {
			canMove = false;
		}
		
		// Allow for jumps
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y  < terrainHeight){
			upwardsSpeed = 0;
			super.getPosition().y = terrainHeight;
			isAirborn = false;
		}

		// Check the bounds
		if (getPosition().x  < 0){
			upwardsSpeed = 0;
			getPosition().x = 0;
		}
		if (getPosition().x  > 800){
			upwardsSpeed = 0;
			getPosition().x = 800;
		}

		if (getPosition().z  > 0){
			upwardsSpeed = 0;
			getPosition().z = 0;
		}
		if (getPosition().z  < -800){
			upwardsSpeed = 0;
			getPosition().z = -800;
		}
		return canMove;
		
	}
	
	private void jump()
	{
		if (!isAirborn){
			upwardsSpeed = JUMP_POWER;
			isAirborn = true;
		}
	}
	
	private void checkInputs(){
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			this.currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
	
	public boolean canMoveWithoutCollision(Vector3f direction, List<Entity> entities) {
		
		Vector3f newPosition = new Vector3f(getPosition().x + direction.x, getPosition().y + direction.y, getPosition().z + direction.z);

		for (Entity entity: entities) {
			if (entity.isCollidable()) {
				float xLower = entity.getPosition().x - entity.getCollisionBoxSize() / 2;
				float xUpper = entity.getPosition().x + entity.getCollisionBoxSize() / 2;
				float zLower = entity.getPosition().z - entity.getCollisionBoxSize() / 2;
				float zUpper = entity.getPosition().z + entity.getCollisionBoxSize() / 2;
				
				if (newPosition.x > xLower && newPosition.x < xUpper && newPosition.z > zLower && newPosition.z < zUpper)
					return false;
			}
		}
		return true;
	}
}
