package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbProject
import isel.leic.daw.g29.ProjectIssuesManager.database.DbProject
import isel.leic.daw.g29.ProjectIssuesManager.models.Project
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IProjectService
import org.springframework.stereotype.Service

@Service
class ProjectServices(private val DbProject: IDbProject): IProjectService {
    override fun getProjects(): List<Project> = DbProject.getProjects()
    override fun getProject(name: String): Project = DbProject.getProject(name)
    override fun postProject(project: ProjectInputModel): String = DbProject.postProject(project)
    override fun deleteProject(name: ProjectInputModel): String = DbProject.deleteProject(name)
}