package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.LabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbLabel
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.*
import isel.leic.daw.g29.ProjectIssuesManager.models.IssueLabel
import isel.leic.daw.g29.ProjectIssuesManager.models.Label
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Repository

@Repository
class DbLabel(private val jdbi: Jdbi) : IDbLabel {
    override fun getLabels(): List<Label> = jdbi.withHandle<List<Label>, Exception> { handle ->
        try {
            handle.createQuery("select * from label")
                .mapTo(Label::class.java)
                .list()
        } catch (e: Exception) {
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}")
        }
    }

    override fun postLabel(label: LabelInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("INSERT INTO label VALUES(?)", label.labelName)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, inserted ${label.labelName}"

        }catch(e: Exception){
            if(e.cause is PSQLException){
                if((e.cause as PSQLException).sqlState.equals("23505")){
                    throw KeyViolationException("DUPLICATED KEY -> ${label.labelName}")
                }
                throw IntegrityConstraintException("Problems with constraints in request")
            }
            throw DbAccessException("Problems with connection in Post")
        }
    }

    override fun deleteLabel(label: LabelInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("DELETE FROM label WHERE labelName = (?)", label.labelName)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, deleted  ${label.labelName}"
        }catch (e: ControllerException){
            throw DomainNotFoundException("The label -> ${label.labelName} wasn't found")
        } catch (e: Exception) {
            if (e.cause is PSQLException)
                throw DomainNotFoundException("NOT FOUND domain label")
            throw DbAccessException("Problems with connection in Delete")
        }
    }
}