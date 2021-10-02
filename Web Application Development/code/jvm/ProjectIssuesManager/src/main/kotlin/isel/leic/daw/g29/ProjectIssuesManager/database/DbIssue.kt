package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.ProjectIssuesManagerApplication
import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbIssue
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.*
import isel.leic.daw.g29.ProjectIssuesManager.models.Comment
import isel.leic.daw.g29.ProjectIssuesManager.models.Issue
import isel.leic.daw.g29.ProjectIssuesManager.models.NextState
import isel.leic.daw.g29.ProjectIssuesManager.models.Project
import org.jdbi.v3.core.Jdbi
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.lang.IllegalStateException

@Repository
class DbIssue(private val jdbi: Jdbi) : IDbIssue {
    override fun getIssues(projectName: String): List<Issue> = jdbi.withHandle<List<Issue>, Exception> { handle ->
        try {
            handle.createQuery("select * from Issue where projectName = :projectName")
                .bind("projectName", projectName)
                .mapTo(Issue::class.java)
                .list()


        } catch (e: Exception) {
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}. Error cause = ${e.cause}")
        }
    }

    override fun getIssue(projectName: String, id: Int): Issue {
        try {
            return jdbi.withHandle<Issue, Exception> { handle ->
                handle.createQuery("select * from issue where projectName = :projectName and id = :issueId")
                    .bind("projectName", projectName)
                    .bind("issueId", id)
                    .mapTo(Issue::class.java).first()
            }
        } catch (e: IllegalStateException) {
            throw DomainNotFoundException("Issue not found.")
        } catch (e: Exception) {
            throw DbAccessException("Problems with connection to the db. Error message = ${e.message}")
        }
    }
    private val log = LoggerFactory.getLogger(ProjectIssuesManagerApplication::class.java)
    override fun postIssue(issue: IssueInputModel): String {
        try {
            val ret: Int = jdbi.inTransaction<Int, Exception> { handle ->

                log.info("------------------------------> ")

                val project: Project = handle.createQuery("SELECT * FROM project WHERE name = :name")
                    .bind("name", issue.projectName)
                    .mapTo(Project::class.java).first()

                handle.execute(
                    "INSERT INTO issue (projectName, name, description, endDate, stateName) " +
                            "VALUES('${issue.projectName}', '${issue.name}', '${issue.description}', ${null}, '${project.startState}')"
                )
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS,  inserted ${issue.name} $ret rows affected"

        } catch (e: Exception) {
            if (e.cause is PSQLException) {
                var sqlExcept: PSQLException = e.cause as PSQLException
                when {
                    sqlExcept.sqlState.equals("23505") -> {
                        throw KeyViolationException("DUPLICATED KEY -> ${issue.projectName}, ${issue.id}}")
                    }
                    sqlExcept.sqlState.equals("23503") -> {
                        throw ForeignKeyException("Foreign key exception in postIssue")
                    }
                    sqlExcept.sqlState.equals("23502") -> {
                        throw NotNullException("Non-null value was inserted as null")
                    }
                    else -> throw IntegrityConstraintException("Problems with constraints in request" + e.message)
                }
            }
            throw DbAccessException("Problems with connection in Post")
        }
    }

    override fun putIssueUpdate(issue: IssueInputModel): String {

        try {
            val ret: Int = jdbi.inTransaction<Int, Exception> { handle ->

                val currentIssue: Issue = handle.createQuery("SELECT * FROM issue WHERE projectName = :projectName AND id = :id")
                    .bind("projectName", issue.projectName)
                    .bind("id", issue.id)
                    .mapTo(Issue::class.java).first()

                handle.createQuery("SELECT * FROM nextstate WHERE projectName = :projectName AND stateName = :stateName AND nextState = :nextState")
                    .bind("projectName", issue.projectName)
                    .bind("stateName", currentIssue.stateName)
                    .bind("nextState", issue.stateName)
                    .mapTo(NextState::class.java).first()

                handle.execute(
                    "UPDATE issue SET stateName = (?) WHERE projectName = (?) AND id = (?) ",
                    issue.stateName,
                    issue.projectName,
                    issue.id
                )

            }

            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, update ${issue.id} ${issue.stateName} $ret rows affected"

        } catch (e: Exception) {
            if (e.cause is PSQLException)
                throw KeyViolationException("DUPLICATED KEY ---> ${e.message}")
            throw DbAccessException("Problems with connection in Post")
        }
    }


    override fun deleteIssue(input: IssueInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute(
                    "DELETE FROM issue WHERE projectName = (?) and id = (?)",
                    input.projectName, input.id
                )
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, Deleted ${input.id} $ret rows affected"

        }catch (e: ControllerException){
            throw DomainNotFoundException("The issue -> ${input.projectName}, ${input.id} wasn't found")
        } catch (e: Exception) {
            if (e.cause is PSQLException)
                throw DomainNotFoundException("NOT FOUND domain Issue")
            throw DbAccessException("Problems with connection in Delete")
        }
    }
}
