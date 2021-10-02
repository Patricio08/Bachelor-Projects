package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel


import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.COMMENTS_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.ISSUE_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.sql.Timestamp

data class IssueOutputModel(
        val projectName: String,
        val id: Int,
        val name: String,
        val description: String,
        val beginDate: Timestamp,
        val endDate: Timestamp?,
        val stateName: String
) : IOutputModel<IssueOutputModel> {
    override fun mapToSiren(): SirenEntity<IssueOutputModel> = SirenEntity<IssueOutputModel>(
            clazz = listOf("Issue"),
            properties = this,
            actions = listOf(PUT_ISSUE()),
            links = listOf(selfLink(ISSUE_PATH(projectName, id)), GOTOCOMMENTS())
    )

    fun PUT_ISSUE() = SirenAction(
            name = "put-issue",
            title = "Put Issue",
            method = HttpMethod.PUT,
            href = ISSUE_PATH(projectName, id),
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "stateName", type = "text"))
    )

    fun GOTOCOMMENTS() = SirenLink(
            href = COMMENTS_PATH(projectName, id),
            rel = listOf("Comments")
    )

    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<IssueOutputModel> = EmbeddedEntity<IssueOutputModel>(
            clazz = listOf("Issue"),
            rel = listOf("item"),
            properties = this,
            links = listOf(selfLink(ISSUE_PATH(projectName, id)))
    )
}

