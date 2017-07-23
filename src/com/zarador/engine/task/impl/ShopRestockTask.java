package com.zarador.engine.task.impl;

import com.zarador.engine.task.Task;
import com.zarador.model.Item;
import com.zarador.model.container.impl.Shop;

public class ShopRestockTask extends Task {

	public ShopRestockTask(Shop shop) {
		super(5);
		this.shop = shop;
	}

	private final Shop shop;

	@Override
	protected void execute() {
		if (shop.fullyRestocked()) {
			stop();
			return;
		}
		if (shop.getId() != Shop.GENERAL_STORE && shop.getId() != Shop.DIANGO_STORE) {
			for (int shopItemIndex = 0; shopItemIndex < shop.getOriginalStock().length; shopItemIndex++) {

				int originalStockAmount = shop.getOriginalStock()[shopItemIndex].getAmount();
				int currentStockAmount = shop.getItems()[shopItemIndex].getAmount();
				if (originalStockAmount > currentStockAmount) {
					shop.add(shop.getItems()[shopItemIndex].getId(), 1);
				} else if (originalStockAmount < currentStockAmount) {
					shop.delete(shop.getItems()[shopItemIndex].getId(),
							getDeleteRatio(shop.getItems()[shopItemIndex].getAmount()));
				}

			}
		} else {
			for (Item it : shop.getValidItems()) {
				int delete = getDeleteRatio(it.getAmount());
				if(shop.getId() == 0) {
					if (it.getId() == 590 || it.getId() == 1755 || it.getId() == 2347 || it.getId() == 952 || it.getId() == 946 || it.getId() == 228 || it.getId() == 1540 || it.getId() == 1523 || it.getId() == 1734 || it.getId() == 1733 || it.getId() == 314) {
						continue;
					}
				}
				if(shop.getId() != Shop.DIANGO_STORE) {
					shop.delete(it.getId(), 1);
				}
			}
		}
		if(shop.getId() == Shop.GENERAL_STORE) {
			for (int shopItemIndex = 0; shopItemIndex < shop.getOriginalStock().length; shopItemIndex++) {
				int originalStockAmount = shop.getOriginalStock()[shopItemIndex].getAmount();
				int currentStockAmount = shop.getItems()[shopItemIndex].getAmount();
				Item it = new Item(shop.getItems()[shopItemIndex].getId(), shop.getItems()[shopItemIndex].getAmount());
				if (it.getId() == 590 || it.getId() == 1755 || it.getId() == 2347 || it.getId() == 952 || it.getId() == 946 || it.getId() == 228 || it.getId() == 1540 || it.getId() == 1523 || it.getId() == 1734 || it.getId() == 1733 || it.getId() == 314) {
					if (originalStockAmount > currentStockAmount) {
						shop.add(shop.getItems()[shopItemIndex].getId(), 1);
					}
				}
			}
		}
		shop.publicRefresh();
		shop.refreshItems();
		if (shop.fullyRestocked())
			stop();
	}

	@Override
	public void stop() {
		setEventRunning(false);
		shop.setRestockingItems(false);
	}

	public static int getRestockAmount(int amountMissing) {
		return (int) (Math.pow(amountMissing, 1.2) / 30 + 1);
	}

	public static int getDeleteRatio(int x) {
		return (int) (Math.pow(x, 1.05) / 50 + 1);
	}
}
