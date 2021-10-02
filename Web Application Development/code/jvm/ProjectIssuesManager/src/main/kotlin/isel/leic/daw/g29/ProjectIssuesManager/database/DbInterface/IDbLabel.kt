package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.LabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Issue
import isel.leic.daw.g29.ProjectIssuesManager.models.Label

interface IDbLabel {
    fun getLabels(): List<Label>
    fun postLabel(label: LabelInputModel): String
    fun deleteLabel(label: LabelInputModel): String
}