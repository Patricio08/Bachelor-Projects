package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CollaboratorInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator

interface ICollaboratorService {
    fun getCollaborator(name: String): Collaborator
    fun getCollaborators(): List<Collaborator>
    fun postCollaborator(input: CollaboratorInputModel): String
}