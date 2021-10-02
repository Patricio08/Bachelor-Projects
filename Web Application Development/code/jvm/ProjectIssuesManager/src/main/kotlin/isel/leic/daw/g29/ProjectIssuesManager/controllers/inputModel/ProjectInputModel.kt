package isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel

import isel.leic.daw.g29.ProjectIssuesManager.models.Project

data class ProjectInputModel(
    val name: String?,
    val description : String?,
    val startState: String?
    )
{
    //fun mapToProject() = Project(name, description, startState)

}