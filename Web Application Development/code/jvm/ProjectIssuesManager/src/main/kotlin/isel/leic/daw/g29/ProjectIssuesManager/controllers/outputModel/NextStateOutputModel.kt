package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*

data class NextStateOutputModel(
        val projectName: String,
        val stateName: String,
        val nextState: String

) : IOutputModel<NextStateOutputModel> {
    override fun mapToSiren(): SirenEntity<NextStateOutputModel> = SirenEntity(
            properties = this,
            clazz = listOf("NextState"),
    )


    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<NextStateOutputModel> = EmbeddedEntity<NextStateOutputModel>(
            clazz = listOf("Project-Label"),
            rel = listOf("item"),
            properties = this,
    )
}

