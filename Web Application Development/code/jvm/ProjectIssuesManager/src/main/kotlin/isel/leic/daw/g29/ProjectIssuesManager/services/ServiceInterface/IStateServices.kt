package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.StateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.State

interface IStateServices {
    fun getStates(): List<State>
    fun postState(input: StateInputModel): String
    fun deleteState(input: StateInputModel): String
}