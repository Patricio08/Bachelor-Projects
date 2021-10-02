package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CollaboratorInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.Project

interface IDbCollaborator {
    fun getCollaborator(name: String): Collaborator
    fun getCollaborators(): List<Collaborator>
    fun postCollaborator(collab: CollaboratorInputModel): String
}