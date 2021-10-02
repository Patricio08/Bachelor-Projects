package isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel

import isel.leic.daw.g29.ProjectIssuesManager.models.IssueLabel

data class IssueLabelInputModel (
    var projectName: String?,
    var id: Int?,
    var labelName: String?,
) {
    /*fun mapToIssueLabel() = IssueLabel(projectName, id, labelname)*/
}