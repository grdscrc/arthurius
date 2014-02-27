package gameplay;

import java.util.UUID;

import gameframework.game.GameMovable;

/**
 * Adds an UUID to the class GameMovable, and overrides the equals() method.
 */

public abstract class GameMovableUnique extends GameMovable {
	private UUID uuid = UUID.randomUUID();
	
	public boolean equals(GameMovableUnique gameEntity){
		return gameEntity.uuid == this.uuid;
	}

}
