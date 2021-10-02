package isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel

import isel.leic.daw.g29.ProjectIssuesManager.models.Issue
import java.sql.Timestamp

data class IssueInputModel(
    var projectName: String?,
    var id: Int?,
    var name: String?,
    var description: String?,
    var beginDate: Timestamp?,
    var endDate: Timestamp?,
    var stateName: String?
) {
    //fun mapToIssue() = Issue(projectName, id, name, description, beginDate, endDate, stateName)
}