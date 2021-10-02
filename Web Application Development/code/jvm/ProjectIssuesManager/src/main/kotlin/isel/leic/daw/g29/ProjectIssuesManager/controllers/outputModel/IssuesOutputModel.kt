package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.ISSUES_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType

data class IssuesOutputModel(
        val projectName: String,
        val collectionSize: Int
) : IOutputModelList<IssuesOutputModel, IssueOutputModel> {
    override fun mapToSiren(list: List<IssueOutputModel>): SirenEntity<IssuesOutputModel> = SirenEntity(
            clazz = listOf("Issues, Collection"),
            properties = this,
            entities = list.map(IssueOutputModel::mapToSirenEmbeddedEntity),
            actions = listOf(POST_ISSUE()),
            links = listOf(selfLink(ISSUES_PATH(projectName)))
    )

    fun POST_ISSUE() = SirenAction(
            name = "add-issue",
            title = "Post Issue",
            method = HttpMethod.POST,
            href = ISSUES_PATH(projectName),
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field(name = "name", type = "text"), SirenAction.Field(name = "description", type = "text"))
    )

}

