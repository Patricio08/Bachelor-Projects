package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.COLLABORATORS_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class CollaboratorsOutputModel(
        val collectionSize: Int
) : IOutputModelList<CollaboratorsOutputModel, CollaboratorOutputModel> {
    override fun mapToSiren(list: List<CollaboratorOutputModel>): SirenEntity<CollaboratorsOutputModel> = SirenEntity(
            clazz = listOf("Collaborators", "collection"),
            properties = this,
            entities = list.map(CollaboratorOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_COLLABORATOR),
            links = listOf(selfLink(URI(COLLABORATORS_PATH)))
    )
}

val POST_COLLABORATOR = SirenAction(
        name = "add-collaborator",
        title = "Post Collaborator",
        method = HttpMethod.POST,
        href = URI(COLLABORATORS_PATH),
        type = MediaType.APPLICATION_JSON,
        fields = listOf(SirenAction.Field(name = "userName", type = "text"), SirenAction.Field(name = "password", type = "text"))
)

