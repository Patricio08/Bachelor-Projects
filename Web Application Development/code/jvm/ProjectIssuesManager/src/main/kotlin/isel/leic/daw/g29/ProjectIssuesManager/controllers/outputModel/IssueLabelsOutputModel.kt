package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.ISSUELAVEL_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.IOutputModelList
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenAction
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.selfLink
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType

data class IssueLabelsOutputModel(
        val projectName: String,
        val id: Int,
        val collectionSize: Int
) : IOutputModelList<IssueLabelsOutputModel, IssueLabelOutputModel> {

    override fun mapToSiren(list: List<IssueLabelOutputModel>): SirenEntity<IssueLabelsOutputModel> = SirenEntity(
            clazz = listOf("Issue Labels", "collection"),
            properties = this,
            entities = list.map(IssueLabelOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_ISSUELABEL(), DELETE_ISSUELABEL()),
            links = listOf(selfLink(ISSUELAVEL_PATH(projectName, id)))
    )

    fun POST_ISSUELABEL(): SirenAction = SirenAction(
            name = "add-issueLabel",
            title = "Post Issue Label ",
            href = ISSUELAVEL_PATH(projectName, id),
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "labelName", type = "text"))
    )

    fun DELETE_ISSUELABEL(): SirenAction = SirenAction(
            name = "remove-issueLabel",
            title = "Delete Issue Label ",
            href = ISSUELAVEL_PATH(projectName, id),
            method = HttpMethod.DELETE,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "labelName", type = "text"))
    )

}