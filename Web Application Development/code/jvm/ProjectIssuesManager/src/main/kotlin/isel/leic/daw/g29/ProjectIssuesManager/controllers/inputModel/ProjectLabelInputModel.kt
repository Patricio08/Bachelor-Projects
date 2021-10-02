package isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel

import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectLabel

data class ProjectLabelInputModel(
    var projectName: String?,
    val labelName: String?
) {
    /*fun ProjectLabel() = ProjectLabel(projectName, labelName)*/
}