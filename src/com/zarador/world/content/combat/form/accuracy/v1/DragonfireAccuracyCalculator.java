package com.zarador.world.content.combat.form.accuracy.v1;


import com.zarador.model.Prayerbook;
import com.zarador.world.content.combat.form.accuracy.AccuracyCalculator;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.player.Player;

/**
 * An implementation of {@link AccuracyCalculator} used to calculate
 * whether a {@link org.niobe.model.GameCharacter} will land their next hit
 * in combat while using {@link org.niobe.model.AttackType#DRAGON_FIRE}.
 *
 * @author Relex
 */
public final class DragonfireAccuracyCalculator implements AccuracyCalculator {

	@Override
	public double getAccuracy(Character source, Character victim) {
		
		/*
		 * Defining some constants
		 */
		Prayerbook victimPrayerBook = Prayerbook.NORMAL;
		boolean[] victimPrayer = victim.getPrayerActive();

		if(source.isPlayer()) {
			Player player = ((Player)source);
			victimPrayerBook = player.getPrayerbook();
		}
		
		/*
		 * Prayer protection
		 */
		double prayerProtection = 1.0;
		
		if (source.isNpc()) {
			//if {@param victim} has magic protection prayer and our {@param source} is a
			//mob, block the hit completely
			if (victimPrayerBook == Prayerbook.NORMAL && victimPrayer[PrayerHandler.PROTECT_FROM_MAGIC]) {
				return 0;
			} else if (victimPrayerBook == Prayerbook.CURSES && victimPrayer[CurseHandler.CurseData.DEFLECT_MAGIC.ordinal()]) {
				return 0;
			}
		} else if (source.isPlayer()) {
			//if {@param victim} has magic protection prayer and our {@param source}
			//is a {@link org.niobe.world.Player}, set our {@value prayerProtection} to 0.6
			//to reduce 40% of the damage
			if (victimPrayerBook == Prayerbook.NORMAL && victimPrayer[PrayerHandler.PROTECT_FROM_MAGIC]) {
				prayerProtection = 0.6;
			} else if (victimPrayerBook == Prayerbook.CURSES && victimPrayer[CurseHandler.CurseData.DEFLECT_MAGIC.ordinal()]) {
				prayerProtection = 0.6;
			}
		}
		
		return 1 * prayerProtection;
	}
}
