package org.scripts.kotlin.content.world;

import com.zarador.model.GameObject
import com.zarador.model.Position
import com.zarador.world.entity.impl.player.Player

/**
 * Author Vados
 */

object ChaosTunnelHandler {

    @JvmStatic fun handleObjects(player: Player, gameObject: GameObject): Boolean {
        when (gameObject.id) {
            28779 -> { val destination: Position = when (gameObject.position) {
                    Position(3245, 5451) -> Position(3250, 5448)
                    Position(3254, 5451) -> Position(3250, 5448)
                    Position(3250, 5448) -> Position(3254, 5451)
                    Position(3241, 5445) -> Position(3233, 5445)
                    Position(3233, 5445) -> Position(3241, 5445)
                    Position(3259, 5446) -> Position(3265, 5491)
                    Position(3265, 5491) -> Position(3259, 5446)
                    Position(3260, 5491) -> Position(3266, 5446)
                    Position(3266, 5446) -> Position(3260, 5491)
                    Position(3241, 5469) -> Position(3233, 5470)
                    Position(3233, 5470) -> Position(3241, 5469)
                    Position(3235, 5457) -> Position(3229, 5454)
                    Position(3229, 5454) -> Position(3235, 5457)
                    Position(3280, 5460) -> Position(3273, 5460)
                    Position(3273, 5460) -> Position(3280, 5460)
                    Position(3283, 5448) -> Position(3287, 5448)
                    Position(3287, 5448) -> Position(3283, 5448)
                    Position(3244, 5495) -> Position(3239, 5498)
                    Position(3239, 5498) -> Position(3244, 5495)
                    Position(3232, 5501) -> Position(3238, 5507)
                    Position(3238, 5507) -> Position(3232, 5501)
                    Position(3218, 5497) -> Position(3222, 5488)
                    Position(3222, 5488) -> Position(3218, 5497)
                    Position(3218, 5478) -> Position(3215, 5475)
                    Position(3215, 5475) -> Position(3218, 5478)
                    Position(3224, 5479) -> Position(3222, 5474)
                    Position(3222, 5474) -> Position(3224, 5479)
                    Position(3208, 5471) -> Position(3210, 5477)
                    Position(3210, 5477) -> Position(3208, 5471)
                    Position(3214, 5456) -> Position(3212, 5452)
                    Position(3212, 5452) -> Position(3214, 5456)
                    Position(3204, 5445) -> Position(3197, 5448)
                    Position(3197, 5448) -> Position(3204, 5445)
                    Position(3189, 5444) -> Position(3187, 5460)
                    Position(3187, 5460) -> Position(3189, 5444)
                    Position(3192, 5472) -> Position(3186, 5472)
                    Position(3186, 5472) -> Position(3192, 5472)
                    Position(3185, 5478) -> Position(3191, 5482)
                    Position(3191, 5482) -> Position(3185, 5478)
                    Position(3171, 5473) -> Position(3167, 5471)
                    Position(3167, 5471) -> Position(3171, 5473)
                    Position(3171, 5478) -> Position(3167, 5478)
                    Position(3167, 5478) -> Position(3171, 5478)
                    Position(3168, 5456) -> Position(3178, 5460)
                    Position(3178, 5460) -> Position(3168, 5456)
                    Position(3191, 5495) -> Position(3194, 5490)
                    Position(3194, 5490) -> Position(3191, 5495)
                    Position(3141, 5480) -> Position(3142, 5489)
                    Position(3142, 5489) -> Position(3141, 5480)
                    Position(3142, 5462) -> Position(3154, 5462)
                    Position(3154, 5462) -> Position(3142, 5462)
                    Position(3143, 5443) -> Position(3155, 5449)
                    Position(3155, 5449) -> Position(3143, 5443)
                    Position(3307, 5496) -> Position(3317, 5496)
                    Position(3317, 5496) -> Position(3307, 5496)
                    Position(3318, 5481) -> Position(3322, 5480)
                    Position(3322, 5480) -> Position(3318, 5481)
                    Position(3299, 5484) -> Position(3303, 5477)
                    Position(3303, 5477) -> Position(3299, 5484)
                    Position(3286, 5470) -> Position(3285, 5474)
                    Position(3285, 5474) -> Position(3286, 5470)
                    Position(3290, 5463) -> Position(3302, 5469)
                    Position(3302, 5469) -> Position(3290, 5463)
                    Position(3296, 5455) -> Position(3299, 5450)
                    Position(3299, 5450) -> Position(3296, 5455)
                    Position(3280, 5501) -> Position(3285, 5508)
                    Position(3285, 5508) -> Position(3280, 5501)
                    Position(3300, 5514) -> Position(3297, 5510)
                    Position(3297, 5510) -> Position(3300, 5514)
                    Position(3289, 5533) -> Position(3288, 5536)
                    Position(3288, 5536) -> Position(3289, 5533)
                    Position(3285, 5527) -> Position(3282, 5531)
                    Position(3282, 5531) -> Position(3285, 5527)
                    Position(3325, 5518) -> Position(3323, 5531)
                    Position(3323, 5531) -> Position(3325, 5518)
                    Position(3299, 5533) -> Position(3297, 5536)
                    Position(3297, 5536) -> Position(3299, 5533)
                    Position(3321, 5554) -> Position(3315, 5552)
                    Position(3315, 5552) -> Position(3321, 5554)
                    Position(3291, 5555) -> Position(3285, 5556)
                    Position(3285, 5556) -> Position(3291, 5555)
                    Position(3266, 5552) -> Position(3262, 5552)
                    Position(3262, 5552) -> Position(3266, 5552)
                    Position(3256, 5561) -> Position(3253, 5561)
                    Position(3253, 5561) -> Position(3256, 5561)
                    Position(3249, 5546) -> Position(3252, 5543)
                    Position(3252, 5543) -> Position(3249, 5546)
                    Position(3261, 5536) -> Position(3268, 5534)
                    Position(3268, 5534) -> Position(3261, 5536)
                    Position(3243, 5526) -> Position(3241, 5529)
                    Position(3241, 5529) -> Position(3243, 5526)
                    Position(3230, 5547) -> Position(3226, 5553)
                    Position(3226, 5553) -> Position(3230, 5547)
                    Position(3206, 5553) -> Position(3204, 5546)
                    Position(3204, 5546) -> Position(3206, 5553)
                    Position(3211, 5533) -> Position(3214, 5533)
                    Position(3214, 5533) -> Position(3211, 5533)
                    Position(3208, 5527) -> Position(3211, 5523)
                    Position(3211, 5523) -> Position(3208, 5527)
                    Position(3201, 5531) -> Position(3197, 5529)
                    Position(3197, 5529) -> Position(3201, 5531)
                    Position(3202, 5515) -> Position(3196, 5512)
                    Position(3196, 5512) -> Position(3202, 5515)
                    Position(3190, 5515) -> Position(3190, 5519)
                    Position(3190, 5519) -> Position(3190, 5515)
                    Position(3185, 5518) -> Position(3181, 5517)
                    Position(3181, 5517) -> Position(3185, 5518)
                    Position(3187, 5531) -> Position(3182, 5530)
                    Position(3182, 5530) -> Position(3187, 5531)
                    Position(3169, 5510) -> Position(3159, 5501)
                    Position(3159, 5501) -> Position(3169, 5510)
                    Position(3165, 5515) -> Position(3173, 5530)
                    Position(3173, 5530) -> Position(3165, 5515)
                    Position(3156, 5523) -> Position(3152, 5520)
                    Position(3152, 5520) -> Position(3156, 5523)
                    Position(3148, 5533) -> Position(3153, 5537)
                    Position(3153, 5537) -> Position(3148, 5533)
                    Position(3143, 5535) -> Position(3147, 5541)
                    Position(3147, 5541) -> Position(3143, 5535)
                    Position(3168, 5541) -> Position(3171, 5542)
                    Position(3171, 5542) -> Position(3168, 5541)
                    Position(3190, 5549) -> Position(3190, 5554)
                    Position(3190, 5554) -> Position(3190, 5549)
                    Position(3180, 5557) -> Position(3174, 5558)
                    Position(3174, 5558) -> Position(3180, 5557)
                    Position(3162, 5557) -> Position(3158, 5561)
                    Position(3158, 5561) -> Position(3162, 5557)
                    Position(3166, 5553) -> Position(3162, 5545)
                    Position(3162, 5545) -> Position(3166, 5553)
                    Position(3142, 5545) -> Position(3115, 5528)
                    else -> Position(0, 0)
                }

                if (destination == Position(0, 0))
                    player.packetSender.sendMessage("This tunnel isn't supported.")
                else player.moveTo(destination)

                return true
            }

            28891 -> {
                if (player.combatBuilder.isBeingAttacked)
                    player.packetSender.sendMessage("You cannot enter the rift whilst your under attack.")
                else player.moveTo(Position(3183, 5470))
                return true
            }
            28892 -> {
                if (player.combatBuilder.isBeingAttacked) {
                    player.packetSender.sendMessage("You cannot enter the rift whilst your under attack.")
                } else {
                    if (gameObject.position.x == 3165 && gameObject.position.y == 3561) {
                        player.moveTo(Position(3292, 5479))
                    }
                    if (gameObject.position == Position(3165, 3618)) {
                        player.moveTo(Position(3291, 5538))
                    }
                }
                return true
            }
            28893 -> {
                if (player.combatBuilder.isBeingAttacked) {
                    player.packetSender.sendMessage("You cannot enter the rift whilst your under attack.")
                } else {
                    if (gameObject.position == Position(3119, 3571)) {
                        player.moveTo(Position(3248, 5490))
                    }
                    if (gameObject.position == Position(3107, 3639)) {
                        player.moveTo(Position(3234, 5559))
                    }
                }
                return true
            }
            28782 -> {
                if (gameObject.position == Position(3183, 5470)) { player.moveTo(Position(3059, 3549)) }
                if (gameObject.position == Position(3248, 5490)) { player.moveTo(Position(3120, 3571)) }
                if (gameObject.position == Position(3292, 5479)) { player.moveTo(Position(3166, 3561)) }
                if (gameObject.position == Position(3291, 5538)) { player.moveTo(Position(3166, 3618)) }
                if (gameObject.position == Position(3234, 5559)) { player.moveTo(Position(3107, 3640)) }
                return true
            }

            29537 -> {
                if (gameObject.position == Position(3115, 5528)) { player.moveTo(Position(3142, 5545)) }
                return true
            }
        }
        return false
    }
}
