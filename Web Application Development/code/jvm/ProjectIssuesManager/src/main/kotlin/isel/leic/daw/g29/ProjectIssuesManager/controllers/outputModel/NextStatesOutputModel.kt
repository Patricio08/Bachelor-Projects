package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.NEXTSTATE_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType

data class NextStatesOutputModel(
        val projectName: String,
        val stateName: String,
        val collectionSize: Int
) : IOutputModelList<NextStatesOutputModel, NextStateOutputModel> {

    override fun mapToSiren(list: List<NextStateOutputModel>): SirenEntity<NextStatesOutputModel> = SirenEntity(
            clazz = listOf("NextState", "collection"),
            properties = this,
            entities = list.map(NextStateOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_NEXTSTATE()),
            links = listOf(selfLink(NEXTSTATE_PATH(projectName, stateName)))
    )

    fun POST_NEXTSTATE(): SirenAction = SirenAction(
            name = "add-nextstate",
            title = "Post Next State",
            href = NEXTSTATE_PATH(projectName, stateName),
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "nextState", type = "text"))
    )
}

