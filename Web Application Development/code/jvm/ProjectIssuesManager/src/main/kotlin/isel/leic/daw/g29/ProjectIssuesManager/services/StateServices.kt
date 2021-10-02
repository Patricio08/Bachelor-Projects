package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.StateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbState
import isel.leic.daw.g29.ProjectIssuesManager.database.DbState
import isel.leic.daw.g29.ProjectIssuesManager.models.State
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IStateServices
import org.springframework.stereotype.Service

@Service
class StateServices(private val DbState: IDbState) : IStateServices {
    override fun getStates(): List<State> = DbState.getStates()

    override fun postState(input: StateInputModel): String = DbState.postState(input)

    override fun deleteState(input: StateInputModel): String = DbState.deleteState(input)
}