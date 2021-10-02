package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.IOutputModelList
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenAction
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.selfLink
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType

data class ProjectLabelsOutputModel(
        val projectName: String,
        val collectionSize: Int
) : IOutputModelList<ProjectLabelsOutputModel, ProjectLabelOutputModel> {
    override fun mapToSiren(list: List<ProjectLabelOutputModel>): SirenEntity<ProjectLabelsOutputModel> = SirenEntity(
            clazz = listOf("Project-Label", "collection"),
            properties = this,
            entities = list.map(ProjectLabelOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_PROJECT_LABEL(), DELETE_PROJECT_LABEL()),
            links = listOf(selfLink(Paths.PROJECTLABEL_PATH(projectName)))
    )

    fun POST_PROJECT_LABEL(): SirenAction = SirenAction(
            name = "add-project-label",
            title = "Post Project Label",
            href = Paths.PROJECTLABEL_PATH(projectName),
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "labelName", type = "text"))
    )

    fun DELETE_PROJECT_LABEL(): SirenAction = SirenAction(
            name = "remove-project-Label",
            title = "Delete Project Label",
            href = Paths.PROJECTLABEL_PATH(projectName),
            method = HttpMethod.DELETE,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "labelName", type = "text"))
    )
}