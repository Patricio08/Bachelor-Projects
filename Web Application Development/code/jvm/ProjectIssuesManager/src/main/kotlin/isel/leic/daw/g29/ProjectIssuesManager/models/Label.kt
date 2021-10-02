package isel.leic.daw.g29.ProjectIssuesManager.models

import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.LabelOutputModel

data class Label (
    val labelName: String
) {
    fun mapToOutputModel () = LabelOutputModel(labelName)
}