package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectState

interface IDbProjectState {
    fun getProjectStates(projectName: String): List<ProjectState>
    fun postProjectState(input: ProjectStateInputModel): String
    fun deleteProjectState(input: ProjectStateInputModel): String
}