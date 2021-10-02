package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.Issue

interface IDbIssue {
    fun getIssues(projectName: String): List<Issue>
    fun getIssue(projectName: String, id: Int): Issue
    fun postIssue(issue: IssueInputModel): String
    fun putIssueUpdate(input: IssueInputModel): String
    fun deleteIssue(issue: IssueInputModel): String
}