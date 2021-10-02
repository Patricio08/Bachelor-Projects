package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.ProjectOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.StateOutputModel

data class State(
        val stateName: String,
){
    fun mapToOutputModel () = StateOutputModel(stateName)
}