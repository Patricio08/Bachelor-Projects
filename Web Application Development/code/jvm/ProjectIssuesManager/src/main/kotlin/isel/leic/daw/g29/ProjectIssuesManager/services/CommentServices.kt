package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CommentInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbCollaborator
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbComment
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.DomainNotFoundException
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.Comment
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.ICollaboratorService
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.ICommentSevice
import org.springframework.stereotype.Service

@Service
class CommentServices(private val DbComment: IDbComment): ICommentSevice {
    override fun getComment(projectName: String, issueId: Int, CommentId: Int): Comment = DbComment.getComment(projectName, issueId, CommentId)
    override fun getComments(projectName: String, issueId: Int) :List<Comment> = DbComment.getComments(projectName, issueId)
    override fun postComment(input: CommentInputModel) = DbComment.postComment(input)
    override fun deleteComment(input: CommentInputModel) = DbComment.deleteComment(input)

}