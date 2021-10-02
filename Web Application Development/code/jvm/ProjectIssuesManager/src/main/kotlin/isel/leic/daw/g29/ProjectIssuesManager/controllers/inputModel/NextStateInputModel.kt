package isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel

import isel.leic.daw.g29.ProjectIssuesManager.models.Comment
import isel.leic.daw.g29.ProjectIssuesManager.models.NextState

data class NextStateInputModel(
        var projectName: String?,
        var stateName: String?,
        var nextState: String?
) {
    //fun mapToNextState() = NextState(projectName, stateName, nextState)
}