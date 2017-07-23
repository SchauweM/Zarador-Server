package com.zarador.world.content.combat.form.max;

import com.zarador.world.entity.impl.Character;

/**
 * An interface that represents a max hit 'calculator' for algorithms
 * for any of our {@link org.niobe.model.AttackType}s.
 *
 * @author Relex
 */
public interface MaxHitCalculator {

	/**
	 * Gets the highest hit possible that can be dealt from our {@param source}
	 * to our {@param victim}.
	 * 
	 * @param source	The {@link org.niobe.model.GameCharacter} who's max hit is being calculated.
	 * @param victim	The {@link org.niobe.model.GameCharacter} being hit, if not available, {@param source} should be used
	 * 					instead.
	 * @return			The max hit {@param source} can possibly deal.
	 */
	public int getMaxHit(Character source, Character victim);
}