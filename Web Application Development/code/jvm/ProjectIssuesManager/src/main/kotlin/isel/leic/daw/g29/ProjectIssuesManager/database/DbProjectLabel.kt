package isel.leic.daw.g29.ProjectIssuesManager.database

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbProjectLabel
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.*
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.ProjectLabel
import org.jdbi.v3.core.Jdbi
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Repository

@Repository
class DbProjectLabel(private val jdbi: Jdbi): IDbProjectLabel {

    override fun getProjectLabels(name: String): List<ProjectLabel> = jdbi.withHandle<List<ProjectLabel>, Exception> { handle ->
        try{
             handle.createQuery("select * from projectlabel where projectname = :name")
                .bind("name", name)
                .mapTo(ProjectLabel::class.java)
                .list()

        }catch (e: Exception){
            throw DbAccessException("Problems with connection to the db in GetAll. Error message = ${e.message}")
        }
    }

    override fun postProjectLabel(projectLabel: ProjectLabelInputModel): String {
        try{
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("INSERT INTO projectlabel VALUES(?, ?)", projectLabel.projectName, projectLabel.labelName)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, inserted ${projectLabel.labelName}  $ret rows affected"

        }catch(e: Exception){
            if(e.cause is PSQLException){
                var sqlExcept: PSQLException = e.cause as PSQLException
                when {
                    sqlExcept.sqlState.equals("23505") -> {
                        throw KeyViolationException("DUPLICATED KEY -> ${projectLabel.projectName}, ${projectLabel.labelName}")
                    }
                    sqlExcept.sqlState.equals("23503") -> {
                        throw ForeignKeyException("Foreign key exception in postProjectLabel")
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

    override fun deleteProjectLabel(projectLabel: ProjectLabelInputModel): String {
        try {
            val ret: Int = jdbi.withHandle<Int, Exception> { handle ->
                handle.execute("DELETE FROM projectlabel WHERE projectName = (?) AND labelName = (?)", projectLabel.projectName, projectLabel.labelName)
            }
            if (ret == 0) throw ControllerException("No rows affected")

            return "SUCCESS, deleted ${projectLabel.labelName} $ret rows affected"

        }catch (e: ControllerException){
            throw DomainNotFoundException("The Projectlabel -> ${projectLabel.projectName},  ${projectLabel.labelName} wasn't found")
        } catch (e: Exception) {
            if (e.cause is PSQLException)
                throw DomainNotFoundException("NOT FOUND domain projectlabel")
            throw DbAccessException("Problems with connection in Post")
        }
    }
}