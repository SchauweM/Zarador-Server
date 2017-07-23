package com.zarador.world.content.transportation;

import com.zarador.model.Animation;
import com.zarador.model.Graphic;

public enum TeleportType {

	NORMAL(3, new Animation(8939), new Animation(8941), new Graphic(1576), new Graphic(1577)),
	ANCIENT(5, new Animation(9599), Animations.DEFAULT_RESET_ANIMATION, new Graphic(1681, 0), null),
	DUNGEONEERING(8, new Animation(13652), new Animation(13654), new Graphic(2602, 0), new Graphic(2603, 0)),
	LUNAR(4, new Animation(9606), new Animation(9013), new Graphic(1685), null),
	TELE_TAB(2, new Animation(4731), Animations.DEFAULT_RESET_ANIMATION, new Graphic(678), null),
	JEWELRY_TELE(2, new Animation(9603), Animations.DEFAULT_RESET_ANIMATION, new Graphic(1684), null),
	LEVER(3, new Animation(8939), new Animation(8941), new Graphic(1576), new Graphic(1577)),
	PURO_PURO(9, new Animation(6601), Animations.DEFAULT_RESET_ANIMATION, new Graphic(1118), null);

	TeleportType(int startTick, Animation startAnim, Animation endAnim, Graphic startGraphic, Graphic endGraphic) {
		this.startTick = startTick;
		this.startAnim = startAnim;
		this.endAnim = endAnim;
		this.startGraphic = startGraphic;
		this.endGraphic = endGraphic;
	}

	private Animation startAnim, endAnim;
	private Graphic startGraphic, endGraphic;
	private int startTick;

	public Animation getStartAnimation() {
		return startAnim;
	}

	public Animation getEndAnimation() {
		return endAnim;
	}

	public Graphic getStartGraphic() {
		return startGraphic;
	}

	public Graphic getEndGraphic() {
		return endGraphic;
	}

	public int getStartTick() {
		return startTick;
	}

	static class Animations {
		static Animation DEFAULT_RESET_ANIMATION = new Animation(65535);
	}
}
