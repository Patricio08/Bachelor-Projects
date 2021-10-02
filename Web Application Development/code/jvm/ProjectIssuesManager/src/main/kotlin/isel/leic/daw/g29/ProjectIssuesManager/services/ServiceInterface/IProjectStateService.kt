package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectState

interface IProjectStateService {
    fun getProjectStates(projectname: String): List<ProjectState>
    fun postProjectState(input: ProjectStateInputModel): String
    fun deleteProjectState(input: ProjectStateInputModel): String
}