package com.zarador.model.action;

import com.zarador.world.entity.impl.Character;

public final class ActionQueue {

	private static final int QUEUE_SIZE = 20;
	// private final Mobile mobile;
	private boolean accepting = true;
	private final Action[] queue = new Action[ActionQueue.QUEUE_SIZE];

	public ActionQueue(Character mobile) {
		// this.mobile = mobile;
	}

	public Action getCurrentAction() {
		return queue[0];
	}

	public boolean hasAction() {
		return queue[0] != null;
	}

	public void close() {
		this.accepting = false;
	}

	public void addAction(Action action) {
		if (!accepting) {
			return;
		}
		Action first = queue[0];
		if (first != null) {
			if (first.getActionPolicy() == PlayerAction.ActionPolicy.CLEAR) {
				// if(first.getClass() != CombatFollowMobileAction.class &&
				// action.getClass() == WieldItemAction.class) {
				first.stop();
				queue[0] = null;
				// }
			} else {
				if (action.getActionPolicy() != PlayerAction.ActionPolicy.QUEUE) {
					return;
				}
				if (first.getActionPolicy() != PlayerAction.ActionPolicy.QUEUE) {
					action.handleFailed();
					return;
				}
			}
		}
		for (int i = 0; i < ActionQueue.QUEUE_SIZE; i++) {
			if (queue[i] != null) {
				continue;
			}
			action.initialize();
			queue[i] = action;
			return;
		}
		action.handleFailed();
	}

	public void clearActions() {
		if (queue[0] == null) {
			return;
		}
		this.clear();
	}

	public void clearNonFixedActions() {
		if (queue[0] == null) {
			return;
		}
		if (queue[0].getActionPolicy() == PlayerAction.ActionPolicy.FIXED) {
			return;
		}
		this.clear();
	}

	public void clearNonQueueActions() {
		if (queue[0] == null) {
			return;
		}
		if (queue[0].getActionPolicy() == PlayerAction.ActionPolicy.QUEUE) {
			return;
		}
		this.clear();
	}

	public void processActions() {
		if (queue[0] == null) {
			return;
		}
		for (int i = 0; i < ActionQueue.QUEUE_SIZE; i++) {
			if (queue[i] == null) {
				return;
			}
			queue[i].run();
			if (queue[i] == null) {
				continue;
			}
			if (queue[i].isRunning()) {
				continue;
			}
			queue[i] = null;
		}
	}

	private void clear() {
		Action action = queue[0];
		queue[0] = null;
		action.stop();
		for (int i = 0; i < ActionQueue.QUEUE_SIZE; i++) {
			if (queue[i] == null) {
				return;
			}
			queue[i] = null;
		}
	}
}
