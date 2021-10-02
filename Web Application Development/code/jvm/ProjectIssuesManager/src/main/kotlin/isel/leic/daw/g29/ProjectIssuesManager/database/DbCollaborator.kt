package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CollaboratorInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbCollaborator
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.*
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.postgresql.util.PSQLException
import org.springframework.boot.autoconfigure.quartz.QuartzProperties
import org.springframework.stereotype.Repository
import org.springframework.web.util.NestedServletException
import java.lang.IllegalStateException
import java.sql.SQLIntegrityConstraintViolationException


@Repository
class DbCollaborator(private val jdbi: Jdbi): IDbCollaborator {
    override fun getCollaborator(name: String): Collaborator {
        try{
            return jdbi.withHandle<Collaborator, Exception> { handle ->
                handle.createQuery("select * from collaborator where name = :collaboratorName")
                    .bind("collaboratorName", name)
                    .mapTo(Collaborator::class.java).first()
            }
        }catch (e: IllegalStateException) {
            throw DomainNotFoundException("Collaboratorr $name not found.")
        }catch (e: Exception){
            throw DbAccessException("Problems with connection to the db in GetById. Error message = ${e.message}")
        }
    }

    override fun getCollaborators(): List<Collaborator> = jdbi.withHandle<List<Collaborator>, Exception> { handle ->
        try{
            handle.createQuery("select * from collaborator")
                .mapTo(Collaborator::class.java)
                .list()
        }catch (e: Exception){
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}")
        }
    }

    override fun postCollaborator(collab: CollaboratorInputModel) : String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("INSERT INTO collaborator VALUES(?, ?)", collab.name, collab.password)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, inserted ${collab.name} $ret rows affected"

        }catch(e: Exception){
                if(e.cause is PSQLException){
                    if((e.cause as PSQLException).sqlState.equals("23505")){
                        throw KeyViolationException("DUPLICATED KEY -> ${collab.name}")
                    }
                    throw IntegrityConstraintException("Problems with constraints in request")
                }
                throw DbAccessException("Problems with connection in Post")
        }
    }
}