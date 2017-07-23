package org.scripts.kotlin.core.engine

import com.zarador.world.content.EvilTrees
import com.zarador.world.content.minigames.impl.FightPit
import com.zarador.world.content.minigames.impl.PestControl

/**
 * Created by Dave on 05/07/2016.
 */

object SequenceControl {

    /**
     * Here we start tasks that are sequenced.
     */

    @JvmStatic fun sequenceStarter() {
        FightPit.sequence()
        PestControl.sequence()
        EvilTrees.sequence()
        System.out.println("Argos has successfully started up World sequences.")
    }
}