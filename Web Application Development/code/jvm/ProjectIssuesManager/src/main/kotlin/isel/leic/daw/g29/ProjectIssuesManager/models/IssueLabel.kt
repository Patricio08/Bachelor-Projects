package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.IssueLabelOutputModel

data class IssueLabel (
    val projectName: String,
    val id: Int,
    val labelName: String,
) {
    fun mapToOutputModel () = IssueLabelOutputModel(projectName, id, labelName)
}