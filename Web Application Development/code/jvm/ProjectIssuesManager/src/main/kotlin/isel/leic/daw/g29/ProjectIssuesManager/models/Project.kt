package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.CollaboratorOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.ProjectOutputModel

data class Project(
    val name: String,
    val description: String,
    val startState: String
){
    fun mapToOutputModel () = ProjectOutputModel(name, description, startState)
}