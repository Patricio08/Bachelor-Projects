package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Project

interface IProjectService {
    fun getProjects(): List<Project>
    fun getProject(name: String): Project
    fun postProject(project: ProjectInputModel): String
    fun deleteProject(name: ProjectInputModel): String
}