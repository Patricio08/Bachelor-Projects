package isel.leic.daw.g29.ProjectIssuesManager.controllers


import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CommentInputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.CommentOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.CommentsOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.ICommentSevice
import org.springframework.web.bind.annotation.*

@RestController
class CommentController(private val CommentServices: ICommentSevice) {

    @PostMapping(COMMENTS_PART, "application/json")
    fun postComment(
            @RequestBody input: CommentInputModel,
            @PathVariable name: String,
            @PathVariable id: Int
    ): String {
        input.projectName = name
        input.issueId = id
        println(input)
        return CommentServices.postComment(input)
    }

    @GetMapping(COMMENTS_PART)
    fun getComment(
            @PathVariable name: String,
            @PathVariable id: Int
    ): SirenEntity<CommentsOutputModel> {
        val comments = CommentServices.getComments(name, id)
                .map { Comment -> Comment.mapToOutputModel() }
        return CommentsOutputModel(name, id, comments.size).mapToSiren(comments)
    }

    @GetMapping(COMMENT_PART)
    fun getComment(
            @PathVariable name: String,
            @PathVariable id: Int,
            @PathVariable commentId: Int
    ): SirenEntity<CommentOutputModel> = CommentServices.getComment(name, id, commentId).mapToOutputModel().mapToSiren()

    @DeleteMapping(COMMENTS_PART, "application/json")
    fun deleteLabel(
            @PathVariable name: String,
            @PathVariable id: Int,
            @RequestBody comment: CommentInputModel
    ): String {
        comment.issueId = id
        comment.projectName = name
        return CommentServices.deleteComment(comment)
    }

}