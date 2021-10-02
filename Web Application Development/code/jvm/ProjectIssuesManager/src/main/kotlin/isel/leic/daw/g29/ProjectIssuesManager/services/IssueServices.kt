package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbIssue
import isel.leic.daw.g29.ProjectIssuesManager.database.DbIssue
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.DomainNotFoundException
import isel.leic.daw.g29.ProjectIssuesManager.models.Issue
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IIssueService
import org.springframework.stereotype.Service

@Service
class IssueServices(private val DbIssue: IDbIssue): IIssueService {
    override fun getIssue(projectName: String, id: Int): Issue = DbIssue.getIssue(projectName, id)

    override fun getIssues(projectName: String): List<Issue> = DbIssue.getIssues(projectName)

    override fun postIssue(input: IssueInputModel): String = DbIssue.postIssue(input)

    override fun putIssue(input: IssueInputModel): String = DbIssue.putIssueUpdate(input)

    override fun deleteIssue(input: IssueInputModel) = DbIssue.deleteIssue(input)

}