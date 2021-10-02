package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.ProjectIssuesManagerApplication
import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbProject
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.DbAccessException
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.DomainNotFoundException
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.IntegrityConstraintException
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.KeyViolationException
import isel.leic.daw.g29.ProjectIssuesManager.models.Project
import isel.leic.daw.g29.ProjectIssuesManager.models.State
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.lang.IllegalStateException

private val log = LoggerFactory.getLogger(ProjectIssuesManagerApplication::class.java)

@Repository
class DbProject(private val jdbi: Jdbi) : IDbProject {

    override fun getProjects(): List<Project> = jdbi.withHandle<List<Project>, Exception> { handle ->
        try {
            handle.createQuery("SELECT * FROM project")
                .mapTo(Project::class.java)
                .list()
        } catch (e: Exception) {
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}")
        }

    }

    override fun getProject(name: String): Project = jdbi.withHandle<Project, Exception> { handle ->
        try {
            handle.createQuery("SELECT * FROM project WHERE name = :projectName")
                .bind("projectName", name)
                .mapTo(Project::class.java).first()
        } catch (e: IllegalStateException) {
            throw DomainNotFoundException("Project $name not found.")
        } catch (e: Exception) {
            throw DbAccessException("Problems with connection to the db in GetById. Error message = ${e.message}")
        }

    }

    override fun postProject(project: ProjectInputModel): String {

        try {
            jdbi.inTransaction<Int, Exception> { handle ->
                // If there is none exception is thrown
                /*handle.createQuery("SELECT * FROM state WHERE stateName = :stateName")
                    .bind("stateName", project.startState)
                    .mapTo(State::class.java).first()*/

                handle.execute(
                    "INSERT INTO project VALUES(?, ?, ?)",
                    project.name,
                    project.description,
                    project.startState
                )

                handle.execute("INSERT INTO projectstate VALUES(?, ?)", project.name, project.startState)

                // TODO fix hardcoded
                handle.execute("INSERT INTO projectstate VALUES(?, ?)", project.name, "closed")

                handle.execute("INSERT INTO projectstate VALUES(?, ?)", project.name, "archived")


                handle.execute("INSERT INTO nextstate VALUES(?, ?, ?)", project.name, project.startState, "closed")
                handle.execute("INSERT INTO nextstate VALUES(?, ?, ?)", project.name, "closed", "archived")
            }

            /*jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("INSERT INTO project VALUES(?, ?, ?)", project.name, project.description, project.startState)
            }*/
            return "Project " + project.name + " SUCCESSFULLY CREATED!"
        } catch (e: Exception) {
            if (e.cause is PSQLException) {
                if ((e.cause as PSQLException).sqlState.equals("23505")) {
                    throw KeyViolationException("DUPLICATED KEY -> ${project.name}")
                }
                throw IntegrityConstraintException("Problems with constraints in request, " + e.message)
            }
            throw DbAccessException("Problems with connection in Post, " + e.message)
        }
    }

    override fun deleteProject(project: ProjectInputModel): String {
        try {
            jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("DELETE FROM project WHERE name = (?)", project.name)
            }
            return "Delete project " + project.name + " successfully"
        } catch (e: IllegalStateException) {
            throw DomainNotFoundException("Project $project.name not found.")
        } catch (e: Exception) {
            throw DbAccessException("Problems with connection to the db in GetById. Error message = ${e.message}")
        }

    }
}
