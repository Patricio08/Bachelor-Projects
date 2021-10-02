package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.CommentOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.ProjectOutputModel

data class Comment(
        val projectName: String,
        val issueId: Int,
        val id: Int,
        val text: String,
        val date: String
){
    fun mapToOutputModel () = CommentOutputModel(projectName, issueId, id, text, date)
}