package com.zarador.model.player.dialog;

import com.zarador.model.options.Option;

public final class DialogMessage {

	public DialogMessage(DialogType type, int dialogueAnimation, String ...lines) {
		this.type = type;
		this.lines = lines;
		this.dialogueAnimation = dialogueAnimation;
	}

	public DialogMessage(Option option) {
		this.type = DialogType.OPTION;
		this.option = option;
	}

	/**
	 * Returns the lines of the dialog message.
	 * @return
	 */
	public String[] getLines() {
		return this.lines;
	}

	/**
	 * Returns the {@link DialogType} of the message.
	 * @return
	 */
	public DialogType getType() {
		return this.type;
	}

	/**
	 * Returns the option for the message.
	 * @return
	 */
	public Option getOption() {
		return this.option;
	}

	/**
	 * Returns the dialogue animation
	 * @return
	 */
	public int getDialogueAnimation() {
		return this.dialogueAnimation;
	}

	private Option option;
	private String[] lines;
	private DialogType type;
	private int dialogueAnimation;

}