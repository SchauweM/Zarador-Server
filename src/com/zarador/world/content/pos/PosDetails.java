package com.zarador.world.content.pos;

/**
 * Represents the details of a players pos.
 * 
 * @author Blake
 */
public class PosDetails {

	private int buttonId;
	private String owner;
	private String caption;

	public PosDetails(int buttonId, String owner, String caption) {
		this.buttonId = buttonId;
		this.owner = owner;
		this.caption = caption;
	}

	/**
	 * Gets the button id.
	 * 
	 * @return the button id
	 */
	public int getButtonId() {
		return buttonId;
	}

	public void setButtonId(int ii) {
		this.buttonId = ii;
	}

	/**
	 * Gets the caption.
	 * 
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Gets the owner.
	 * 
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

}
