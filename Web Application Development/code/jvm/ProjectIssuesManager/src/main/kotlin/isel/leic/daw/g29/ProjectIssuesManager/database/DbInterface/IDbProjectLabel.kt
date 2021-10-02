package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectLabel

interface IDbProjectLabel {
    fun getProjectLabels(name: String): List<ProjectLabel>
    fun postProjectLabel(projectLabel: ProjectLabelInputModel): String
    fun deleteProjectLabel(projectLabel: ProjectLabelInputModel): String
}