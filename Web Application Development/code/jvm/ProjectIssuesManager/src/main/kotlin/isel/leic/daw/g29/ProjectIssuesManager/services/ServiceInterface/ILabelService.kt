package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.LabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Label

interface ILabelService {
    fun getLabels(): List<Label>
    fun postLabel(input: LabelInputModel): String
    fun deleteLabel(input: LabelInputModel): String
}