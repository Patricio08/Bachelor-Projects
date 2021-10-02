package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.CollaboratorOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.NextStateOutputModel

data class NextState(
        val projectName: String,
        val stateName: String,
        val nextState: String
){
    fun mapToOutputModel () = NextStateOutputModel(projectName, stateName, nextState)
}
