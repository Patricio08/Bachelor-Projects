package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CommentInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.LabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Comment

interface IDbComment {
    fun getComments(projectName: String, issueId: Int): List<Comment>
    fun getComment(projectName: String, issueId: Int, CommentId: Int): Comment
    fun postComment(comment: CommentInputModel): String
    fun deleteComment(comment: CommentInputModel): String
}