package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*


data class IssueLabelOutputModel(
        val projectName: String,
        val id: Int,
        val labelName: String,
): IOutputModel<IssueLabelOutputModel> {
    override fun mapToSiren(): SirenEntity<IssueLabelOutputModel> = SirenEntity(
        properties = this,
        clazz = listOf("IssueLabel"),
    )

    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<IssueLabelOutputModel> = EmbeddedEntity<IssueLabelOutputModel>(
            clazz = listOf("IssueLabel"),
            rel = listOf("item"),
            properties = this,
    )
}
