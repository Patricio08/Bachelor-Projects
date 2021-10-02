package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.IssueOutputModel
import java.sql.Timestamp

data class Issue (
    val projectName: String,
    val id: Int,
    val name: String,
    val description: String,
    val beginDate: Timestamp,
    val endDate: Timestamp?,
    val stateName: String
) {
    fun mapToOutputModel () = IssueOutputModel(projectName, id, name, description, beginDate, endDate, stateName)
}



