package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths
import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.PROJECTS_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.PROJECT_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class ProjectsOutputModel(
        val collectionSize: Int
) : IOutputModelList<ProjectsOutputModel, ProjectOutputModel> {
    override fun mapToSiren(list: List<ProjectOutputModel>): SirenEntity<ProjectsOutputModel> = SirenEntity(
            clazz = listOf("Projects", "collection"),
            properties = this,
            entities = list.map(ProjectOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_PROJECTS, DELETE_PROJECT),
            links = listOf(selfLink(PROJECTS_PATH))
    )
}

val POST_PROJECTS = SirenAction(
        name = "add-project",
        title = "Post Project",
        method = HttpMethod.POST,
        href = URI(PROJECTS_PATH),
        type = MediaType.APPLICATION_JSON,
        fields = listOf(SirenAction.Field(name = "name", type = "text"),
                SirenAction.Field(name = "description", type = "text"), SirenAction.Field(name = "startState", type = "text"))
)

val DELETE_PROJECT = SirenAction(
        name = "remove-project",
        title = "Delete Project",
        href = URI(PROJECTS_PATH),
        method = HttpMethod.DELETE,
        type = MediaType.APPLICATION_JSON,
        fields = listOf(SirenAction.Field(name = "name", type = "text"))
)