package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.CollaboratorOutputModel

data class Collaborator(
    val name: String,
    val password: String,
){
    fun mapToOutputModel () = CollaboratorOutputModel(name, password)
}
