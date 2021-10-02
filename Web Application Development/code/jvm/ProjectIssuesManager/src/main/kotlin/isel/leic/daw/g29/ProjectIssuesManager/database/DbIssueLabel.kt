package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbIssueLabel
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.*
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.IssueLabel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Repository

@Repository
class DbIssueLabel(private val jdbi: Jdbi) : IDbIssueLabel {
    override fun getIssueLabels(name: String, id: Int): List<IssueLabel> = jdbi.withHandleUnchecked { handle ->
        try {
            handle.createQuery("select * from issuelabel where projectname = :name and id = :id")
                .bind("name", name)
                .bind("id", id)
                .mapTo(IssueLabel::class.java)
                .list()
        } catch (e: Exception) {
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}")
        }
    }

    override fun postIssueLabel(issueLabel: IssueLabelInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("INSERT INTO issuelabel VALUES(?, ?, ?)", issueLabel.projectName, issueLabel.id, issueLabel.labelName)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS,  inserted ${issueLabel.id} on ${issueLabel.projectName} $ret rows affected"

        }catch(e: Exception){
            if(e.cause is PSQLException){
                var sqlExcept: PSQLException = e.cause as PSQLException
                when {
                    sqlExcept.sqlState.equals("23505") -> {
                        throw KeyViolationException("DUPLICATED KEY -> ${issueLabel.projectName}, ${issueLabel.id}, ${issueLabel.labelName}")
                    }
                    sqlExcept.sqlState.equals("23503") -> {
                        throw ForeignKeyException("Foreign key exception in postIssueLabel")
                    }
                    sqlExcept.sqlState.equals("23502") -> {
                        throw NotNullException("Non-null value was inserted as null")
                    }
                    else -> throw IntegrityConstraintException("Problems with constraints in request")
                }
            }
            throw DbAccessException("Problems with connection in Post")
        }
    }

    override fun deleteIssueLabel(issueLabel: IssueLabelInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("DELETE FROM issuelabel WHERE projectName = (?) AND id = (?) AND labelName = (?)", issueLabel.projectName, issueLabel.id, issueLabel.labelName)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, deleted ${issueLabel.id} on ${issueLabel.projectName} $ret rows affected"

        }catch (e: ControllerException){
            throw DomainNotFoundException("The issueLabel -> ${issueLabel.projectName}, ${issueLabel.id}, ${issueLabel.labelName} wasn't found")
        } catch (e: Exception) {
            if (e.cause is PSQLException)
                throw DomainNotFoundException("NOT FOUND domain label")
            throw DbAccessException("Problems with connection in Post")
        }
    }


}