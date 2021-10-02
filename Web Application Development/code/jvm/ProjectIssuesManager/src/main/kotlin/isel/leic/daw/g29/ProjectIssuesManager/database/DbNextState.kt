package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.LabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.NextStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbLabel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbNextState
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.*
import isel.leic.daw.g29.ProjectIssuesManager.models.Label
import isel.leic.daw.g29.ProjectIssuesManager.models.NextState
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Repository

@Repository
class DbNextState(private val jdbi: Jdbi) : IDbNextState {
    override fun getNextStates(projectName: String, stateName: String): List<NextState> = jdbi.withHandle<List<NextState>, Exception> { handle ->
        try {
            handle.createQuery("select * from nextState where projectName = :projectName and stateName = :stateName")
                    .bind("projectName", projectName)
                    .bind("stateName", stateName)
                    .mapTo(NextState::class.java)
                    .list()
        } catch (e: Exception) {
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}")
        }
    }

    override fun postNextState(nextState: NextStateInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("INSERT INTO NEXTSTATE VALUES(?, ?, ?)", nextState.projectName, nextState.stateName, nextState.nextState)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, inserted ${nextState.stateName} to ${nextState.nextState} $ret rows affected"

        }catch(e: Exception){
            if(e.cause is PSQLException){
                var sqlExcept: PSQLException = e.cause as PSQLException
                when {
                    sqlExcept.sqlState.equals("23505") -> {
                        throw KeyViolationException("DUPLICATED KEY -> ${nextState.projectName}, ${nextState.stateName}, ${nextState.nextState}")
                    }
                    sqlExcept.sqlState.equals("23503") -> {
                        throw ForeignKeyException("Foreign key exception in postNextState")
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
}