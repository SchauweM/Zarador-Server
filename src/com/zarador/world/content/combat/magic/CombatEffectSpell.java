package com.zarador.world.content.combat.magic;

import java.util.Optional;

import com.zarador.model.Item;
import com.zarador.model.MagicSpellbook;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.player.Player;

/**
 * A {@link Spell} implementation primarily used for spells that have effects
 * when they hit the player.
 *
 * @author lare96
 */
public abstract class CombatEffectSpell extends CombatSpell {

	public CombatEffectSpell(MagicSpellbook spellbook) {
		this.setSpellbook(spellbook);
	}

	@Override
	public int maximumHit() {
		return 0;
	}

	@Override
	public Optional<Item[]> equipmentRequired(Player player) {

		// These types of spells never require any equipment, although the
		// method can still be overridden if by some chance a spell does.
		return Optional.empty();
	}

	@Override
	public void finishCast(Character cast, Character castOn, boolean accurate, int damage) {
		if (accurate) {
			spellEffect(cast, castOn);
		} else {
			castOn.setFreeze(false);
		}
	}

	/**
	 * The effect that will take place once the spell hits the target.
	 *
	 * @param cast
	 *            the entity casting the spell.
	 * @param castOn
	 *            the entity being hit by the spell.
	 */
	public abstract void spellEffect(Character cast, Character castOn);
}
