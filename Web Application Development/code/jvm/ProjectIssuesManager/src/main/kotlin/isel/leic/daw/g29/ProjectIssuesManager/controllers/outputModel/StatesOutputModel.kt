package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths
import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.STATE_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class StatesOutputModel(
        val collectionSize: Int
) : IOutputModelList<StatesOutputModel, StateOutputModel> {
    override fun mapToSiren(list: List<StateOutputModel>): SirenEntity<StatesOutputModel> = SirenEntity(
            clazz = listOf("States", "collection"),
            properties = this,
            entities = list.map(StateOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_STATE, DELETE_STATE),
            links = listOf(selfLink(URI(STATE_PATH)))
    )
}

val POST_STATE = SirenAction(
        name = "add-State",
        title = "Post State",
        method = HttpMethod.POST,
        href = URI(STATE_PATH),
        type = MediaType.APPLICATION_JSON,
        fields = listOf(SirenAction.Field(name = "stateName", type = "text"))
)

val DELETE_STATE = SirenAction(
        name = "remove-state",
        title = "Delete State",
        href = URI(Paths.STATE_PATH),
        method = HttpMethod.DELETE,
        type = MediaType.APPLICATION_JSON,
        fields = listOf(SirenAction.Field(name = "stateName", type = "text"))
)