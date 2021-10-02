package isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel

import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.Comment


data class CommentInputModel(
        var projectName: String?,
        var issueId: Int?,
        var id: Int?,
        var text: String?,
        var date: String?
) {
    //fun mapToComment() = Comment(projectName, issueId, id, text, date)
}