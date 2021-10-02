package isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel

import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectState
import isel.leic.daw.g29.ProjectIssuesManager.models.State

data class StateInputModel(
        val stateName: String
) {
    fun mapToState() = State(stateName)
}