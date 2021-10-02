package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.ProjectLabelOutputModel

data class ProjectLabel (
    val projectName: String,
    val labelName: String
) {
    fun mapToOutputModel () = ProjectLabelOutputModel(projectName, labelName)
}