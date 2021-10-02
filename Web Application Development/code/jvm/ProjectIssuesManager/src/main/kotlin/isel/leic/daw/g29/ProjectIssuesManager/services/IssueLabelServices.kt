package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbIssueLabel
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.DomainNotFoundException
import isel.leic.daw.g29.ProjectIssuesManager.models.IssueLabel
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IIssueLabelService
import org.springframework.stereotype.Service

@Service
class IssueLabelServices(private val DbIssueLabel: IDbIssueLabel): IIssueLabelService {
    override fun getIssueLabel(name: String, id: Int): List<IssueLabel> = DbIssueLabel.getIssueLabels(name, id)

    override fun postIssueLabel(input: IssueLabelInputModel): String = DbIssueLabel.postIssueLabel(input)

    override fun deleteIssueLabel(input: IssueLabelInputModel): String = DbIssueLabel.deleteIssueLabel(input)

}