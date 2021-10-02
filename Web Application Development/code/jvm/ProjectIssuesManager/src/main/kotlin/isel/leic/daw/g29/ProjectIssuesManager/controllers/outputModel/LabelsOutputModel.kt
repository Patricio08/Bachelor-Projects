package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.LABEL_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.IOutputModelList
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenAction
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.selfLink
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class LabelsOutputModel(
        val collectionSize: Int
) : IOutputModelList<LabelsOutputModel, LabelOutputModel> {
    override fun mapToSiren(list: List<LabelOutputModel>): SirenEntity<LabelsOutputModel> = SirenEntity<LabelsOutputModel>(
            clazz = listOf("Labels", "collection"),
            properties = this,
            entities = list.map(LabelOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_LABEL(), DELETE_LABEL()),
            links = listOf(selfLink(LABEL_PATH))
    )

    fun POST_LABEL(): SirenAction = SirenAction(
            name = "add-label",
            title = "Post Label ",
            href = URI(LABEL_PATH),
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "labelName", type = "text"))
    )

    fun DELETE_LABEL(): SirenAction = SirenAction(
            name = "remove-label",
            title = "Delete Label ",
            href = URI(LABEL_PATH),
            method = HttpMethod.DELETE,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "labelName", type = "text"))
    )

}