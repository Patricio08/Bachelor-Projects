package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.IssueLabel

interface IIssueLabelService {
    fun getIssueLabel(name: String, id: Int): List<IssueLabel>
    fun postIssueLabel(input: IssueLabelInputModel): String
    fun deleteIssueLabel(input: IssueLabelInputModel): String
}