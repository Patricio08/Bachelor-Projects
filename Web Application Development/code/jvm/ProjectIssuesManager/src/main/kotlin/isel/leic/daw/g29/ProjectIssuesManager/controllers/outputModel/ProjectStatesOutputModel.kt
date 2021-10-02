package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.PROJECTSTATE_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType

data class ProjectStatesOutputModel(
        val projectName: String,
        val collectionSize: Int
) : IOutputModelList<ProjectStatesOutputModel, ProjectStateOutputModel> {
    override fun mapToSiren(list: List<ProjectStateOutputModel>): SirenEntity<ProjectStatesOutputModel> = SirenEntity(
            clazz = listOf("Project-States", "collection"),
            properties = this,
            entities = list.map(ProjectStateOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_PROJECT_STATES(), DELETE_PROJECT_STATES()),
            links = listOf(selfLink(PROJECTSTATE_PATH(projectName)))
    )

    fun POST_PROJECT_STATES(): SirenAction = SirenAction(
            name = "add-project-state",
            title = "Post Project State",
            href = PROJECTSTATE_PATH(projectName),
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "stateName", type = "text"))
    )

    fun DELETE_PROJECT_STATES(): SirenAction = SirenAction(
            name = "remove-project-state",
            title = "Delete Project State",
            href = PROJECTSTATE_PATH(projectName),
            method = HttpMethod.DELETE,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "stateName", type = "text"))
    )
}

