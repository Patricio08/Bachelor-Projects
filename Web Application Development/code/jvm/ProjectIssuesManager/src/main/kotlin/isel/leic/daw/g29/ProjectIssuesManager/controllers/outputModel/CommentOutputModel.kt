package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.COMMENT_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*


data class CommentOutputModel(
        val projectName: String,
        val issueId: Int,
        val id: Int,
        val text: String,
        val date: String
) : IOutputModel<CommentOutputModel> {
    override fun mapToSiren(): SirenEntity<CommentOutputModel> = SirenEntity(
            properties = this,
            clazz = listOf("comment"),
            links = listOf(selfLink(COMMENT_PATH(projectName, issueId, id))),
    )

    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<CommentOutputModel> = EmbeddedEntity<CommentOutputModel>(
            clazz = listOf("Comment"),
            rel = listOf("item"),
            properties = this,
            links = listOf(selfLink(COMMENT_PATH(projectName, issueId, id)))
    )
}

