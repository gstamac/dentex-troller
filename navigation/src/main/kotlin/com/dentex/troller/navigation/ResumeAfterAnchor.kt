package com.dentex.troller.navigation

/**
 * Remembers what was left of a go-to when the motor went from navigation to anchor mode, so that
 * the user can continue it from there.
 *
 * Only a go-to this app started counts: one the motor ran on its own (from the remote, say) has
 * no waypoints on the phone. What is left is counted from the waypoint the motor reported it
 * headed for last. It is forgotten as soon as the motor reports navigation again or any mode other
 * than anchor; while the motor reports no mode (a lost link) it is kept.
 *
 * Nothing is sent from here. Continuing starts a new go-to with [Rest.waypoints], which uploads
 * them and sends navigation mode once, with every check of [GoToTarget].
 */
class ResumeAfterAnchor {

    /** The waypoints still to go, and how many of the route came before them. */
    data class Rest(val waypoints: List<Waypoint>, val name: String?, val skipped: Int)

    private var underWay: Rest? = null

    /** What can be continued now, or null. */
    var rest: Rest? = null
        private set

    /**
     * Takes the motor's latest report.
     *
     * @param navigating whether the motor reports navigation mode, or null while it reports no mode.
     * @param anchored whether the motor reports anchor mode.
     * @param goTo the go-to as it stands.
     * @param targetIndex the index of the waypoint the motor reports it heads for.
     * @return what can be continued now.
     */
    fun update(navigating: Boolean?, anchored: Boolean, goTo: GoToState, targetIndex: Int?): Rest? {
        when {
            navigating == null -> Unit
            navigating -> {
                rest = null
                // Anchor here ends the go-to before the motor reports anchor mode, so what was
                // left is kept from the last report while it was under way.
                if (goTo.phase == GoToState.Phase.UNDER_WAY && goTo.waypoints.isNotEmpty()) {
                    val index = (targetIndex ?: 0).coerceIn(0, goTo.waypoints.lastIndex)
                    underWay = Rest(goTo.waypoints.drop(index), goTo.name, goTo.skipped + index)
                }
            }
            anchored -> {
                underWay?.let { rest = it }
                underWay = null
            }
            else -> {
                underWay = null
                rest = null
            }
        }
        return rest
    }
}
