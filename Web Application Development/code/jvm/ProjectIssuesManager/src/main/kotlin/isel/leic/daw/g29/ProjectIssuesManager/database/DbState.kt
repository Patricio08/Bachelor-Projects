package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.StateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbCollaborator
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbState
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.*
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.State
import org.jdbi.v3.core.Jdbi
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Repository

@Repository
class DbState(private val jdbi: Jdbi): IDbState {
    override fun getStates(): List<State> = jdbi.withHandle<List<State>, Exception> { handle ->
        try{
            handle.createQuery("select * from state")
                .mapTo(State::class.java)
                .list()
        }catch (e: Exception){
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}")
        }
    }

    override fun postState(input: StateInputModel): String {
        try{
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("INSERT INTO state VALUES(?)", input.stateName)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, inserted ${input.stateName}"

        }catch(e: Exception){
            if(e.cause is PSQLException){
                if((e.cause as PSQLException).sqlState.equals("23505")){
                    throw KeyViolationException("DUPLICATED KEY -> ${input.stateName}")
                }
                throw IntegrityConstraintException("Problems with constraints in request")
            }
            throw DbAccessException("Problems with connection in Post")
        }
    }

    override fun deleteState(input: StateInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("DELETE FROM state WHERE stateName = (?)", input.stateName)
        }
        if (ret == 0) throw ControllerException("No rows affected")

        return "SUCCESS, deleted ${input.stateName}"

        }catch (e: ControllerException){
            throw DomainNotFoundException("The state -> ${input.stateName} wasn't found")
        } catch (e: Exception) {
            if (e.cause is PSQLException)  throw DomainNotFoundException("NOT FOUND domain state")
            throw DbAccessException("Problems with connection in delete")
        }
    }
}