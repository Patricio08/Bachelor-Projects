package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbProjectLabel
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.DomainNotFoundException
import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectLabel
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IProjectLabelService
import org.springframework.stereotype.Service

@Service
class ProjectLabelServices(private val dbProjectLabel: IDbProjectLabel): IProjectLabelService {
    override fun getProjectLabels(name: String): List<ProjectLabel> = dbProjectLabel.getProjectLabels(name)

    override fun postProjectLabel(input: ProjectLabelInputModel): String = dbProjectLabel.postProjectLabel(input)

    override fun deleteProjectLabel(input: ProjectLabelInputModel): String  = dbProjectLabel.deleteProjectLabel(input)
}