package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Issue

interface IIssueService {
    fun getIssue(projectName: String, id: Int): Issue
    fun getIssues(projectName: String): List<Issue>
    fun postIssue(input: IssueInputModel): String
    fun putIssue(input: IssueInputModel): String
    fun deleteIssue(input: IssueInputModel): String
}