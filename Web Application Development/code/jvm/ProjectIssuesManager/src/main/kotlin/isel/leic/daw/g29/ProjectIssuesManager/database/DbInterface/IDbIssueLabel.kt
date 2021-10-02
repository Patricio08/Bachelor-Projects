package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.IssueLabel

interface IDbIssueLabel {
    fun getIssueLabels(name: String, id: Int): List<IssueLabel>
    fun postIssueLabel(issueLabel: IssueLabelInputModel): String
    fun deleteIssueLabel(issueLabel: IssueLabelInputModel): String
}