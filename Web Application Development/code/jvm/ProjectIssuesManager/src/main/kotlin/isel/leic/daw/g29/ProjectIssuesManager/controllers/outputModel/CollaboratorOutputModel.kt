package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.COLLABORATOR_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*



data class CollaboratorOutputModel(
        val userName: String,
        val password: String
) : IOutputModel<CollaboratorOutputModel> {
    override fun mapToSiren(): SirenEntity<CollaboratorOutputModel> = SirenEntity(
            properties = this,
            clazz = listOf("Collaborator"),
            links = listOf(selfLink(COLLABORATOR_PATH(userName))),
    )

    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<CollaboratorOutputModel> = EmbeddedEntity<CollaboratorOutputModel>(
            clazz = listOf("Collaborator"),
            rel = listOf("item"),
            properties = this,
            links = listOf(selfLink(COLLABORATOR_PATH(userName)))
    )
}


