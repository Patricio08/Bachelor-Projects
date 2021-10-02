package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Project

interface IDbProject {
    fun getProjects(): List<Project>
    fun getProject(name: String): Project
    fun postProject(project: ProjectInputModel): String
    fun deleteProject(name: ProjectInputModel): String
}