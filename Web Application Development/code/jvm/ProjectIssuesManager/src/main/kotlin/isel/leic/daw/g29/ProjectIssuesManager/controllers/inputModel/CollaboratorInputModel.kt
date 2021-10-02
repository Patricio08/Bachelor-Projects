package isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel

import isel.leic.daw.g29.ProjectIssuesManager.models.Collaborator

data class CollaboratorInputModel(
    val name: String,
    val password: String
) {
    fun mapToCollaborator() = Collaborator(name, password)
}