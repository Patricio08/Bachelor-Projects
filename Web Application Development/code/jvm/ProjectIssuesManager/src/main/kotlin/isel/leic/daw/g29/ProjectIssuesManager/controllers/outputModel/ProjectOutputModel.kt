package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.ISSUES_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.Paths.PROJECT_PATH
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*


data class ProjectOutputModel(
        val name: String,
        val description: String,
        val startState: String
) : IOutputModel<ProjectOutputModel> {
    override fun mapToSiren(): SirenEntity<ProjectOutputModel> = SirenEntity(
            clazz = listOf("Project"),
            properties = this,
            links = listOf(selfLink(PROJECT_PATH(name)), GOTOISSUE())
    )

    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<ProjectOutputModel> = EmbeddedEntity<ProjectOutputModel>(
            clazz = listOf("Project"),
            rel = listOf("item"),
            properties = this,
            links = listOf(selfLink(PROJECT_PATH(name)))
    )

    fun GOTOISSUE() = SirenLink(
            href = ISSUES_PATH(name),
            rel = listOf("Issues")
    )

}





