package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CollaboratorInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbCollaborator
import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.ICollaboratorService
import org.springframework.stereotype.Service

@Service
class CollaboratorServices(private val DbCollaborator: IDbCollaborator): ICollaboratorService {
    override fun getCollaborator(name: String) = DbCollaborator.getCollaborator(name)
    override fun getCollaborators() = DbCollaborator.getCollaborators()
    override fun postCollaborator(input: CollaboratorInputModel) = DbCollaborator.postCollaborator(input)
}