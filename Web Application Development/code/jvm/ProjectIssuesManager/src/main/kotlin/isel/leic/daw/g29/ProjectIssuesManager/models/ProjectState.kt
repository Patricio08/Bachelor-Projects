package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.ProjectOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.ProjectStateOutputModel

data class ProjectState(
        val projectName: String,
        val stateName: String,
){
    fun mapToOutputModel () = ProjectStateOutputModel(projectName, stateName)
}