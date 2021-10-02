package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CommentInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Comment

interface ICommentSevice {
    fun getComments(projectName: String, issueId: Int): List<Comment>
    fun getComment(projectName: String, issueId: Int, CommentId: Int): Comment
    fun postComment(comment: CommentInputModel): String
    fun deleteComment(input: CommentInputModel): String
}