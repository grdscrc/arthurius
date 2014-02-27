package gameframework.game;

public abstract class GameMovableEphemeral extends GameMovable {
	private int disappearTimer;

	public void setDisappearTimer(int timer){
		this.disappearTimer = timer;
	}

	public void decrementDisappearTimer(){
		this.disappearTimer--;
	}
	
	public boolean shouldDisappear(){
		return disappearTimer == 0;
	}
}
