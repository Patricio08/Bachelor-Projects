package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.COMMENTS_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType


data class CommentsOutputModel(
        val projectName: String,
        val issueId: Int,
        val collectionSize: Int
) : IOutputModelList<CommentsOutputModel, CommentOutputModel> {
    override fun mapToSiren(list: List<CommentOutputModel>): SirenEntity<CommentsOutputModel> = SirenEntity(
            clazz = listOf("Comments"),
            properties = this,
            entities = list.map(CommentOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_COMMENT(), DELETE_COMMENT()),
            links = listOf(selfLink(COMMENTS_PATH(projectName, issueId)))
    )

    fun POST_COMMENT() = SirenAction(
            name = "add-comment",
            title = "Post Comment",
            method = HttpMethod.POST,
            href = COMMENTS_PATH(projectName, issueId),
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "text", type = "text"))
    )

    fun DELETE_COMMENT() = SirenAction(
            name = "remove-comment",
            title = "Delete Comment",
            method = HttpMethod.DELETE,
            href = COMMENTS_PATH(projectName, issueId),
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "id", type = "text"))
    )
}

