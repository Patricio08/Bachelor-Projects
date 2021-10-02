package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.EmbeddedEntity
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.IOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity

data class LabelOutputModel(
        val labelName: String
) : IOutputModel<LabelOutputModel> {
    override fun mapToSiren(): SirenEntity<LabelOutputModel> = SirenEntity(
            properties = this,
            clazz = listOf("Label")
    )

    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<LabelOutputModel> = EmbeddedEntity<LabelOutputModel>(
            clazz = listOf("Label"),
            rel = listOf("item"),
            properties = this,
    )

}