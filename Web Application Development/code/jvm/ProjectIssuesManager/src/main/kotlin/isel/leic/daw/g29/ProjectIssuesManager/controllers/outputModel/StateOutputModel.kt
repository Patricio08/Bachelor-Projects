package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.*

data class StateOutputModel(
        val stateName: String
) : IOutputModel<StateOutputModel> {
    override fun mapToSiren(): SirenEntity<StateOutputModel> = SirenEntity(
            properties = this,
            clazz = listOf("State")
    )

    fun mapToSirenEmbeddedEntity(): EmbeddedEntity<StateOutputModel> = EmbeddedEntity<StateOutputModel>(
            clazz = listOf("State"),
            rel = listOf("item"),
            properties = this
    )
}

