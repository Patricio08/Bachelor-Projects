package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbProjectState
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.DomainNotFoundException
import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectState
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IProjectStateService
import org.springframework.stereotype.Service

@Service
class ProjectStateServices(private val DbProjectState: IDbProjectState): IProjectStateService {
    override fun getProjectStates(projectName: String): List<ProjectState> =  DbProjectState.getProjectStates(projectName)

    override fun postProjectState(input: ProjectStateInputModel): String = DbProjectState.postProjectState(input)

    override fun deleteProjectState(input: ProjectStateInputModel): String = DbProjectState.deleteProjectState(input)
}