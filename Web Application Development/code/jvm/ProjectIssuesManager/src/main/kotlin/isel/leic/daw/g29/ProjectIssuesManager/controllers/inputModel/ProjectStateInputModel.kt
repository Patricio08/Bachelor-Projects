package isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel

import isel.leic.daw.g29.ProjectIssuesManager.models.NextState
import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectState

data class ProjectStateInputModel(
        var projectName: String?,
        val stateName: String
) {
    //fun mapToProjectState() = ProjectState(projectName?, stateName)
}