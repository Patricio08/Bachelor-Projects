package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.StateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.State

interface IDbState {
    fun getStates(): List<State>
    fun postState(input: StateInputModel): String
    fun deleteState(input: StateInputModel): String
}