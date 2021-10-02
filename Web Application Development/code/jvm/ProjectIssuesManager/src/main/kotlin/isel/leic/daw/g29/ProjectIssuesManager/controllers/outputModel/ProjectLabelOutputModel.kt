package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.EmbeddedEntity
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.IOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity

data class ProjectLabelOutputModel(
        val projectName: String,
        val labelName: String
) : IOutputModel<ProjectLabelOutputModel> {
    override fun mapToSiren(): SirenEntity<ProjectLabelOutputModel> = SirenEntity(
            properties = this,
            clazz = listOf("Project-Label"),
    )

    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<ProjectLabelOutputModel> = EmbeddedEntity<ProjectLabelOutputModel>(
            clazz = listOf("Project-Label"),
            rel = listOf("item"),
            properties = this,
    )

}