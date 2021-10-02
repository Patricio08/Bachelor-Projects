package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectLabel

interface IProjectLabelService {
    fun getProjectLabels(name: String): List<ProjectLabel>
    fun postProjectLabel(input: ProjectLabelInputModel): String
    fun deleteProjectLabel(input: ProjectLabelInputModel): String
}