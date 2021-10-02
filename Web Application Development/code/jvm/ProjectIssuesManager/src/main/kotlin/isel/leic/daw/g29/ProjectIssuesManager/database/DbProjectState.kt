package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbProjectState
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.*
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectState
import org.jdbi.v3.core.Jdbi
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Repository
import java.lang.IllegalStateException

@Repository
class DbProjectState(private val jdbi: Jdbi): IDbProjectState {
    override fun getProjectStates(projectName: String): List<ProjectState>  =
        jdbi.withHandle<List<ProjectState>, Exception> { handle ->
        try{
            handle.createQuery("select * from projectState where projectname = :projectName")
                .bind("projectName", projectName)
                .mapTo(ProjectState::class.java)
                .list()
        }catch (e: Exception){
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}")
        }
    }

    override fun postProjectState(input: ProjectStateInputModel): String {
        try{
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("INSERT INTO projectState VALUES(?, ?)", input.projectName, input.stateName)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, inserted ${input.stateName} $ret rows affected"

        }catch(e: Exception){
            if(e.cause is PSQLException){
                var sqlExcept: PSQLException = e.cause as PSQLException
                when {
                    sqlExcept.sqlState.equals("23505") -> {
                        throw KeyViolationException("DUPLICATED KEY -> ${input.projectName}, ${input.stateName}")
                    }
                    sqlExcept.sqlState.equals("23503") -> {
                        throw ForeignKeyException("Foreign key exception in postProjectState")
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

    override fun deleteProjectState(input: ProjectStateInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("DELETE FROM projectstate WHERE projectName = (?) AND stateName = (?)", input.projectName, input.stateName)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, deleted ${input.stateName} $ret rows affected"

        }catch (e: ControllerException){
            throw DomainNotFoundException("The projectState -> ${input.projectName}, ${input.stateName}wasn't found")
        } catch (e: Exception) {
            if (e.cause is PSQLException)
                throw DomainNotFoundException("NOT FOUND domain projectstate + ${e.message}")
            throw DbAccessException("Problems with connection in delete")
        }}
}