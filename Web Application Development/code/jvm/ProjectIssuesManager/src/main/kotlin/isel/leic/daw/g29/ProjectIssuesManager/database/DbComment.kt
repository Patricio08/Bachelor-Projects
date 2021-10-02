package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.ProjectIssuesManagerApplication
import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CommentInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbCollaborator
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbComment
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.*
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.Comment
import isel.leic.daw.g29.ProjectIssuesManager.models.Issue
import org.jdbi.v3.core.Jdbi
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.lang.IllegalStateException

@Repository
class DbComment(private val jdbi: Jdbi): IDbComment {
    override fun getComment(projectName: String, issueId: Int, CommentId: Int): Comment {
        try{
            return jdbi.withHandle<Comment, Exception> { handle ->
                handle.createQuery("select * from comment where projectName = :projectName and issueId = :issueId and id = :CommentId ORDER BY date DESC")
                        .bind("projectName", projectName)
                        .bind("issueId", issueId)
                        .bind("CommentId", CommentId)
                        .mapTo(Comment::class.java).first()
            }
        }catch (e: IllegalStateException) {
            throw DomainNotFoundException("Comment not found.")
        }catch (e: Exception){
            throw DbAccessException("Problems with connection to the db. Error message = ${e.message}")
        }
    }

    override fun getComments(projectName: String, issueId: Int): List<Comment> = jdbi.withHandle<List<Comment>, Exception> { handle ->
        try{
            handle.createQuery("select * from Comment where projectName = :projectName and issueId = :issueId")
                    .bind("projectName", projectName)
                    .bind("issueId", issueId)
                    .mapTo(Comment::class.java)
                    .list()
        }catch (e: Exception){
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}")
        }
    }


    override fun postComment(Comment: CommentInputModel) : String {
        try {
            val ret: Int = jdbi.inTransaction<Int, Exception> { handle ->
                // If there is none exception is thrown
                var aux = handle.createQuery("SELECT * FROM issue WHERE projectName = :projectName AND id = :issueId ")
                    .bind("projectName", Comment.projectName)
                    .bind("issueId", Comment.issueId)
                    .mapTo(Issue::class.java).first()

                if (aux.stateName != "archived") {
                    handle.execute(
                        "INSERT INTO comment (projectName, issueId, text) VALUES('${Comment.projectName}', '${Comment.issueId}', '${Comment.text}')",
                    )
                } else {
                    throw CommentArchivedException("This issue is archived")
                }
            }

            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS,  inserted ${Comment.text} $ret rows affected"

        }catch(e: CommentArchivedException){
            throw CommentArchivedException("This issue is archived")
        }catch(e: Exception){
            if(e.cause is PSQLException){
                var sqlExcept: PSQLException = e.cause as PSQLException
                when {
                    sqlExcept.sqlState.equals("23505") -> {
                        throw KeyViolationException("DUPLICATED KEY -> ${Comment.projectName}, ${Comment.issueId}, ${Comment.id}}")
                    }
                    sqlExcept.sqlState.equals("23503") -> {
                        throw ForeignKeyException("Foreign key exception in postComment")
                    }
                    sqlExcept.sqlState.equals("23502") -> {
                        throw NotNullException("Non-null value was inserted as null")
                    }
                    else -> throw IntegrityConstraintException("Problems with constraints in request")
                }
            }
            throw DbAccessException("Problems with connection in Post ${e.message}, ${e.cause}")
        }
    }



    override fun deleteComment(comment: CommentInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("DELETE FROM COMMENT WHERE projectName = (?) and issueId = (?) and id = (?)",
                        comment.projectName, comment.issueId, comment.id)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, Deleted ${comment.id} $ret rows affected"

        }catch (e: ControllerException){
            throw DomainNotFoundException("The comment wasn't found")

        } catch (e: Exception) {
            if (e.cause is PSQLException)
                throw DomainNotFoundException("NOT FOUND domain Comment")
            throw DbAccessException("Problems with connection in Delete")
        }
    }
}