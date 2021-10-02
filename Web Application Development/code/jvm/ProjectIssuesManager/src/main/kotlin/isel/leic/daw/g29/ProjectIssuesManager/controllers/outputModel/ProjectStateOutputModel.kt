package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*

data class ProjectStateOutputModel(
        val projectName: String,
        val stateName: String
) : IOutputModel<ProjectStateOutputModel> {
    override fun mapToSiren(): SirenEntity<ProjectStateOutputModel> = SirenEntity(
            properties = this,
            clazz = listOf("Project-State"),
    )

    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<ProjectStateOutputModel> = EmbeddedEntity<ProjectStateOutputModel>(
            clazz = listOf("Project-State"),
            rel = listOf("item"),
            properties = this,
    )
}

